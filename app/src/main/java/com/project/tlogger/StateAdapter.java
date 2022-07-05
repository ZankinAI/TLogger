package com.project.tlogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
public class StateAdapter extends ArrayAdapter<HistoryState>{

    private LayoutInflater inflater;
    private int layout;
    private List<HistoryState> states;

    public StateAdapter(Context context, int resource, List<HistoryState> states) {
        super(context, resource, states);
        this.states = states;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ImageView measurmentIconView = convertView.findViewById(R.id.measurmentIcon);
        TextView nfcIdView = convertView.findViewById(R.id.nfcId);
        TextView stateView = convertView.findViewById(R.id.state);
        TextView apiView = convertView.findViewById(R.id.version);
        TextView timeView = convertView.findViewById(R.id.date);

        HistoryState state = states.get(position);

        measurmentIconView.setImageResource(state.getMeasurementResource());
        nfcIdView.setText(state.getNfcId());
        stateView.setText(state.getState());
        apiView.setText(state.getApi());
        timeView.setText(state.getTime());


        return convertView;
    }

    private class ViewHolder {
        final ImageView measurmentIconView;
        final TextView nfcIdView, stateView, apiView, timeView;

        ViewHolder(View view){
            measurmentIconView=view.findViewById(R.id.measurmentIcon);
            nfcIdView = view.findViewWithTag(R.id.nfcId);
            stateView = view.findViewById(R.id.state);
            apiView = view.findViewById(R.id.version);
            timeView = view.findViewById(R.id.date);
        }
    }

}
