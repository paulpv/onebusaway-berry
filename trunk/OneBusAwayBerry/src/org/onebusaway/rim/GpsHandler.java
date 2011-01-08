package org.onebusaway.rim;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.gps.BlackBerryCriteria;
import net.rim.device.api.gps.GPSInfo;

public class GpsHandler
{
    static LocationProvider locationProvider;
    static double           lat, lon;
    static float            alt, spd, crs;

    public GpsHandler(int gpsMode)
    {
        if (locationProvider != null)
        {
            locationProvider.reset();
            locationProvider.setLocationListener(null, -1, -1, -1);
        }

        BlackBerryCriteria foo;

        Criteria myCriteria = new Criteria();
        myCriteria.setPreferredResponseTime(Settings.locationResponseTime);
        //myCriteria.setHorizontalAccuracy(Settings.locationAccuracyHorizontal);
        //myCriteria.setVerticalAccuracy(Settings.locationAccuracyVertical);
        //myCriteria.setPreferredPowerConsumption(Settings.locationPowerUsage);
        myCriteria.setCostAllowed(Settings.locationCostAllowed);

        if (gpsMode == GPSInfo.GPS_MODE_AUTONOMOUS)
        {
            myCriteria.setCostAllowed(false);
        }
        else if (gpsMode == GPSInfo.GPS_MODE_ASSIST)
        {
            myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
        }
        else
        {
            myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
        }

        try
        {
            locationProvider = LocationProvider.getInstance(myCriteria);

            if (locationProvider != null)
            {
                locationProvider.setLocationListener(new myLocationListener(), -1, -1, -1);
            }
        }
        catch (Exception err)
        {
        }
    }

    private static class myLocationListener implements LocationListener
    {
        public void locationUpdated(LocationProvider provider, Location location)
        {
            lat = location.getQualifiedCoordinates().getLatitude();
            lon = location.getQualifiedCoordinates().getLongitude();
            alt = location.getQualifiedCoordinates().getAltitude();
            spd = location.getSpeed();
            crs = location.getCourse();
        }

        public void providerStateChanged(LocationProvider provider, int newState)
        {
        }
    }
}
