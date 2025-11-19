package com.techsisters.gatherly.controller;

import com.techsisters.gatherly.dto.EventDTO;
import com.techsisters.gatherly.response.AllEventsResponse;
import com.techsisters.gatherly.response.EventDetailsResponse;
import com.techsisters.gatherly.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/all")
    public AllEventsResponse getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        AllEventsResponse response = new AllEventsResponse();
        if(events != null){
            response.setEvents(events);
            response.setMessage("Data returned successfully");
            response.setSuccess(true);
        }else{
            response.setSuccess(false);
            response.setMessage("No Events found");
        }
        return response;
    }

    @GetMapping("/id")
    public EventDetailsResponse getEventDetails(@RequestParam Long eventId){
        EventDetailsResponse response = new EventDetailsResponse();
        EventDTO dto = eventService.getEventDetails(eventId);
        if(dto != null){
            response.setDto(dto);
            response.setSuccess(true);
            response.setMessage("Data returned successfully");
        }else{
            response.setSuccess(false);
        }
        return response;
    }
}