package org.onebusaway.rim;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.location.Coordinates;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

public class MyMapMarker
{
    protected MyMapField        parent         = null;
    protected final String      id;
    protected final Coordinates coordinates;
    protected final Bitmap      imageMain;
    protected final Bitmap      imageAlt;

    protected final Vector      vectorOverlays = new Vector();

    protected boolean           useImageAlt;

    protected MyMapMarker(String id, Coordinates coordinates, String imageMain)
    {
        this(id, coordinates, imageMain, null);
    }

    protected MyMapMarker(String id, Coordinates coordinates, String imageMain, String imageAlt)
    {
        this.id = id;
        this.coordinates = coordinates;
        this.imageMain = Bitmap.getBitmapResource(imageMain);

        Bitmap temp = null;
        if (imageAlt != null)
        {
            temp = Bitmap.getBitmapResource(imageAlt);
            if (!isSameSize(this.imageMain, temp))
            {
                temp = null;
            }
        }
        this.imageAlt = temp;

        useImageAlt = false;
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

    public boolean getUseImageAlt()
    {
        return useImageAlt;
    }

    public void setUseImageAlt(boolean useImageAlt)
    {
        this.useImageAlt = useImageAlt;
    }

    public void setParent(MyMapField parent)
    {
        this.parent = parent;
    }

    public Bitmap getBitmap()
    {
        return (imageAlt != null && getUseImageAlt()) ? imageAlt : imageMain;
    }

    private boolean isSameSize(Bitmap a, Bitmap b)
    {
        if (a != null && b != null)
        {
            int aWidth = a.getWidth();
            int aHeight = a.getHeight();

            int bWidth = b.getWidth();
            int bHeight = b.getHeight();

            return (aWidth == bWidth && aHeight == bHeight);
        }
        return false;
    }

    protected void addOverlay(String pathOverlay)
    {
        addOverlay(Bitmap.getBitmapResource(pathOverlay));
    }

    protected void addOverlay(Bitmap imageOverlay)
    {
        if (isSameSize(imageMain, imageOverlay))
        {
            vectorOverlays.addElement(imageOverlay);
        }
    }

    protected void drawBitmap(Graphics g, int x, int y)
    {
        Bitmap bitmap = getBitmap();
        g.drawBitmap(x, y, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);

        Enumeration overlays = vectorOverlays.elements();
        while (overlays.hasMoreElements())
        {
            bitmap = (Bitmap) overlays.nextElement();
            g.drawBitmap(x, y, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);
        }
    }
}
