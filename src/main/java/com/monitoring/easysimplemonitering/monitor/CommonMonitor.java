package com.monitoring.easysimplemonitering.monitor;


import com.monitoring.easysimplemonitering.metricinfo.CommonMetricInfo;
import com.monitoring.easysimplemonitering.utility.MetricCalOperand;
import com.monitoring.easysimplemonitering.utility.MetricUtility;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;

public class CommonMonitor implements ServerMonitorStrategy{
    private final MetricsEndpoint metricsEndpoint;
    private final CommonMetricInfo metricInfo;

    public CommonMonitor(MetricsEndpoint metricsEndpoint, CommonMetricInfo metricInfo) {
        this.metricsEndpoint = metricsEndpoint;
        this.metricInfo = metricInfo;
    }

    @Override
    public boolean isOverThreshold() {
        double val = getMetricsResult();
        if(metricInfo.metricCalOperand()== MetricCalOperand.BIGGER_THEN) {
            return val >= metricInfo.valueThreshold();
        }
        return val < metricInfo.valueThreshold();
    }

    @Override
    public double getMetricsResult() {
        return MetricUtility.getMetricsResult(metricInfo.metric(),metricsEndpoint);
    }


}
