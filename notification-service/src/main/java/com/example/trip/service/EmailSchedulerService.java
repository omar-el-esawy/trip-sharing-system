package com.example.trip.service;

import com.example.trip.payload.EmailRequest;
import com.example.trip.payload.EmailResponse;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class EmailSchedulerService {

    private final String smtpHost;
    private final int smtpPort;
    private final String username;
    private final String password;

    public EmailSchedulerService() {
        // Load from application.yml
        String tempSmtpHost = null;
        int tempSmtpPort = 0;
        String tempUsername = null;
        String tempPassword = null;
        try (InputStream in = EmailSchedulerService.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                Map<String, Object> obj = yaml.load(in);
                Map<String, Object> mail = (Map<String, Object>) obj.get("mail");
                tempSmtpHost = (String) mail.get("smtpHost");
                tempSmtpPort = (Integer) mail.get("smtpPort");
                tempUsername = (String) mail.get("username");
                tempPassword = (String) mail.get("password");
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Failed to load mail configuration from application.yml", e);
        }
        this.smtpHost = tempSmtpHost;
        this.smtpPort = tempSmtpPort;
        this.username = tempUsername;
        this.password = tempPassword;

        System.out.println("EmailSchedulerService initialized with SMTP host: " + smtpHost + ", port: " + smtpPort);
        System.out.println("Username: " + username);
        System.out.println("Password: " + (password != null && !password.isEmpty() ? "******" : "not set"));

    }

    public EmailSchedulerService(String smtpHost, int smtpPort, String username, String password) {

        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.username = username;
        this.password = password;

    }

    public EmailResponse sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password);
            }
        });


        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
        return new EmailResponse(true, "Email Sent Successfully");

    }
}
