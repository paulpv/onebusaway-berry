package org.onebusaway.rim;

import javax.microedition.location.Coordinates;

import net.rim.device.api.system.Bitmap;

public class MyMapMarker
{
    protected final String      id;
    protected final Coordinates coordinates;
    protected final Bitmap      imageUnfocused;
    protected final Bitmap      imageFocused;
    protected boolean           focused;

    public MyMapMarker(String id, Coordinates coordinates)
    {
        this.id = id;
        this.coordinates = coordinates;

        imageUnfocused = Bitmap.getBitmapResource("pin.png");
        imageFocused = Bitmap.getBitmapResource("pin_focus.png");
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

    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }

    public Bitmap getBitmap()
    {
        return (focused) ? imageFocused : imageUnfocused;
    }
}
