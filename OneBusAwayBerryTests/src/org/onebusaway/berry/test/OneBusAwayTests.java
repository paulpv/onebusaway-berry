package org.onebusaway.berry.test;

import j2meunit.framework.TestCase;
import j2meunit.rimui.TestRunner;

import org.onebusaway.berry.test.api.request.test.RequestTests;

public class OneBusAwayTests {
    public static void main(String[] args) {
        /*
        StringBuffer sb = new StringBuffer("args=[");
        for(int i=0; i<args.length; i++) {
            if (i != 0)
            {
                System.out.print(", ");
            }
            sb.append("\"" + args[i] + "\"");
        }
        sb.append("]");
        System.out.println(sb.toString());
        
        if (!ObaPermissions.checkPermissions(true))
        {
            ObaUtils.exitMessageNoUi(1, "Required permissions not set; exiting.");
            return;
        }
        */

        TestCase[] testCases = new TestCase[]
        {
            new RequestTests(),
        };

        TestRunner instance = new TestRunner(testCases);

        instance.enterEventDispatcher();
    }
}
