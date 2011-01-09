package org.onebusaway.rim;

import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.CoverageStatusListener;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.RadioStatusListener;
import net.rim.device.api.system.WLANConnectionListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

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

    private ResourceBundle resourceStrings = ResourceBundle.getBundle(BBResource.BUNDLE_ID, BBResource.BUNDLE_NAME);

    private static AppMain instance        = null;

    public static AppMain get()
    {
        return instance;
    }

    public static final int RADIO_UNSUPPORTED = -3;
    public static final int RADIO_UNAVAILABLE = -2;
    public static final int RADIO_OFF         = -1;
    public static final int RADIO_NO_SIGNAL   = 0;
    public static final int RADIO_ONE_BAR     = 1;
    public static final int RADIO_TWO_BARS    = 2;
    public static final int RADIO_THREE_BARS  = 3;
    public static final int RADIO_FOUR_BARS   = 4;
    public static final int RADIO_FIVE_BARS   = 5;

    private AppMain()
    {
        instance = this;
        pushScreen(new ScreenMap());
    }

    public String getResourceString(int id)
    {
        return resourceStrings.getString(id);
    }

    public Bitmap getResourceBitmap(String name)
    {
        return Bitmap.getBitmapResource(name);
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
        super.activate();

        // TODO:(pv) Refresh current screen, especially ScreenMyStopList
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

    private boolean  isRadioEnabled;
    private int      cellBars                 = RADIO_OFF;
    private int      wiFiBars                 = RADIO_OFF;
    private boolean  updateRadioStatePending  = false;
    private Runnable updateRadioStateRunnable = new Runnable()
                                              {
                                                  public void run()
                                                  {
                                                      updateRadioStateNow(true);
                                                  }
                                              };

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
        //logger.debug("updateRadioStateNow()");

        try
        {
            updateRadioStatePending = false;

            int oldCellBars = cellBars;
            int oldWiFiBars = wiFiBars;
            // boolean oldIsRadioEnabled = isRadioEnabled;
            //logger.debug("oldIsRadioEnabled=" + isRadioEnabled);

            cellBars = updateBars(false);
            //logger.debug("cellBars=" + cellBars);
            wiFiBars = updateBars(true);
            //logger.debug("wifiBars=" + wiFiBars);

            isRadioEnabled = getNetworkBars() > 0;
            //logger.debug("isRadioEnabled=" + isRadioEnabled);

            /*
            if (isRadioEnabled)
            {
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
            }
            else
            {
                // radio is no longer enabled, so stop client if it is running
                stopClientAndIgnoreRadio();
                startClientWhenRadioEnabled = true;
            }

            if (alwaysUpdateIndicators || oldCellBars != cellBars || oldWiFiBars != wiFiBars)
            {
                // Notify all interested screens
                notifyWtcScreenListeners(WtcScreenListenerEventId.BANNER);
            }
            */
        }
        catch (Exception e)
        {
            //logger.error("EXCEPTION: updateRadioStateNow()", e);
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
            //logger.error("EXCEPTION: updateBars", e);
        }
        return RADIO_FIVE_BARS;
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
        return isWiFiSupported();// && settings.getUseWiFi();
    }
}
