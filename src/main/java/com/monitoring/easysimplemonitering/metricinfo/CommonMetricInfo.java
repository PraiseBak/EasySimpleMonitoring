package com.monitoring.easysimplemonitering.metricinfo;

import com.monitoring.easysimplemonitering.utility.MetricCalOperand;

public record CommonMetricInfo(String metric, double valueThreshold, String warnMsg, MetricCalOperand metricCalOperand){


}
