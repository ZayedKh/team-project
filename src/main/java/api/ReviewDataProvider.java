package api;
import model.Review;
import java.util.List;

public interface ReviewDataProvider {
    // Method to get all reviews
    List<Review> getAllReviews();
    // Method to get a review by ID
    Review getReviewByID(String reviewId);
    // Method to add a review
    boolean addReview(Review review);
    // Method to delete a review
    boolean deleteReview(String reviewId);
    // Method to update a review
    boolean updateReview(String reviewId, Review review);
}