package org.onebusaway.berry.api;

public class ObaInteger
{
    public static int getInteger(String string, int defaultValue)
    {
        try
        {
            return Integer.parseInt(string);
        }
        catch(NumberFormatException nfe)
        {
            return defaultValue;
        }
    }
}
