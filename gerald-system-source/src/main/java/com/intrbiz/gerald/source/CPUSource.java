package com.intrbiz.gerald.source;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public final class CPUSource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public CPUSource()
    {
        super("cpu-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        // CPU Info
        registry.newGauge(CPUSource.class, "cpu-vendor", this.cpuVendorGague());
        registry.newGauge(CPUSource.class, "cpu-model", this.cpuModelGague());
        registry.newGauge(CPUSource.class, "cpu-sockets", this.cpuSocketsGague());
        registry.newGauge(CPUSource.class, "cpu-cores", this.cpuCoresGague());
        registry.newGauge(CPUSource.class, "cpu-frequency", this.cpuFrequencyGague());
        // Load
        registry.newGauge(CPUSource.class, "load-one-minute-average", this.oneMinLoadGague());
        registry.newGauge(CPUSource.class, "load-five-minute-average", this.fiveMinLoadGague());
        registry.newGauge(CPUSource.class, "load-fifteen-minute-average", this.fifteenMinLoadGague());
        // CPU Time
        registry.newGauge(CPUSource.class, "cpu-total-time", this.cpuTotalTimeGague());
        registry.newGauge(CPUSource.class, "cpu-system-time", this.cpuSystemTimeGague());
        registry.newGauge(CPUSource.class, "cpu-user-time", this.cpuUserTimeGague());
        registry.newGauge(CPUSource.class, "cpu-wait-time", this.cpuWaitTimeGague());
        // CPU Usage
        registry.newGauge(CPUSource.class, "cpu-total-usage", this.cpuTotalUsageGague());
        registry.newGauge(CPUSource.class, "cpu-system-usage", this.cpuSystemUsageGague());
        registry.newGauge(CPUSource.class, "cpu-user-usage", this.cpuUserUsageGague());
        registry.newGauge(CPUSource.class, "cpu-wait-usage", this.cpuWaitUsageGague());
    }
    
    protected Gauge<Double> cpuTotalUsageGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    CpuPerc cpuPc = sigar.getCpuPerc();
                    return round(cpuPc.getCombined());
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> cpuSystemUsageGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    CpuPerc cpuPc = sigar.getCpuPerc();
                    return round(cpuPc.getSys());
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> cpuUserUsageGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    CpuPerc cpuPc = sigar.getCpuPerc();
                    return round(cpuPc.getUser());
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> cpuWaitUsageGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    CpuPerc cpuPc = sigar.getCpuPerc();
                    return round(cpuPc.getWait());
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> cpuTotalTimeGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Cpu cpu = sigar.getCpu();
                    return cpu.getTotal();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> cpuSystemTimeGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Cpu cpu = sigar.getCpu();
                    return cpu.getSys();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> cpuUserTimeGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Cpu cpu = sigar.getCpu();
                    return cpu.getUser();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> cpuWaitTimeGague()
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    Cpu cpu = sigar.getCpu();
                    return cpu.getWait();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Integer> cpuSocketsGague()
    {
        return new Gauge<Integer>() {
            @Override
            public Integer value()
            {
                try
                {
                    CpuInfo[] cpuInfo = sigar.getCpuInfoList();
                    return cpuInfo[0].getTotalSockets();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Integer> cpuCoresGague()
    {
        return new Gauge<Integer>() {
            @Override
            public Integer value()
            {
                try
                {
                    CpuInfo[] cpuInfo = sigar.getCpuInfoList();
                    return cpuInfo[0].getTotalCores();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Integer> cpuFrequencyGague()
    {
        return new Gauge<Integer>() {
            @Override
            public Integer value()
            {
                try
                {
                    CpuInfo[] cpuInfo = sigar.getCpuInfoList();
                    return cpuInfo[0].getMhz();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<String> cpuVendorGague()
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    CpuInfo[] cpuInfo = sigar.getCpuInfoList();
                    return cpuInfo[0].getVendor();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }

    protected Gauge<String> cpuModelGague()
    {
        return new Gauge<String>() {
            @Override
            public String value()
            {
                try
                {
                    CpuInfo[] cpuInfo = sigar.getCpuInfoList();
                    return cpuInfo[0].getModel();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> oneMinLoadGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    double[] la = sigar.getLoadAverage();
                    return la[0];
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> fiveMinLoadGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    double[] la = sigar.getLoadAverage();
                    return la[1];
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> fifteenMinLoadGague()
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    double[] la = sigar.getLoadAverage();
                    return la[2];
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
