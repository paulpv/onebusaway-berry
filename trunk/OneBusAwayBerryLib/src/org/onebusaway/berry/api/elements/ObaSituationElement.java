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
import org.onebusaway.berry.api.ObaListString;
import org.onebusaway.berry.api.TextUtils;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaSituationElement implements ObaSituation, JSONReceivable {
    public static final ObaSituationElement   EMPTY_OBJECT = new ObaSituationElement();
    public static final ObaSituationElement[] EMPTY_ARRAY  = new ObaSituationElement[] {};

    public static final class Text {
        private final String value;

        //private final String lang;

        Text() {
            value = "";
            //lang = "";
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

        //@Override
        public ObaListString getStopIds() {
            return StopId.toList(stops);
        }

        //@Override
        public VehicleJourney[] getVehicleJourneys() {
            return vehicleJourneys;
        }
    }

    public static final class ConditionDetailsElement implements ConditionDetails, JSONReceivable {

        private String[]        diversionStopIds;
        private ObaShapeElement diversionPath;

        ConditionDetailsElement() {
            diversionStopIds = ObaApi.EMPTY_ARRAY_STRING;
            diversionPath = ObaShapeElement.EMPTY_OBJECT;
        }

        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
            JSONArray jsonDiversionStopIds = json.getJSONArray("diversionStopIds");
            diversionStopIds = ObaApi.fromJSON(jsonDiversionStopIds, new String[jsonDiversionStopIds.length()]);

            diversionPath = (ObaShapeElement) ObaApi.fromJSON(json, "diversionPath", new ObaShapeElement());
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

    public static final class ConsequenceElement implements Consequence, JSONReceivable {
        public static final ConsequenceElement[] EMPTY_ARRAY = new ConsequenceElement[] {};

        private String                           condition;
        private ConditionDetailsElement          conditionDetails;

        ConsequenceElement() {
            condition = "";
            conditionDetails = null;
        }

        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
            condition = json.getString("condition");
            JSONObject jsonConditionDetails = json.getJSONObject("conditionDetails");
            conditionDetails = new ConditionDetailsElement();
            conditionDetails.fromJSON(jsonConditionDetails);
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

    private String               id;
    private Text                 summary;
    private Text                 description;
    private Text                 advice;
    private String               equipmentReason;
    private String               environmentReason;
    private String               personnelReason;
    private String               miscellaneousReason;
    private String               undefinedReason;
    //private String securityAlert;
    private long                 creationTime;
    private AffectsElement       affects;
    private ConsequenceElement[] consequences;

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

    public void fromJSON(JSONObject jsonSituation) {
        // TODO:(pv) fromJSON Auto-generated method stub

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
        }
        else if (!TextUtils.isEmpty(environmentReason)) {
            return environmentReason;
        }
        else if (!TextUtils.isEmpty(personnelReason)) {
            return personnelReason;
        }
        else if (!TextUtils.isEmpty(miscellaneousReason)) {
            return miscellaneousReason;
        }
        else {
            return undefinedReason;
        }
    }

    //@Override
    public String getReasonType() {
        if (!TextUtils.isEmpty(equipmentReason)) {
            return ObaSituation.REASON_TYPE_EQUIPMENT;
        }
        else if (!TextUtils.isEmpty(environmentReason)) {
            return ObaSituation.REASON_TYPE_ENVIRONMENT;
        }
        else if (!TextUtils.isEmpty(personnelReason)) {
            return ObaSituation.REASON_TYPE_PERSONNEL;
        }
        else if (!TextUtils.isEmpty(miscellaneousReason)) {
            return ObaSituation.REASON_TYPE_MISCELLANEOUS;
        }
        else {
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
