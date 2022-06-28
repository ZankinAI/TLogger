package com.project.tlogger.msg;

import com.project.tlogger.msg.model.MeasurementStatusModel;
import com.project.tlogger.msg.model.Protocol;
import com.project.tlogger.msg.model.TemperatureStatusModel;

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

    public boolean setConfigurationFlag = false;

    public short[] measuredData;

    public boolean flagTloggerConnected;
    public boolean flagOpenFragmentFromHistory;
    public short interval;
    public short validMinimum;
    public short validMaximum;
    public short attainedMinimunm;
    public short attainedMaximum;

    public MeasurementStatusModel measurementStatus;
    public TemperatureStatusModel temperatureStatus;

    public Protocol.TLOGGER_MSG_CMD_SETCONFIG cmdSetConfig;

    public Lib(){
        this.cmdSetConfig = new Protocol.TLOGGER_MSG_CMD_SETCONFIG();
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
        this.measurementStatus = new MeasurementStatusModel();
        this.temperatureStatus = new TemperatureStatusModel();
    }
}

