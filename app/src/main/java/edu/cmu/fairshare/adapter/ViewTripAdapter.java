package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.Trip;

/**
 * Created by dil on 7/26/14.
 */
public class ViewTripAdapter extends ArrayAdapter<Trip> {
    private Activity context;
    private ArrayList<Trip> tripList;

    public ViewTripAdapter(Activity context, ArrayList<Trip> tripList) {
        super(context, R.layout.activity_view_trip,tripList);
        this.context = context;
        this.tripList = tripList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.activity_view_trip, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.labelText = (TextView) view.findViewById(R.id.trip_label);
            viewHolder.dateText = (TextView) view.findViewById(R.id.trip_date);
            viewHolder.locationText = (TextView) view.findViewById(R.id.trip_location);
            view.setTag(viewHolder);
            viewHolder.labelText.setTag(tripList.get(position).getTripName());
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).labelText.setTag(tripList.get(position).getTripName());
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.labelText.setText(tripList.get(position).getTripName());
        holder.dateText.setText(getDate(tripList.get(position).getDate()));
        holder.locationText.setText(tripList.get(position).getStartLocation()+" - "+tripList.get(position).getEndLocation());
        return view;
    }

    public String getDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(date);
    }

    static class ViewHolder {
        protected TextView labelText;
        protected TextView dateText;
        protected TextView locationText;
    }
}
