package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReviewByIds() {
        // Arrange
        List<String> reviewIds = List.of("review1", "review2");

        Review review1 = new Review(
                "review1",
                25.0,
                "I've already bought it, my Children love it",
                "user1",
                "User One",
                4.5,
                new Date()
        );
        Review review2 = new Review(
                "review2",
                30.0,
                "Nice atmosphere",
                "user2",
                "User Two",
                4.0,
                new Date()
        );

        when(reviewRepository.findAllById(reviewIds)).thenReturn(List.of(review1, review2));

        // Act
        List<Review> result = reviewService.getReviewByIds(reviewIds);

        // Assert
        assertEquals(List.of(review1, review2), result);
        verify(reviewRepository).findAllById(reviewIds);
    }

    @Test
    void testGetReviewByIdsWithSomeMissingReviews() {
        // Arrange
        List<String> reviewIds = List.of("review1", "review3");

        Review review1 = new Review(
                "review1",
                25.0,
                "I've already bought it, my Children love it",
                "user1",
                "User One",
                4.5,
                new Date()
        );

        when(reviewRepository.findAllById(reviewIds)).thenReturn(List.of(review1));

        // Act
        List<Review> result = reviewService.getReviewByIds(reviewIds);

        // Assert
        assertEquals(List.of(review1), result);
        verify(reviewRepository).findAllById(reviewIds);
    }

    @Test
    void testGetReviewByIdsWithMissingReviews() {
        // Arrange
        List<String> reviewIds = List.of("review3");

        Review review1 = new Review(
                "review1",
                25.0,
                "I've already bought it, my Children love it",
                "user1",
                "User One",
                4.5,
                new Date()
        );

        when(reviewRepository.findAllById(reviewIds)).thenReturn(List.of());

        // Act
        List<Review> result = reviewService.getReviewByIds(reviewIds);

        // Assert
        assertEquals(List.of(), result);
        verify(reviewRepository).findAllById(reviewIds);
    }

}
