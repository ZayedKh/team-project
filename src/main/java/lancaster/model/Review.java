package lancaster.model;

public class Review {
    private int reviewID;
    private int rating;
    private String author;
    private String title;
    private String description;
    private String room;
    private String timestamp;

    public Review(int reviewID, int rating, String author, String title, String description, String room) {
        this.reviewID = reviewID;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.description = description;
        this.room = room;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}