package com.techsisters.gatherly.service;

import com.techsisters.gatherly.dto.EventDTO;
import com.techsisters.gatherly.mapper.EventMapper;
import com.techsisters.gatherly.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public EventDTO createEvent(EventDTO eventDTO) {
        return null;
    }
}
