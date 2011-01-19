//#preprocess

package org.onebusaway.rim;

import java.util.Random;

import javax.microedition.location.Coordinates;

import net.rim.device.api.lbs.maps.MapFactory;
import net.rim.device.api.lbs.maps.model.MapDataModel;
import net.rim.device.api.lbs.maps.model.MapLocation;
import net.rim.device.api.lbs.maps.model.MapPoint;
import net.rim.device.api.lbs.maps.model.Mappable;
import net.rim.device.api.lbs.maps.ui.RichMapField;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.input.InputSettings;
import net.rim.device.api.ui.input.NavigationDeviceSettings;
import net.rim.device.api.ui.input.TouchscreenSettings;

import org.onebusaway.berry.map.ObaMapField;
import org.onebusaway.berry.map.ObaMapMarker;
import org.onebusaway.berry.map.ObaMapMarkerLocation;
import org.onebusaway.berry.map.ObaMapMarkerStop;
import org.onebusaway.berry.map.ObaMapMarkerStop.StopDirections;
import org.onebusaway.berry.map.ObaMapMarkerStop.StopTypes;

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

    public static String toString(int[] ints)
    {
        StringBuffer sb = new StringBuffer();
        if (ints == null)
        {
            sb.append("null");
        }
        else
        {
            sb.append("[");
            for (int i = 0; i < ints.length; i++)
            {
                if (i != 0)
                {
                    sb.append(",");
                }
                sb.append(ints[i]);
            }
            sb.append("]");
        }
        return sb.toString();
    }

    class RichMapFieldScreen extends MainScreen
    {
        public final MapPoint                             MAPPOINT_SEATTLE = new MapPoint(47.6063889, -122.3308333);

        protected ObaMapField                              mapVer4;
        protected net.rim.device.api.lbs.maps.ui.MapField mapVer6;
        protected RichMapField                            mapVer6Rich;

        public RichMapFieldScreen()
        {
            super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);

            // Enable touch pinch and trackpad swipe gestures:
            // http://docs.blackberry.com/en/developers/deliverables/21157/Enable_pinch_1222743_11.jsp
            // http://docs.blackberry.com/en/developers/deliverables/21157/Enable_swipe_trackpad_1226852_11.jsp
            InputSettings ts = TouchscreenSettings.createEmptySet();
            ts.set(TouchscreenSettings.DETECT_PINCH, 1);
            addInputSettings(ts);
            InputSettings nd = NavigationDeviceSettings.createEmptySet();
            nd.set(NavigationDeviceSettings.DETECT_SWIPE, 1);
            addInputSettings(nd);

            if (true)
            {
                Coordinates coordinates = MAPPOINT_SEATTLE.toCoordinates();
                mapVer4 = new ObaMapField();
                mapVer4.moveTo(coordinates);
                mapVer4.setZoom(2);
                add(mapVer4);

                Random random = new Random();

                coordinates = MAPPOINT_SEATTLE.toCoordinates();
                ObaMapMarkerLocation markerLocation = new ObaMapMarkerLocation("-1", coordinates);
                mapVer4.mapLocationAdd(markerLocation);
                mapVer4.setGpsLocked(true);

                ObaMapMarker mapMarker;
                String id;
                String name;
                int type;
                int direction;
                boolean favorite;

                for (int i = 0; i < 100; i++)
                {
                    id = String.valueOf(i);
                    coordinates = MAPPOINT_SEATTLE.toCoordinates();
                    coordinates.setLatitude(coordinates.getLatitude() + ((random.nextFloat() - random.nextFloat()) / 10));
                    coordinates.setLongitude(coordinates.getLongitude() + ((random.nextFloat() - random.nextFloat()) / 10));
                    name = "#" + id;
                    type = StopTypes.BUS;
                    direction = random.nextInt(StopTypes.getMax()) + 1;
                    direction = random.nextInt(StopDirections.getMax()) + 1;
                    favorite = (random.nextInt() % 2 == 0);

                    mapMarker = new ObaMapMarkerStop(id, coordinates, name, type, direction, favorite);
                    mapVer4.mapMarkersAdd(mapMarker, false);
                }
                mapVer4.invalidate();
            }
            else
            {
                // Use of either of these two v6.0 fields eventually locks up the app

                if (false)
                {
                    mapVer6 = new net.rim.device.api.lbs.maps.ui.MapField();
                    mapVer6.getAction().setCentreAndZoom(MAPPOINT_SEATTLE, 2);
                    add(mapVer6);

                    // addMapVer6Data(mapVer6);
                }
                else if (false)
                {
                    mapVer6Rich = MapFactory.getInstance().generateRichMapField();
                    mapVer6Rich.getAction().setCentreAndZoom(MAPPOINT_SEATTLE, 2);
                    add(mapVer6Rich);

                    // addMapVer6Data(mapVer6Rich.getMapField());
                }
            }
        }

        public void addMapVer6Data(net.rim.device.api.lbs.maps.ui.MapField map6)
        {
            MapDataModel data = map6.getModel();

            // create Andrea's Home and Work locations
            MapLocation andreaHome = new MapLocation(45.32503, -75.91334, "Andrea - Home", null);
            MapLocation rimMarch = new MapLocation(45.34154, -75.91484, "RIM - March", null);

            // add Andrea's home and work with tags "andrea", "home" and "work"
            // this adds Andrea's home location and tags it "andrea"
            int andreaHomeId = data.add((Mappable) andreaHome, "andrea");

            // this ID is used for direct access of data within this container
            // allowing the addition of a second tag to a single piece of data
            data.tag(andreaHomeId, "home");

            // this adds andrea's work and tags it "andrea"
            int rimMarchId = data.add((Mappable) rimMarch, "andrea");

            // ... and this tags it "work"
            data.tag(rimMarchId, "work");

            // create Mike's home location and add it with appropriate tags
            MapLocation mikeHome = new MapLocation(45.73471, -75.92935, "Mike - Home", null);
            int mikeHomeId = data.add((Mappable) mikeHome, "mike");
            data.tag(mikeHomeId, "home");

            // Mike also works at RIM March, so tag it with "mike"
            data.tag(rimMarchId, "mike");

            // Andrea, Mike's wife, lives at the same home location as Mike, but
            // works at a different work location
            data.tag(mikeHomeId, "andrea");

            MapLocation rimInnovation = new MapLocation(45.34332, -75.92992, "RIM - Innovation", null);

            int rimInnovationId = data.add((Mappable) rimInnovation, "andrea");

            data.tag(rimInnovationId, "work");

            // At this point, there are 4 visible locations in the data container,
            // each tagged with at least one items of information.
            // To see all of the locations
            // map.getMapField().update(true);
            map6.update(true);

            // To show only items that are tagged "work", turn everything invisible
            data.setVisibleNone();

            // ... and set items tagged "work" as visible
            data.setVisible("work");

            // the map will automatically update at this point, but the screen may not
            // be "efficient". To recenter and change the zoom so that the data fills
            // the screen...
            // map.getMapField().update(true);
            map6.update(true);
        }

        protected boolean keyDown(int keycode, int time)
        {
            log("keyDown(" + keycode + ")");

            switch (keycode)
            {
                case 268435456:
                    // Zoom in
                    mapVer4.setZoom(Math.max(mapVer4.getZoom() - 1, mapVer4.getMinZoom()));
                    return true;

                case 268500992:
                    // Zoom out
                    mapVer4.setZoom(Math.min(mapVer4.getZoom() + 1, mapVer4.getMaxZoom()));
                    return true;
            }
            return super.keyDown(keycode, time);
        }
    }
}
