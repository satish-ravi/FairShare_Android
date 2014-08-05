package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.PlacesAutoCompleteAdapter;
import edu.cmu.fairshare.adapter.TripDetailsAdapter;
import edu.cmu.fairshare.adapter.TripDetailsEditAdapter;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.TripUser;

public class TripDetailsEditActivity extends Activity {
    TripDetailsEditAdapter tripExpandableListAdapter;
    ArrayList<TripUser> tripUsersList;
    HashMap<TripUser, ArrayList<String>> tripStringArrayListHashMap;
    String trip;
    String tripName;
    private Trip currentTrip;
    MenuItem menuItem;
    TripDetailsAdapter userArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_edit);
        TextView labelTextView = (TextView) findViewById(R.id.trip_label);
        ExpandableListView tripExpandableListView = (ExpandableListView)findViewById(R.id.expandable_list_id);
        tripUsersList = new ArrayList<TripUser>();
        tripStringArrayListHashMap = new HashMap<TripUser, ArrayList<String>>();
        Intent intent = getIntent();
        trip = (String)intent.getSerializableExtra("tripID");
        tripName = (String) intent.getSerializableExtra("tripName");
        labelTextView.setText(tripName);
        getTripUserData(trip);
        tripExpandableListAdapter = new TripDetailsEditAdapter(this,tripUsersList);
        tripExpandableListView.setAdapter(tripExpandableListAdapter);
        tripExpandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trip_details_edit, menu);
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
        }if (id==R.id.total){
            if (isLocationsUpdated()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Total");
                alert.setMessage("Enter The Trip amount");

// Set an EditText view to get user input
                final EditText input = new EditText(this);
                input.setSingleLine();
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
                        // Canceled.
                    }
                });

                alert.show();
            }
            else
                Toast.makeText(this,"Please complete the trip to enter total",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.done){
            tripExpandableListAdapter.onDone();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.total);
        if(isLocationsUpdated()) {
            menuItem.setVisible(true);
            if(currentTrip.getCost()>0) {
                menuItem.setTitle("$"+currentTrip.getCost());
            }
        } else {
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);

    }

    private void getTripUserData(String tripId){
        ParseQuery<TripUser> query = ParseQuery.getQuery("TripUser");
        ParseObject object = ParseObject.create("Trip");
        object.setObjectId(tripId);
        query.whereEqualTo("tripId",object);
        query.findInBackground(new FindCallback<TripUser>() {
            public void done(List<TripUser> usersList, ParseException e) {
                if (e == null) {
                    tripUsersList.addAll(usersList);
                    tripExpandableListAdapter.notifyDataSetChanged();
                    for(int i = 0; i<tripUsersList.size();i++){

                        tripExpandableListAdapter.start.add("");
                        tripExpandableListAdapter.end.add("");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error Retrieving data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ParseQuery<Trip> query2 = ParseQuery.getQuery("Trip");
        query2.whereEqualTo("objectId", tripId);
        try {
            currentTrip = query2.find().get(0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isLocationsUpdated(){
        return currentTrip != null && currentTrip.getStartLocation() != null && currentTrip.getEndLocation() != null;
    }
}
