package org.onebusaway.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.MainScreen;

public class ScreenMyStopList extends MainScreen
{
    // Star, Stop Name, Stop Direction
    private Field         header;

    // #, Bus Direction, Time Stop, Minutes Delay, Minutes From Now (BlueDelayed, GreenOk, RedEarly)
    private Field         listBus;

    private final AppMain app;

    public ScreenMyStopList()
    {
        app = AppMain.get();

    }
}
