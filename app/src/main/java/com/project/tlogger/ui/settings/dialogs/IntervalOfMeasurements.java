package com.project.tlogger.ui.settings.dialogs;

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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

import org.jetbrains.annotations.Nullable;

public class IntervalOfMeasurements extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "DialogFragment";
    public interface OnInputListener {
        void sendInput(int dialogId, int text, int number);
    }
    public OnInputListener mOnInputListener;

    EditText etext;
    Spinner espinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.interval_of_measurements, container, false);

        Bundle bundle = getArguments();
        int[] timeToTextView = bundle.getIntArray("");


        view.findViewById(R.id.interval_of_measurements_button_ok).setOnClickListener(this);
        view.findViewById(R.id.interval_of_measurements_cancel).setOnClickListener(this);
        etext = view.findViewById(R.id.interval_of_measurements_time);

        etext.setText(Integer.toString(timeToTextView[0]));

        espinner = view.findViewById(R.id.interval_of_measurements_spinner);

        espinner.setSelection(timeToTextView[1]);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        etext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.interval_of_measurements_button_ok:
                if (etext != null){
                    int text = Integer.valueOf(etext.getText().toString().trim());
                    MainActivity.msgLib.cmdSetConfig.interval = text;
                    int textSpinner = espinner.getSelectedItemPosition();
                    MainActivity.msgLib.cmdSetConfig.intervalMeasure = textSpinner;
                    mOnInputListener.sendInput(1, text, textSpinner);

                }
                else {
                    mOnInputListener.sendInput(1, 0, 0);
                    MainActivity.msgLib.cmdSetConfig.intervalMeasure = 0;
                    MainActivity.msgLib.cmdSetConfig.interval = 0;
                }
                //
                dismiss();
                break;
            case R.id.interval_of_measurements_cancel:
                dismiss();
                break;
            default:
                break;
        }

    }

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}
