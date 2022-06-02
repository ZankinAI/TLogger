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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.tlogger.HistoryState;
import com.project.tlogger.R;
import com.project.tlogger.StateAdapter;
import com.project.tlogger.ui.temperature.TemperatureFragment;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private final static String TAG = "ContentFragment";
    public interface onSomeEventListener {
        public void someEvent(String s);
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

        measurmentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                someEventListener.someEvent("Item");


            }
        });


        return view;
    }

    private void setInitialData(){

        states.add(new HistoryState ("NFCID:083535301", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535302", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535303", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535304", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535305", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535306", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535307", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535308", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535309", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535310", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535311", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535312", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535313", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535314", "Запущено", R.drawable.starting_state));
        states.add(new HistoryState ("NFCID:083535315", "Запущено", R.drawable.starting_state));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        // TODO: Use the ViewModel
    }

}