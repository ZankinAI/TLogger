package com.project.tlogger.ui.temperature.pages;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
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
import java.io.FileOutputStream;
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
    private short count;
    private short receivedCount;
    long configTime;
    int interval;
    int startDelay;
    private BarChart topdfChart;




    public static TemperatureGraphFragment newInstance() {
        return new TemperatureGraphFragment();
    }

    private BarChart chart;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.temperature_graph_fragment_adaptive, container, false);

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
           count = _msgLib.selectedStoreData.responseConfigData.count;
           receivedCount = (short)_msgLib.selectedStoreData.retrievedCount;
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
           count = _msgLib.storeData.responseConfigData.count;
           receivedCount = (short)_msgLib.storeData.retrievedCount;
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
                return Utils.convertSeconds((int)(value + 1)*interval, getContext());

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

        chart.setDrawingCacheEnabled(true);
        chart.buildDrawingCache(true);

        BarData data = new BarData(dataset);
        chart.setData(data);



        topdfChart = chart;



        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myTag", "exportBtn Clicked");
                exportDataToCSV();
                generatePDF();
            }
        });


       return v;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
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
                dataList.add(new String[]{String.valueOf(i), String.valueOf(new Timestamp(this.configTime * 1000 +((i+1) * this.interval + this.startDelay) * 1000)), String.format("%2f", this.data[i]/10.0)});
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

    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        int pageHeight = 3000;
        int pagewidth = 2000;
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        //Bitmap bm = loadBitmapFromView(this.topdfChart);
        Bitmap bm = Bitmap.createBitmap(this.topdfChart.getDrawingCache());
        canvas.drawBitmap(bm, 700, 200, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(30);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(getContext(), R.color.black));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        title.setTextAlign(Paint.Align.CENTER);
        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("График зависимости температуры", 1000, 100, title);
        canvas.drawText("Создан :" + dateFormat.format(timestamp) , 1000, 130, title);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(25);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("Начало измерений: " + String.valueOf(new Timestamp(this.configTime * 1000)), 20, 200, paint);

        canvas.drawText("Продолжительность измерения: " + Utils.convertSeconds(interval * count, getContext()) , 20, 250, paint);

        canvas.drawText("Интервал измерений: " + Utils.convertSeconds(interval, getContext()), 20, 300, paint);

        canvas.drawText("Измерено значений: " + String.valueOf(count) , 20, 350, paint);

        canvas.drawText("Считано значений: " + String.valueOf(receivedCount), 20, 400, paint);

        canvas.drawText("Максимальное допустимое значение: " + String.valueOf(validMax/10.0) + " \u2103", 20, 450, paint);

        canvas.drawText("Максимальное измеренное значение: " + String.valueOf(attainedMax/10.0) + " \u2103", 20, 500, paint);

        canvas.drawText("Минимальное допустимое значение: " + String.valueOf(validMin/10.0) + " \u2103", 20, 550, paint);

        canvas.drawText("Минимальное измеренное значение: " + String.valueOf(attainedMin/10.0) + " \u2103", 20, 600, paint);
        //canvas.drawText("Geeks for Geeks", 209, 80, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(getContext(), R.color.purple_200));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);


        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss");

        String fileName = "TSense"+dateFormat.format(timestamp)+".pdf";
        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), fileName);

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            //Toast.makeText(getContext(), "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
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