package com.project.tlogger.ui.settings;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<Integer> time;
    private MutableLiveData<Integer> number;

    public MutableLiveData<Integer> getTime() {
        return time;
    }

    public LiveData<Integer> getNumber() {
        return number;
    }
}