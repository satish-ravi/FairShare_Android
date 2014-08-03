package edu.cmu.fairshare.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseException;
import java.util.Arrays;
import java.util.List;
import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.TripUser;


public class MyActivity extends Activity {
    private Dialog progressDialog;
    static final String TAG = "FairShare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(TripUser.class);
        Parse.initialize(this, "8670VmAcvjt4wvLbpEQWTWviW8WIpDNePGUdCvUA", "vTGi3cFFJDjK0Glbx2Z4Z8B5SHlG2MiczHShlo04");
        ParseFacebookUtils.initialize(getString(R.string.appID));
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            showUserDetailsActivity();
        }
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

    public void logInFb(View view){
        MyActivity.this.progressDialog = ProgressDialog.show(
                MyActivity.this, "", "Logging in...", true);
        List<String> permissions = Arrays.asList("public_profile",
                "user_friends","user_location");
        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                MyActivity.this.progressDialog.dismiss();
                if (user == null) {
                    Log.d(MyActivity.TAG,
                            "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(MyActivity.TAG,
                            "User signed up and logged in through Facebook!");
                    getFacebookIdInBackground();
                    showUserDetailsActivity();
                } else {
                    Log.d(MyActivity.TAG,
                            "User logged in through Facebook!");
                    getFacebookIdInBackground();
                    showUserDetailsActivity();
                }
            }
        });
    }

    private static void getFacebookIdInBackground() {
        Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    ParseUser.getCurrentUser().put("fbId", user.getId());
                    ParseUser.getCurrentUser().put("displayName", user.getFirstName()+" "+user.getLastName());
                    ParseUser.getCurrentUser().saveInBackground();
                }
            }
        });
    }

    private void showUserDetailsActivity() {
        Intent intent = new Intent(this, ViewTripActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
