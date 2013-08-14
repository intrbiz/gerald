package com.intrbiz.gerald.polyakov.packager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.intrbiz.gerald.polyakov.Packager;
import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.util.Option;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.stats.Snapshot;

public class JSONPackager implements Packager
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ssZ");
    
    private final JsonFactory factory;

    public JSONPackager()
    {
        this.factory = new JsonFactory();
    }

    @Override
    public void option(Option<String> option, String value)
    {
    }

    @Override
    public void option(Option<Integer> option, int value)
    {
    }

    @Override
    public void option(Option<Long> option, long value)
    {
    }

    @Override
    public void option(Option<Float> option, float value)
    {
    }

    @Override
    public void option(Option<Double> option, double value)
    {
    }

    @Override
    public void option(Option<Boolean> option, boolean value)
    {
    }

    @Override
    public Parcel packageMetrics(Map<MetricName, Metric> metrics)
    {
        Parcel p = new Parcel("json", "application/json");
        //
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
            try
            {
                JsonGenerator jg = this.factory.createGenerator(baos);
                jg.setPrettyPrinter(new DefaultPrettyPrinter());
                jg.writeStartObject();
                //
                jg.writeFieldName("class");
                jg.writeString("com.intrbiz.lamplighter.model.Parcel");
                //
                jg.writeFieldName("time");
                jg.writeString(DATE_FORMAT.format(new Date()));                
                //
                jg.writeFieldName("metrics");
                jg.writeStartArray();
                //
                for (Entry<MetricName, Metric> m : metrics.entrySet())
                {
                    jg.writeStartObject();
                    // the metric name
                    jg.writeFieldName("name");
                    jg.writeString(m.getKey().getGroup() + "." + m.getKey().getType() + "." + m.getKey().getName());
                    // the metric scope
                    jg.writeFieldName("scope");
                    jg.writeString(m.getKey().getScope());
                    //
                    Metric mv = m.getValue();
                    if (mv instanceof Gauge) this.writeGauge((Gauge<?>) mv, jg);
                    else if (mv instanceof Counter) this.writeCounter((Counter) mv, jg);
                    else if (mv instanceof Meter) this.writeMeter((Meter) mv, jg);
                    else if (mv instanceof Timer) this.writeTimer((Timer) mv, jg);
                    else if (mv instanceof Histogram) this.writeHistogram((Histogram) mv, jg);
                    //
                    jg.writeEndObject();
                }
                //
                jg.writeEndArray();
                //
                jg.writeEndObject();
                jg.close();
            }
            finally
            {
                baos.close();
            }
            p.buffer(ByteBuffer.wrap(baos.toByteArray()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return p;
    }

    protected void writeCounter(Counter c, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(c.count());
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.CounterReading");
    }
    
    protected void writeMeter(Meter m, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(m.count());
        //
        jg.writeFieldName("mean-rate");
        jg.writeNumber(m.meanRate());
        //
        jg.writeFieldName("one-minute-rate");
        jg.writeNumber(m.oneMinuteRate());
        //
        jg.writeFieldName("five-minute-rate");
        jg.writeNumber(m.fiveMinuteRate());
        //
        jg.writeFieldName("fifteen-minute-rate");
        jg.writeNumber(m.fifteenMinuteRate());
        //
        jg.writeFieldName("rate-unit");
        jg.writeString(m.rateUnit().toString());
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.MeterReading");
    }
    
    protected void writeTimer(Timer t, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(t.count());
        //
        jg.writeFieldName("min");
        jg.writeNumber(t.min());
        //
        jg.writeFieldName("mean");
        jg.writeNumber(t.mean());
        //
        jg.writeFieldName("max");
        jg.writeNumber(t.max());
        //
        jg.writeFieldName("sum");
        jg.writeNumber(t.sum());
        //
        jg.writeFieldName("std-dev");
        jg.writeNumber(t.stdDev());
        //
        jg.writeFieldName("duration-unit");
        jg.writeString(t.durationUnit().toString());
        //
        jg.writeFieldName("event-type");
        jg.writeString(t.eventType());
        //
        jg.writeFieldName("mean-rate");
        jg.writeNumber(t.meanRate());
        //
        jg.writeFieldName("one-minute-rate");
        jg.writeNumber(t.oneMinuteRate());
        //
        jg.writeFieldName("five-minute-rate");
        jg.writeNumber(t.fiveMinuteRate());
        //
        jg.writeFieldName("fifteen-minute-rate");
        jg.writeNumber(t.fifteenMinuteRate());
        //
        jg.writeFieldName("rate-unit");
        jg.writeString(t.rateUnit().toString());
        //
        Snapshot s = t.getSnapshot();
        jg.writeFieldName("snapshot");
        jg.writeStartObject();
        //
        jg.writeFieldName("percentile-75");
        jg.writeNumber(s.get75thPercentile());
        //
        jg.writeFieldName("percentile-95");
        jg.writeNumber(s.get95thPercentile());
        //
        jg.writeFieldName("percentile-98");
        jg.writeNumber(s.get98thPercentile());
        //
        jg.writeFieldName("percentile-99");
        jg.writeNumber(s.get99thPercentile());
        //
        jg.writeFieldName("percentile-999");
        jg.writeNumber(s.get999thPercentile());
        //
        jg.writeFieldName("size");
        jg.writeNumber(s.size());
        //
        jg.writeFieldName("median");
        jg.writeNumber(s.getMedian());
        jg.writeEndObject();
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.TimerReading");
    }
    
    protected void writeHistogram(Histogram h, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(h.count());
        //
        jg.writeFieldName("min");
        jg.writeNumber(h.min());
        //
        jg.writeFieldName("mean");
        jg.writeNumber(h.mean());
        //
        jg.writeFieldName("max");
        jg.writeNumber(h.max());
        //
        jg.writeFieldName("sum");
        jg.writeNumber(h.sum());
        //
        jg.writeFieldName("std-dev");
        jg.writeNumber(h.stdDev());
        //
        Snapshot s = h.getSnapshot();
        jg.writeFieldName("snapshot");
        jg.writeStartObject();
        //
        jg.writeFieldName("percentile-75");
        jg.writeNumber(s.get75thPercentile());
        //
        jg.writeFieldName("percentile-95");
        jg.writeNumber(s.get95thPercentile());
        //
        jg.writeFieldName("percentile-98");
        jg.writeNumber(s.get98thPercentile());
        //
        jg.writeFieldName("percentile-99");
        jg.writeNumber(s.get99thPercentile());
        //
        jg.writeFieldName("percentile-999");
        jg.writeNumber(s.get999thPercentile());
        //
        jg.writeFieldName("size");
        jg.writeNumber(s.size());
        //
        jg.writeFieldName("median");
        jg.writeNumber(s.getMedian());
        jg.writeEndObject();
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.HistogramReading");
    }
    
    protected void writeGauge(Gauge<?> g, JsonGenerator jg) throws IOException
    {
        Object val = g.value();
        jg.writeFieldName("value");
        if (val instanceof String)
        {
            jg.writeString((String) val);
            //
            jg.writeFieldName("value-type");
            jg.writeString("String");
        }
        else if (val instanceof Integer)
        {
            jg.writeNumber((int) val);
            //
            jg.writeFieldName("value-type");
            jg.writeString("Integer");
        }
        else if (val instanceof Long)
        {
            jg.writeNumber((long) val);
            //
            jg.writeFieldName("value-type");
            jg.writeString("Long");
        }
        else if (val instanceof Float)
        {
            jg.writeNumber((float) val);
            //
            jg.writeFieldName("value-type");
            jg.writeString("Float");
        }
        else if (val instanceof Double)
        {
            jg.writeNumber((double) val);
            //
            jg.writeFieldName("value-type");
            jg.writeString("Double");
        }
        else if (val instanceof Boolean)
        {
            jg.writeBoolean((boolean) val);
            //
            jg.writeFieldName("value-type");
            jg.writeString("Boolean");
        }
        else
        {
            jg.writeNull();
            //
            jg.writeFieldName("value-type");
            jg.writeNull();
        }
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.GaugeReading");
    }
}
