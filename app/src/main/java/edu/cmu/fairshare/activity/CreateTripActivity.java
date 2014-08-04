package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseUser;

import java.util.List;

import edu.cmu.fairshare.R;

public class CreateTripActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_trip, menu);
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
        return super.onOptionsItemSelected(item);
    }

    public void chooseFriends(View v) {

        final EditText newTrip = (EditText) findViewById(R.id.new_trip_name);
        if(newTrip.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "Please enter trip name", Toast.LENGTH_LONG).show();
        }
        else {
            Request request = Request.newMyFriendsRequest(
                    Session.getActiveSession(),
                    new Request.GraphUserListCallback() {

                        @Override
                        public void onCompleted(List<GraphUser> users, Response response) {
                            System.out.println("Users: " + users);
                            ViewFriendsActivity.app_users.clear();
                            for (int i = 0; i < users.size(); i++) {
                                ViewFriendsActivity.app_users.add(users.get(i));
                            }
                            Intent intent = new Intent(getApplicationContext(), ViewFriendsActivity.class);
                            intent.putExtra("tripName",newTrip.getText().toString());
                            startActivity(intent);
                        }
                    }
            );
            request.executeAsync();
        }

    }
}
