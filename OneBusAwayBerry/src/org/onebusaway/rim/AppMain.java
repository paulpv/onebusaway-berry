package org.onebusaway.rim;

import net.rim.device.api.gps.GPSInfo;
import net.rim.device.api.gps.LocationInfo;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class AppMain extends UiApplication
{
    public static void main(String[] args)
    {
        AppMain app = new AppMain();
        app.enterEventDispatcher();
    }
    
    public static void errorDialog(final String message)
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                Dialog.alert(message);
            }
        });
    }

    private ResourceBundle resourceStrings = ResourceBundle.getBundle(BBResource.BUNDLE_ID, BBResource.BUNDLE_NAME);

    private static AppMain instance = null;
    
    public static AppMain get()
    {
        return instance;
    }
    
    private AppMain()
    {
        instance = this;
        pushScreen(new ScreenMap());
    }

    public String getResourceString(int id)
    {
        return resourceStrings.getString(id);
    }

    public Bitmap getResourceBitmap(String name)
    {
        return Bitmap.getBitmapResource(name);
    }

    public void log(String msg)
    {
        System.out.println(msg);
    }

    public void exit()
    {
        exit(0);
    }
    
    public void exit(int exitCode)
    {
        //UiApplication.getUiApplication().requestClose();
        System.exit(exitCode);
    }
    
    public void activate()
    {
        super.activate();
        
        // TODO:(pv) Refresh current screen, especially ScreenMyStopList
    }
}
