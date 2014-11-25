package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

/**
 * Created by dil on 7/29/14.
 */
public class ViewTripActivity extends Activity {
    Session session;
    ViewTripAdapter tripArrayAdapter;
    ArrayList<Trip> trips;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView tripListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            trips = new ArrayList<Trip>();
            getTripData();
            tripArrayAdapter = new ViewTripAdapter(this,trips);
            tripListView = (ListView)findViewById(R.id.trip_list);
            tripListView.setAdapter(tripArrayAdapter);
            swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            getTripData();
                        }
                    }, 5000);
                }
            });
            tripListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int topRowVerticalPosition =
                            (tripListView == null || tripListView.getChildCount() == 0) ?
                                    0 : tripListView.getChildAt(0).getTop();
                    swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                }
            });
            tripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Trip item = (Trip) tripArrayAdapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), TripDetails.class);
                    intent.putExtra("tripID", item.getObjectId());
                    intent.putExtra("tripName",item.getTripName());
                    startActivity(intent);
                }
            });
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
        if(id==R.id.action_logout){
            ParseUser.logOut();
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
                    trips.clear();
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
