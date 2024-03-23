package com.example.backend.mail;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final JavaSmtpGmailSenderService mailSenderService;

    public EmailController(JavaSmtpGmailSenderService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<String>> sendEmail(@RequestBody String toEmail) {
        EmailRequest emailRequest =  new EmailRequest();
        emailRequest.setToEmail(toEmail);
        return mailSenderService.sendEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getBody())
                .subscribeOn(Schedulers.boundedElastic())
                .then(Mono.just(ResponseEntity.ok("Email sent successfully")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in sending email: " + e.getMessage())));
    }

    @Data
    @NoArgsConstructor
    public static class EmailRequest {
        private String toEmail;
        private String subject = "Subject";
        private String body = "Body";
    }
}

