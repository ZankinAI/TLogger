package com.project.tlogger.ui.history;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.tlogger.HistoryState;
import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.StateAdapter;
import com.project.tlogger.msg.model.MeasurementStatusModel;
import com.project.tlogger.msg.model.StoreDataModel;
import com.project.tlogger.msg.model.TemperatureStatusModel;
import com.project.tlogger.msg.model.Utils;
import com.project.tlogger.ui.AppClearHistory;
import com.project.tlogger.ui.AppSettings;
import com.project.tlogger.ui.temperature.TemperatureFragment;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private final static String TAG = "ContentFragment";

    private int statusIcon;
    private String statusText;
    private Object[] parsedStatus;

    public interface onSomeEventListener {
        public void someEvent(int pos);
    }


    onSomeEventListener someEventListener;
    private HistoryViewModel mViewModel;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }
    ArrayList<HistoryState> states = new ArrayList<HistoryState>();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.history_fragment, container, false);
        ListView measurmentsList = view.findViewById(R.id.measurmentsList);

        setInitialData();
        // создаем адаптер
        //ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,countries);
        StateAdapter stateAdapter = new StateAdapter(getActivity(),R.layout.list_item, states);
        // устанавливаем для списка адаптер
        measurmentsList.setAdapter(stateAdapter);

        setHasOptionsMenu(true);

        measurmentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                someEventListener.someEvent(i);


            }
        });


        return view;
    }

    private void setInitialData(){

        for(StoreDataModel storeDataModel: MainActivity.msgLib.storeDataModelList){
            parsedStatus = Utils.parsingEvent(storeDataModel.responseConfigData.status);
            MeasurementStatusModel.Measurement measurementStatus = (MeasurementStatusModel.Measurement) parsedStatus[0];
            MeasurementStatusModel.Failure failureStatus = (MeasurementStatusModel.Failure) parsedStatus[1];
            TemperatureStatusModel.Temperature temperatureStatus = (TemperatureStatusModel.Temperature) parsedStatus[2];
            createStatus(measurementStatus, failureStatus, temperatureStatus);
            states.add(new HistoryState(storeDataModel.nfcId, storeDataModel.textStatus, statusIcon, storeDataModel.apiVersion, storeDataModel.dataTime));
        }

        /*states.add(new HistoryState ("NFCID:083535301", "Запущено", R.drawable.logo_low_bat));
        states.add(new HistoryState ("NFCID:083535302", "Запущено", R.drawable.logo_no_space));
        states.add(new HistoryState ("NFCID:083535303", "Запущено", R.drawable.logo_high_temp));
        states.add(new HistoryState ("NFCID:083535304", "Запущено", R.drawable.logo_logging_state));
        states.add(new HistoryState ("NFCID:083535305", "Запущено", R.drawable.logo_low_temp));
        states.add(new HistoryState ("NFCID:083535306", "Запущено", R.drawable.logo_pristine_state));
        states.add(new HistoryState ("NFCID:083535307", "Запущено", R.drawable.logo_starting_state));
        states.add(new HistoryState ("NFCID:083535308", "Запущено", R.drawable.logo_logging_state));
        states.add(new HistoryState ("NFCID:083535309", "Запущено", R.drawable.logo_logging_state));
        states.add(new HistoryState ("NFCID:083535310", "Запущено", R.drawable.logo_logging_state));
        states.add(new HistoryState ("NFCID:083535311", "Запущено", R.drawable.logo_logging_state));
        states.add(new HistoryState ("NFCID:083535312", "Запущено", R.drawable.logo_starting_state));
        states.add(new HistoryState ("NFCID:083535313", "Запущено", R.drawable.logo_starting_state));
        states.add(new HistoryState ("NFCID:083535314", "Запущено", R.drawable.logo_starting_state));
        states.add(new HistoryState ("NFCID:083535315", "Запущено", R.drawable.logo_starting_state));*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int historyId = R.layout.history_fragment;
        AppClearHistory dialog = new AppClearHistory();
        Bundle bundle = new Bundle();
        bundle.putBoolean("", false);
        dialog.setArguments(bundle);
        dialog.show(getActivity().getSupportFragmentManager(), "custom");

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        // TODO: Use the ViewModel
    }

    private void createStatus(MeasurementStatusModel.Measurement measurementStatus, MeasurementStatusModel.Failure failureStatus,  TemperatureStatusModel.Temperature temperatureStatus ){

        switch (measurementStatus){
            case Reset:

            case Unknown:

                statusIcon = R.drawable.logo_unknown_state;
                break;
            case NotConfigured:

                statusIcon = R.drawable.logo_pristine_state;
                break;
            case Starting:

                statusIcon = R.drawable.logo_starting_state;
                break;
            case Configured:

                statusIcon = R.drawable.logo_settings;
                break;
            case Logging:
                if (temperatureStatus == TemperatureStatusModel.Temperature.Low){

                    statusIcon = R.drawable.logo_low_temp;
                }
                else if (temperatureStatus == TemperatureStatusModel.Temperature.High){

                    statusIcon = R.drawable.logo_high_temp;
                }
                else {

                    statusIcon = R.drawable.logo_logging_state;
                }
                break;
            case Stopped:
                switch (failureStatus){
                    case NoFailure:

                        statusIcon = R.drawable.logo_stopped_state;
                        break;
                    case Bod:

                        statusIcon = R.drawable.logo_low_bat;
                        break;
                    case Full:

                        statusIcon = R.drawable.logo_no_space;
                        break;
                    case Expired:

                        statusIcon = R.drawable.logo_stopped_expired;
                        break;
                }
                break;


        }
    }

}