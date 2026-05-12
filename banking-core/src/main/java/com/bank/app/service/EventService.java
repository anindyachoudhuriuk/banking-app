package com.bank.app.service;

import com.bank.app.entity.Event;
import com.bank.app.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event recordEvent(String type, String details) {
        return saveEvent(type, details);
    }

    public Event recordException(String type, String details, Throwable throwable) {
        StringBuilder fullDetails = new StringBuilder();
        if (details != null && !details.isBlank()) {
            fullDetails.append(details).append(" | ");
        }
        fullDetails.append("exception=")
                .append(throwable.getClass().getSimpleName())
                .append(": ")
                .append(throwable.getMessage());

        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        fullDetails.append("\n").append(stringWriter.toString());

        return saveEvent(type, fullDetails.toString());
    }

    private Event saveEvent(String type, String details) {
        Event event = new Event();
        event.setType(type);
        event.setDetails(details);
        event.setTimestamp(Instant.now());
        return eventRepository.save(event);
    }
}
