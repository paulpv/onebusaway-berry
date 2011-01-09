package org.onebusaway.rim.banner;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import org.onebusaway.rim.AppMain;

/**
 * Used to display the signal strength bars in the banner. The different indicator images all live in one file. Various
 * RadioInfo API calls are the source of this information. Call the update() method periodically to update the banner
 * display.
 */
public class CellularIndicator extends Field
{
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

    // Bars image loaded in constructor
    private Bitmap           barsIcon;

    // The image has nine different frames, each 62 pixels wide.
    // Image is 31 px high.
    //private static final int ICON_FRAME_WIDTH            = 62;
    //private static final int ICON_FRAME_HEIGHT           = 31;

    private int              currentFrame;

    private int              currentSelectionFrame;

    private static final int frameCount                  = 9;
    private final int        frameWidth;
    private final int        frameHeight;

    /*
    // Icon frames from left to right...
    private static final int FRAME_OFF                   = 0;
    private static final int FRAME_UNAVAILABLE           = 62;
    private static final int FRAME_NO_BARS               = 124;
    private static final int FRAME_ONE_BAR               = 186;
    private static final int FRAME_TWO_BARS              = 248;
    private static final int FRAME_THREE_BARS            = 310;
    private static final int FRAME_FOUR_BARS             = 372;
    private static final int FRAME_FIVE_BARS             = 434;
    private static final int FRAME_UNKNOWN               = 496;
    */
    private final int        FRAME_OFF;
    private final int        FRAME_UNAVAILABLE;
    private final int        FRAME_NO_BARS;
    private final int        FRAME_ONE_BAR;
    private final int        FRAME_TWO_BARS;
    private final int        FRAME_THREE_BARS;
    private final int        FRAME_FOUR_BARS;
    private final int        FRAME_FIVE_BARS;
    private final int        FRAME_UNKNOWN;

    private final AppMain    app;

    public CellularIndicator()
    {
        super(Field.FIELD_RIGHT | Field.NON_FOCUSABLE);

        app = AppMain.get();

        barsIcon = app.getResourceBitmap("wireless-signal-icons.png");
        frameWidth = barsIcon.getWidth() / frameCount; // should be 62
        frameHeight = barsIcon.getHeight();

        FRAME_OFF = 0;
        FRAME_UNAVAILABLE = FRAME_OFF + frameWidth;
        FRAME_NO_BARS = FRAME_UNAVAILABLE + frameWidth;
        FRAME_ONE_BAR = FRAME_NO_BARS + frameWidth;
        FRAME_TWO_BARS = FRAME_ONE_BAR + frameWidth;
        FRAME_THREE_BARS = FRAME_TWO_BARS + frameWidth;
        FRAME_FOUR_BARS = FRAME_THREE_BARS + frameWidth;
        FRAME_FIVE_BARS = FRAME_FOUR_BARS + frameWidth;
        FRAME_UNKNOWN = FRAME_FIVE_BARS + frameWidth;

        selectionIcon = app.getResourceBitmap("network-indicator-icons.png");

        // defaults to unknown
        currentFrame = FRAME_UNKNOWN;
        currentSelectionFrame = SELECTION_OTHER;

        update();
    }

    public void update()
    {
        int newFrame;

        int bars = app.getCellBars();
        switch (bars)
        {
            case AppMain.RADIO_OFF:
                newFrame = FRAME_OFF;
                break;
            case AppMain.RADIO_UNAVAILABLE:
            case AppMain.RADIO_UNSUPPORTED:
                newFrame = FRAME_UNAVAILABLE;
                break;
            case AppMain.RADIO_NO_SIGNAL:
                newFrame = FRAME_NO_BARS;
                break;
            case AppMain.RADIO_ONE_BAR:
                newFrame = FRAME_ONE_BAR;
                break;
            case AppMain.RADIO_TWO_BARS:
                newFrame = FRAME_TWO_BARS;
                break;
            case AppMain.RADIO_THREE_BARS:
                newFrame = FRAME_THREE_BARS;
                break;
            case AppMain.RADIO_FOUR_BARS:
                newFrame = FRAME_FOUR_BARS;
                break;
            case AppMain.RADIO_FIVE_BARS:
                newFrame = FRAME_FIVE_BARS;
                break;
            default:
                throw new IllegalArgumentException("Illegal value for cellular bars: " + bars);
        }

        int newSelectionFrame;
        if (app.getUseWiFi())
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
        }
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
                        barsIcon, currentFrame, 0);
        g.drawBitmap(0, 2, SELECTION_ICON_FRAME_WIDTH, SELECTION_ICON_FRAME_HEIGHT, //
                        selectionIcon, currentSelectionFrame, 0);
    }
}
