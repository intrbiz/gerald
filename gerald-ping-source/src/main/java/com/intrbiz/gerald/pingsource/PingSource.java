package com.intrbiz.gerald.pingsource;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.intrbiz.gerald.InteligenceSource;
import com.intrbiz.pinger.Pinger;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

/**
 * A server to ping hosts and monitor their availability
 */
public class PingSource extends InteligenceSource implements Runnable
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
    protected void register(MetricsRegistry registry)
    {
        registry.newGauge(PingSource.class, "pinger-version", new Gauge<String>(){
            @Override
            public String value()
            {
                return "1.0.0";
            }
        });
        //
        registry.newGauge(PingSource.class, "pinger-total-sent", new Gauge<Integer>(){
            @Override
            public Integer value()
            {
                return pinger.getTotalSent();
            }
        });
        //
        registry.newGauge(PingSource.class, "pinger-total-recv", new Gauge<Integer>(){
            @Override
            public Integer value()
            {
                return pinger.getTotalRecv();
            }
        });
        //
        registry.newGauge(PingSource.class, "pinger-send-rate", new Gauge<Double>(){
            @Override
            public Double value()
            {
                return pinger.getSendRate();
            }
        });
        //
        registry.newGauge(PingSource.class, "pinger-recv-rate", new Gauge<Double>(){
            @Override
            public Double value()
            {
                return pinger.getRecvRate();
            }
        });
    }
    
    protected void doStart()
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
