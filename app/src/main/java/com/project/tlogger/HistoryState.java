package com.project.tlogger;

import java.sql.Timestamp;

public class HistoryState {

    private String nfcId; // nfcId
    private String state;  // measurement state
    private int measurmentIcon; // icon
    private String api; //api
    private String time;

    public HistoryState(String nfcId, String state, int measurmentIcon, String api, long timeSeconds){

        this.nfcId=nfcId;
        this.state=state;
        this.measurmentIcon=measurmentIcon;
        this.api = api;

        Timestamp timestamp = new Timestamp(timeSeconds*1000);
        this.time = String.valueOf(timestamp);

    }

    public String getNfcId() {
        return this.nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getMeasurementResource() {
        return this.measurmentIcon;
    }

    public void setMeasurementResource(int measurmentIcon) {
        this.measurmentIcon = measurmentIcon;
    }

    public void setApi(String api) {this.api = api;}

    public String getApi() {return this.api;}

    public void setTime(long timeSeconds) {
        Timestamp timestamp = new Timestamp(timeSeconds*1000);
        this.time = String.valueOf(timestamp);
    }

    public String getTime() {return this.time;}

}
