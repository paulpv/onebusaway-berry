//#preprocess

package org.onebusaway.rim;

import java.util.Random;
import java.util.Vector;

import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.input.InputSettings;
import net.rim.device.api.ui.input.NavigationDeviceSettings;
import net.rim.device.api.ui.input.TouchscreenSettings;

import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.map.GeoPoint;
import org.onebusaway.berry.map.ObaMapField;
import org.onebusaway.berry.map.ObaMapMarker;
import org.onebusaway.berry.map.ObaMapMarkerLocation;
import org.onebusaway.berry.map.ObaMapMarkerStop;

public class MyApp extends UiApplication
{

    public static void main(String[] args)
    {
        MyApp theApp = new MyApp();
        theApp.enterEventDispatcher();
    }

    public MyApp()
    {
        pushScreen(new RichMapFieldScreen());
    }

    public static void log(String msg)
    {
        System.out.println(msg);
    }

    class RichMapFieldScreen extends MainScreen
    {
        protected ObaMapField map;
        
        public RichMapFieldScreen()
        {
            super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);
            
            map = new ObaMapField();
            map.moveTo(ObaMapField.GEOPOINT_SEATTLE);
            map.setZoom(2);
            add(map);

            enablePinchAndSwipe();
            
            fakeItAsync();
        }

        protected boolean keyDown(int keycode, int time)
        {
            log("keyDown(" + keycode + ")");

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

        private void fakeItAsync()
        {
            new Thread(new Runnable()
            {

                public void run()
                {
                    createFakeMyLocation(ObaMapField.GEOPOINT_SEATTLE);
                    createFakeMarkers(ObaMapField.GEOPOINT_SEATTLE);
                }
            }).start();
        }

        private void createFakeMyLocation(GeoPoint point)
        {
            ObaMapMarkerLocation markerLocation = new ObaMapMarkerLocation("-1", point);
            synchronized (UiApplication.getEventLock())
            {
                map.mapLocationAdd(markerLocation);
                map.setGpsLocked(true);
            }
            map.invalidate();
        }

        private void createFakeMarkers(GeoPoint center)
        {
            Random random = new Random();
            double lat;
            double lon;

            GeoPoint coordinates;
            ObaMapMarker mapMarker;
            String id;
            String name;
            int type;
            String direction;
            boolean favorite;

            Vector mapMarkers = new Vector();
            for (int i = 0; i < 100; i++)
            {
                lat = center.getLatitude() + ((random.nextFloat() - random.nextFloat()) / 10);
                lon = center.getLongitude() + ((random.nextFloat() - random.nextFloat()) / 10);
                
                id = String.valueOf(i);
                coordinates = new GeoPoint(lat, lon);
                name = "#" + id;
                type = ObaStop.LOCATION_STOP;
                direction = (String) ObaMapMarkerStop.directions.elementAt(random.nextInt(ObaMapMarkerStop.directions.size()));
                favorite = (random.nextInt() % 2 == 0);

                mapMarker = new ObaMapMarkerStop(id, coordinates, name, type, direction, favorite);
                mapMarkers.addElement(mapMarker);
            }
            
            synchronized (UiApplication.getEventLock())
            {
                map.mapMarkersAdd(mapMarkers, true);
                // TODO:(pv) Get mapMarkersAdd to properly invalidate
            }
            map.invalidate();
        }
    }
}
