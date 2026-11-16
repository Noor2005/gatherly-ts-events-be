package com.techsisters.gatherly.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techsisters.gatherly.entity.Event;
import com.techsisters.gatherly.entity.EventRSVP;

@Repository
public interface EventRSVPRepository extends JpaRepository<EventRSVP, Long> {
    Optional<EventRSVP> findByEventAndUserEmail(Event event, String userEmail);

    List<EventRSVP> findAllByEvent_EventId(Long eventId);

    Page<EventRSVP> findByUserEmailAndRsvpStatus(String userEmail, boolean rsvpStatus, Pageable pageable);

    @Query("SELECT rsvp FROM EventRSVP rsvp WHERE rsvp.userEmail = :userEmail "
            + "AND rsvp.rsvpStatus = true "
            + "AND (rsvp.event.eventEndDateTime IS NULL OR rsvp.event.eventEndDateTime >= :now)")
    Page<EventRSVP> findUpcomingByUserEmail(
            @Param("userEmail") String userEmail, @Param("now") OffsetDateTime now, Pageable pageable);

    @Query("SELECT rsvp FROM EventRSVP rsvp WHERE rsvp.userEmail = :userEmail "
            + "AND rsvp.rsvpStatus = true AND rsvp.event.eventEndDateTime < :now")
    Page<EventRSVP> findPastByUserEmail(
            @Param("userEmail") String userEmail, @Param("now") OffsetDateTime now, Pageable pageable);

    List<EventRSVP> findByEventAndRsvpStatus(Event event, boolean rsvpStatus);

    // @Query("SELECT e FROM Event e WHERE e.id IN :eventIds")
    // List<Event> findByIds(@Param("eventIds") List<Long> eventIds);

}
