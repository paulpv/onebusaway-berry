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

/**
 * Retrieve the current system time.
 * {@link http://code.google.com/p/onebusaway/wiki/OneBusAwayRestApi_CurrentTime}
 *
 * @author Paul Watts (paulcwatts@gmail.com) ORIGINAL
 * @author Paul Peavyhouse (pv@swooby.com) JME
 */
public final class ObaCurrentTimeRequest extends RequestBase implements ObaCallable {
    protected ObaCurrentTimeRequest(Uri uri) {
        super(new ObaCurrentTimeResponse(), uri);
    }

    public static class Builder extends RequestBase.BuilderBase {
        public Builder(Context context) {
            super(context, BASE_PATH + "/current-time.json", true);
        }

        public ObaCurrentTimeRequest build() {
            return new ObaCurrentTimeRequest(buildUri());
        }
    }

    /**
     * Helper method for constructing new instances.
     * @param context The package context.
     * @return The new request instance.
     */
    public static ObaCurrentTimeRequest newRequest(Context context) {
        return new Builder(context).build();
    }

    /*
    @Override
    public ObaCurrentTimeResponse call() {
        return call(ObaCurrentTimeResponse.class);
    }

    @Override
    public String toString() {
        return "ObaCurrentTimeRequest [mUri=" + mUri + "]";
    }
    */
}
