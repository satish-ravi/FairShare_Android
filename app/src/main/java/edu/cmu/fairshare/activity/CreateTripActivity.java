package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void chooseFriends(View v) {
        Request request = Request.newMyFriendsRequest(
                Session.getActiveSession(),
                new Request.GraphUserListCallback() {

                    @Override
                    public void onCompleted(List<GraphUser> users, Response response) {
                        System.out.println("Users: " + users);
                        for(int i=0; i<users.size();i++) {
                            ViewFriendsActivity.app_users.add(users.get(i));
                        }
                    }
                });
        request.executeAsync();

        Intent intent = new Intent(this, ViewFriendsActivity.class);
        startActivity(intent);

    }
}
