package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.Trip;

/**
 * Created by dil on 7/26/14.
 */
public class ViewTripAdapter extends ParseQueryAdapter<ParseObject> {
    private Activity context;
    private ArrayList<Trip> tripList;

    public ViewTripAdapter(Activity context) {
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Trip");
//                query.whereEqualTo("highPri", true);
                return query;
            }
        });
        this.context = context;
        this.tripList = tripList;
    }

    @Override
    public View getItemView(ParseObject parseObject, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.activity_view_trip, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.labelText = (TextView) view.findViewById(R.id.trip_label);
            viewHolder.dateText = (TextView) view.findViewById(R.id.trip_date);
            viewHolder.locationText = (TextView) view.findViewById(R.id.trip_location);
            view.setTag(viewHolder);
            viewHolder.labelText.setTag(parseObject.getString("trip_name"));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).labelText.setTag(parseObject.getString("trip_name"));
        }
        for (int i = 0; i<getCount();i++){
            if(parseObject.equals((ParseObject)getItem(i))){
                Log.i("Inside","Success");
            }
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.labelText.setText(parseObject.getString("trip_name"));
        holder.dateText.setText(String.valueOf(parseObject.getDate("tripDate")));
        holder.locationText.setText(parseObject.getString("start_location")+" = "+parseObject.getString("end_location"));
        return view;
    }



    static class ViewHolder {
        protected TextView labelText;
        protected TextView dateText;
        protected TextView locationText;
    }
}
