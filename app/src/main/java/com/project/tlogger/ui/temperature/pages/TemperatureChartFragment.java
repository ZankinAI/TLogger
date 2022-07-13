package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.msg.Lib;
import com.project.tlogger.msg.model.MeasurementStatusModel;
import com.project.tlogger.msg.model.TemperatureStatusModel;
import com.project.tlogger.msg.model.Utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import kotlin.text.UStringsKt;

public class TemperatureChartFragment extends Fragment {
    private final static String TAG = "NFC Debbug";
    private TemperatureChartViewModel mViewModel;

    private Lib _msgLib;

    private String nfcId;
    private int count;
    private String dataTime;
    private int interval;
    private int measurementsCount;
    private short validMin;
    private short validMax;
    private short attainedMin;
    private short attainedMax;
    private int measurementsStatus;


    public static String  htmlContent =
            "<html>\n" +
                    "<head>\n" +
                    "<script type=\"text/javascript\" src=\"fusioncharts.js\"></script>" +
                    "<meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1'/>\n" +
                    "</head>\n" +
                        "<body>\n" +
                        "<div id='container'>Preparing graph!...</div>\n" +
                         "<script type='text/javascript'>\n" +
                    "new FusionCharts({\n" +
                    "type: 'hlineargauge',\n" +
                    "renderAt: 'container',\n" +
                    "width: '100%',\n" +
                    "height: '100%',\n" +
                    "dataFormat: 'json',\n" +
                    "dataSource: {\n" +
                    "\"chart\": {\n" +
                        "\"theme\": \"fint\",\n" +
                        "\"bgColor\": \"#ffffff\",\n" +
                        "\"showBorder\": \"0\",\n" +
                        "\"lowerLimit\": \"0\",\n" +
                        "\"upperLimit\": \"100\",\n" +
                        "\"chartBottomMargin\": \"0\",\n" +
                        "\"chartTopMargin\": \"0\",\n" +
                        "\"valueFontSize\": \"12\",\n" +
                        "\"valueFontBold\": \"0\",\n" +
                        "\"gaugeFillMix\": \"{{light-10}},{{light-70}},{{dark-10}}\",\n" +
                        "\"gaugeFillRatio\": \"40,20,40\",\n" +
                        "\"showTickMarks\": \"1\",\n" +
                        "\"showTickValues\": \"1\",\n" +
                        "\"pointerOnTop\": \"1\",\n" +
                        "\"valueAbovePointer\": \"0\",\n" +
                        "\"majorTMNumber\": \"3\",\n" +
                        "\"placeValuesInside\": \"1\",\n" +
                    "},\n" +
                    "\"colorRange\": {\n" +
                        "\"color\": [{\n" +
                            "\"minValue\": \"20\",\n"+
                            "\"maxValue\": \"40\",\n"+
                            "\"label\": \"Низкая\",\n"+
                            "\"code\": \"#ff6666\",\n"+
                            "},{\n"+
                            "\"minValue\": \"41\",\n"+
                            "\"maxValue\": \"60\",\n"+
                            "\"label\": \"Допустимая\",\n"+
                            "\"code\": \"#70db70\",\n"+
                            "},{\n"+
                            "\"minValue\": \"61\",\n"+
                            "\"maxValue\": \"100\",\n"+
                            "\"label\": \"Высокая\",\n"+
                            "\"code\": \"#ff6666\",\n"+
                        "}]\n"+
                    "},\n" +
                    "\"pointers\": {\n" +
                        "\"pointer\": [\n"+
                            "{\"value\": \""+String.valueOf(50)+"\",},\n"+
                            "{\"value\": \"60\",},\n"+
                        "]},\n" +
                    "}\n" +
                    "}).render();\n" +
                    "</script>\n" +
                    "</body>\n" +
                    "</html>\n";


