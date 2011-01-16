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
package org.onebusaway.api.request;

import org.onebusaway.api.ObaReceivable;
import org.onebusaway.api.elements.ObaReferences;
import org.onebusaway.api.elements.ObaReferencesElement;
import org.onebusaway.api.elements.ObaShape;
import org.onebusaway.api.elements.ObaShapeElement;
import org.onebusaway.api.elements.ObaStop;
import org.onebusaway.api.elements.ObaStopGrouping;
import org.onebusaway.json.me.JSONObject;

/**
 * Response object for ObaStopForRouteRequest requests.
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaStopsForRouteResponse implements ObaReceivable {
    
    private static final class Entry {
        private static final Entry EMPTY_OBJECT = new Entry();

        private final String[] stopIds;
        private final ObaStopGrouping[] stopGroupings;
        private final ObaShapeElement[] polylines;

        Entry() {
            stopIds = new String[] {};
            stopGroupings = ObaStopGrouping.EMPTY_ARRAY;
            polylines = ObaShapeElement.EMPTY_ARRAY;
        }
    }

    private ObaReferencesElement references;
    private Entry entry;

    private ObaStopsForRouteResponse() {
        references = ObaReferencesElement.EMPTY_OBJECT;
        entry = Entry.EMPTY_OBJECT;
    }

    public void fromJSON(JSONObject json)
    {
        JSONObject jsonReferences = json.optJSONObject("references");
        if (jsonReferences != null)
        {
            
        }
        
        JSONObject jsonEntry = json.optJSONObject("entry");
    }
    
    /**
     * Returns the list of dereferenced stops.
     */
    public ObaStop[] getStops() {
        return references.getStops(entry.stopIds);
    }

    /**
     * @return The list of shapes, if they exist; otherwise returns an empty list.
     */
    public ObaShape[] getShapes() {
        return entry.polylines;
    }

    /**
     * @return Returns a collection of stops grouped into useful collections.
     */
    public ObaStopGrouping[] getStopGroupings() {
        return entry.stopGroupings;
    }

    protected ObaReferences getRefs() {
        return references;
    }
}
