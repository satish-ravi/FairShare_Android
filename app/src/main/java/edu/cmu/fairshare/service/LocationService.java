package edu.cmu.fairshare.service;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseGeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

/**
 * Created by Sreevatson on 8/3/2014.
 */
public class LocationService {

    static double latitude;
    static double longitude;


    public static ParseGeoPoint getCurrentLocation(Context context) {

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();



        String provider = manager.getBestProvider(criteria, true);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                longitude = location.getLongitude();
                latitude = location.getLatitude();


            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0F, listener );
        }
        else {
            manager.requestLocationUpdates(provider, 0L, 0.0F, listener);
        }
        ParseGeoPoint geoPoint = new ParseGeoPoint(latitude,longitude);

        return geoPoint;
    }


    public static ParseGeoPoint getLocationFromAddress(Context context,String address) {
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList = null;
        double lat=0.0,lng=0.0;
        try {
            addressList = geoCoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addressList != null && addressList.size() > 0) {
            lat = addressList.get(0).getLatitude();
            lng = addressList.get(0).getLongitude();
        }
        ParseGeoPoint geoPoint = new ParseGeoPoint(lat,lng);

        return geoPoint;
    }

    public static String getCurrentAddress(Context context) {
        String locationText="";
        StringBuilder sb = new StringBuilder();
        try {
            Geocoder gc;
            gc = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);




            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                for(int i=0;i<address.getMaxAddressLineIndex();i++);

                sb.append(address.getAddressLine(0));

                locationText = sb.toString();



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(locationText);
        return locationText;
    }
}
