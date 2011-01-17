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
package org.onebusaway.api.request.test;

import org.onebusaway.api.Context;
import org.onebusaway.api.ObaApi;

//import j2meunit.framework.TestCase;

//TODO:(pv) Decide on BB unit test framework
//http://www.developer.com/java/j2me/article.php/3760891/A-Unit-Testing-Framework-for-the-BlackBerry.htm
//J2MEUnit: Need to download code and compile a BB .jar file.

public class ObaTestCase //extends TestCase
{
    protected Context getContext()
    {
        return ObaApi.getContext();
    }

    /*
    public void assertOK(ObaResponse response)
    {
        assertNotNull(response);
        assertEquals(ObaApi.OBA_OK, response.getCode());
    }
    
    public void assertNotNullOrEmpty(String s)
    {
        assertNotNull(s);
        assertTrue(s.length() > 0);
    }
    */
}
