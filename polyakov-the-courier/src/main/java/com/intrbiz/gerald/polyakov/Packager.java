package com.intrbiz.gerald.polyakov;

import java.util.Map;

import com.intrbiz.util.Optioned;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;

public interface Packager extends Optioned
{   
    Parcel packageMetrics(Map<MetricName,Metric> metrics);
}
