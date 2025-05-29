package com.example.trip.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class EmailRequest {

    private String to;
    private String subject;
    private String body;
    private LocalDateTime dateTime;
    private ZoneId timeZone;

    public EmailRequest() {}

    public EmailRequest(String to, String subject, String body, LocalDateTime dateTime, ZoneId timeZone) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.dateTime = dateTime;
        this.timeZone = timeZone;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
