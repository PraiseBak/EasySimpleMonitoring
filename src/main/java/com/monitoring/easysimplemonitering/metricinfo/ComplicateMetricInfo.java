package com.monitoring.easysimplemonitering.metricinfo;

import com.monitoring.easysimplemonitering.utility.MetricCalOperand;

public record ComplicateMetricInfo(String metric, String secondMetric , double valueThreshold, String warnMsg,
                                   MetricCalOperand metricCalOperand) {


}
