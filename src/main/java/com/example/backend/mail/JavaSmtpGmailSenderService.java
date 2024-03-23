package com.example.backend.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
public class JavaSmtpGmailSenderService {
    private final JavaMailSender emailSender;

    public Mono<Void> sendEmail(String toEmail, String subject, String body){
        return Mono.fromCallable(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
            return null;
        });
    }

    public Mono<Void> sendHtmlEmail() throws MessagingException {
        return Mono.fromCallable(() -> {

            MimeMessage message = emailSender.createMimeMessage();

            message.setFrom(new InternetAddress("sender@example.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, "recipient@example.com");
            message.setSubject("Test email from Spring");

            String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
                    "<p>It can contain <strong>HTML</strong> content.</p>";
            message.setContent(htmlContent, "text/html; charset=utf-8");

            emailSender.send(message);
            return null;
        });
    }
}
