package com.intrbiz.gerald.polyakov;

import com.codahale.metrics.Meter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("meter")
public class MeterReading extends Reading implements MeteredReading
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
    
    public MeterReading()
    {
        super();
    }
    
    public MeterReading(String name, Meter metric)
    {
        super(name);
        this.count = metric.getCount();
        this.meanRate = metric.getMeanRate();
        this.oneMinuteRate = metric.getOneMinuteRate();
        this.fiveMinuteRate = metric.getFiveMinuteRate();
        this.fifteenMinuteRate = metric.getFifteenMinuteRate();
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
}
