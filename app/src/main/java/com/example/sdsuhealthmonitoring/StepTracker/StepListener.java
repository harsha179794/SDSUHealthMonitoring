package com.example.sdsuhealthmonitoring.StepTracker;

// Will listen to step alerts
public interface StepListener {

    void step(long timeNs);

}
