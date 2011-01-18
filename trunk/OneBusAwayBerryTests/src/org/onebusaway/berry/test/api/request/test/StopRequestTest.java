/*
 * Copyright (C) 2010 Paul Watts (paulcwatts@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.berry.test.api.request.test;

import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestMethod;
import j2meunit.framework.TestSuite;

import java.util.Vector;

import net.rim.device.api.util.Arrays;

import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.request.ObaStopRequest;
import org.onebusaway.berry.api.request.ObaStopResponse;
import org.onebusaway.berry.test.MoreAsserts;
import org.onebusaway.berry.test.ObaTestCase;

public class StopRequestTest extends ObaTestCase {

    public StopRequestTest() {
        super();
    }
    
    public StopRequestTest(String testName, TestMethod testMethod) {
        super(testName, testMethod);
    }

    public Test suite() {
        TestSuite suite = new TestSuite("StopRequestTest");

        suite.addTest(new StopRequestTest("testKCMStop", new TestMethod()
        { public void run(TestCase tc) {((StopRequestTest)tc).testKCMStop(); } }));
        suite.addTest(new StopRequestTest("testNewRequest", new TestMethod()
        { public void run(TestCase tc) {((StopRequestTest)tc).testNewRequest(); } }));

        return suite;
    }
    
    public void testKCMStop() {
        ObaStopRequest.Builder builder = new ObaStopRequest.Builder(getContext(), "1_29261");
        ObaStopRequest request = builder.build();
        ObaStopResponse response = (ObaStopResponse) request.call();
        assertOK(response);
        assertEquals("1_29261", response.getId());
        assertEquals("29261", response.getStopCode());
        assertEquals(ObaStop.LOCATION_STOP, response.getLocationType());
        final String[] routeIds = response.getRouteIds();
        assertNotNull(routeIds);
        MoreAsserts.assertContentsInAnyOrder(routeIds, new String[] { "1_8", "1_10", "1_43"});

        final Vector routes = response.getRoutes();
        assertNotNull(routes);
        assertEquals(routes.size(), routeIds.length);
    }

    public void testNewRequest() {
        // This is just to make sure we copy and call newRequest() at least once
        ObaStopRequest request = ObaStopRequest.newRequest(getContext(), "1_29261");
        assertNotNull(request);
    }
}
