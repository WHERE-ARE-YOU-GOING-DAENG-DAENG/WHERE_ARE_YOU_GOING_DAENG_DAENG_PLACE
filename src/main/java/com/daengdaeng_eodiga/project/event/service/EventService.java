package com.daengdaeng_eodiga.project.event.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.EventTicketLimitException;
import com.daengdaeng_eodiga.project.event.dto.CreateEventRequest;
import com.daengdaeng_eodiga.project.event.entity.Event;
import com.daengdaeng_eodiga.project.event.scheduler.EventScheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
	private final EventManageService eventManageService;
	private final TicketService ticketService;
	private final EventScheduler eventScheduler;

	public void createEvent(CreateEventRequest createEventRequest) {
		Event event = eventManageService.createEvent(createEventRequest);
		String key = "event:ticket:" + event.getEventId() + ":" + LocalDate.from(event.getStartDate());
		createEventScheduler(event, key);
	}

	private void createEventScheduler(Event event, String key) {
		eventScheduler.createAddEventScheduler(event, key);
		eventScheduler.createDeleteEventScheduler(event, key);
	}

	public void generateEventTicket(Integer userId,Integer eventId) {
		String key = "event:ticket:" + eventId + ":" + LocalDate.now();
		ticketService.generateEventTicket(userId, key);
	}


}
