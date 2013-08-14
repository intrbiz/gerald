package com.intrbiz.gerald.polyakov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.intrbiz.gerald.polyakov.packager.JSONPackager;
import com.intrbiz.gerald.polyakov.transport.HTTPTransport;
import com.intrbiz.util.Option;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

/**
 * Polyakov the courier.
 * 
 * Periodically collate, package and courier your metrics.
 * 
 */
public class Polyakov implements Runnable
{
    protected Set<MetricsRegistry> registries = new HashSet<MetricsRegistry>();

    protected List<MetricFilter> filters = new ArrayList<MetricFilter>();

    protected Packager packager;

    protected Transport transport;

    protected Thread runner;

    protected boolean run = false;

    protected long period = TimeUnit.MINUTES.toMillis(5);

    protected volatile boolean sendNow = false;
    
    private Logger logger = Logger.getLogger(Polyakov.class);

    public Polyakov()
    {
        super();
    }

    public Polyakov registry(MetricsRegistry reg)
    {
        this.registries.add(reg);
        return this;
    }

    public Polyakov filter(MetricFilter filter)
    {
        this.filters.add(filter);
        return this;
    }

    public Polyakov packager(Packager p)
    {
        this.packager = p;
        return this;
    }
    
    public Polyakov json()
    {
        return this.packager(new JSONPackager());
    }

    public Polyakov packagerOption(Option<String> option, String value)
    {
        this.packager.option(option, value);
        return this;
    }

    public Polyakov packagerOption(Option<Integer> option, int value)
    {
        this.packager.option(option, value);
        return this;
    }

    public Polyakov packagerOption(Option<Long> option, long value)
    {
        this.packager.option(option, value);
        return this;
    }

    public Polyakov packagerOption(Option<Float> option, float value)
    {
        this.packager.option(option, value);
        return this;
    }

    public Polyakov packagerOption(Option<Double> option, double value)
    {
        this.packager.option(option, value);
        return this;
    }

    public Polyakov packagerOption(Option<Boolean> option, boolean value)
    {
        this.packager.option(option, value);
        return this;
    }

    public Polyakov transport(Transport t)
    {
        this.transport = t;
        return this;
    }
    
    public Polyakov http()
    {
        return this.transport(new HTTPTransport());
    }
    
    public Polyakov from(UUID id, String name, String service)
    {
        this.transport.node(id, name, service);
        return this;
    }
    
    public Polyakov key(PolyakovKey key)
    {
        this.transport.key(key);
        return this;
    }

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
        if (this.packager == null) throw new RuntimeException("Polyakov needs a packager");
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
        logger.trace("Couriering Metrics");
        // select the metrics to send
        Map<MetricName,Metric> metrics = this.revealMetrics();
        // package the metrics
        Parcel p = this.packager.packageMetrics(metrics);
        // courier the metrics
        this.transport.courier(p);
    }
    
    protected Map<MetricName,Metric> revealMetrics()
    {
        Map<MetricName,Metric> metrics = new HashMap<MetricName,Metric>();
        for (MetricsRegistry reg : this.registries)
        {
            for (Entry<MetricName,Metric> m : reg.allMetrics().entrySet())
            {
                if (this.revealMetric(m))
                {
                    logger.trace("Revealing: " + m.getKey().toString());
                    metrics.put(m.getKey(), m.getValue());
                }
            }
        }
        return metrics;
    }
    
    protected boolean revealMetric(Entry<MetricName,Metric> m)
    {
        for (MetricFilter filter : this.filters)
        {
            logger.trace("Applying filter: " + filter.toString());
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
            logger.trace("Waited: " + (System.currentTimeMillis() - sleepTime) + " " + waitTime);
        }
        while (waitTime > 0 && (!this.sendNow));
    }
}
