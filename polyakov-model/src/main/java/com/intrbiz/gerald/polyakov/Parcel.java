package com.intrbiz.gerald.polyakov;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intrbiz.gerald.polyakov.gauge.BooleanGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.DoubleGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.FloatGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.IntegerGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.LongGaugeReading;
import com.intrbiz.gerald.polyakov.gauge.StringGaugeReading;

/**
 * A parcel of readings from a source
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonTypeName("parcel")
public class Parcel
{
    @JsonProperty("node")
    private Node node;

    @JsonProperty("source")
    private String source;

    @JsonProperty("captured")
    private Date captured;

    @JsonProperty("readings")
    private List<Reading> readings = new LinkedList<Reading>();

    public Parcel()
    {
        super();
    }

    public Parcel(Node node, String source)
    {
        this();
        this.node = node;
        this.source = source;
        this.captured = new Date();
    }
    
    public Parcel(Node node, String source, MetricRegistry registry)
    {
        this(node, source);
        // read the metrics
        for (Entry<String, Metric> metric : registry.getMetrics().entrySet())
        {
            this.addMetric(metric.getKey(), metric.getValue());
        }
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public Node getNode()
    {
        return node;
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    public Date getCaptured()
    {
        return captured;
    }

    public void setCaptured(Date captured)
    {
        this.captured = captured;
    }

    public List<Reading> getReadings()
    {
        return readings;
    }

    public void setReadings(List<Reading> readings)
    {
        this.readings = readings;
    }

    public void addReading(Reading reading)
    {
        this.readings.add(reading);
    }
    
    @SuppressWarnings("unchecked")
    public void addMetric(String name, Metric metric)
    {
        if (metric instanceof Counter)
        {
            this.addReading(new CounterReading(name, (Counter) metric));
        }
        else if (metric instanceof Meter)
        {
            this.addReading(new MeterReading(name, (Meter) metric));
        }
        else if (metric instanceof Timer)
        {
            this.addReading(new TimerReading(name, (Timer) metric));
        }
        else if (metric instanceof Histogram)
        {
            this.addReading(new HistogramReading(name, (Histogram) metric));
        }
        else if (metric instanceof Gauge)
        {
            Object value = ((Gauge<?>) metric).getValue();
            if (value instanceof String)
            {
                this.addReading(new StringGaugeReading(name, (Gauge<String>) metric));
            }
            else if (value instanceof Long)
            {
                this.addReading(new LongGaugeReading(name, (Gauge<Long>) metric));
            }
            else if (value instanceof Integer)
            {
                this.addReading(new IntegerGaugeReading(name, (Gauge<Integer>) metric));
            }
            else if (value instanceof Double)
            {
                this.addReading(new DoubleGaugeReading(name, (Gauge<Double>) metric));
            }
            else if (value instanceof Float)
            {
                this.addReading(new FloatGaugeReading(name, (Gauge<Float>) metric));
            }
            else if (value instanceof Boolean)
            {
                this.addReading(new BooleanGaugeReading(name, (Gauge<Boolean>) metric));
            }
        }
    }
    
    //
    
    public static String encode(Parcel parcel)
    {
        try
        {
            ObjectMapper factory = new ObjectMapper();
            //
            factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            factory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            factory.configure(SerializationFeature.INDENT_OUTPUT, true);
            //
            factory.registerSubtypes(
                Node.class,
                Parcel.class,
                CounterReading.class,
                MeterReading.class,
                TimerReading.class,
                HistogramReading.class,
                StringGaugeReading.class,
                LongGaugeReading.class,
                IntegerGaugeReading.class,
                DoubleGaugeReading.class,
                FloatGaugeReading.class,
                BooleanGaugeReading.class
            );
            //
            return factory.writeValueAsString(parcel);
        }
        catch (Exception e)
        {
        }
        return null;
    }
}
