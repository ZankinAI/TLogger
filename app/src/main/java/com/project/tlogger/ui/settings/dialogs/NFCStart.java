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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.msg.model.Protocol;

import org.jetbrains.annotations.Nullable;

public class NFCStart extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "DialogFragment";



    EditText lower_range, upper_range;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        //Log.d("mytag", "true");
        super.onCreateView(inflater,container, savedInstanceState);

        View view = inflater.inflate(R.layout.nfc_start, container, false);

        Bundle bundle = getArguments();

        TextView textView = view.findViewById(R.id.nfc_start_text);


        Button btn = view.findViewById(R.id.nfc_start_button);
        if (bundle != null){

            if (bundle.getInt("from")==0){
                int error = bundle.getInt("");
                //String textToTextView = bundle.getString("");
                textView.setText(getTextFromError(error));
                btn.setText("OK");
            }
            if (bundle.getInt("from")==1){
                String bundleReset = bundle.getString("reset");
                textView.setText(bundleReset);;
                btn.setText(R.string.cancel_btn);
            }

            if (bundle.getInt("from")==2){
                int error = bundle.getInt("");
                if (error==Protocol.MSG_ERR.MSG_OK.getValue()) textView.setText(R.string.reset_isok);
                else {
                    textView.setText(getTextFromError(error));
                }
                btn.setText("OK");
            }


        }
        view.findViewById(R.id.nfc_start_button).setOnClickListener(this);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }


    private String getTextFromError(int error){
        String resultString = getResources().getString(R.string.msg_unknown_error);

        if (error == Protocol.MSG_ERR.MSG_OK.getValue())
            return getResources().getString(R.string.msg_set_config_isok) +" "
                    + String.valueOf(MainActivity.msgLib.cmdSetConfig.startDelay) + " " + getResources().getString(R.string.second);
        if (error == Protocol.MSG_ERR.MSG_ERR_UNKNOWN_COMMAND.getValue())
            return getResources().getString(R.string.msg_unknown_command);
        if (error == Protocol.MSG_ERR.MSG_ERR_NO_RESPONSE.getValue())
            return getResources().getString(R.string.msg_no_response);;
        if (error == Protocol.MSG_ERR.MSG_ERR_INVALID_COMMAND_SIZE.getValue())
            return getResources().getString(R.string.msg_wrong_command_size);
        if (error == Protocol.MSG_ERR.MSG_ERR_INVALID_PARAMETER.getValue())
            return getResources().getString(R.string.msg_invalid_parameter);
        if (error == Protocol.MSG_ERR.MSG_ERR_INVALID_PRECONDITION.getValue())
            return getResources().getString(R.string.msg_command_without_header);

        return resultString;
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
        MainActivity.msgLib.resetFlag = false;
        //Log.d("mytag", "false");
        super.onDestroy();
    }
}
