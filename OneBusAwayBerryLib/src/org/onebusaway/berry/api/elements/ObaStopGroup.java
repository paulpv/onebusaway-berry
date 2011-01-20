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
package org.onebusaway.berry.api.elements;

import org.onebusaway.berry.api.JSONReceivable;
import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaStopGroup implements JSONReceivable {
    public static final ObaStopGroup   EMPTY_OBJECT = new ObaStopGroup();
    public static final ObaStopGroup[] EMPTY_ARRAY  = new ObaStopGroup[] {};

    private static final class StopGroupName implements JSONReceivable {
        private static final StopGroupName EMPTY_OBJECT = new StopGroupName();

        private String                     type;
        private String[]                   names;

        public StopGroupName() {
            type = "";
            names = new String[] {};
        }

        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
            type = json.getString("type");
            JSONArray jsonNames = json.getJSONArray("names");
            names = ObaApi.fromJSON(jsonNames, new String[jsonNames.length()]);
        }

        String getType() {
            return type;
        }

        String[] getNames() {
            return names;
        }
    }

    private String[]           stopIds;
    private ObaShapeElement[]  polylines;
    private StopGroupName      name;

    public static final String TYPE_DESTINATION = "destination";

    /**
     * Constructor.
     */
    public ObaStopGroup() {
        stopIds = ObaApi.EMPTY_ARRAY_STRING;
        polylines = ObaShapeElement.EMPTY_ARRAY;
        name = StopGroupName.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        JSONArray jsonStopIds = json.getJSONArray("stopIds");
        stopIds = ObaApi.fromJSON(jsonStopIds, new String[jsonStopIds.length()]);

        JSONArray jsonPolylines = json.getJSONArray("polylines");
        polylines = (ObaShapeElement[]) ObaApi.fromJSON(jsonPolylines, new ObaShapeElement[jsonPolylines.length()], ObaShapeElement.class);

        name = (StopGroupName) ObaApi.fromJSON(json, "name", new StopGroupName());
    }

    /**
     * Returns the type of grouping.
     *
     * @return One of the TYPE_* string constants.
     */
    public String getType() {
        return name.getType();
    }

    /**
     * Returns the name of this grouping, or the empty string.
     *
     * @return The name of this grouping, or the empty string.
     */
    public String getName() {
        if (name == null) {
            return "";
        }
        String[] names = name.getNames();
        if (names.length > 0) {
            return names[0];
        }
        return "";
    }

    /**
     * Returns a list of StopIDs for this grouping.
     *
     * @return The stop IDs for this grouping.
     */
    public String[] getStopIds() {
        return stopIds;
    }

    /**
     * Returns the array of shapes.
     *
     * @return The array of shapes, or an empty array.
     */
    public ObaShape[] getShapes() {
        return polylines;
    }

    /*
    @Override
    public String toString() {
        return ObaApi.getGson().toJson(this);
    }
    */
}
