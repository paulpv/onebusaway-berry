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

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaArrivalInfo implements JSONReceivable {
    public static final ObaArrivalInfo   EMPTY_OBJECT = new ObaArrivalInfo();
    public static final ObaArrivalInfo[] EMPTY_ARRAY  = new ObaArrivalInfo[] {};

    public static final class Frequency implements JSONReceivable {
        private long startTime;
        private long endTime;
        private long headway;

        public Frequency() {
            startTime = 0;
            endTime = 0;
            headway = 0;
        }

        public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
            startTime = json.getLong("startTime");
            endTime = json.getLong("endTime");
            headway = json.getLong("headway");
        }
        
        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public long getHeadway() {
            return headway;
        }
    }

    private String               routeId;
    private String               routeShortName;
    private String               routeLongName;
    private String               tripId;
    private String               tripHeadsign;
    private String               stopId;
    private long                 predictedArrivalTime;
    private long                 scheduledArrivalTime;
    private long                 predictedDepartureTime;
    private long                 scheduledDepartureTime;
    private String               status;
    private Frequency            frequency;
    private String               vehicleId;
    private Double               distanceFromStop;
    private Integer              numberOfStopsAway;
    private long                 serviceDate;
    private long                 lastUpdateTime;
    private Boolean              predicted;
    private ObaTripStatusElement tripStatus;
    private String[]             situationIds;

    public ObaArrivalInfo() {
        routeId = "";
        routeShortName = "";
        routeLongName = "";
        tripId = "";
        tripHeadsign = "";
        stopId = "";
        predictedArrivalTime = 0;
        scheduledArrivalTime = 0;
        predictedDepartureTime = 0;
        scheduledDepartureTime = 0;
        status = "";
        frequency = null;
        vehicleId = null;
        distanceFromStop = null;
        numberOfStopsAway = null;
        serviceDate = 0;
        lastUpdateTime = 0;
        predicted = null;
        tripStatus = null;
        situationIds = null;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        routeId = json.getString("routeId");
        routeShortName = json.getString("routeShortName");
        routeLongName = json.getString("routeLongName");
        tripId = json.getString("tripId");
        tripHeadsign = json.getString("tripHeadsign");
        stopId = json.getString("stopId");
        predictedArrivalTime = json.getLong("predictedArrivalTime");
        scheduledArrivalTime = json.getLong("scheduledArrivalTime");
        predictedDepartureTime = json.getLong("predictedDepartureTime");
        scheduledDepartureTime = json.getLong("scheduledDepartureTime");
        status = json.getString("status");
        frequency = (Frequency) ObaApi.optJSON(json, "frequency", new Frequency(), null);
        vehicleId = json.optString("vehicleId");
        distanceFromStop = ObaApi.optDouble(json, "distanceFromStop");
        numberOfStopsAway = ObaApi.optInteger(json, "numberOfStopsAway");
        serviceDate = json.getLong("serviceDate");
        lastUpdateTime = json.getLong("lastUpdateTime");
        predicted = ObaApi.optBoolean(json, "predicted");
        tripStatus = (ObaTripStatusElement) ObaApi.optJSON(json, "tripStatus", new ObaTripStatusElement(), null);
        JSONArray jsonSituationIds = json.getJSONArray("situationIds");
        situationIds = ObaApi.optJSON(jsonSituationIds, new String[jsonSituationIds.length()], null);
    }
    
    /**
     * @return The ID of the route.
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * @return The short name of the route.
     */
    public String getShortName() {
        return routeShortName;
    }

    /**
     * @return The long name of the route.
     */
    public String getRouteLongName() {
        return routeLongName;
    }

    /**
     * @return The trip ID of the route.
     */
    public String getTripId() {
        return tripId;
    }

    /**
     * @return The trip headsign.
     */
    public String getHeadsign() {
        return tripHeadsign;
    }

    /**
     * @return The stop ID.
     */
    public String getStopId() {
        return stopId;
    }

    /**
     * @return The scheduled arrival time.
     */
    public long getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    /**
     * @return The predicted arrival time, or 0.
     */
    public long getPredictedArrivalTime() {
        return predictedArrivalTime;
    }

    /**
     * @return The scheduled departure time.
     */
    public long getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    /**
     * @return The predicted arrival time, or 0.
     */
    public long getPredictedDepartureTime() {
        return predictedDepartureTime;
    }

    /**
     * @return The status of the route.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return The frequency of the trip, for frequency-based scheduling. For
     *         time-based schedules, this is null.
     */
    public Frequency getFrequency() {
        return frequency;
    }

    /**
     * @return The vehicle ID of the trip, or null if it is not provided.
     */
    public String getVehicleId() {
        return vehicleId;
    }

    /**
     * @return The distance, in meters, of the transit vehicle from the stop,
     * or null if it is not provided.
     */
    public Double getDistanceFromStop() {
        return distanceFromStop;
    }

    /**
     * @return The number of stops between the transit vehicle and the current stop.
     */
    public Integer getNumberOfStopsAway() {
        return numberOfStopsAway;
    }

    /**
     * @return The midnight-based start time of the day of service of which a trip is
     * operating, in Unix time, or 0 if this is not provided.
     */
    public long getServiceDate() {
        return serviceDate;
    }

    /**
     * @return
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @return Whether this arrival has prediction information. If the 'predicted'
     * value is set, then that is used; otherwise it is inferred from the existence
     * of a non-zero predicted start or end time.
     */
    public boolean getPredicted() {
        return (predicted != null) ? predicted.booleanValue() : (predictedDepartureTime != 0);
    }

    /**
     * @return The trip status, if it exists.
     */
    public ObaTripStatus getTripStatus() {
        return tripStatus;
    }

    /**
     * @return The array of situation IDs, or null.
     */
    public String[] getSituationIds() {
        return situationIds;
    }
}
