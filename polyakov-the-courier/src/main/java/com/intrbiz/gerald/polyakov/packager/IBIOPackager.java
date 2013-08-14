package com.intrbiz.gerald.polyakov.packager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import com.intrbiz.gerald.polyakov.Packager;
import com.intrbiz.gerald.polyakov.Parcel;
import com.intrbiz.ibio.IBIO;
import com.intrbiz.ibio.IBIO.FieldMeta;
import com.intrbiz.ibio.IBIO.TypeMeta;
import com.intrbiz.ibio.IBIOOutputStream;
import com.intrbiz.util.Option;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.stats.Snapshot;

public class IBIOPackager implements Packager
{   
    public IBIOPackager()
    {
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
    
    private static final TypeMeta TYPE_PARCEL = new TypeMeta("parcel", 1, 
            new FieldMeta("time", 1, IBIO.TypeCodes.INT64),
            new FieldMeta("metrics", 2, IBIO.TypeCodes.LIST)
    );

    @Override
    public Parcel packageMetrics(Map<MetricName, Metric> metrics)
    {
        Parcel p = new Parcel("ibioz", "application/x-ibioz");
        //
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
            try
            {
                IBIOOutputStream out = new IBIOOutputStream(new GZIPOutputStream(baos));
                // the stream header
                out.writeHeader();
                // start the parcel object
                out.writeObjectStart(TYPE_PARCEL);
                // the time
                out.writeObjectField(1);
                out.writeInt64(System.currentTimeMillis());
                out.writeObjectSep();
                // the metrics list
                out.writeObjectField(2);
                out.writeListStart(metrics.size());
                //
                boolean ns = false;
                for (Entry<MetricName, Metric> m : metrics.entrySet())
                {
                    if (ns) out.writeListSep();
                    //
                    if (m.getValue() instanceof Gauge)
                    {
                        this.writeGauge(m.getKey(), (Gauge<?>) m.getValue(), out);
                    }
                    else if (m.getValue() instanceof Counter)
                    {
                        this.writeCounter(m.getKey(), (Counter) m.getValue(), out);
                    }
                    else if (m.getValue() instanceof Meter)
                    {
                        this.writeMeter(m.getKey(), (Meter) m.getValue(), out); 
                    }
                    else if (m.getValue() instanceof Timer)
                    {
                        this.writeTimer(m.getKey(), (Timer) m.getValue(), out);
                    }
                    else if (m.getValue() instanceof Histogram)
                    {
                        this.writeHistogram(m.getKey(), (Histogram) m.getValue(), out);
                    }
                    else
                    {
                        out.writeNull();
                    }
                    //
                    ns = true;
                }
                out.writeListEnd();
                out.writeObjectEnd();
                //
                out.close();
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
        //
        return p;
    }
    
    private void writeReadingName(MetricName name, IBIOOutputStream out) throws IOException
    {
        // name
        out.writeObjectField(1);
        out.writeString(name.getGroup() + "." + name.getType() + "." + name.getName());
        out.writeObjectSep();
        // scope
        out.writeObjectField(2);
        if (name.getScope() == null)
        {
            out.writeNull();
        }
        else
        {
            out.writeString(name.getScope());
        }
    }
    
    private static final TypeMeta TYPE_GAUGE = new TypeMeta("gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.NULL)
    );
    
    private static final TypeMeta TYPE_GAUGE_STRING = new TypeMeta("string-gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.STRING)
    );
    
    private static final TypeMeta TYPE_GAUGE_INT = new TypeMeta("int-gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.INT32)
    );
    
    private static final TypeMeta TYPE_GAUGE_LONG = new TypeMeta("long-gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.INT64)
    );
    
    private static final TypeMeta TYPE_GAUGE_FLOAT = new TypeMeta("float-gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.REAL)
    );
    
    private static final TypeMeta TYPE_GAUGE_DOUBLE = new TypeMeta("double-gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.DP_REAL)
    );
    
    private static final TypeMeta TYPE_GAUGE_BOOLEAN = new TypeMeta("boolean-gauge", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("value", 3, IBIO.TypeCodes.BOOL)
    );
    
    protected void writeGauge(MetricName name, Gauge<?> g, IBIOOutputStream out) throws IOException
    {
        Object val = g.value();
        if (val instanceof String)
        {
            out.writeObjectStart(TYPE_GAUGE_STRING);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeString((String) val);
            out.writeObjectEnd();
        }
        else if (val instanceof Integer)
        {
            out.writeObjectStart(TYPE_GAUGE_INT);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeInt32((Integer) val);
            out.writeObjectEnd();
        }
        else if (val instanceof Long)
        {
            out.writeObjectStart(TYPE_GAUGE_LONG);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeInt64((Long) val);
            out.writeObjectEnd();
        }
        else if (val instanceof Float)
        {
            out.writeObjectStart(TYPE_GAUGE_FLOAT);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeFloat((Float) val);
            out.writeObjectEnd();
        }
        else if (val instanceof Double)
        {
            out.writeObjectStart(TYPE_GAUGE_DOUBLE);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeDouble((Double) val);
            out.writeObjectEnd();
        }
        else if (val instanceof Boolean)
        {
            out.writeObjectStart(TYPE_GAUGE_BOOLEAN);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeBool((Boolean) val);
            out.writeObjectEnd();
        }
        else
        {
            out.writeObjectStart(TYPE_GAUGE);
            this.writeReadingName(name, out);
            out.writeObjectSep();
            out.writeObjectField(3);
            out.writeNull();
            out.writeObjectEnd();
        }
    }
    
    private static final TypeMeta TYPE_COUNTER = new TypeMeta("counter", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("count", 3, IBIO.TypeCodes.INT64)
    );
    
    protected void writeCounter(MetricName name, Counter c, IBIOOutputStream out) throws IOException
    {
        out.writeObjectStart(TYPE_COUNTER);
        this.writeReadingName(name, out);
        out.writeObjectSep();
        out.writeObjectField(3);
        out.writeInt64(c.count());
        out.writeObjectEnd();
    }
    
    private static final TypeMeta TYPE_METER = new TypeMeta("meter", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("count", 3, IBIO.TypeCodes.INT64),
            new FieldMeta("mean-rate", 4, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("one-minute-rate", 5, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("five-minute-rate", 6, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("fifteen-minute-rate", 7, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("rate-unit", 8, IBIO.TypeCodes.STRING)
    );
    
    protected void writeMeter(MetricName name, Meter m, IBIOOutputStream out) throws IOException
    {
        out.writeObjectStart(TYPE_METER);
        //
        this.writeReadingName(name, out);
        // count
        out.writeObjectSep();
        out.writeObjectField(3);
        out.writeInt64(m.count());
        // mean rate
        out.writeObjectSep();
        out.writeObjectField(4);
        out.writeDouble(m.meanRate());
        // 1 min rate
        out.writeObjectSep();
        out.writeObjectField(5);
        out.writeDouble(m.oneMinuteRate());
        // 5 min rate
        out.writeObjectSep();
        out.writeObjectField(6);
        out.writeDouble(m.fiveMinuteRate());
        // 15 min rate
        out.writeObjectSep();
        out.writeObjectField(7);
        out.writeDouble(m.fifteenMinuteRate());
        // rate unit
        out.writeObjectSep();
        out.writeObjectField(8);
        out.writeString(m.rateUnit().toString());
        //
        out.writeObjectEnd();
    }
    
    private static final TypeMeta TYPE_SNAPSHOT = new TypeMeta("snapshot", 1, 
            new FieldMeta("percentile-75", 1, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("percentile-95", 2, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("percentile-98", 3, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("percentile-99", 4, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("percentile-999", 5, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("size", 6, IBIO.TypeCodes.INT32),
            new FieldMeta("median", 7, IBIO.TypeCodes.DP_REAL)
    );
    
    protected void writeSnapshot(Snapshot s, IBIOOutputStream out) throws IOException
    {
        out.writeObjectStart(TYPE_SNAPSHOT);
        // 75pt
        out.writeObjectField(1);
        out.writeDouble(s.get75thPercentile());
        // 95pt
        out.writeObjectSep();
        out.writeObjectField(2);
        out.writeDouble(s.get95thPercentile());
        // 98pt
        out.writeObjectSep();
        out.writeObjectField(3);
        out.writeDouble(s.get98thPercentile());
        // 99pt
        out.writeObjectSep();
        out.writeObjectField(4);
        out.writeDouble(s.get99thPercentile());
        // 999pt
        out.writeObjectSep();
        out.writeObjectField(5);
        out.writeDouble(s.get999thPercentile());
        // size
        out.writeObjectSep();
        out.writeObjectField(6);
        out.writeInt32(s.size());
        // median
        out.writeObjectSep();
        out.writeObjectField(7);
        out.writeDouble(s.getMedian());
        //
        out.writeObjectEnd();
    }
    
    private static final TypeMeta TYPE_TIMER = new TypeMeta("meter", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("count", 3, IBIO.TypeCodes.INT64),
            new FieldMeta("mean-rate", 4, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("one-minute-rate", 5, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("five-minute-rate", 6, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("fifteen-minute-rate", 7, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("rate-unit", 8, IBIO.TypeCodes.STRING),
            new FieldMeta("min", 9, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("mean", 10, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("max", 11, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("sum", 12, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("std-dev", 13, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("duration-unit", 14, IBIO.TypeCodes.STRING),
            new FieldMeta("event-type", 15, IBIO.TypeCodes.STRING),
            new FieldMeta("snapshot", 16, IBIO.TypeCodes.OBJECT)
    );
    
    protected void writeTimer(MetricName name, Timer t, IBIOOutputStream out) throws IOException
    {
        out.writeObjectStart(TYPE_TIMER);
        //
        this.writeReadingName(name, out);
        // count
        out.writeObjectSep();
        out.writeObjectField(3);
        out.writeInt64(t.count());
        // mean rate
        out.writeObjectSep();
        out.writeObjectField(4);
        out.writeDouble(t.meanRate());
        // 1 min rate
        out.writeObjectSep();
        out.writeObjectField(5);
        out.writeDouble(t.oneMinuteRate());
        // 5 min rate
        out.writeObjectSep();
        out.writeObjectField(6);
        out.writeDouble(t.fiveMinuteRate());
        // 15 min rate
        out.writeObjectSep();
        out.writeObjectField(7);
        out.writeDouble(t.fifteenMinuteRate());
        // rate unit
        out.writeObjectSep();
        out.writeObjectField(8);
        out.writeString(t.rateUnit().toString());
        // min
        out.writeObjectSep();
        out.writeObjectField(9);
        out.writeDouble(t.min());
        // mean
        out.writeObjectSep();
        out.writeObjectField(10);
        out.writeDouble(t.mean());
        // max
        out.writeObjectSep();
        out.writeObjectField(11);
        out.writeDouble(t.max());
        // sum
        out.writeObjectSep();
        out.writeObjectField(12);
        out.writeDouble(t.sum());
        // std-dev
        out.writeObjectSep();
        out.writeObjectField(13);
        out.writeDouble(t.stdDev());
        // duration-unit
        out.writeObjectSep();
        out.writeObjectField(14);
        out.writeString(t.durationUnit().toString());
        // event-type
        out.writeObjectSep();
        out.writeObjectField(15);
        out.writeString(t.eventType());
        // snapshot
        out.writeObjectSep();
        out.writeObjectField(16);
        this.writeSnapshot(t.getSnapshot(), out);
        //
        out.writeObjectEnd();
    }
    
    private static final TypeMeta TYPE_HISTOGRAM = new TypeMeta("meter", 1, 
            new FieldMeta("name", 1, IBIO.TypeCodes.STRING),
            new FieldMeta("scope", 2, IBIO.TypeCodes.STRING),
            new FieldMeta("count", 3, IBIO.TypeCodes.INT64),
            new FieldMeta("min", 4, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("mean", 5, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("max", 6, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("sum", 7, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("std-dev", 8, IBIO.TypeCodes.DP_REAL),
            new FieldMeta("snapshot", 9, IBIO.TypeCodes.OBJECT)
    );
    
    protected void writeHistogram(MetricName name, Histogram h, IBIOOutputStream out) throws IOException
    {
        out.writeObjectStart(TYPE_HISTOGRAM);
        //
        this.writeReadingName(name, out);
        // count
        out.writeObjectSep();
        out.writeObjectField(3);
        out.writeInt64(h.count());
        // min
        out.writeObjectSep();
        out.writeObjectField(4);
        out.writeDouble(h.min());
        // mean
        out.writeObjectSep();
        out.writeObjectField(5);
        out.writeDouble(h.mean());
        // max
        out.writeObjectSep();
        out.writeObjectField(6);
        out.writeDouble(h.max());
        // sum
        out.writeObjectSep();
        out.writeObjectField(7);
        out.writeDouble(h.sum());
        // std-dev
        out.writeObjectSep();
        out.writeObjectField(8);
        out.writeDouble(h.stdDev());
        // snapshot
        out.writeObjectSep();
        out.writeObjectField(9);
        this.writeSnapshot(h.getSnapshot(), out);
        //
        out.writeObjectEnd();
    }
}
