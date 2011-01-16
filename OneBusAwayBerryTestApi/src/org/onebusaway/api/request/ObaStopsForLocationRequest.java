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

import org.onebusaway.api.GeoPoint;
import org.onebusaway.api.ObaCallable;
import org.onebusaway.net.Uri;

/**
 * Search for stops near a specific location, optionally by stop code
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_StopsForLocation}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaStopsForLocationRequest extends RequestBase
        implements ObaCallable {
    protected ObaStopsForLocationRequest(Uri uri) {
        super(ObaStopsForLocationResponse.class, uri);
    }

    public static class Builder extends RequestBase.BuilderBase {
        public Builder(GeoPoint location) {
            super(BASE_PATH + "/stops-for-location.json");
            builder.appendQueryParameter("lat", String.valueOf(location.getLatitudeE6()/1E6));
            builder.appendQueryParameter("lon", String.valueOf(location.getLongitudeE6()/1E6));
        }

        /**
         * Sets the optional search radius.
         * @param radius The search radius, in meters.
         */
        public Builder setRadius(int radius) {
            builder.appendQueryParameter("radius", String.valueOf(radius));
            return this;
        }

        /**
         * An alternative to {@link #setRadius(int)} to set the search bounding box
         * @param latSpan The latitude span of the bounding box.
         * @param lonSpan The longitude span of the bounding box.
         */
        public Builder setSpan(double latSpan, double lonSpan) {
            builder.appendQueryParameter("latSpan", String.valueOf(latSpan));
            builder.appendQueryParameter("lonSpan", String.valueOf(lonSpan));
            return this;
        }

        /**
         * An alternative to {@link #setRadius(int)} to set the search bounding box
         * @param latSpan The latitude span of the bounding box in microdegrees.
         * @param lonSpan The longitude span of the bounding box in microdegrees.
         */
        public Builder setSpan(int latSpan, int lonSpan) {
            builder.appendQueryParameter("latSpan", String.valueOf(latSpan/1E6));
            builder.appendQueryParameter("lonSpan", String.valueOf(lonSpan/1E6));
            return this;
        }

        /**
         * A specific route short name to search for.
         * @param query The short name query string.
         */
        public Builder setQuery(String query) {
            builder.appendQueryParameter("query", query);
            return this;
        }

        public ObaStopsForLocationRequest build() {
            return new ObaStopsForLocationRequest(buildUri());
        }
    }
}
