package org.onebusaway.rim;

import javax.microedition.location.Coordinates;
import javax.microedition.location.Location;

public class GeoLocation
{
    final Coordinates coordinates;

    final long timestamp;
    final double latitude, longitude;
    final float  altitude, speed, course;
    
    public GeoLocation(Location location)
    {
        timestamp = location.getTimestamp();
        latitude = location.getQualifiedCoordinates().getLatitude();
        longitude = location.getQualifiedCoordinates().getLongitude();
        altitude = location.getQualifiedCoordinates().getAltitude();
        speed = location.getSpeed();
        course = location.getCourse();
        
        coordinates = new Coordinates(latitude, longitude, altitude);
    }
    
    public String toString()
    {
        return "GeoLocation({time=" + timestamp + ", lat=" + latitude + ", lon=" + longitude + ", alt=" + altitude + ", speed=" + speed + ", course=" + course + "})";
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public float getAltitude()
    {
        return altitude;
    }

    public float getSpeed()
    {
        return speed;
    }

    public float getCourse()
    {
        return course;
    }
    
    public Coordinates getCoordinates()
    {
        return coordinates;
    }
}
