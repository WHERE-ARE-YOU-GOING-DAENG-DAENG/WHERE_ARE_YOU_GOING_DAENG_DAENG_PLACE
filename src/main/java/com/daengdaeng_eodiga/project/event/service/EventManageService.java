package com.daengdaeng_eodiga.project.event.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.event.dto.CreateEventRequest;
import com.daengdaeng_eodiga.project.event.entity.Event;
import com.daengdaeng_eodiga.project.event.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventManageService {
	private final EventRepository eventRepository;
	public static HashMap<String,Integer> activeEventParticipantLimit = new HashMap<>();

	public Event createEvent(CreateEventRequest createEventRequest) {
		Event event = Event.builder()
			.eventName(createEventRequest.getEventName())
			.eventImage(createEventRequest.getEventImage())
			.eventDescription(createEventRequest.getEventDescription())
			.placeName(createEventRequest.getPlaceName())
			.placeAddress(createEventRequest.getPlaceAddress())
			.startDate(createEventRequest.getStartDate())
			.endDate(createEventRequest.getEndDate())
			.active(false)
			.participantLimit(createEventRequest.getParticipantLimit())
			.build();
		return eventRepository.save(event);
	}

	public void startEvent(Event event,String key) {
		updateActive(event.getEventId(), true);
		activeEventParticipantLimit.put(key, event.getParticipantLimit()*3);
		log.info("Event started event : key : {} value :{}",key,activeEventParticipantLimit.get(key));
	}

	public void finishEvent(Event event,String key) {
		updateActive(event.getEventId(), false);
		activeEventParticipantLimit.remove(key);
	}

	public void updateActive(Integer eventId, boolean active) {
		Event event = eventRepository.findById(eventId).orElseThrow();
		event.setActive(active);
		eventRepository.save(event);
	}



}
