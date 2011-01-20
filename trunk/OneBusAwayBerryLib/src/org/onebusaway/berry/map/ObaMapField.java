//#preprocess

package org.onebusaway.berry.map;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.location.Coordinates;

import net.rim.device.api.lbs.MapField;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYPoint;

public class ObaMapField extends MapField {
    public static final GeoPoint   GEOPOINT_SEATTLE         = new GeoPoint(47.6063889, -122.3308333);

    protected final Hashtable      markersStops             = new Hashtable();
    protected ObaMapMarkerLocation markerLocation           = null;

    // Used for outputting debugging geometry during touch events.
    // The drawn geometry can be a bit buggy, but it meets my current needs. 
    protected static final int     TOUCH_INDICATOR_RADIUS   = 10;
    protected static final int     TOUCH_INDICATOR_DIAMETER = TOUCH_INDICATOR_RADIUS * 2;
    int                            touchX1                  = -1;
    int                            touchX2                  = -1;
    int                            touchY1                  = -1;
    int                            touchY2                  = -1;

    /**
     * First, calls the super.paint(g).
     * Then, draws (for debugging purposes) a circle for the last two points touched.
     * Finally, locks markersStops and draws only the ones that are visible in the current view.  
     */
    protected void paint(Graphics g) {
        super.paint(g);

        if (touchX1 != -1 && touchY1 != -1) {
            if (touchX2 == -1 && touchY2 == -1) {
                g.setColor(Color.GREEN);
            }
            else {
                g.setColor(Color.RED);
            }
            g.drawArc(touchX1 - TOUCH_INDICATOR_RADIUS, touchY1 - TOUCH_INDICATOR_RADIUS, //
                            TOUCH_INDICATOR_DIAMETER, TOUCH_INDICATOR_DIAMETER, 0, 360);
        }

        if (touchX2 != -1 && touchY2 != -1) {
            g.setColor(Color.BLUE);
            g.drawArc(touchX2 - TOUCH_INDICATOR_RADIUS, touchY2 - TOUCH_INDICATOR_RADIUS, //
                            TOUCH_INDICATOR_DIAMETER, TOUCH_INDICATOR_DIAMETER, 0, 360);

            if (touchX1 != -1 && touchY1 != -1) {
                g.setColor(Color.MAGENTA);
                int x = Math.min(touchX1, touchX2);
                int y = Math.min(touchY1, touchY2);
                int width = Math.abs(touchX2 - touchX1);
                int height = Math.abs(touchY2 - touchY1);
                g.drawRect(x, y, width, height);
            }
        }

        int width = getPreferredWidth();
        int height = getPreferredHeight();

        ObaMapMarker marker;
        GeoPoint markerGeoPoint;
        Coordinates markerCoordinates;
        XYPoint markerXY;
        Bitmap markerBitmap;
        int markerWidth;
        int markerHeight;

        synchronized (markersStops) {
            Enumeration markers = markersStops.elements();
            while (markers.hasMoreElements()) {
                marker = (ObaMapMarker) markers.nextElement();

                markerGeoPoint = marker.getPoint();
                markerCoordinates = new Coordinates(markerGeoPoint.getLatitude(), markerGeoPoint.getLongitude(), Float.NaN);
                markerXY = new XYPoint();
                convertWorldToField(markerCoordinates, markerXY);

                markerBitmap = marker.getBitmap();
                markerWidth = markerBitmap.getWidth();
                markerHeight = markerBitmap.getHeight();

                if (markerXY.x - markerWidth >= 0 && markerXY.x + markerWidth <= width //
                                && markerXY.y - markerHeight >= 0 || markerXY.y + markerHeight <= height) {
                    //MyApp.log("Drawing " + marker);
                    marker.drawBitmap(g, markerXY.x, markerXY.y);
                }
                else {
                    //MyApp.log("Not drawing " + marker);
                }
            }
        }
    }

