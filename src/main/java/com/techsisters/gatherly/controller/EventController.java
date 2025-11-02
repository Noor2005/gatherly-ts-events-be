package com.techsisters.gatherly.controller;

import com.techsisters.gatherly.dto.EventDTO;
import com.techsisters.gatherly.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    @PostMapping
    public EventDTO createEvent(@RequestBody EventDTO eventDTO) {
        EventDTO event = eventService.createEvent(eventDTO);
        return event;
    }
}
