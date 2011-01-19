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

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

import net.rim.device.api.compress.GZIPInputStream;

import org.onebusaway.berry.net.Uri;

public final class ObaHelp
{
    private static final String TAG = "ObaHelp";

    public static String getUri(String url) throws IOException
    {
        return getUri(Uri.parse(url));
    }

    public static String getUri(Uri uri) throws IOException
    {
        // TODO:(pv) Be even more robust:
        // http://vasudevkamath.blogspot.com/2009/09/posting-data-via-http-from-blackberry.html
        
        // TODO:(pv) Accept deviceside=true; interface=wifi; apn, etc
        //  http://developerlife.com/tutorials/?p=884
        //  http://docs.blackberry.com/en/developers/deliverables/5779/Creating_connections_508946_11.jsp

        String url = uri.toString();

        //Log.d(TAG, "getUri: " + uri);

        HttpConnection conn;

        if (uri.getScheme().equalsIgnoreCase("https"))
        {
            conn = (HttpsConnection) Connector.open(url);
        }
        else
        {
            conn = (HttpConnection) Connector.open(url);
        }

        //
        // Below ideas per the original Android code and...
        // http://www.blackberryforums.com/developer-forum/181071-http-post-passing-parameters-urls.html#post1320379
        //

        //conn.setReadTimeout(30*1000);

        String clientAgent = conn.getRequestProperty("Client-Agent");
        
        // Request support for gzip compression
        conn.setRequestProperty("Accept-Encoding", "gzip");

        // Make the request and get the response error code
        int responseCode = conn.getResponseCode();
               
        String responseMessage = conn.getResponseMessage();
        String contentType = conn.getType();
        String contentEncoding = conn.getHeaderField("Content-Encoding");
        int contentLength = (int) conn.getLength();
        InputStream in = conn.openInputStream();
        
        boolean useGzip = contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip");
        
        Reader reader;
        if (useGzip)
        {
            reader = new InputStreamReader(new GZIPInputStream(in));
        }
        else
        {
            reader = new InputStreamReader(in);
        }
        
        String content;

        if (contentLength > 0)
        {
            char[] buf = new char[contentLength];
            reader.read(buf);
            content = new String(buf);
        }
        else
        {
            StringBuffer sb = new StringBuffer();
            char[] buf = new char[1024];
            int n = 0;
            while ((n = reader.read(buf)) > 0)
            {
                sb.append(buf, 0, n);
            }
            content = sb.toString();
        }

        in.close();
        conn.close();

        return content;
    }
}
