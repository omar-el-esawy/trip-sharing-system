package com.example.trip.service;

import com.example.trip.dto.EmailResponse;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

import org.example.YamlInjector;
import org.example.YamlValue;
import org.yaml.snakeyaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(EmailSchedulerService.class);

    @YamlValue(key = "mail.smtpHost")
    private String smtpHost;
    @YamlValue(key = "mail.smtpPort")
    private int smtpPort;
    @YamlValue(key = "mail.username")
    private String username;
    @YamlValue(key = "mail.password")
    private String password;

    public EmailSchedulerService() {
        YamlInjector.inject(this);

        logger.info("EmailSchedulerService initialized with SMTP host: {}, port: {}", smtpHost, smtpPort);
        logger.info("Username: {}", username);
        logger.info("Password: {}", (password != null && !password.isEmpty() ? "******" : "not set"));
    }

    public EmailSchedulerService(String smtpHost, int smtpPort, String username, String password) {
        YamlInjector.inject(this);

        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;
    }

    public EmailResponse sendEmail(String to, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);  // This will be mocked in the test
            return new EmailResponse(true, "Email Sent Successfully");

        } catch (MessagingException e) {
            return new EmailResponse(false, "Error sending email: " + e.getMessage());
        }
    }

}
