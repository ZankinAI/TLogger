package com.project.tlogger.msg.model;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.utils.MPPointF;
import com.project.tlogger.R;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.sql.Timestamp;

public class GraphMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView dataTime;
    private TextView temperature;
    private long configTime;
    private  int interval, startDelay;


    private boolean isReverse = true;


    public GraphMarkerView(Context context, int layoutResource, long configTime, int interval, int startDelay) {
        super(context, layoutResource);
        dataTime = (TextView) findViewById(R.id.graph_data_time);
        temperature = (TextView) findViewById(R.id.graph_temperature);
        this.configTime = configTime;
        this.interval = interval;
        this.startDelay = startDelay;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        isReverse = !(highlight.getX() > 8);
        dataTime.setText(String.valueOf(new Timestamp(configTime * 1000 + ((long)e.getX() * interval + startDelay) * 1000))); // set the entry-value as the display text
        temperature.setText("Температура: " + String.valueOf(e.getY()) + "\u2103");
        super.refreshContent(e,highlight);
    }


    @Override
    public MPPointF getOffset() {
        MPPointF mpPointF = super.getOffset();
        if (!isReverse ) {
            mpPointF.x = -dataTime.getWidth();
        } else {
            mpPointF.x = 0;
        }
        mpPointF.y = -dataTime.getHeight();
        return mpPointF;
    }

}
