package org.onebusaway.berry.map;

/**
 * Closely follows BB OS v6's MapAction's constants.
 * Used on fieldChangeNotify events.
 * 
 * @author pv
 */
public interface ObaMapAction {
    public static final int ACTION_ZOOM_CHANGE              = 1;
    public static final int ACTION_CENTRE_CHANGE            = 2;
    public static final int ACTION_FOCUSED_CHANGE           = 4;
    public static final int ACTION_OPERATION_MODE_CHANGE    = 8;
    public static final int ACTION_NAVIGATE_CHANGE          = 16;
    public static final int ACTION_SELECTED_MAPPABLE_CHANGE = 32;
}
