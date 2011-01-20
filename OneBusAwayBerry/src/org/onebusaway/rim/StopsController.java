package org.onebusaway.rim;

import net.rim.blackberry.api.maps.MapView;

import org.onebusaway.berry.map.GeoPoint;

public class StopsController
{
    //public interface Listener {
    //    public void onRequestFulfilled(ObaResponse response);
    //}

    private static final class RequestInfo
    {
        private final String   mRouteId;
        private final GeoPoint mCenter;
        private final int      mLatSpan;
        private final int      mLonSpan;
        private final int      mZoomLevel;

        RequestInfo(String routeId, GeoPoint center, int latSpan, int lonSpan, int zoomLevel)
        {
            mRouteId = routeId;
            mCenter = center;
            mLatSpan = latSpan;
            mLonSpan = lonSpan;
            mZoomLevel = zoomLevel;
        }

        String getRouteId()
        {
            return mRouteId;
        }

        GeoPoint getCenter()
        {
            return mCenter;
        }

        int getLatSpan()
        {
            return mLatSpan;
        }

        int getLonSpan()
        {
            return mLonSpan;
        }

        int getZoomLevel()
        {
            return mZoomLevel;
        }

        public String toString()
        {
            return "Request: Center=(" + mCenter + ") Zoom=" + mZoomLevel + " Route=" + mRouteId;
        }
    }

    /*
    private static final class ResponseInfo {
        private final RequestInfo mRequest;
        private final ObaResponse mResponse;

        ResponseInfo(RequestInfo req, ObaResponse resp) {
            mRequest = req;
            mResponse = resp;
        }
        RequestInfo getRequest() {
            return mRequest;
        }
        ObaResponse getResponse() {
            return mResponse;
        }
    }
    */

    static RequestInfo requestFromView(MapView view, String routeId)
    {
        int lat = view.getLatitude();
        int lon = view.getLongitude();
        GeoPoint mCenter = new GeoPoint(lat, lon);
        return new RequestInfo(routeId, mCenter, 0,//view.getLatitudeSpan(),
                        0,//view.getLongitudeSpan(),
                        view.getZoom());
    }

    private final ScreenMap parent;

    public StopsController(ScreenMap parent)
    {
        this.parent = parent;
    }
}
