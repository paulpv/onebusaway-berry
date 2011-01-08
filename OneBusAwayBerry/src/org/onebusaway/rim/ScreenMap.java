package org.onebusaway.rim;

import javax.microedition.location.Coordinates;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.TouchGesture;
import net.rim.device.api.ui.Trackball;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ScreenMap extends MainScreen
{
    private ObaMapField map;
    private LabelField  statusLine;

    private boolean     checkedTrackballSupport = false;

    ScreenMap()
    {
        super(Manager.NO_VERTICAL_SCROLL);

        Coordinates defaultLocation = new Coordinates(47.6063889, -122.3308333, 100);

        setTitle(AppMain.getResourceString(BBResource.TEXT_TITLE));

        map = new ObaMapField();
        map.moveTo(defaultLocation);
        map.setZoom(map.getMinZoom() + 1);
        add(map);

        statusLine = new LabelField("statusLine...", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);

        VerticalFieldManager statusVfm = new VerticalFieldManager();
        statusVfm.add(statusLine);
        setStatus(statusVfm);
    }

    public void close()
    {
        System.exit(0);
        super.close();
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
}
