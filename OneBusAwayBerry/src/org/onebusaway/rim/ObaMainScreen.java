package org.onebusaway.rim;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.container.MainScreen;

public abstract class ObaMainScreen extends MainScreen
{
    public abstract void onUpdateBanner();

    public ObaMainScreen()
    {
        super(NO_VERTICAL_SCROLL | NO_HORIZONTAL_SCROLL);
    }

    public interface ObaScreenListenerEventId
    {
        public static final int BANNER = 1;
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
}
