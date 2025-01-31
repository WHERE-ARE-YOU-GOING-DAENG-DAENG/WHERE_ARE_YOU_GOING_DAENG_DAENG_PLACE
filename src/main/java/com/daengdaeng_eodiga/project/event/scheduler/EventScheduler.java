package com.daengdaeng_eodiga.project.event.scheduler;

import static com.daengdaeng_eodiga.project.event.service.EventManageService.*;
import static com.daengdaeng_eodiga.project.event.service.EventService.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.daengdaeng_eodiga.project.event.entity.Event;
import com.daengdaeng_eodiga.project.event.service.EventManageService;
import com.daengdaeng_eodiga.project.event.service.TicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventScheduler {

	private final TaskScheduler taskScheduler;
	private Map<String, ScheduledFuture<?>> periodicTasks = new ConcurrentHashMap<>();
	private final EventManageService eventManageService;
	private final TicketService ticketService;

	public void createAddEventScheduler(Event event,String key) {
		Instant instant = toInstant(event.getStartDate().minusSeconds(1));
		taskScheduler.schedule(() -> {
			eventManageService.startEvent(event, key);
			ScheduledFuture<?> scheduledPeriodicTask = taskScheduler.scheduleAtFixedRate(() -> {
				boolean isExceeded = ticketService.checkEventTicketLimitExceeded(key);
				if(isExceeded) {
					ScheduledFuture<?> checkLimitExceedScheduledFuture = periodicTasks.remove(key);
					if (checkLimitExceedScheduledFuture != null) {
						checkLimitExceedScheduledFuture.cancel(false);
					}
					log.info("Event [{}] 종료됨: 티켓 제한 초과", key);
				}
				}, Duration.ofMillis(1000));
			periodicTasks.put(key, scheduledPeriodicTask);
			log.info("Event started : {}",key);
		}, instant);
	}

	public void createDeleteEventScheduler(Event event,String key) {
		taskScheduler.schedule(() -> {
			eventManageService.finishEvent(event, key);
			if(periodicTasks.containsKey(key)){
				periodicTasks.get(key).cancel(false);
			}
			log.info("Event finished : {}",key);
		}, toInstant(event.getEndDate()));
	}

	private Instant toInstant(LocalDateTime localDateTime) {
		return localDateTime.toInstant(ZoneId.systemDefault().getRules().getOffset(localDateTime));
	}



}
