package com.project.tlogger.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

import org.jetbrains.annotations.Nullable;

public class NFCStart extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "DialogFragment";

    EditText lower_range, upper_range;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        MainActivity.msgLib.setConfigurationFlag = true;

        //Log.d("mytag", "true");

        View view = inflater.inflate(R.layout.nfc_start, container, false);

        Bundle bundle = getArguments();

        TextView textView = view.findViewById(R.id.nfc_start_text);

        if (bundle != null){
            String textToTextView = bundle.getString("");
            textView.setText(textToTextView);
        }
        view.findViewById(R.id.nfc_start_button).setOnClickListener(this);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    public void setText(String item){
        TextView view = (TextView) getView().findViewById(R.id.nfc_start_text);
        view.setText(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nfc_start_button:
                dismiss();
                break;
        }
    }
    @Override
    public void onDestroy(){
        MainActivity.msgLib.setConfigurationFlag = false;
        //Log.d("mytag", "false");
        super.onDestroy();
    }
}
