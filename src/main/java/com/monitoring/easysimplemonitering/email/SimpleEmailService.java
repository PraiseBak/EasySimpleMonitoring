
package com.monitoring.easysimplemonitering.email;

import com.monitoring.easysimplemonitering.utility.MonitoringError;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleEmailService {
    private final JavaMailSender javaMailSender;

    @Value("${email.sender.name:easy-simple-monitoring@server.com}")
    private String FROM;

    @Value("${email.receiver.name:easy-simple-monitoring@server.com}")
    private String TO;



    public void sendEmail(String subject, String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(FROM);
            helper.setTo(TO);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.warn(MonitoringError.CANNOT_FIND_EMAIL);
        }
    }

    public void sendAlter(String content) {
        try{
            String subject = "monitoring result";
            sendEmail(subject,content);
        }catch (RuntimeException e){
            log.warn(MonitoringError.FAILED_SEND_EMAIL + " :" + e.getMessage());
        }

    }
}
