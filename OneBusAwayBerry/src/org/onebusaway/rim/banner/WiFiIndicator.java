package org.onebusaway.rim.banner;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import org.onebusaway.rim.AppMain;

/**
 * WiFi signal strength indicator in the banner.
 */
public class WiFiIndicator extends Field implements BannerIndicator
{
    // WiFi icon loaded in constructor
    private Bitmap           wifiIcon;

    // The wifi icon image has six different frames, each 45 pixels wide.
    // Image is 31 px high.
    //private static final int ICON_FRAME_WIDTH            = 45;
    //private static final int ICON_FRAME_HEIGHT           = 31;

    private static final int frameCount                  = 6;
    private final int        frameWidth;
    private final int        frameHeight;

    /*
    // Icon frames from left to right...
    private static final int WIFI_NONE                   = 0;
    private static final int WIFI_OFF                    = 45;
    private static final int WIFI_NO_NETWORK             = 90;
    private static final int WIFI_LOW_SIGNAL             = 135;
    private static final int WIFI_MEDIUM_SIGNAL          = 180;
    private static final int WIFI_HIGH_SIGNAL            = 225;
    */
    private final int        WIFI_NONE;
    private final int        WIFI_OFF;
    private final int        WIFI_NO_NETWORK;
    private final int        WIFI_LOW_SIGNAL;
    private final int        WIFI_MEDIUM_SIGNAL;
    private final int        WIFI_HIGH_SIGNAL;

    private int              currentFrame;

    // network selection icon loaded in constructor
    private Bitmap           selectionIcon;

    // The selection icon image has three different frames, each 11 pixels wide
    // x 11 pixels high.
    private static final int SELECTION_ICON_FRAME_WIDTH  = 11;
    private static final int SELECTION_ICON_FRAME_HEIGHT = 11;

    // Selection icon frames from left to right
    private static final int SELECTION_OTHER             = 0; // empty frame, some other
    // transport selected
    private static final int SELECTION_OK                = 11; // green, selected, working
    private static final int SELECTION_WARNING           = 22; // amber, selected, warning

    // The current selection frame is also set in update()
    private int              currentSelectionFrame;

    private final AppMain    app;

    public WiFiIndicator()
    {
        super(Field.FIELD_RIGHT | Field.NON_FOCUSABLE);

        app = AppMain.get();

        wifiIcon = app.getResourceBitmap("wifi-signal-icons.png");
        frameWidth = wifiIcon.getWidth() / frameCount; // should be 45
        frameHeight = wifiIcon.getHeight();

        WIFI_NONE = 0;
        WIFI_OFF = WIFI_NONE + frameWidth;
        WIFI_NO_NETWORK = WIFI_OFF + frameWidth;
        WIFI_LOW_SIGNAL = WIFI_NO_NETWORK + frameWidth;
        WIFI_MEDIUM_SIGNAL = WIFI_LOW_SIGNAL + frameWidth;
        WIFI_HIGH_SIGNAL = WIFI_MEDIUM_SIGNAL + frameWidth;

        selectionIcon = app.getResourceBitmap("network-indicator-icons.png");

        // defaults to no WiFi
        currentFrame = WIFI_NONE;
        currentSelectionFrame = SELECTION_OTHER;

        update();
    }

    public boolean update()
    {
        int newFrame;

        int bars = app.getWiFiBars();
        switch (bars)
        {
            case AppMain.RADIO_OFF:
            case AppMain.RADIO_UNAVAILABLE:
                newFrame = WIFI_OFF;
                break;
            case AppMain.RADIO_UNSUPPORTED:
                newFrame = WIFI_NONE;
                break;
            case AppMain.RADIO_NO_SIGNAL:
                newFrame = WIFI_NO_NETWORK;
                break;
            case AppMain.RADIO_ONE_BAR:
                newFrame = WIFI_LOW_SIGNAL;
                break;
            case AppMain.RADIO_TWO_BARS:
                newFrame = WIFI_MEDIUM_SIGNAL;
                break;
            case AppMain.RADIO_THREE_BARS:
                newFrame = WIFI_HIGH_SIGNAL;
                break;
            case AppMain.RADIO_FOUR_BARS:
            case AppMain.RADIO_FIVE_BARS:
            default:
                // wi-fi only goes up to 3 bars
                throw new IllegalArgumentException("Illegal value for wi-fi bars: " + bars);
        }

        // Show appropriate network selection indicator
        int newSelectionFrame;
        if (!app.getUseWiFi())
        {
            newSelectionFrame = SELECTION_OTHER; // empty frame
        }
        else if (bars > 0)
        {
            newSelectionFrame = SELECTION_OK; // green happy dot
        }
        else
        {
            newSelectionFrame = SELECTION_WARNING; // warning amber triangle
        }

        if (newFrame != currentFrame || newSelectionFrame != currentSelectionFrame)
        {
            currentFrame = newFrame;
            currentSelectionFrame = newSelectionFrame;
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
        return frameWidth + SELECTION_ICON_FRAME_WIDTH;
    }

    protected void layout(int width, int height)
    {
        setExtent(getPreferredWidth(), getPreferredHeight());
    }

    protected void paint(Graphics g)
    {
        g.drawBitmap(SELECTION_ICON_FRAME_WIDTH, 0, frameWidth, frameHeight, //
                        wifiIcon, currentFrame, 0);
        g.drawBitmap(0, 2, SELECTION_ICON_FRAME_WIDTH, SELECTION_ICON_FRAME_HEIGHT, //
                        selectionIcon, currentSelectionFrame, 0);
    }
}
