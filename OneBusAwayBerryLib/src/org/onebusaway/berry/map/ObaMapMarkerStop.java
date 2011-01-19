package org.onebusaway.berry.map;

import javax.microedition.location.Coordinates;

public class ObaMapMarkerStop extends ObaMapMarker
{
    public static class StopDirections
    {
        public static final int UNKNOWN    = 0;
        public static final int NORTH      = 1;
        public static final int NORTH_EAST = 2;
        public static final int EAST       = 3;
        public static final int SOUTH_EAST = 4;
        public static final int SOUTH      = 5;
        public static final int SOUTH_WEST = 6;
        public static final int WEST       = 7;
        public static final int NORTH_WEST = 8;

        public static int getMin()
        {
            return UNKNOWN;
        }

        public static int getMax()
        {
            return NORTH_WEST;
        }
    }

    public static class StopTypes
    {
        public static final int UNKNOWN = 0;
        public static final int BUS     = 1;

        public static int getMin()
        {
            return UNKNOWN;
        }

        public static int getMax()
        {
            return BUS;
        }
    }

    protected final String name;
    protected final int    type;
    protected final int    direction;
    protected boolean      favorite;

    public ObaMapMarkerStop(String id, Coordinates coordinates, String name, int type, int direction, boolean favorite)
    {
        super(id, coordinates, "pin.png", "pin_focus.png");

        if (direction < StopDirections.getMin() || direction > StopDirections.getMax())
        {
            throw new IllegalArgumentException("direction must be a value of MyStopMapMarker.StopDirections");
        }

        if (type < StopTypes.getMin() || type > StopTypes.getMax())
        {
            throw new IllegalArgumentException("type must be a value of MyStopMapMarker.StopTypes");
        }

        this.name = name;
        this.type = type;
        this.direction = direction;
        setFavorite(favorite);

        switch (direction)
        {
            case StopDirections.NORTH:
                addOverlay("dir_n.png");
                break;
            case StopDirections.NORTH_EAST:
                addOverlay("dir_ne.png");
                break;
            case StopDirections.EAST:
                addOverlay("dir_e.png");
                break;
            case StopDirections.SOUTH_EAST:
                addOverlay("dir_se.png");
                break;
            case StopDirections.SOUTH:
                addOverlay("dir_s.png");
                break;
            case StopDirections.SOUTH_WEST:
                addOverlay("dir_sw.png");
                break;
            case StopDirections.WEST:
                addOverlay("dir_w.png");
                break;
            case StopDirections.NORTH_WEST:
                addOverlay("dir_nw.png");
                break;
        }

        switch (type)
        {
            case StopTypes.BUS:
                addOverlay("stop_type_bus.png");
                break;
        }
    }

    public boolean getFavorite()
    {
        return favorite;
    }

    public void setFavorite(boolean favorite)
    {
        this.favorite = favorite;
    }

    public boolean getUseImageAlt()
    {
        return favorite || super.getUseImageAlt();
    }
}
