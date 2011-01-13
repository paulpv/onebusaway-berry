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

import net.rim.device.api.collection.List;
import net.rim.device.api.collection.ReadableList;
import net.rim.device.api.collection.util.SparseList;
import net.rim.device.api.collection.util.UnsortedReadableList;

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

    //public ObaStop[] getStops(String[] ids)
    public Vector getStops(String[] ids)
    {
        Vector results = findList(stops, ids);
        //ObaStop[] stops = new ObaStop[results.size()];
        //results.copyInto(stops);
        return results;//stops;
    }

    public ObaRoute getRoute(String id) {
        return (ObaRoute) findById(routes, id);
    }

    //public ObaRoute[] getRoutes(String[] ids)
    public Vector getRoutes(String[] ids)
    {
        Vector results = findList(routes, ids);
        //ObaRoute[] routes = new ObaRoute[results.size()];
        //results.copyInto(routes);
        return results;//routes;
    }

    public ObaTrip getTrip(String id) {
        return (ObaTrip) findById(trips, id);
    }

    //public ObaTrip[] getTrips(String[] ids)
    public Vector getTrips(String[] ids)
    {
        Vector results = findList(trips, ids);
        //ObaTrip[] trips = new ObaTrip[results.size()];
        //results.copyInto(trips);
        return results;//trips;
    }

    public ObaAgency getAgency(String id) {
        return (ObaAgency) findById(agencies, id);
    }

    //public ObaAgency[] getAgencies(String[] ids)
    public Vector getAgencies(String[] ids)
    {
        Vector results = findList(agencies, ids);
        //ObaAgency[] agencies = new ObaAgency[results.size()];
        //results.copyInto(agencies);
        return results;//agencies;
    }

    public ObaSituation getSituation(String id) {
        return (ObaSituation) findById(situations, id);
    }

    //public ObaSituation[] getSituations(String[] ids)
    public Vector getSituations(String[] ids)
    {
        Vector results = findList(situations, ids);
        //ObaSituation[] situations = new ObaSituation[results.size()];
        //results.copyInto(situations);
        return results;//situations;
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