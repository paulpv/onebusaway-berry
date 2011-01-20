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
import org.onebusaway.berry.api.ObaList;
import org.onebusaway.berry.api.ObaListObaAgency;
import org.onebusaway.berry.api.ObaListObaRoute;
import org.onebusaway.berry.api.ObaListObaSituation;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.ObaListObaTrip;
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
        stops = (ObaStopElement[]) ObaApi.fromJSON(jsonStops, new ObaStopElement[jsonStops.length()], ObaStopElement.class);

        JSONArray jsonRoutes = json.getJSONArray("routes");
        routes = (ObaRouteElement[]) ObaApi.fromJSON(jsonRoutes, new ObaRouteElement[jsonRoutes.length()], ObaRouteElement.class);

        JSONArray jsonTrips = json.getJSONArray("trips");
        trips = (ObaTripElement[]) ObaApi.fromJSON(jsonTrips, new ObaTripElement[jsonTrips.length()], ObaTripElement.class);

        JSONArray jsonAgencies = json.getJSONArray("agencies");
        agencies = (ObaAgencyElement[]) ObaApi.fromJSON(jsonAgencies, new ObaAgencyElement[jsonAgencies.length()], ObaAgencyElement.class);

        JSONArray jsonSituations = json.getJSONArray("situations");
        situations = (ObaSituationElement[]) ObaApi.fromJSON(jsonSituations, new ObaSituationElement[jsonSituations.length()], ObaSituationElement.class);
    }

    //@Override
    public ObaStop getStop(String id) {
        return (ObaStop) findById(stops, id);
    }

    //@Override
    public ObaListObaStop getStops(String[] ids) {
        ObaListObaStop stops = new ObaListObaStop();
        findList(this.stops, ids, stops);
        return stops;
    }

    //@Override
    public ObaRoute getRoute(String id) {
        return (ObaRoute) findById(routes, id);
    }

    //@Override
    public ObaListObaRoute getRoutes(String[] ids) {
        ObaListObaRoute routes = new ObaListObaRoute();
        findList(this.routes, ids, routes);
        return routes;
    }

    //@Override
    public ObaTrip getTrip(String id) {
        return (ObaTrip) findById(trips, id);
    }

    //@Override
    public ObaListObaTrip getTrips(String[] ids) {
        ObaListObaTrip trips = new ObaListObaTrip();
        findList(this.trips, ids, trips);
        return trips;
    }

    //@Override
    public ObaAgency getAgency(String id) {
        return (ObaAgency) findById(agencies, id);
    }

    //@Override
    public ObaListObaAgency getAgencies(String[] ids) {
        ObaListObaAgency agencies = new ObaListObaAgency();
        findList(this.agencies, ids, agencies);
        return agencies;
    }

    //@Override
    public ObaSituation getSituation(String id) {
        return (ObaSituation) findById(situations, id);
    }

    //@Override
    public ObaListObaSituation getSituations(String[] ids) {
        ObaListObaSituation situations = new ObaListObaSituation();
        findList(this.situations, ids, situations);
        return situations;
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

    private static void findList(ObaElement[] objects, String[] ids, ObaList result) {
        final int len = ids.length;
        for (int i = 0; i < len; ++i) {
            final String id = ids[i];
            final ObaElement obj = findById(objects, id);
            if (obj != null) {
                result.addElement(obj);
            }
        }
    }
}
