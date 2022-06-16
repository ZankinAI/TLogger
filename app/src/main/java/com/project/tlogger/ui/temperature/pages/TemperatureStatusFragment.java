package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

public class TemperatureStatusFragment extends Fragment {

    private TemperatureStatusViewModel mViewModel;

    public static TemperatureStatusFragment newInstance() {
        return new TemperatureStatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.temperature_status_fragment, container, false);
        TextView textViewStatus = view.findViewById(R.id.temp_info);
        textViewStatus.setText(MainActivity.msgLib.textStatus);

        TextView textViewApiVersion = view.findViewById(R.id.system_info);
        textViewApiVersion.setText(MainActivity.msgLib.apiVersion);

        TextView textViewMimeType = view.findViewById(R.id.mime_type);
        textViewMimeType.setText(MainActivity.msgLib.mimeType);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureStatusViewModel.class);
        // TODO: Use the ViewModel
    }

}