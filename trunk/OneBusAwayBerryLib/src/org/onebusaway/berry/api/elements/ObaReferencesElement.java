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

import java.util.Vector;

import org.onebusaway.berry.api.JSONReceivable;
import org.onebusaway.berry.api.ObaApi;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaReferencesElement implements ObaReferences, JSONReceivable {

    public static final ObaReferencesElement EMPTY_OBJECT = new ObaReferencesElement();

    private ObaStopElement[]                 stops;
    private ObaRouteElement[]                routes;
    private ObaTripElement[]                 trips;
    private ObaAgencyElement[]               agencies;
    private ObaSituationElement[]            situations;

    public ObaReferencesElement() {
        stops = ObaStopElement.EMPTY_ARRAY;
        routes = ObaRouteElement.EMPTY_ARRAY;
        trips = ObaTripElement.EMPTY_ARRAY;
        agencies = ObaAgencyElement.EMPTY_ARRAY;
        situations = ObaSituationElement.EMPTY_ARRAY;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException {
        JSONArray jsonStops = json.getJSONArray("stops");
        stops = new ObaStopElement[jsonStops.length()];
        ObaApi.copyTo(jsonStops, stops, ObaStopElement.class);

        JSONArray jsonRoutes = json.getJSONArray("routes");
        routes = new ObaRouteElement[jsonRoutes.length()];
        ObaApi.copyTo(jsonRoutes, routes, ObaRouteElement.class);

        JSONArray jsonTrips = json.getJSONArray("trips");
        trips = new ObaTripElement[jsonTrips.length()];
        ObaApi.copyTo(jsonTrips, trips, ObaTripElement.class);

        JSONArray jsonAgencies = json.getJSONArray("agencies");
        agencies = new ObaAgencyElement[jsonAgencies.length()];
        ObaApi.copyTo(jsonAgencies, agencies, ObaAgencyElement.class);

        JSONArray jsonSituations = json.getJSONArray("situations");
        situations = new ObaSituationElement[jsonSituations.length()];
        ObaApi.copyTo(jsonSituations, situations, ObaSituationElement.class);
    }

    //@Override
    public ObaStop getStop(String id) {
        return (ObaStop) findById(stops, id);
    }

    //@Override
    public Vector getStops(String[] ids) {
        return findList(stops, ids);
    }

    //@Override
    public ObaRoute getRoute(String id) {
        return (ObaRoute) findById(routes, id);
    }

    //@Override
    public Vector getRoutes(String[] ids) {
        return findList(routes, ids);
    }

    //@Override
    public ObaTrip getTrip(String id) {
        return (ObaTrip) findById(trips, id);
    }

    //@Override
    public Vector getTrips(String[] ids) {
        return findList(trips, ids);
    }

    //@Override
    public ObaAgency getAgency(String id) {
        return (ObaAgency) findById(agencies, id);
    }

    //@Override
    public Vector getAgencies(String[] ids) {
        return findList(agencies, ids);
    }

    //@Override
    public ObaSituation getSituation(String id) {
        return (ObaSituation) findById(situations, id);
    }

    //@Override
    public Vector getSituations(String[] ids) {
        return findList(situations, ids);
    }

    //
    // TODO:(oba) This will be much easier when we convert to HashMap storage.
    //
    private static ObaElement findById(ObaElement[] objects, String id) {
        final int len = objects.length;
        for (int i = 0; i < len; ++i) {
            final ObaElement obj = objects[i];
            if (obj.getId().equals(id)) {
                return obj;
            }
        }
        return null;
    }

    private static Vector findList(ObaElement[] objects, String[] ids) {
        Vector result = new Vector();
        final int len = ids.length;
        for (int i = 0; i < len; ++i) {
            final String id = ids[i];
            final ObaElement obj = findById(objects, id);
            if (obj != null) {
                result.addElement(obj);
            }
        }
        return result;
    }
}
