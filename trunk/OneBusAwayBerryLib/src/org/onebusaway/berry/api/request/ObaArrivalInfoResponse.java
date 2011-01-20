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
import org.onebusaway.berry.api.ObaListObaSituation;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.elements.ObaArrivalInfo;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaArrivalInfoRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaArrivalInfoResponse extends ObaResponseWithRefs {
    private static final class Entry implements JSONReceivable {
        private static final Entry EMPTY_OBJECT = new Entry();

        private String stopId;
        private ObaArrivalInfo[] arrivalsAndDepartures;
        private String[] nearbyStopIds;
        private String[] situationIds;

        public Entry() {
            stopId = "";
            arrivalsAndDepartures = ObaArrivalInfo.EMPTY_ARRAY;
            nearbyStopIds = new String[] {};
            situationIds = new String[] {};
        }
        
        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
            stopId = json.getString("stopId");
            
            JSONArray jsonArrivalsAndDepartures = json.getJSONArray("arrivalsAndDepartures");
            arrivalsAndDepartures = (ObaArrivalInfo[]) ObaApi.fromJSON(jsonArrivalsAndDepartures, new ObaArrivalInfo[jsonArrivalsAndDepartures.length()], ObaArrivalInfo.class);
            
            JSONArray jsonNearbyStopIds = json.getJSONArray("nearbyStopIds");
            nearbyStopIds = ObaApi.fromJSON(jsonNearbyStopIds, new String[jsonNearbyStopIds.length()]);

            JSONArray jsonSituationIds = json.getJSONArray("situationIds");
            situationIds = ObaApi.fromJSON(jsonSituationIds, new String[jsonSituationIds.length()]);
        }
    }

    private Entry entry;

    public ObaArrivalInfoResponse() {
        entry = Entry.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        entry = (Entry) ObaApi.fromJSON(json, "entry", new Entry());
    }
    
    /**
     * @return The stop information for this arrival info.
     */
    public ObaStop getStop() {
        return references.getStop(entry.stopId);
    }

    /**
     * @return The list of nearby stops.
     */
    public ObaListObaStop getNearbyStops() {
        return references.getStops(entry.nearbyStopIds);
    }

    public ObaArrivalInfo[] getArrivalInfo() {
        return entry.arrivalsAndDepartures;
    }

    public ObaListObaSituation getSituations() {
        return references.getSituations(entry.situationIds);
    }

    /*
    @Override
    protected ObaReferences getRefs() {
        return data.references;
    }
    */
}
