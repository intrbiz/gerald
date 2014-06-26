package com.intrbiz.gerald.sources;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.source.AbstractIntelligenceSource;

public class JVMSource extends AbstractIntelligenceSource
{
    public JVMSource()
    {
        super("com.intrbiz.JVMSource");
    }
    
    public void register(MetricRegistry registry)
    {
        this.registerJVMMetrics(registry);
        this.registerMemoryMetrics(registry);
    }
    
    protected void registerJVMMetrics(MetricRegistry registry)
    {
        registry.register(name("jvm", "name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("java.vendor");
            }
        });
        registry.register(name("jvm", "version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("java.version");
            }
        });
        registry.register(name("os", "arch"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("os.arch");
            }
        });
        registry.register(name("os", "name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("os.name");
            }
        });
        registry.register(name("os", "version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return System.getProperty("os.version");
            }
        });
    }
    
    protected void registerMemoryMetrics(MetricRegistry registry)
    {
        registry.register(name("memory", "free"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return (long) Runtime.getRuntime().freeMemory();
            }
        });
        registry.register(name("memory", "max"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return (long) Runtime.getRuntime().maxMemory();
            }
        });
        registry.register(name("memory", "total"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return (long) Runtime.getRuntime().totalMemory();
            }
        });
    }
}
