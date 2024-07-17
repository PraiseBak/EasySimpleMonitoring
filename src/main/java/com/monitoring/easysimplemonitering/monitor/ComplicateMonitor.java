package com.monitoring.easysimplemonitering.monitor;

import com.monitoring.easysimplemonitering.metricinfo.ComplicateMetricInfo;
import com.monitoring.easysimplemonitering.utility.MetricUtility;
import com.monitoring.easysimplemonitering.utility.MetricCalOperand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;

@Slf4j
public class ComplicateMonitor implements ServerMonitorStrategy{
    private final MetricsEndpoint metricsEndpoint;
    private final ComplicateMetricInfo complicateMetricInfo;

    public ComplicateMonitor(MetricsEndpoint metricsEndpoint, ComplicateMetricInfo complicateMetricInfo) {
        this.metricsEndpoint = metricsEndpoint;
        this.complicateMetricInfo = complicateMetricInfo;
    }

    @Override
    public double getMetricsResult() {
        double firstResult = MetricUtility.getMetricsResult(complicateMetricInfo.metric(), metricsEndpoint);
        double secondResult = MetricUtility.getMetricsResult(complicateMetricInfo.metric(), metricsEndpoint);

        MetricCalOperand metricCalOperand = complicateMetricInfo.metricCalOperand();
        switch (metricCalOperand) {
            case PLUS:
                return firstResult + secondResult;
            case MINUS:
                return firstResult - secondResult;
            case MULTIPLE:
                return firstResult * secondResult;
            case DIVIDED:
                if (secondResult != 0) {
                    return firstResult / secondResult;
                } else {
                    log.warn("Division by zero is not allowed.");
                    return Double.NaN; // or throw an exception as per your application's logic
                }
            default:
                log.warn("Unsupported operand: " + metricCalOperand);
                return Double.NaN; // or handle the unsupported case appropriately
        }
    }


    @Override
    public boolean isOverThreshold() {
        double result = getMetricsResult();

        if (Double.isNaN(result)) {
            return false; // Handle invalid calculation result
        }

        return result < complicateMetricInfo.valueThreshold();
    }



}
