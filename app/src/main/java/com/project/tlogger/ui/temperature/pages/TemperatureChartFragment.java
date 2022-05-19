package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.project.tlogger.R;


import kotlin.text.UStringsKt;

public class TemperatureChartFragment extends Fragment {

    private TemperatureChartViewModel mViewModel;
    public static String  htmlContent =
            "<html>" +
                    "<head>" +
                    "<script type=\"text/javascript\" src=\"file:///android_asset/Js/fusioncharts.js\"></script>" +
                    "<meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1'/>" +
                    "</head>" +
                        "<body>" +
                        "<div id='container'>Preparing graph!...</div>" +
                         "<script type='text/javascript'>" +
                    "new FusionCharts({" +
                    "type: 'hlineargauge'," +
                    "renderAt: 'container'," +
                    "width: '100%'," +
                    "height: '100%'," +
                    "dataFormat: 'json'," +
                    "dataSource: {" +
                    "\"chart\": {" +
                        "\"theme\": \"fint\"," +
                        "\"bgColor\": \"#ffffff\"," +
                        "\"showBorder\": \"0\"," +
                        "\"lowerLimit\": \"0\"," +
                        "\"upperLimit\": \"100\"," +
                        "\"chartBottomMargin\": \"0\"," +
                        "\"chartTopMargin: \"0\"," +
                        "\"valueFontSize\": \"12\"," +
                        "\"valueFontBold\": \"0\"," +
                        "\"gaugeFillMix\": \"{{light-10}},{{light-70}},{{dark-10}}\"," +
                        "\"gaugeFillRatio\": \"40,20,40\"," +
                        "\"showTickMarks\": \"1\"," +
                        "\"showTickValues\": \"1\"," +
                        "\"pointerOnTop\": \"1\"," +
                        "\"valueAbovePointer\": \"0\"," +
                        "\"majorTMNumber\": \"3\"," +
                        "\"placeValuesInside\": \"1\"," +
                    "}," +
                    "\"colorRange\": {" +
                        "\"color\": [{," +
                            "\"minValue\": \"20\","+
                            "\"maxValue\": \"40\","+
                            "\"label\": \"Низкая\","+
                            "\"code\": \"#ff6666\","+
                            "},{"+
                            "\"minValue\": \"41\","+
                            "\"maxValue\": \"60\","+
                            "\"label\": \"Допустимая\","+
                            "\"code\": \"#70db70\","+
                            "},{"+
                            "\"minValue\": \"61\","+
                            "\"maxValue\": \"100\","+
                            "\"label\": \"Высокая\","+
                            "\"code\": \"#ff6666\","+
                        "}]"+
                    "}," +
                    "\"pointers\": {" +
                        "\"pointer\": ["+
                            "{\"value\": \"40\",}"+
                            "{\"value\": \"60\",}"+
                        "]}," +
                    "}" +
                    "}).render();" +
                    "</script>" +
                    "</body>" +
                    "</html>";


    public static TemperatureChartFragment newInstance() {
        return new TemperatureChartFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.temperature_chart_fragment, container, false);
        WebView browserChart=view.findViewById(R.id.webview_chart);
        WebSettings webSettings = browserChart.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //browserChart.loadData(htmlContent, "text/html", "UTF-8");
        browserChart.loadUrl("file:///android_asset/chart1.html");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureChartViewModel.class);
        // TODO: Use the ViewModel
    }

}