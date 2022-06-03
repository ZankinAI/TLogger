package com.project.tlogger.msg.model;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Protocol {

    private static Hashtable<String, Byte>  tloggerIds = new Hashtable<String, Byte>();



    public Protocol(){
        tloggerIds.put("GETMEASUREMENTS", new Byte((byte)0x46));
        tloggerIds.put("GETCONFIG", new Byte((byte)0x48));
        tloggerIds.put("SETCONFIG", new Byte((byte)0x49));
        tloggerIds.put("MEASURETEMPERATURE", new Byte((byte)0x50));
        tloggerIds.put("START", new Byte((byte)0x5a));
        tloggerIds.put("GETEVENTS", new Byte((byte)0x5b));
        tloggerIds.put("GETPERIODICDATA", new Byte((byte)0x5e));
    }

    public static byte MSG_ID(String msg)
    {
        byte id;
        id = tloggerIds.get(msg);
        return id;
    }

}
