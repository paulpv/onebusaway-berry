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

public final class ObaReferencesElement implements ObaReferences {
    public static final ObaReferencesElement EMPTY_OBJECT = new ObaReferencesElement();

    private final ObaStopElement[] stops;
    private final ObaRouteElement[] routes;
    private final ObaTripElement[] trips;
    private final ObaAgencyElement[] agencies;
    private final ObaSituationElement[] situations;

    public ObaReferencesElement() {
        stops = ObaStopElement.EMPTY_ARRAY;
        routes = ObaRouteElement.EMPTY_ARRAY;
        trips = ObaTripElement.EMPTY_ARRAY;
        agencies = ObaAgencyElement.EMPTY_ARRAY;
        situations = ObaSituationElement.EMPTY_ARRAY;
    }

    public ObaStop getStop(String id) {
        return (ObaStop) findById(stops, id);
    }

    public List getStops(String[] ids) {
        return findList(stops, ids);
    }

    public ObaRoute getRoute(String id) {
        return (ObaRoute) findById(routes, id);
    }

    public List getRoutes(String[] ids) {
        return findList(routes, ids);
    }

    public ObaTrip getTrip(String id) {
        return (ObaTrip) findById(trips, id);
    }

    public List getTrips(String[] ids) {
        return findList(trips, ids);
    }

    public ObaAgency getAgency(String id) {
        return (ObaAgency) findById(agencies, id);
    }

    public List getAgencies(String[] ids) {
        return findList(agencies, ids);
    }

    public ObaSituation getSituation(String id) {
        return (ObaSituation) findById(situations, id);
    }

    public List getSituations(String[] ids) {
        return findList(situations, ids);
    }

    //
    // TODO: This will be much easier when we convert to HashMap storage.
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
    
    private static List findList(ObaElement[] objects, String[] ids) {
        ArrayList<E> result = new ArrayList<E>();
        final int len = ids.length;
        for (int i=0; i < len; ++i) {
            final String id = ids[i];
            final ObaElement obj = findById(objects, id);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }
}
