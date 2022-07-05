package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.msg.Lib;
import com.project.tlogger.msg.model.Utils;

import java.util.ArrayList;

public class TemperatureGraphFragment extends Fragment implements OnChartGestureListener {

    private TemperatureGraphViewModel mViewModel;
    private Lib _msgLib;
    private short[] data;

    public static TemperatureGraphFragment newInstance() {
        return new TemperatureGraphFragment();
    }

    private BarChart chart;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.temperature_graph_fragment, container, false);


       _msgLib = MainActivity.msgLib;

       if (_msgLib.flagOpenFragmentFromHistory)
           data = Utils.StringtoMasShort(_msgLib.selectedStoreData.data);
       else if (_msgLib.flagTloggerConnected)
           data = Utils.StringtoMasShort(_msgLib.storeData.data);

       if (data==null)
       {
           return v;
       }


        ArrayList<BarEntry> entries = new ArrayList<>();

        int[] colors = new int[data.length];
        float[] hsv= new float[3];
        for (int i = 0; i< data.length; i++){
            entries.add(new BarEntry(i,   (float) (data[i]/10.0) ));
            hsv[0]=300-data[i]/10;
            hsv[1]=100;
            hsv[2]=100;
            colors[i]=Color.HSVToColor(hsv);
        }



        BarDataSet dataset = new BarDataSet(entries, "Temperature");
       // dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Январь");
        labels.add("Февраль");
        labels.add("Март");
        labels.add("Апрель");

        dataset.setColors(colors);
        chart = v.findViewById(R.id.bar_chart);
        YAxis y = chart.getAxisLeft();
        y.setAxisMinimum(-50);
        y.setAxisMaximum(50);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawLabels(false);


        chart.getXAxis().setLabelCount(4);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawLimitLinesBehindData(false);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLinesBehindData(true);
        chart.getXAxis().setDrawLabels(true);
        chart.getLegend().setEnabled(false);

        BarData data = new BarData(dataset);
        chart.setData(data);


       return v;
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