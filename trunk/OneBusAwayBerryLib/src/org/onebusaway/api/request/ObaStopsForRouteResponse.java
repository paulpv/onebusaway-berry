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
package org.onebusaway.api.request;

import java.util.Vector;

import org.onebusaway.api.JSONReceivable;
import org.onebusaway.api.ObaApi;
import org.onebusaway.api.elements.ObaReferences;
import org.onebusaway.api.elements.ObaShape;
import org.onebusaway.api.elements.ObaShapeElement;
import org.onebusaway.api.elements.ObaStopGrouping;
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

        private String[] stopIds;
        private ObaStopGrouping[] stopGroupings;
        private ObaShapeElement[] polylines;

        Entry() {
            stopIds = new String[] {};
            stopGroupings = ObaStopGrouping.EMPTY_ARRAY;
            polylines = ObaShapeElement.EMPTY_ARRAY;
        }

        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException
        {
            JSONArray jsonStopIds = json.getJSONArray("stopIds");
            stopIds = new String[jsonStopIds.length()];
            ObaApi.copyTo(jsonStopIds, stopIds);
            
            JSONArray jsonStopGroupings = json.getJSONArray("stopGroupings");
            stopGroupings = new ObaStopGrouping[jsonStopGroupings.length()];
            ObaApi.copyTo(jsonStopIds, stopGroupings, ObaStopGrouping.class);

            JSONArray jsonPolylines = json.getJSONArray("polylines");
            polylines = new ObaShapeElement[jsonPolylines.length()];
            ObaApi.copyTo(jsonStopIds, polylines, ObaShapeElement.class);
        }
    }

    private Entry entry;

    private ObaStopsForRouteResponse() {
        entry = Entry.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException
    {
        entry = new Entry();
        ObaApi.fromJSON(json, "entry", entry);
    }
    
    /**
     * Returns the list of dereferenced stops.
     */
    public Vector getStops() {
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

    protected ObaReferences getRefs() {
        return references;
    }
}