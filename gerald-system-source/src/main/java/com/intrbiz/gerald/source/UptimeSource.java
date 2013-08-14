package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Uptime;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public class UptimeSource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public UptimeSource()
    {
        super("uptime-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        registry.newGauge(UptimeSource.class, "uptime", this.uptimeGague());
    }
    
    protected Gauge<Double> uptimeGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    Uptime u = sigar.getUptime();
                    return u.getUptime();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
}
