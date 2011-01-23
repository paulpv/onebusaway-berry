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

import org.onebusaway.berry.api.ObaListObaSituation;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.elements.ObaArrivalInfo;
import org.onebusaway.berry.api.elements.ObaReferences;
import org.onebusaway.berry.api.elements.ObaReferencesElement;
import org.onebusaway.berry.api.elements.ObaStop;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaArrivalInfoRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaArrivalInfoResponse extends ObaResponseWithRefs {
    private static final class Entry {
        private static final Entry     EMPTY_OBJECT = new Entry();

        private final String           stopId;
        private final ObaArrivalInfo[] arrivalsAndDepartures;
        private final String[]         nearbyStopIds;
        private final String[]         situationIds;

        private Entry() {
            stopId = "";
            arrivalsAndDepartures = ObaArrivalInfo.EMPTY_ARRAY;
            nearbyStopIds = new String[] {};
            situationIds = new String[] {};
        }

        public Entry(JSONObject json) throws JSONException {
            this.stopId = json.getString("stopId");

            JSONArray arrivalsAndDepartures = json.getJSONArray("arrivalsAndDepartures");
            this.arrivalsAndDepartures = new ObaArrivalInfo[arrivalsAndDepartures.length()];
            for (int i = 0; i < this.arrivalsAndDepartures.length; i++) {
                this.arrivalsAndDepartures[i] = new ObaArrivalInfo(arrivalsAndDepartures.getJSONObject(i));
            }

            JSONArray nearbyStopIds = json.getJSONArray("nearbyStopIds");
            this.nearbyStopIds = new String[nearbyStopIds.length()];
            for (int i = 0; i < this.nearbyStopIds.length; i++) {
                this.nearbyStopIds[i] = nearbyStopIds.getString(i);
            }

            JSONArray situationIds = json.getJSONArray("situationIds");
            this.situationIds = new String[situationIds.length()];
            for (int i = 0; i < this.situationIds.length; i++) {
                this.situationIds[i] = situationIds.getString(i);
            }
        }
    }

    private static final class Data {
        private static final Data          EMPTY_OBJECT = new Data();

        private final ObaReferencesElement references;
        private final Entry                entry;

        private Data() {
            references = ObaReferencesElement.EMPTY_OBJECT;
            entry = Entry.EMPTY_OBJECT;
        }

        public Data(JSONObject json) throws JSONException {
            references = new ObaReferencesElement(json.getJSONObject("references"));
            entry = new Entry(json.getJSONObject("entry"));
        }
    }

    private final Data data;

    ObaArrivalInfoResponse() {
        super();
        data = Data.EMPTY_OBJECT;
    }

    public ObaArrivalInfoResponse(int obaErrorCode, Throwable err) {
        super(obaErrorCode, err);
        data = Data.EMPTY_OBJECT;
    }

    public ObaArrivalInfoResponse(JSONObject json) throws JSONException {
        super(json);
        data = new Data(json.getJSONObject("data"));
    }

    /**
     * @return The stop information for this arrival info.
     */
    public ObaStop getStop() {
        return data.references.getStop(data.entry.stopId);
    }

    /**
     * @return The list of nearby stops.
     */
    public ObaListObaStop getNearbyStops() {
        return data.references.getStops(data.entry.nearbyStopIds);
    }

    public ObaArrivalInfo[] getArrivalInfo() {
        return data.entry.arrivalsAndDepartures;
    }

    public ObaListObaSituation getSituations() {
        return data.references.getSituations(data.entry.situationIds);
    }

    //@Override
    protected ObaReferences getRefs() {
        return data.references;
    }
}
