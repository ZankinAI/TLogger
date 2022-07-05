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

public class TemperatureRange extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "DialogFragment";
    public interface OnInputListener {
        void sendInput(int dialogId, int text, int number);
    }
    public OnInputListener mOnInputListener;

    EditText lower_range, upper_range;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.temperature_range, container, false);

        Bundle bundle = getArguments();
        int[] temperatureRangeToTextView = bundle.getIntArray("");


        view.findViewById(R.id.temperature_range_button_ok).setOnClickListener(this);
        view.findViewById(R.id.temperature_range_cancel).setOnClickListener(this);

        lower_range = view.findViewById(R.id.lower_range);

        lower_range.setText(Integer.toString(temperatureRangeToTextView[0]));

        upper_range = view.findViewById(R.id.upper_range);

        upper_range.setText(Integer.toString(temperatureRangeToTextView[1]));

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        lower_range.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        upper_range.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
            case R.id.temperature_range_button_ok:
                if (lower_range != null && upper_range != null){
                    int lower_range_send = Integer.valueOf(lower_range.getText().toString().trim());
                    int upper_range_send = Integer.valueOf(upper_range.getText().toString().trim());
                    if (lower_range_send > upper_range_send){
                        int temp = lower_range_send;
                        lower_range_send = upper_range_send;
                        upper_range_send = temp;
                    }
                    MainActivity.msgLib.cmdSetConfig.validMinimum = lower_range_send*10;
                    MainActivity.msgLib.cmdSetConfig.validMaximum = upper_range_send*10;
                    mOnInputListener.sendInput(3, lower_range_send, upper_range_send);

                }
                else {
                    mOnInputListener.sendInput(3, 0, 0);
                }
                //
                dismiss();
                break;

            case R.id.temperature_range_cancel:
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
