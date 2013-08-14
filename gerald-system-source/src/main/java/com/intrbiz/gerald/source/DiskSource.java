package com.intrbiz.gerald.source;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import com.intrbiz.gerald.InteligenceSource;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricsRegistry;

public class DiskSource extends InteligenceSource
{
    private SigarProxy sigar = Humidor.getInstance().getSigar();
    
    public DiskSource()
    {
        super("disk-source");
    }

    @Override
    public void register(MetricsRegistry registry)
    {
        try
        {
            FileSystem[] fileSystems = sigar.getFileSystemList();
            //
            for (FileSystem fs : fileSystems)
            {
                if (2 == fs.getType())
                {
                    final String mnt  = fs.getDirName();
                    final String dev  = fs.getDevName();
                    final String type = fs.getSysTypeName();
                    //
                    registry.newGauge(DiskSource.class, "fs-device", mnt, new Gauge<String>(){
                        @Override
                        public String value()
                        {
                            return dev;
                        }
                    });
                    registry.newGauge(DiskSource.class, "fs-type", mnt, new Gauge<String>(){
                        @Override
                        public String value()
                        {
                            return type;
                        }
                    });
                    // usage
                    registry.newGauge(DiskSource.class, "fs-size", mnt, this.totalSpaceGague(mnt));
                    registry.newGauge(DiskSource.class, "fs-available", mnt, this.availableSpaceGague(mnt));
                    registry.newGauge(DiskSource.class, "fs-used", mnt, this.usedSpaceGague(mnt));
                    registry.newGauge(DiskSource.class, "fs-used-percent", mnt, this.usedPercentGague(mnt));
                }
            }
        }
        catch (SigarException e)
        {
        }
    }
    
    protected Gauge<Long> totalSpaceGague(final String dir)
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getTotal() * 1024L;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> availableSpaceGague(final String dir)
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getFree() * 1024L;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Long> usedSpaceGague(final String dir)
    {
        return new Gauge<Long>() {
            @Override
            public Long value()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getUsed() * 1024L;
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
    
    protected Gauge<Double> usedPercentGague(final String dir)
    {
        return new Gauge<Double>() {
            @Override
            public Double value()
            {
                try
                {
                    FileSystemUsage fsu = sigar.getFileSystemUsage(dir);
                    return fsu.getUsePercent();
                }
                catch (SigarException e)
                {
                }
                return null;
            }
        };
    }
}
