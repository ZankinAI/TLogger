package com.project.tlogger.msg.model;

public class TemperatureStatusModel {

    public Temperature temperature;

    public TemperatureStatusModel(){
        this.temperature = Temperature.Reset;
    }

    public enum Temperature{
        Reset,
        Unknown,
        Normal,
        Low,
        High,
        Both
    }
}
