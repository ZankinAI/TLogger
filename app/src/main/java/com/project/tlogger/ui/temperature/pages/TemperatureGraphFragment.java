package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.opencsv.CSVWriter;
import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.msg.Lib;
import com.project.tlogger.msg.model.GraphMarkerView;
import com.project.tlogger.msg.model.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TemperatureGraphFragment extends Fragment implements OnChartGestureListener {

    private TemperatureGraphViewModel mViewModel;
    private Lib _msgLib;
    private short[] data;
    private short attainedMin;
    private short attainedMax;
    private short validMin;
    private short validMax;
    long configTime;
    int interval;
    int startDelay;




    public static TemperatureGraphFragment newInstance() {
        return new TemperatureGraphFragment();
    }

    private BarChart chart;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.temperature_graph_fragment, container, false);

       Button exportBtn = v.findViewById(R.id.button_export);

       _msgLib = MainActivity.msgLib;

       if (_msgLib.flagOpenFragmentFromHistory){
           data = Utils.approximation(Utils.StringtoMasShort(_msgLib.selectedStoreData.data));
           attainedMin = _msgLib.selectedStoreData.responseConfigData.attainedMinimum;
           attainedMax = _msgLib.selectedStoreData.responseConfigData.attainedMaximum;
           configTime = _msgLib.selectedStoreData.responseConfigData.configTime;
           interval = _msgLib.selectedStoreData.responseConfigData.interval;
           startDelay = _msgLib.selectedStoreData.responseConfigData.startDelay;
           validMin = _msgLib.selectedStoreData.responseConfigData.validMinimum;
           validMax = _msgLib.selectedStoreData.responseConfigData.validMaximum;
       }
       else if (_msgLib.flagTloggerConnected){
           data =Utils.approximation(Utils.StringtoMasShort(_msgLib.storeData.data));
           attainedMin = _msgLib.storeData.responseConfigData.attainedMinimum;
           attainedMax = _msgLib.storeData.responseConfigData.attainedMaximum;
           configTime = _msgLib.storeData.responseConfigData.configTime;
           interval = _msgLib.storeData.responseConfigData.interval;
           startDelay = _msgLib.storeData.responseConfigData.startDelay;
           validMin = _msgLib.storeData.responseConfigData.validMinimum;
           validMax = _msgLib.storeData.responseConfigData.validMaximum;
       }


       if (data==null)
       {
           return v;
       }


        ArrayList<BarEntry> entries = new ArrayList<>();

        int[] colors = new int[data.length];
        float[] hsv= new float[3];
        for (int i = 0; i< data.length; i++){
            entries.add(new BarEntry(i,   (float) (data[i]/10.0) ));
            hsv[0]=300 - (int)(95 + data[i]/10 * 2.38); //min -40, max 85.  всего 126 значений. 300- диапазон цветов от красного к фиолетовому.
            // 40 / 126 * 300 = 95 - значение цвета для 0 С

            hsv[1]=100;
            hsv[2]=100;
            colors[i]=Color.HSVToColor(hsv);
        }



        BarDataSet dataset = new BarDataSet(entries, "Temperature");

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Utils.convertSeconds((int) value*interval);

            }
        };

        dataset.setColors(colors);

        chart = v.findViewById(R.id.bar_chart);
        YAxis y = chart.getAxisLeft();
        y.setAxisMinimum(attainedMin/10-5);
        y.setAxisMaximum(attainedMax/10+3);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);  // Минимальный шаг оси (интервал) равен 1
        xAxis.setValueFormatter(formatter);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);

        GraphMarkerView marker = new GraphMarkerView(getContext(), R.layout.graph_marker_view, configTime, interval, 0);

        chart.setMarker(marker);

        chart.getXAxis().setLabelCount(5);

        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLinesBehindData(true);
        chart.getXAxis().setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getLegend().setEnabled(false);
        chart.setScaleYEnabled(false);
        LimitLine maxlimitLine = new LimitLine(validMax/10, "Max");
        LimitLine minlimitLine = new LimitLine(validMin/10, "Min");
        y.addLimitLine(maxlimitLine);
        y.addLimitLine(minlimitLine);
        y.setDrawLimitLinesBehindData(false);

        chart.setDescription(null);
        chart.setNoDataText("Нет данных");

        BarData data = new BarData(dataset);
        chart.setData(data);



        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "exportBtn Clicked");
                exportDataToCSV();
            }
        });


       return v;
    }

    private void exportDataToCSV(){

        try {
            String baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String fileName = "TSense"+dateFormat.format(timestamp)+".csv";
            String filePath = baseDir + File.separator + fileName;
            File f = new File(filePath);
            CSVWriter writer;
            FileWriter mFileWriter;
            // File exist
            if(f.exists())
            {
                Log.d("myTag", "exists");
                mFileWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mFileWriter);
            }
            else
            {
                Log.d("myTag", "not exists");
                writer = new CSVWriter(new FileWriter(filePath));
            }

            String[] header = {"№", "Время считывания", "Температура"};
            writer.writeNext(header);

            List<String[]> dataList = new ArrayList<String[]>();

            for (int i = 0; i < this.data.length; i++){
                dataList.add(new String[]{String.valueOf(i), String.valueOf(new Timestamp(this.configTime * 1000 +(i * this.interval + this.startDelay) * 1000)), String.valueOf(this.data[i]/10.0)});
            }

            writer.writeAll(dataList);
            writer.close();
            String info = "Файл " + fileName + " сохранен в папку " + baseDir;
            Toast.makeText(getContext(),info, Toast.LENGTH_SHORT).show();



            //callRead();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Не удалось сохранить файл", Toast.LENGTH_SHORT).show();
        }

       /* String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "Test.pdf";

        File file = new File(stringFile);
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("application/pdf");
        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        startActivity(Intent.createChooser(intentShare, "Share the file ..."));*/

    }

    private static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureGraphViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {


    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}