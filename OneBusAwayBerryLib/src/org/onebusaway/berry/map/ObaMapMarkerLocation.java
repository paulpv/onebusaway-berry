package org.onebusaway.berry.map;

import java.util.Timer;

import javax.microedition.location.Coordinates;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

public class ObaMapMarkerLocation extends ObaMapMarker
{
    protected Timer   timerMarker = null;
    protected boolean isGpsLocked = false;
    protected int     colorMarker;

    public ObaMapMarkerLocation(String id, Coordinates coordinates)
    {
        super(id, coordinates, "mylocation.png");
    }

    protected void drawBitmap(Graphics g, int x, int y, boolean isGpsLocked)
    {
        super.drawBitmap(g, x, y);

        // Draw pulsing effect on my location if GPS is acquired

        if (!isGpsLocked)
        {
            return;
        }

        Bitmap bitmap = getBitmap();
        int width = bitmap.getWidth() / 2;
        int height = bitmap.getHeight() / 2;

        g.setColor(colorMarker);
        g.fillArc(1 + x + width / 2, 1 + y + height / 2, width, height, 0, 360);
    }
}
