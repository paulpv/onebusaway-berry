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
package org.onebusaway.api.request.test;

import org.onebusaway.api.request.ObaCurrentTimeRequest;
import org.onebusaway.api.request.ObaCurrentTimeResponse;

public class CurrentTimeRequestTest extends ObaTestCase
{
    public void testCurrentTime()
    {
        ObaCurrentTimeRequest.Builder builder = new ObaCurrentTimeRequest.Builder(getContext());
        ObaCurrentTimeRequest request = builder.build();
        ObaCurrentTimeResponse response = (ObaCurrentTimeResponse) request.call();
        //assertOK(response);
        final long time = response.getTime();
        //assertTrue(time > 0);
        final String readableTime = response.getReadableTime();
        //assertNotNullOrEmpty(readableTime);
    }

    public void testNewRequest()
    {
        ObaCurrentTimeRequest request = ObaCurrentTimeRequest.newRequest(getContext());
        //assertNotNull(request);
    }
}
