package com.pettalk.review.controller;

import com.pettalk.argumentresolver.LoginMemberId;
import com.pettalk.review.dto.ReviewDto;
import com.pettalk.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@LoginMemberId @Positive Long memberId, @RequestBody ReviewDto.Post post) {
        try {
            reviewService.createReview(memberId, post);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reviews")
    public ResponseEntity<ReviewDto.petSitterReviews> getPetSitterReviews(@LoginMemberId Long memberId) {
        ReviewDto.petSitterReviews result = reviewService.getPetSitterReviews(memberId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
