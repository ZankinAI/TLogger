package com.project.tlogger.ui.temperature;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.project.tlogger.ui.temperature.pages.TemperatureChartFragment;
import com.project.tlogger.ui.temperature.pages.TemperatureGraphFragment;
import com.project.tlogger.ui.temperature.pages.TemperatureStatusFragment;

public class TemperatureAdapter extends FragmentStateAdapter{
    public TemperatureAdapter(@NonNull FragmentActivity fm){
        super(fm);
    }
    @Override
    public int getItemCount(){
        return 3;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch (position){
            case 0:
                return new TemperatureStatusFragment();
            case 1:
                return new TemperatureChartFragment();
            case 2:
                return new TemperatureGraphFragment();

            default:
                return new TemperatureStatusFragment();

        }
    }

}
