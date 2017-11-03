package com.intrbiz.gerald.source;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.intrbiz.gerald.source.AbstractIntelligenceSource;

/**
 * JVM metrics, covering:
 *   JVM vendor information
 *   Operating System information
 *   Uptime
 *   CPU info
 *   JVM memory information
 *   Thread counts
 *   Loaded class counts
 *   JIT time
 *   Memory pool usage
 */
public class JVMSource extends AbstractIntelligenceSource
{
    private final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
    
    private final OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
    
    private final MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
    
    private final ClassLoadingMXBean classes = ManagementFactory.getClassLoadingMXBean();
    
    private final ThreadMXBean threads = ManagementFactory.getThreadMXBean();
    
    private final CompilationMXBean compilation = ManagementFactory.getCompilationMXBean();
    
    public JVMSource()
    {
        super("com.intrbiz.jvm");
    }
    
    public void register(MetricRegistry registry)
    {
        // vm info
        registry.register(name("vm", "name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return runtime.getVmName();
            }
        });
        registry.register(name("vm", "vendor"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return runtime.getVmVendor();
            }
        });
        registry.register(name("vm", "version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return runtime.getVmVersion();
            }
        });
        // os info
        registry.register(name("os", "arch"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return os.getArch();
            }
        });
        registry.register(name("os", "name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return os.getName();
            }
        });
        registry.register(name("os", "version"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return os.getVersion();
            }
        });
        // basic uptime info
        registry.register(name("uptime"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return runtime.getUptime();
            }
        });
        registry.register(name("start_time"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return runtime.getStartTime();
            }
        });
        // basic cpu info
        registry.register(name("available_processors"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return os.getAvailableProcessors();
            }
        });
        registry.register(name("system_load_average"), new Gauge<Double>(){
            @Override
            public Double getValue()
            {
                return os.getSystemLoadAverage();
            }
        });
        // memory
        registry.register(name("memory", "objects_pending_finalization"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return memory.getObjectPendingFinalizationCount();
            }
        });
        // heap
        registry.register(name("memory", "heap", "max"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getHeapMemoryUsage().getMax();
            }
        });
        registry.register(name("memory", "heap", "committed"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getHeapMemoryUsage().getCommitted();
            }
        });
        registry.register(name("memory", "heap", "used"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getHeapMemoryUsage().getUsed();
            }
        });
        registry.register(name("memory", "heap", "free"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                MemoryUsage usage = memory.getHeapMemoryUsage();
                return usage.getMax() - usage.getUsed();
            }
        });
        registry.register(name("memory", "heap", "initial"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getHeapMemoryUsage().getInit();
            }
        });
        registry.register(name("memory", "heap", "used", "percentage"), new Gauge<Double>(){
            @Override
            public Double getValue()
            {
                MemoryUsage usage = memory.getHeapMemoryUsage();
                return (((double) usage.getUsed()) / ((double) usage.getMax())) * 100D;
            }
        });
        registry.register(name("memory", "heap", "free", "percentage"), new Gauge<Double>(){
            @Override
            public Double getValue()
            {
                MemoryUsage usage = memory.getHeapMemoryUsage();
                return (((double) (usage.getMax() - usage.getUsed())) / ((double) usage.getMax())) * 100D;
            }
        });
        // non - heap
        registry.register(name("memory", "non_heap", "max"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getNonHeapMemoryUsage().getMax();
            }
        });
        registry.register(name("memory", "non_heap", "committed"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getNonHeapMemoryUsage().getCommitted();
            }
        });
        registry.register(name("memory", "non_heap", "used"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getNonHeapMemoryUsage().getUsed();
            }
        });
        registry.register(name("memory", "non_heap", "free"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                MemoryUsage usage = memory.getNonHeapMemoryUsage();
                return usage.getMax() - usage.getUsed();
            }
        });
        registry.register(name("memory", "non_heap", "initial"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getNonHeapMemoryUsage().getInit();
            }
        });
        // total
        registry.register(name("memory", "total", "used"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return memory.getHeapMemoryUsage().getUsed() + memory.getNonHeapMemoryUsage().getUsed();
            }
        });
        // classes
        registry.register(name("classes", "total_loaded"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return classes.getTotalLoadedClassCount();
            }
        });
        registry.register(name("classes", "loaded"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return classes.getLoadedClassCount();
            }
        });
        registry.register(name("classes", "unloaded"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return classes.getUnloadedClassCount();
            }
        });
        // threads
        registry.register(name("thread", "count"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return threads.getThreadCount();
            }
        });
        registry.register(name("thread", "peak", "count"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return threads.getPeakThreadCount();
            }
        });
        registry.register(name("thread", "daemon", "count"), new Gauge<Integer>(){
            @Override
            public Integer getValue()
            {
                return threads.getDaemonThreadCount();
            }
        });
        registry.register(name("thread", "total_started"), new Gauge<Long>(){
            @Override
            public Long getValue()
            {
                return threads.getTotalStartedThreadCount();
            }
        });
        // JIT
        registry.register(name("jit", "name"), new Gauge<String>(){
            @Override
            public String getValue()
            {
                return compilation.getName();
            }
        });
        if (compilation.isCompilationTimeMonitoringSupported())
        {
            registry.register(name("jit", "time"), new Gauge<Long>(){
                @Override
                public Long getValue()
                {
                    return compilation.getTotalCompilationTime();
                }
            });
        }
        // memory pool metrics?
        for (final MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans())
        {
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "name"), new Gauge<String>(){
                @Override
                public String getValue()
                {
                    return pool.getName();
                }
            });
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "type"), new Gauge<String>(){
                @Override
                public String getValue()
                {
                    return pool.getType().toString();
                }
            });            
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "max"), new Gauge<Long>(){
                @Override
                public Long getValue()
                {
                    return pool.getUsage().getMax();
                }
            });
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "committed"), new Gauge<Long>(){
                @Override
                public Long getValue()
                {
                    return pool.getUsage().getCommitted();
                }
            });
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "used"), new Gauge<Long>(){
                @Override
                public Long getValue()
                {
                    return memory.getHeapMemoryUsage().getUsed();
                }
            });
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "initial"), new Gauge<Long>(){
                @Override
                public Long getValue()
                {
                    return pool.getUsage().getInit();
                }
            });
            registry.register(name("memory", "pool", "[" + pool.getName() + "]", "used", "peak"), new Gauge<Long>(){
                @Override
                public Long getValue()
                {
                    return pool.getPeakUsage().getUsed();
                }
            });
            if (pool.isUsageThresholdSupported())
            {
                registry.register(name("memory", "pool", "[" + pool.getName() + "]", "usage_threshold"), new Gauge<Long>(){
                    @Override
                    public Long getValue()
                    {
                        return pool.getUsageThreshold();
                    }
                });
                registry.register(name("memory", "pool", "[" + pool.getName() + "]", "usage_threshold_crossed"), new Gauge<Long>(){
                    @Override
                    public Long getValue()
                    {
                        return pool.getUsageThresholdCount();
                    }
                });
            }
            if (pool.isCollectionUsageThresholdSupported())
            {
                registry.register(name("memory", "pool", "[" + pool.getName() + "]", "collection", "usage_threshold"), new Gauge<Long>(){
                    @Override
                    public Long getValue()
                    {
                        return pool.getCollectionUsageThresholdCount();
                    }
                });
                registry.register(name("memory", "pool", "[" + pool.getName() + "]", "collection", "usage_threshold_crossed"), new Gauge<Long>(){
                    @Override
                    public Long getValue()
                    {
                        return pool.getCollectionUsageThresholdCount();
                    }
                });
            }
        }
    }
}
