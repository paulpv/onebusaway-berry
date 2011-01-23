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

import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
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
public final class ObaStopsForRouteResponse extends ObaResponseWithRefs {

    private static final class Entry {
        private static final Entry      EMPTY_OBJECT = new Entry();

        private final String[]          stopIds;
        private final ObaStopGrouping[] stopGroupings;
        private final ObaShapeElement[] polylines;

        Entry() {
            stopIds = new String[] {};
            stopGroupings = ObaStopGrouping.EMPTY_ARRAY;
            polylines = ObaShapeElement.EMPTY_ARRAY;
        }

        public Entry(JSONObject json) throws JSONException {
            JSONArray stopIds = json.getJSONArray("stopIds");
            this.stopIds = new String[stopIds.length()];
            for (int i = 0; i < this.stopIds.length; i++) {
                this.stopIds[i] = stopIds.getString(i);
            }

            JSONArray stopGroupings = json.getJSONArray("stopGroupings");
            this.stopGroupings = new ObaStopGrouping[stopGroupings.length()];
            for (int i = 0; i < this.stopGroupings.length; i++) {
                this.stopGroupings[i] = new ObaStopGrouping(stopGroupings.getJSONObject(i));
            }

            JSONArray polylines = json.getJSONArray("polylines");
            this.polylines = new ObaShapeElement[polylines.length()];
            for (int i = 0; i < this.polylines.length; i++) {
                this.polylines[i] = new ObaShapeElement(polylines.getJSONObject(i));
            }
        }
    }

    private static final class Data {
        private static final Data          EMPTY_OBJECT = new Data();

        private final ObaReferencesElement references;
        private final Entry                entry;

        Data() {
            references = ObaReferencesElement.EMPTY_OBJECT;
            entry = Entry.EMPTY_OBJECT;
        }

        public Data(JSONObject json) throws JSONException {
            references = new ObaReferencesElement(json.getJSONObject("references"));
            entry = new Entry(json.getJSONObject("entry"));
        }

    }

    private final Data data;

    private ObaStopsForRouteResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaStopsForRouteResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaStopsForRouteResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    /**
     * Returns the list of dereferenced stops.
     */
    public ObaListObaStop getStops() {
        return data.references.getStops(data.entry.stopIds);
    }

    /**
     * @return The list of shapes, if they exist; otherwise returns an empty list.
     */
    public ObaShape[] getShapes() {
        return data.entry.polylines;
    }

    /**
     * @return Returns a collection of stops grouped into useful collections.
     */
    public ObaStopGrouping[] getStopGroupings() {
        return data.entry.stopGroupings;
    }

    //@Override
    protected ObaReferences getRefs() {
        return data.references;
    }
}
