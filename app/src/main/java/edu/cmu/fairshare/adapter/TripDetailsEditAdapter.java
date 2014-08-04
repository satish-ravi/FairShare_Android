package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.widget.ProfilePictureView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.TripUser;

/**
 * Created by dil on 7/30/14.
 */
public class TripDetailsEditAdapter extends BaseExpandableListAdapter implements TextWatcher {
    private Activity context;
    private ArrayList<TripUser> tripList;
    private HashMap<TripUser, ArrayList<String>> tripMap;
    public ArrayList<EditText> editTextStartList = new ArrayList<EditText>();
    public ArrayList<EditText> editTextEndList = new ArrayList<EditText>();
    public ArrayList<Integer> selectedList = new ArrayList<Integer>();

    public TripDetailsEditAdapter(Activity context, ArrayList<TripUser> tripList, HashMap<TripUser, ArrayList<String>> tripMap){
        this.context = context;
        this.tripList = tripList;
        this.tripMap = tripMap;
    }

    @Override
    public int getGroupCount() {
        return tripList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.tripMap.get(this.tripList.get(groupPosition))
                .size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.tripMap.get(this.tripList.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return tripList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition+childPosition;
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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
//        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.expandable_list_items, null);
            ChildViewHolder viewHolder = new ChildViewHolder();
            viewHolder.startLocationEdit = editTextStartList.get(groupPosition);
            viewHolder.startLocationEdit.addTextChangedListener(this);
            view.setTag(viewHolder);
            viewHolder.startLocationEdit.setTag(tripList.get(groupPosition));
//            Log.i("group", String.valueOf(groupPosition));
//        } else {
//            view = convertView;
//            ((ChildViewHolder) view.getTag()).startLocationEdit.setTag(tripList.get(groupPosition));
//        }
        Log.i("group", String.valueOf(groupPosition));
        return view;
    }
    public String decimalFormatter(double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

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
        protected EditText startLocationEdit;
        protected EditText endLocationEdit;
    }
}
