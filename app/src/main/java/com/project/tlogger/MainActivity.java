package com.project.tlogger;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.project.tlogger.msg.CommandHandler;
import com.project.tlogger.msg.Lib;
import com.project.tlogger.msg.ResponseHandler;
import com.project.tlogger.ui.StartMeasurements;
import com.project.tlogger.msg.model.Protocol;
import com.project.tlogger.ui.history.HistoryFragment.onSomeEventListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.tlogger.databinding.ActivityMainBinding;
import com.project.tlogger.ui.temperature.TemperatureFragment;

import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity implements onSomeEventListener, StartMeasurements.OnInputListener{
    public static Lib msgLib;
    Tag tag;
    private NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    private Protocol protocol;
    public NavController navController;
    private Tag myTag;
    private ActivityMainBinding binding;
    private final static String TAG = "NFC Debbug";
    String[] time = {"секунды", "минуты", "часы"};
    int startMeasurementsTime = 60;
    int getStartMeasurementsTimeMeasure = 0;
    private static final String TAG2 = "MainActivity";
    public int mInput;
    int[] timeMeasure = {R.plurals.seconds_plural ,R.plurals.minuits_plural, R.plurals.hours_plural};
    private  CommandHandler cmdHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        msgLib = new Lib();
        cmdHandler = new CommandHandler(msgLib);


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
    }


    @Override
    public void someEvent(String s){
        Log.d(TAG, s);
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
        MainActivity.msgLib.flagOpenFragmentFromHistory = false;


        Log.d(TAG, "on Resume");

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

        msgLib.textStatus="";
        msgLib.flagTloggerConnected = true;

        Protocol.Direction direction = Protocol.Direction.Incoming;
       /* byte[] testPayload = {0x48, 0x01, 0x00, 0x00, 0x00, 0x00, 0x08, 0x10,
                (byte)0x8E, 0x62, 0x0f, 0x00, 0x0A, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, (byte)0xC8, 0x00, 0x5e,0x01,
                (byte) 0xff, 0x7f, 0x00, (byte)0x80, 0x00, 0x00, (byte)0x97,0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,0x00,
                0x00, 0x00
        };

        byte[] testGetVersion = { 0x02, 0x01, 0x00, 0x00, (byte)0xFB, 0x07, 0x13, 0x00,
                0x06, 0x00, 0x01, 0x00, 0x20, 0x00, 0x31, 0x4E};

        byte[] testText1 = {0x02, 0x65, 0x6E, 0x53, 0x74, 0x6F, 0x70, 0x70,
                0x65, 0x64, 0x2E, 0x20, 0x20, 0x20, 0x20, 0x20,
                0x30, 0x20, 0x73, 0x61, 0x6D, 0x70, 0x6C, 0x65,
                0x73, 0x20, 0x6C, 0x6F, 0x67, 0x67, 0x65, 0x64,
                0x2E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00
        };


        byte[] testText2 = {0x02, 0x65, 0x6E, 0x41, 0x4C, 0x45, 0x52, 0x54,
                0x3A, 0x20, 0x62, 0x61, 0x74, 0x74, 0x65, 0x72,
                0x79, 0x20, 0x69, 0x73, 0x20, 0x65, 0x6D, 0x70,
                0x74, 0x79, 0x2E, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00
        };

        byte[] testText3 = {0x02, 0x65, 0x6E, 0x43, 0x75, 0x72, 0x72, 0x65,
                0x6E, 0x74, 0x20, 0x74, 0x65, 0x6D, 0x70, 0x65,
                0x72, 0x61, 0x74, 0x75, 0x72, 0x65, 0x3A, 0x20,
                0x20, 0x32, 0x37, 0x2E, 0x39, 0x43, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00
        };



        NdefRecord mimeRecord = NdefRecord.createMime("n/p", testPayload);
        NdefRecord mimeRecordGetVersion = NdefRecord.createMime("n/p", testGetVersion);
        NdefRecord ndefText1 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, testText1);
        ResponseHandler rsptext1 = new ResponseHandler(msgLib, ndefText1);
        NdefRecord ndefText2 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, testText2);
        ResponseHandler rsptext2 = new ResponseHandler(msgLib, ndefText2);
        NdefRecord ndefText3 = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, testText3);
        ResponseHandler rsptext3 = new ResponseHandler(msgLib, ndefText3);
        ResponseHandler rsp = new ResponseHandler(msgLib, mimeRecord);
        ResponseHandler rsp1 = new ResponseHandler(msgLib, mimeRecordGetVersion);*/
        ResponseHandler rsp;
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
        NdefRecord mimCmdGetVersion = NdefRecord.createMime("n/p", testCmdGetVersion);
        NdefMessage ndefMessage = cmdHandler.createCmdGetMeasurements((short)0);
        NdefMessage response;
        try {
            ndef.connect();
            if (ndef.isConnected()) Log.d(TAG, "connected ready to write");
            ndef.writeNdefMessage(ndefMessage);

            response = ndef.getNdefMessage();
            NdefRecord[] records1 = response.getRecords();
            rsp = new ResponseHandler(msgLib, records1[0]);
            Log.d(TAG, "write msg");
            ndef.close();

        }
        catch (Exception e){
            Log.d(TAG, "no connect");

        }


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

    public void showDialog(View v) {

        StartMeasurements dialog = new StartMeasurements();

        Bundle bundle = new Bundle();
        bundle.putIntArray("", new int[]{startMeasurementsTime, getStartMeasurementsTimeMeasure});
        dialog.setArguments(bundle);

        dialog.show(getSupportFragmentManager(), "custom");
    }


    @Override
    public void sendInput(int number, int time) {
        //Log.d(TAG2, "sendInput: got the input: " + number + " " + time);
        startMeasurementsTime = number;
        getStartMeasurementsTimeMeasure = time;
        setInputToTextView(startMeasurementsTime, getStartMeasurementsTimeMeasure);
    }

    private void setInputToTextView(int number, int time)
    {
        TextView textView = findViewById(R.id.text_startup_delay);
        textView.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[time], number, number));
    }

}

class Utils {
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    public static long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public static long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }


}

