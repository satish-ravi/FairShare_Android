package edu.cmu.fairshare.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.widget.ProfilePictureView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import edu.cmu.fairshare.R;
import edu.cmu.fairshare.model.TripUser;

/**
 * Created by dil on 7/30/14.
 */
public class TripDetailsEditAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private ArrayList<TripUser> tripList;

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
        return 1;
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
        return childPosition;
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
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.expandable_list_items, null);
            ChildViewHolder viewHolder = new ChildViewHolder();
            viewHolder.startLocationEdit = (EditText) view.findViewById(R.id.start_loc_id);
            viewHolder.endLocationEdit = (EditText) view.findViewById(R.id.end_loc_id);
            view.setTag(viewHolder);
            viewHolder.startLocationEdit.setTag(tripList.get(groupPosition));
        } else {
            view = convertView;
            ((ChildViewHolder) view.getTag()).startLocationEdit.setTag(tripList.get(groupPosition));
        }
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
