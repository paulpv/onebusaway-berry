package org.onebusaway.rim;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.MainScreen;

public class OneBusAwayUiApp extends UiApplication
{
    public static void main(String[] args)
    {
        OneBusAwayUiApp app = new OneBusAwayUiApp();
        app.enterEventDispatcher();
    }

    private OneBusAwayUiApp()
    {
        pushScreen(new HelloWorldScreen());
    }
}

class HelloWorldScreen extends MainScreen
{
    HelloWorldScreen()
    {
        LabelField title = new LabelField("Hello World Demo", LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        setTitle(title);

        add(new RichTextField("Hello World!", Field.NON_FOCUSABLE));
    }

    public void close()
    {
        Dialog.alert("Goodbye!");
        System.exit(0);

        super.close();
    }
}
