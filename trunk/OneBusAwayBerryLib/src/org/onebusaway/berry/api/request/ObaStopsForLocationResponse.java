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

import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
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

    private static final class Data {
        private static final Data          EMPTY_OBJECT = new Data();

        private final ObaReferencesElement references;
        private final ObaStopElement[]     list;
        private final boolean              outOfRange;
        private final boolean              limitExceeded;

        private Data() {
            references = ObaReferencesElement.EMPTY_OBJECT;
            list = ObaStopElement.EMPTY_ARRAY;
            outOfRange = false;
            limitExceeded = false;
        }

        public Data(JSONObject json) throws JSONException {
            this.references = new ObaReferencesElement(json.getJSONObject("references"));
            JSONArray list = json.getJSONArray("list");
            this.list = new ObaStopElement[list.length()];
            for (int i = 0; i < this.list.length; i++) {
                this.list[i] = new ObaStopElement(list.getJSONObject(i));
            }
            this.outOfRange = json.getBoolean("outOfRange");
            this.limitExceeded = json.getBoolean("limitExceeded");
        }
    }

    private final Data data;

    private ObaStopsForLocationResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaStopsForLocationResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaStopsForLocationResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    /**
     * @return The list of stops.
     */
    public ObaStop[] getStops() {
        return data.list;
    }

    /**
     * @return Whether the request is out of range of the coverage area.
     */
    public boolean getOutOfRange() {
        return data.outOfRange;
    }

    /**
     * @return Whether the results exceeded the limits of the response.
     */
    public boolean getLimitExceeded() {
        return data.limitExceeded;
    }

    //@Override
    protected ObaReferences getRefs() {
        return data.references;
    }
}
