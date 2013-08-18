package com.intrbiz.gerald;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.intrbiz.gerald.polyakov.MetricFilter;
import com.intrbiz.gerald.polyakov.Node;
import com.intrbiz.gerald.polyakov.Polyakov;
import com.intrbiz.gerald.polyakov.Transport;
import com.intrbiz.util.Option;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricsRegistry;

/**
 * Gerald the mole!
 * 
 * Collecting all your most secret metrics and exposing them.
 *  
 */
public final class Gerald
{
    private static final Gerald theMole = new Gerald();
    
    public static final Gerald theMole()
    {
        return theMole;
    }
    
    protected final ConcurrentMap<String,InteligenceSource> sources = new ConcurrentHashMap<String, InteligenceSource>();
    
    protected final Polyakov courier = new Polyakov();
    
    protected volatile boolean started = false;
    
    private Gerald()
    {
        super();
        // register the default registry with polyakov
        this.courier.registry(Metrics.defaultRegistry());
    }
    
    /**
     * Add an inteligence source for Gerald to expose
     * @param source
     * @return
     */
    public Gerald source(InteligenceSource source)
    {
        if (source == null) throw new IllegalArgumentException("Cannot register a null intelligence source");
        if (this.started) 
        {
            if (! this.sources.containsKey(source.getName()))
            {
                this.sources.put(source.getName(), source);
                //
                MetricsRegistry reg = source.register();
                this.courier.registry(reg);
                source.start();
            }
        }
        else
        {
            this.sources.put(source.getName(), source);
        }
        return this;
    }
    
    public Gerald from(Node node)
    {
        this.courier.from(node);
        return this;
    }
    
    public Gerald courierTo(String url)
    {
        courier.courierTo(url);
        return this;
    }

    public Gerald registry(MetricsRegistry reg)
    {
        courier.registry(reg);
        return this;
    }

    public Gerald filter(MetricFilter filter)
    {
        courier.filter(filter);
        return this;
    }

    public Gerald transport(Transport t)
    {
        courier.transport(t);
        return this;
    }
    
    public Gerald lamplighter()
    {
        this.courier.lamplighter();
        return this;
    }
    
    public Gerald lamplighter(String lamplighterKey)
    {
        this.courier.lamplighter(lamplighterKey);
        return this;
    }

    public Gerald transportOption(Option<String> option, String value)
    {
        courier.transportOption(option, value);
        return this;
    }

    public Gerald transportOption(Option<Integer> option, int value)
    {
        courier.transportOption(option, value);
        return this;
    }

    public Gerald transportOption(Option<Long> option, long value)
    {
        courier.transportOption(option, value);
        return this;
    }

    public Gerald transportOption(Option<Float> option, float value)
    {
        courier.transportOption(option, value);
        return this;
    }

    public Gerald transportOption(Option<Double> option, double value)
    {
        courier.transportOption(option, value);
        return this;
    }

    public Gerald transportOption(Option<Boolean> option, boolean value)
    {
        courier.transportOption(option, value);
        return this;
    }

    public Gerald period(long period, TimeUnit unit)
    {
        courier.period(period, unit);
        return this;
    }

    /**
     * Make Gerald start digging
     */
    public synchronized void start()
    {
        if (this.courier == null) throw new RuntimeException("Gerald needs a courier to handle him!");
        // start the sources
        for (InteligenceSource src : this.sources.values())
        {
            MetricsRegistry reg = src.register();
            this.courier.registry(reg);
            src.start();
        }
        // start Polyakov the Courier to dispatch the metrics
        this.courier.start();
        this.started = true;
    }
    
    public InteligenceSource getSource(String name)
    {
        return this.sources.get(name);
    }
    
    public boolean containsSource(String name)
    {
        return this.sources.containsKey(name);
    }
    
    public Set<String> getSourceNames()
    {
        return Collections.unmodifiableSet(this.sources.keySet());
    }
    
    public Collection<InteligenceSource> getSources()
    {
        return Collections.unmodifiableCollection(this.sources.values());
    }
}
