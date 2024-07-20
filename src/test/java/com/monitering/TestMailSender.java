package com.monitering;

import com.monitoring.easysimplemonitering.EasySimpleMonitoringModuleApplication;
import com.monitoring.easysimplemonitering.email.SimpleEmailService;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@TestPropertySource(locations = "classpath:/application-test.properties")
@SpringBootTest
@EnableAutoConfiguration
@SpringBootConfiguration
public class TestMailSender {

    @Autowired
    private SimpleEmailService emailService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SimpleEmailService simpleEmailService(JavaMailSender javaMailSender) {
            return new SimpleEmailService(javaMailSender);
        }
    }

//    @Autowired
//    public TestMailSender(JavaMailSenderImpl javaMailSender) {
//        System.out.println(javaMailSender.getHost() + "," + javaMailSender.getUsername());
//        this.emailService = new SimpleEmailService(javaMailSender);
//    }

    @Test
    public void 테스트() {
//        emailService.sendAlter("test");
    }




}


