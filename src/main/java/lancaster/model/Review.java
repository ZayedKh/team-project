package lancaster.model;

/**
 * Represents a review submitted for a show or event in a venue.
 * <p>
 * This class encapsulates the details of a review, including its unique identifier, rating, author information,
 * review title, description, associated room, review type, and the show name. A timestamp can also be set to record
 * when the review was submitted.
 * </p>
 */
public class Review {
    private int id;
    private int rating;
    private String author;
    private String title;
    private String description;
    private String room;
    private String timestamp;
    private String reviewType;
    private String showName;

    /**
     * Constructs a new {@code Review} instance with the provided details.
     *
     * @param id          the unique identifier for the review.
     * @param rating      the numeric rating assigned in the review.
     * @param author      the author of the review.
     * @param title       the title of the review.
     * @param description a detailed description of the review.
     * @param room        the room associated with the review.
     * @param reviewType  the type or category of the review.
     * @param showName    the name of the show or event being reviewed.
     */
    public Review(int id, int rating, String author, String title, String description, String room, String reviewType, String showName) {
        this.id = id;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.description = description;
        this.room = room;
        this.reviewType = reviewType;
        this.showName = showName;
    }

    /**
     * Returns the unique identifier of the review.
     *
     * @return the review ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the rating given in the review.
     *
     * @return the review rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Returns the author of the review.
     *
     * @return the review author's name.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the title of the review.
     *
     * @return the review title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the detailed description of the review.
     *
     * @return the review description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the room associated with the review.
     *
     * @return the room name.
     */
    public String getRoom() {
        return room;
    }

    /**
     * Returns the timestamp when the review was submitted.
     *
     * @return the review timestamp.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp for the review submission.
     *
     * @param timestamp the timestamp to set.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns the type of the review.
     *
     * @return the review type.
     */
    public String getReviewType() {
        return reviewType;
    }

    /**
     * Returns the name of the show or event being reviewed.
     *
     * @return the show name.
     */
    public String getShowName() {
        return showName;
    }
}
