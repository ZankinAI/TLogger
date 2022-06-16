package com.project.tlogger.msg;

import com.project.tlogger.msg.model.Protocol;

public class Lib {

    public Protocol protocol;
    public String textStatus;
    public String apiVersion;
    public String mimeType;
    public String nfcId;
    public int configTime;
    public int runningTime;

    public short count;
    public byte measurementsCount;

    public short[] measuredData;



    public boolean flagTloggerConnected;
    public boolean flagOpenFragmentFromHistory;
    public short interval;
    public short validMinimum;
    public short validMaximum;


    public Lib(){
        this.protocol = new Protocol();
        this.textStatus = " ";
        this.apiVersion = "n.a.";
        this.mimeType = "MIME type: n.a.";
        this.flagTloggerConnected = false;
        this.flagOpenFragmentFromHistory = false;
        this.count = 0;
        this.configTime = 0;
        this.runningTime = 0;
        this.interval = 0;
        this.validMinimum = 0;
        this.validMaximum = 0;

    }


}