    //#ifndef BB_VER_4_7_AND_ABOVE
    /*
    //#endif
    
    int pinchBeginX = -1;
    int pinchBeginY = -1;
    int moveLastX   = -1;
    int moveLastY   = -1;

    protected boolean touchEvent(net.rim.device.api.ui.TouchEvent message)
    {
        net.rim.device.api.ui.TouchGesture gesture = message.getGesture();
        int x1 = message.getX(1);
        int y1 = message.getY(1);
        int x2 = message.getX(2);
        int y2 = message.getY(2);

        //#ifdef DEBUG
        touchX1 = x1;
        touchY1 = y1;
        touchX2 = x2;
        touchY2 = y2;
        invalidate();
        //#endif

        switch (message.getEvent())
        {
            case net.rim.device.api.ui.TouchEvent.GESTURE:
                switch (gesture.getEvent())
                {
                    case net.rim.device.api.ui.TouchGesture.PINCH_BEGIN:
                        pinchBeginX = -1;
                        pinchBeginY = -1;
                        return true;

                    case net.rim.device.api.ui.TouchGesture.PINCH_UPDATE:
                        int size = message.getMovePointsSize();
                        if (size > 1)
                        {
                            int[] x_points1;
                            int[] y_points1;
                            int[] time_points1;
                            x_points1 = new int[size];
                            y_points1 = new int[size];
                            time_points1 = new int[size];
                            message.getMovePoints(1, x_points1, y_points1, time_points1);

                            int[] x_points2;
                            int[] y_points2;
                            int[] time_points2;
                            x_points2 = new int[size];
                            y_points2 = new int[size];
                            time_points2 = new int[size];
                            message.getMovePoints(2, x_points2, y_points2, time_points2);

                            int xFirst = Math.min(x_points1[0], x_points2[0]);
                            int yFirst = Math.min(y_points1[0], y_points2[0]);

                            int xLast = Math.max(x_points1[size - 1], x_points2[size - 1]);
                            int yLast = Math.max(y_points1[size - 1], y_points2[size - 1]);

                            if (pinchBeginX == -1 && pinchBeginY == -1)
                            {
                                pinchBeginX = xFirst;
                                pinchBeginY = yFirst;
                            }

                            int dx = xLast - pinchBeginX;
                            int dy = yLast - pinchBeginY;

                            //MyApp.log("GESTURE PINCH x1=" + MyApp.toString(x_points1) + " y1=" + MyApp.toString(y_points1));
                            //MyApp.log("GESTURE PINCH x2=" + MyApp.toString(x_points2) + " y2=" + MyApp.toString(y_points2));
                            //MyApp.log("GESTURE PINCH d=(" + dx + "," + dy + ")");

                            int d = Math.max(dx, dy);
                            if (Math.abs(d) > 20)
                            {
                                pinchBeginX = xLast;
                                pinchBeginY = yLast;

                                if (d > 0)
                                {
                                    zoomIn();
                                }
                                else
                                {
                                    zoomOut();
                                }
                                fieldChangeNotify(ObaMapAction.ACTION_ZOOM_CHANGE);
                            }
                        }
                        return true;

                    case net.rim.device.api.ui.TouchGesture.PINCH_END:
                        pinchBeginX = -1;
                        pinchBeginY = -1;
                        return true;
                }
                break;

            case net.rim.device.api.ui.TouchEvent.MOVE:
                int size = message.getMovePointsSize();
                //MyApp.log("MOVE gesture=" + gesture + ", size=" + size + ", 1=" + x1 + "," + y1 + ", 2=" + x2 + "," + y2);
                if (size > 1 && x2 == -1 && y2 == -1)
                {
                    int[] x_points;
                    int[] y_points;
                    int[] time_points;
                    x_points = new int[size];
                    y_points = new int[size];
                    time_points = new int[size];
                    message.getMovePoints(1, x_points, y_points, time_points);

                    int dx = x_points[size - 2] - x_points[size - 1];
                    int dy = y_points[size - 2] - y_points[size - 1];

                    //MyApp.log("MOVE d=(" + dx + "," + dy + ")");

                    move(dx, dy);
                    fieldChangeNotify(ObaMapAction.ACTION_CENTRE_CHANGE);
                }
                return true;

                //case TouchGesture.DOUBLE_TAP:
                // TODO:(pv) Shift-Double-Tap should zoom out
                //zoomIn();
                //return true;
        }
        return super.touchEvent(message);
    }

    //#ifndef BB_VER_4_7_AND_ABOVE
    */
    //#endif

