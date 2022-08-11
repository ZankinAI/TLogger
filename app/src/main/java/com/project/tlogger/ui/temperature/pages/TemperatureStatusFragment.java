package com.project.tlogger.ui.temperature.pages;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.tlogger.MainActivity;
import com.project.tlogger.R;
import com.project.tlogger.msg.Lib;
import com.project.tlogger.msg.model.MeasurementStatusModel;
import com.project.tlogger.msg.model.TemperatureStatusModel;
import com.project.tlogger.msg.model.Utils;

public class TemperatureStatusFragment extends Fragment {

    private TemperatureStatusViewModel mViewModel;
    private  Lib _msgLib;

    private int statusIcon;
    private String statusText;
    private String textStatus;
    private String apiVersion;
    private String mimeType;


    private static  String STATUS_LABEL_MESSAGE_NOT_SUPPORTED =
            "Firmware not supported. Please upgrade and try again";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_DISABLED =
            "Invalid NDEF message";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_PRISTINE_STATE =
            "In Pristine State";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_RECORDING =
            "The tag is configured and is logging.";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_STOPPED_BATTERY_DIED =
            "Stopped as battery died";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_STOPPED_STORAGE_FULL =
            "Stopped as storage is full";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_STOPPED_DEMO_EXPIRED =
            "Stopped as configured running time has expired";
    private static  String STATUS_LABEL_MESSAGE_DEVICE_UNSUPPORTED =
            "Unsupported tag, please tap an NTAG SmartSensor.";
    private static  String STATUS_APP_MSG_EVENT_EXPIRED =
            "Logging was stopped after the configured running time.";
    private static  String STATUS_APP_MSG_EVENT_FULL =
            "Logging has stopped because no more free space is available to store samples.";
    private static  String STATUS_APP_MSG_EVENT_BOD =
            "Battery is (nearly) depleted.";
    private static  String STATUS_APP_MSG_EVENT_TEMPERATURE_TOO_LOW =
            "At least one temperature value was lower than the valid minimum value.";
    private static  String STATUS_APP_MSG_EVENT_TEMPERATURE_TOO_HIGH =
            "At least one temperature value was higher than the valid maximum value.";
    private static  String STATUS_APP_MSG_EVENT_STOPPED =
            "The tag is configured and has been logging. Now it has stopped logging.";
    private static  String STATUS_APP_MSG_EVENT_LOGGING =
            "The tag is configured and is logging. At least one sample is available.";
    private static  String STATUS_APP_MSG_EVENT_STARTING =
            "The tag is configured and will make a first measurement after the configured delay.";
    private static  String STATUS_APP_MSG_EVENT_CONFIGURED =
            "The tag is configured, but requires an external trigger to start measuring.";
    private static  String STATUS_APP_MSG_EVENT_PRISTINE =
            "The tag is not yet configured and contains no data.";


    public static TemperatureStatusFragment newInstance() {
        return new TemperatureStatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        _msgLib = MainActivity.msgLib;

        STATUS_LABEL_MESSAGE_NOT_SUPPORTED = getResources().getString(R.string.status_label_message_not_supported);
        STATUS_APP_MSG_EVENT_EXPIRED = getResources().getString(R.string.status_app_msg_event_expired);
        STATUS_APP_MSG_EVENT_FULL = getResources().getString(R.string.status_app_msg_event_full);
        STATUS_APP_MSG_EVENT_BOD = getResources().getString(R.string.status_app_msg_event_bod);
        STATUS_APP_MSG_EVENT_TEMPERATURE_TOO_LOW = getResources().getString(R.string.status_app_msg_event_temperature_too_low);
        STATUS_APP_MSG_EVENT_TEMPERATURE_TOO_HIGH = getResources().getString(R.string.status_app_msg_event_temperature_too_high);
        STATUS_APP_MSG_EVENT_STOPPED = getResources().getString(R.string.status_app_msg_event_stopped);
        STATUS_APP_MSG_EVENT_LOGGING = getResources().getString(R.string.status_app_msg_event_logging);
        STATUS_APP_MSG_EVENT_STARTING = getResources().getString(R.string.status_app_msg_event_starting);
        STATUS_APP_MSG_EVENT_CONFIGURED = getResources().getString(R.string.status_app_msg_event_configured);
        STATUS_APP_MSG_EVENT_PRISTINE = getResources().getString(R.string.status_app_msg_event_pristine);

        statusIcon = R.drawable.logo_bordered;
        createDataFragment();
        View view = inflater.inflate(R.layout.temperature_status_fragment_adaptive, container, false);
        TextView textViewInfo = view.findViewById(R.id.temp_info);
        if ((_msgLib.language == 1)&&(textStatus!=null))
            textStatus = Utils.translateStatus(textStatus);
        textViewInfo.setText(textStatus);

        /*TextView textViewApiVersion = view.findViewById(R.id.system_info);
        textViewApiVersion.setText(apiVersion);

        TextView textViewMimeType = view.findViewById(R.id.mime_type);
        textViewMimeType.setText(_msgLib.mimeType);*/

        TextView textViewStatus = view.findViewById(R.id.temp_status);
        ImageView imageViewStatus = view.findViewById(R.id.temp_status_image);
        //if (_msgLib.flagTloggerConnected) createStatus();

        imageViewStatus.setImageResource(statusIcon);
        textViewStatus.setText(statusText);

         ImageView imagePages = view.findViewById(R.id.page);
        imagePages.setVisibility(View.VISIBLE);

        if ((MainActivity.msgLib.flagUnknownMessage)&& (!MainActivity.msgLib.flagOpenFragmentFromHistory))
            imagePages.setVisibility(View.INVISIBLE);

        if (((MainActivity.msgLib.flagTloggerConnected)||(MainActivity.msgLib.flagOpenFragmentFromHistory))==false)
            imagePages.setVisibility(View.INVISIBLE);




        return view;
    }

