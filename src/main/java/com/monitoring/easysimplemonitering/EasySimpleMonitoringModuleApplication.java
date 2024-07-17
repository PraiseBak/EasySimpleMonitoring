package com.monitoring.easysimplemonitering;

import com.monitoring.easysimplemonitering.monitor.ServerStatusMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EasySimpleMonitoringModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasySimpleMonitoringModuleApplication.class, args);
    }

}
