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

import org.onebusaway.berry.api.JSONReceivable;
import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.elements.ObaShape;
import org.onebusaway.berry.api.elements.ObaShapeElement;
import org.onebusaway.berry.api.elements.ObaStopGrouping;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaStopForRouteRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaStopsForRouteResponse extends ObaResponseWithRefs implements JSONReceivable {

    private static final class Entry implements JSONReceivable {
        private static final Entry EMPTY_OBJECT = new Entry();

        private String[]           stopIds;
        private ObaStopGrouping[]  stopGroupings;
        private ObaShapeElement[]  polylines;

        public Entry() {
            stopIds = new String[] {};
            stopGroupings = ObaStopGrouping.EMPTY_ARRAY;
            polylines = ObaShapeElement.EMPTY_ARRAY;
        }

        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
            JSONArray jsonStopIds = json.getJSONArray("stopIds");
            stopIds = ObaApi.fromJSON(jsonStopIds, new String[jsonStopIds.length()]);

            JSONArray jsonStopGroupings = json.getJSONArray("stopGroupings");
            stopGroupings = (ObaStopGrouping[]) ObaApi.fromJSON(jsonStopGroupings, new ObaStopGrouping[jsonStopGroupings.length()], ObaStopGrouping.class);

            JSONArray jsonPolylines = json.getJSONArray("polylines");
            polylines = (ObaShapeElement[]) ObaApi.fromJSON(jsonPolylines, new ObaShapeElement[jsonPolylines.length()], ObaShapeElement.class);
        }
    }

    private Entry entry;

    public ObaStopsForRouteResponse() {
        entry = Entry.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        entry = (Entry) ObaApi.fromJSON(json, "entry", new Entry());
    }

    /**
     * Returns the list of dereferenced stops.
     */
    public ObaListObaStop getStops() {
        return references.getStops(entry.stopIds);
    }

    /**
     * @return The list of shapes, if they exist; otherwise returns an empty list.
     */
    public ObaShape[] getShapes() {
        return entry.polylines;
    }

    /**
     * @return Returns a collection of stops grouped into useful collections.
     */
    public ObaStopGrouping[] getStopGroupings() {
        return entry.stopGroupings;
    }

    /*
    @Override
    protected ObaReferences getRefs() {
        return data.references;
    }
    */
}