    public static TemperatureChartFragment newInstance() {
        return new TemperatureChartFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        boolean flagStatusNormal = true;



        _msgLib = MainActivity.msgLib;

        createDataFragment();

        if ((attainedMax>validMax)||(attainedMin<validMin))
            flagStatusNormal = false;

        if (measurementsCount == 0)
            flagStatusNormal = false;
        View view = inflater.inflate(R.layout.temperature_chart_fragment, container, false);

        TextView textNfcId = view.findViewById(R.id.nfc_id_text);
        textNfcId.setText(nfcId);

        TextView textCount = view.findViewById(R.id.number_of_measurements_text);
        textCount.setText(String.valueOf(count));

        /*LocalDateTime dateTime = LocalDateTime.ofEpochSecond(MainActivity.msgLib.configTime+10800, 0, ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);*/
        TextView textConfigurationTime = view.findViewById(R.id.configuration_time_text);
        textConfigurationTime.setText(dataTime);

        TextView textLoggingFor = view.findViewById(R.id.logging_for_text);
        String textLoggingForStr = Utils.convertSeconds(count * interval);
        textLoggingFor.setText(textLoggingForStr);

        TextView textMeasurements = view.findViewById(R.id.values_count_text);
        if (!flagStatusNormal) textMeasurements.setTextColor(Color.rgb(255,0,0));
        else textMeasurements.setTextColor(Color.rgb(7,159,13));
        textMeasurements.setText(String.valueOf(measurementsCount));

        TextView textStatusOfMeasured = view.findViewById(R.id.status_text);
        if (!flagStatusNormal) textStatusOfMeasured.setTextColor(Color.rgb(255,0,0));
        else textStatusOfMeasured.setTextColor(Color.rgb(7,159,13));
        textStatusOfMeasured.setText("OK");

        if ((attainedMax>validMax)||(attainedMin<validMin))
            textStatusOfMeasured.setText("Выход за пределы");

        if (measurementsCount == 0)
            textStatusOfMeasured.setText("Нет данных");





        TextView textMeasurementInterval = view.findViewById(R.id.measurment_intertval_text);
        if (!flagStatusNormal) textMeasurementInterval.setTextColor(Color.rgb(255,0,0));
        else textMeasurementInterval.setTextColor(Color.rgb(7,159,13));
        String textMeasurementIntervalStr = String.valueOf(interval) + " сек";
        textMeasurementInterval.setText(textMeasurementIntervalStr);

        TextView textMinValid = view.findViewById(R.id.min_valid_text);
        if (!flagStatusNormal) textMinValid.setTextColor(Color.rgb(255,0,0));
        else textMinValid.setTextColor(Color.rgb(7,159,13));
        String textMinValidStr = String.valueOf(validMin/10) +  "\u2103";
        textMinValid.setText(textMinValidStr);

        TextView textMaxValid = view.findViewById(R.id.max_valid_text);
        if (!flagStatusNormal) textMaxValid.setTextColor(Color.rgb(255,0,0));
        else textMaxValid.setTextColor(Color.rgb(7,159,13));
        String textMaxValidStr = String.valueOf(validMax/10) + "\u2103";
        textMaxValid.setText(textMaxValidStr);

        //saveHtmlFile();

        WebView browserChart=view.findViewById(R.id.webview_chart);
        WebSettings webSettings = browserChart.getSettings();


        webSettings.setJavaScriptEnabled(true);
        String html = "<html><head><title>Title</title></head><body>This is random text.</body></html>";
        //browserChart.loadData(htmlContent, "text/html", "UTF-8");
        //browserChart.loadUrl("file:///android_asset/chart1.html");
        String htmlText = createChartHtml((float)(validMin/10.0), (float)(validMax/10.0), (float)(attainedMin/10.0),  (float)(attainedMax/10.0));
        browserChart.loadDataWithBaseURL("file:///android_asset/Js/", htmlText, "text/html", "UTF-8", null);
        //browserChart.loadData(htmlContent, "text/html", "UTF-8");
        return view;
    }

