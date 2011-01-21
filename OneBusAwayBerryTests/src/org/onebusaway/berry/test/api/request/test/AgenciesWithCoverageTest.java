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

import org.onebusaway.berry.api.elements.ObaAgency;
import org.onebusaway.berry.api.elements.ObaAgencyWithCoverage;
import org.onebusaway.berry.api.request.ObaAgenciesWithCoverageRequest;
import org.onebusaway.berry.api.request.ObaAgenciesWithCoverageResponse;


public class AgenciesWithCoverageTest extends ObaTestCase {

    public AgenciesWithCoverageTest() {
        super();
    }

    public AgenciesWithCoverageTest(String testName) {
        super(testName);
    }
    
    public Test suite() {
        TestSuite suite = new TestSuite("AgenciesWithCoverageTest");

        suite.addTest(new AgenciesWithCoverageTest("testRequest")
        {
            public void runTest() {
                testRequest();
            }
        });
        suite.addTest(new AgenciesWithCoverageTest("testBuilder")
        {
            public void runTest() {
                testBuilder();
            }
        });

        return suite;
    }
    
    
    public void testRequest() {
        ObaAgenciesWithCoverageRequest request =
            ObaAgenciesWithCoverageRequest.newRequest(getContext());
        ObaAgenciesWithCoverageResponse response = (ObaAgenciesWithCoverageResponse) request.call();
        assertOK(response);
        final ObaAgencyWithCoverage[] list = response.getAgencies();
        assertTrue(list.length > 0);
        for (int i=0; i < list.length; i++) {
            final ObaAgencyWithCoverage agency = list[i];
            final ObaAgency a = response.getAgency(agency.getId());
            assertNotNull(a);
        }
    }

    public void testBuilder() {
        ObaAgenciesWithCoverageRequest.Builder builder =
            new ObaAgenciesWithCoverageRequest.Builder(getContext());
        ObaAgenciesWithCoverageRequest request = builder.build();
        assertNotNull(request);
    }
}
