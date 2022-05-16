package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.tlogger.R;

public class TemperatureStatusFragment extends Fragment {

    private TemperatureStatusViewModel mViewModel;

    public static TemperatureStatusFragment newInstance() {
        return new TemperatureStatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.temperature_status_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureStatusViewModel.class);
        // TODO: Use the ViewModel
    }

}