package com.monitoring.easysimplemonitering.utility;


import lombok.Getter;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;


@Getter
public class MetricUtility{

    public static double getMetricsResult(String metric,MetricsEndpoint metricsEndpoint) {
        return metricsEndpoint.metric(metric, null)
                .getMeasurements().stream()
                .filter(m -> "value".equals(m.getStatistic().getTagValueRepresentation()))
                .mapToDouble(MetricsEndpoint.Sample::getValue)
                .findFirst()
                .orElse(0.0);
    }




}
