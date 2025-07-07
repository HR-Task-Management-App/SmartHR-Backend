package com.hrms.backend.services.emailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class EmailServiceImpl implements EmailServiceInterface {
    @Autowired private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent)  {
        MimeMessage msg = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(msg);
        }
        catch (MessagingException ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
}
