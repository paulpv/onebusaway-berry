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

import org.onebusaway.api.ObaCallable;
import org.onebusaway.net.Uri;

/**
 * Retrieve info about a specific stop.
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_Stop}
 *
 * @author Paul Watts (paulcwatts@gmail.com)
 */
public final class ObaStopRequest extends RequestBase implements ObaCallable {
    protected ObaStopRequest(Uri uri) {
        super(ObaStopResponse.class, uri);
    }

    public static class Builder extends RequestBase.BuilderBase {
        public Builder(String stopId) {
            super(getPathWithId("/stop/", stopId));
        }

        public ObaStopRequest build() {
            return new ObaStopRequest(buildUri());
        }
    }

    /**
     * Helper method for constructing new instances.
     * @param context The package context.
     * @param stopId The stopId to request.
     * @return The new request instance.
     */
    public static ObaStopRequest newRequest(String stopId) {
        return new Builder(stopId).build();
    }
}
