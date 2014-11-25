package edu.cmu.fairshare.activity;

import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.TripUser;


public class ViewFriendsActivity extends Activity {

    private ListView mainListView ;
    public static ArrayList<GraphUser> app_users = new ArrayList<GraphUser>();
    private FBUser[] FBUsers;
    private ArrayAdapter<FBUser> listAdapter ;
    ArrayList<FBUser> FBUserList;
    Session session;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_main);
        session = ParseFacebookUtils.getSession();

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainListView );
        FBUserList = new ArrayList<FBUser>();
        // Create and populate FBUsers.
            for(int i=0; i< app_users.size();i++) {
                FBUserList.add(new FBUser(app_users.get(i).getName(),app_users.get(i).getId()));
            }

//        FBUserList.addAll(Arrays.asList(FBUsers));

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new FriendArrayAdapter(this, FBUserList);
        mainListView.setAdapter(listAdapter);

        // When item is tapped, toggle checked properties of CheckBox and FBUser.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                FBUser FBUser = listAdapter.getItem( position );
                FBUser.toggleChecked();
                FriendViewHolder viewHolder = (FriendViewHolder) item.getTag();
                viewHolder.checkBox.setChecked( FBUser.isChecked() );
            }
        });
    }

    /** Holds Friends data. */
    private static class FBUser {
        private String name = "" ;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        private String ID = "";
        private boolean checked = false ;
        public FBUser() {}
        public FBUser(String name,String ID) {
            this.name = name ;
            this.ID = ID;
        }
        public FBUser(String name, boolean checked) {
            this.name = name ;
            this.checked = checked ;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public boolean isChecked() {
            return checked;
        }
        public void setChecked(boolean checked) {
            this.checked = checked;
        }
        public String toString() {
            return name ;
        }
        public void toggleChecked() {
            checked = !checked ;
        }
    }

    /** Holds child views for one row. */
    private static class FriendViewHolder {
        private CheckBox checkBox ;
        private TextView textView ;
        private ProfilePictureView imageView;
    }

    /** Custom adapter for displaying an array of FBUser objects. */
    private static class FriendArrayAdapter extends ArrayAdapter<FBUser> {

        private LayoutInflater inflater;
        private ArrayList<FBUser> fbUserArrayList;
        public FriendArrayAdapter(Context context, ArrayList<FBUser> FBUserList) {
            super( context, R.layout.simplerow, R.id.rowTextView, FBUserList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
            fbUserArrayList = FBUserList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // FBUser to display

            // The child views in each row.
            View view = null;
            // Create a new row view
            if ( convertView == null ) {
                view = inflater.inflate(R.layout.simplerow, null);
                final FriendViewHolder friendViewHolder = new FriendViewHolder();
                // Find the child views.
                friendViewHolder.textView = (TextView) view.findViewById( R.id.rowTextView );
                friendViewHolder.checkBox = (CheckBox) view.findViewById( R.id.CheckBox01 );
                friendViewHolder.imageView = (ProfilePictureView) view.findViewById(R.id.com_facebook_picker_profile_pic_stub);

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                view.setTag(friendViewHolder);
                friendViewHolder.textView.setTag(fbUserArrayList.get(position));

                // If CheckBox is toggled, update the FBUser it is tagged with.
                friendViewHolder.checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        FBUser FBUser = (FBUser) cb.getTag();
                        FBUser.setChecked( cb.isChecked() );
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                view = convertView;
                ((FriendViewHolder) view.getTag()).textView.setTag(fbUserArrayList.get(position));
            }

            // Tag the CheckBox with the FBUser it is displaying, so that we can
            // access the FBUser in onClick() when the CheckBox is toggled.
            FriendViewHolder friendViewHolder = (FriendViewHolder) view.getTag();
            friendViewHolder.checkBox.setTag(fbUserArrayList.get(position));

            // Display FBUser data
            friendViewHolder.checkBox.setChecked( fbUserArrayList.get(position).isChecked() );
            friendViewHolder.textView.setText( fbUserArrayList.get(position).getName() );
            friendViewHolder.imageView.setProfileId(fbUserArrayList.get(position).getID());

           // imageView.setImageDrawable(getFacebookProfilePicture(FBUser.getID()));

            return view;
        }

    }

    public Object onRetainNonConfigurationInstance() {
        return FBUsers;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.done){
            new DataSaveProgress(this).execute();
        }
        if(id==R.id.action_logout){
            ParseUser.logOut();
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDone() {
        CheckBox cb;
        int count = mainListView.getAdapter().getCount();
        Intent intent =getIntent();
        String tripName = (String)intent.getSerializableExtra("tripName");
        final Trip trip = new Trip();
        trip.setTripName(tripName);
        trip.setDate(new Date());
        trip.setCreatedBy(ParseUser.getCurrentUser());
        try {
            trip.save();
            TripUser tripUser = new TripUser();
            tripUser.setTrip(trip);
            tripUser.setCommuterId((String)ParseUser.getCurrentUser().get("fbId"));
            tripUser.setName((String)ParseUser.getCurrentUser().get("displayName"));
            tripUser.save();
            for(int i =0; i< count;i++) {
                cb = (CheckBox) mainListView.getChildAt(i).findViewById(R.id.CheckBox01);
                if(cb.isChecked()) {
                    tripUser = new TripUser();
                    tripUser.setTrip(trip);
                    tripUser.setCommuterId(FBUserList.get(i).getID());
                    tripUser.setName(FBUserList.get(i).getName());
                    tripUser.save();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseObject object = ParseObject.create("Trip");
        object.setObjectId(trip.getObjectId());
        Intent intent1 = new Intent(this , TripDetails.class);
        Log.i("trip", object.getObjectId());
        intent1.putExtra("tripID",object.getObjectId());
        intent1.putExtra("tripName", trip.getTripName());
        startActivity(intent1);
    }

    private class DataSaveProgress extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private Activity activity;
        public DataSaveProgress(Activity activity){
            this.activity =activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            onDone();
            return true;
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Creating Trip");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}