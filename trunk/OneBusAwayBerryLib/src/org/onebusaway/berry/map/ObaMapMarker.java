package org.onebusaway.berry.map;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import org.onebusaway.berry.api.elements.ObaStopElement;

public class ObaMapMarker {
    //protected ObaMapField    parent         = null;
    protected final String   id;
    protected final GeoPoint point;
    protected final Bitmap   imageMain;
    protected final Bitmap   imageAlt;

    protected final Vector   vectorOverlays = new Vector();

    protected boolean        useImageAlt;

    protected ObaMapMarker(String id, GeoPoint point, String imageMain) {
        this(id, point, imageMain, null);
    }

    protected ObaMapMarker(String id, GeoPoint point, String imageMain, String imageAlt) {
        this.id = id;
        this.point = point;
        this.imageMain = Bitmap.getBitmapResource(imageMain);

        Bitmap temp = null;
        if (imageAlt != null) {
            temp = Bitmap.getBitmapResource(imageAlt);
            if (!isSameSize(this.imageMain, temp)) {
                temp = null;
            }
        }
        this.imageAlt = temp;

        useImageAlt = false;
    }

    public String toString() {
        return getId();
    }

    public int hashCode() {
        // TODO:(pv) Understand this code
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ObaStopElement))
            return false;

        ObaMapMarker other = (ObaMapMarker) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public String getId() {
        return id;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public boolean getUseImageAlt() {
        return useImageAlt;
    }

    public void setUseImageAlt(boolean useImageAlt) {
        this.useImageAlt = useImageAlt;
    }

    /*
    public void setParent(ObaMapField parent) {
        this.parent = parent;
    }
    */

    public Bitmap getBitmap() {
        return (imageAlt != null && getUseImageAlt()) ? imageAlt : imageMain;
    }

    private boolean isSameSize(Bitmap a, Bitmap b) {
        if (a != null && b != null) {
            int aWidth = a.getWidth();
            int aHeight = a.getHeight();

            int bWidth = b.getWidth();
            int bHeight = b.getHeight();

            return (aWidth == bWidth && aHeight == bHeight);
        }
        return false;
    }

    protected void addOverlay(String pathOverlay) {
        addOverlay(Bitmap.getBitmapResource(pathOverlay));
    }

    protected void addOverlay(Bitmap imageOverlay) {
        if (isSameSize(imageMain, imageOverlay)) {
            vectorOverlays.addElement(imageOverlay);
        }
    }

    protected void drawBitmap(Graphics g, int x, int y) {
        Bitmap bitmap = getBitmap();
        g.drawBitmap(x, y, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);

        Enumeration overlays = vectorOverlays.elements();
        while (overlays.hasMoreElements()) {
            bitmap = (Bitmap) overlays.nextElement();
            g.drawBitmap(x, y, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);
        }
    }
}
