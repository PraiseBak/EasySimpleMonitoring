package com.monitoring.easysimplemonitering.monitor;

public interface ServerMonitorStrategy{

    boolean isOverThreshold();
    double getMetricsResult();
}
