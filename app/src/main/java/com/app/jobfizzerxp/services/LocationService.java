package com.app.jobfizzerxp.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.app.jobfizzerxp.utilities.helpers.AppSettings;
import com.app.jobfizzerxp.utilities.helpers.BaseUtils;
import com.app.jobfizzerxp.utilities.helpers.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import static com.app.jobfizzerxp.utilities.helpers.UrlHelper.UPDATE_LOCATION;

/**
 * Created by user on 02-11-2017.
 */

public class LocationService extends Service {
    private static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60;
    private LocationManager locationManager;
    private MyLocationListener listener;
    private Location previousBestLocation = null;
    private int value = 0;
    private AppSettings appSettings = new AppSettings(LocationService.this);

    private Intent intent;
    private String TAG = LocationService.class.getSimpleName();
    private Socket socket;


    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        BaseUtils.log(TAG, "Started :Service ");
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        final Handler handler = new Handler();
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        try {
            socket = IO.socket(UrlHelper.LOCATION_SOCKET, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            BaseUtils.log("SOCKET.IO ", e.getMessage());
        }
        socket.connect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat_a);
        locationA.setLongitude(lng_a);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat_b);
        locationB.setLongitude(lng_b);
        float distance = locationA.distanceTo(locationB);
        BaseUtils.log(TAG, "meterDistanceBetweenPoints: " + distance);

        return (int) distance;
    }


    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
//            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        float old_lat = (float) location.getLatitude();
        float old_lng = (float) location.getLongitude();
        float new_lat = (float) currentBestLocation.getLatitude();
        float new_lng = (float) currentBestLocation.getLongitude();
        return meterDistanceBetweenPoints(old_lat, old_lng, new_lat, new_lng) > 200;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseUtils.log(TAG, "STop SErvice" + "DONE");
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {

            if (isBetterLocation(loc, previousBestLocation)) {

                loc.getLatitude();
                loc.getLongitude();
                if (value == 0) {
                    previousBestLocation = loc;
                    value++;
                } else {

                    if (meterDistanceBetweenPoints(loc.getLatitude(), loc.getLongitude(), previousBestLocation.getLatitude(), previousBestLocation.getLongitude()) > 200) {
                        previousBestLocation = loc;
                    }
                }
                BaseUtils.log(TAG, "Latitude: " + loc.getLatitude());
                BaseUtils.log(TAG, "Longitude: " + loc.getLongitude());
                JSONObject jsonObject = new JSONObject();

                appSettings.setLatitude(String.valueOf(loc.getLatitude()));
                appSettings.setLongitude(String.valueOf(loc.getLongitude()));

                try {
                    jsonObject.put("latitude", "" + loc.getLatitude());
                    jsonObject.put("longitude", "" + loc.getLongitude());
                    jsonObject.put("bearing", "" + loc.getBearing());
                    jsonObject.put("provider_id", appSettings.getProviderId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit(UPDATE_LOCATION, jsonObject);

            }
        }

        public void onProviderDisabled(String provider) {
        }


        public void onProviderEnabled(String provider) {
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}