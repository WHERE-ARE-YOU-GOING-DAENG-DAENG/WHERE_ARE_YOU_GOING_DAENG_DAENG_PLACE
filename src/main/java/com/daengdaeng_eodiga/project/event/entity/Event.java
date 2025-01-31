package com.daengdaeng_eodiga.project.event.entity;

import com.daengdaeng_eodiga.project.Global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Table(name = "event")
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_image", length = 700)
    private String eventImage;

    @Column(name = "event_description", length = 500)
    private String eventDescription;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "place_address")
    private String placeAddress;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private boolean active;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Builder
    public Event(String eventName, String eventImage, String eventDescription, String placeName, String placeAddress, LocalDateTime startDate, LocalDateTime endDate,Boolean active, int participantLimit) {
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.eventDescription = eventDescription;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.participantLimit = participantLimit;
    }
    public Event() {}
}