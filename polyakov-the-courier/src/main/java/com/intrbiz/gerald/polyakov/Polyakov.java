package com.intrbiz.gerald.polyakov;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.codahale.metrics.Metric;
import com.intrbiz.gerald.polyakov.transport.LamplighterTransport;
import com.intrbiz.gerald.source.IntelligenceSource;
import com.intrbiz.util.Option;

/**
 * Polyakov the courier.
 * 
 * Periodically collate, package and courier your metrics.
 * 
 */
public class Polyakov implements Runnable
{
    protected Set<IntelligenceSource> sources = new HashSet<IntelligenceSource>();

    protected List<MetricFilter> filters = new ArrayList<MetricFilter>();

    protected Transport transport;
    
    protected Node node;

    protected Thread runner;

    protected boolean run = false;

    protected long period = TimeUnit.MINUTES.toMillis(5);

    protected volatile boolean sendNow = false;
    
    private Logger logger = Logger.getLogger(Polyakov.class);

    public Polyakov()
    {
        super();
    }
    
    public Polyakov from(Node node)
    {
        this.node = node;
        return this;
    }

    public Polyakov source(IntelligenceSource source)
    {
        this.sources.add(source);
        return this;
    }

    public Polyakov filter(MetricFilter filter)
    {
        this.filters.add(filter);
        return this;
    }

    public Polyakov transport(Transport t)
    {
        this.transport = t;
        return this;
    }
    
    // out the box transports
    
    public Polyakov lamplighter()
    {
        this.transport = new LamplighterTransport();
        return this;
    }
    
    public Polyakov lamplighter(String lamplighterKey)
    {
        this.transport = new LamplighterTransport();
        this.transport.option(LamplighterTransport.LAMPLIGHTER_KEY, lamplighterKey);
        return this;
    }
    
    //

    public Polyakov courierTo(String url)
    {
        this.transport.to(url);
        return this;
    }

    public Polyakov transportOption(Option<String> option, String value)
    {
        this.transport.option(option, value);
        return this;
    }

    public Polyakov transportOption(Option<Integer> option, int value)
    {
        this.transport.option(option, value);
        return this;
    }

    public Polyakov transportOption(Option<Long> option, long value)
    {
        this.transport.option(option, value);
        return this;
    }

    public Polyakov transportOption(Option<Float> option, float value)
    {
        this.transport.option(option, value);
        return this;
    }

    public Polyakov transportOption(Option<Double> option, double value)
    {
        this.transport.option(option, value);
        return this;
    }

    public Polyakov transportOption(Option<Boolean> option, boolean value)
    {
        this.transport.option(option, value);
        return this;
    }

    public Polyakov period(long period, TimeUnit unit)
    {
        this.period = unit.toMillis(period);
        return this;
    }

    public void start()
    {
        // sanity checks
        if (this.node == null) throw new RuntimeException("Polyakov needs an identity");
        if (this.transport == null) throw new RuntimeException("Polyakov needs a transport");
        // start
        synchronized (this)
        {
            if (this.runner == null)
            {
                this.runner = new Thread(this);
                this.run = true;
                this.runner.start();
            }
        }
    }

    public void shutdown()
    {
        synchronized (this)
        {
            this.run = false;
            this.notifyAll();
        }
    }

    public void sendNow()
    {
        synchronized (this)
        {
            this.sendNow = true;
            this.notifyAll();
        }
    }

    public void run()
    {
        // sleep
        this.sleep();
        while (this.run)
        {
            // send the metrics
            this.courierMetrics();
            // reset stuff
            this.sendNow = false;
            // sleep
            this.sleep();
        }
    }

    protected void courierMetrics()
    {
        logger.debug("Couriering Metrics");
        // parcel up and send our sources
        for (IntelligenceSource source : this.sources)
        {
            this.transport.courier(this.parcelUp(source));
        }
    }
    
    protected Parcel parcelUp(IntelligenceSource source)
    {
        Parcel metrics = new Parcel(this.node, source.getName());
        for (Entry<String,Metric> m : source.getRegistry().getMetrics().entrySet())
        {
            if (this.revealMetric(m))
            {
                if (logger.isTraceEnabled()) logger.trace("Revealing: " + m.getKey().toString());
                metrics.addMetric(m.getKey(), m.getValue());
            }
        }
        return metrics;
    }
    
    protected boolean revealMetric(Entry<String,Metric> m)
    {
        for (MetricFilter filter : this.filters)
        {
            if (logger.isTraceEnabled()) logger.trace("Applying filter: " + filter.toString());
            if (filter.match(m.getKey()))
            {
                return filter.reveal();
            }
        }
        return true;
    }

    protected void sleep()
    {
        long sleepTime = System.currentTimeMillis();
        long waitTime = this.period;
        do
        {
            try
            {
                synchronized (this)
                {
                    this.wait(waitTime);
                }
            }
            catch (InterruptedException e)
            {
            }
            waitTime -= (System.currentTimeMillis() - sleepTime);
            if (logger.isTraceEnabled()) logger.trace("Waited: " + (System.currentTimeMillis() - sleepTime) + " " + waitTime);
        }
        while (waitTime > 0 && (!this.sendNow));
    }
}
