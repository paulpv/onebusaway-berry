package org.onebusaway.rim.banner;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.SystemListener;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import org.onebusaway.rim.AppMain;

public class BatteryIndicator extends Field implements SystemListener, BannerIndicator
{
    private final Bitmap     batteryIcon;

    private static final int frameCount = 6;
    private final int        frameWidth;
    private final int        frameHeight;

    // Icon frames from left to right...
    private final int        BATTERY_0_BARS;
    private final int        BATTERY_1_BARS;
    private final int        BATTERY_2_BARS;
    private final int        BATTERY_3_BARS;
    private final int        BATTERY_4_BARS;
    private final int        BATTERY_5_BARS;

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

        BATTERY_0_BARS = 0;
        BATTERY_1_BARS = BATTERY_0_BARS + frameWidth;
        BATTERY_2_BARS = BATTERY_1_BARS + frameWidth;
        BATTERY_3_BARS = BATTERY_2_BARS + frameWidth;
        BATTERY_4_BARS = BATTERY_3_BARS + frameWidth;
        BATTERY_5_BARS = BATTERY_4_BARS + frameWidth;

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

    public boolean update()
    {
        int newFrame;

        int batteryLevel = DeviceInfo.getBatteryLevel();
        if (batteryLevel < 10)
        {
            newFrame = BATTERY_0_BARS;
        }
        else if (batteryLevel < 20)
        {
            newFrame = BATTERY_1_BARS;
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
            return true;
        }
        return false;
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
        g.drawBitmap(0, 0, frameWidth, frameHeight, batteryIcon, currentFrame, 0);
    }
}
