package com.project.tlogger.ui.temperature;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

public class TemperatureFragment extends Fragment {

    private TemperatureViewModel mViewModel;

    public static TemperatureFragment newInstance() {
        return new TemperatureFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temperature_fragment, container, false);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewpager2);
        TemperatureAdapter temperatureAdapter = new TemperatureAdapter(getActivity());
        viewPager2.setAdapter(temperatureAdapter);
        viewPager2.setCurrentItem(0);
        TextView title  = view.findViewById(R.id.title_temperature);

        if (MainActivity.msgLib.flagOpenFragmentFromHistory)
            title.setText(getResources().getString(R.string.title_history));
        else title.setText(getResources().getString(R.string.title_temperature));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureViewModel.class);
        // TODO: Use the ViewModel
    }

}