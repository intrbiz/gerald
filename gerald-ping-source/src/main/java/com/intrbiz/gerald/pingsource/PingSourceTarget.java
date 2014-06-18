package com.intrbiz.gerald.pingsource;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.intrbiz.gerald.witchcraft.Witchcraft;
import com.intrbiz.pinger.PingTarget;

public class PingSourceTarget extends PingTarget
{
    private boolean up = false;
    
    private final Timer latencyTimer;
    
    private final Gauge<Boolean> isUpGauge;

    private final Gauge<Integer> pingsGauge;

    private final Gauge<Integer> timeoutsGauge;

    public PingSourceTarget(MetricRegistry registry, String name, InetAddress address)
    {
        super(name, address);
        //
        this.latencyTimer = registry.timer(Witchcraft.name(PingSource.class, "latency", this.getName()));
        this.isUpGauge = registry.register(Witchcraft.name(PingSource.class, "isUp", this.getName()), new Gauge<Boolean>()
        {
            @Override
            public Boolean getValue()
            {
                return isUp();
            }
        });
        this.pingsGauge = registry.register(Witchcraft.name(PingSource.class, "pings", this.getName()), new Gauge<Integer>()
        {
            @Override
            public Integer getValue()
            {
                return getSentCount();
            }
        });
        this.timeoutsGauge = registry.register(Witchcraft.name(PingSource.class, "timeouts", this.getName()), new Gauge<Integer>()
        {
            @Override
            public Integer getValue()
            {
                return getDroppedCount();
            }
        });
    }
    
    public boolean isUp()
    {
        return this.up;
    }

    public Timer getLatencyTimer()
    {
        return latencyTimer;
    }
    
    public Gauge<Boolean> getIsUpGauge()
    {
        return isUpGauge;
    }

    public Gauge<Integer> getPingsGauge()
    {
        return pingsGauge;
    }

    public Gauge<Integer> getTimeoutsGauge()
    {
        return timeoutsGauge;
    }
    
    public void close(MetricRegistry registry)
    {
        registry.remove(Witchcraft.name(PingSource.class, "latency", this.getName()));
        registry.remove(Witchcraft.name(PingSource.class, "isUp", this.getName()));
        registry.remove(Witchcraft.name(PingSource.class, "pings", this.getName()));
        registry.remove(Witchcraft.name(PingSource.class, "timeouts", this.getName()));
    }

    @Override
    protected void recvCallback(int latency, int sent, int recv)
    {
        if (latency == -1)
        {
            this.up = false;
        }
        else
        {
            this.up = true;
            this.latencyTimer.update(latency, TimeUnit.MILLISECONDS);
        }
    }
    
}
