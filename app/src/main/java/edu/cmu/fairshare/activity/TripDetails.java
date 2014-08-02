package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.ParseFacebookUtils;

import java.util.ArrayList;
import java.util.zip.Inflater;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.TripDetailsAdapter;
import edu.cmu.fairshare.adapter.ViewTripAdapter;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.User;

public class TripDetails extends Activity {
    Session session;
    int itemPosition;
    TripDetailsAdapter userArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            Intent intent = getIntent();
            String tripID = (String)intent.getSerializableExtra("tripID");
            final ListView tripDetailsListView = (ListView) findViewById(R.id.details_list_id);
            userArrayAdapter = new TripDetailsAdapter(this,getModel());
            tripDetailsListView.setAdapter(userArrayAdapter);
            LayoutInflater inflater = getLayoutInflater();
            tripDetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    User item = (User) userArrayAdapter.getItem(position);
                    if(userArrayAdapter.getSelectedItemArray().get(position)==1)
                        userArrayAdapter.getSelectedItemArray().set(position,0);
                    else
                        userArrayAdapter.getSelectedItemArray().set(position,1);
                    userArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), item.getUserName() + " selected "+(position), Toast.LENGTH_LONG).show();
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
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.edit){
            Intent intent = new Intent(this, TripDetailsEditActivity.class);
            startActivity(intent);
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

        return list;
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
