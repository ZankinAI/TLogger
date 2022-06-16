package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

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


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import kotlin.text.UStringsKt;

public class TemperatureChartFragment extends Fragment {
    private final static String TAG = "NFC Debbug";
    private TemperatureChartViewModel mViewModel;
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



        View view = inflater.inflate(R.layout.temperature_chart_fragment, container, false);

        TextView textNfcId = view.findViewById(R.id.nfc_id_text);
        textNfcId.setText(MainActivity.msgLib.nfcId);

        TextView textCount = view.findViewById(R.id.number_of_measurements_text);
        textCount.setText(String.valueOf(MainActivity.msgLib.count));

        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(MainActivity.msgLib.configTime+10800, 0, ZoneOffset.UTC);
        TextView textConfigurationTime = view.findViewById(R.id.configuration_time_text);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");;
        String formattedDateTime = dateTime.format(formatter);
        textConfigurationTime.setText(formattedDateTime);

        TextView textLoggingFor = view.findViewById(R.id.logging_for_text);
        String textLoggingForStr = String.valueOf(MainActivity.msgLib.count * MainActivity.msgLib.interval);
        textLoggingForStr +=" сек";
        textLoggingFor.setText(textLoggingForStr);

        TextView textMeasurements = view.findViewById(R.id.values_count_text);
        textMeasurements.setText(String.valueOf(MainActivity.msgLib.measurementsCount));

        TextView textMeasurementInterval = view.findViewById(R.id.measurment_intertval_text);
        String textMeasurementIntervalStr = String.valueOf(MainActivity.msgLib.interval) + " сек";
        textMeasurementInterval.setText(textMeasurementIntervalStr);

        TextView textMinValid = view.findViewById(R.id.min_valid_text);
        String textMinValidStr = String.valueOf(MainActivity.msgLib.validMinimum/10) + " 'C";
        textMinValid.setText(textMinValidStr);

        TextView textMaxValid = view.findViewById(R.id.max_valid_text);
        String textMaxValidStr = String.valueOf(MainActivity.msgLib.validMaximum/10) + " 'C";
        textMaxValid.setText(textMaxValidStr);

        //saveHtmlFile();

        WebView browserChart=view.findViewById(R.id.webview_chart);
        WebSettings webSettings = browserChart.getSettings();


        webSettings.setJavaScriptEnabled(true);
        String html = "<html><head><title>Title</title></head><body>This is random text.</body></html>";
        //browserChart.loadData(htmlContent, "text/html", "UTF-8");
        browserChart.loadUrl("file:///android_asset/chart1.html");
        browserChart.loadDataWithBaseURL("file:///android_asset/Js/", htmlContent, "text/html", "UTF-8", null);
        //browserChart.loadData(htmlContent, "text/html", "UTF-8");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureChartViewModel.class);
        // TODO: Use the ViewModel
    }

    private void saveHtmlFile() {

        String path = "file:///android_asset";
        String fileName = "chartTest.html";

        File file = new File(path, fileName);
        String html = "<html><head><title>Title</title></head><body>This is random text.</body></html>";

        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] data = html.getBytes();
            out.write(data);
            out.close();
            Log.e(TAG, "File Save : " + file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}