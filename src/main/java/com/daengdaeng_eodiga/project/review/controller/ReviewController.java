package com.daengdaeng_eodiga.project.review.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.Global.enums.OrderType;
import com.daengdaeng_eodiga.project.region.dto.VisitRegionRequest;
import com.daengdaeng_eodiga.project.review.dto.ReviewDto;
import com.daengdaeng_eodiga.project.review.dto.ReviewRegisterRequest;
import com.daengdaeng_eodiga.project.review.dto.ReviewsResponse;
import com.daengdaeng_eodiga.project.review.dto.VisitRegionResponse;
import com.daengdaeng_eodiga.project.review.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class ReviewController {

	private final ReviewService reviewService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping("/review")
	public ResponseEntity<ApiResponse<ReviewDto>> registerReview(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @Valid @RequestBody ReviewRegisterRequest request) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		ReviewDto response = reviewService.registerReview(request, userId);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@DeleteMapping("/review/{reviewId}")
	public ResponseEntity<ApiResponse<?>> deleteReview(@Min(1) @PathVariable int reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.ok(ApiResponse.success(null));
	}

	@GetMapping("/reviews/place/{placeId}/{orderType}")
	public ResponseEntity<ApiResponse<ReviewsResponse>> fetchPlaceReviews(@PathVariable int placeId, @PathVariable OrderType orderType, @Min(0) @RequestParam int page, @Min(1) @RequestParam int size) {
		ReviewsResponse response = reviewService.fetchPlaceReviews(placeId,page,size, orderType);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@GetMapping("/reviews/place/no-offset/{placeId}/{orderType}")
	public ResponseEntity<ApiResponse<ReviewsResponse>> fetchPlaceReviews2(@PathVariable int placeId, @PathVariable OrderType orderType, @RequestParam int lastReviewId, @RequestParam int lastScore,@Min(1) @RequestParam int size) {
		ReviewsResponse response = reviewService.fetchPlaceReviewsByNoOffset(placeId,orderType,lastReviewId,lastScore,size);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@GetMapping("/reviews/user")
	public ResponseEntity<ApiResponse<ReviewsResponse>> fetchUserReviews(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @Min(0) @RequestParam int page, @Min(1)@RequestParam int size) {
		int userId = customOAuth2User.getUserDTO().getUserid();
		ReviewsResponse response = reviewService.fetchUserReviews(userId,page,size);
		return ResponseEntity.ok(ApiResponse.success(response));
	}

	@KafkaListener(topics = "add_visitRegion_transaction_response", groupId = "region_visit_transaction_response")
	public void visitRegionResponseListener(ConsumerRecord data) throws JsonProcessingException {
		VisitRegionResponse response = objectMapper.readValue((String)data.value(), VisitRegionResponse.class);
		if(!response.visitRegionStatus()) {
			reviewService.deleteReviewForTransaction(response.reviewId());
		}
	}
}
