package com.intrbiz.gerald.sources;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.source.AbstractIntelligenceSource;
import com.intrbiz.gerald.witchcraft.Witchcraft;

public class JVMSource extends AbstractIntelligenceSource
{
    public JVMSource()
    {
        super("Java Metrics");
    }
    
    public void register(MetricRegistry registry)
    {
        this.registerJVMMetrics(registry);
        this.registerMemoryMetrics(registry);
        this.registerThreadMetrics(registry);
    }
    
    protected void registerThreadMetrics(MetricRegistry registry)
    {
    }
    
    protected void registerJVMMetrics(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(JVMSource.class, "jvm-name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("java.vendor");
            }
        });
        registry.register(Witchcraft.name(JVMSource.class, "jvm-version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("java.version");
            }
        });
        registry.register(Witchcraft.name(JVMSource.class, "os-arch"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("os.arch");
            }
        });
        registry.register(Witchcraft.name(JVMSource.class, "os-name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("os.name");
            }
        });
        registry.register(Witchcraft.name(JVMSource.class, "os-version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("os.version");
            }
        });
    }
    
    protected void registerMemoryMetrics(MetricRegistry registry)
    {
        registry.register(Witchcraft.name(JVMSource.class, "memory-free"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return (long) Runtime.getRuntime().freeMemory();
            }
        });
        registry.register(Witchcraft.name(JVMSource.class, "memory-max"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return (long) Runtime.getRuntime().maxMemory();
            }
        });
        registry.register(Witchcraft.name(JVMSource.class, "memory-total"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return (long) Runtime.getRuntime().totalMemory();
            }
        });
    }
}
