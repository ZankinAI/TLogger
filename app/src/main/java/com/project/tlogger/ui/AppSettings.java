package com.project.tlogger.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

import org.jetbrains.annotations.Nullable;

public class AppSettings extends DialogFragment implements View.OnClickListener {

    public interface ConfirmationListener {
        void confirmClear();
    }
    public ConfirmationListener confirmationListener;

    private static final String TAG = "DialogFragment";

    EditText lower_range, upper_range;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //MainActivity.msgLib.setConfigurationFlag = true;

        //Log.d("mytag", "true");

        View view = inflater.inflate(R.layout.app_settings, container, false);

        Bundle bundle = getArguments();

        //TextView textView = view.findViewById(R.id.nfc_start_text);
        RadioButton rusRadioBtn = view.findViewById(R.id.app_settings_ru);
        RadioButton enRadioBtn = view.findViewById(R.id.app_settings_en);

        if (MainActivity.msgLib.language==1) rusRadioBtn.setChecked(true);
        if (MainActivity.msgLib.language==2) enRadioBtn.setChecked(true);

        if (bundle != null){
            Boolean textToTextView = bundle.getBoolean("");
            //textView.setText(textToTextView);
        }
        //view.findViewById(R.id.app_settings_button).setOnClickListener(this);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    /*public void setText(String item){
        TextView view = (TextView) getView().findViewById(R.id.nfc_start_text);
        view.setText(item);
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.app_settings_button:

                new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                        .setTitle(getResources().getString(R.string.clear_history_confirm))
                        .setMessage(getResources().getString(R.string.clear_history_sure))
                        .setPositiveButton(getResources().getString(R.string.cancel_btn), null)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.e("mytag", "clear");
                                confirmationListener.confirmClear();
                                Toast.makeText(getActivity(), getResources().getString(R.string.clear_history_cleared),
                                        Toast.LENGTH_LONG).show();
                            }
                        }).show();
                break;
        }
    }
    /*@Override
    public void onDestroy(){
        MainActivity.msgLib.setConfigurationFlag = false;
        //Log.d("mytag", "false");
        super.onDestroy();
    }*/

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            confirmationListener
                    = (AppSettings.ConfirmationListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("mytag", "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}
