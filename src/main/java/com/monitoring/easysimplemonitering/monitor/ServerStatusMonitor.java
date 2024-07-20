package com.monitoring.easysimplemonitering.monitor;

import com.monitoring.easysimplemonitering.metricinfo.CommonMetricInfo;
import com.monitoring.easysimplemonitering.metricinfo.ComplicateMetricInfo;
import com.monitoring.easysimplemonitering.email.SimpleEmailService;
import com.monitoring.easysimplemonitering.utility.MetricCalOperand;
import com.monitoring.easysimplemonitering.utility.WarnSend;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerStatusMonitor {
    private final MetricsEndpoint metricsEndpoint;
    private static final String DISK_METRIC= "disk.free";
    private static final String DISK_METRIC_WARN_MSG= "disk 용량이 부족합니다 free : %s";
    private static final String CPU_USAGE_METRIC= "process.cpu.usage";
    private static final String CPU_USAGE_METRIC_WARN_MSG= "cpu 점유율이 높습니다 usage : %s";
    private static final String MEMORY_MAX_METRIC= "jvm.memory.max";
    private static final String MEMORY_USAGE_METRIC= "jvm.memory.used";
    private static final String MEMORY_USAGE_METRIC_WARN_MSG= "메모리 사용율이 높습니다 usage : %s";

    @Value("${easy.monitoring.disk.free.threshold:1073741824}")
    public double diskFreeThreshold;

    @Value("${easy.monitoring.cpu.usage.threshold:80}")
    public double cpuUsageThreshold;

    @Value("${easy.monitoring.memory.usage.threshold:80}")
    public double memoryUsageThreshold;

    @Nullable
    private final SimpleEmailService emailService;

    private static final String METRIC_RESULT_MSG_FORMAT = "metric %s : result = %s";
    public Map<String, CommonMetricInfo> commonMetricMap;



    @PostConstruct
    public void initVariables(){
        commonMetricMap = new HashMap<>();
        commonMetricMap.put(DISK_METRIC, new CommonMetricInfo(DISK_METRIC, diskFreeThreshold,
                DISK_METRIC_WARN_MSG,MetricCalOperand.SMALLER));
        commonMetricMap.put(CPU_USAGE_METRIC, new CommonMetricInfo(CPU_USAGE_METRIC,cpuUsageThreshold,
                CPU_USAGE_METRIC_WARN_MSG,MetricCalOperand.BIGGER_THEN));

        log.info("EasySimpleMonitoring Initial Complete");
    }


    public Map<String, ComplicateMetricInfo> complicateMetricInfoMap;
    {
        complicateMetricInfoMap = new HashMap<>();
        complicateMetricInfoMap.put(MEMORY_USAGE_METRIC, new ComplicateMetricInfo(MEMORY_USAGE_METRIC,MEMORY_MAX_METRIC,memoryUsageThreshold,MEMORY_USAGE_METRIC_WARN_MSG, MetricCalOperand.DIVIDED));
    }


    public ThreadLocal<HashMap<String, WarnSend>> warnSendHolder = ThreadLocal.withInitial(() -> {
        HashMap<String, WarnSend> map = new HashMap<>();
        commonMetricMap.keySet()
                .stream().forEach((key) ->
                        map.put(key,new WarnSend()));
        complicateMetricInfoMap.keySet()
                .stream().forEach((key) ->
                        map.put(key,new WarnSend()));
        return map;
    });

    public void commonMetricMonitoring(){
        //common의 경우
        commonMetricMap.keySet()
                .forEach((key) -> {
                    CommonMetricInfo metricInfo = commonMetricMap.get(key);
                    CommonMonitor commonMonitor = new CommonMonitor(metricsEndpoint, metricInfo);
                    if(commonMonitor.isOverThreshold() && warnSendHolder.get().get(key).isSendable()){
                        double result = commonMonitor.getMetricsResult();
                        sendAlter(metricInfo,result);
                    }
                });
    }


    public void complicateMetricMonitoring(){
        //메트릭 조합 모니터링 부분
        complicateMetricInfoMap.keySet()
                .forEach((key) -> {
                    ComplicateMetricInfo metricInfo = complicateMetricInfoMap.get(key);
                    ComplicateMonitor complicateMonitor = new ComplicateMonitor(metricsEndpoint, metricInfo);
                    if(complicateMonitor.isOverThreshold() && warnSendHolder.get().get(key).isSendable()){
                        double result = complicateMonitor.getMetricsResult();
                        sendAlter(metricInfo,result);
                    }
                });

    }

    @Scheduled(fixedRateString = "${easy.monitoring.send.duration:3600000}")
    public void serverMonitoring(){
        commonMetricMonitoring();
        complicateMetricMonitoring();
    }

    public void sendAlter(MetricInfo metricInfo,double result){
        if(emailService != null) {
            emailService.sendAlter(String.format(metricInfo.getWarnMsg(),result + ""));
        }
        log.info(String.format(METRIC_RESULT_MSG_FORMAT, metricInfo.getMetric(),result +""));
    }
}
