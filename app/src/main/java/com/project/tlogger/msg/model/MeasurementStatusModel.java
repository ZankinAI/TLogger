package com.project.tlogger.msg.model;

public class MeasurementStatusModel {

    public Measurement measurement;
    public Failure failure;


    public MeasurementStatusModel(){
        this.measurement = Measurement.Reset;
        this.failure = Failure.Reset;
    }
    public enum Measurement{
        Reset,
        Unknown,
        NotConfigured,
        Starting,
        Configured,
        Logging,
        Stopped
    }

    public enum Failure{
        Reset,
        Unknown,
        NoFailure,
        Bod,
        Full,
        Expired
    }
}
