package com.intrbiz.gerald.pingsource;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import com.intrbiz.pinger.PingTarget;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.core.Timer;

public class PingSourceTarget extends PingTarget
{
    private boolean up = false;
    
    private final Timer latencyTimer;
    
    private final Gauge<Boolean> isUpGauge;

    private final Gauge<Integer> pingsGauge;

    private final Gauge<Integer> timeoutsGauge;

    public PingSourceTarget(MetricsRegistry registry, String name, InetAddress address)
    {
        super(name, address);
        //
        this.latencyTimer = registry.newTimer(PingSource.class, "latency", this.getName(), TimeUnit.MICROSECONDS, TimeUnit.MINUTES);
        this.isUpGauge = registry.newGauge(PingSource.class, "isUp", this.getName(), new Gauge<Boolean>()
        {
            @Override
            public Boolean value()
            {
                return isUp();
            }
        });
        this.pingsGauge = registry.newGauge(PingSource.class, "pings", this.getName(), new Gauge<Integer>()
        {
            @Override
            public Integer value()
            {
                return getSentCount();
            }
        });
        this.timeoutsGauge = registry.newGauge(PingSource.class, "timeouts", this.getName(), new Gauge<Integer>()
        {
            @Override
            public Integer value()
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
    
    public void close(MetricsRegistry registry)
    {
        registry.removeMetric(PingSource.class, "latency", this.getName());
        registry.removeMetric(PingSource.class, "isUp", this.getName());
        registry.removeMetric(PingSource.class, "pings", this.getName());
        registry.removeMetric(PingSource.class, "timeouts", this.getName());
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
