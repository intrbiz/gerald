package com.intrbiz.gerald.source;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public final class MemorySource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public MemorySource()
    {
        super("memory-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        // The amount of RAM
        registry.newGauge(MemorySource.class, "ram", this.ramGague());
        // Usage
        registry.newGauge(MemorySource.class, "total-memory", this.totalGague());
        registry.newGauge(MemorySource.class, "used-memory", this.usedGague());
        registry.newGauge(MemorySource.class, "free-memory", this.freeGague());
        registry.newGauge(MemorySource.class, "actual-used-memory", this.actualUsedGague());
        registry.newGauge(MemorySource.class, "actual-free-memory", this.actualFreeGague());
        // Usage percentage
        registry.newGauge(MemorySource.class, "used-memory-percent", this.usedPercentGague());
        registry.newGauge(MemorySource.class, "free-memory-percent", this.freePercentGague());
    }
    
    protected Gauge<Long> ramGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return m.getRam() * 1024 * 1024;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> totalGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return m.getTotal();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> usedGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return m.getUsed();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> freeGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return m.getFree();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> actualUsedGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return m.getActualUsed();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> actualFreeGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return m.getActualFree();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> usedPercentGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return round(m.getUsedPercent());
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> freePercentGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    Mem m = sigar.getMem();
                    return round(m.getFreePercent());
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected static double round(double d)
    {
        return (((double) Math.round(d * 1000)) / 1000D);
    }
}
