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

}
