package edu.cmu.fairshare;

/**
 * Created by dil on 8/5/14.
 */
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.PushService;

import edu.cmu.fairshare.activity.MyActivity;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.TripUser;

public class Application extends android.app.Application {

    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Parse SDK.
        ParseObject.registerSubclass(Trip.class);
        ParseObject.registerSubclass(TripUser.class);
        Parse.initialize(this, "8670VmAcvjt4wvLbpEQWTWviW8WIpDNePGUdCvUA", "vTGi3cFFJDjK0Glbx2Z4Z8B5SHlG2MiczHShlo04");
        ParseFacebookUtils.initialize(getString(R.string.appID));

        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, MyActivity.class);
    }
}
