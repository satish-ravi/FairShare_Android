package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.PlacesAutoCompleteAdapter;
import edu.cmu.fairshare.adapter.TripDetailsEditAdapter;
import edu.cmu.fairshare.model.TripUser;
import edu.cmu.fairshare.model.User;

public class TripDetailsEditActivity extends Activity  {
    TripDetailsEditAdapter tripExpandableListAdapter;
    ArrayList<TripUser> tripUsersList;
    String trip;
    String tripName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_edit);
        TextView labelTextView = (TextView) findViewById(R.id.trip_label);
        ExpandableListView tripExpandableListView = (ExpandableListView)findViewById(R.id.expandable_list_id);
        tripUsersList = new ArrayList<TripUser>();
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTripUserData(String tripId){
        ParseQuery<TripUser> query = ParseQuery.getQuery("TripUser");
        ParseObject object = ParseObject.create("Trip");
        object.setObjectId(tripId);
        query.whereEqualTo("tripId",object);
        query.findInBackground(new FindCallback<TripUser>() {
            public void done(List<TripUser> usersList, ParseException e) {
                if (e == null) {
                    Log.i("Inside", String.valueOf(usersList.size()));
                    tripUsersList.addAll(usersList);
                    tripExpandableListAdapter.notifyDataSetChanged();
                } else {
                    Log.i("Error", e.toString());
                    Toast.makeText(getApplicationContext(), "Error Retrieving data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
