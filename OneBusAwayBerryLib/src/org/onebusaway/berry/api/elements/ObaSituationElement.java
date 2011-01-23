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

import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.berry.api.ObaListString;
import org.onebusaway.berry.api.TextUtils;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaSituationElement implements ObaSituation {
    public static final ObaSituationElement   EMPTY_OBJECT = new ObaSituationElement();
    public static final ObaSituationElement[] EMPTY_ARRAY  = new ObaSituationElement[] {};

    public static final class Text {
        private final String value;

        //private final String lang;

        Text() {
            value = "";
            //lang = "";
        }

        public Text(JSONObject json) throws JSONException {
            value = json.getString("value");
            //lang = json.get("lang");
        }

        public String getValue() {
            return value;
        }
    }

    public static final class StopId {
        public static final StopId[] EMPTY_ARRAY = new StopId[] {};

        private final String         stopId;

        public StopId() {
            stopId = null;
        }

        public StopId(JSONObject json) throws JSONException {
            stopId = json.getString("stopId");
        }

        public String getId() {
            return stopId;
        }

        public static ObaListString toList(StopId[] ids) {
            ObaListString result = new ObaListString(ids.length);
            for (int i = 0; i < ids.length; i++) {
                result.addElement(ids[i].getId());
            }
            return result;
        }
    }

    public static final class VehicleJourneyElement implements VehicleJourney {
        public static final VehicleJourneyElement[] EMPTY_ARRAY = new VehicleJourneyElement[] {};

        private final String                        direction;
        private final String                        lineId;                                      // routeId
        private final StopId[]                      calls;

        VehicleJourneyElement() {
            direction = "";
            lineId = "";
            calls = StopId.EMPTY_ARRAY;
        }

        public VehicleJourneyElement(JSONObject json) throws JSONException {
            this.direction = json.getString("direction");
            this.lineId = json.getString("lineId");
            JSONArray calls = json.getJSONArray("calls");
            this.calls = new StopId[calls.length()];
            for (int i = 0; i < this.calls.length; i++) {
                this.calls[i] = new StopId(calls.getJSONObject(i));
            }
        }

        //@Override
        public String getDirection() {
            return direction;
        }

        //@Override
        public String getRouteId() {
            return lineId;
        }

        //@Override
        public ObaListString getStopIds() {
            return StopId.toList(calls);
        }
    }

    public static final class AffectsElement implements Affects {
        public static final AffectsElement    EMPTY_OBJECT = new AffectsElement();

        private final StopId[]                stops;
        private final VehicleJourneyElement[] vehicleJourneys;

        AffectsElement() {
            stops = StopId.EMPTY_ARRAY;
            vehicleJourneys = VehicleJourneyElement.EMPTY_ARRAY;
        }

        public AffectsElement(JSONObject json) throws JSONException {
            JSONArray stops = json.getJSONArray("stops");
            this.stops = new StopId[stops.length()];
            for (int i = 0; i < this.stops.length; i++) {
                this.stops[i] = new StopId(stops.getJSONObject(i));
            }
            JSONArray vehicleJourneys = json.getJSONArray("vehicleJourneys");
            this.vehicleJourneys = new VehicleJourneyElement[vehicleJourneys.length()];
            for (int i = 0; i < this.vehicleJourneys.length; i++) {
                this.vehicleJourneys[i] = new VehicleJourneyElement(vehicleJourneys.getJSONObject(i));
            }
        }

        //@Override
        public ObaListString getStopIds() {
            return StopId.toList(stops);
        }

        //@Override
        public VehicleJourney[] getVehicleJourneys() {
            return vehicleJourneys;
        }
    }

    public static final class ConditionDetailsElement implements ConditionDetails {

        private final String[]        diversionStopIds;
        private final ObaShapeElement diversionPath;

        ConditionDetailsElement() {
            diversionStopIds = new String[] {};
            diversionPath = ObaShapeElement.EMPTY_OBJECT;
        }

        public ConditionDetailsElement(JSONObject json) throws JSONException {
            JSONArray diversionStopIds = json.getJSONArray("diversionStopIds");
            this.diversionStopIds = new String[diversionStopIds.length()];
            for (int i = 0; i < this.diversionStopIds.length; i++) {
                this.diversionStopIds[i] = diversionStopIds.getString(i);
            }
            this.diversionPath = new ObaShapeElement(json.getJSONObject("diversionPath"));
        }

        //@Override
        public ObaShape getDiversionPath() {
            return diversionPath;
        }

        //@Override
        public ObaListString getDiversionStopIds() {
            ObaListString diversionStopIds = new ObaListString(this.diversionStopIds.length);
            for (int i = 0; i < this.diversionStopIds.length; i++) {
                diversionStopIds.addElement(diversionStopIds.elementAt(i));
            }
            return diversionStopIds;
        }
    }

    public static final class ConsequenceElement implements Consequence {
        public static final ConsequenceElement[] EMPTY_ARRAY = new ConsequenceElement[] {};

        private final String                     condition;
        private final ConditionDetailsElement    conditionDetails;

        ConsequenceElement() {
            condition = "";
            conditionDetails = null;
        }

        public ConsequenceElement(JSONObject json) throws JSONException {
            condition = json.getString("condition");
            conditionDetails = new ConditionDetailsElement(json.getJSONObject("conditionDetails"));
        }

        //@Override
        public String getCondition() {
            return condition;
        }

        //@Override
        public ConditionDetails getDetails() {
            return conditionDetails;
        }
    }

    private final String               id;
    private final Text                 summary;
    private final Text                 description;
    private final Text                 advice;
    private final String               equipmentReason;
    private final String               environmentReason;
    private final String               personnelReason;
    private final String               miscellaneousReason;
    private final String               undefinedReason;
    //private final String securityAlert;
    private final long                 creationTime;
    private final AffectsElement       affects;
    private final ConsequenceElement[] consequences;

    ObaSituationElement() {
        id = "";
        summary = null;
        description = null;
        advice = null;
        equipmentReason = null;
        environmentReason = null;
        personnelReason = null;
        miscellaneousReason = null;
        undefinedReason = null;
        //securityAlert = null;
        creationTime = 0;
        affects = AffectsElement.EMPTY_OBJECT;
        consequences = ConsequenceElement.EMPTY_ARRAY;
    }

    public ObaSituationElement(JSONObject json) throws JSONException {
        this.id = json.getString("id");
        this.summary = new Text(json.getJSONObject("summary"));
        this.description = new Text(json.getJSONObject("description"));
        this.advice = new Text(json.getJSONObject("advice"));
        this.equipmentReason = json.getString("equipmentReason");
        this.environmentReason = json.getString("environmentReason");
        this.personnelReason = json.getString("personnelReason");
        this.miscellaneousReason = json.getString("miscellaneousReason");
        this.undefinedReason = json.getString("undefinedReason");
        //this.securityAlert = json.getString("securityAlert");
        this.creationTime = json.getLong("creationTime");
        this.affects = new AffectsElement(json.getJSONObject("affects"));
        JSONArray consequences = json.getJSONArray("consequences");
        this.consequences = new ConsequenceElement[consequences.length()];
        for (int i = 0; i < this.consequences.length; i++) {
            this.consequences[i] = new ConsequenceElement(consequences.getJSONObject(i));
        }
    }

    //@Override
    public String getId() {
        return id;
    }

    //@Override
    public String getSummary() {
        return (summary != null) ? summary.getValue() : null;
    }

    //@Override
    public String getDescription() {
        return (description != null) ? description.getValue() : null;
    }

    //@Override
    public String getAdvice() {
        return (advice != null) ? advice.getValue() : null;
    }

    //@Override
    public String getReason() {
        if (!TextUtils.isEmpty(equipmentReason)) {
            return equipmentReason;
        } else if (!TextUtils.isEmpty(environmentReason)) {
            return environmentReason;
        } else if (!TextUtils.isEmpty(personnelReason)) {
            return personnelReason;
        } else if (!TextUtils.isEmpty(miscellaneousReason)) {
            return miscellaneousReason;
        } else {
            return undefinedReason;
        }
    }

    //@Override
    public String getReasonType() {
        if (!TextUtils.isEmpty(equipmentReason)) {
            return ObaSituation.REASON_TYPE_EQUIPMENT;
        } else if (!TextUtils.isEmpty(environmentReason)) {
            return ObaSituation.REASON_TYPE_ENVIRONMENT;
        } else if (!TextUtils.isEmpty(personnelReason)) {
            return ObaSituation.REASON_TYPE_PERSONNEL;
        } else if (!TextUtils.isEmpty(miscellaneousReason)) {
            return ObaSituation.REASON_TYPE_MISCELLANEOUS;
        } else {
            return ObaSituation.REASON_TYPE_UNDEFINED;
        } 
    }

    //@Override
    public long getCreationTime() {
        return creationTime;
    }

    //@Override
    public Affects getAffects() {
        return affects;
    }

    //@Override
    public Consequence[] getConsequences() {
        return consequences;
    }
}
