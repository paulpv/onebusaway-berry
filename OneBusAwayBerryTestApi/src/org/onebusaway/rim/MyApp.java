//#preprocess

package org.onebusaway.rim;

import org.onebusaway.api.ObaApi;
import org.onebusaway.tests.CurrentTimeRequestTest;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

public class MyApp extends UiApplication
{
    public static void main(String[] args)
    {
        MyApp theApp = new MyApp();
        theApp.enterEventDispatcher();
    }

    public MyApp()
    {
        pushScreen(new MyMainScreen());
    }

    public static void log(String tag, String msg)
    {
        System.out.println(tag + " " + msg);
    }

    class MyMainScreen extends MainScreen
    {
        protected RichTextField rtf;

        public MyMainScreen()
        {
            //super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL);

            rtf = new RichTextField(USE_ALL_HEIGHT | USE_ALL_WIDTH);
            rtf.setText("Testing, testing, 1, 2, 3...");
            add(rtf);
            
            invokeLater(new Runnable()
            {
                public void run()
                {
                    testApi();
                }
            });
        }
        
        protected void testApi()
        {
            //ObaApi.setAppInfo(2, "");
            
            CurrentTimeRequestTest test = new CurrentTimeRequestTest();
            test.testCurrentTime();
            test.testNewRequest();
        }
    }
}
