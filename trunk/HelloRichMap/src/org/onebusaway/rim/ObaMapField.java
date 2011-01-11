//#preprocess

package org.onebusaway.rim;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.lbs.MapField;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.TouchGesture;
import net.rim.device.api.ui.XYPoint;

public class ObaMapField extends MapField
{
    protected final Hashtable     markersStops             = new Hashtable();
    protected ObaMapMarkerLocation markerLocation           = null;

    // Used for outputting debugging geometry during touch events.
    // The drawn geometry can be a bit buggy, but it meets my current needs. 
    protected static final int    TOUCH_INDICATOR_RADIUS   = 10;
    protected static final int    TOUCH_INDICATOR_DIAMETER = TOUCH_INDICATOR_RADIUS * 2;
    int                           touchX1                  = -1;
    int                           touchX2                  = -1;
    int                           touchY1                  = -1;
    int                           touchY2                  = -1;

    protected void paint(Graphics g)
    {
        super.paint(g);

        if (touchX1 != -1 && touchY1 != -1)
        {
            if (touchX2 == -1 && touchY2 == -1)
            {
                g.setColor(Color.GREEN);
            }
            else
            {
                g.setColor(Color.RED);
            }
            g.drawArc(touchX1 - TOUCH_INDICATOR_RADIUS, touchY1 - TOUCH_INDICATOR_RADIUS, //
                            TOUCH_INDICATOR_DIAMETER, TOUCH_INDICATOR_DIAMETER, 0, 360);
        }

        if (touchX2 != -1 && touchY2 != -1)
        {
            g.setColor(Color.BLUE);
            g.drawArc(touchX2 - TOUCH_INDICATOR_RADIUS, touchY2 - TOUCH_INDICATOR_RADIUS, //
                            TOUCH_INDICATOR_DIAMETER, TOUCH_INDICATOR_DIAMETER, 0, 360);

            if (touchX1 != -1 && touchY1 != -1)
            {
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
        XYPoint markerXY;
        int markerX;
        int markerY;
        Bitmap markerBitmap;
        int markerWidth;
        int markerHeight;

        synchronized (markersStops)
        {
            Enumeration markers = markersStops.elements();
            while (markers.hasMoreElements())
            {
                marker = (ObaMapMarker) markers.nextElement();

                markerXY = new XYPoint();
                convertWorldToField(marker.getCoordinates(), markerXY);
                markerX = markerXY.x;
                markerY = markerXY.y;

                markerBitmap = marker.getBitmap();
                markerWidth = markerBitmap.getWidth();
                markerHeight = markerBitmap.getHeight();

                if (markerX + markerWidth < 0 || markerX - markerWidth > width //
                                || markerY + markerHeight < 0 || markerY - markerHeight > height)
                {
                    //MyApp.log("Not drawing " + marker);
                }
                else
                {
                    //MyApp.log("Drawing " + marker);
                    marker.drawBitmap(g, markerX, markerY);
                    //g.drawBitmap(markerX, markerY, markerWidth, markerHeight, markerBitmap, 0, 0);
                }
            }
        }
    }

    int pinchBeginX = -1;
    int pinchBeginY = -1;
    int moveLastX   = -1;
    int moveLastY   = -1;

    protected boolean touchEvent(TouchEvent message)
    {
        TouchGesture gesture = message.getGesture();
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
            case TouchEvent.GESTURE:
                switch (gesture.getEvent())
                {
                    case TouchGesture.PINCH_BEGIN:
                        pinchBeginX = -1;
                        pinchBeginY = -1;
                        return true;

                    case TouchGesture.PINCH_UPDATE:
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

                            MyApp.log("GESTURE PINCH x1=" + MyApp.toString(x_points1) + " y1=" + MyApp.toString(y_points1));
                            MyApp.log("GESTURE PINCH x2=" + MyApp.toString(x_points2) + " y2=" + MyApp.toString(y_points2));
                            MyApp.log("GESTURE PINCH d=(" + dx + "," + dy + ")");

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
                            }
                        }
                        return true;

                    case TouchGesture.PINCH_END:
                        pinchBeginX = -1;
                        pinchBeginY = -1;
                        return true;
                }
                break;

            case TouchEvent.MOVE:
                int size = message.getMovePointsSize();
                MyApp.log("MOVE gesture=" + gesture + ", size=" + size + ", 1=" + x1 + "," + y1 + ", 2=" + x2 + "," + y2);
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

                    MyApp.log("MOVE d=(" + dx + "," + dy + ")");

                    move(dx, dy);
                }
                return true;

                //case TouchGesture.DOUBLE_TAP:
                // TODO:(pv) Shift-Double-Tap should zoom out
                //zoomIn();
                //return true;
        }
        return super.touchEvent(message);
    }

    public void invalidate()
    {
        super.invalidate();
    }

    public void mapMarkersAdd(ObaMapMarker mapMarker, boolean invalidate)
    {
        synchronized (markersStops)
        {
            mapMarker.setParent(this);
            markersStops.put(mapMarker.getId(), mapMarker);
            if (invalidate)
            {
                //mapMarker.invalidate();
            }
        }
    }

    public void mapMarkersRemove(String id, boolean invalidate)
    {
        synchronized (markersStops)
        {
            ObaMapMarker mapMarker = (ObaMapMarker) markersStops.remove(id);
            mapMarker.setParent(null);
            if (invalidate)
            {
                //mapMarker.invalidate();
            }
        }
    }

    public void mapLocationAdd(ObaMapMarkerLocation markerLocation)
    {
        this.markerLocation = markerLocation;
        //markerLocation.invalidate();
    }

    public void setGpsLocked(boolean locked)
    {
        synchronized (this)
        {
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
