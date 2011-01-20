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
import j2meunit.framework.TestSuite;

import org.onebusaway.berry.api.ObaListObaRoute;
import org.onebusaway.berry.api.ObaListObaSituation;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.ObaListString;
import org.onebusaway.berry.api.elements.ObaAgency;
import org.onebusaway.berry.api.elements.ObaArrivalInfo;
import org.onebusaway.berry.api.elements.ObaRoute;
import org.onebusaway.berry.api.elements.ObaShape;
import org.onebusaway.berry.api.elements.ObaSituation;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.request.ObaArrivalInfoRequest;
import org.onebusaway.berry.api.request.ObaArrivalInfoResponse;
import org.onebusaway.berry.test.ObaTestCase;


public class ArrivalInfoRequestTest extends ObaTestCase {

    public ArrivalInfoRequestTest() {
        super();
    }

    public ArrivalInfoRequestTest(String testName) {
        super(testName);
    }
    
    public Test suite() {
        TestSuite suite = new TestSuite("ArrivalInfoRequestTest");

        suite.addTest(new ArrivalInfoRequestTest("testKCMStop")
        {
            public void runTest() {
                testKCMStop();
            }
        });
        suite.addTest(new ArrivalInfoRequestTest("testNewRequest")
        {
            public void runTest() {
                testNewRequest();
            }
        });
        suite.addTest(new ArrivalInfoRequestTest("testStopSituation")
        {
            public void runTest() {
                testStopSituation();
            }
        });
        suite.addTest(new ArrivalInfoRequestTest("testTripSituation")
        {
            public void runTest() {
                testTripSituation();
            }
        });

        return suite;
    }
    
    public void testKCMStop() {
        ObaArrivalInfoRequest.Builder builder =
            new ObaArrivalInfoRequest.Builder(getContext(), "1_29261");
        ObaArrivalInfoRequest request = builder.build();
        ObaArrivalInfoResponse response = (ObaArrivalInfoResponse) request.call();
        assertOK(response);
        ObaStop stop = response.getStop();
        assertNotNull(stop);
        assertEquals("1_29261", stop.getId());
        final ObaListObaRoute routes = response.getRoutes(stop.getRouteIds());
        assertTrue(routes.size() > 0);
        ObaAgency agency = response.getAgency(((ObaRoute)routes.elementAt(0)).getAgencyId());
        assertEquals("1", agency.getId());

        final ObaArrivalInfo[] arrivals = response.getArrivalInfo();
        // Uhh, this will vary considerably depending on when this is run.
        assertNotNull(arrivals);

        final ObaListObaStop nearbyStops = response.getNearbyStops();
        assertTrue(nearbyStops.size() > 0);
    }

    public void testNewRequest() {
        // This is just to make sure we copy and call newRequest() at least once
        ObaArrivalInfoRequest request = ObaArrivalInfoRequest.newRequest(getContext(), "1_10");
        assertNotNull(request);
    }

    public void testStopSituation() {
        ObaArrivalInfoRequest.Builder builder =
            new ObaArrivalInfoRequest.Builder(getContext(), "1_75403");
        builder.setServer("soak-api.onebusaway.org");
        builder.setApiKey("TEST");
        ObaArrivalInfoRequest request = builder.build();
        ObaArrivalInfoResponse response = (ObaArrivalInfoResponse) request.call();
        assertOK(response);
        ObaListObaSituation situations = response.getSituations();
        assertNotNull(situations);
        //assertTrue(situations.size() > 0);
        if (situations.size() > 0)
        {
            // This is all test data, we really shouldn't depend on it.
            // This is why we need a way of inserting canned data for testing.
            ObaSituation situation = (ObaSituation) situations.elementAt(0);
            assertEquals("Stop Moved", situation.getSummary());
            assertEquals("", situation.getReason());
            assertEquals(ObaSituation.REASON_TYPE_UNDEFINED, situation.getReasonType());
    
            ObaSituation.Affects affects = situation.getAffects();
            assertNotNull(affects);
            ObaListString affectedStops = affects.getStopIds();
            assertNotNull(affectedStops);
            assertEquals(1, affectedStops.size());
            assertEquals("1_75403", affectedStops.elementAt(0));
            //ObaSituation.VehicleJourney[] journeys = affects.getVehicleJourneys();
            //assertNotNull(journeys);
            //ObaSituation.VehicleJourney journey = journeys[0];
            //assertNotNull("1", journey.getDirection());
            //assertNotNull("1_30", journey.getRouteId());
            //List<String> journeyStops = journey.getStopIds();
            //assertNotNull(journeyStops);
            //assertTrue(journeyStops.size() > 0);
            //assertEquals("1_9980", journeyStops.get(0));
    
            //ObaSituation.Consequence[] consequences = situation.getConsequences();
            //assertNotNull(consequences);
            //assertEquals(1, consequences.length);
            //assertEquals("diversion", consequences[0].getCondition());
        }
    }

    public void testTripSituation() {
        ObaArrivalInfoRequest.Builder builder =
            new ObaArrivalInfoRequest.Builder(getContext(), "1_10020");
        builder.setServer("soak-api.onebusaway.org");
        builder.setApiKey("TEST");
        ObaArrivalInfoRequest request = builder.build();
        ObaArrivalInfoResponse response = (ObaArrivalInfoResponse) request.call();
        assertOK(response);
        ObaArrivalInfo[] infoList = response.getArrivalInfo();
        assertNotNull(infoList);
        assertEquals(1, infoList.length);
        ObaArrivalInfo info = infoList[0];
        String[] situationIds = info.getSituationIds();
        assertNotNull(situationIds);
        ObaListObaSituation situations = response.getSituations(situationIds);
        assertNotNull(situations);
        //assertEquals(1, situations.size());
        if (situations.size() > 0)
        {
            ObaSituation situation = (ObaSituation) situations.elementAt(0);
            assertEquals("Snow Reroute", situation.getSummary());
            assertEquals("heavySnowFall", situation.getReason());
            assertEquals(ObaSituation.REASON_TYPE_ENVIRONMENT,
                    situation.getReasonType());
    
            ObaSituation.Affects affects = situation.getAffects();
            assertNotNull(affects);
            ObaSituation.VehicleJourney[] journeys = affects.getVehicleJourneys();
            assertNotNull(journeys);
            ObaSituation.VehicleJourney journey = journeys[0];
            assertNotNull("1", journey.getDirection());
            assertNotNull("1_30", journey.getRouteId());
            ObaListString journeyStops = journey.getStopIds();
            assertNotNull(journeyStops);
            assertTrue(journeyStops.size() > 0);
            assertEquals("1_9980", journeyStops.elementAt(0));
    
            ObaSituation.Consequence[] consequences = situation.getConsequences();
            assertNotNull(consequences);
            assertEquals(1, consequences.length);
            ObaSituation.Consequence c = consequences[0];
            assertEquals(ObaSituation.Consequence.CONDITION_DIVERSION,
                    c.getCondition());
            ObaSituation.ConditionDetails details = c.getDetails();
            assertNotNull(details);
            ObaListString stopIds = details.getDiversionStopIds();
            assertNotNull(stopIds);
            assertTrue(stopIds.size() > 0);
            assertEquals("1_9972", stopIds.elementAt(0));
            ObaShape diversion = details.getDiversionPath();
            assertNotNull(diversion);
        }
    }
}
