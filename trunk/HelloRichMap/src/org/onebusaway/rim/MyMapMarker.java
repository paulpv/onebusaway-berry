package org.onebusaway.rim;

import javax.microedition.location.Coordinates;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

public class MyMapMarker
{
    protected final String      id;
    protected final Coordinates coordinates;
    protected final Bitmap      imageUnfocused;
    protected final Bitmap      imageFocused;

    protected boolean           focused;

    protected MyMapMarker(String id, Coordinates coordinates, String imageUnfocused, String imageFocused)
    {
        this.id = id;
        this.coordinates = coordinates;
        this.imageUnfocused = Bitmap.getBitmapResource(imageUnfocused);
        this.imageFocused = Bitmap.getBitmapResource(imageFocused);

        focused = false;
    }

    public String toString()
    {
        return getId();
    }

    public String getId()
    {
        return id;
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    public boolean getFocused()
    {
        return focused;
    }

    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    public Bitmap getBitmap()
    {
        return (getFocused()) ? imageFocused : imageUnfocused;
    }

    protected Bitmap getOverlay()
    {
        return null;
    }

    protected void drawBitmap(Graphics g, int x, int y)
    {
        Bitmap bitmap = getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        g.drawBitmap(x, y, bitmapWidth, bitmapHeight, bitmap, 0, 0);

        Bitmap overlay = getOverlay();
        if (overlay != null)
        {
            int overlayWidth = overlay.getWidth();
            int overlayHeight = overlay.getHeight();
            if (bitmapWidth == overlayWidth && bitmapHeight == overlayHeight)
            {
                //MyApp.log("overlaying");
                g.drawBitmap(x, y, overlayWidth, overlayHeight, overlay, 0, 0);
                return;
            }
        }
        //MyApp.log("not overlaying");
    }
}
