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

import org.onebusaway.berry.api.request.ObaCurrentTimeRequest;
import org.onebusaway.berry.api.request.ObaCurrentTimeResponse;
import org.onebusaway.berry.test.ObaTestCase;

public class CurrentTimeRequestTest extends ObaTestCase
{
    public CurrentTimeRequestTest() {
        super();
    }

    public CurrentTimeRequestTest(String testName, TestMethod testMethod) {
        super(testName, testMethod);
    }
    
    public Test suite() {
        TestSuite suite = new TestSuite("CurrentTimeRequestTest");

        suite.addTest(new CurrentTimeRequestTest("testCurrentTime", new TestMethod()
        { public void run(TestCase tc) {((CurrentTimeRequestTest)tc).testCurrentTime(); } }));
        suite.addTest(new CurrentTimeRequestTest("testNewRequest", new TestMethod()
        { public void run(TestCase tc) {((CurrentTimeRequestTest)tc).testNewRequest(); } }));

        return suite;
    }
    
    public void testCurrentTime() {
        ObaCurrentTimeRequest.Builder builder = new ObaCurrentTimeRequest.Builder(getContext());
        ObaCurrentTimeRequest request = builder.build();
        ObaCurrentTimeResponse response = (ObaCurrentTimeResponse) request.call();
        assertOK(response);
        final long time = response.getTime();
        assertTrue(time > 0);

        final String readableTime = response.getReadableTime();
        assertNotNull(readableTime);
    }

    public void testNewRequest() {
        // This is just to make sure we copy and call newRequest() at least once
        ObaCurrentTimeRequest request = ObaCurrentTimeRequest.newRequest(getContext());
        assertNotNull(request);
    }
}
