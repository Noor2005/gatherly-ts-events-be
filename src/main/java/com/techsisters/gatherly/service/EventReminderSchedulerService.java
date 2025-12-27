package com.techsisters.gatherly.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.resend.core.exception.ResendException;
import com.techsisters.gatherly.entity.Event;
import com.techsisters.gatherly.entity.EventRSVP;
import com.techsisters.gatherly.entity.User;
import com.techsisters.gatherly.repository.EventRSVPRepository;
import com.techsisters.gatherly.repository.EventRepository;
import com.techsisters.gatherly.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReminderSchedulerService {

    private final EventRepository eventRepository;
    private final EventRSVPRepository eventRSVPRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Run every 5 minutes
    @Scheduled(cron = "0 */5 * * * *")
    public void sendEventReminders() {
        log.info("Running event reminder scheduler...");

        try {
            OffsetDateTime now = OffsetDateTime.now();
            OffsetDateTime thirtyMinutesFromNow = now.plusMinutes(30);
            OffsetDateTime thirtyFiveMinutesFromNow = now.plusMinutes(35);

            // Find events starting in the next 30-35 minutes
            List<Event> upcomingEvents = eventRepository.findAll().stream()
                    .filter(event -> {
                        OffsetDateTime eventTime = event.getEventDateTime();
                        return eventTime.isAfter(thirtyMinutesFromNow) &&
                                eventTime.isBefore(thirtyFiveMinutesFromNow);
                    })
                    .toList();

            log.info("Found {} events starting in 30-35 minutes", upcomingEvents.size());

            for (Event event : upcomingEvents) {
                sendRemindersForEvent(event);
            }

        } catch (Exception e) {
            log.error("Error in event reminder scheduler: {}", e.getMessage(), e);
        }
    }

    private void sendRemindersForEvent(Event event) {
        log.info("Sending reminders for event: {} (ID: {})", event.getTitle(), event.getEventId());

        // Get all RSVPs for this event where status is true (attending)
        List<EventRSVP> rsvps = eventRSVPRepository.findByEventAndRsvpStatus(event, true);

        log.info("Found {} confirmed RSVPs for event: {}", rsvps.size(), event.getTitle());

        for (EventRSVP rsvp : rsvps) {
            try {
                // Get user details
                User user = userRepository.findByEmail(rsvp.getUserEmail()).orElse(null);
                if (user == null) {
                    log.warn("User not found for email: {}", rsvp.getUserEmail());
                    continue;
                }
                // Format event date time for display
                String formattedDateTime = formatEventDateTime(event.getEventDateTime(), event.getTimezone());
                // Send reminder email
                emailService.sendEventReminderEmail(
                        user.getEmail(),
                        user.getName(),
                        event.getTitle(),
                        event.getShortDescription(),
                        formattedDateTime,
                        event.getEventType(),
                        event.getEventLocation(),
                        event.getEventLink(),
                        event.getDuration()
                );

                log.info("Sent reminder email to {} for event: {}", user.getEmail(), event.getTitle());

            } catch (MessagingException | ResendException e) {
                log.error("Failed to send reminder email to {} for event {}: {}",
                        rsvp.getUserEmail(), event.getTitle(), e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error sending reminder to {}: {}",
                        rsvp.getUserEmail(), e.getMessage(), e);
            }
        }
    }

    private String formatEventDateTime(OffsetDateTime eventDateTime, String timezone) {
        try {
            ZoneId zoneId = timezone != null ? ZoneId.of(timezone) : ZoneId.systemDefault();
            LocalDateTime localDateTime = eventDateTime.atZoneSameInstant(zoneId).toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a");
            return localDateTime.format(formatter);
        } catch (Exception e) {
            log.warn("Error formatting date time, using default: {}", e.getMessage());
            return eventDateTime.toString();
        }
    }
}