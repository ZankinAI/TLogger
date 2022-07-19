package com.project.tlogger.msg;

import com.project.tlogger.msg.model.MeasurementStatusModel;
import com.project.tlogger.msg.model.Protocol;
import com.project.tlogger.msg.model.StoreDataModel;
import com.project.tlogger.msg.model.TemperatureStatusModel;

import java.util.ArrayList;
import java.util.List;

public class Lib {

    public Protocol protocol;
    public String textStatus;
    public String apiVersion;
    public String mimeType;
    public String nfcId;
    public int configTime;
    public int runningTime;

    public short count;
    public short measurementsCount;
    public short currentMeasurementsCount;

    public boolean setConfigurationFlag = false;
    public boolean resetFlag = false;

    public short[] measuredData;

    public boolean flagTloggerConnected;
    public boolean flagTloggerCurrentConnect;
    public boolean flagOpenFragmentFromHistory;
    public boolean flagStandartMessageReceive;
    public boolean flagReadFromDB;

    public short interval;
    public short validMinimum;
    public short validMaximum;
    public short attainedMinimunm;
    public short attainedMaximum;
    public int status;
    public int measuredStatus;
    public byte language;

    public MeasurementStatusModel measurementStatus;
    public TemperatureStatusModel temperatureStatus;

    public Protocol.TLOGGER_MSG_CMD_SETCONFIG cmdSetConfig;
    public Protocol.MSG_ERR msgErr;
    public Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG rspGetConfig;

    public StoreDataModel storeData;
    public StoreDataModel selectedStoreData;
    public List<StoreDataModel> storeDataModelList;

    public Lib(){
        this.storeDataModelList = new ArrayList<StoreDataModel>();
        this.cmdSetConfig = new Protocol.TLOGGER_MSG_CMD_SETCONFIG();
        this.rspGetConfig = new Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG();
        this.protocol = new Protocol();
        this.storeData = new StoreDataModel();
        this.selectedStoreData = new StoreDataModel();
        this.textStatus = " ";
        this.apiVersion = "n.a.";
        this.mimeType = "MIME type: n.a.";
        this.flagTloggerCurrentConnect = false;
        this.flagTloggerConnected = false;
        this.flagOpenFragmentFromHistory = false;
        this.flagStandartMessageReceive = false;
        this.flagReadFromDB = false;
        this.count = 0;
        this.configTime = 0;
        this.runningTime = 0;
        this.interval = 0;
        this.validMinimum = 0;
        this.validMaximum = 0;
        this.status = 0;
        this.measuredStatus=0;
        this.measurementStatus = new MeasurementStatusModel();
        this.temperatureStatus = new TemperatureStatusModel();
        this.msgErr = Protocol.MSG_ERR.MSG_ERR_UNKNOWN_ERROR;
    }
}

