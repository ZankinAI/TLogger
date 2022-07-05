package com.project.tlogger.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.databinding.SettingsFragmentBinding;

public class SettingsFragment extends Fragment {

    String field1;
    int mCounter = 8;

    private SettingsViewModel mViewModel;
    private TextView mTextView;

    private String mData;

    int startMeasurementsTime = 60;
    int startMeasurementsMeasure =0;

    int intervalOfMeasurmentsTime = 15;
    int intervalOfMeasurmentsMeasure = 0;

    int endMeasurementsTime = 0;
    int endMeasurementsMeasure = 0;

    int[] timeMeasure = {R.plurals.seconds_plural ,R.plurals.minuits_plural, R.plurals.hours_plural};

    private SettingsFragmentBinding binding;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        Log.d("mylog", "CreateView");
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textViewStart = binding.textStartupDelay;
        TextView textViewInterval = binding.textMeasurementInterval;
        TextView textMeasurementDuration = binding.textMeasurementDuration;
        TextView textTemperatureRange = binding.textTemperatureLimits;

        //textViewStart.setMovementMethod(new ScrollingMovementMethod());
        //settingsViewModel.getTime().observe(getViewLifecycleOwner(), textViewStart::setText);

        //ПЕРЕПУТАНО!!!
        int startMeasurementsTime = MainActivity.msgLib.cmdSetConfig.startDelayMeasure;
        int startMeasurementsMeasure = MainActivity.msgLib.cmdSetConfig.startDelay;

