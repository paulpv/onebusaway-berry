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
package org.onebusaway.api.elements;

import java.util.Vector;

import org.onebusaway.api.ObaApi;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

public final class ObaReferencesElement implements ObaReferences {
    
    public static final ObaReferencesElement EMPTY_OBJECT = new ObaReferencesElement();

    private ObaStopElement[] stops;
    private ObaRouteElement[] routes;
    private ObaTripElement[] trips;
    private ObaAgencyElement[] agencies;
    private ObaSituationElement[] situations;

    public ObaReferencesElement() {
        stops = ObaStopElement.EMPTY_ARRAY;
        routes = ObaRouteElement.EMPTY_ARRAY;
        trips = ObaTripElement.EMPTY_ARRAY;
        agencies = ObaAgencyElement.EMPTY_ARRAY;
        situations = ObaSituationElement.EMPTY_ARRAY;
    }

    public void fromJSON(JSONObject json) throws JSONException, InstantiationException, IllegalAccessException
    {
        JSONArray jsonStops = json.getJSONArray("stops");
        if (jsonStops != null)
        {
            stops = new ObaStopElement[jsonStops.length()];
            ObaApi.copyJSONArrayToObaReceivableArray(jsonStops, stops, ObaStopElement.class);
        }
        else
        {
            stops = ObaStopElement.EMPTY_ARRAY;
        }
        
        JSONArray jsonRoutes = json.getJSONArray("routes");
        if (jsonRoutes != null)
        {
            routes = new ObaRouteElement[jsonRoutes.length()];
            ObaApi.copyJSONArrayToObaReceivableArray(jsonRoutes, routes, ObaRouteElement.class);
        }
        else
        {
            routes = ObaRouteElement.EMPTY_ARRAY;
        }
        
        JSONArray jsonTrips = json.getJSONArray("trips");
        if (jsonTrips != null)
        {
            trips = new ObaTripElement[jsonTrips.length()];
            ObaApi.copyJSONArrayToObaReceivableArray(jsonTrips, trips, ObaTripElement.class);
        }
        else
        {
            trips = ObaTripElement.EMPTY_ARRAY;
        }
        
        JSONArray jsonAgencies = json.getJSONArray("agencies");
        if (jsonAgencies != null)
        {
            agencies = new ObaAgencyElement[jsonAgencies.length()];
            ObaApi.copyJSONArrayToObaReceivableArray(jsonAgencies, agencies, ObaAgencyElement.class);
        }
        else
        {
            agencies = ObaAgencyElement.EMPTY_ARRAY;
        }
        
        JSONArray jsonSituations = json.getJSONArray("situations");
        if (jsonSituations != null)
        {
            situations = new ObaSituationElement[jsonSituations.length()];
            ObaApi.copyJSONArrayToObaReceivableArray(jsonSituations, situations, ObaSituationElement.class);
        }
        else
        {
            situations = ObaSituationElement.EMPTY_ARRAY;
        }
    }
    
    public ObaStop getStop(String id) {
        return (ObaStop) findById(stops, id);
    }

    public ObaStop[] getStops(String[] ids)
    {
        Vector results = findList(stops, ids);
        ObaStop[] stops = new ObaStop[results.size()];
        results.copyInto(stops);
        return stops;
    }

    public ObaRoute getRoute(String id) {
        return (ObaRoute) findById(routes, id);
    }

    public ObaRoute[] getRoutes(String[] ids)
    {
        Vector results = findList(routes, ids);
        ObaRoute[] routes = new ObaRoute[results.size()];
        results.copyInto(routes);
        return routes;
    }

    public ObaTrip getTrip(String id) {
        return (ObaTrip) findById(trips, id);
    }

    public ObaTrip[] getTrips(String[] ids)
    {
        Vector results = findList(trips, ids);
        ObaTrip[] trips = new ObaTrip[results.size()];
        results.copyInto(trips);
        return trips;
    }

    public ObaAgency getAgency(String id) {
        return (ObaAgency) findById(agencies, id);
    }

    public ObaAgency[] getAgencies(String[] ids)
    {
        Vector results = findList(agencies, ids);
        ObaAgency[] agencies = new ObaAgency[results.size()];
        results.copyInto(agencies);
        return agencies;
    }

    public ObaSituation getSituation(String id) {
        return (ObaSituation) findById(situations, id);
    }

    public ObaSituation[] getSituations(String[] ids)
    {
        Vector results = findList(situations, ids);
        ObaSituation[] situations = new ObaSituation[results.size()];
        results.copyInto(situations);
        return situations;
    }

    //
    // TODO:(oba) This will be much easier when we convert to HashMap storage.
    //
    private static ObaElement findById(ObaElement[] objects, String id) {
        final int len = objects.length;
        for (int i=0; i < len; ++i) {
            final ObaElement obj = objects[i];
            if (obj.getId().equals(id)) {
                return obj;
            }
        }
        return null;
    }
    
    private static Vector findList(ObaElement[] objects, String[] ids) {
        Vector results = new Vector();
        final int len = ids.length;
        for (int i=0; i < len; ++i) {
            final String id = ids[i];
            final ObaElement obj = findById(objects, id);
            if (obj != null) {
                results.addElement(obj);
            }
        }
        return results;
    }
}
