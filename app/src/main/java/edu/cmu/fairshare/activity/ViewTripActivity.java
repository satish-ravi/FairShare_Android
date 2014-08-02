package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.adapter.ViewTripAdapter;
import edu.cmu.fairshare.model.Trip;

public class ViewTripActivity extends ListActivity {
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_trip);
        session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            ViewTripAdapter tripArrayAdapter = new ViewTripAdapter(this);
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
            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject item = (ParseObject) getListAdapter().getItem(position);
        Intent intent = new Intent(this, TripDetails.class);
        intent.putExtra("tripID", item.getObjectId());
        startActivity(intent);
        Toast.makeText(this, item.getObjectId() + " selected", Toast.LENGTH_LONG).show();
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
}