        Log.e("mytag", String.valueOf(MainActivity.msgLib.cmdSetConfig.startDelay) + " " + String.valueOf(MainActivity.msgLib.cmdSetConfig.startDelayMeasure));
        switch (startMeasurementsTime) {
            case 0:
                if (startMeasurementsMeasure == 0)
                {textViewStart.setText("Начать измерения немедленно");}
                else if (startMeasurementsMeasure <= 60)
                {textViewStart.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[startMeasurementsTime], startMeasurementsMeasure, startMeasurementsMeasure));}
                else
                {textViewStart.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[1], startMeasurementsMeasure / 60 % 60, startMeasurementsMeasure / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[startMeasurementsTime], startMeasurementsMeasure / 1 % 60, startMeasurementsMeasure / 1 % 60));}
                break;
            case 1:
                if (startMeasurementsMeasure == 0)
                {textViewStart.setText("Начать измерения немедленно");}
                else if (startMeasurementsMeasure <= 60)
                    textViewStart.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[startMeasurementsTime], startMeasurementsMeasure, startMeasurementsMeasure));
                else
                    textViewStart.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[2], startMeasurementsMeasure / 60 % 60, startMeasurementsMeasure / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[startMeasurementsTime], startMeasurementsMeasure / 1 % 60, startMeasurementsMeasure / 1 % 60));
                break;
            case 2:
                if (startMeasurementsMeasure == 0)
                {textViewStart.setText("Начать измерения немедленно");}
                else textViewStart.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[startMeasurementsTime], startMeasurementsMeasure, startMeasurementsMeasure));
                break;
        }

        //ПЕРЕПУТАНО
         int intervalOfMeasurmentsTime = MainActivity.msgLib.cmdSetConfig.intervalMeasure;
         int intervalOfMeasurmentsMeasure = MainActivity.msgLib.cmdSetConfig.interval;

        switch (intervalOfMeasurmentsTime) {
            case 0:
                if (intervalOfMeasurmentsMeasure == 0)
                {textViewInterval.setText("Периодичность измерений: 1 секунда");}
                else if (intervalOfMeasurmentsMeasure <= 60)
                {textViewInterval.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[intervalOfMeasurmentsTime], intervalOfMeasurmentsMeasure, intervalOfMeasurmentsMeasure));}
                else
                {textViewInterval.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[1], intervalOfMeasurmentsMeasure / 60 % 60, intervalOfMeasurmentsMeasure / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[intervalOfMeasurmentsTime], intervalOfMeasurmentsMeasure / 1 % 60, intervalOfMeasurmentsMeasure / 1 % 60));}
                break;
            case 1:
                if (intervalOfMeasurmentsMeasure == 0)
                {textViewInterval.setText("Периодичность измерений: 1 секунда");}
                else if (intervalOfMeasurmentsMeasure <= 60)
                    textViewInterval.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[intervalOfMeasurmentsTime], intervalOfMeasurmentsMeasure, intervalOfMeasurmentsMeasure));
                else
                    textViewInterval.setText("Периодичность измерений: " + getResources().getQuantityString(timeMeasure[2], intervalOfMeasurmentsMeasure / 60 % 60, intervalOfMeasurmentsMeasure / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[intervalOfMeasurmentsTime], intervalOfMeasurmentsMeasure / 1 % 60, intervalOfMeasurmentsMeasure / 1 % 60));
                break;
            case 2:
                if (intervalOfMeasurmentsMeasure == 0)
                {textViewInterval.setText("Периодичность измерений: 1 секунда");}
                else textViewInterval.setText("Начать измерения через " + getResources().getQuantityString(timeMeasure[intervalOfMeasurmentsTime], intervalOfMeasurmentsMeasure, intervalOfMeasurmentsMeasure));
                break;
            case 3:
                textViewInterval.setText("Периодичность измерений: 1 секунда");
                this.intervalOfMeasurmentsMeasure = 0;
                break;
        }

        //ПЕРЕПУТАНО
        int endMeasurementsTime = MainActivity.msgLib.cmdSetConfig.runningTimeMeasure;
        int endMeasurementsMeasure = MainActivity.msgLib.cmdSetConfig.runningTime;


        switch (endMeasurementsTime) {
            case 0:
                if (endMeasurementsMeasure == 0)
                {textMeasurementDuration.setText("Измерять, пока не закончится память");}
                else if (endMeasurementsMeasure <= 60)
                {textMeasurementDuration.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[endMeasurementsTime], endMeasurementsMeasure, endMeasurementsMeasure));}
                else
                {textMeasurementDuration.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[1], endMeasurementsMeasure / 60 % 60, endMeasurementsMeasure / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[endMeasurementsTime], endMeasurementsMeasure / 1 % 60, endMeasurementsMeasure / 1 % 60));}
                break;
            case 1:
                if (endMeasurementsMeasure == 0)
                {textMeasurementDuration.setText("Измерять, пока не закончится память");}
                else if (endMeasurementsMeasure <= 60)
                    textMeasurementDuration.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[endMeasurementsTime], endMeasurementsMeasure, endMeasurementsMeasure));
                else
                    textMeasurementDuration.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[2], endMeasurementsMeasure / 60 % 60, endMeasurementsMeasure / 60 % 60) + " " + getResources().getQuantityString(timeMeasure[endMeasurementsTime], endMeasurementsMeasure / 1 % 60, endMeasurementsMeasure / 1 % 60));
                break;
            case 2:
                if (endMeasurementsMeasure == 0)
                {textMeasurementDuration.setText("Измерять, пока не закончится память");}
                else textMeasurementDuration.setText("Продолжать измерения: " + getResources().getQuantityString(timeMeasure[endMeasurementsTime], endMeasurementsMeasure, endMeasurementsMeasure));
                break;
            case 3:
                textMeasurementDuration.setText("Измерять, пока не закончится память");
                this.endMeasurementsMeasure = 0;
                break;
        }

        int lower_range = MainActivity.msgLib.cmdSetConfig.validMinimum;
        int upper_range = MainActivity.msgLib.cmdSetConfig.validMaximum;
        textTemperatureRange.setText("Границы измерения: от " + lower_range/10 + " °C до " + upper_range/10 + " °C");



        //textViewStart.setMovementMethod(new ScrollingMovementMethod());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        // TODO: Use the ViewModel
    }
}