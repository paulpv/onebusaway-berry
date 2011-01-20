package org.onebusaway.berry.map;

import java.util.Vector;

import org.onebusaway.berry.api.elements.ObaStop;

public class ObaMapMarkerStop extends ObaMapMarker {
    public static final Vector directions;
    static {
        directions = new Vector();
        directions.addElement("n");
        directions.addElement("ne");
        directions.addElement("e");
        directions.addElement("se");
        directions.addElement("s");
        directions.addElement("sw");
        directions.addElement("w");
        directions.addElement("nw");
    }

    protected final String     name;
    protected boolean          favorite;

    public ObaMapMarkerStop(String id, GeoPoint point, String name, int type, String direction, boolean favorite) {
        super(id, point, "pin.png", "pin_focus.png");

        direction = (direction == null) ? "" : direction.toLowerCase();

        this.name = name;

        switch (type) {
            case ObaStop.LOCATION_STATION:
            case ObaStop.LOCATION_STOP:
                addOverlay("stop_type_bus.png");
                break;
        }

        if (directions.contains(direction)) {
            addOverlay("dir_" + direction + ".png");
        }

        setFavorite(favorite);
    }

    public boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean getUseImageAlt() {
        return favorite || super.getUseImageAlt();
    }
}
