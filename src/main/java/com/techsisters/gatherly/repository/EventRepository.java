package com.techsisters.gatherly.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techsisters.gatherly.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByEventIdIn(List<Long> eventIds);

    Page<Event> findByCreatedBy(String createdBy, Pageable pageable);

    @Query("SELECT event FROM Event event WHERE event.createdBy = :createdBy "
            + "AND (event.eventEndDateTime IS NULL OR event.eventEndDateTime >= :now)")
    Page<Event> findUpcomingByCreatedBy(
            @Param("createdBy") String createdBy, @Param("now") OffsetDateTime now, Pageable pageable);

    @Query("SELECT event FROM Event event WHERE event.createdBy = :createdBy "
            + "AND event.eventEndDateTime < :now")
    Page<Event> findPastByCreatedBy(
            @Param("createdBy") String createdBy, @Param("now") OffsetDateTime now, Pageable pageable);

    List<Event> findByEventDateTimeBetweenAndReminderSentFalse(
            OffsetDateTime start, OffsetDateTime end);

}
