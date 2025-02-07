
package com.monitoring.easysimplemonitering.email;

import com.monitoring.easysimplemonitering.utility.MonitoringError;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "easy.monitoring.mail.enable", havingValue = "true")
public class SimpleEmailService {

    private final JavaMailSender javaMailSender;

    @PostConstruct
    public void init(){
        log.info(String.format("SimpleEmailService Enable With From = %s To = %s",FROM,TO));
    }

    @Value("${easy.monitoring.mail.sender.name}")
    private String FROM;

    @Value("${easy.monitoring.mail.receiver.name}")
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
            System.out.println(FROM + "," + TO);
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
