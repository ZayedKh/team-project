package com.lancaster.app.api;

import java.util.List;
import com.lancaster.app.model.*;

public interface ReviewAPI {

    // This method returns all the reviews for a particular event by searching for its eventId
    List<Review> fetchReviewsForEvent(int eventId);

    // This method allows the user to respond to a review
    void respondToReview(int reviewId, String response);
}