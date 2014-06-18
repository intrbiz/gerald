package com.intrbiz.gerald.polyakov.transport.lamplighter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Counting;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
import com.codahale.metrics.Metric;
import com.codahale.metrics.Sampling;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.intrbiz.gerald.polyakov.Parcel;

public class LamplighterJSONEntity implements HttpEntity
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ssZ");

    private final Parcel parcel;
    
    private final JsonFactory factory = new JsonFactory();

    public LamplighterJSONEntity(Parcel parcel)
    {
        super();
        this.parcel = parcel;
    }

    @Override
    public boolean isRepeatable()
    {
        return true;
    }

    @Override
    public boolean isChunked()
    {
        return true;
    }

    @Override
    public long getContentLength()
    {
        return -1;
    }

    @Override
    public Header getContentType()
    {
        return new BasicHeader("Content-Type", "application/json");
    }

    @Override
    public Header getContentEncoding()
    {
        return null;
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException
    {
        return null;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException
    {
        JsonGenerator jg = this.factory.createGenerator(outstream);
        jg.setPrettyPrinter(new DefaultPrettyPrinter());
        jg.writeStartObject();
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.Parcel");
        // node
        jg.writeFieldName("node");
        jg.writeStartObject();
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.Node");
        // host id
        jg.writeFieldName("host-id");
        jg.writeString(this.parcel.getNode().getHostId().toString());
        // host name
        jg.writeFieldName("host-name");
        jg.writeString(this.parcel.getNode().getHostName());
        // service
        jg.writeFieldName("service");
        jg.writeString(this.parcel.getNode().getService());
        jg.writeEndObject();
        //
        jg.writeFieldName("time");
        jg.writeString(DATE_FORMAT.format(new Date()));
        //
        jg.writeFieldName("metrics");
        jg.writeStartArray();
        for (Entry<String, Metric> m : this.parcel.getMetrics().entrySet())
        {
            jg.writeStartObject();
            // the metric name
            jg.writeFieldName("name");
            jg.writeString(m.getKey());
            // the metric
            Metric mv = m.getValue();
            if (mv instanceof Gauge)
                this.writeGauge((Gauge<?>) mv, jg);
            else if (mv instanceof Counter)
                this.writeCounter((Counter) mv, jg);
            else if (mv instanceof Meter)
                this.writeMeter((Meter) mv, jg);
            else if (mv instanceof Timer)
                this.writeTimer((Timer) mv, jg);
            else if (mv instanceof Histogram) this.writeHistogram((Histogram) mv, jg);
            //
            jg.writeEndObject();
        }
        jg.writeEndArray();
        jg.writeEndObject();
        //
        jg.flush();
        // close?
        jg.close();
    }

    protected void writeCounter(Counter c, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(c.getCount());
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.CounterReading");
    }

    protected void writeMeter(Meter m, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(m.getCount());
        //
        jg.writeFieldName("mean-rate");
        jg.writeNumber(m.getMeanRate());
        //
        jg.writeFieldName("one-minute-rate");
        jg.writeNumber(m.getOneMinuteRate());
        //
        jg.writeFieldName("five-minute-rate");
        jg.writeNumber(m.getFiveMinuteRate());
        //
        jg.writeFieldName("fifteen-minute-rate");
        jg.writeNumber(m.getFifteenMinuteRate());
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.MeterReading");
    }

    protected void writeTimer(Timer t, JsonGenerator jg) throws IOException
    {
        this.writeMetered(t, jg);
        this.writeSampling(t, jg);
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.TimerReading");
    }
    
    protected void writeCounting(Counting m, JsonGenerator jg) throws IOException
    {
        jg.writeFieldName("count");
        jg.writeNumber(m.getCount());
    }
    
    protected void writeMetered(Metered m, JsonGenerator jg) throws IOException
    {
        this.writeCounting(m, jg);
        //
        jg.writeFieldName("mean-rate");
        jg.writeNumber(m.getMeanRate());
        //
        jg.writeFieldName("one-minute-rate");
        jg.writeNumber(m.getOneMinuteRate());
        //
        jg.writeFieldName("five-minute-rate");
        jg.writeNumber(m.getFiveMinuteRate());
        //
        jg.writeFieldName("fifteen-minute-rate");
        jg.writeNumber(m.getFifteenMinuteRate());
    }
    
    protected void writeSampling(Sampling m, JsonGenerator jg) throws IOException
    {
        Snapshot s = m.getSnapshot();
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
        //
        jg.writeFieldName("min");
        jg.writeNumber(s.getMin());
        //
        jg.writeFieldName("mean");
        jg.writeNumber(s.getMean());
        //
        jg.writeFieldName("max");
        jg.writeNumber(s.getMax());
        //
        jg.writeFieldName("std-dev");
        jg.writeNumber(s.getStdDev());
        //
        jg.writeEndObject();
    }

    protected void writeHistogram(Histogram h, JsonGenerator jg) throws IOException
    {
        this.writeCounting(h, jg);
        this.writeSampling(h, jg);
        //
        jg.writeFieldName("class");
        jg.writeString("com.intrbiz.lamplighter.model.HistogramReading");
    }

    protected void writeGauge(Gauge<?> g, JsonGenerator jg) throws IOException
    {
        Object val = g.getValue();
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

    @Override
    public boolean isStreaming()
    {
        return true;
    }

    @Override
    @Deprecated
    public void consumeContent() throws IOException
    {
    }
}
