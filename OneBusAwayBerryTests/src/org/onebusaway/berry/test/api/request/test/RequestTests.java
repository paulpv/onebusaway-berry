package org.onebusaway.berry.test.api.request.test;

import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestSuite;

/**
 * Unit test suite for the LogicMail.mail classes
 */
public class RequestTests extends TestCase {
    
    public Test suite() {
        TestSuite suite = new TestSuite("OneBusAway.request");
        suite.addTest(new CurrentTimeRequestTest().suite());
        suite.addTest(new StopRequestTest().suite());
        suite.addTest(new StopsForLocationTest().suite());
        suite.addTest(new StopsForRouteRequestTest().suite());
        return suite;
    }
}
