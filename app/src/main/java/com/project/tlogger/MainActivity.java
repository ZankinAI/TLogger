package com.project.tlogger;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.project.tlogger.ui.AppSettings;
import com.project.tlogger.msg.model.DatabaseHelper;
import com.project.tlogger.msg.model.StoreDataModel;
import com.project.tlogger.msg.model.Utils;
import com.project.tlogger.ui.settings.dialogs.EndMeasurements;
import com.project.tlogger.ui.settings.dialogs.IntervalOfMeasurements;
import com.project.tlogger.msg.CommandHandler;
import com.project.tlogger.msg.Lib;
import com.project.tlogger.msg.ResponseHandler;
import com.project.tlogger.ui.settings.dialogs.NFCStart;
import com.project.tlogger.ui.settings.dialogs.StartMeasurements;
import com.project.tlogger.msg.model.Protocol;
import com.project.tlogger.ui.settings.dialogs.TemperatureRange;
import com.project.tlogger.ui.history.HistoryFragment.onSomeEventListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.tlogger.databinding.ActivityMainBinding;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements onSomeEventListener, AppSettings.ConfirmationListener, StartMeasurements.OnInputListener, IntervalOfMeasurements.OnInputListener, EndMeasurements.OnInputListener, TemperatureRange.OnInputListener {
    public static Lib msgLib;
    boolean flag = false;
    Tag tag;
    private NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private Protocol protocol;
    public NavController navController;
    private Tag myTag;
    private ActivityMainBinding binding;
    private final static String TAG = "NFC Debbug";
    String[] time = {"секунды", "минуты", "часы"};
    int startMeasurementsTime;
    int startMeasurementsMeasure;
    int intervalOfMeasurmentsTime;
    int intervalOfMeasurmentsMeasure;
    int endMeasurementsTime;
    int endMeasurementsMeasure;
    int lower_range;
    int upper_range;
    private static final String TAG2 = "MainActivity";
    public int mInput;
    public NFCStart nfcDialog;
    public NFCStart nfcDialogEvent;
    int[] timeMeasure = {R.plurals.seconds_plural ,R.plurals.minuits_plural, R.plurals.hours_plural};
    private  CommandHandler cmdHandler;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        msgLib = new Lib();

        cmdHandler = new CommandHandler(msgLib);

        startMeasurementsTime = msgLib.cmdSetConfig.startDelay;
        startMeasurementsMeasure = msgLib.cmdSetConfig.startDelayMeasure;
        intervalOfMeasurmentsTime = msgLib.cmdSetConfig.interval;
        intervalOfMeasurmentsMeasure = msgLib.cmdSetConfig.intervalMeasure;
        endMeasurementsTime = msgLib.cmdSetConfig.runningTime;
        endMeasurementsMeasure = msgLib.cmdSetConfig.runningTimeMeasure;
        lower_range = msgLib.cmdSetConfig.validMinimum;
        upper_range = msgLib.cmdSetConfig.validMaximum;


        Log.d(TAG, "onStart");
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this,0, nfcIntent,0);

        if (nfcAdapter!=null)
            Toast.makeText(this, "NFC Adapter founded", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "NFC Adapter not founded", Toast.LENGTH_SHORT).show();



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_action_bar,null);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(v);
        /*//bar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO);
        bar.setIcon(getDrawable(R.drawable.logo));
        //bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("TempSense");
        bar.setDisplayShowTitleEnabled(true);
        bar.show();*/
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(

                R.id.navigation_temperature, R.id.navigation_history,
                R.id.navigation_settings, R.id.navigation_about)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        AppSettings dialog = new AppSettings();

        Bundle bundle = new Bundle();
        bundle.putBoolean("", false);
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void someEvent(int pos){
        Log.d(TAG, String.valueOf(pos));
        msgLib.selectedStoreData = msgLib.storeDataModelList.get(pos);
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        TemperatureFragment temperatureFragment = new TemperatureFragment();
        ft.replace(R.id.nav_host_fragment_activity_main, temperatureFragment, "temperatureFragment");
        ft.addToBackStack(null);
        ft.commit();*/
        msgLib.flagOpenFragmentFromHistory = true;
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "on Resume");
        MainActivity.msgLib.flagOpenFragmentFromHistory = false;
        String nfcid = "gfg";
        String status = "gfgfg";

        if (!msgLib.flagReadFromDB){
            StoreDataModel temp;
            db = databaseHelper.open();
            //получаем данные из бд в виде курсора
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
            if (userCursor.moveToFirst()){
                do {
                    temp = new StoreDataModel(userCursor);
                    msgLib.storeDataModelList.add(temp);
                } while(userCursor.moveToNext());
                Log.d(TAG, "first data from db");
                msgLib.flagReadFromDB = true;
            }
            else {
                Log.d(TAG, "no data from db");
            }

            userCursor.close();
            db.close();

            Log.d(TAG, "end db");


        }


        if (nfcAdapter!=null)
            if (!nfcAdapter.isEnabled())
                Log.d(TAG, "not Enable NFS");
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "onNewIntent");





        ResponseHandler rsp;
        if (msgLib.setConfigurationFlag){

            if( nfcDialog!=null) {
                //if (nfcDialog.isVisible()) {
                nfcDialog.dismiss();
                //}
            }
            if(nfcDialogEvent!=null)
            {
                nfcDialogEvent.dismiss();
            }
            nfcDialogEvent = new NFCStart();
            Bundle bundle = new Bundle();
            //bundle.putString("", "Hello");


            Ndef ndef = Ndef.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            msgLib.cmdSetConfig.currentTime = (int)(timestamp.getTime()/1000);

            NdefMessage ndefMessageSetConfig = cmdHandler.createCmdSetConfig(msgLib.cmdSetConfig);

            try {
                ndef.connect();
                if (ndef.isConnected()) Log.d(TAG, "connected ready to write");
                ndef.writeNdefMessage(ndefMessageSetConfig);
                NdefMessage response;
                response = ndef.getNdefMessage();
                NdefRecord[] records1 = response.getRecords();
                rsp = new ResponseHandler(msgLib, records1[0]);
                Log.d(TAG, "write msg");
                ndef.close();

            }
            catch (Exception e){
                Log.d(TAG, "no connect");
            }
            bundle.putInt("",msgLib.msgErr.getValue());
            nfcDialogEvent.setArguments(bundle);
            nfcDialogEvent.show(getSupportFragmentManager(), "custom");
            msgLib.setConfigurationFlag = false;
            return;

        }
        msgLib.flagTloggerConnected = true;

        Ndef ndef = Ndef.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        navController.navigate( R.id.navigation_temperature);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (myTag==null)
                Log.d(TAG, "myTag is null");
            else
                Log.d(TAG, "myTag is not null");
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;



            if (rawMsgs!=null){

                NdefMessage msg = (NdefMessage)rawMsgs[0];
                NdefRecord[] records = msg.getRecords();
                if (records.length>2) {
                    msgLib.flagStandartMessageReceive = true;
                    msgLib.textStatus="";

                }

                for(NdefRecord ndefRecord : records ){
                    Log.d(TAG, "read the record");
                    rsp = new ResponseHandler(msgLib, ndefRecord);
                }

                /*NdefRecord firstRecord = ((NdefMessage)rawMsgs[0]).getRecords()[1];
                Log.d(TAG, "create first record");
                byte[] payload = firstRecord.getPayload();
                int payloadLenght = payload.length;
                int langLenght = payload[0];
                int textLenght = payloadLenght-langLenght-1;
                byte[] text = new byte[textLenght];
                System.arraycopy(payload, 1+ langLenght, text, 0, textLenght);
                Toast.makeText(this, new String(text), Toast.LENGTH_SHORT).show();

                Log.d(TAG, new String(text));*/

            }
            else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                byte[] payload = dumpTagData(myTag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
                Log.d(TAG, "msg null but cool");
            }


        }

        byte[] testCmdGetVersion = { 0x46, 0x00, 0x00, 0x00};
        byte[] testCmdGetResponse = { 0x01, 0x00};
        NdefRecord mimCmdGetVersion = NdefRecord.createMime("n/p", testCmdGetVersion);
        NdefRecord mimCmdGetResponse = NdefRecord.createMime("n/p", testCmdGetResponse);
        NdefMessage ndefMessageGetResponse = new NdefMessage(mimCmdGetResponse);
        NdefMessage ndefMessage = cmdHandler.createCmdGetMeasurements((short)0);
        NdefMessage ndefMessageSetConfig = cmdHandler.createCmdSetConfig(msgLib.cmdSetConfig);
        msgLib.measurementsCount = 0;
        NdefMessage response;
        try {
            ndef.connect();
            if (ndef.isConnected()) Log.d(TAG, "connected ready to write");
            ndef.writeNdefMessage(ndefMessage);
            Log.d(TAG, "write msg");
            Thread.sleep(300);
            response = ndef.getNdefMessage();

            Log.d(TAG, "read msg");
            NdefRecord[] records1 = response.getRecords();

            rsp = new ResponseHandler(msgLib, records1[0]);
            //ndef.writeNdefMessage(ndefMessageGetResponse);

            Log.d(TAG, "write msg");


            ndef.close();

        }
        catch (Exception e){
            Log.d(TAG, "no connect");

        }

        if (msgLib.flagStandartMessageReceive){

            //Добавить запись в БД
            createStoreData();
            StoreDataModel toListStoreData = null;

            try{
                toListStoreData = msgLib.storeData.clone();

            }
            catch (CloneNotSupportedException ex){
                Log.d(TAG, "can't clone StoreDataModel");
            }


            msgLib.storeDataModelList.add(toListStoreData);

            db = databaseHelper.open();
            databaseHelper.saveDataToDB(db, msgLib.storeData);
            db.close();

        }
        msgLib.flagStandartMessageReceive = false;
    }

    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(Utils.toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(Utils.toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(Utils.toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(Utils.toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    public void showDialogStartMeasurements(View v) {

        StartMeasurements dialog = new StartMeasurements();

        Bundle bundle = new Bundle();
        bundle.putIntArray("", new int[]{startMeasurementsTime, startMeasurementsMeasure});
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void showDialogIntervalOfMeasurements(View v) {

        IntervalOfMeasurements dialog = new IntervalOfMeasurements();

        Bundle bundle = new Bundle();
        bundle.putIntArray("", new int[]{intervalOfMeasurmentsTime, intervalOfMeasurmentsMeasure});
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void showDialogEndMeasurements(View v) {

        EndMeasurements dialog = new EndMeasurements();

        Bundle bundle = new Bundle();
        bundle.putIntArray("", new int[]{endMeasurementsTime, endMeasurementsMeasure});
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void showDialogTemperatureRange(View v) {

        TemperatureRange dialog = new TemperatureRange();

        Bundle bundle = new Bundle();
        bundle.putIntArray("", new int[]{lower_range, upper_range});
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");
    }
    /*public void dbClearConfirmation(View v){
        new AlertDialog.Builder(getactivi)
                .setTitle("Title")
                .setMessage("Do you really want to whatever?").show();
    }*/

    /*public void showDialogAppSettings(View v) {

        AppSettings dialog = new AppSettings();

        Bundle bundle = new Bundle();
        bundle.putBoolean("", false);
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");
    }*/

    public void setConfiguration(View v){
        nfcDialog = new NFCStart();
        nfcDialog.show(getSupportFragmentManager(), "custom");

        /*while (true)
        {
            if (flag) break;;
        }*/
    }

    public void resetTag(View v){

    }



    @Override
    public void sendInput(int dialogId, int number, int time) {
        //Log.d(TAG2, "sendInput: got the input: " + number + " " + time);
        switch (dialogId){
            case 0:
                startMeasurementsTime = number;
                startMeasurementsMeasure = time;
                break;
            case 1:
                intervalOfMeasurmentsTime = number;
                intervalOfMeasurmentsMeasure = time;
                break;
            case 2:
                endMeasurementsTime = number;
                endMeasurementsMeasure = time;
                break;
            case 3:
                lower_range = number;
                upper_range = time;
                break;
        }
        setInputToTextView(dialogId, number, time);
    }


    private void setInputToTextView(int dialogId, int number, int time)
    {
        //TextView [] textView = {findViewById(R.id.text_startup_delay), findViewById(R.id.text_measurement_interval), findViewById(R.id.text_measurement_interval)};
        //ПРОВЕРИТЬ!!!
        TextView textView;
        switch (dialogId) {
            case 0:
                textView = findViewById(R.id.text_startup_delay);
                switch (time) {
                    case 0:
                        if (number == 0)
                        {textView.setText("Начать измерения немедленно");}
                        else if (number <= 60)
                        {textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[time], number, number));}
                        else
                        {textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[1], number / 60 % 60, number / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[time], number / 1 % 60, number / 1 % 60));}
                        break;
                    case 1:
                        if (number == 0)
                        {textView.setText("Начать измерения немедленно");}
                        else if (number <= 60)
                            textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[time], number, number));
                        else
                            textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[2], number / 60 % 60, number / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[time], number / 1 % 60, number / 1 % 60));
                        break;
                    case 2:
                        if (number == 0)
                        {textView.setText("Начать измерения немедленно");}
                        else textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[time], number, number));
                        break;
                    case 3:
                        textView.setText("Начать измерения немедленно");
                        startMeasurementsMeasure = 0;
                        break;
                }
                break;
            case 1:
                textView = findViewById(R.id.text_measurement_interval);
                switch (time) {
                    case 0:
                        if (number == 0)
                        {textView.setText("Периодичность измерений: 1 секунда");}
                        else if (number <= 60)
                        {textView.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[time], number, number));}
                        else
                        {textView.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[1], number / 60 % 60, number / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[time], number / 1 % 60, number / 1 % 60));}
                        break;
                    case 1:
                        if (number == 0)
                        {textView.setText("Периодичность измерений: 1 секунда");}
                        else if (number <= 60)
                            textView.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[time], number, number));
                        else
                            textView.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[2], number / 60 % 60, number / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[time], number / 1 % 60, number / 1 % 60));
                        break;
                    case 2:
                        if (number == 0)
                        {textView.setText("Периодичность измерений: 1 секунда");}
                        else textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[time], number, number));
                        break;
                    case 3:
                        textView.setText("Начать измерения немедленно");
                        intervalOfMeasurmentsMeasure = 0;
                        break;
                }
                break;
            case 2:
                textView = findViewById(R.id.text_measurement_duration);
                switch (time) {
                    case 0:
                        if (number == 0)
                        {textView.setText("Измерять, пока не закончится память");}
                        else if (number <= 60)
                        {textView.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[time], number, number));}
                        else
                        {textView.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[1], number / 60 % 60, number / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[time], number / 1 % 60, number / 1 % 60));}
                        break;
                    case 1:
                        if (number == 0)
                        {textView.setText("Измерять, пока не закончится память");}
                        else if (number <= 60)
                            textView.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[time], number, number));
                        else
                            textView.setText("Продолжать измеренияПериодичность измерений: " + getResources().getQuantityString(timeMeasure[2], number / 60 % 60, number / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[time], number / 1 % 60, number / 1 % 60));
                        break;
                    case 2:
                        if (number == 0)
                        {textView.setText("Измерять, пока не закончится память");}
                        else textView.setText("Продолжать измерения " + getResources().getQuantityString(timeMeasure[time], number, number));
                        break;
                    case 3:
                        textView.setText("Измерять, пока не закончится память");
                        endMeasurementsMeasure = 0;
                        break;
                }
                break;
            case 3:
                textView = findViewById(R.id.text_temperature_limits);
                textView.setText("Границы измерения: от " + number + " °C до " + time + " °C");
                break;

        }
    }

    public void createStoreData(){
        msgLib.storeData.nfcId = msgLib.nfcId;
        msgLib.storeData.apiVersion = msgLib.apiVersion;
        msgLib.storeData.textStatus = msgLib.textStatus;
        msgLib.storeData.statusOfMeasured = msgLib.measuredStatus;
        msgLib.storeData.responseConfigData =  msgLib.rspGetConfig;
        msgLib.storeData.retrievedCount = msgLib.measurementsCount;
        Date currentDate = new Date();
        msgLib.storeData.data = Utils.masShortToString(msgLib.measuredData);

        short[] temp = Utils.StringtoMasShort(msgLib.storeData.data);
        msgLib.storeData.dataTime = currentDate.getTime()/1000;
    }

    @Override
    public void confirmClear() {
        Log.e("mytag", "cleared");
    }
}


