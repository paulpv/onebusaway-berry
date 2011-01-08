package org.onebusaway.rim;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

public class ScreenSettings extends MainScreen
{
    private final AppMain app;
    
    public ScreenSettings()
    {
        app = AppMain.get();
        
        LabelField title =
            new LabelField(app.getResourceString(BBResource.TEXT_TITLE), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        setTitle(title);
    }
}
