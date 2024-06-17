package it.cgmconsulting.msrating.controller;

import it.cgmconsulting.msrating.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/v3/{postId}/{rate}")
    @CacheEvict(value = "avg-rating", key = "{#postId}")
    public ResponseEntity<?> rate(
            @PathVariable @Min(1) int postId,
            @PathVariable @Min(1) @Max(5) byte rate,
            @RequestHeader("userId") @Min(1) int userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.rate(postId,userId,rate));
    }

    @GetMapping("/v99/{postId}")
    @Cacheable(value = "avg-rating", key = "{#postId}", unless = "#result == null")
    public ResponseEntity<?> getAvg(@PathVariable @Min(1) int postId){
        double avg = ratingService.getAvg(postId);
        log.info("### la media per il post {} vale {} ###", postId, avg);
        return ResponseEntity.ok(avg);
    }
}
