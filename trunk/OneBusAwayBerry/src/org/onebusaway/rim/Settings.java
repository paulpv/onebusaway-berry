package org.onebusaway.rim;

import javax.microedition.location.Criteria;

public class Settings
{
    public interface LocationModes
    {
        public static final int Cell                        = 1;
        public static final int PositionDeterminationEntity = 2;
        public static final int GPS                         = 3;
    }

    private static int    locationMode         = LocationModes.GPS;

    // Set these to Criteria.NO_REQUIREMENT if not needed
    //public static int locationAccuracyHorizontal = 10;
    //public static int locationAccuracyVertical = 10;
    public static int     locationResponseTime = Criteria.NO_REQUIREMENT;
    //public static int locationPowerUsage = Criteria.POWER_USAGE_LOW;
    public static boolean locationCostAllowed  = false;
}
