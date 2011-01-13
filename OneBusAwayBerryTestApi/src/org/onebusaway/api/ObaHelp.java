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
package org.onebusaway.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

import org.onebusaway.net.Uri;
import org.onebusaway.rim.MyApp;

public final class ObaHelp
{
    private static final String TAG = "ObaHelp";

    public static String getUri(String url) throws IOException
    {
        return getUri(Uri.parse(url));
    }

    public static String getUri(Uri uri) throws IOException
    {

        // TODO:(pv) Be a little more robust:
        // http://vasudevkamath.blogspot.com/2009/09/posting-data-via-http-from-blackberry.html

        String url = uri.toString();

        MyApp.log(TAG, "getUri: " + url);
        //Log.d(TAG, "getUri: " + url);

        boolean isSecure = uri.getScheme().equalsIgnoreCase("https");
        HttpConnection conn;

        if (isSecure)
        {
            conn = (HttpsConnection) Connector.open(url);
        }
        else
        {
            conn = (HttpConnection) Connector.open(url);
        }

        //conn.setReadTimeout(30*1000);
        
        // TODO:(pv) Support gzip encoding on BB...
        boolean useGzip = false;
        //conn.setRequestProperty("Accept-Encoding", "gzip");

        //
        // Per:
        // http://www.blackberryforums.com/developer-forum/181071-http-post-passing-parameters-urls.html#post1320379
        //
        int responseCode = conn.getResponseCode();
        String responseMessage = conn.getResponseMessage();
        String contentType = conn.getType();
        String contentEncoding = conn.getHeaderField("Content-Encoding");
        int contentLength = (int) conn.getLength();
        InputStream in = conn.openInputStream();
        
        if (!TextUtils.isNullOrEmpty(contentEncoding))
        {
            useGzip = contentEncoding.equalsIgnoreCase("gzip");
        }

        /*
        Reader reader;
        if (useGzip) {
            reader = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(in)), 8*1024);
        }
        else {
            reader = new BufferedReader(
                    new InputStreamReader(in), 8*1024);
        }
        */

        String content;

        if (contentLength > 0)
        {
            byte[] buf = new byte[contentLength];
            in.read(buf);
            content = new String(buf);
        }
        else
        {
            StringBuffer sb = new StringBuffer();
            byte[] buf = new byte[1024];
            int n = 0;
            while ((n = in.read(buf)) > 0)
            {
                sb.append(new String(buf, 0, n));
            }
            content = sb.toString();
        }

        in.close();
        conn.close();

        return content;

    }
}
