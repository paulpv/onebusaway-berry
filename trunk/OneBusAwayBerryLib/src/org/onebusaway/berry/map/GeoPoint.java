package org.onebusaway.berry.map;

import org.onebusaway.berry.util.ObaString;

/**
 * Custom implementation of custom implementation of com.google.android.maps.GeoPoint:
 * http://code.google.com/android/add-ons/google-apis/reference/com/google/android/maps/GeoPoint.html
 * 
 * Much of this code was graciously "borrowed" from:
 * http://code.google.com/p/mapsforge/source/browse/trunk/mapsforge/src/org/mapsforge/android/map/GeoPoint.java
 * 
 * Differs in one area:
 * The original code for Android limits latitude to +/-80 degrees.
 * This code for BlackBerry limits latitude to +/-90 degrees.
 * 
 * @author pv
 *
 */
public class GeoPoint implements Comparable {
    private static final int LATITUDE_MIN          = -90;
    private static final int LATITUDE_MAX          = +90;
    private static final int LONGITUDE_MIN         = -180;
    private static final int LONGITUDE_MAX         = +180;

    private static final int MULTIPLICATION_FACTOR = 1000000;
    private final int        hashCode;
    private final int        latitudeE6;
    private final int        longitudeE6;

    public GeoPoint(double latitude, double longitude) {
        this.latitudeE6 = clipLatitude((int) (latitude * MULTIPLICATION_FACTOR));
        this.longitudeE6 = clipLongitude((int) (longitude * MULTIPLICATION_FACTOR));
        this.hashCode = calculateHashCode();
    }

    public GeoPoint(int latitudeE6, int longitudeE6) {
        this.latitudeE6 = clipLatitude(latitudeE6);
        this.longitudeE6 = clipLongitude(longitudeE6);
        this.hashCode = calculateHashCode();
    }

    public int compareTo(Object obj) {
        if (obj.getClass() != getClass()) {
            throw new IllegalArgumentException("obj must be of type " + ObaString.getShortClassName(getClass()));
        }

        GeoPoint geoPoint = (GeoPoint) obj;

        if (this.longitudeE6 > geoPoint.longitudeE6) {
            return 1;
        }
        else if (this.longitudeE6 < geoPoint.longitudeE6) {
            return -1;
        }
        else if (this.latitudeE6 > geoPoint.latitudeE6) {
            return 1;
        }
        else if (this.latitudeE6 < geoPoint.latitudeE6) {
            return -1;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        GeoPoint geoPoint = (GeoPoint) obj;

        return latitudeE6 == geoPoint.latitudeE6 && longitudeE6 == geoPoint.longitudeE6;
    }

    public double getLatitude() {
        return this.latitudeE6 / (double) MULTIPLICATION_FACTOR;
    }

    public int getLatitudeE6() {
        return this.latitudeE6;
    }

    public double getLongitude() {
        return this.longitudeE6 / (double) MULTIPLICATION_FACTOR;
    }

    public int getLongitudeE6() {
        return this.longitudeE6;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public String toString() {
        return new StringBuffer().append(latitudeE6).append(",").append(longitudeE6).toString();
    }

    public String toDoubleString() {
        return new StringBuffer().append(latitudeE6 / (double) MULTIPLICATION_FACTOR).append(",") //
        .append(longitudeE6 / (double) MULTIPLICATION_FACTOR).toString();
    }

    private int calculateHashCode() {
        int result = 7;
        result = 31 * result + this.latitudeE6;
        result = 31 * result + this.longitudeE6;
        return result;
    }

    private int clipLatitude(int latitude) {
        if (latitude < LATITUDE_MIN * MULTIPLICATION_FACTOR) {
            return (int) (LATITUDE_MIN * MULTIPLICATION_FACTOR);
        }
        else if (latitude > LATITUDE_MAX * MULTIPLICATION_FACTOR) {
            return (int) (LATITUDE_MAX * MULTIPLICATION_FACTOR);
        }
        else {
            return latitude;
        }
    }

    private int clipLongitude(int longitude) {
        if (longitude < LONGITUDE_MIN * MULTIPLICATION_FACTOR) {
            return (int) (LONGITUDE_MIN * MULTIPLICATION_FACTOR);
        }
        else if (longitude > LONGITUDE_MAX * MULTIPLICATION_FACTOR) {
            return (int) (LONGITUDE_MAX * MULTIPLICATION_FACTOR);
        }
        else {
            return longitude;
        }
    }
}