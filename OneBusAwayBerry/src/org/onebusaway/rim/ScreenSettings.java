package org.onebusaway.rim;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

public class ScreenSettings extends MainScreen
{
    public ScreenSettings()
    {
        LabelField title =
            new LabelField(AppMain.getResourceString(BBResource.TEXT_TITLE), LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
        setTitle(title);
    }
}
