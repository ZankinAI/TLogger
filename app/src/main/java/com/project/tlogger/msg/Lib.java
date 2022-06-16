package com.project.tlogger.msg;

import com.project.tlogger.msg.model.Protocol;

public class Lib {

    public Protocol protocol;
    public String textStatus;
    public String apiVersion;
    public String mimeType;

    public short[] measuredData;



    public boolean flagTloggerConnected;
    public boolean flagOpenFragmentFromHistory;


    public Lib(){
        this.protocol = new Protocol();
        this.textStatus = " ";
        this.apiVersion = "n.a.";
        this.mimeType = "MIME type: n.a.";
        this.flagTloggerConnected = false;
        this.flagOpenFragmentFromHistory = false;

    }


}