    public void invalidate() {
        super.invalidate();
    }

    /**
     * Adds the marker to markersStops only if it doesn't already exist.
     * Does *not* lock, and does *not* invalidate the field.
     * @param mapMarker
     */
    private void mapMarkersAddInternal(ObaMapMarker mapMarker) {
        // TODO:(pv) Verify if we need to override mapMarker hash or equal
        if (!markersStops.contains(mapMarker)) {
            mapMarker.setParent(this);
            markersStops.put(mapMarker.getId(), mapMarker);
            System.out.println("Marker added to collection: " + mapMarker.getId());
        }
        else {
            // w00t! we found an existing and skipped it!
            System.out.println("Marker already in collection: " + mapMarker.getId());
        }
    }

    public void mapMarkersAdd(ObaMapMarker mapMarker, boolean invalidate) {
        synchronized (markersStops) {
            mapMarkersAddInternal(mapMarker);
        }

        if (invalidate) {
            invalidate();
        }
    }

    public void mapMarkersAdd(Vector mapMarkers, boolean invalidate) {
        if (mapMarkers == null) {
            return;
        }

        synchronized (markersStops) {
            ObaMapMarker mapMarker;
            Enumeration mapMarkerEnum = mapMarkers.elements();
            while (mapMarkerEnum.hasMoreElements()) {
                mapMarker = (ObaMapMarker) mapMarkerEnum.nextElement();
                mapMarkersAddInternal(mapMarker);
            }
        }

        if (invalidate) {
            invalidate();
        }
    }

    public void mapMarkersAdd(ObaMapMarker[] mapMarkers, boolean invalidate) {
        if (mapMarkers == null) {
            return;
        }

        synchronized (markersStops) {
            ObaMapMarker mapMarker;
            for (int i = 0; i < mapMarkers.length; i++) {
                mapMarker = mapMarkers[i];
                mapMarkersAddInternal(mapMarker);
            }
        }

        if (invalidate) {
            invalidate();
        }
    }

    public void mapMarkersRemove(String id, boolean invalidate) {
        synchronized (markersStops) {
            ObaMapMarker mapMarker = (ObaMapMarker) markersStops.remove(id);
            mapMarker.setParent(null);
        }
        if (invalidate) {
            invalidate();
        }
    }

    public void mapLocationAdd(ObaMapMarkerLocation markerLocation) {
        this.markerLocation = markerLocation;
        //markerLocation.invalidate();
    }

    public void moveTo(GeoPoint point) {
        super.moveTo(point.getLatitudeE6() / 10, point.getLongitudeE6() / 10);
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(getLatitude(), getLongitude());
    }

    public void setGpsLocked(boolean locked) {
        synchronized (this) {
            /*
            isGpsLocked = locked;
            
            if (isGpsLocked)
            {
                colorMarker = Color.CADETBLUE;
                
                timerMarker = new Timer();
                timerMarker.schedule(new TimerTask()
                {
                    public void run()
                    {
                        colorMarker = (colorMarker == Color.CADETBLUE) ? Color.DARKBLUE : Color.CADETBLUE;
                    }
                }, 0, 250);
            }
            else
            {
                timerMarker.cancel();
                timerMarker = null;
            }
            */
        }
    }
}
