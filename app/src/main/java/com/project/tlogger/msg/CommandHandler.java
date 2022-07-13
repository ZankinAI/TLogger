package com.project.tlogger.msg;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.project.tlogger.msg.model.Protocol;

public class CommandHandler{

    private Lib libCmdHndlr;

    public CommandHandler(Lib lib){
        this.libCmdHndlr = lib;
        
    }

    public NdefMessage createCmdGetMeasurements(short offset){
        byte[] payload = new byte[4];
        payload[0] = Protocol.tloggerIds.get("GETMEASUREMENTS");
        payload[1] = Protocol.Direction.Outgoing.getValue();
        payload[2] = (byte)(offset&0x00ff);
        payload[3] = (byte)((offset&0xff00)>>8);
        NdefRecord mimeCmdGetMeasurements = NdefRecord.createMime("n/p", payload);
        return new NdefMessage(mimeCmdGetMeasurements);
    }
    public NdefMessage createCmdSetConfig(Protocol.TLOGGER_MSG_CMD_SETCONFIG setConfig){
        int startDelaySeconds = toSeconds(setConfig.startDelay, setConfig.startDelayMeasure);
        int runningTimeSeconds = toSeconds(setConfig.runningTime, setConfig.runningTimeMeasure);
        int intervalSeconds = toSeconds(setConfig.interval, setConfig.intervalMeasure);

        byte[] payload = new byte[20];
        payload[0] = Protocol.tloggerIds.get("SETCONFIG");
        payload[1] = Protocol.Direction.Outgoing.getValue();
        payload[2] = (byte)(setConfig.currentTime&0x000000ff);
        payload[3] = (byte)((setConfig.currentTime&0x0000ff00)>>8);
        payload[4] = (byte)((setConfig.currentTime&0x00ff0000)>>16);
        payload[5] = (byte)((setConfig.currentTime&0xff000000)>>24);

        payload[6] = (byte)(intervalSeconds&0x00ff);
        payload[7] = (byte)((intervalSeconds&0xff00)>>8);

        payload[8] = (byte)(startDelaySeconds&0x000000ff);
        payload[9] = (byte)((startDelaySeconds&0x0000ff00)>>8);
        payload[10] = (byte)((startDelaySeconds&0x00ff0000)>>16);
        payload[11] = (byte)((startDelaySeconds&0xff000000)>>24);

        payload[12] = (byte)(runningTimeSeconds&0x000000ff);
        payload[13] = (byte)((runningTimeSeconds&0x0000ff00)>>8);
        payload[14] = (byte)((runningTimeSeconds&0x00ff0000)>>16);
        payload[15] = (byte)((runningTimeSeconds&0xff000000)>>24);

        payload[16] = (byte)(setConfig.validMinimum&0x00ff);
        payload[17] = (byte)((setConfig.validMinimum&0xff00)>>8);

        payload[18] = (byte)(setConfig.validMaximum&0x00ff);
        payload[19] = (byte)((setConfig.validMaximum&0xff00)>>8);

        NdefRecord mimeCmdGetMeasurements = NdefRecord.createMime("n/p", payload);
        return new NdefMessage(mimeCmdGetMeasurements);
    }

    public NdefMessage createCmdStart(){
        byte[] payload = new byte[2];
        payload[0] = Protocol.tloggerIds.get("START");
        payload[1] = Protocol.Direction.Outgoing.getValue();

        NdefRecord mimeCmdGetMeasurements = NdefRecord.createMime("n/p", payload);
        return new NdefMessage(mimeCmdGetMeasurements);
    }

    public NdefMessage createCmdReset(){
        byte[] payload = new byte[2];
        payload[0] = Protocol.tloggerIds.get("RESET");
        payload[1] = Protocol.Direction.Outgoing.getValue();

        NdefRecord mimeCmdGetMeasurements = NdefRecord.createMime("n/p", payload);
        return new NdefMessage(mimeCmdGetMeasurements);
    }

    public int toSeconds(int number, int measure){
        int seconds = number;
        switch (measure) {
            case 0:
                seconds = number;
                break;
            case 1:
                seconds = number*60;
                break;
            case 2:
                seconds = number*3600;
                break;
            default:
                seconds = number;
                break;
        }
        return seconds;
    }

}
