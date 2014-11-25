package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.TripDetailsAdapter;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.TripUser;
import edu.cmu.fairshare.service.LocationService;

/**
 * Created by dil on 7/29/14.
 */
public class TripDetails extends Activity {
    Session session;
    TripDetailsAdapter userArrayAdapter;
    ArrayList<TripUser> tripUsersList;
    String trip;
    String tripName;
    Trip currentTrip;
    String locationText;
    MenuItem menuItem;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        TextView labelTextView = (TextView) findViewById(R.id.trip_label);
        session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            Intent intent = getIntent();
            trip = (String)intent.getSerializableExtra("tripID");
            tripName = (String) intent.getSerializableExtra("tripName");
            labelTextView.setText(tripName);
            final ListView tripDetailsListView = (ListView) findViewById(R.id.details_list_id);
            tripUsersList = new ArrayList<TripUser>();
            getTripUserData(trip);
            userArrayAdapter = new TripDetailsAdapter(this,tripUsersList);
            tripDetailsListView.setAdapter(userArrayAdapter);
            tripDetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(tripUsersList.get(position).getStartLocation()==null){
                        getLocation(0, position);
                    }
                    else if(tripUsersList.get(position).getEndLocation()==null){
                        getLocation(1, position);
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.action_logout){
            ParseUser.logOut();
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
        if(id==R.id.edit){
            Intent intent = new Intent(this, TripDetailsEditActivity.class);
            intent.putExtra("tripID",trip);
            intent.putExtra("tripName",tripName);
            startActivity(intent);
        }
        if (id==R.id.total){
            getTripUserData(trip);
            if (isLocationsUpdated()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Total");
                alert.setMessage("Enter The Trip amount");
                final EditText input = new EditText(this);
                input.setSingleLine();
                input.setText(""+currentTrip.getCost());
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        double value = Double.parseDouble(input.getText().toString());
                        currentTrip.setCost(value);

                        currentTrip.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                tripUsersList.clear();
                                getTripUserData(trip);
                                if(menuItem!=null && currentTrip.getCost()>0) {
                                    menuItem.setTitle("$"+currentTrip.getCost());
                                }
                            }
                        });
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
            }
            else
                Toast.makeText(this,"Please complete the trip to enter total",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.total);
        if(isLocationsUpdated()) {
            if(currentTrip.getCost()>0) {
                menuItem.setTitle("$"+currentTrip.getCost());
            }
        }
        return super.onPrepareOptionsMenu(menu);

    }
    @Override
    public void onBackPressed() {
        if(session!=null && session.isOpened()) {
            Intent setIntent = new Intent(this,ViewTripActivity.class);
            startActivity(setIntent);
        }
    }

    private void getTripUserData(String tripId){
        ParseQuery<TripUser> query = ParseQuery.getQuery("TripUser");
        query.orderByAscending("displayName");
        ParseObject object = ParseObject.create("Trip");
        object.setObjectId(tripId);
        query.whereEqualTo("tripId",object);
        query.findInBackground(new FindCallback<TripUser>() {
            public void done(List<TripUser> usersList, ParseException e) {
                if (e == null) {
                    tripUsersList.clear();
                    tripUsersList.addAll(usersList);
                    userArrayAdapter.notifyDataSetChanged();
                    for(int i = 0; i<tripUsersList.size();i++){
                        userArrayAdapter.getSelectedItemArray().add(0);
                    }
                } else {
                    Log.i("Error", e.toString());
                    Toast.makeText(getApplicationContext(),"Error Retrieving data",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ParseQuery<Trip> query2 = ParseQuery.getQuery("Trip");
        query2.whereEqualTo("objectId", tripId);
        try {
            currentTrip = query2.find().get(0);
            if (isLocationsUpdated() && menuItem!=null)
                menuItem.setVisible(true);
            if(currentTrip.getCost()>0 && menuItem!=null)
                menuItem.setTitle("$"+currentTrip.getCost());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isLocationsUpdated(){
        return currentTrip != null && currentTrip.getStartLocation() != null && currentTrip.getEndLocation() != null;
    }

    private void getLocation(final int type, final int position) {
        ParseGeoPoint.getCurrentLocationInBackground(100000
                , new Criteria(), new LocationCallback() {
            @Override
            public void done(ParseGeoPoint parseGeoPoint, ParseException e) {
                TripUser tripUser = tripUsersList.get(position);
                double latitude = 0;
                double longitude = 0;
                if (parseGeoPoint != null) {
                    latitude = parseGeoPoint.getLatitude();
                    longitude = parseGeoPoint.getLongitude();
                    String address = getGeo(latitude, longitude);
                    if (type == 0) {
                        tripUser.setStartLocGeo(parseGeoPoint);
                        tripUser.setStartLocation(address);
                    } else {
                        tripUser.setEndLocGeo(parseGeoPoint);
                        tripUser.setEndLocation(address);
                    }
                    tripUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            userArrayAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private String getGeo(double latitude, double longitude) {
        Geocoder gc;
        gc = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gc.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();

        if (addresses.size() > 0) {
            Address address = addresses.get(0);

            sb.append(address.getAddressLine(0));

            locationText = sb.toString();
        }
        return sb.toString();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getTripUserData(trip);
        if (isLocationsUpdated() && menuItem!=null)
            menuItem.setVisible(true);
        if(currentTrip.getCost()>0 && menuItem!=null)
            menuItem.setTitle("$"+currentTrip.getCost());
    }
}
