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

import org.onebusaway.berry.api.ObaList;
import org.onebusaway.berry.api.ObaListObaAgency;
import org.onebusaway.berry.api.ObaListObaRoute;
import org.onebusaway.berry.api.ObaListObaSituation;
import org.onebusaway.berry.api.ObaListObaStop;
import org.onebusaway.berry.api.ObaListObaTrip;
import org.onebusaway.json.me.JSONArray;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 */
public final class ObaReferencesElement implements ObaReferences {

    public static final ObaReferencesElement EMPTY_OBJECT = new ObaReferencesElement();

    private final ObaStopElement[]           stops;
    private final ObaRouteElement[]          routes;
    private final ObaTripElement[]           trips;
    private final ObaAgencyElement[]         agencies;
    private final ObaSituationElement[]      situations;

    public ObaReferencesElement() {
        stops = ObaStopElement.EMPTY_ARRAY;
        routes = ObaRouteElement.EMPTY_ARRAY;
        trips = ObaTripElement.EMPTY_ARRAY;
        agencies = ObaAgencyElement.EMPTY_ARRAY;
        situations = ObaSituationElement.EMPTY_ARRAY;
    }

    public ObaReferencesElement(JSONObject json) throws JSONException {
        JSONArray stops = json.getJSONArray("stops");
        this.stops = new ObaStopElement[stops.length()];
        for (int i = 0; i < this.stops.length; i++) {
            this.stops[i] = new ObaStopElement(stops.getJSONObject(i));
        }

        JSONArray routes = json.getJSONArray("routes");
        this.routes = new ObaRouteElement[routes.length()];
        for (int i = 0; i < this.routes.length; i++) {
            this.routes[i] = new ObaRouteElement(routes.getJSONObject(i));
        }

        JSONArray trips = json.getJSONArray("trips");
        this.trips = new ObaTripElement[trips.length()];
        for (int i = 0; i < this.trips.length; i++) {
            this.trips[i] = new ObaTripElement(trips.getJSONObject(i));
        }

        JSONArray agencies = json.getJSONArray("agencies");
        this.agencies = new ObaAgencyElement[agencies.length()];
        for (int i = 0; i < this.agencies.length; i++) {
            this.agencies[i] = new ObaAgencyElement(agencies.getJSONObject(i));
        }

        JSONArray situations = json.getJSONArray("situations");
        this.situations = new ObaSituationElement[situations.length()];
        for (int i = 0; i < this.situations.length; i++) {
            this.situations[i] = new ObaSituationElement(situations.getJSONObject(i));
        }
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
    // TODO: This will be much easier when we convert to HashMap storage.
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
