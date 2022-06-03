package com.project.tlogger.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.tlogger.R;

import org.jetbrains.annotations.Nullable;

public class StartMeasurements extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "DialogFragment";
    public interface OnInputListener {
        void sendInput(int text, int number);
    }
    public OnInputListener mOnInputListener;

    EditText etext;
    Spinner espinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.start_measurements, container, false);

        Bundle bundle = getArguments();
        int[] timeToTextView = bundle.getIntArray("");


        view.findViewById(R.id.start_measurement_button_cancel).setOnClickListener(this);
        view.findViewById(R.id.start_measurement_button_ok).setOnClickListener(this);
        etext = view.findViewById(R.id.start_time);

        etext.setText(Integer.toString(timeToTextView[0]));

        espinner = view.findViewById(R.id.spinner1);

        espinner.setSelection(timeToTextView[1]);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.start_measurement_button_ok:
                if (etext != null){
                    int text = Integer.valueOf(etext.getText().toString().trim());
                    int textSpinner = espinner.getSelectedItemPosition();
                    mOnInputListener.sendInput(text, textSpinner);

                }
                else {
                    mOnInputListener.sendInput(0, 0);
                }
                //
                dismiss();
                break;
            case R.id.start_measurement_button_cancel:
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
