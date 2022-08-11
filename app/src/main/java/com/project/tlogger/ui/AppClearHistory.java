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
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;

import org.jetbrains.annotations.Nullable;

public class AppClearHistory extends DialogFragment implements View.OnClickListener{

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

    public interface ConfirmationListener {
        void confirmClear();
    }
    public AppClearHistory.ConfirmationListener confirmationListener;

    private static final String TAG = "DialogFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //MainActivity.msgLib.setConfigurationFlag = true;

        //Log.d("mytag", "true");

        View view = inflater.inflate(R.layout.app_clear_history, container, false);

        Bundle bundle = getArguments();
        view.findViewById(R.id.app_settings_button).setOnClickListener(this);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            confirmationListener
                    = (AppClearHistory.ConfirmationListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("mytag", "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }


}
