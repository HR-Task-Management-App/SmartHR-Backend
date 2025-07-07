package com.hrms.backend.services.emailService;


public interface EmailServiceInterface {

    void sendHtmlEmail(String to, String subject, String htmlContent);
}
