//#preprocess

package org.onebusaway.rim.banner;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.onebusaway.rim.AppMain;

/**
 * Arranges the banner above the screen.
 * This includes the logo, title, and network indicators.
 * Radio event listeners are attached and removed from the application here.
 */
public class BannerManager extends HorizontalFieldManager
{
    private LabelField         title;
    private WiFiIndicator      wifi;
    private CellularIndicator  cellular;
    private BatteryIndicator   battery;

    // Allow this much room on far right for network activity indicator.
    // There's not quite enough room for shift indicator, but that's true on the main banner as well.
    private static final int   MARGIN_BANNER_RIGHT = 10; //25;

    // We're this tall
    private static final int   BANNER_HEIGHT       = 34;

    // Leave a bit 'o space between indicators
    private static final int   MARGIN_INDICATOR    = 5;

    private static final int[] GRADIENT_COLORS     =
                                                   {
        Color.CADETBLUE, Color.CADETBLUE, Color.DARKBLUE, Color.DARKBLUE
                                                   };

    private final AppMain      app;

    public BannerManager()
    {
        super(USE_ALL_WIDTH);

        app = AppMain.get();

        Background bg = BackgroundFactory.createLinearGradientBackground( //
                        GRADIENT_COLORS[0], GRADIENT_COLORS[1], //
                        GRADIENT_COLORS[2], GRADIENT_COLORS[3]);
        setBackground(bg);

        // Logo and Title in the default banner
        title = new LabelField("", USE_ALL_HEIGHT | FIELD_VCENTER)
        {
            protected void paint(Graphics g)
            {
                //#ifdef DEBUG
                g.setBackgroundColor(Color.MAGENTA);
                g.drawRect(0, 0, getWidth(), getHeight());
                //#endif
                g.setColor(Color.WHITE);
                super.paint(g);
            }
        };
        title.setFont(title.getFont().derive(Font.PLAIN, 22, Ui.UNITS_px));
        add(title);

        // WiFi indicator
        if (app.isWiFiSupported())
        {
            wifi = new WiFiIndicator()
            //#ifdef DEBUG
                {
                    protected void paint(Graphics g)
                    {
                        g.setBackgroundColor(Color.RED);
                        g.drawRect(0, 0, getWidth(), getHeight());
                        super.paint(g);
                    }
                }
            //#endif
            ;
            add(wifi);
        }

        // Cellular indicator
        if (app.isCellularSupported())
        {
            cellular = new CellularIndicator()
            //#ifdef DEBUG
                {
                    protected void paint(Graphics g)
                    {
                        g.setBackgroundColor(Color.GREEN);
                        g.drawRect(0, 0, getWidth(), getHeight());
                        super.paint(g);
                    }
                }
            //#endif
            ;
            add(cellular);
        }

        // Battery indicator
        battery = new BatteryIndicator()
        //#ifdef DEBUG
            {
                protected void paint(Graphics g)
                {
                    g.setBackgroundColor(Color.YELLOW);
                    g.drawRect(0, 0, getWidth(), getHeight());
                    super.paint(g);
                }
            }
        //#endif
        ;
        add(battery);
    }

    public void setText(String titleText)
    {
        title.setText(titleText);
    }

    public void update()
    {
        if (wifi != null)
        {
            wifi.update();
        }
        if (cellular != null)
        {
            cellular.update();
        }
        battery.update();
    }

    public int getPreferredHeight()
    {
        return BANNER_HEIGHT;
    }

    public int getPreferredWidth()
    {
        return Display.getWidth();
    }

    protected void sublayout(int width, int height)
    {
        width = getPreferredWidth();
        height = getPreferredHeight();

        // banner with logo, title, colored gradient background, full width
        int bannerHeight = height;
        setExtent(width, bannerHeight);
        layoutChild(title, width, bannerHeight);
        int titleX = 2;
        int titleY = (bannerHeight - title.getHeight()) / 2;
        setPositionChild(title, titleX, titleY);

        // NOTE remainder of metrics are calced from right edge to left...
        int cellularWidth = cellular.getPreferredWidth();
        int cellularHeight = cellular.getPreferredHeight();
        int cellularX = width - cellularWidth - MARGIN_BANNER_RIGHT;

        int wifiWidth = (wifi == null) ? 0 : wifi.getPreferredWidth();
        int wifiHeight = (wifi == null) ? 0 : wifi.getPreferredHeight();
        int wifiX = cellularX - MARGIN_INDICATOR - wifiWidth;

        int batteryWidth = battery.getPreferredWidth();
        int batteryHeight = battery.getPreferredHeight();
        int batteryX = wifiX - batteryWidth - MARGIN_INDICATOR;

        // wifi indicator
        if (wifi != null)
        {
            layoutChild(wifi, wifiWidth, wifiHeight);
            setPositionChild(wifi, wifiX, 0);
        }

        // cellular indicator
        layoutChild(cellular, cellularWidth, cellularHeight);
        setPositionChild(cellular, cellularX, 0);

        // battery indicator
        layoutChild(battery, batteryWidth, batteryHeight);
        setPositionChild(battery, batteryX, 0);
    }
}
