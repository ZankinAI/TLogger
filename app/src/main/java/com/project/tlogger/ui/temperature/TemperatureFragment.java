package com.project.tlogger.ui.temperature;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.ui.AppSettings;

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

        if (MainActivity.msgLib.activity==1)
            setHasOptionsMenu(true);





        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureViewModel.class);
        // TODO: Use the ViewModel
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
        AppSettings dialog = new AppSettings();
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

}