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
package org.onebusaway.berry.api.request;

import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.berry.api.elements.ObaStopElement;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;


/**
 * Response object for ObaStopsForLocation objects.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaStopsForLocationResponse extends ObaResponseWithRefs {

    private ObaStopElement[] list;
    private boolean outOfRange;
    private boolean limitExceeded;
    
    public ObaStopsForLocationResponse() {
        list = ObaStopElement.EMPTY_ARRAY;
        outOfRange = false;
        limitExceeded = false;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException
    {
        JSONArray jsonList = json.getJSONArray("list");
        list = new ObaStopElement[jsonList.length()];
        ObaApi.copyTo(jsonList, list, ObaStopElement.class);

        outOfRange = json.getBoolean("outOfRange");
        limitExceeded = json.getBoolean("limitExceeded");
    }
    
    /**
     * @return The list of stops.
     */
    public ObaStop[] getStops() {
        return list;
    }

    /**
     * @return Whether the request is out of range of the coverage area.
     */
    public boolean getOutOfRange() {
        return outOfRange;
    }

    /**
     * @return Whether the results exceeded the limits of the response.
     */
    public boolean getLimitExceeded() {
        return limitExceeded;
    }
}