    private String createChartHtml(float validMin, float validMax, float attainedMin, float attainedMax){

        boolean addTrandPoints = true;

        final float  MinLimit = -40.0f;
        final float MaxLimit = 85.0f;

        float minLimit = MinLimit;
        float maxLimit = MaxLimit;

        minLimit = validMin;
        maxLimit = validMax;

        float configMin = validMin;
        float configMax = validMax;
        float recMin = attainedMin;
        float recMax = attainedMax;

        if (recMin*10 == 32767) {

            recMin = configMin;
        }
        if (recMax*10 == -32768){
            addTrandPoints = false;
            recMax = configMax;
        }


        if (recMin < configMin) minLimit = recMin;
        if (recMax > configMax) maxLimit = recMax;

        minLimit -= 5.0f;
        maxLimit += 5.0f;

        if (minLimit < MinLimit) minLimit = MinLimit;
        if (maxLimit > MaxLimit) maxLimit = MaxLimit;

        String trendPoint;
        if (addTrandPoints){
            trendPoint =                 "\"trendPoints\": {\n" +
                    "\"point\": [\n" +
                    "{\n"+
                    "\"startValue\": \""+String.valueOf(recMin)+"\",\n"+
                    "\"color\": \"#dddddd\",\n" +
                    "\"dashed\": \"1\",\n" +
                    "\"dashlen\": \"3\",\n" +
                    "\"dashgap\": \"3\",\n" +
                    "\"thickness\": \"2\",\n" +
                    "\"useMarker\":\"1\",\n" +
                    "\"showOnTop\":\"0\",\n" +
                    "},\n" +
                    "{\n"+
                    "\"startValue\": \""+String.valueOf(recMax)+"\",\n"+
                    "\"color\": \"#dddddd\",\n" +
                    "\"dashed\": \"1\",\n" +
                    "\"dashlen\": \"3\",\n" +
                    "\"dashgap\": \"3\",\n" +
                    "\"thickness\": \"2\",\n" +
                    "\"useMarker\":\"1\",\n" +
                    "\"showOnTop\":\"0\",\n" +
                    "},\n" +
                    "{\n"+
                    "\"startValue\": \""+String.valueOf(recMin)+"\",\n" +
                    "\"endValue\": \""+String.valueOf(recMax)+"\",\n" +
                    "\"displayValue\": \" \",\n" +
                    "\"alpha\": \"40\"" +
                    "}\n" +
                    "]},\n";

        }else trendPoint = "";

        String htmlChart =  "<html>\n" +
                "<head>\n" +
                "<script type=\"text/javascript\" src=\"fusioncharts.js\"></script>" +
                "<meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1'/>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div id='container'>Preparing graph!...</div>\n" +
                "<script type='text/javascript'>\n" +
                "new FusionCharts({\n" +
                "type: 'hlineargauge',\n" +
                "renderAt: 'container',\n" +
                "width: '100%',\n" +
                "height: '100%',\n" +
                "dataFormat: 'json',\n" +
                "dataSource: {\n" +
                "\"chart\": {\n" +
                "\"theme\": \"fint\",\n" +
                "\"bgColor\": \"#ffffff\",\n" +
                "\"showBorder\": \"0\",\n" +
                "\"lowerLimit\": \""+String.valueOf(minLimit)+"\",\n" +
                "\"upperLimit\": \""+String.valueOf(maxLimit)+"\",\n" +
                "\"chartBottomMargin\": \"0\",\n" +
                "\"chartTopMargin\": \"0\",\n" +
                "\"valueFontSize\": \"12\",\n" +
                "\"valueFontBold\": \"0\",\n" +
                "\"gaugeFillMix\": \"{{light-10}},{{light-70}},{{dark-10}}\",\n" +
                "\"gaugeFillRatio\": \"40,20,40\",\n" +
                "\"showTickMarks\": \"1\",\n" +
                "\"showTickValues\": \"1\",\n" +
                "\"pointerOnTop\": \"1\",\n" +
                "\"valueAbovePointer\": \"0\",\n" +
                "\"majorTMNumber\": \"3\",\n" +
                "\"placeValuesInside\": \"1\",\n" +
                "},\n" +
                "\"colorRange\": {\n" +
                "\"color\": [{\n" +
                "\"minValue\": \""+String.valueOf(minLimit)+"\",\n"+
                "\"maxValue\": \""+String.valueOf(configMin)+"\",\n"+
                "\"label\": \"Низкая\",\n"+
                "\"code\": \"#f59842\",\n"+
                "},{\n"+
                "\"minValue\": \""+String.valueOf(configMin)+"\",\n"+
                "\"maxValue\": \""+String.valueOf(configMax)+"\",\n"+
                "\"label\": \"Допустимая\",\n"+
                "\"code\": \"#4290f5\",\n"+
                "},{\n"+
                "\"minValue\": \""+String.valueOf(configMax)+"\",\n"+
                "\"maxValue\": \""+String.valueOf(maxLimit)+"\",\n"+
                "\"label\": \"Высокая\",\n"+
                "\"code\": \"#f59842\",\n"+
                "}]\n"+
                "},\n" +
                "\"pointers\": {\n" +
                "\"pointer\": [\n"+
                "{\"value\": \""+String.valueOf(configMin)+"\",},\n"+
                "{\"value\": \""+String.valueOf(configMax)+"\",},\n"+
                "]},\n" +
                trendPoint
                +
                "}\n" +
                "}).render();\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";

        return htmlChart;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureChartViewModel.class);
        // TODO: Use the ViewModel
    }

    private void createDataFragment(){
        if (_msgLib.flagOpenFragmentFromHistory){
            nfcId = _msgLib.selectedStoreData.nfcId;
            count = _msgLib.selectedStoreData.responseConfigData.count;
            dataTime = String.valueOf(new Timestamp((long)_msgLib.selectedStoreData.responseConfigData.configTime*1000));
            interval = _msgLib.selectedStoreData.responseConfigData.interval;
            measurementsCount = _msgLib.selectedStoreData.retrievedCount;
            validMin = _msgLib.selectedStoreData.responseConfigData.validMinimum;
            validMax = _msgLib.selectedStoreData.responseConfigData.validMaximum;
            attainedMin =_msgLib.selectedStoreData.responseConfigData.attainedMinimum;
            attainedMax =_msgLib.selectedStoreData.responseConfigData.attainedMaximum;
            measurementsStatus = _msgLib.selectedStoreData.statusOfMeasured;
        }
        else if (_msgLib.flagTloggerConnected) {

            nfcId = _msgLib.storeData.nfcId;
            count = _msgLib.storeData.responseConfigData.count;
            dataTime = String.valueOf(new Timestamp((long)_msgLib.storeData.responseConfigData.configTime*1000));
            interval = _msgLib.storeData.responseConfigData.interval;
            measurementsCount = _msgLib.storeData.retrievedCount;
            validMin = _msgLib.storeData.responseConfigData.validMinimum;
            validMax = _msgLib.storeData.responseConfigData.validMaximum;
            attainedMin =_msgLib.storeData.responseConfigData.attainedMinimum;
            attainedMax =_msgLib.storeData.responseConfigData.attainedMaximum;
            measurementsStatus = _msgLib.storeData.statusOfMeasured;

        }


    }



}