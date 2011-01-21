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
import j2meunit.framework.TestSuite;


public class AllTests extends TestCase {
    public Test suite() {
        TestSuite suite = new TestSuite("OneBusAway.request.AllTests");
        suite.addTest(new ArrivalInfoRequestTest().suite());
        suite.addTest(new AgenciesWithCoverageTest().suite());
        suite.addTest(new CurrentTimeRequestTest().suite());
        //suite.addTest(new ScheduleForStopTest().suite()); // TODO:(pv) Fails: verify if the same in Android.
        suite.addTest(new StopRequestTest().suite());
        suite.addTest(new StopsForLocationTest().suite());
        suite.addTest(new StopsForRouteRequestTest().suite());
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/AgencyRequestTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/FailTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/RouteIdsForAgencyRequestTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/RouteRequestTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/RoutesForLocationTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/ShapeRequestTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/ShapeTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/StopIdsForAgencyRequestTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/TripDetailsRequest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/TripRequestTest.java
        //OneBusAwayBerryTests/src/org/onebusaway/berry/test/api/request/test/TripsForLocationTest.java        
        return suite;
    }
}
