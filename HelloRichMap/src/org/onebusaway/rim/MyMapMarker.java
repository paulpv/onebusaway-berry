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
}
