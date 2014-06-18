package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Uptime;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class UptimeSource extends AbstractIntelligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public UptimeSource()
    {
        super("uptime-source");
    }

    @Override
    public void register(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(UptimeSource.class, "uptime"), this.uptimeGague());
    }
    
    protected Gauge<Double> uptimeGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double getValue()
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
