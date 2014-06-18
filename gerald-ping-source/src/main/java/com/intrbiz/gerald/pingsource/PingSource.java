package com.intrbiz.gerald.pingsource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.source.AbstractIntelligenceSource;
import com.intrbiz.gerald.witchcraft.Witchcraft;
import com.intrbiz.pinger.Pinger;

/**
 * A server to ping hosts and monitor their availability
 */
public class PingSource extends AbstractIntelligenceSource implements Runnable
{    
    private Pinger<PingSourceTarget> pinger;

    private Thread pingThread;

    public PingSource()
    {
        super("ping-source");
        this.pinger = new Pinger<PingSourceTarget>(1000);
        //
        this.pinger.setPeriod(30, TimeUnit.SECONDS);
        //
        this.pingThread = new Thread(this, "ping-source");
        this.pingThread.setDaemon(true);
    }

    @Override
    protected void register(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(PingSource.class, "pinger-version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return "1.0.0";
            }
        });
        //
        registry.register(Witchcraft.name(PingSource.class, "pinger-total-sent"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return pinger.getTotalSent();
            }
        });
        //
        registry.register(Witchcraft.name(PingSource.class, "pinger-total-recv"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return pinger.getTotalRecv();
            }
        });
        //
        registry.register(Witchcraft.name(PingSource.class, "pinger-send-rate"), new Gauge<Double>(){
            @Override
            public Double getValue()
            {
                return pinger.getSendRate();
            }
        });
        //
        registry.register(Witchcraft.name(PingSource.class, "pinger-recv-rate"), new Gauge<Double>(){
            @Override
            public Double getValue()
            {
                return pinger.getRecvRate();
            }
        });
    }
    
    public void start()
    {
        this.pingThread.start();        
    }

    public void addTarget(String host) throws UnknownHostException
    {
        this.pinger.addTarget(new PingSourceTarget(this.registry, host, InetAddress.getByName(host)), true);
    }

    public void removeTarget(String host)
    {
    }

    public void run()
    {
        this.pinger.run();
    }
}
