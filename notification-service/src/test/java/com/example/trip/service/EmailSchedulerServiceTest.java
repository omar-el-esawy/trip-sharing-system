package com.example.trip.service;

import com.example.trip.dto.EmailResponse;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import jakarta.mail.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class EmailSchedulerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(EmailSchedulerServiceTest.class);
    private EmailSchedulerService emailService;

    @BeforeEach
    void setUp() {
        // Use dummy SMTP config for testing
        emailService = new EmailSchedulerService("smtp.gmail.com", 587, "oesawy610@gmail.com", "ojxrtnqigjdhushn");
    }

    @Test
    void testSendEmail_Success() throws Exception {
        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenAnswer(invocation -> null);   // no exception → success

            EmailResponse response = emailService.sendEmail(
                    "omar.m.elesawy2002@gmail.com",
                    "Test Subject",
                    "Test Body"
            );

            assertTrue(response.isSuccess());
            assertEquals("Email Sent Successfully", response.getMessage());
        }
    }

    @Test
    void testSendEmail_Failure() {
        try (MockedStatic<Transport> transportMock = Mockito.mockStatic(Transport.class)) {
            transportMock.when(() -> Transport.send(any(Message.class)))
                    .thenThrow(new MessagingException("SMTP error"));

            EmailResponse response = emailService.sendEmail(
                    "to@example.com",
                    "Test Subject",
                    "Test Body"
            );

            assertFalse(response.isSuccess());
            assertTrue(response.getMessage().contains("Error sending email"));
        }
    }

    @Test
    void testDataRetrieval() throws Exception {
        EmailSchedulerService emailService = new EmailSchedulerService();

        var smtpHostField = EmailSchedulerService.class.getDeclaredField("smtpHost");
        smtpHostField.setAccessible(true);
        assertEquals("smtp.gmail.com", smtpHostField.get(emailService));

        var smtpPortField = EmailSchedulerService.class.getDeclaredField("smtpPort");
        smtpPortField.setAccessible(true);
        assertEquals(587, smtpPortField.get(emailService));

        var usernameField = EmailSchedulerService.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        assertEquals("oesawy610@gmail.com", usernameField.get(emailService));

        var passwordField = EmailSchedulerService.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        assertEquals("ojxrtnqigjdhushn", passwordField.get(emailService));
    }

}
