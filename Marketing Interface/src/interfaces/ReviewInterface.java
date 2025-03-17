package interfaces;

import classes.Review;

import java.util.List;

public interface ReviewInterface {
    List<Review> fetchReviewsFromAPI();
    void displayReviewSummary();
}