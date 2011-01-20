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

public final class ObaStopGrouping implements JSONReceivable {
    public static final ObaStopGrouping   EMPTY_OBJECT   = new ObaStopGrouping();
    public static final ObaStopGrouping[] EMPTY_ARRAY    = new ObaStopGrouping[] {};

    public static final String            TYPE_DIRECTION = "direction";

    private boolean                       ordered;
    private String                        type;
    private ObaStopGroup[]                stopGroups;

    /**
     * Constructor.
     */
    public ObaStopGrouping() {
        ordered = false;
        type = "";
        stopGroups = ObaStopGroup.EMPTY_ARRAY;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        ordered = json.getBoolean("ordered");
        type = json.getString("type");
        JSONArray jsonStopGroups = json.getJSONArray("stopGroups");
        stopGroups = new ObaStopGroup[jsonStopGroups.length()];
        ObaApi.copyTo(jsonStopGroups, stopGroups, ObaStopGroup.class);
    }

    /**
     * Returns whether or not this grouping is ordered.
     * @return A boolean indicating whether this grouping is ordered.
     */
    public boolean getOrdered() {
        return ordered;
    }

    /**
     * Returns the type of ordering.
     * @return The type of ordering.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the list of stop groups.
     *
     * @return The list of stop groups.
     */
    public ObaStopGroup[] getStopGroups() {
        return stopGroups;
    }

    /*
    @Override
    public String toString() {
        return ObaApi.getGson().toJson(this);
    }
    */
}
