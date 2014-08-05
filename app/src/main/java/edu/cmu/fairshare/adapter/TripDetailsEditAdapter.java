package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.TripUser;
import edu.cmu.fairshare.service.LocationService;

/**
 * Created by dil on 7/30/14.
 */
public class TripDetailsEditAdapter extends BaseExpandableListAdapter implements AdapterView.OnItemClickListener {
    private Activity context;
    private ArrayList<TripUser> tripList;
    private HashMap<TripUser, ArrayList<String>> tripMap;
    public ArrayList<EditText> editTextStartList = new ArrayList<EditText>();
    public ArrayList<EditText> editTextEndList = new ArrayList<EditText>();
    public ArrayList<Integer> selectedList = new ArrayList<Integer>();

    public static ArrayList<String> start= new ArrayList<String>();
    public static ArrayList<String> end=new ArrayList<String>();
    int pos=0;

    public TripDetailsEditAdapter(Activity context, ArrayList<TripUser> tripList){
        this.context = context;
        this.tripList = tripList;

    }


    @Override
    public int getGroupCount() {
        return tripList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tripList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;


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
            viewHolder.userText.setTag(tripList.get(groupPosition));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).userText.setTag(tripList.get(groupPosition));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.userText.setText(tripList.get(groupPosition).getName());
        holder.startLocationText.setText(tripList.get(groupPosition).getStartLocation());
        holder.endLocationText.setText(tripList.get(groupPosition).getEndLocation());
        holder.costText.setText("$"+decimalFormatter(tripList.get(groupPosition).getCost()));
        holder.profilePic.setProfileId(tripList.get(groupPosition).getCommuterId());
        holder.distanceText.setText(decimalFormatter(tripList.get(groupPosition).getDistance()/1609.344)+" miles");
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;

        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.expandable_list_items, null);
        ChildViewHolder viewHolder = new ChildViewHolder();
        viewHolder.startLocationEdit = (AutoCompleteTextView) view.findViewById(R.id.start_loc_id);

        if(childPosition == 0) {
            viewHolder.startLocationEdit.setHint("Start Location");
            viewHolder.startLocationEdit.setText(start.get(groupPosition));
        }
        else {
            viewHolder.startLocationEdit.setHint("End Location");
            viewHolder.startLocationEdit.setText(end.get(groupPosition));
        }
        viewHolder.startLocationEdit.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.list_item));
        viewHolder.startLocationEdit.setOnItemClickListener(this);
        view.setTag(viewHolder);
        viewHolder.startLocationEdit.setTag(tripList.get(groupPosition));
        viewHolder.startLocationEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                if(childPosition%2==0) {
                    start.set(groupPosition,str);
                }
                else {
                    end.set(groupPosition,str);
                }
            }
        });
//        Log.i("Pos", ""+pos);
        return view;
    }
    public String decimalFormatter(double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        pos = groupPosition;return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    static class ViewHolder {
        protected ProfilePictureView profilePic;
        protected TextView userText;
        protected TextView startLocationText;
        protected TextView endLocationText;
        protected TextView costText;
        protected TextView distanceText;
    }

    static class ChildViewHolder {
        protected AutoCompleteTextView startLocationEdit;
    }

    public void onDone() {
        for(int i=0; i< getGroupCount();i++) {
            if(start.get(i)!="") {
                tripList.get(i).setStartLocation(start.get(i));
                tripList.get(i).setStartLocGeo(LocationService.getLocationFromAddress(context, start.get(i)));
            }
            if(end.get(i)!="") {
                tripList.get(i).setEndLocation(end.get(i));
                tripList.get(i).setEndLocGeo(LocationService.getLocationFromAddress(context, end.get(i)));
            }
            tripList.get(i).saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    notifyDataSetChanged();
                }
            });

        }

    }
}

