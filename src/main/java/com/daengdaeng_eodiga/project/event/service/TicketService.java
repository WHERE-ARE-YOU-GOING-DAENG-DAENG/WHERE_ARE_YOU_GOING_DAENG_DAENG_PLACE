package com.daengdaeng_eodiga.project.event.service;

import static com.daengdaeng_eodiga.project.event.service.EventManageService.*;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.daengdaeng_eodiga.project.Global.exception.EventTicketLimitException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {
	private final RedisTemplate<String, Object> redisTemplateObject;


	public void generateEventTicket(Integer userId,String key) {
		HashOperations<String, String, String> hashOperations = redisTemplateObject.opsForHash();
		if(activeEventParticipantLimit.containsKey(key)) {
			hashOperations.put(key, userId.toString(), LocalDateTime.now().toString());
		}else {
			throw new EventTicketLimitException();
		}
	}

	public boolean checkEventTicketLimitExceeded(String key) {
		HashOperations<String, String, String> hashOperations = redisTemplateObject.opsForHash();
		if(activeEventParticipantLimit.containsKey(key)&&hashOperations.size(key) >= activeEventParticipantLimit.get(key)) {
			activeEventParticipantLimit.remove(key);
			return true;
		}
		return false;
	}
}
