package com.intrbiz.gerald.sources;

import java.lang.Thread.State;
import java.util.Map;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.core.VirtualMachineMetrics;

public class JVMSource extends InteligenceSource
{
    public JVMSource()
    {
        super("Java Metrics");
    }
    
    public void register(MetricsRegistry registry)
    {
        this.registerJVMMetrics(registry);
        this.registerMemoryMetrics(registry);
        this.registerThreadMetrics(registry);
    }
    
    protected void registerThreadMetrics(MetricsRegistry registry)
    {
        final VirtualMachineMetrics vmm = VirtualMachineMetrics.getInstance();
        //
        registry.newGauge(JVMSource.class, "daemon-thread-count", new Gauge<Integer>(){
            @Override
            public Integer value()
            {
                return vmm.daemonThreadCount();
            }
        });
        for (final Thread.State threadState : Thread.State.values())
        {
            registry.newGauge(JVMSource.class, "thread-state-percentage", threadState.toString().toLowerCase(), new Gauge<Double>() {
                @Override
                public Double value()
                {
                    Map<State, Double> states = vmm.threadStatePercentages();
                    if (! states.containsKey(threadState)) return 0D;
                    return states.get(threadState);
                }
            });            
        }
    }
    
    protected void registerJVMMetrics(MetricsRegistry registry)
    {
        final VirtualMachineMetrics vmm = VirtualMachineMetrics.getInstance();
        //
        registry.newGauge(JVMSource.class, "jvm-name", new Gauge<String>(){
            @Override
            public String value()
            {
                return vmm.name();
            }
        });
        registry.newGauge(JVMSource.class, "jvm-version", new Gauge<String>(){
            @Override
            public String value()
            {
                return vmm.version();
            }
        });
        registry.newGauge(JVMSource.class, "uptime", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return vmm.uptime();
            }
        });
    }
    
    protected void registerMemoryMetrics(MetricsRegistry registry)
    {
        final VirtualMachineMetrics vmm = VirtualMachineMetrics.getInstance();
        //
        registry.newGauge(JVMSource.class, "heap-init", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.heapInit();
            }
        });
        registry.newGauge(JVMSource.class, "heap-committed", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.heapCommitted();
            }
        });
        registry.newGauge(JVMSource.class, "heap-max", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.heapMax();
            }
        });
        registry.newGauge(JVMSource.class, "heap-used", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.heapUsed();
            }
        });
        registry.newGauge(JVMSource.class, "heap-usage", new Gauge<Double>(){
            @Override
            public Double value()
            {
                return vmm.heapUsage();
            }
        });
        registry.newGauge(JVMSource.class, "non-heap-usage", new Gauge<Double>(){
            @Override
            public Double value()
            {
                return (double) vmm.nonHeapUsage();
            }
        });
        //
        registry.newGauge(JVMSource.class, "total-init", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.totalInit();
            }
        });
        registry.newGauge(JVMSource.class, "total-committed", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.totalCommitted();
            }
        });
        registry.newGauge(JVMSource.class, "total-max", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.totalMax();
            }
        });
        registry.newGauge(JVMSource.class, "total-used", new Gauge<Long>(){
            @Override
            public Long value()
            {
                return (long) vmm.totalUsed();
            }
        });
    }
}
