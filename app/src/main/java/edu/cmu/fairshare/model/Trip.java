package edu.cmu.fairshare.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.Date;

/**
 * Created by dil on 7/27/14.
 */
@ParseClassName("Trip")
public class Trip extends ParseObject {
    public Trip(){

    }

    public String getId() {
        return getString("objectId");
    }

    public void setId(String id) {
        put("objectId", id);
    }

    public double getCost() {
        return getDouble("totalCost");
    }

    public void setCost(double cost) {
        put("totalCost",cost);
    }

    public String getTripName() {
        return getString("tripName");
    }

    public void setTripName(String tripName) {
        put("tripName",tripName);
    }

    public Date getDate() {
        return getDate("tripDate");
    }

    public void setDate(Date date) {
        put("tripDate",date);
    }

    public String getStartLocation() {
        return getString("startLocation");
    }

    public void setStartLocation(String startLocation) {
        put("startLocation",startLocation);
    }

    public String getEndLocation() {
        return getString("endLocation");
    }

    public void setEndLocation(String endLocation) {
        put("endLocation",endLocation);
    }

    public void setCreatedBy(ParseUser parseUser){
        put("createdBy", parseUser);
    }
}
