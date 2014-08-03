package edu.cmu.fairshare.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.User;

public class ViewFriendsActivity extends Activity {

    private ListView mainListView ;
    public static ArrayList<GraphUser> app_users = new ArrayList<GraphUser>();
    private FBUser[] FBUsers;
    private ArrayAdapter<FBUser> listAdapter ;
    Session session;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_main);
        session = ParseFacebookUtils.getSession();

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainListView );

        // When item is tapped, toggle checked properties of CheckBox and FBUser.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                FBUser FBUser = listAdapter.getItem( position );
                FBUser.toggleChecked();
                FriendViewHolder viewHolder = (FriendViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked( FBUser.isChecked() );
            }
        });

        ArrayList<FBUser> FBUserList = new ArrayList<FBUser>();
        // Create and populate FBUsers.
        if ( FBUsers == null ) {
            for(int i=0; i< app_users.size();i++) {
                FBUserList.add(new FBUser(app_users.get(i).getName(),app_users.get(i).getId()));
            }
        }

//        FBUserList.addAll(Arrays.asList(FBUsers));

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new FriendArrayAdapter(this, FBUserList);
        mainListView.setAdapter(listAdapter);
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

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        private ImageView imageView;
        public FriendViewHolder() {}
        public FriendViewHolder(TextView textView, CheckBox checkBox,ImageView imageView) {
            this.checkBox = checkBox ;
            this.textView = textView ;
            this.imageView = imageView;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    /** Custom adapter for displaying an array of FBUser objects. */
    private static class FriendArrayAdapter extends ArrayAdapter<FBUser> {

        private LayoutInflater inflater;

        public FriendArrayAdapter(Context context, List<FBUser> FBUserList) {
            super( context, R.layout.simplerow, R.id.rowTextView, FBUserList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // FBUser to display
            FBUser FBUser = (FBUser) this.getItem( position );

            // The child views in each row.
            CheckBox checkBox ;
            TextView textView ;
            ImageView imageView;

            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.simplerow, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.rowTextView );
                checkBox = (CheckBox) convertView.findViewById( R.id.CheckBox01 );
                imageView = (ImageView) convertView.findViewById(R.id.com_facebook_picker_profile_pic_stub);

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new FriendViewHolder(textView,checkBox,imageView) );

                // If CheckBox is toggled, update the FBUser it is tagged with.
                checkBox.setOnClickListener( new View.OnClickListener() {
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
                FriendViewHolder viewHolder = (FriendViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
                imageView = viewHolder.getImageView();
            }

            // Tag the CheckBox with the FBUser it is displaying, so that we can
            // access the FBUser in onClick() when the CheckBox is toggled.
            checkBox.setTag(FBUser);

            // Display FBUser data
            checkBox.setChecked( FBUser.isChecked() );
            textView.setText( FBUser.getName() );
            imageView.setImageBitmap(getFacebookProfilePicture(FBUser.getID()));

           // imageView.setImageDrawable(getFacebookProfilePicture(FBUser.getID()));

            return convertView;
        }

    }

    public Object onRetainNonConfigurationInstance() {
        return FBUsers;
    }
    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;

        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=small");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void onDone(View v) {
        CheckBox cb;
        int count = mainListView.getAdapter().getCount();
        User newUser = new User();
        for(int i =0; i< count;i++) {
            cb = (CheckBox) mainListView.getChildAt(i).findViewById(R.id.CheckBox01);
            if(cb.isChecked()) {
                newUser.setUserName( mainListView.getChildAt(i).findViewById(R.id.rowTextView).toString());
                TripDetails.list.add(newUser);
            }
        }

        Intent intent = new Intent(this , TripDetails.class);
        startActivity(intent);
    }
}