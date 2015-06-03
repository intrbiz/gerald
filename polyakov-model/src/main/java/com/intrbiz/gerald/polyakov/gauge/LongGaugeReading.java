package com.intrbiz.gerald.polyakov.gauge;

import com.codahale.metrics.Gauge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intrbiz.gerald.polyakov.ContinuousGaugeReading;
import com.intrbiz.gerald.polyakov.Reading;

@JsonTypeName("long-gauge")
public class LongGaugeReading extends Reading implements ContinuousGaugeReading<Long>
{
    @JsonProperty("value")
    private Long value;
    
    @JsonProperty("warning")
    private Long warning;
    
    @JsonProperty("critical")
    private Long critical;
    
    @JsonProperty("min")
    private Long min;
    
    @JsonProperty("max")
    private Long max;
    
    public LongGaugeReading()
    {
        super();
    }
    
    public LongGaugeReading(String name, Gauge<Long> metric)
    {
        super(name);
        this.value = metric.getValue();
    }

    @Override
    public Long getValue()
    {
        return value;
    }

    @Override
    public void setValue(Long value)
    {
        this.value = value;
    }
    
    @Override
    public Long getWarning()
    {
        return warning;
    }

    @Override
    public void setWarning(Long warning)
    {
        this.warning = warning;
    }

    @Override
    public Long getCritical()
    {
        return critical;
    }

    @Override
    public void setCritical(Long critical)
    {
        this.critical = critical;
    }

    @Override
    public Long getMin()
    {
        return min;
    }

    @Override
    public void setMin(Long min)
    {
        this.min = min;
    }

    @Override
    public Long getMax()
    {
        return max;
    }

    @Override
    public void setMax(Long max)
    {
        this.max = max;
    }
}
