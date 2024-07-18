package com.monitoring.easysimplemonitering.metricinfo;

import com.monitoring.easysimplemonitering.monitor.MetricInfo;
import com.monitoring.easysimplemonitering.utility.MetricCalOperand;

public record CommonMetricInfo(String metric, double valueThreshold, String warnMsg, MetricCalOperand metricCalOperand) implements MetricInfo {


    @Override
    public String getWarnMsg() {
        return warnMsg;
    }

    @Override
    public String getMetric() {
        return metric;
    }
}
