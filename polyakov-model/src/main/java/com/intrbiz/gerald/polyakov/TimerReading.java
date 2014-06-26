package com.intrbiz.gerald.polyakov;

import com.codahale.metrics.Timer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("timer")
public class TimerReading extends Reading implements MeteredReading, SamplingReading
{
    @JsonProperty("count")
    private long count;
    
    @JsonProperty("mean-rate")
    private double meanRate;

    @JsonProperty("one-minute-rate")
    private double oneMinuteRate;

    @JsonProperty("five-minute-rate")
    private double fiveMinuteRate;
    
    @JsonProperty("fifteen-minute-rate")
    private double fifteenMinuteRate;
    
    @JsonProperty("snapshot")
    private ReadingSnapshot snapshot;
    
    public TimerReading()
    {
        super();
    }
    
    public TimerReading(String name, Timer metric)
    {
        super(name);
        this.count = metric.getCount();
        this.meanRate = metric.getMeanRate();
        this.oneMinuteRate = metric.getOneMinuteRate();
        this.fiveMinuteRate = metric.getFiveMinuteRate();
        this.fifteenMinuteRate = metric.getFifteenMinuteRate();
        this.snapshot = new ReadingSnapshot(metric.getSnapshot());
        
    }

    @Override
    public long getCount()
    {
        return count;
    }

    @Override
    public void setCount(long count)
    {
        this.count = count;
    }

    @Override
    public double getMeanRate()
    {
        return meanRate;
    }

    @Override
    public void setMeanRate(double meanRate)
    {
        this.meanRate = meanRate;
    }

    @Override
    public double getOneMinuteRate()
    {
        return oneMinuteRate;
    }

    @Override
    public void setOneMinuteRate(double oneMinuteRate)
    {
        this.oneMinuteRate = oneMinuteRate;
    }

    @Override
    public double getFiveMinuteRate()
    {
        return fiveMinuteRate;
    }

    @Override
    public void setFiveMinuteRate(double fiveMinuteRate)
    {
        this.fiveMinuteRate = fiveMinuteRate;
    }

    @Override
    public double getFifteenMinuteRate()
    {
        return fifteenMinuteRate;
    }

    @Override
    public void setFifteenMinuteRate(double fifteenMinuteRate)
    {
        this.fifteenMinuteRate = fifteenMinuteRate;
    }

    @Override
    public ReadingSnapshot getSnapshot()
    {
        return snapshot;
    }

    @Override
    public void setSnapshot(ReadingSnapshot snapshot)
    {
        this.snapshot = snapshot;
    }
}
