package com.project.tlogger.msg.model;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String translateStatus(String status){
        String result;

        Pattern pattern = Pattern.compile("Stopped");
        Matcher matcher = pattern.matcher(status);
        result = matcher.replaceAll("Остановлен");

        pattern = Pattern.compile("ALERT");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("ПРЕДУЖДЕНИЕ");

        pattern = Pattern.compile("Current temperature");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Текущая температура");

        pattern = Pattern.compile("Minimum");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Минимум");

        pattern = Pattern.compile("Maximum");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Максимум");

        pattern = Pattern.compile("samples logged");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("значений измерено");

        pattern = Pattern.compile("seconds");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("секунд");

        pattern = Pattern.compile("minutes");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("минут");

        pattern = Pattern.compile("hours");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("часов");

        pattern = Pattern.compile("days");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("дней");

        pattern = Pattern.compile("Empty. Not yet configured");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Настройки логгера сброшены");

        pattern = Pattern.compile("Empty. Configured but not yet started");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Настройки логгера заданы. Измерение не запущено");

        pattern = Pattern.compile("Running. No sample taken yet");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Измерение запущено. Нет считанных значений");

        pattern = Pattern.compile("Running for");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("Идет измерение температуры в течение");

        pattern = Pattern.compile("first high excursion after");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("первое значение выше заданного получено спустя");

        pattern = Pattern.compile("first low excursion after");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("первое значение ниже заданного получено спустя");

        pattern = Pattern.compile("battary is empty");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("низкий уровень зараяда батареи");

        pattern = Pattern.compile("no space left for storing samples");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("память логгера заполнена");

        pattern = Pattern.compile("stopped after the configured time");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("измерение закончилось спустя по истечении заданного времени");

        pattern = Pattern.compile("last I2C access failed");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("ошибка I2C");

        pattern = Pattern.compile("last SPI access failed");
        matcher = pattern.matcher(result);
        result = matcher.replaceAll("ошибка SPI");



        return result;
    }


    public static Object[] parsingEvent(int status){

        Object[] resultParsing = new Object[3];
        Integer statusIsNull = status;
        if (statusIsNull == null)
        {
            resultParsing[0] = MeasurementStatusModel.Measurement.Unknown;
            resultParsing[1] = MeasurementStatusModel.Failure.Unknown;
            resultParsing[2] = TemperatureStatusModel.Temperature.Unknown;
            return resultParsing;
        }
        int result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_PRISTINE.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_PRISTINE.getValue()) resultParsing[0] = MeasurementStatusModel.Measurement.NotConfigured;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_CONFIGURED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_CONFIGURED.getValue()) resultParsing[0] = MeasurementStatusModel.Measurement.Configured;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STARTING.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STARTING.getValue()) resultParsing[0] = MeasurementStatusModel.Measurement.Starting;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue()) resultParsing[0] = MeasurementStatusModel.Measurement.Logging;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue()) resultParsing[0] = MeasurementStatusModel.Measurement.Stopped;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_STOPPED.getValue()) resultParsing[0] = MeasurementStatusModel.Measurement.Stopped;


        resultParsing[1] = MeasurementStatusModel.Failure.NoFailure;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_FULL.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_FULL.getValue()) resultParsing[1] = MeasurementStatusModel.Failure.Full;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_BOD.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_BOD.getValue()) resultParsing[1] = MeasurementStatusModel.Failure.Bod;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_EXPIRED.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_EXPIRED.getValue()) resultParsing[1] = MeasurementStatusModel.Failure.Expired;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_LOGGING.getValue()) resultParsing[2] = TemperatureStatusModel.Temperature.Normal;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_LOW.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_LOW.getValue()) resultParsing[2] = TemperatureStatusModel.Temperature.Low;

        result = status&Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_HIGH.getValue();
        if (result == Protocol.APP_MSG_EVENT.APP_MSG_EVENT_TEMPERATURE_TOO_HIGH.getValue()) resultParsing[2] = TemperatureStatusModel.Temperature.High;

        return resultParsing;


    }




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

    public static String masShortToString(short[] mas){
        String result="";
        if (mas==null)
            return result;

        byte[] temp = new byte[mas.length*2];
        for (int i = 0; i< mas.length; i++){
            temp[i*2] = (byte)(mas[i]&0x00ff);
            temp[i*2+1] = (byte)((mas[i]&0xff00)>>8);
        }
        result = new String(temp, StandardCharsets.UTF_16);
        result = Base64.encodeToString(temp, Base64.DEFAULT);
        return result;
    }

    public static short[] StringtoMasShort(String str){

        //byte[] temp = str.getBytes(StandardCharsets.UTF_16);
        byte[] temp = Base64.decode(str, Base64.DEFAULT);
        short[] mas = new short[temp.length/2];
        for (int i = 0; i<mas.length ; i++){
            mas[i] = (short) ((temp[i*2]&0xff)|((temp[i*2+1]&0xff)<<8));
        }

        return mas;


    }

    public static  short[] concatShorts(short[] first, short[] second) {

        if (((first == null)||(first.length == 0))&&((second == null)||(second.length == 0))) return null;

        if ((first == null)||(first.length == 0)) return second;

        if ((second == null)||(second.length == 0)) return first;

        short[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static short[] approximation(short[] mas){

        boolean returned = false;

        for (int i=0; i<mas.length; i++){
            if (mas[i] == 851) {
                returned = false;
                for (int k=i; k>=0; k--) {
                    if (mas[k] < 851) {
                        mas[i] = mas[k];
                        returned = true;
                        break;
                    }
                }
                if (returned) continue;
                else{
                    for (int k=i; k<=mas.length; k++) {
                        if (mas[k] < 851) {
                            mas[i] = mas[k];
                            returned = true;
                            break;
                        }
                    }

                }
            }
        }
        return mas;
    }

    public static String convertSeconds (int seconds, Context context){
        String result = "hui";
        if (seconds<60) result = String.valueOf(seconds) + " " + context.getResources().getString(R.string.second);
        if ((seconds>=60)&&(seconds<3600)){
            result = String.valueOf(seconds/60) + " " +context.getResources().getString(R.string.minute);
            if (seconds % 60 >0)
                result += " " + String.valueOf(seconds % 60) + " " + context.getResources().getString(R.string.second);
        }
        if (seconds>=3600)
        {
            result = String.valueOf(seconds/3600) + " " +context.getResources().getString(R.string.hour);
            if (seconds % 3600 / 60 >0)
                result+= " " + String.valueOf(seconds % 3600 / 60) + " " +context.getResources().getString(R.string.minute);
            if (seconds % 3600 % 60>0)
                result = String.valueOf(seconds/3600) + " " +context.getResources().getString(R.string.hour)  + " " + String.valueOf(seconds % 3600 % 60) + " " + context.getResources().getString(R.string.second);
        }
        return result;
    }


}