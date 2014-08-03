package edu.cmu.fairshare.model;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by dil on 8/2/14.
 */
@ParseClassName("TripUser")
public class TripUser extends ParseObject {

    public TripUser(){

    }

    public String getId() {
        return getString("objectId");
    }

    public double getCost() {
        return getDouble("cost");
    }

    public void setCost(double cost) {
        put("cost",cost);
    }

    public double getDistance() {
        return getDouble("distance");
    }

    public void setDistance(double distance) {
        put("distance",distance);
    }

    public String getCommuterId() {
        return getString("commuterId");
    }

    public void setCommuterId(String commuterId) {
        put("commuterId",commuterId);
    }

    public String getName() {
        return getString("displayName");
    }

    public void setName(String displayName) {
        put("displayName",displayName);
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

    public ParseGeoPoint getStartLocGeo(){
        return getParseGeoPoint("startLocGeo");
    }

    public void setStartLocGeo(ParseGeoPoint startLocGeo){
        put("startLocGeo",startLocGeo);
    }

    public ParseGeoPoint getEndLocGeo(){
        return getParseGeoPoint("endLocGeo");
    }

    public void setEndLocGeo(ParseGeoPoint endLocGeo){
        put("endLocGeo",endLocGeo);
    }

    public ParseFile getPicture(){
        return getParseFile("picture");
    }

    public void setPicture(ParseFile picture){
         put("picture", picture);
    }
}
