package org.onebusaway.rim;

import java.util.Enumeration;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.applicationcontrol.ReasonProvider;
import net.rim.device.api.gps.GPSInfo;
import net.rim.device.api.gps.LocationInfo;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.CoverageStatusListener;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioStatusListener;
import net.rim.device.api.system.WLANConnectionListener;
import net.rim.device.api.system.WLANInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

public class AppMain extends UiApplication implements //
                CoverageStatusListener, RadioStatusListener, WLANConnectionListener
{
    public static void main(String[] args)
    {
        AppMain app = new AppMain();
        app.enterEventDispatcher();
    }

    public static void errorDialog(final String message)
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                Dialog.alert(message);
            }
        });
    }

    private static AppMain instance = null;

    public static AppMain get()
    {
        return instance;
    }

    public static final int                   RADIO_UNSUPPORTED        = -3;
    public static final int                   RADIO_UNAVAILABLE        = -2;
    public static final int                   RADIO_OFF                = -1;
    public static final int                   RADIO_NO_SIGNAL          = 0;
    public static final int                   RADIO_ONE_BAR            = 1;
    public static final int                   RADIO_TWO_BARS           = 2;
    public static final int                   RADIO_THREE_BARS         = 3;
    public static final int                   RADIO_FOUR_BARS          = 4;
    public static final int                   RADIO_FIVE_BARS          = 5;

    private ResourceBundle                    resourceStrings          =
                                                                           ResourceBundle.getBundle(BBResource.BUNDLE_ID,
                                                                                           BBResource.BUNDLE_NAME);

    private boolean                           applicationPermissionsOk = false;
    private final ApplicationPermissionsTable applicationPermissionsTable;

    private boolean                           isRadioEnabled;
    private int                               cellBars                 = RADIO_OFF;
    private int                               wiFiBars                 = RADIO_OFF;
    private boolean                           updateRadioStatePending  = false;
    private Runnable                          updateRadioStateRunnable = new Runnable()
                                                                       {
                                                                           public void run()
                                                                           {
                                                                               updateRadioStateNow(true);
                                                                           }
                                                                       };

    private AppMain()
    {
        instance = this;

        // Populated in initializeApplicationPermissionsReasons()
        applicationPermissionsTable = new ApplicationPermissionsTable();

        // Call initialize() as soon as enterEventDispatcher is called 
        invokeLater(new Runnable()
        {
            public void run()
            {
                initialize();
            }
        });
    }

    public ResourceBundle getResourceBundleStrings()
    {
        return resourceStrings;
    }

    public String getResourceString(int id)
    {
        return resourceStrings.getString(id);
    }

    public Bitmap getResourceBitmap(String name)
    {
        return Bitmap.getBitmapResource(name);
    }

    public void log(String msg, Throwable tr)
    {
        if (tr != null)
        {
            msg += ": " + tr;
        }
        log(msg);
    }

    public void log(String msg)
    {
        System.out.println(msg);
    }

    public void exit()
    {
        exit(0);
    }

    public void exit(int exitCode)
    {
        //UiApplication.getUiApplication().requestClose();
        System.exit(exitCode);
    }

    public void activate()
    {
        log("+activate()");
        super.activate();

        // TODO:(pv) Refresh current screen, especially ScreenMyStopList

        log("-activate()");
    }

    /**
     * Add callback to list user friendly reasons why permissions are needed.
     */
    protected void applicationPermissionsInitialize()
    {
        if (applicationPermissionsTable.size() > 0)
        {
            return;
        }

        // TODO:(pv) Localize BBResource
        final String reasonOther = "Required in order to run properly.";
        //applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_BLUETOOTH, //
        //                "Required to access bluetooth devices and profiles."));
        //applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS, //
        //                "TODO:(pv) A workaround for enabling the backlight?"));
        //applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION, //
        //                "TODO:(pv) A workaround for enabling the backlight?"));
        //applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_APPLICATION_MANAGEMENT, //
        //                "TODO:(pv) Could be used to manage the installed app modules."));
        //applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK, //
        //                "Required for corporate MDS connections."));
        if (isGpsSupported())
        {
            applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA, false, //
                            "Required for access to the GPS."));
        }
        applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_INTERNET, true, //
                        "Required to access the internet."));
        if (isWiFiSupported())
        {
            applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_WIFI, true, //
                            "Required to access Wi-Fi networks."));
        }
        applicationPermissionsTable.put(new ApplicationPermission(ApplicationPermissions.PERMISSION_IDLE_TIMER, false, //
                        "Optional: Required to enable the backlight when showing reminders."));

        // Add the actual callback; this should only ever be called *ONCE*!
        ApplicationPermissionsManager.getInstance().addReasonProvider(ApplicationDescriptor.currentApplicationDescriptor(),
                        new ReasonProvider()
                        {
                            public String getMessage(int permissionID)
                            {
                                ApplicationPermission applicationPermission =
                                    (ApplicationPermission) applicationPermissionsTable.get(permissionID);
                                if (applicationPermission == null)
                                {
                                    return reasonOther;
                                }
                                return applicationPermission.getReason();
                            }
                        });
    }

    protected boolean applicationPermissionsCheck()
    {
        try
        {
            log("+applicationPermissionsCheck()");

            applicationPermissionsInitialize();

            boolean requestPermissions = false;

            boolean settingsPermissionsOk = true;//settingsDB.getPermissionsOk();
            log("settingsPermissionsOk=" + settingsPermissionsOk);

            int permissionId;
            int permissionValue;
            ApplicationPermission permission;
            boolean permissionRequired;

            ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
            ApplicationPermissions permissionsCurrent = apm.getApplicationPermissions();
            ApplicationPermissions permissionsRequest = new ApplicationPermissions();

            Enumeration enumValues = applicationPermissionsTable.elements();
            while (enumValues.hasMoreElements())
            {
                permission = ((ApplicationPermission) enumValues.nextElement());
                permissionId = permission.getId();

                permissionValue = permissionsCurrent.getPermission(permissionId);
                if (permissionValue != ApplicationPermissions.VALUE_ALLOW)
                {
                    permissionRequired = permission.getRequired();
                    log("permissionId=" + permissionId + ", permissionRequired=" + permissionRequired);
                    if (!settingsPermissionsOk || permissionRequired)
                    {
                        permissionsRequest.addPermission(permissionId);
                        requestPermissions = true;
                    }
                }
                else
                {
                    log("permissionId=" + permissionId + " already Allowed");
                }
            }

            if (requestPermissions)
            {
                if (!apm.invokePermissionsRequest(permissionsRequest))
                {
                    // One of the requested permissions failed.
                    // Get the [new] current permissions and check if we allow the permission to be ignored.

                    permissionsCurrent = apm.getApplicationPermissions();

                    enumValues = applicationPermissionsTable.elements();
                    while (enumValues.hasMoreElements())
                    {
                        permission = ((ApplicationPermission) enumValues.nextElement());
                        permissionId = permission.getId();

                        permissionValue = permissionsCurrent.getPermission(permissionId);
                        if (permissionValue != ApplicationPermissions.VALUE_ALLOW)
                        {
                            if (permission.getRequired())
                            {
                                // Required; return false so that app can handle (optionally show dialog and/or exit)
                                return false;
                            }
                        }
                    }
                }
            }

            return true;
        }
        finally
        {
            log("-applicationPermissionsCheck()");
        }
    }

    public void initialize()
    {
        applicationPermissionsOk = applicationPermissionsCheck();

        if (applicationPermissionsOk)
        {
            onApplicationPermissionsOk();
            return;
        }

        invokeLater(new Runnable()
        {
            public void run()
            {
                applicationPermissionsOk = false;

                // TODO:(pv) Localize BBResource
                String permissionsFailed = "Failed to set required permissions.\nRetry setting permissions?";
                if (Dialog.ask(Dialog.D_YES_NO, permissionsFailed, Dialog.YES) == Dialog.YES)
                {
                    applicationPermissionsOk = applicationPermissionsCheck();
                }

                if (applicationPermissionsOk)
                {
                    onApplicationPermissionsOk();
                    return;
                }

                // TODO:(pv) Localize BBResource
                permissionsFailed = "Failed to set required permissions.\nExiting the application.";
                Dialog.alert(permissionsFailed);
                exit(1);
            }
        });
    }

    public void uninitialize(int exitCode)
    {
        synchronized (AppMain.getEventLock())
        {
            if (applicationPermissionsOk)
            {
                CoverageInfo.removeListener(this);
                WLANInfo.removeListener(this);
                removeRadioListener(this);
                //removeAlertListener(this);
                //removeGlobalEventListener(this);
            }

            exit(exitCode);
        }
    }

    protected void onApplicationPermissionsOk()
    {
        //settingsDB.setPermissionsOk(true);
        //settingsDB.saveRecord();

        //Phone.addPhoneListener(this);
        //addGlobalEventListener(this);
        //addAlertListener(this);
        addRadioListener(this);
        WLANInfo.addListener(this);
        CoverageInfo.addListener(this);

        pushScreen(new ScreenMap());

    }

    protected void pushScreen(ObaMainScreen obaMainScreen)
    {
        pushScreen((MainScreen) obaMainScreen);
        ObaMainScreen.addObaScreenListener(obaMainScreen);
        updateRadioState();
    }

    protected void popScreen(ObaMainScreen obaMainScreen)
    {
        ObaMainScreen.removeObaScreenListener(obaMainScreen);
        popScreen((MainScreen) obaMainScreen);
    }

    public void coverageStatusChanged(int arg0)
    {
        updateRadioState();
    }

    public void baseStationChange()
    {
        updateRadioState();
    }

    public void networkScanComplete(boolean success)
    {
        updateRadioState();
    }

    public void networkServiceChange(int networkId, int service)
    {
        updateRadioState();
    }

    public void networkStarted(int networkId, int service)
    {
        updateRadioState();
    }

    public void networkStateChange(int state)
    {
        updateRadioState();
    }

    public void pdpStateChange(int apn, int state, int cause)
    {
        updateRadioState();
    }

    public void radioTurnedOff()
    {
        updateRadioState();
    }

    public void signalLevel(int level)
    {
        updateRadioState();
    }

    public void networkConnected()
    {
        updateRadioState();
    }

    public void networkDisconnected(int reason)
    {
        updateRadioState();
    }

    private void updateRadioState()
    {
        // consolidate duplicate events into a single update
        if (!updateRadioStatePending)
        {
            updateRadioStatePending = true;
        }
        invokeLater(updateRadioStateRunnable);
    }

    private void updateRadioStateNow(boolean alwaysUpdateIndicators)
    {
        try
        {
            log("+updateRadioStateNow()");

            updateRadioStatePending = false;

            int oldCellBars = cellBars;
            int oldWiFiBars = wiFiBars;
            boolean oldIsRadioEnabled = isRadioEnabled;
            log("oldIsRadioEnabled=" + isRadioEnabled);

            cellBars = updateBars(false);
            log("cellBars=" + cellBars);
            wiFiBars = updateBars(true);
            log("wifiBars=" + wiFiBars);

            isRadioEnabled = getNetworkBars() > 0;
            log("isRadioEnabled=" + isRadioEnabled);

            if (isRadioEnabled)
            {
                /*
                if (startClientWhenRadioEnabled)
                {
                    //logger.info("startclientWhenRadioEnabled=true");

                    if (getUseWiFi() && (getWiFiBars() > 0))
                    {
                        startClient();
                    }
                    else if (!getUseWiFi() && (getCellBars() > 0))
                    {
                        startClient();
                    }
                }
                // else do nothing for now
                */
            }
            else
            {
                /*
                // radio is no longer enabled, so stop client if it is running
                stopClientAndIgnoreRadio();
                startClientWhenRadioEnabled = true;
                */
            }

            if (alwaysUpdateIndicators || oldCellBars != cellBars || oldWiFiBars != wiFiBars)
            {
                // Notify all interested screens
                ObaMainScreen.notifyObaScreenListeners(ObaMainScreen.ObaScreenListenerEventId.BANNER);
            }
        }
        catch (Exception e)
        {
            log("EXCEPTION: updateRadioStateNow()", e);
        }
        finally
        {
            log("-updateRadioStateNow()");
        }
    }

    public static int getWAF(boolean wifi)
    {
        if (wifi)
        {
            return RadioInfo.WAF_WLAN;
        }

        switch (RadioInfo.getNetworkType())
        {
            case RadioInfo.NETWORK_802_11:
                return RadioInfo.WAF_WLAN;

            case RadioInfo.NETWORK_CDMA:
                return RadioInfo.WAF_CDMA;

            case RadioInfo.NETWORK_GPRS:
            case RadioInfo.NETWORK_UMTS:
                return RadioInfo.WAF_3GPP;

            case RadioInfo.NETWORK_IDEN:
                return RadioInfo.WAF_IDEN;

            case RadioInfo.NETWORK_NONE:
            default:
                return -1;
        }
    }

    protected int updateBars(boolean wifi)
    {
        try
        {
            log("+updateBars(" + wifi + ")");

            try
            {
                int radioState = RadioInfo.getState();

                if ((!wifi) && (radioState != RadioInfo.STATE_ON))
                {
                    return RADIO_OFF;
                }
                else if (wifi && ((RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN) != RadioInfo.WAF_WLAN))
                {
                    return RADIO_OFF;
                }

                int waf = getWAF(wifi);
                if (waf < 0)
                {
                    return RADIO_UNSUPPORTED;
                }

                if ((RadioInfo.getSupportedWAFs() & waf) == 0)
                {
                    // unknown or unsupported WAF
                    return RADIO_UNSUPPORTED;
                }
                if ((RadioInfo.getActiveWAFs() & waf) == 0)
                {
                    // inactive WAF
                    return RADIO_OFF;
                }

                if (wifi)
                {
                    if ((!RadioInfo.isDataServiceOperational() || RadioInfo.isDataServiceSuspended())
                                    && (CoverageInfo.getCoverageStatus(waf, false) == CoverageInfo.COVERAGE_NONE))
                    {
                        // no data
                        return RADIO_UNAVAILABLE;
                    }
                }
                else
                {
                    if (!RadioInfo.isDataServiceOperational() || RadioInfo.isDataServiceSuspended())
                    {
                        // no data
                        return RADIO_UNAVAILABLE;
                    }
                }

                int signalLevel = RadioInfo.getSignalLevel(waf);
                if ((!wifi) && (signalLevel == RadioInfo.LEVEL_NO_COVERAGE))
                {
                    return RADIO_NO_SIGNAL;
                }

                if (CoverageInfo.getCoverageStatus(waf, false) == CoverageInfo.COVERAGE_NONE)
                {
                    // otherwise unavailable, not really sure what else could cause this
                    return RADIO_UNAVAILABLE;
                }

                if (wifi)
                {
                    // TODO calibrate
                    if (signalLevel <= -87)
                    {
                        return RADIO_ONE_BAR;
                    }
                    if (signalLevel <= -60)
                    {
                        return RADIO_TWO_BARS;
                    }
                    // wifi maxes out as 3 bars
                    return RADIO_THREE_BARS;
                }

                // cellular network

                int networkService = RadioInfo.getNetworkService(waf);

                if ((networkService & RadioInfo.NETWORK_SERVICE_EMERGENCY_ONLY) == RadioInfo.NETWORK_SERVICE_EMERGENCY_ONLY)
                {
                    // emergency only (probably wouldn't get this far, but just in case)
                    return RADIO_UNAVAILABLE;
                }

                if ((networkService & RadioInfo.NETWORK_SERVICE_DATA) != RadioInfo.NETWORK_SERVICE_DATA)
                {
                    // no data on this WAF
                    return RADIO_UNAVAILABLE;
                }

                if (signalLevel <= -102)
                {
                    return RADIO_ONE_BAR;
                }
                if (signalLevel <= -93)
                {
                    return RADIO_TWO_BARS;
                }
                if (signalLevel <= -87)
                {
                    return RADIO_THREE_BARS;
                }
                if (signalLevel <= -78)
                {
                    return RADIO_FOUR_BARS;
                }
            }
            catch (Exception e)
            {
                log("EXCEPTION: updateBars", e);
            }

            return RADIO_FIVE_BARS;
        }
        finally
        {
            log("-updateBars");
        }
    }

    public int getNetworkBars()
    {
        return getUseWiFi() ? wiFiBars : cellBars;
    }

    public int getCellBars()
    {
        return cellBars;
    }

    public int getWiFiBars()
    {
        return wiFiBars;
    }

    public boolean isWiFiSupported()
    {
        return (RadioInfo.getSupportedWAFs() & RadioInfo.WAF_WLAN) != 0;
    }

    public boolean isCellularSupported()
    {
        // TODO:(pv) ...
        return true;//(RadioInfo.getSupportedWAFs() & RadioInfo..WAF_WLAN) != 0;
    }

    public boolean getUseWiFi()
    {
        return false;//isWiFiSupported();// && settings.getUseWiFi();
    }

    /*
    public boolean getUseCellular()
    {
        return isCellularSupported();// && settings.getUseCellular();
    }
    */

    public boolean isGpsSupported()
    {
        int locationCapability = LocationInfo.getSupportedLocationSources();

        return (locationCapability & GPSInfo.GPS_DEVICE_INTERNAL) != 0 //
                        || (locationCapability & GPSInfo.GPS_DEVICE_BLUETOOTH) != 0 //
                        || (locationCapability & LocationInfo.LOCATION_SOURCE_GEOLOCATION_CELL) != 0 //
                        || (locationCapability & LocationInfo.LOCATION_SOURCE_GEOLOCATION_WLAN) != 0;
    }
}
