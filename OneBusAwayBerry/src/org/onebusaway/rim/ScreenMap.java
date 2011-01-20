package org.onebusaway.rim;

import java.util.Random;
import java.util.Vector;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.gps.GPSInfo;
import net.rim.device.api.gps.LocationInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.input.InputSettings;
import net.rim.device.api.ui.input.NavigationDeviceSettings;
import net.rim.device.api.ui.input.TouchscreenSettings;

import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.request.ObaStopsForLocationRequest;
import org.onebusaway.berry.api.request.ObaStopsForLocationResponse;
import org.onebusaway.berry.map.GeoPoint;
import org.onebusaway.berry.map.ObaMapAction;
import org.onebusaway.berry.map.ObaMapField;
import org.onebusaway.berry.map.ObaMapMarker;
import org.onebusaway.berry.map.ObaMapMarkerLocation;
import org.onebusaway.berry.map.ObaMapMarkerStop;
import org.onebusaway.rim.banner.BannerManager;
import org.onebusaway.rim.gps.GeoLocation;

public class ScreenMap extends ObaMainScreen implements LocationListener, FieldChangeListener
{
    // ----- separator -----
    protected static final int  MENU_ORDINAL_MY_LOCATION  = 0x10000;
    protected static final int  MENU_ORDINAL_MY_REMINDERS = MENU_ORDINAL_MY_LOCATION + 1;
    protected static final int  MENU_ORDINAL_MY_ROUTES    = MENU_ORDINAL_MY_REMINDERS + 1;
    protected static final int  MENU_ORDINAL_MY_STOPS     = MENU_ORDINAL_MY_ROUTES + 1;
    // ----- separator -----
    protected static final int  MENU_ORDINAL_ABOUT        = MENU_ORDINAL_MY_LOCATION + 0x10000;
    // TODO:(pv) Check For Updates, Settings, WiFi/Cellular?
    protected static final int  MENU_ORDINAL_HELP         = MENU_ORDINAL_ABOUT + 1;
    // ----- separator -----
    protected static final int  MENU_ORDINAL_MINIMIZE     = MENU_ORDINAL_ABOUT + 0x10000;
    // ----- separator -----
    protected static final int  MENU_ORDINAL_EXIT         = MENU_ORDINAL_MINIMIZE + 0x10000;

    // Determines which item is selected over another...
    protected static final int  MENU_PRIORITY_NONE        = Integer.MAX_VALUE;
    protected static final int  MENU_PRIORITY_LOW         = 2;
    protected static final int  MENU_PRIORITY_MEDIUM      = 1;
    protected static final int  MENU_PRIORITY_HIGH        = 0;

    public static final String  HELP_URL                  = "http://www.joulespersecond.com/onebusaway-userguide2/";
    public static final String  TWITTER_URL               = "http://mobile.twitter.com/onebusawayberry";

    private static final String FOCUS_STOP_ID             = ".FocusStopId";
    private static final String CENTER_LAT                = ".CenterLat";
    private static final String CENTER_LON                = ".CenterLon";
    private static final String MAP_ZOOM                  = ".MapZoom";
    // Switches to 'route mode' -- stops aren't updated on move
    private static final String ROUTE_ID                  = ".RouteId";
    private static final String SHOW_ROUTES               = ".ShowRoutes";

    private BannerManager       banner;
    private Field               headerTODO;
    private ObaMapField         map;
    private LabelField          statusLine;

    private boolean             checkedTrackballSupport   = false;

    private final Object        lockGps                   = new Object();
    private LocationProvider    locationProvider          = null;
    private GeoLocation         geoLocation               = null;

