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
package org.onebusaway.berry.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.compress.GZIPInputStream;

import org.onebusaway.berry.net.Uri;

/**
 * 
 * @author Paul Watts (paulcwatts@gmail.com)
 * @author Paul Peavyhouse (pv@swooby.com) JME BB
 *
 */
public final class ObaHelp {
    private static final String TAG = "ObaHelp";

    // TODO:(pv) Accept deviceside=true; interface=wifi; apn, etc
    //  http://developerlife.com/tutorials/?p=884
    //  http://docs.blackberry.com/en/developers/deliverables/5779/Creating_connections_508946_11.jsp

    public static String getUri(Uri uri) throws IOException {
        return getUri(new URL(uri.toString()));
    }

    public static String getUri(URL url) throws IOException {
        Log.d(TAG, "getUri: " + url);

        boolean useGzip = false;
        HttpConnection conn = (HttpConnection) url.openConnection();
        //conn.setReadTimeout(30*1000);

        // Request support for gzip compression
        conn.setRequestProperty("Accept-Encoding", "gzip");

        // TODO:(pv) Add OBA to Client=Agent value
        //String clientAgent = conn.getRequestProperty("Client-Agent");
        //conn.setRequestProperty("Client-Agent", ?);

        //
        // Below ideas per the original Android code and...
        // http://www.blackberryforums.com/developer-forum/181071-http-post-passing-parameters-urls.html#post1320379
        //

        // Make the request and get the response error code
        int responseCode = conn.getResponseCode();
        //String responseMessage = conn.getResponseMessage();
        //String contentType = conn.getType();

        // TODO:(pv) Be even more robust:
        // http://vasudevkamath.blogspot.com/2009/09/posting-data-via-http-from-blackberry.html

        InputStream in = conn.openInputStream();

        String contentEncoding = conn.getEncoding();
        if (contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip")) {
            useGzip = true;
        }

        Reader reader;
        if (useGzip) {
            reader = new InputStreamReader(new GZIPInputStream(in));
        }
        else {
            reader = new InputStreamReader(in);
        }

        String content;
        int contentLength = (int) conn.getLength();
        if (contentLength > 0) {
            char[] buf = new char[contentLength];
            reader.read(buf);
            content = new String(buf);
        }
        else {
            StringBuffer sb = new StringBuffer();
            char[] buf = new char[1024];
            int n = 0;
            while ((n = reader.read(buf)) > 0) {
                sb.append(buf, 0, n);
            }
            content = sb.toString();
        }

        in.close();
        conn.close();

        Log.d(TAG, "getUri: content=" + content);

        return content;
    }
}
