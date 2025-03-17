package classes;


/**
 * Review entity representing event reviews.
 */
public class Review {
    private int reviewID;
    private int rating;
    private String author;
    private String title;
    private String description;

    public Review(int reviewID, int rating, String author, String title, String description) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.description = description;
    }
}