package org.onebusaway.tests;

//import ObaTestCase;

import org.onebusaway.api.request.ObaCurrentTimeRequest;
import org.onebusaway.api.request.ObaCurrentTimeResponse;
import org.onebusaway.api.request.ObaResponse;

public class CurrentTimeRequestTest// extends ObaTestCase
{
    public void testCurrentTime()
    {
        ObaCurrentTimeRequest.Builder builder = new ObaCurrentTimeRequest.Builder();
        ObaCurrentTimeRequest request = builder.build();
        ObaResponse response = request.call();
        //assertOK(response);
        ObaCurrentTimeResponse currentTime = (ObaCurrentTimeResponse) response.getData();
        //assertOK(currentTime);
        final long time = currentTime.getTime();
        //assertTrue(time > 0);
        final String readableTime = currentTime.getReadableTime();
        //assertNotNull(readableTime);
    }

    public void testNewRequest()
    {
        ObaCurrentTimeRequest request = ObaCurrentTimeRequest.newRequest();
        //assertNotNull(request);
        ObaResponse response = request.call();
        //assertOK(response);
        ObaCurrentTimeResponse currentTime = (ObaCurrentTimeResponse) response.getData();
        //assertOK(currentTime);
        final long time = currentTime.getTime();
        //assertTrue(time > 0);
        final String readableTime = currentTime.getReadableTime();
        //assertNotNull(readableTime);
    }
}