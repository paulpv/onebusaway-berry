package org.onebusaway.rim;

import net.rim.device.api.gps.GPSInfo;
import net.rim.device.api.gps.LocationInfo;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class AppMain extends UiApplication
{
    private static ResourceBundle resourceStrings = ResourceBundle.getBundle(BBResource.BUNDLE_ID, BBResource.BUNDLE_NAME);

    public static String getResourceString(int id)
    {
        return resourceStrings.getString(id);
    }

    public static Bitmap getResourceBitmap(String name)
    {
        return Bitmap.getBitmapResource(name);
    }

    public static void main(String[] args)
    {
        AppMain app = new AppMain();
        app.enterEventDispatcher();
    }

    private AppMain()
    {
        pushScreen(new ScreenMap());

        invokeLater(new Runnable()
        {
            public void run()
            {
                initialize();
            }
        });
    }

    protected void initialize()
    {
        int locationCapability = LocationInfo.getSupportedLocationSources();

        boolean gps = (locationCapability & (GPSInfo.GPS_DEVICE_INTERNAL | GPSInfo.GPS_DEVICE_BLUETOOTH)) != 0;

        //gpsThread = new GPSThread();
        //gpsThread.start();
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
}
