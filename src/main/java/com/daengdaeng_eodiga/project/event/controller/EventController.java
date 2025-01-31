package com.daengdaeng_eodiga.project.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.event.dto.CreateEventRequest;
import com.daengdaeng_eodiga.project.event.service.EventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/event")
@RequiredArgsConstructor
public class EventController {

	private final EventService eventService;

	@GetMapping("/ticket/{eventId}")
	public ResponseEntity<ApiResponse<?>> generateTicket(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,@PathVariable("eventId") Integer eventId) {
		Integer userId = customOAuth2User.getUserDTO() != null? customOAuth2User.getUserDTO().getUserid(): null;
		if(userId != null) {
			eventService.generateEventTicket(userId,eventId);
		}
		return ResponseEntity.ok(ApiResponse.success(null));
	}


	@PostMapping("/")
	public ResponseEntity<ApiResponse<?>> createEvent(@RequestBody CreateEventRequest createEventRequest) {
		eventService.createEvent(createEventRequest);
		return ResponseEntity.ok(ApiResponse.success(null));
	}
}
