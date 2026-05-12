package com.bank.app.service;

import com.bank.app.entity.Event;
import com.bank.app.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event recordEvent(String type, String details) {
        Event event = new Event();
        event.setType(type);
        event.setDetails(details);
        event.setTimestamp(Instant.now());
        return eventRepository.save(event);
    }
}
