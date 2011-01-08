package org.onebusaway.rim;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.gps.GPSInfo;
import net.rim.device.api.gps.LocationInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.TouchGesture;
import net.rim.device.api.ui.Trackball;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ScreenMap extends MainScreen implements LocationListener
{
    // Alt-DEBG: Display Debug Dialog
    public static final int    BACKDOOR_DEBG             = ('D' << 24) | ('E' << 16) | ('B' << 8) | 'G';
    // Alt-DEBC: Clear the Debug Dialog
    public static final int    BACKDOOR_DEBC             = ('D' << 24) | ('E' << 16) | ('B' << 8) | 'C';
    // Alt-DEB0: Disable output to Debug Dialog; the default debug level
    public static final int    BACKDOOR_DEB0             = ('D' << 24) | ('E' << 16) | ('B' << 8) | '0';
    // Alt-DEB1: Enable output to Debug Dialog
    public static final int    BACKDOOR_DEB1             = ('D' << 24) | ('E' << 16) | ('B' << 8) | 'W';

    // ----- separator -----
    protected static final int MENU_ORDINAL_MY_LOCATION  = 0x10000;
    protected static final int MENU_ORDINAL_MY_REMINDERS = MENU_ORDINAL_MY_LOCATION + 1;
    protected static final int MENU_ORDINAL_MY_ROUTES    = MENU_ORDINAL_MY_REMINDERS + 1;
    protected static final int MENU_ORDINAL_MY_STOPS     = MENU_ORDINAL_MY_ROUTES + 1;
    // ----- separator -----
    protected static final int MENU_ORDINAL_ABOUT        = MENU_ORDINAL_MY_LOCATION + 0x10000;
    // TODO:(pv) Check For Updates, Settings, WiFi/Cellular?
    protected static final int MENU_ORDINAL_HELP         = MENU_ORDINAL_ABOUT + 1;
    // ----- separator -----
    protected static final int MENU_ORDINAL_MINIMIZE     = MENU_ORDINAL_ABOUT + 0x10000;
    // ----- separator -----
    protected static final int MENU_ORDINAL_EXIT         = MENU_ORDINAL_MINIMIZE + 0x10000;

    // Determines which item is selected over another...
    protected static final int MENU_PRIORITY_NONE        = Integer.MAX_VALUE;
    protected static final int MENU_PRIORITY_LOW         = 2;
    protected static final int MENU_PRIORITY_MEDIUM      = 1;
    protected static final int MENU_PRIORITY_HIGH        = 0;

    private Field              headerTODO;
    private ObaMapField        map;
    private LabelField         statusLine;

    private boolean            checkedTrackballSupport   = false;

    private final Object       lockGps                   = new Object();
    private LocationProvider   locationProvider          = null;
    private GeoLocation        geoLocation               = null;

    private final AppMain      app;

    ScreenMap()
    {
        super(Manager.NO_VERTICAL_SCROLL);

        app = AppMain.get();

        Coordinates defaultLocation = new Coordinates(47.6063889, -122.3308333, 100);

        setTitle(app.getResourceString(BBResource.TEXT_TITLE));

        map = new ObaMapField();
        map.moveTo(defaultLocation);
        map.setZoom(map.getMinZoom() + 1);
        add(map);

        statusLine = new LabelField("statusLine... foo", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);

        VerticalFieldManager statusVfm = new VerticalFieldManager();
        statusVfm.add(statusLine);
        setStatus(statusVfm);

        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                gpsStart(GPSInfo.GPS_MODE_AUTONOMOUS);
            }
        });
    }

    public void close()
    {
        //UiApplication.getUiApplication().requestClose();
        app.requestBackground();
    }

    /**
     * Overridden to always return true so as to prevent the Save/Discard/Cancel dialog popping up
     */
    protected boolean onSavePrompt()
    {
        return true;
    }

    protected void makeMenu(Menu menu, int instance)
    {
        MenuItem menuItemMyLocation =
            new MenuItem(app.getResourceString(BBResource.MENU_MY_LOCATION), MENU_ORDINAL_MY_LOCATION, MENU_PRIORITY_HIGH)
            {
                public void run()
                {
                    synchronized (lockGps)
                    {
                        if (geoLocation != null)
                        {
                            map.moveTo(geoLocation.getCoordinates());
                        }
                    }
                }
            };
        menu.add(menuItemMyLocation);
        
        /*
        MenuItem menuItemMyReminders =
            new MenuItem(app.getResourceString(BBResource.MENU_MY_REMINDERS), MENU_ORDINAL_MY_REMINDERS, MENU_PRIORITY_MEDIUM)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMyReminders);
        
        MenuItem menuItemMyRoutes =
            new MenuItem(app.getResourceString(BBResource.MENU_MY_ROUTES), MENU_ORDINAL_MY_ROUTES, MENU_PRIORITY_MEDIUM)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMyRoutes);
        
        MenuItem menuItemMyStops =
            new MenuItem(app.getResourceString(BBResource.MENU_MY_STOPS), MENU_ORDINAL_MY_STOPS, MENU_PRIORITY_MEDIUM)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMyStops);
        */

        MenuItem menuItemMinimize =
            new MenuItem(app.getResourceString(BBResource.MENU_MINIMIZE), MENU_ORDINAL_MINIMIZE, MENU_PRIORITY_NONE)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMinimize);

        MenuItem menuItemExit = new MenuItem(app.getResourceString(BBResource.MENU_EXIT), MENU_ORDINAL_EXIT, MENU_PRIORITY_NONE)
        {
            public void run()
            {
                app.exit();
            }
        };
        menu.add(menuItemExit);

        //super.makeMenu(menu, instance);
    }

    /**
     * Handles the backdoor ALT+XXXX key sequences for the hidden debugging functions.
     */
    protected boolean openProductionBackdoor(int backdoorCode)
    {
        switch (backdoorCode)
        {
            case BACKDOOR_DEBG:
                //app.showScreenDebug();
                return true;
            case BACKDOOR_DEBC:
                //app.debugDialog.clear();
                return true;
            case BACKDOOR_DEB0:
                //app.setIsGlobalLoggingEnabled(false);
                return true;
            case BACKDOOR_DEB1:
                //app.setIsGlobalLoggingEnabled(true);
                return true;
        }

        return super.openProductionBackdoor(backdoorCode);
    }

    /*
    protected boolean keyDown(int keycode, int time) 
    {
        StringBuffer sb = new StringBuffer();
        
        Keypad.getKeyChars(keycode, sb);        
        
        // Zoom in
        if(sb.toString().indexOf('i') != -1) 
        { 
            map.setZoom(Math.max(map.getZoom() - 1, map.getMinZoom()));
            return true;
        }
        // Zoom out
        else if(sb.toString().indexOf('o') != -1) 
        { 
            map.setZoom(Math.min(map.getZoom() + 1, map.getMaxZoom()));
            return true;
        }        

        return super.keyDown(keycode, time);
    }
    */

    protected boolean touchEvent(TouchEvent message)
    {
        boolean isConsumed = false;

        TouchGesture touchGesture = message.getGesture();
        if (touchGesture != null)
        {
            switch (touchGesture.getEvent())
            {
                case TouchGesture.SWIPE:
                    int magnitude = touchGesture.getSwipeMagnitude();

                    // The below code only handles N/S/E/W
                    // TODO:(pv) Find the vector components of the magnitude and move that amount.

                    switch (touchGesture.getSwipeDirection())
                    {
                        case TouchGesture.SWIPE_NORTH:
                            map.move(0, -magnitude);
                            break;
                        case TouchGesture.SWIPE_SOUTH:
                            map.move(0, magnitude);
                            break;
                        case TouchGesture.SWIPE_EAST:
                            map.move(-magnitude, 0);
                            break;
                        case TouchGesture.SWIPE_WEST:
                            map.move(magnitude, 0);
                            break;
                    }
                    isConsumed = true;
                    break;
                case TouchGesture.DOUBLE_TAP:
                    // TODO:(pv) SHIFT should zoom out
                    map.zoomIn();
            }
        }
        return isConsumed;
    }

    protected boolean navigationMovement(int dx, int dy, int status, int time)
    {
        if (!checkedTrackballSupport)
        {
            checkedTrackballSupport = true;

            // Allows smoother panning on the map.
            if (Trackball.isSupported())
            {
                // Adjust the filter.
                getScreen().setTrackballFilter(Trackball.FILTER_NO_TIME_WINDOW | Trackball.FILTER_ACCELERATION);
            }
        }

        return map.navigationMovement(dx, dy, status, time);
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        /*
        // Only show the debug dialog indicator on screens that are actively showing the banner and statusbar
        if (showDebugDialogIndicator == Boolean.TRUE && banner.isVisible() && statusBar.isVisible())
        {
            g.setColor(Color.BLACK);
            int width = g.getFont().getAdvance(DEBUG_INDICATOR_TEXT);
            g.drawText(DEBUG_INDICATOR_TEXT, //
                            Display.getWidth() - width - DEBUG_INDICATOR_PADDING, //
                            banner.getHeight() + DEBUG_INDICATOR_PADDING);
        }
        */
    }

    /**
     * Starts GPS tracking using the given mode.
     *  
     * @param gpsMode example GPSInfo.GPS_MODE_AUTONOMOUS
     */
    protected void gpsStart(int gpsMode)
    {
        synchronized (lockGps)
        {
            if (locationProvider != null)
            {
                locationProvider.reset();
                locationProvider.setLocationListener(null, -1, -1, -1);
            }

            geoLocation = null;

            int locationCapability = LocationInfo.getSupportedLocationSources();

            boolean isGpsModeSupported = (locationCapability & gpsMode) != 0;
            if (!isGpsModeSupported)
            {
                // TODO:(pv) Dialog.setDontAskAgainPrompt(...);
                Dialog.alert(app.getResourceString(BBResource.ERROR_GPS_NOT_SUPPORTED));
                return;
            }

            //BlackBerryCriteria foo;

            Criteria myCriteria = new Criteria();
            myCriteria.setPreferredResponseTime(Criteria.NO_REQUIREMENT);

            if (gpsMode == GPSInfo.GPS_MODE_AUTONOMOUS)
            {
                myCriteria.setCostAllowed(false);
                myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH);
            }
            else
            {
                myCriteria.setCostAllowed(true);

                if (gpsMode == GPSInfo.GPS_MODE_ASSIST)
                {
                    myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
                }
                else
                {
                    myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
                }
            }

            try
            {
                locationProvider = LocationProvider.getInstance(myCriteria);
                if (locationProvider != null)
                {
                    locationProvider.setLocationListener(this, -1, -1, -1);
                }
            }
            catch (Exception err)
            {
            }
        }
    }

    public void locationUpdated(LocationProvider provider, Location location)
    {
        synchronized (lockGps)
        {
            geoLocation = new GeoLocation(location);

            app.log("geoLocation=" + geoLocation.toString());
            
            //map.setLocation(geoLocation.getCoordinates());
            map.moveTo(geoLocation.getCoordinates());
        }
    }

    public void providerStateChanged(LocationProvider provider, int newState)
    {
        // TODO:(pv) ...
    }

    public GeoLocation getLocation()
    {
        synchronized (lockGps)
        {
            return geoLocation;
        }
    }
}
