package com.intrbiz.gerald.polyakov;


public interface MeteredReading extends CountingReading
{
    double getMeanRate();

    void setMeanRate(double meanRate);

    double getOneMinuteRate();

    void setOneMinuteRate(double oneMinuteRate);

    double getFiveMinuteRate();

    void setFiveMinuteRate(double fiveMinuteRate);

    double getFifteenMinuteRate();

    void setFifteenMinuteRate(double fifteenMinuteRate);
}
