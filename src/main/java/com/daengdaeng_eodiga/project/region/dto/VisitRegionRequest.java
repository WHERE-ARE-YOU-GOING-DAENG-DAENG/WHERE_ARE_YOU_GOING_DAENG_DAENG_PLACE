package com.daengdaeng_eodiga.project.region.dto;

import java.time.LocalDate;

public record VisitRegionRequest(String city, String cityDetail, Integer userId, Integer reviewId, LocalDate visitDate) {
}