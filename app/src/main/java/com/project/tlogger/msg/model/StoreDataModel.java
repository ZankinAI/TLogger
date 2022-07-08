package com.project.tlogger.msg.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StoreDataModel implements Cloneable{
    public Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG responseConfigData;
    public long dataTime;
    public String nfcId;
    public String textStatus;
    public String apiVersion;
    public int retrievedCount;
    public int statusOfMeasured;
    public String data;

    public StoreDataModel(){

    }

    public StoreDataModel(Cursor userCursor){
        this.responseConfigData = new Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG();
        this.nfcId = userCursor.getString(1);
        this.dataTime = userCursor.getLong(2);
        this.textStatus = userCursor.getString(3);
        this.apiVersion = userCursor.getString(5);
        this.responseConfigData.configTime = userCursor.getInt(6);
        this.responseConfigData.count = userCursor.getShort(7);
        this.responseConfigData.interval = userCursor.getShort(8);
        this.retrievedCount = userCursor.getInt(9);
        this.statusOfMeasured = userCursor.getInt(10);
        this.responseConfigData.validMinimum = userCursor.getShort(11);
        this.responseConfigData.validMaximum = userCursor.getShort(12);
        this.responseConfigData.attainedMinimum = userCursor.getShort(13);
        this.responseConfigData.attainedMaximum = userCursor.getShort(14);
        this.responseConfigData.status = userCursor.getInt(15);
        this.data = userCursor.getString(16);

    }

    public StoreDataModel clone() throws CloneNotSupportedException{
        StoreDataModel newStoreDataModel = (StoreDataModel) super.clone();
        newStoreDataModel.responseConfigData = (Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG) responseConfigData.clone();

        return newStoreDataModel;
    }



}
