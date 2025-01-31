package com.daengdaeng_eodiga.project.event.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventRequest {

	private String eventName;
	private String eventDescription;
	private String eventImage;
	private String placeName;
	private String placeAddress;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private Integer participantLimit;
}