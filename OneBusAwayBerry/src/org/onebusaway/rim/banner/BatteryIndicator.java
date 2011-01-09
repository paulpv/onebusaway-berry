package org.onebusaway.rim.banner;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.SystemListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import org.onebusaway.rim.AppMain;

/**
 * Battery indicator icon for the banner.
 */
public class BatteryIndicator extends Field implements SystemListener
{
    private final Bitmap     batteryIcon;

    // The battery icon image has six different frames, each 44 pixels wide.
    // Image is 31 px high.
    //private static final int ICON_FRAME_WIDTH  = 44;
    //private static final int ICON_FRAME_HEIGHT = 31;

    private static final int frameCount     = 6;
    private final int        frameWidth;
    private final int        frameHeight;

    // Icon frames from left to right...
    private static final int BATTERY_0_BARS = 0;
    private static final int BATTERY_1_BAR  = 44;
    private static final int BATTERY_2_BARS = 88;
    private static final int BATTERY_3_BARS = 132;
    private static final int BATTERY_4_BARS = 176;
    private static final int BATTERY_5_BARS = 220;

    // The current frame is set in update() and used in paint()
    private int              currentFrame;

    private final AppMain    app;

    public BatteryIndicator()
    {
        super(Field.FIELD_RIGHT | Field.NON_FOCUSABLE);

        app = AppMain.get();

        batteryIcon = app.getResourceBitmap("battery-icons.png");
        frameWidth = batteryIcon.getWidth() / frameCount; // should be 44
        frameHeight = batteryIcon.getHeight(); // should be 31 

        currentFrame = BATTERY_0_BARS;

        update();

        app.addSystemListener(this);
    }

    public void batteryGood()
    {
        update();
    }

    public void batteryLow()
    {
        update();
    }

    public void batteryStatusChange(int status)
    {
        update();
    }

    public void powerOff()
    {
        update();
    }

    public void powerUp()
    {
        update();
    }

    /**
     * Checks the battery level and determines the right number of bars to show in the indicator, then repaints.
     * TODO: check what battery levels the different indicators appear at.
     */
    public void update()
    {
        int newFrame;

        int batteryLevel = DeviceInfo.getBatteryLevel();
        if (batteryLevel < 10)
        {
            newFrame = BATTERY_0_BARS;
        }
        else if (batteryLevel < 20)
        {
            newFrame = BATTERY_1_BAR;
        }
        else if (batteryLevel < 40)
        {
            newFrame = BATTERY_2_BARS;
        }
        else if (batteryLevel < 60)
        {
            newFrame = BATTERY_3_BARS;
        }
        else if (batteryLevel < 80)
        {
            newFrame = BATTERY_4_BARS;
        }
        else
        {
            newFrame = BATTERY_5_BARS;
        }

        if (newFrame != currentFrame)
        {
            currentFrame = newFrame;
            invalidate();
        }
    }

    public int getPreferredHeight()
    {
        return frameHeight;
    }

    public int getPreferredWidth()
    {
        return frameWidth;
    }

    protected void layout(int width, int height)
    {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    protected void paint(Graphics g)
    {
        g.drawBitmap(0, 0, batteryIcon.getWidth(), batteryIcon.getHeight(), //
                        batteryIcon, currentFrame, 0);
    }
}
