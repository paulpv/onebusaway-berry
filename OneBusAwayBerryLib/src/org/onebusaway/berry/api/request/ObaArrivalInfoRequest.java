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

public final class ObaArrivalInfoRequest extends RequestBase {

    protected ObaArrivalInfoRequest(Uri uri) {
        super(uri);
    }

    public static class Builder extends RequestBase.BuilderBase {
        public Builder(Context context, String stopId) {
            super(context, getPathWithId("/arrivals-and-departures-for-stop/", stopId));
        }

        public ObaArrivalInfoRequest build() {
            return new ObaArrivalInfoRequest(buildUri());
        }
    }

    /**
     * Helper method for constructing new instances.
     * @param context The package context.
     * @param routeId The stop Id to request.
     * @return The new request instance.
     */
    public static ObaArrivalInfoRequest newRequest(Context context, String stopId) {
        return new Builder(context, stopId).build();
    }

    protected ObaResponse createResponse(JSONObject json) throws JSONException {
        return new ObaArrivalInfoResponse(json);
    }

    protected ObaResponse createResponseFromError(int obaErrorCode, Throwable err) {
        return new ObaArrivalInfoResponse(obaErrorCode, err);
    }

    /*
    @Override
    public ObaArrivalInfoResponse call() {
        return call(ObaArrivalInfoResponse.class);
    }
    */
}