    private void createDataFragment(){

        if (_msgLib.flagOpenFragmentFromHistory){
            textStatus = _msgLib.selectedStoreData.textStatus;
            apiVersion = _msgLib.selectedStoreData.apiVersion;
            Object[] parsedStatus = Utils.parsingEvent(_msgLib.selectedStoreData.responseConfigData.status);
            MeasurementStatusModel.Measurement measurementStatus = (MeasurementStatusModel.Measurement) parsedStatus[0];
            MeasurementStatusModel.Failure failureStatus = (MeasurementStatusModel.Failure) parsedStatus[1];
            TemperatureStatusModel.Temperature temperatureStatus = (TemperatureStatusModel.Temperature) parsedStatus[2];
            createStatus(measurementStatus, failureStatus, temperatureStatus);
        }
        else if (_msgLib.flagTloggerConnected) {

            if (_msgLib.flagUnknownMessage){
                MeasurementStatusModel.Measurement measurementStatus = MeasurementStatusModel.Measurement.Unknown;
                MeasurementStatusModel.Failure failureStatus  = MeasurementStatusModel.Failure.Unknown;
                TemperatureStatusModel.Temperature temperatureStatus = TemperatureStatusModel.Temperature.Unknown;
                createStatus(measurementStatus, failureStatus, temperatureStatus);
                return;

            }

            textStatus = _msgLib.storeData.textStatus;
            apiVersion = _msgLib.storeData.apiVersion;


            if (_msgLib.storeData.responseConfigData==null){
                MeasurementStatusModel.Measurement measurementStatus = MeasurementStatusModel.Measurement.Unknown;
                MeasurementStatusModel.Failure failureStatus  = MeasurementStatusModel.Failure.Unknown;
                TemperatureStatusModel.Temperature temperatureStatus = TemperatureStatusModel.Temperature.Unknown;
                createStatus(measurementStatus, failureStatus, temperatureStatus);
                _msgLib.flagUnknownMessage=true;
                return;

            }

            Object[] parsedStatus = Utils.parsingEvent(_msgLib.storeData.responseConfigData.status);
            MeasurementStatusModel.Measurement measurementStatus = (MeasurementStatusModel.Measurement) parsedStatus[0];
            MeasurementStatusModel.Failure failureStatus = (MeasurementStatusModel.Failure) parsedStatus[1];
            TemperatureStatusModel.Temperature temperatureStatus = (TemperatureStatusModel.Temperature) parsedStatus[2];
            createStatus(measurementStatus, failureStatus, temperatureStatus);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemperatureStatusViewModel.class);
        // TODO: Use the ViewModel
    }

    private void createStatus(MeasurementStatusModel.Measurement measurementStatus, MeasurementStatusModel.Failure failureStatus,
                              TemperatureStatusModel.Temperature temperatureStatus ){
        MeasurementStatusModel.Measurement status = measurementStatus;
        switch (status){
            case Reset:

            case Unknown:
                statusText = STATUS_LABEL_MESSAGE_NOT_SUPPORTED;
                statusIcon = R.drawable.logo_unknown_state;
                break;
            case NotConfigured:
                statusText = STATUS_APP_MSG_EVENT_PRISTINE;
                statusIcon = R.drawable.logo_pristine_state;
                break;
            case Starting:
                statusText = STATUS_APP_MSG_EVENT_STARTING;
                statusIcon = R.drawable.logo_starting_state;
                break;
            case Configured:
                statusText = STATUS_APP_MSG_EVENT_CONFIGURED;
                statusIcon = R.drawable.logo_settings;
                break;
            case Logging:
                if (temperatureStatus == TemperatureStatusModel.Temperature.Low){
                    statusText = STATUS_APP_MSG_EVENT_TEMPERATURE_TOO_LOW;
                    statusIcon = R.drawable.logo_low_temp;
                }
                else if (temperatureStatus == TemperatureStatusModel.Temperature.High){
                    statusText = STATUS_APP_MSG_EVENT_TEMPERATURE_TOO_HIGH;
                    statusIcon = R.drawable.logo_high_temp;
                }
                else {
                    statusText = STATUS_APP_MSG_EVENT_LOGGING;
                    statusIcon = R.drawable.logo_logging_state;
                }
                break;
            case Stopped:
                switch (failureStatus){
                    case NoFailure:
                        statusText = STATUS_APP_MSG_EVENT_STOPPED;
                        statusIcon = R.drawable.logo_stopped_state;
                        break;
                    case Bod:
                        statusText = STATUS_APP_MSG_EVENT_BOD;
                        statusIcon = R.drawable.logo_low_bat;
                        break;
                    case Full:
                        statusText = STATUS_APP_MSG_EVENT_FULL;
                        statusIcon = R.drawable.logo_no_space;
                        break;
                    case Expired:
                        statusText = STATUS_APP_MSG_EVENT_EXPIRED;
                        statusIcon = R.drawable.logo_stopped_expired;
                        break;
                }
                break;


        }
    }

}