package com.intrbiz.gerald;

import java.util.concurrent.TimeUnit;

import com.intrbiz.gerald.polyakov.MetricFilter;
import com.intrbiz.gerald.polyakov.Node;
import com.intrbiz.gerald.polyakov.Option;
import com.intrbiz.gerald.polyakov.Polyakov;
import com.intrbiz.gerald.polyakov.Transport;
import com.intrbiz.gerald.source.IntelligenceSource;
import com.intrbiz.gerald.sources.JVMSource;
import com.intrbiz.gerald.witchcraft.Witchcraft;
import com.intrbiz.gerald.witchcraft.WitchcraftListener;

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
    
    protected final Witchcraft witchcraft;
    
    protected final Polyakov courier;
    
    protected volatile boolean started = false;
    
    private Gerald()
    {
        super();
        this.courier = new Polyakov();
        this.witchcraft = Witchcraft.get();
        this.witchcraft.addRegisterListener(new WitchcraftListener()
        {
            @Override
            public void onAdded(IntelligenceSource src)
            {
                if (started)
                {
                    courier.source(src);
                    src.start();
                }
            }
        });
        // default sources
        this.source(new JVMSource());
    }
    
    public Gerald from(Node node)
    {
        this.courier.from(node);
        return this;
    }
    
    public Gerald from()
    {
        this.courier.from(Node.service());
        return this;
    }
    
    public Gerald from(String service)
    {
        this.courier.from(Node.service(service));
        return this;
    }
    
    public Gerald courierTo(String url)
    {
        courier.courierTo(url);
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
    
    public Witchcraft witchcraft()
    {
        return this.witchcraft;
    }
    
    public Gerald source(IntelligenceSource source)
    {
        this.witchcraft.register(source);
        return this;
    }

    /**
     * Tell Gerald to start digging
     */
    public synchronized void start()
    {
        if (this.courier == null) throw new RuntimeException("Gerald needs a courier to handle him!");
        // start the sources
        for (IntelligenceSource src : this.witchcraft.getSources())
        {
            this.courier.source(src);
            src.start();
        }
        // start Polyakov the Courier to dispatch the metrics
        this.courier.start();
        this.started = true;
    }
}
