package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.Trip;
import edu.cmu.fairshare.model.User;

/**
 * Created by dil on 7/29/14.
 */
public class TripDetailsAdapter extends ArrayAdapter<User> {
    private Activity context;
    private ArrayList<User> tripList;
    private ArrayList<Integer> selectedItemArray;

    public ArrayList<Integer> getSelectedItemArray() {
        return selectedItemArray;
    }

    public void setSelectedItemArray(ArrayList<Integer> selectedItemArray) {
        this.selectedItemArray = selectedItemArray;
    }

    public TripDetailsAdapter(Activity context, ArrayList<User> tripList) {
        super(context, R.layout.activity_view_trip,tripList);
        this.context = context;
        this.tripList = tripList;
        Log.i("Trip_size", String.valueOf(tripList.size()));
        selectedItemArray = new ArrayList<Integer>(tripList.size());
        for (int i=0; i<tripList.size();i++){
            selectedItemArray.add(0);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.trip_details_items, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.profilePic = (ImageView) view.findViewById(R.id.user_pic_id);
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
        holder.userText.setText(tripList.get(position).getUserName());
        holder.startLocationText.setText(tripList.get(position).getStartLocation());
        holder.endLocationText.setText(tripList.get(position).getDropLocation());
        holder.costText.setText("$"+Double.toString(tripList.get(position).getCost()));
        holder.distanceText.setText(Double.toString(tripList.get(position).getDistance())+"miles");
        if(selectedItemArray!=null && selectedItemArray.size()>0 && selectedItemArray.get(position)==1){
            view.setBackgroundResource(R.drawable.green_gradiant);
        }else{
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }



    static class ViewHolder {
        protected ImageView profilePic;
        protected TextView userText;
        protected TextView startLocationText;
        protected TextView endLocationText;
        protected TextView costText;
        protected TextView distanceText;
    }
}
