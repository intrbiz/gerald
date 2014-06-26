package com.intrbiz.gerald.polyakov;

import com.codahale.metrics.Snapshot;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonTypeName("snapshot")
public class ReadingSnapshot
{
    @JsonProperty("size")
    private int size;

    @JsonProperty("median")
    private double median;

    @JsonProperty("mean")
    private double mean;

    @JsonProperty("min")
    private double min;

    @JsonProperty("max")
    private double max;

    @JsonProperty("std_dev")
    private double stdDev;

    @JsonProperty("the_75th_percentile")
    private double the75thPercentile;

    @JsonProperty("the_95th_percentile")
    private double the95thPercentile;

    @JsonProperty("the_98th_percentile")
    private double the98thPercentile;

    @JsonProperty("the_99th_percentile")
    private double the99thPercentile;

    @JsonProperty("the_999th_percentile")
    private double the999thPercentile;

    public ReadingSnapshot()
    {
        super();
    }

    public ReadingSnapshot(Snapshot snapshot)
    {
        super();
        this.size = snapshot.size();
        this.median = snapshot.getMedian();
        this.mean = snapshot.getMean();
        this.min = snapshot.getMin();
        this.max = snapshot.getMax();
        this.stdDev = snapshot.getStdDev();
        this.the75thPercentile = snapshot.get75thPercentile();
        this.the95thPercentile = snapshot.get95thPercentile();
        this.the98thPercentile = snapshot.get98thPercentile();
        this.the99thPercentile = snapshot.get99thPercentile();
        this.the999thPercentile = snapshot.get999thPercentile();
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public double getMedian()
    {
        return median;
    }

    public void setMedian(double median)
    {
        this.median = median;
    }

    public double getMean()
    {
        return mean;
    }

    public void setMean(double mean)
    {
        this.mean = mean;
    }

    public double getMin()
    {
        return min;
    }

    public void setMin(double min)
    {
        this.min = min;
    }

    public double getMax()
    {
        return max;
    }

    public void setMax(double max)
    {
        this.max = max;
    }

    public double getStdDev()
    {
        return stdDev;
    }

    public void setStdDev(double stdDev)
    {
        this.stdDev = stdDev;
    }

    public double getThe75thPercentile()
    {
        return the75thPercentile;
    }

    public void setThe75thPercentile(double the75thPercentile)
    {
        this.the75thPercentile = the75thPercentile;
    }

    public double getThe95thPercentile()
    {
        return the95thPercentile;
    }

    public void setThe95thPercentile(double the95thPercentile)
    {
        this.the95thPercentile = the95thPercentile;
    }

    public double getThe98thPercentile()
    {
        return the98thPercentile;
    }

    public void setThe98thPercentile(double the98thPercentile)
    {
        this.the98thPercentile = the98thPercentile;
    }

    public double getThe99thPercentile()
    {
        return the99thPercentile;
    }

    public void setThe99thPercentile(double the99thPercentile)
    {
        this.the99thPercentile = the99thPercentile;
    }

    public double getThe999thPercentile()
    {
        return the999thPercentile;
    }

    public void setThe999thPercentile(double the999thPercentile)
    {
        this.the999thPercentile = the999thPercentile;
    }
}
