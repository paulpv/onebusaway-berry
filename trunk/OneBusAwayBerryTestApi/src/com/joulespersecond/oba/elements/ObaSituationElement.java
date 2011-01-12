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
package com.joulespersecond.oba.elements;

import net.rim.device.api.collection.List;
import net.rim.device.api.util.Arrays;

import com.joulespersecond.oba.TextUtils;

public final class ObaSituationElement implements ObaSituation {
    public static final ObaSituationElement EMPTY_OBJECT = new ObaSituationElement();
    public static final ObaSituationElement[] EMPTY_ARRAY = new ObaSituationElement[] {};

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

        private final String stopId;

        public StopId() {
            stopId = null;
        }

        public String getId() {
            return stopId;
        }

        public static List toList(StopId[] ids) {
            ArrayList result = new ArrayList(ids.length);
            StopId id;
            for (int i=0; i < ids.length; i++) {
                id = ids[i];
                result.add(id.getId());
            }
            return result;
        }
    }

    public static final class VehicleJourneyElement implements VehicleJourney {
        public static final VehicleJourneyElement[] EMPTY_ARRAY = new VehicleJourneyElement[] {};

        private final String direction;
        private final String lineId; // routeId
        private final StopId[] calls;

        VehicleJourneyElement() {
            direction = "";
            lineId = "";
            calls = StopId.EMPTY_ARRAY;
        }

        public String getDirection() {
            return direction;
        }

        public String getRouteId() {
            return lineId;
        }

        public List getStopIds() {
            return StopId.toList(calls);
        }
    }

    public static final class AffectsElement implements Affects {
        public static final AffectsElement EMPTY_OBJECT = new AffectsElement();

        private final StopId[] stops;
        private final VehicleJourneyElement[] vehicleJourneys;

        AffectsElement() {
            stops = StopId.EMPTY_ARRAY;
            vehicleJourneys = VehicleJourneyElement.EMPTY_ARRAY;
        }

        public List getStopIds() {
            return StopId.toList(stops);
        }

        public VehicleJourney[] getVehicleJourneys() {
            return vehicleJourneys;
        }
    }

    public static final class ConditionDetailsElement
            implements ConditionDetails {

        private final String[] diversionStopIds;
        private final ObaShapeElement diversionPath;

        ConditionDetailsElement() {
            diversionStopIds = null;
            diversionPath = null;
        }

        public ObaShape getDiversionPath() {
            return diversionPath;
        }

        public List getDiversionStopIds() {
            return Arrays.asList(diversionStopIds);
        }

    }

    public static final class ConsequenceElement implements Consequence {
        public static final ConsequenceElement[] EMPTY_ARRAY = new ConsequenceElement[] {};

        private final String condition;
        private final ConditionDetailsElement conditionDetails;

        ConsequenceElement() {
            condition = "";
            conditionDetails = null;
        }

        public String getCondition() {
            return condition;
        }

        public ConditionDetails getDetails() {
            return conditionDetails;
        }
    }


    private final String id;
    private final Text summary;
    private final Text description;
    private final Text advice;
    private final String equipmentReason;
    private final String environmentReason;
    private final String personnelReason;
    private final String miscellaneousReason;
    private final String undefinedReason;
    //private final String securityAlert;
    private final long creationTime;
    private final AffectsElement affects;
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

    public String getId() {
        return id;
    }

    public String getSummary() {
        return (summary != null) ? summary.getValue() : null;
    }

    public String getDescription() {
        return (description != null) ? description.getValue() : null;
    }

    public String getAdvice() {
        return (advice != null) ? advice.getValue() : null;
    }

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

    public long getCreationTime() {
        return creationTime;
    }

    public Affects getAffects() {
        return affects;
    }

    public Consequence[] getConsequences() {
        return consequences;
    }
}
