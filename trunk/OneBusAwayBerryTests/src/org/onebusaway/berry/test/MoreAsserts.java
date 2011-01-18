package org.onebusaway.berry.test;

import j2meunit.framework.Assert;

import java.util.Hashtable;

/**
 * A copy of Android's MoreAsserts.java code.
 * 
 * TODO:(pv) Incorporate Android MoreAsserts.java code as required
 * http://hi-android.info/src/android/test/MoreAsserts.java.html
 * 
 * See: http://developer.android.com/reference/android/test/MoreAsserts.html
 * 
 * @author pv
 * 
 */
public class MoreAsserts
{
    /**
     * Variant of assertContentsInAnyOrder(String, Iterable, Object...) using a generic message.
     * @param actual
     * @param expected
     */
    public static void assertContentsInAnyOrder(Object[] actual, Object[] expected)
    {
        assertContentsInAnyOrder(null, actual, expected);
    }

    /**
     * Asserts that actual contains precisely the elements expected, but in any order.
     * @param message
     * @param actual
     * @param expected
     */
    public static void assertContentsInAnyOrder(String message, Object[] actual, Object[] expected)
    {
        Hashtable expectedMap = new Hashtable(expected.length);
        Object expectedObj;
        for (int i = 0; i < expected.length; i++)
        {
            expectedObj = expected[i];
            expectedMap.put(expectedObj, expectedObj);
        }

        Object actualObj;
        for (int i = 0; i < actual.length; i++)
        {
            actualObj = actual[i];
            if (expectedMap.remove(actualObj) == null)
            {
                failWithMessage(message, "Extra object in actual: (" + actualObj.toString() + ")");
            }
        }
    }

    private static void failWithMessage(String userMessage, String ourMessage)
    {
        new Assert().fail((userMessage == null) ? ourMessage : userMessage + ' ' + ourMessage);
    }
}
