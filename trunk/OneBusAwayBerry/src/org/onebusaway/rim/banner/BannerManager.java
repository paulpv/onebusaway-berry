//#preprocess

package org.onebusaway.rim.banner;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
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
    private LabelField         bannerTitle;
    private BatteryIndicator   battery;
    private WiFiIndicator      wifi;
    private CellularIndicator  cellular;

    // Allow this much room on far right for network/shift/alt indicator.
    // There's not quite enough room for shift/alt indicator, but that's true on the main banner as well.
    private static final int   MARGIN_BANNER_RIGHT = 25;

    // We're this tall
    private static final int   BANNER_HEIGHT       = 34;

    // Leave a bit 'o space between indicators
    private static final int   MARGIN_INDICATOR    = 5;

    private static final int[] GRADIENT_COLORS     =
                                                   {
        Color.CADETBLUE, Color.CADETBLUE, Color.DARKBLUE, Color.DARKBLUE
                                                   };

    private final AppMain      app;

    public BannerManager(String bannerText)
    {
        super(USE_ALL_WIDTH);

        app = AppMain.get();

        Background bg = BackgroundFactory.createLinearGradientBackground( //
                        GRADIENT_COLORS[0], GRADIENT_COLORS[1], //
                        GRADIENT_COLORS[2], GRADIENT_COLORS[3]);
        setBackground(bg);

        // Logo and Title in the default banner
        bannerTitle = new LabelField()
            {
                protected void paint(Graphics g)
                {
                    //#ifdef DEBUG
                    g.setColor(Color.MAGENTA);
                    g.drawRect(0, 0, getPreferredWidth(), getPreferredHeight());
                    //#endif
                    g.setColor(Color.WHITE);
                    super.paint(g);
                }
            };
        bannerTitle.setText(bannerText);
        add(bannerTitle);

        // Battery indicator
        battery = new BatteryIndicator()
        //#ifdef DEBUG
            {
                protected void paint(Graphics g)
                {
                    g.setColor(Color.YELLOW);
                    g.drawRect(0, 0, getPreferredWidth(), getPreferredHeight());
                    super.paint(g);
                }
            }
        //#endif
        ;
        add(battery);

        // WiFi indicator
        if (app.isWiFiSupported())
        {
            wifi = new WiFiIndicator()
            //#ifdef DEBUG
                {
                    protected void paint(Graphics g)
                    {
                        g.setColor(Color.RED);
                        g.drawRect(0, 0, getPreferredWidth(), getPreferredHeight());
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
                        g.setColor(Color.GREEN);
                        g.drawRect(0, 0, getPreferredWidth(), getPreferredHeight());
                        super.paint(g);
                    }
                }
            //#endif
            ;
            add(cellular);
        }
    }

    public void update()
    {
        battery.update();
        if (wifi != null)
        {
            wifi.update();
        }
        if (cellular != null)
        {
            cellular.update();
        }
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
        layoutChild(bannerTitle, width, bannerHeight);
        int titleX = 2;
        int titleY = (bannerHeight - bannerTitle.getPreferredHeight()) / 2;
        setPositionChild(bannerTitle, titleX, titleY);

        // NOTE remainder of metrics are calced from right edge to left...
        int cellularWidth = (cellular == null) ? 0 : cellular.getPreferredWidth();
        int cellularHeight = (cellular == null) ? 0 : cellular.getPreferredHeight();
        int cellularX = width - cellularWidth - MARGIN_BANNER_RIGHT;

        int wifiWidth = (wifi == null) ? 0 : wifi.getPreferredWidth();
        int wifiHeight = (wifi == null) ? 0 : wifi.getPreferredHeight();
        int wifiX = cellularX - MARGIN_INDICATOR - wifiWidth;

        int batteryWidth = battery.getPreferredWidth();
        int batteryHeight = battery.getPreferredHeight();
        int batteryX = wifiX - batteryWidth - MARGIN_INDICATOR;

        // battery indicator
        layoutChild(battery, batteryWidth, batteryHeight);
        setPositionChild(battery, batteryX, 0);

        // wifi indicator
        if (wifi != null)
        {
            layoutChild(wifi, wifiWidth, wifiHeight);
            setPositionChild(wifi, wifiX, 0);
        }

        // cellular indicator
        if (cellular != null)
        {
            layoutChild(cellular, cellularWidth, cellularHeight);
            setPositionChild(cellular, cellularX, 0);
        }
    }
}
