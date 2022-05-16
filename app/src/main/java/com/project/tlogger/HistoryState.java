package com.project.tlogger;

public class HistoryState {

    private String nfcId; // nfcId
    private String state;  // measurement state
    private int measurmentIcon; // icon

    public HistoryState(String nfcId, String state, int measurmentIcon){

        this.nfcId=nfcId;
        this.state=state;
        this.measurmentIcon=measurmentIcon;
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

}
