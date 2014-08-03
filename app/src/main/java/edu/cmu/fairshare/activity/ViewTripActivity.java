package edu.cmu.fairshare.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.HashMap;
import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.ViewTripAdapter;
import edu.cmu.fairshare.model.Trip;

public class ViewTripActivity extends ListActivity {
    Session session;
    ViewTripAdapter tripArrayAdapter;
    ArrayList<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            trips = new ArrayList<Trip>();
            getTripData();
            tripArrayAdapter = new ViewTripAdapter(this,trips);
            setListAdapter(tripArrayAdapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.create_trip){
            Intent intent = new Intent(this, CreateTripActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_logout){
            ParseUser.logOut();
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Trip item = (Trip) getListAdapter().getItem(position);
        Intent intent = new Intent(this, TripDetails.class);
        intent.putExtra("tripID", item.getObjectId());
        intent.putExtra("tripName",item.getTripName());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(session!=null && session.isOpened()) {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

    public void getTripData(){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", ParseUser.getCurrentUser().get("fbId"));
        ParseCloud.callFunctionInBackground("getTripByUser", params, new FunctionCallback<ArrayList<Trip>>() {
            public void done(ArrayList<Trip> result, ParseException e) {
                if (e == null) {
                    trips.addAll(result);
                    tripArrayAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error retrieve data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
