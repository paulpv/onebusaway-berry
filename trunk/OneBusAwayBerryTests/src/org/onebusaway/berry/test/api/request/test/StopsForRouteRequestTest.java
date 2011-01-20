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

import org.onebusaway.berry.api.elements.ObaShape;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaStopGrouping;
import org.onebusaway.berry.api.request.ObaStopsForRouteRequest;
import org.onebusaway.berry.api.request.ObaStopsForRouteResponse;
import org.onebusaway.berry.test.ObaTestCase;

public class StopsForRouteRequestTest extends ObaTestCase {

    public StopsForRouteRequestTest() {
        super();
    }

    public StopsForRouteRequestTest(String testName, TestMethod testMethod) {
        super(testName, testMethod);
    }

    public Test suite() {
        TestSuite suite = new TestSuite("StopsForRouteRequestTest");

        suite.addTest(new StopsForRouteRequestTest("testKCMRoute", new TestMethod()
        {
            public void run(TestCase tc) {
                ((StopsForRouteRequestTest) tc).testKCMRoute();
            }
        }));
        suite.addTest(new StopsForRouteRequestTest("testNoShapes", new TestMethod()
        {
            public void run(TestCase tc) {
                ((StopsForRouteRequestTest) tc).testNoShapes();
            }
        }));

        return suite;
    }

    public void testKCMRoute() {
        ObaStopsForRouteRequest.Builder builder = new ObaStopsForRouteRequest.Builder(getContext(), "1_44");
        ObaStopsForRouteRequest request = builder.build();
        ObaStopsForRouteResponse response = (ObaStopsForRouteResponse) request.call();
        assertOK(response);
        final Vector /*List<ObaStop>*/stops = response.getStops();
        assertNotNull(stops);
        assertTrue(stops.size() > 0);
        // Check one
        final ObaStop stop = (ObaStop) stops.elementAt(0);
        assertEquals("1_10911", stop.getId());
        final ObaStopGrouping[] groupings = response.getStopGroupings();
        assertNotNull(groupings);

        final ObaShape[] shapes = response.getShapes();
        assertNotNull(shapes);
        assertTrue(shapes.length > 0);
    }

    public void testNoShapes() {
        ObaStopsForRouteResponse response =
            (ObaStopsForRouteResponse) new ObaStopsForRouteRequest.Builder(getContext(), "1_44").setIncludeShapes(false).build().call();
        assertOK(response);
        final ObaShape[] shapes = response.getShapes();
        assertNotNull(shapes);
        assertEquals(0, shapes.length);
    }
}
