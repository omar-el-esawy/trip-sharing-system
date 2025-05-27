package com.example.trip.service;

import com.example.trip.payload.EmailResponse;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.mail.Transport;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailSchedulerServiceTest {

    private EmailSchedulerService emailService;

    @BeforeEach
    void setUp() {
        // Use dummy SMTP config for testing
        emailService = new EmailSchedulerService("smtp.gmail.com", 587, "oesawy610@gmail.com", "ojxrtnqigjdhushn");
    }

    @Test
    void testSendEmail_Success() throws Exception {
        // Mock static Transport.send
        Transport transportMock = mock(Transport.class);
        Mockito.mockStatic(Transport.class).when(() -> Transport.send(any(javax.mail.Message.class))).then(invocation -> null);

        EmailResponse response = emailService.sendEmail(
                "omar.m.elesawy2002@gmail.com\n",
                "Test Subject",
                "Test Body"
        );

        assertTrue(response.isSuccess());
        assertEquals("Email Sent Successfully", response.getMessage());
    }

    @Test
    void testSendEmail_Failure() throws Exception {
        // Mock static Transport.send to throw exception
        Mockito.mockStatic(Transport.class).when(() -> Transport.send(any(javax.mail.Message.class)))
                .thenThrow(new jakarta.mail.MessagingException("SMTP error"));

        EmailResponse response = emailService.sendEmail(
                "to@example.com",
                "Test Subject",
                "Test Body"
        );

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Error sending email"));
    }
}
