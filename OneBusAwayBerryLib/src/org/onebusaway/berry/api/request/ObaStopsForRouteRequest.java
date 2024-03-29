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
package org.onebusaway.berry.api.request;

import org.onebusaway.berry.api.Context;
import org.onebusaway.berry.api.ObaCallable;
import org.onebusaway.berry.net.Uri;
import org.onebusaway.json.me.JSONException;
import org.onebusaway.json.me.JSONObject;

/**
 * Retrieve the set of stops serving a particular route
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_StopsForRoute}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaStopsForRouteRequest extends RequestBase {
    protected ObaStopsForRouteRequest(Uri uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {
        public Builder(Context context, String routeId) {
            super(context, getPathWithId("/stops-for-route/", routeId));
        }

        public Builder setIncludeShapes(boolean includePolylines) {
            mBuilder.appendQueryParameter("includePolylines",
                            includePolylines ? "true" : "false");
            return this;
        }

        public ObaStopsForRouteRequest build() {
            return new ObaStopsForRouteRequest(buildUri());
        }
    }

    protected ObaResponse createResponse(JSONObject json) throws JSONException {
        return new ObaStopsForRouteResponse(json);
    }

    protected ObaResponse createResponseFromError(int obaErrorCode, Throwable err) {
        return new ObaStopsForRouteResponse(obaErrorCode, err);
    }

    /*
    @Override
    public ObaStopsForRouteResponse call() {
        return call(ObaStopsForRouteResponse.class);
    }

    @Override
    public String toString() {
        return "ObaStopsForRouteRequest [mUri=" + mUri + "]";
    }
    */
}
