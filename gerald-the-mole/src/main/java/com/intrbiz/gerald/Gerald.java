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
 * <p>
 * Gerald the mole! Collecting and reporting your application's 
 * metrics.
 * </p><p>
 * Gerald collects metrics about your application, these metrics 
 * can cover: performance, errors, etc.  Allowing your application 
 * to be monitored in great depth.
 * </p><p>
 * Gerald, with the help of Polyakov publishes the gathered 
 * application metrics to a collection hub.  Polyakov is most often 
 * used in conjunction with Lamplighter to collect application metrics.
 * </p><p>
 * It is pretty simple to get Gerald up and running and doing his 
 * digging:
 * </p>
 * <code>
 *   Gerald.theMole().start();
 * </code>
 * <p>
 * Would start Gerald using the default, auto detected configuration, 
 * for specific setups use the fluent interface that Gerald provides 
 * to alter his activities.
 * </p>
 * <p>
 * Gerald will make use of the following system properties by default:
 * </p>
 * <ul>
 *   <li>
 *      <strong>gerald.host.name</strong> - override the host name of this node
 *   </li>
 *   <li>
 *      <strong>gerald.host.id</strong> - override the host UUID of this node, otherwise the file /etc/gerald.host.id is tried
 *   </li>
 *   <li>
 *      <strong>gerald.service</strong> - the service name for this application, unless explicit set by the application
 *   </li>
 *   <li>
 *      <strong>lamplighter.host</strong> - override the Lamplighter host to publish too
 *   </li>
 *   <li>
 *      <strong>lamplighter.key</strong> - the key to use when authenticating with Lamplighter
 *   </li>
 * </ul>
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
    
    /**
     * Set the identifier of this node
     */
    public Gerald from(Node node)
    {
        this.courier.from(node);
        return this;
    }
    
    /**
     * Auto detect our node identifier
     */
    public Gerald from()
    {
        this.courier.from(Node.service());
        return this;
    }
    
    /**
     * Set our node identifier using the given service name and 
     * auto detecting the host identifier
     * @param service the service name to use
     */
    public Gerald from(String service)
    {
        this.courier.from(Node.service(service));
        return this;
    }
    
    /**
     * Set URL to where metrics will be sent
     */
    public Gerald courierTo(String url)
    {
        courier.courierTo(url);
        return this;
    }
    
    /**
     * Add a filter to restrict what metrics will be 
     * revealed
     */
    public Gerald filter(MetricFilter filter)
    {
        courier.filter(filter);
        return this;
    }

    /**
     * Set the transport protocol which will be used 
     * to send metrics
     */
    public Gerald transport(Transport t)
    {
        courier.transport(t);
        return this;
    }
    
    /**
     * Send metrics to Lamplighter.
     * 
     * The Lamplighter key is determined by:
     * 
     *   1. Using the "lamplighter.key" system property to 
     *      determine the file containing the key to use
     *      
     *   2. Reading the key from /etc/lamplighter.key
     * 
     * The Lamplighter key is determined by:
     * 
     *   1. Using the value of "lamplighter.host" system property
     *   
     *   2. Using the following hosts within the configured search domains:
     *      a. "stationx.lamplighter"
     *      b. "stationx"
     *      c. "lamplighter"
     *   
     *   3. Defaulting to "ws://127.0.0.1/stationx"
     * 
     */
    public Gerald lamplighter()
    {
        this.courier.lamplighter();
        return this;
    }
    
    /**
     * Send metrics to Lamplighter using the given key,
     * Note: remember to set where metrics should be sent to
     */
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

    /**
     * Set how often metrics should be reported
     */
    public Gerald period(long period, TimeUnit unit)
    {
        courier.period(period, unit);
        return this;
    }
    
    /**
     * Get the registry of intelligence sources (metrics registers)
     */
    public Witchcraft witchcraft()
    {
        return this.witchcraft;
    }
    
    /**
     * Register an intelligence source with Gerald which will be revealed
     */
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
