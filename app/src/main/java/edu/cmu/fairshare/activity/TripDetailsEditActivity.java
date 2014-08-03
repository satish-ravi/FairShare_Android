package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.TripDetailsEditAdapter;
import edu.cmu.fairshare.model.User;

public class TripDetailsEditActivity extends Activity {
    private Activity context;
    private ArrayList<User> tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_edit);
        ExpandableListView tripExpandableListView = (ExpandableListView)findViewById(R.id.expandable_list_id);
        ExpandableListAdapter tripExpandableListAdapter = new TripDetailsEditAdapter(this,getModel());
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

    private ArrayList<User> getModel() {
        ArrayList<User> list = new ArrayList<User>();
        list.add(get("user 1", "location1", "location_1", 150, 23));
        list.add(get("user 2", "location2", "location_2", 20, 2.3));
        list.add(get("trip 1", "location1", "location_1", 150, 23));
        list.add(get("trip 1", "location1", "location_1", 150, 23));
        list.add(get("user 1", "location1", "location_1", 150, 23));
        list.add(get("user 2", "location2", "location_2", 20, 2.3));
        list.add(get("trip 1", "location1", "location_1", 150, 23));
        list.add(get("trip 1", "location1", "location_1", 150, 23));
        list.add(get("user 1", "location1", "location_1", 150, 23));
        list.add(get("user 2", "location2", "location_2", 20, 2.3));
        list.add(get("trip 1", "location1", "location_1", 150, 23));
        list.add(get("trip 1", "location1", "location_1", 150, 23));

        return TripDetails.list;
    }

    private User get(String userName, String startLocation, String dropLocation, double cost, double distance) {
        User user = new User();
        user.setUserName(userName);
        user.setStartLocation(startLocation);
        user.setDropLocation(dropLocation);
        user.setCost(cost);
        user.setDistance(distance);
        return user;
    }
}
