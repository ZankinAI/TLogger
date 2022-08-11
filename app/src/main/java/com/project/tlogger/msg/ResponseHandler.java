package com.project.tlogger.msg;

import android.nfc.NdefRecord;
import android.os.Debug;

import com.project.tlogger.msg.model.MeasurementStatusModel;
import com.project.tlogger.msg.model.Protocol;
import com.project.tlogger.msg.model.TemperatureStatusModel;
import com.project.tlogger.msg.model.Utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.xml.transform.sax.SAXResult;

public class ResponseHandler {
    Lib _lib;
    public ResponseHandler(Lib lib, NdefRecord ndefRecord){
        this._lib = lib;

        if (ndefRecord.getTnf()==NdefRecord.TNF_MIME_MEDIA)
        {
            byte[] payload = ndefRecord.getPayload();
            if (ndefRecord.getPayload()==null)
            {
                //Обработка если нет пайлоада
                return;
            }
            Protocol.MimePayloadHeader header = new Protocol.MimePayloadHeader();
            header.msgId = payload[0];
            if (payload[1]== Protocol.Direction.Incoming.getValue()){
                header.direction = Protocol.Direction.Incoming;
            }
            else if (payload[1]== Protocol.Direction.Outgoing.getValue()){
                header.direction = Protocol.Direction.Outgoing;
                }
            else {
                return;
            }

            Protocol.MimePayload mimePayload = new Protocol.MimePayload();
            mimePayload.header = header;
            mimePayload.data = new byte[payload.length-2];
            System.arraycopy(payload,2,mimePayload.data,0,payload.length-2);
            int k=0;
            //Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG msgResponseGetconfig = GetResponseGetConfig(mimePayload);

            if (Protocol.tloggerIds.get("GETCONFIG")==header.msgId) {
                Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG msgResponseGetConfig = GetResponseGetConfig(mimePayload);
                lib.rspGetConfig = msgResponseGetConfig;
                lib.storeData.responseConfigData = msgResponseGetConfig;
                lib.count = msgResponseGetConfig.count;
                lib.configTime = msgResponseGetConfig.configTime;
                lib.runningTime = msgResponseGetConfig.runningTime;
                lib.runningTime = msgResponseGetConfig.runningTime;
                lib.interval = msgResponseGetConfig.interval;
                lib.validMinimum = msgResponseGetConfig.validMinimum;
                //parsingEvent(msgResponseGetConfig.status);

                Object[] parsedStat = Utils.parsingEvent(msgResponseGetConfig.status);
                lib.measurementStatus.measurement = (MeasurementStatusModel.Measurement) parsedStat[0];
                lib.measurementStatus.failure = (MeasurementStatusModel.Failure) parsedStat[1];
                lib.temperatureStatus.temperature = (TemperatureStatusModel.Temperature) parsedStat[2];
                lib.validMaximum = msgResponseGetConfig.validMaximum;
                lib.attainedMinimunm = msgResponseGetConfig.attainedMinimum;
                lib.attainedMaximum = msgResponseGetConfig.attainedMaximum;



            }
            if (Protocol.tloggerIds.get("GETVERSION")==header.msgId){
                Protocol.MSG_RESPONSE_GETVERSION msgResponseGetVersion = GetResponseGetVersion(mimePayload);
                lib.apiVersion = getVersionText(msgResponseGetVersion);
                lib.storeData.apiVersion = lib.apiVersion;
            }

            if (Protocol.tloggerIds.get("GETNFCUID") == header.msgId){
                lib.nfcId = GetResponseGetNfcUid(mimePayload);
                lib.storeData.nfcId = lib.nfcId;
            }

            if ((Protocol.tloggerIds.get("SETCONFIG") == header.msgId)&&(header.direction.getValue()==1)){
                lib.msgErr = findError(mimePayload);
            }

            if (Protocol.tloggerIds.get("GETMEASUREMENTS")==header.msgId){
                Protocol.TLOGGER_MSG_RESPONSE_GETMEASUREMENTS msgResponseGetMeasurements = GetResponseGetMeasurements(mimePayload);
                lib.measuredStatus = msgResponseGetMeasurements.result;
                //lib.measuredData = new short[msgResponseGetMeasurements.count];
                lib.measuredData = Utils.concatShorts(lib.measuredData, msgResponseGetMeasurements.data);
                lib.measurementsCount+= msgResponseGetMeasurements.count;
                lib.currentMeasurementsCount = msgResponseGetMeasurements.count;
                lib.storeData.retrievedCount = lib.measurementsCount;

            }
        }

        else if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN){

            byte[] payload =ndefRecord.getPayload();
            int payloadLenght = payload.length;
            int langLenght = payload[0];
            int textLenght = payloadLenght-langLenght-1;
            byte[] text = new byte[textLenght];
            System.arraycopy(payload, 1+ langLenght, text, 0, textLenght);
            String textData = new String(text);
            textData += "\n";
            
            lib.textStatus += textData;
            lib.storeData.textStatus = lib.textStatus;

        }



    }


    private String GetResponseGetNfcUid(Protocol.MimePayload mimePayload){
        String msgResponseGetnfcid = "";
        for (int i=0; i<mimePayload.data.length; i++){
            msgResponseGetnfcid+=byteToHex(mimePayload.data[i]);
            if (i<mimePayload.data.length-1) msgResponseGetnfcid += ":";

        }

        return msgResponseGetnfcid;
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    private Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG GetResponseGetConfig(Protocol.MimePayload mimePayload)
    {
        Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG getConfig = new Protocol.TLOGGER_MSG_RESPONSE_GETCONFIG();
        getConfig.result = convertFromArrayByteToInt(mimePayload.data,0);
        getConfig.configTime = convertFromArrayByteToInt(mimePayload.data,4);
        //LocalDateTime dateTime = LocalDateTime.ofEpochSecond(getConfig.configTime+10800, 0, ZoneOffset.UTC);
        getConfig.interval = convertFromArrayByteToShort(mimePayload.data, 8);
        getConfig.startDelay = convertFromArrayByteToInt(mimePayload.data,10);
        getConfig.runningTime = convertFromArrayByteToInt(mimePayload.data,14);
        getConfig.validMinimum = convertFromArrayByteToShort(mimePayload.data, 18);
        getConfig.validMaximum = convertFromArrayByteToShort(mimePayload.data, 20);
        getConfig.attainedMinimum = convertFromArrayByteToShort(mimePayload.data, 22);
        getConfig.attainedMaximum = convertFromArrayByteToShort(mimePayload.data, 24);
        getConfig.count = convertFromArrayByteToShort(mimePayload.data, 26);
        getConfig.status = convertFromArrayByteToInt(mimePayload.data, 28);
        getConfig.startTime = convertFromArrayByteToInt(mimePayload.data, 32);
        getConfig.currentTime = convertFromArrayByteToInt(mimePayload.data, 36);
        return getConfig;
    }

    private Protocol.MSG_RESPONSE_GETVERSION GetResponseGetVersion(Protocol.MimePayload mimePayload){
        Protocol.MSG_RESPONSE_GETVERSION getVersion = new Protocol.MSG_RESPONSE_GETVERSION();
        getVersion.reserved = convertFromArrayByteToShort(mimePayload.data,0);
        getVersion.swMajorVersion = convertFromArrayByteToShort(mimePayload.data,2);
        getVersion.swMinorVersion = convertFromArrayByteToShort(mimePayload.data,4);
        getVersion.apiMajorVersion = convertFromArrayByteToShort(mimePayload.data,6);
        getVersion.apiMinorVersion = convertFromArrayByteToShort(mimePayload.data,8);
        getVersion.deviceId = findDeviceID(mimePayload);

        return getVersion;
    }



    private Protocol.DEVICE_ID findDeviceID (Protocol.MimePayload mimePayload){
        int deviceId = ((mimePayload.data[10]&0xff)|
                ((mimePayload.data[11]&0xff)<<8)|
                ((mimePayload.data[12]&0xff)<<16)|
                ((mimePayload.data[13]&0xff)<<24)
                );

        if (Protocol.DEVICE_ID.NHS3100_DEVICE.getValue()==deviceId) return Protocol.DEVICE_ID.NHS3100_DEVICE;
        else
        if (Protocol.DEVICE_ID.NHS3152_DEVICE.getValue()==deviceId) return Protocol.DEVICE_ID.NHS3152_DEVICE;
        else return null;

    }

    private Protocol.MSG_ERR findError(Protocol.MimePayload mimePayload){
        Protocol.MSG_ERR msgErr = Protocol.MSG_ERR.MSG_OK;

        int error = ((mimePayload.data[0]&0xff)|
                ((mimePayload.data[1]&0xff)<<8)|
                ((mimePayload.data[2]&0xff)<<16)|
                ((mimePayload.data[3]&0xff)<<24)
        );

        for (Protocol.MSG_ERR msg: Protocol.MSG_ERR.values()){
            if (msg.getValue() == error)
                return msg;
        }
        return msgErr;

    }


    private int convertFromArrayByteToInt(byte[] byteArray, int pos){
        return ((byteArray[pos]&0xff)|
                ((byteArray[pos+1]&0xff)<<8)|
                ((byteArray[pos+2]&0xff)<<16)|
                ((byteArray[pos+3]&0xff)<<24));
    };

    private short convertFromArrayByteToShort(byte[] byteArray, int pos){
        return (short) ((byteArray[pos]&0xff)|
                ((byteArray[pos+1]&0xff)<<8));
    };

    private String getVersionText (Protocol.MSG_RESPONSE_GETVERSION rspGetVersion){
        String versionText = null;
        versionText = "SW:" + String.valueOf(rspGetVersion.swMajorVersion) + "." + String.valueOf(rspGetVersion.swMinorVersion) +
                " API:" + String.valueOf(rspGetVersion.apiMajorVersion) + "." + String.valueOf(rspGetVersion.apiMinorVersion);

        return versionText;
    }

    private Protocol.TLOGGER_MSG_RESPONSE_GETMEASUREMENTS GetResponseGetMeasurements(Protocol.MimePayload mimePayload){
        Protocol.TLOGGER_MSG_RESPONSE_GETMEASUREMENTS msgResponseGetmeasurements = new Protocol.TLOGGER_MSG_RESPONSE_GETMEASUREMENTS();
        msgResponseGetmeasurements.result =  convertFromArrayByteToInt(mimePayload.data,0);
        msgResponseGetmeasurements.offset = convertFromArrayByteToShort(mimePayload.data, 4);
        msgResponseGetmeasurements.count = (short)(mimePayload.data[6]&0xff);
        msgResponseGetmeasurements.zero1 = mimePayload.data[7];
        msgResponseGetmeasurements.zero2 = mimePayload.data[8];
        msgResponseGetmeasurements.zero3 = mimePayload.data[9];
        msgResponseGetmeasurements.data = new short[msgResponseGetmeasurements.count];
        byte[] temp = new byte[msgResponseGetmeasurements.count*2];
        System.arraycopy(mimePayload.data, 10, temp, 0, msgResponseGetmeasurements.count*2 );
        msgResponseGetmeasurements.data = parsingTemperatureData(temp, msgResponseGetmeasurements.count);
        return msgResponseGetmeasurements;


    }

    private short[] parsingTemperatureData (byte[] temperatureData, int count){
        short[] result = new short[count];

        for (int i=0; i < count; i++){
            result[i] = (short) ((temperatureData[i*2]&0xff)|
                    ((temperatureData[i*2+1]&0xff)<<8));
        }

        return result;


    }

    void parsingEvent( int status){
        int result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_PRISTINE.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_PRISTINE.getValue()) _lib.measurementStatus.measurement = MeasurementStatusModel.Measurement.NotConfigured;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_CONFIGURED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_CONFIGURED.getValue()) _lib.measurementStatus.measurement = MeasurementStatusModel.Measurement.Configured;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STARTING.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STARTING.getValue()) _lib.measurementStatus.measurement = MeasurementStatusModel.Measurement.Starting;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue()) _lib.measurementStatus.measurement = MeasurementStatusModel.Measurement.Logging;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue()) _lib.measurementStatus.measurement = MeasurementStatusModel.Measurement.Stopped;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue()) _lib.measurementStatus.measurement = MeasurementStatusModel.Measurement.Stopped;


        _lib.measurementStatus.failure = MeasurementStatusModel.Failure.NoFailure;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_FULL.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_FULL.getValue()) _lib.measurementStatus.failure = MeasurementStatusModel.Failure.Full;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_BOD.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_BOD.getValue()) _lib.measurementStatus.failure = MeasurementStatusModel.Failure.Bod;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_EXPIRED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_EXPIRED.getValue()) _lib.measurementStatus.failure = MeasurementStatusModel.Failure.Expired;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue()) _lib.temperatureStatus.temperature = TemperatureStatusModel.Temperature.Normal;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_LOW.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_LOW.getValue()) _lib.temperatureStatus.temperature = TemperatureStatusModel.Temperature.Low;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_HIGH.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_HIGH.getValue()) _lib.temperatureStatus.temperature = TemperatureStatusModel.Temperature.High;



    }
}
