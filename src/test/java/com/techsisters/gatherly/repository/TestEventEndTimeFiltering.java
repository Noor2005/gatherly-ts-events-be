package com.techsisters.gatherly.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.techsisters.gatherly.dto.EventDTO;
import com.techsisters.gatherly.entity.Event;

@SpringBootTest
@Transactional
class TestEventEndTimeFiltering {
    @Autowired
    private EventRepository eventRepository;

    @Test
    void categorizesEventsByTheirEndTime() {
        OffsetDateTime now = OffsetDateTime.now();
        Event ongoing = createEvent("ongoing", now.minusMinutes(30), 60);
        Event ended = createEvent("ended", now.minusMinutes(90), 60);
        Event withoutDuration = createEvent("without-duration", now.minusHours(1), null);
        eventRepository.saveAll(List.of(ongoing, ended, withoutDuration));
        eventRepository.flush();

        List<Event> pastEvents = eventRepository.findAll(
                EventSpecification.filter(EventDTO.ListType.PAST, "event-end-filter-"),
                Pageable.unpaged()).getContent();
        List<Event> upcomingEvents = eventRepository.findAll(
                EventSpecification.filter(EventDTO.ListType.UPCOMING, "event-end-filter-"),
                Pageable.unpaged()).getContent();

        assertEquals(List.of(ended.getEventId()), pastEvents.stream().map(Event::getEventId).toList());
        assertTrue(upcomingEvents.stream().map(Event::getEventId).toList()
                .containsAll(List.of(ongoing.getEventId(), withoutDuration.getEventId())));
        assertEquals(2, upcomingEvents.size());
    }

    private Event createEvent(String suffix, OffsetDateTime eventDateTime, Integer duration) {
        Event event = new Event();
        event.setTitle("event-end-filter-" + suffix);
        event.setShortDescription("Event end-time filter test");
        event.setEventDateTime(eventDateTime);
        event.setDuration(duration);
        event.setEventHostName("Test host");
        return event;
    }
}
