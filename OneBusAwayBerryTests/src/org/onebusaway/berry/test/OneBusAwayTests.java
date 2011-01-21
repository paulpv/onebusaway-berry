package org.onebusaway.berry.test;

import j2meunit.framework.TestCase;
import j2meunit.framework.TestSuite;
import j2meunit.rimui.TestRunner;

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

        TestSuite testSuite = new TestSuite();
        testSuite.addTest(new org.onebusaway.berry.test.api.request.test.AllTests().suite());

        TestRunner instance = new TestRunner(testSuite);
        instance.enterEventDispatcher();
    }
}
