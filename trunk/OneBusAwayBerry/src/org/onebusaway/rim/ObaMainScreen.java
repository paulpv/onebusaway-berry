package org.onebusaway.rim;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

import org.onebusaway.berry.api.Context;
import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.berry.logging.ObaLogger;

public abstract class ObaMainScreen extends MainScreen
{
    // Alt-DEBG: Display Debug Dialog
    public static final int BACKDOOR_DEBG = ('D' << 24) | ('E' << 16) | ('B' << 8) | 'G';
    // Alt-DEBC: Clear the Debug Dialog
    public static final int BACKDOOR_DEBC = ('D' << 24) | ('E' << 16) | ('B' << 8) | 'C';
    // Alt-DEB0: Disable output to Debug Dialog; the default debug level
    public static final int BACKDOOR_DEB0 = ('D' << 24) | ('E' << 16) | ('B' << 8) | '0';
    // Alt-DEB1: Enable output to Debug Dialog
    public static final int BACKDOOR_DEB1 = ('D' << 24) | ('E' << 16) | ('B' << 8) | 'W';

    public interface ObaScreenListenerEventId
    {
        public static final int BANNER = 1;
    }

    public abstract void onUpdateBanner();

    protected final AppMain        app;
    protected final ResourceBundle resourceStrings;
    protected final ObaLogger      logger;

    public ObaMainScreen()
    {
        super(NO_VERTICAL_SCROLL | NO_HORIZONTAL_SCROLL);

        app = AppMain.get();
        resourceStrings = app.getResourceBundleStrings();
        logger = AppMain.createLogger(this);

        // Allow openProductionBackdoor(...)
        setBackdoorAltStatus(true);
    }

    /**
     * Prevents the app from exiting.
     * If you want to exit the app, call System.exit(...)
     */
    public void close()
    {
        UiApplication.getUiApplication().requestBackground();
    }

    /**
     * Handles the backdoor ALT+XXXX key sequences for the hidden debugging functions.
     */
    protected boolean openProductionBackdoor(int backdoorCode)
    {
        switch (backdoorCode)
        {
            case BACKDOOR_DEBG:
                //app.showScreenDebug();
                return true;
            case BACKDOOR_DEBC:
                //app.debugDialog.clear();
                return true;
            case BACKDOOR_DEB0:
                //app.setIsGlobalLoggingEnabled(false);
                return true;
            case BACKDOOR_DEB1:
                //app.setIsGlobalLoggingEnabled(true);
                return true;
        }

        return super.openProductionBackdoor(backdoorCode);
    }

    public void paint(Graphics g)
    {
        super.paint(g);

        /*
        // Only show the debug dialog indicator on screens that are actively showing the banner and statusbar
        if (showDebugDialogIndicator == Boolean.TRUE && banner.isVisible() && statusBar.isVisible())
        {
            g.setColor(Color.BLACK);
            int width = g.getFont().getAdvance(DEBUG_INDICATOR_TEXT);
            g.drawText(DEBUG_INDICATOR_TEXT, //
                            Display.getWidth() - width - DEBUG_INDICATOR_PADDING, //
                            banner.getHeight() + DEBUG_INDICATOR_PADDING);
        }
        */
    }

    private static Vector obaScreenListeners = new Vector();

    public static void addObaScreenListener(ObaMainScreen listener)
    {
        synchronized (obaScreenListeners)
        {
            if (!obaScreenListeners.contains(listener))
            {
                obaScreenListeners.addElement(listener);
            }
        }
    }

    public static void removeObaScreenListener(ObaMainScreen listener)
    {
        synchronized (obaScreenListeners)
        {
            obaScreenListeners.removeElement(listener);
        }
    }

    protected static void notifyObaScreenListeners(int eventId)
    {
        notifyObaScreenListeners(eventId, (Object) null);
    }

    protected static void notifyObaScreenListeners(int eventId, boolean param1)
    {
        notifyObaScreenListeners(eventId, (param1) ? Boolean.TRUE : Boolean.FALSE);
    }

    protected static void notifyObaScreenListeners(int eventId, int param1)
    {
        notifyObaScreenListeners(eventId, new Integer(param1));
    }

    protected static void notifyObaScreenListeners(int eventId, Object param1)
    {
        synchronized (obaScreenListeners)
        {
            ObaMainScreen listener;
            Enumeration listeners = obaScreenListeners.elements();
            while (listeners.hasMoreElements())
            {
                listener = (ObaMainScreen) listeners.nextElement();

                switch (eventId)
                {
                    case ObaScreenListenerEventId.BANNER:
                        listener.onUpdateBanner();
                        break;
                }
            }
        }
    }

    /**
     * No-Op method used as a placeholder for better compatibility w/ Android code.
     * 
     * @return null
     */
    protected Context getContext()
    {
        return ObaApi.getContext();
    }
}
