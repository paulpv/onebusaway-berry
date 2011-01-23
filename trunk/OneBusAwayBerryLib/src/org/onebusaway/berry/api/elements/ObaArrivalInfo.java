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
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaArrivalInfo {
    public static final ObaArrivalInfo   EMPTY_OBJECT = new ObaArrivalInfo();
    public static final ObaArrivalInfo[] EMPTY_ARRAY  = new ObaArrivalInfo[] {};

    public static final class Frequency {
        private final long startTime;
        private final long endTime;
        private final long headway;

        Frequency() {
            startTime = 0;
            endTime = 0;
            headway = 0;
        }

        public Frequency(JSONObject json) throws JSONException {
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

    private final String               routeId;
    private final String               routeShortName;
    private final String               routeLongName;
    private final String               tripId;
    private final String               tripHeadsign;
    private final String               stopId;
    private final long                 predictedArrivalTime;
    private final long                 scheduledArrivalTime;
    private final long                 predictedDepartureTime;
    private final long                 scheduledDepartureTime;
    private final String               status;
    private final Frequency            frequency;
    private final String               vehicleId;
    private final Double               distanceFromStop;
    private final Integer              numberOfStopsAway;
    private final long                 serviceDate;
    private final long                 lastUpdateTime;
    private final Boolean              predicted;
    private final ObaTripStatusElement tripStatus;
    private final String[]             situationIds;

    ObaArrivalInfo() {
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

    public ObaArrivalInfo(JSONObject json) throws JSONException {
        this.routeId = json.getString("routeId");
        this.routeShortName = json.getString("routeShortName");
        this.routeLongName = json.getString("routeLongName");
        this.tripId = json.getString("tripId");
        this.tripHeadsign = json.getString("tripHeadsign");
        this.stopId = json.getString("stopId");
        this.predictedArrivalTime = json.getLong("predictedArrivalTime");
        this.scheduledArrivalTime = json.getLong("scheduledArrivalTime");
        this.predictedDepartureTime = json.getLong("predictedDepartureTime");
        this.scheduledDepartureTime = json.getLong("scheduledDepartureTime");
        this.status = json.getString("status");
        JSONObject frequency = json.optJSONObject("frequency"); // optional
        this.frequency = (frequency == null) ? null : new Frequency(frequency);
        this.vehicleId = json.optString("vehicleId"); // optional
        this.distanceFromStop = ObaApi.optDouble(json, "distanceFromStop"); // optional
        this.numberOfStopsAway = ObaApi.optInteger(json, "numberOfStopsAway"); // optional
        this.serviceDate = json.getLong("serviceDate");
        this.lastUpdateTime = json.getLong("lastUpdateTime");
        this.predicted = ObaApi.optBoolean(json, "predicted"); // optional
        JSONObject tripStatus = json.optJSONObject("tripStatus"); // optional
        this.tripStatus = (tripStatus == null) ? null : new ObaTripStatusElement(tripStatus);
        JSONArray situationIds = json.optJSONArray("situationIds"); // optional
        if (situationIds != null) {
            this.situationIds = new String[situationIds.length()];
            for (int i = 0; i < this.situationIds.length; i++) {
                this.situationIds[i] = situationIds.getString(i);
            }
        }
        else {
            this.situationIds = null;
        }
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
