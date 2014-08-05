package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.activity.TripDetails;
import edu.cmu.fairshare.model.TripUser;
import edu.cmu.fairshare.service.LocationService;

/**
 * Created by dil on 7/29/14.
 */
public class TripDetailsAdapter extends ArrayAdapter<TripUser> {
    private Activity context;
    private ArrayList<TripUser> tripList;
    private ArrayList<Integer> selectedItemArray;

    public ArrayList<Integer> getSelectedItemArray() {
        return selectedItemArray;
    }

    public void setSelectedItemArray(ArrayList<Integer> selectedItemArray) {
        this.selectedItemArray = selectedItemArray;
    }

    public TripDetailsAdapter(Activity context, ArrayList<TripUser> tripList) {
        super(context, R.layout.activity_view_trip,tripList);
        this.context = context;
        this.tripList = tripList;
        selectedItemArray = new ArrayList<Integer>(tripList.size());
        for (int i=0; i<tripList.size();i++){
            selectedItemArray.add(0);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        TripDetails trip = new TripDetails();
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.trip_details_items, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.profilePic = (ProfilePictureView) view.findViewById(R.id.user_pic_id);
            viewHolder.userText = (TextView) view.findViewById(R.id.user_name_id);
            viewHolder.startLocationText = (TextView) view.findViewById(R.id.start_id);
            viewHolder.endLocationText = (TextView) view.findViewById(R.id.stop_id);
            viewHolder.costText = (TextView) view.findViewById(R.id.cost_id);
            viewHolder.distanceText = (TextView) view.findViewById(R.id.distance_id);
            view.setTag(viewHolder);
            viewHolder.userText.setTag(tripList.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).userText.setTag(tripList.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.userText.setText(tripList.get(position).getName());
        holder.startLocationText.setText(tripList.get(position).getStartLocation());
        holder.endLocationText.setText(tripList.get(position).getEndLocation());
        holder.costText.setText("$"+decimalFormatter(tripList.get(position).getCost()));
        holder.distanceText.setText(decimalFormatter(tripList.get(position).getDistance() / 1609.344) + " miles");
        holder.profilePic.setProfileId(tripList.get(position).getCommuterId());

        if(tripList.get(position).getStartLocation()!=null && tripList.get(position).getEndLocation()==null) {
            selectedItemArray.set(position, 1);
        }

        if((selectedItemArray!=null && selectedItemArray.size()>0 && selectedItemArray.get(position)==1)   ){
            view.setBackgroundResource(R.drawable.green_gradiant);

                    tripList.get(position).setStartLocGeo(LocationService.getCurrentLocation(context));
                    tripList.get(position).setStartLocation(LocationService.getCurrentAddress(context));
                    tripList.get(position).saveInBackground();
                  }
        else{


            view.setBackgroundColor(Color.TRANSPARENT);
            if(tripList.get(position).getStartLocation()!=null && tripList.get(position).getEndLocation()==null ) {
                tripList.get(position).setEndLocGeo(LocationService.getCurrentLocation(this.getContext()));
                tripList.get(position).setEndLocation(LocationService.getCurrentAddress(this.getContext()));
                tripList.get(position).saveInBackground();
            }

        }

        return view;
    }

    public String decimalFormatter(double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    static class ViewHolder {
        protected ProfilePictureView profilePic;
        protected TextView userText;
        protected TextView startLocationText;
        protected TextView endLocationText;
        protected TextView costText;
        protected TextView distanceText;
    }
}
