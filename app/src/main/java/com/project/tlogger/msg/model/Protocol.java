package com.project.tlogger.msg.model;

import android.util.Xml;

import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Protocol {

    public static final Hashtable<String, Byte>  tloggerIds = new Hashtable<String, Byte>();



    public Protocol(){
        tloggerIds.put("GETMEASUREMENTS", new Byte((byte)0x46));
        tloggerIds.put("GETCONFIG", new Byte((byte)0x48));
        tloggerIds.put("SETCONFIG", new Byte((byte)0x49));
        tloggerIds.put("MEASURETEMPERATURE", new Byte((byte)0x50));
        tloggerIds.put("START", new Byte((byte)0x5a));
        tloggerIds.put("GETEVENTS", new Byte((byte)0x5b));
        tloggerIds.put("GETPERIODICDATA", new Byte((byte)0x5e));
        tloggerIds.put("GETVERSION", new Byte((byte)0x02));
        tloggerIds.put("GETNFCUID", new Byte((byte)0x0A));
    }

    public static byte MSG_ID(String msg)
    {
        byte id;
        id = tloggerIds.get(msg);
        return id;
    }

    public enum Direction{
        Outgoing((byte)0),
        Incoming((byte)1);
        private byte value;
        private Direction(byte b) {
            this.value = b;
        }

        public byte getValue() {
            return value;
        }
    }
    public static byte[] mimeType = "n/p".getBytes(StandardCharsets.UTF_8);

    public static class MimePayloadHeader{
        public byte msgId;
        public Direction direction;
    }

    public static class MimePayload{
        public MimePayloadHeader header;
        public  byte[] data;
        public MimePayload(){};
    }

    public enum MSG_ERR{
        MSG_OK(0),
        MSG_ERR_UNKNOWN_COMMAND(0x10007),
        MSG_ERR_NO_RESPONSE(0x1000B),
        MSG_ERR_INVALID_COMMAND_SIZE(0x1000D),
        MSG_ERR_INVALID_PARAMETER( 0x1000E),
        MSG_ERR_UNKNOWN_ERROR(0xFFFFFF),
        MSG_ERR_INVALID_PRECONDITION(0x1000F);

        private int value;

        MSG_ERR(int i) {
            this.value = i;
        }

        public int getValue() { return  value;}
    }

    public enum DEVICE_ID{
        NHS3100_DEVICE(0x4E310020),
        NHS3152_DEVICE(0x4E315220);

        private int value;
        DEVICE_ID(int i) { this.value = i;}

        public int getValue() { return  value;
        }
    }

    public static class MSG_RESPONSE_GETVERSION{
        public short reserved;
        public short swMajorVersion;
        public short swMinorVersion;
        public short apiMajorVersion;
        public short apiMinorVersion;
        public DEVICE_ID deviceId;

        public MSG_RESPONSE_GETVERSION(){};
    }

    public enum APP_MSG_EVENT{
        APP_MSG_EVENT_PRISTINE(1 << 0),
        APP_MSG_EVENT_CONFIGURED(1 << 1) ,
        APP_MSG_EVENT_STARTING(1 << 2),
        APP_MSG_EVENT_LOGGING(1 << 3),
        APP_MSG_EVENT_STOPPED(1 << 4),
        APP_MSG_EVENT_TEMPERATURE_TOO_HIGH(1 << 5),
        APP_MSG_EVENT_TEMPERATURE_TOO_LOW(1 << 6),
        APP_MSG_EVENT_BOD(1 << 7),
        APP_MSG_EVENT_FULL(1 << 8),
        APP_MSG_EVENT_EXPIRED( 1 << 9),
        APP_MSG_EVENT_I2C_ERROR(1 << 10),
        APP_MSG_EVENT_SPI_ERROR( 1 << 11),
        APP_MSG_EVENT_SHOCK(1 << 12),
        APP_MSG_EVENT_SHAKE( 1 << 13),
        APP_MSG_EVENT_VIBRATION(1 << 14),
        APP_MSG_EVENT_TILT (1 << 15),
        APP_MSG_EVENT_SHOCK_CONFIGURED (1 << 16),
        APP_MSG_EVENT_SHAKE_CONFIGURED(1 << 17),
        APP_MSG_EVENT_VIBRATION_CONFIGURED(1 << 18),
        APP_MSG_EVENT_TILT_CONFIGURED(1 << 19),
        APP_MSG_EVENT_HUMIDITY_CONFIGURED(1 << 20),
        APP_MSG_EVENT_HUMIDITY_TOO_HIGH(1 << 21),
        APP_MSG_EVENT_HUMIDITY_TOO_LOW(1 << 22),
        APP_MSG_EVENT_COUNT(23),
        APP_MSG_EVENT_ALL((1 << APP_MSG_EVENT_COUNT.value) - 1);

        private int value;
        APP_MSG_EVENT(int i) {this.value = i;}

        public int getValue() {
            return value;
        }
    }

    public enum EVENT_INFO{
        EVENT_INFO_INDEX((byte)(1 << 0)),
        EVENT_INFO_TIMESTAMP((byte)(1 << 1)),
        EVENT_INFO_ENUM((byte)(1 << 2)),
        EVENT_INFO_DATA ((byte)(1 << 3)),
        EVENT_INFO_COUNT((byte)(4)),
        EVENT_INFO_NONE ((byte)(0)),
        EVENT_INFO_ALL((byte)(EVENT_INFO_INDEX.value|EVENT_INFO_TIMESTAMP.value|EVENT_INFO_ENUM.value| EVENT_INFO_DATA.value)),
        EVENT_INFO_MORE((byte)(1 << 7));
        private byte value;
        EVENT_INFO(byte i) { this.value = i;}

        public byte getValue() {
            return value;
        }
    }

    public static class TLOGGER_MSG_RESPONSE_GETCONFIG{
        public int result;
        public int configTime;
        public short interval;
        public int startDelay;
        public int runningTime;
        public short validMinimum;
        public short validMaximum;
        public short attainedMinimum;
        public short attainedMaximum;
        public short count;
        public int status;
        public int startTime;
        public int currentTime;

        public TLOGGER_MSG_RESPONSE_GETCONFIG(){};

    }

    public static class TLOGGER_MSG_CMD_GETMEASUREMENTS{
        public short offset;

        public TLOGGER_MSG_CMD_GETMEASUREMENTS(){};
    }

    public static class TLOGGER_MSG_CMD_SETCONFIG{
        public int currentTime;

        public int interval=3;
        public int intervalMeasure=0;

        public int startDelay=10;
        public int startDelayMeasure=0;

        public int runningTime=40;
        public int runningTimeMeasure=0;

        public int validMinimum = 200;
        public int validMaximum = 350;

        public TLOGGER_MSG_CMD_SETCONFIG(){};

    }

    public enum APP_MSG_TSEN_RESOLUTION{
        APP_MSG_TSEN_RESOLUTION_7BITS((byte)2),
        APP_MSG_TSEN_RESOLUTION_8BITS((byte)3),
        APP_MSG_TSEN_RESOLUTION_9BITS((byte)4),
        APP_MSG_TSEN_RESOLUTION_10BITS((byte)5),
        APP_MSG_TSEN_RESOLUTION_11BITS((byte)6),
        APP_MSG_TSEN_RESOLUTION_12BITS((byte)7);

        private byte value;
        APP_MSG_TSEN_RESOLUTION(byte i) {
            this.value = i;
        }
    }

    public static class APP_MSG_CMD_GETEVENTS{
        public short index;
        public int eventMask;
        public byte info;

        public APP_MSG_CMD_GETEVENTS(){};
    }

    public static class APP_MSG_RESPONSE_GETEVENTS{
        public short index;
        public int eventMask;
        public byte info;
        public short count;

        public APP_MSG_RESPONSE_GETEVENTS(){};
    }

    public static class TLOGGER_MSG_RESPONSE_GETMEASUREMENTS{
        public int result;
        public short offset;
        public byte count;
        public byte zero1;
        public byte zero2;
        public byte zero3;
        // ushort data[...];
        public short[] data;

        public TLOGGER_MSG_RESPONSE_GETMEASUREMENTS(){};
    }

    public static class MSG_RESPONSE_GETUID{
        public int uid1;
        public int uid2;
        public int uid3;
        public int uid4;

        public MSG_RESPONSE_GETUID(){};
    }

    public static class MSG_RESPONSE_GETNFCID{
        public byte nfcuid1;
        public byte nfcuid2;
        public byte nfcuid3;
        public byte nfcuid4;
        public byte nfcuid5;
        public byte nfcuid6;
        public byte nfcuid7;
        public byte nfcuid8;
        public MSG_RESPONSE_GETNFCID(){};
    }



}
