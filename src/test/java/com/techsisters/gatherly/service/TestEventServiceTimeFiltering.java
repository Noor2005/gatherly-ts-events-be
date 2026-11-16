package com.techsisters.gatherly.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techsisters.gatherly.dto.EventDTO;
import com.techsisters.gatherly.mapper.EventMapper;
import com.techsisters.gatherly.repository.EventRSVPRepository;
import com.techsisters.gatherly.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class TestEventServiceTimeFiltering {
    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventRSVPRepository eventRSVPRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void filtersUpcomingRsvpsByEventEndTime() {
        when(eventRSVPRepository.findUpcomingByUserEmail(
                anyString(), any(OffsetDateTime.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        eventService.getUserRSVPs("member@example.com", 0, 3, EventDTO.ListType.UPCOMING);

        verify(eventRSVPRepository).findUpcomingByUserEmail(
                eq("member@example.com"), any(OffsetDateTime.class), any(Pageable.class));
        verify(eventRSVPRepository, never()).findPastByUserEmail(
                anyString(), any(OffsetDateTime.class), any(Pageable.class));
        verify(eventRepository, never()).findByEventIdIn(any());
    }

    @Test
    void filtersPastRsvpsByEventEndTime() {
        when(eventRSVPRepository.findPastByUserEmail(
                anyString(), any(OffsetDateTime.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        eventService.getUserRSVPs("member@example.com", 0, 3, EventDTO.ListType.PAST);

        verify(eventRSVPRepository).findPastByUserEmail(
                eq("member@example.com"), any(OffsetDateTime.class), any(Pageable.class));
        verify(eventRSVPRepository, never())
                .findUpcomingByUserEmail(anyString(), any(OffsetDateTime.class), any(Pageable.class));
        verify(eventRepository, never()).findByEventIdIn(any());
    }

    @Test
    void filtersUpcomingCreatedEventsByEventEndTime() {
        when(eventRepository.findUpcomingByCreatedBy(
                anyString(), any(OffsetDateTime.class), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(eventMapper.getEvents(List.of())).thenReturn(List.of());

        eventService.getAllUserCreatedEvents("member@example.com", 0, 3, EventDTO.ListType.UPCOMING);

        verify(eventRepository).findUpcomingByCreatedBy(
                eq("member@example.com"), any(OffsetDateTime.class), any(Pageable.class));
        verify(eventRepository, never()).findPastByCreatedBy(
                anyString(), any(OffsetDateTime.class), any(Pageable.class));
    }

    @Test
    void filtersPastCreatedEventsByEventEndTime() {
        when(eventRepository.findPastByCreatedBy(
                anyString(), any(OffsetDateTime.class), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(eventMapper.getEvents(List.of())).thenReturn(List.of());

        eventService.getAllUserCreatedEvents("member@example.com", 0, 3, EventDTO.ListType.PAST);

        verify(eventRepository).findPastByCreatedBy(
                eq("member@example.com"), any(OffsetDateTime.class), any(Pageable.class));
        verify(eventRepository, never()).findUpcomingByCreatedBy(
                anyString(), any(OffsetDateTime.class), any(Pageable.class));
    }
}