    ScreenMap()
    {
        banner = new BannerManager(app.getResourceString(BBResource.TEXT_TITLE));
        setBanner(banner);

        headerTODO = null;

        map = new ObaMapField();
        map.setChangeListener(this);
        add(map);

        statusLine = new LabelField("statusLine...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);

        VerticalFieldManager statusVfm = new VerticalFieldManager();
        statusVfm.add(statusLine);
        setStatus(statusVfm);

        enablePinchAndSwipe();

        // TODO:(pv) Restore this from persistent storage
        map.moveTo(ObaMapField.GEOPOINT_SEATTLE);
        map.setZoom(2);

        //startRequestStopsAsync();
    }

    public void onUpdateBanner()
    {
        banner.update();
    }

    /**
     * Enables touch pinch and trackpad swipe gestures:
     * http://docs.blackberry.com/en/developers/deliverables/21157/Enable_pinch_1222743_11.jsp
     * http://docs.blackberry.com/en/developers/deliverables/21157/Enable_swipe_trackpad_1226852_11.jsp 
     */
    protected void enablePinchAndSwipe()
    {
        InputSettings ts = TouchscreenSettings.createEmptySet();
        ts.set(TouchscreenSettings.DETECT_PINCH, 1);
        addInputSettings(ts);
        InputSettings nd = NavigationDeviceSettings.createEmptySet();
        nd.set(NavigationDeviceSettings.DETECT_SWIPE, 1);
        addInputSettings(nd);
    }

    public void fieldChanged(Field field, int actionId)
    {
        if (field == map)
        {
            switch (actionId)
            {
                case ObaMapAction.ACTION_CENTRE_CHANGE:
                case ObaMapAction.ACTION_ZOOM_CHANGE:
                    startRequestStopsAsync();
                    break;
            }
        }
    }

    public void startRequestStopsAsync()
    {
        // TODO:(pv) Request new stops only if zoom *out* or move; if zoom in we already have all of the stops in the view. 
        // TODO:(pv) Cache these to internal storage.
        // TODO:(pv) Talk to oba.org about use of "If-Modified-Since" header to cache stops that haven't changed. 

        final GeoPoint pt = map.getGeoPoint();

        // TODO:(pv) Optimize this to not create a new thread every time; queue the request to an existing worker thread.
        new Thread(new Runnable()
        {
            public void run()
            {
                ObaStopsForLocationRequest.Builder builder = new ObaStopsForLocationRequest.Builder(getContext(), pt);
                ObaStopsForLocationRequest request = builder.build();
                ObaStopsForLocationResponse response = (ObaStopsForLocationResponse) request.call();

                ObaStop[] stops = response.getStops();

                final ObaMapMarker[] mapMarkers = new ObaMapMarker[stops.length];

                String id;
                GeoPoint coordinates;
                String name;
                int type;
                String direction;
                boolean favorite;

                ObaStop stop;
                for (int i = 0; i < mapMarkers.length; i++)
                {
                    stop = stops[i];

                    id = stop.getId();
                    coordinates = stop.getLocation();
                    name = stop.getName();
                    type = stop.getLocationType();
                    direction = stop.getDirection();
                    favorite = false;// TODO:(pv) Look up in persisted storage by id

                    mapMarkers[i] = new ObaMapMarkerStop(id, coordinates, name, type, direction, favorite);
                }

                app.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        map.mapMarkersAdd(mapMarkers, true);
                        // TODO:(pv) Get mapMarkersAdd to properly invalidate
                        map.invalidate();
                    }
                });
            }
        }).start();
    }

    protected void onUiEngineAttached(boolean attached)
    {
        logger.info("+onUiEngineAttached(" + attached + ")");
        super.onUiEngineAttached(attached);
        if (attached)
        {
            gpsStart(GPSInfo.GPS_MODE_AUTONOMOUS);
        }
        else
        {
            // TODO:(pv) gpsStop?
        }
        logger.info("-onUiEngineAttached(" + attached + ")");
    }

    public void onFocusNotify(boolean focus)
    {
        logger.info("+onFocusNotify(" + focus + ")");
        super.onFocusNotify(focus);
        logger.info("-onFocusNotify(" + focus + ")");
    }

    public void onExposed()
    {
        logger.info("+onExposed()");
        super.onExposed();
        logger.info("-onExposed()");
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
            new MenuItem(resourceStrings, BBResource.MENU_MY_LOCATION, MENU_ORDINAL_MY_LOCATION, MENU_PRIORITY_HIGH)
            {
                public void run()
                {
                    synchronized (lockGps)
                    {
                        if (geoLocation != null)
                        {
                            //map.getAction().setCentre(geoLocation.getMapPoint());
                        }
                    }
                }
            };
        menu.add(menuItemMyLocation);

        /*
        MenuItem menuItemMyReminders =
            new MenuItem(resourceStrings, BBResource.MENU_MY_REMINDERS, MENU_ORDINAL_MY_REMINDERS, MENU_PRIORITY_MEDIUM)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMyReminders);
        
        MenuItem menuItemMyRoutes =
            new MenuItem(resourceStrings, BBResource.MENU_MY_ROUTES, MENU_ORDINAL_MY_ROUTES, MENU_PRIORITY_MEDIUM)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMyRoutes);
        
        MenuItem menuItemMyStops =
            new MenuItem(resourceStrings, BBResource.MENU_MY_STOPS, MENU_ORDINAL_MY_STOPS, MENU_PRIORITY_MEDIUM)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMyStops);
        */

        MenuItem menuItemMinimize =
            new MenuItem(resourceStrings, BBResource.MENU_MINIMIZE, MENU_ORDINAL_MINIMIZE, MENU_PRIORITY_NONE)
            {
                public void run()
                {
                    close();
                }
            };
        menu.add(menuItemMinimize);

        MenuItem menuItemExit = new MenuItem(resourceStrings, BBResource.MENU_EXIT, MENU_ORDINAL_EXIT, MENU_PRIORITY_NONE)
        {
            public void run()
            {
                app.exit();
            }
        };
        menu.add(menuItemExit);
    }

    protected boolean keyDown(int keycode, int time)
    {
        switch (Keypad.key(keycode))
        {
            case Keypad.KEY_VOLUME_UP:
                // Zoom in
                map.zoomIn();//.setZoom(Math.max(map.getZoom() - 1, map.getMinZoom()));
                return true;

            case Keypad.KEY_VOLUME_DOWN:
                // Zoom out
                map.zoomOut();//.setZoom(Math.min(map.getZoom() + 1, map.getMaxZoom()));
                return true;
        }
        return super.keyDown(keycode, time);
    }

    /*
    protected boolean touchEvent(TouchEvent message)
    {
        /*
        switch (message.getEvent())
        {
            case TouchEvent.GESTURE:
                TouchGesture gesture = message.getGesture();
                switch (gesture.getEvent())
                {
                    case TouchGesture.PINCH_UPDATE:
                        int tx1 = message.getX(1);
                        int ty1 = message.getY(1);
                        int tx2 = message.getX(2);
                        int ty2 = message.getY(2);
                        int tx = (tx1 + tx2) / 2;
                        int ty = (ty1 + ty2) / 2;
                        int cx = map.getPreferredWidth() / 2;
                        int cy = map.getPreferredHeight() / 2;
                        int dx = tx - cx;
                        int dy = ty - cy;
                        float mag = gesture.getPinchMagnitude();
                        
                        logger.info("GESTURE PINCH mag=" + mag + " t=(" + tx + "," + ty + "), c=(" + cx + "," + cy + "), d=(" + dx + "," + dy + ")");
                        
                        map.move(dx, dy);
                        
                        if (mag > 1.0)
                        {
                            map.zoomIn();
                        }
                        else
                        {
                            map.zoomOut();
                        }
                        return true;
                }
                break;
            case TouchEvent.MOVE:
                int[] x_points;
                int[] y_points;
                int[] time_points;
                int size = message.getMovePointsSize();
                x_points = new int[size];
                y_points = new int[size];
                time_points = new int[size];
                message.getMovePoints(1, x_points, y_points, time_points);
                
                logger.info("MOVE " + ObaString.toString(x_points) + ", " + ObaString.toString(y_points) + ", " + ObaString.toString(time_points));

                // process that MOVE here!

                return true;
                
            /*    
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
                return true;

            case TouchGesture.DOUBLE_TAP:
                // TODO:(pv) SHIFT should zoom out
                map.zoomIn();
                return true;
            */
    /*
    }
    */
    /*

        return super.touchEvent(message);
    }
    */

    /*
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
    */

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

    private boolean notFirstLocationAcquisition = false;

    public void locationUpdated(LocationProvider provider, Location location)
    {
        try
        {
            logger.info("+locationUpdated(...)");

            if (location != null && location.isValid())
            {
                synchronized (lockGps)
                {
                    geoLocation = new GeoLocation(location);

                    logger.info("geoLocation=" + geoLocation.toString());

                    Dialog.alert("geoLocation=" + geoLocation.toString());

                    if (!notFirstLocationAcquisition)
                    {
                        notFirstLocationAcquisition = true;
                        //map.getAction().setCentre(geoLocation.getMapPoint());
                    }

                    //MapDataModel mapModel = map.getModel();
                    //mapModel.remove("mylocation");
                    //mapModel.add((Mappable) geoLocation.getMapPoint(), "mylocation", true);
                    //map.getMapField().update(true);
                }
            }
        }
        finally
        {
            logger.info("-locationUpdated");
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

    public void setShowHeader(boolean showHeader)
    {
        Manager fieldManager = getMainManager();

        if (showHeader)
        {
            if (headerTODO == null)
            {
                // Create the header and add it to the top of the screen (below the banner)
                //incomingCallAcceptIgnoreButtons = new IncomingCallButtonManager(FIELD_BOTTOM | USE_ALL_WIDTH);
                //fieldManager.insert(headerTODO, 0);
            }
        }
        else
        {
            if (headerTODO != null)
            {
                Field temp = headerTODO;
                headerTODO = null;
                fieldManager.delete(temp);
            }
        }
        fieldManager.invalidate();
    }
}
