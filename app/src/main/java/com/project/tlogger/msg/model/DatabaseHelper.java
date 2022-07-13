package com.project.tlogger.msg.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "tempSense.db";
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "StoreData"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_NFCID = "nfcId";
    public static final String COLUMN_DATETIME = "dateTime";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_API = "api";
    public static final String COLUMN_CONFIGTIME = "configTime";
    public static final String COLUMN_NUMBEROFMEASUREMENTS = "numberOfMeasurements";
    public static final String COLUMN_INTERVAL = "interval";
    public static final String COLUMN_RETRIEVEDCOUNT = "retrievedCount";
    public static final String COLUMN_STATUSOFMEASURED = "statusOfMeasured";
    public static final String COLUMN_VALIDMINIMUM = "validMinimum";
    public static final String COLUMN_VALIDMAXIMUM = "validMaximum";
    public static final String COLUMN_ATTAINEDMINIMUM = "attainedMinimum";
    public static final String COLUMN_ATTAINEDMAXIMUM = "attainedMaximum";
    public static final String COLUMN_STATUSBYTES = "statusBytes";
    public static final String COLUMN_DATA = "data";

    private Context myContext;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void saveDataToDB(SQLiteDatabase db, StoreDataModel storeDataModel){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NFCID, storeDataModel.nfcId);
        cv.put(COLUMN_DATETIME, storeDataModel.dataTime);
        cv.put(COLUMN_STATUS, storeDataModel.textStatus);
        cv.put(COLUMN_API, storeDataModel.apiVersion);
        cv.put(COLUMN_CONFIGTIME, storeDataModel.responseConfigData.configTime);
        cv.put(COLUMN_NUMBEROFMEASUREMENTS,storeDataModel.responseConfigData.count);
        cv.put(COLUMN_INTERVAL, storeDataModel.responseConfigData.interval);
        cv.put(COLUMN_RETRIEVEDCOUNT, storeDataModel.retrievedCount);
        cv.put(COLUMN_STATUSOFMEASURED, storeDataModel.statusOfMeasured);
        cv.put(COLUMN_VALIDMINIMUM, storeDataModel.responseConfigData.validMinimum);
        cv.put(COLUMN_VALIDMAXIMUM, storeDataModel.responseConfigData.validMaximum);
        cv.put(COLUMN_ATTAINEDMINIMUM, storeDataModel.responseConfigData.attainedMinimum);
        cv.put(COLUMN_ATTAINEDMAXIMUM, storeDataModel.responseConfigData.attainedMaximum);
        cv.put(COLUMN_STATUSBYTES, storeDataModel.responseConfigData.status);
        cv.put(COLUMN_DATA, storeDataModel.data);
        db.insert(TABLE,null,cv);

    }

    public void removeDateFromDB(SQLiteDatabase db){

        db.delete(TABLE, null, null);

    }



}
