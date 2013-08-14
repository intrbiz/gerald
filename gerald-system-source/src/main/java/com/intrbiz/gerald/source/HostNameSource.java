package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public class HostNameSource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public HostNameSource()
    {
        super("hostname-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        registry.newGauge(HostNameSource.class, "hostname", this.hostNameGague());
    }
    
    protected Gauge<String> hostNameGague()
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    return sigar.getFQDN();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
}
