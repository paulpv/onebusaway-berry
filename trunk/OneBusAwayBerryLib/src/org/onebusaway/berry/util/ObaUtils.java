package org.onebusaway.berry.util;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.component.Dialog;

public class ObaUtils {
    public static void exitMessageNoUi(final int exitCode, final String message) {
        Application app = new Application()
        {
        };
        app.invokeLater(new Runnable()
        {
            public void run() {
                Dialog.alert(message);
                System.exit(exitCode);
            }
        });
        app.enterEventDispatcher();
    }
}
