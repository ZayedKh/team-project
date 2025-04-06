package lancaster.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lancaster.model.Review;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ReviewController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox centerVBox;

    @FXML
    private GridPane reviewsGrid;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Button addReviewButton;

    @FXML
    private VBox addReviewForm;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ComboBox<String> roomComboBox;

    @FXML
    private ComboBox<String> reviewTypeComboBox;

    @FXML
    private VBox showNameContainer;

    @FXML
    private TextField showNameField;

    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private Button submitReviewButton;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private Label totalReviewsLabel;

    private ObservableList<Review> reviews;
    private double scaleFactor = 1.0;
    private HBox reviewsHeader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reviews = FXCollections.observableArrayList();
        reviewsHeader = (HBox) centerVBox.getChildren().get(0);
        initializeSampleData();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);
        sortComboBox.getItems().addAll("Most Recent", "Highest Rated", "Lowest Rated");
        sortComboBox.setValue("Most Recent");
        roomComboBox.getItems().addAll(
                "The Green Room",
                "Brontë Boardroom",
                "Dickens Den",
                "Poe Parlor",
                "Globe Room",
                "Chekhov Chamber"
        );
        reviewTypeComboBox.getItems().addAll("Show", "Venue", "Both");
        addReviewForm.setVisible(false);
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(reviewsHeader, reviewsGrid);
        populateReviewsGrid();
        updateSummary();
        sortComboBox.setOnAction(event -> {
            String selectedSort = sortComboBox.getValue();
            if (selectedSort.equals("Highest Rated")) {
                reviews.sort((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()));
            } else if (selectedSort.equals("Lowest Rated")) {
                reviews.sort((r1, r2) -> Integer.compare(r1.getRating(), r2.getRating()));
            } else {
                reviews.sort((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp()));
            }
            populateReviewsGrid();
        });
        mainBorderPane.setOnScroll(this::handleScroll);
    }

    private void initializeSampleData() {
        LocalDateTime baseTime = LocalDateTime.now();
        reviews.add(new Review(3, 5, "Irving Wuckert V", "", "We hosted a local theatre production here, and the venue was just the right size. Great lighting and sound. Will be booking again!", "Theatre", "Show", "Theatre Production"));
        reviews.add(new Review(6, 5, "Tobias Chen", "", "The sound quality in the main hall is exceptional, making it perfect for both small gigs and large performances. We’ve never had a bad experience here!", "Main Hall", "Both", "Concert Series"));
        reviews.add(new Review(7, 5, "Dorian D’Amore", "", "The outdoor space was perfect for our summer wedding. Beautifully landscaped and well-kept. The sunset view was breathtaking!", "Outdoor Space", "Venue", null));
        reviews.add(new Review(8, 5, "Maya Patel", "", "I love the variety of rooms available. Whether you need a small studio for practice or a larger space for a workshop, they have it all covered!", "Various Rooms", "Venue", null));
        reviews.add(new Review(9, 4, "Alice Johnson", "", "Great venue for our event! The staff were helpful, but the parking was a bit tricky.", "Main Hall", "Venue", null));
        reviews.add(new Review(10, 5, "Bob Smith", "", "Amazing experience! The acoustics in the main hall were phenomenal.", "Main Hall", "Venue", null));
        reviews.add(new Review(11, 3, "Charlie Brown", "", "The event space was nice, but the catering options were limited.", "Catering", "Venue", null));
        reviews.add(new Review(12, 5, "Diana Prince", "", "Perfect spot for our rehearsal. Will definitely book again!", "Rehearsal Room", "Venue", null));
        reviews.add(new Review(13, 4, "Eve Adams", "", "The venue was great, but the lighting could be improved.", "Theatre", "Show", "Lighting Show"));
        reviews.add(new Review(14, 5, "Frank Wilson", "", "Fantastic experience! The staff were amazing.", "General", "Venue", null));
        reviews.add(new Review(15, 5, "Grace Lee", "", "The main hall was perfect for our concert. Highly recommend!", "Main Hall", "Both", "Spring Concert"));
        reviews.add(new Review(16, 4, "Henry Davis", "", "Good venue, but the booking process was a bit slow.", "Rehearsal Room", "Venue", null));
        reviews.add(new Review(17, 5, "Isabella Clark", "", "Amazing venue! The staff were incredibly helpful.", "Main Hall", "Venue", null));
        reviews.add(new Review(18, 4, "James Brown", "", "Great space, but the sound system could use an upgrade.", "Theatre", "Show", "Sound Show"));
        reviews.add(new Review(19, 5, "Kelly Green", "", "The catering was top-notch! Our guests loved the food.", "Catering", "Venue", null));
        reviews.add(new Review(20, 5, "Liam Harris", "", "Perfect for our event. Will book again!", "Main Hall", "Venue", null));
        reviews.add(new Review(21, 3, "Not That Good", "", "infhdsjpdjiy naposvn", "Main Hall", "Venue", null));

        for (int i = 0; i < reviews.size(); i++) {
            LocalDateTime reviewTime = baseTime.minusMinutes(i * 10);
            String timestamp = reviewTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            reviews.get(i).setTimestamp(timestamp);
        }
    }

    private void populateReviewsGrid() {
        reviewsGrid.getChildren().clear();
        int row = 0;
        int col = 0;

        for (Review review : reviews) {
            VBox reviewCard = new VBox(5);
            reviewCard.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #4CAF50; -fx-border-width: 1; -fx-border-radius: 5;");
            reviewCard.setPrefWidth(595);
            reviewCard.setMinHeight(200);

            Label authorLabel = new Label(review.getAuthor());
            authorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

            HBox ratingBox = new HBox(2);
            for (int i = 0; i < review.getRating(); i++) {
                Label star = new Label("★");
                star.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
                ratingBox.getChildren().add(star);
            }

            Label descriptionLabel = new Label(review.getDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.setStyle("-fx-text-fill: black;");

            Label reviewTypeLabel = new Label("Review Type: " + review.getReviewType());
            reviewTypeLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");

            Label showNameLabel = new Label();
            if (review.getShowName() != null && !review.getShowName().isEmpty()) {
                showNameLabel.setText("Show: " + review.getShowName());
                showNameLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");
            }

            Label timestampLabel = new Label("Submitted: " + review.getTimestamp());
            timestampLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");


            if (review.getShowName() != null && !review.getShowName().isEmpty()) {
                reviewCard.getChildren().addAll(authorLabel, ratingBox, descriptionLabel, reviewTypeLabel, showNameLabel, timestampLabel);
            } else {
                reviewCard.getChildren().addAll(authorLabel, ratingBox, descriptionLabel, reviewTypeLabel, timestampLabel);
            }
            reviewsGrid.add(reviewCard, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    private void updateSummary() {
        int totalReviews = reviews.size();
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        totalReviewsLabel.setText(totalReviews + " Total Reviews");
        averageRatingLabel.setText(String.format("%.1f", averageRating));
    }

    @FXML
    private void toggleAddReviewForm() {
        boolean willBeVisible = !addReviewForm.isVisible();
        addReviewForm.setVisible(willBeVisible);

        centerVBox.getChildren().clear();
        if (willBeVisible) {
            centerVBox.getChildren().addAll(addReviewForm, reviewsHeader, reviewsGrid);
        } else {
            centerVBox.getChildren().addAll(reviewsHeader, reviewsGrid);
        }

        if (!willBeVisible) {
            titleField.clear();
            authorField.clear();
            descriptionArea.clear();
            roomComboBox.getSelectionModel().clearSelection();
            reviewTypeComboBox.getSelectionModel().clearSelection();
            showNameField.clear();
            showNameContainer.setVisible(false);
            showNameContainer.setManaged(false);
            ratingSpinner.getValueFactory().setValue(3);
        }
    }

    @FXML
    private void handleReviewTypeChange() {
        String selectedType = reviewTypeComboBox.getValue();
        if ("Show".equals(selectedType) || "Both".equals(selectedType)) {
            showNameContainer.setVisible(true);
            showNameContainer.setManaged(true);
        } else {
            showNameContainer.setVisible(false);
            showNameContainer.setManaged(false);
            showNameField.clear();
        }
    }

    @FXML
    private void handleAddReview() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String description = descriptionArea.getText().trim();
        String room = roomComboBox.getValue();
        String reviewType = reviewTypeComboBox.getValue();
        String showName = showNameField.getText().trim();
        Integer rating = ratingSpinner.getValue();

        boolean isTitleEmpty = title == null || title.isEmpty();
        boolean isAuthorEmpty = author == null || author.isEmpty();
        boolean isRoomEmpty = room == null || room.trim().isEmpty();
        boolean isReviewTypeEmpty = reviewType == null || reviewType.trim().isEmpty();
        boolean isShowNameEmpty = showName == null || showName.isEmpty();
        boolean isRatingEmpty = rating == null;

        if (isTitleEmpty || isAuthorEmpty || isRoomEmpty || isReviewTypeEmpty || isRatingEmpty) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Required Fields");
            alert.setHeaderText("All Required Fields Must Be Filled");
            alert.setContentText("Please fill in the following fields: Title, Author, Room, Review Type, Rating");
            alert.showAndWait();
            return;
        }

        if (("Show".equals(reviewType) || "Both".equals(reviewType)) && isShowNameEmpty) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Show Name");
            alert.setHeaderText("Show Name Required");
            alert.setContentText("Please enter the name of the show for a Show or Both review type.");
            alert.showAndWait();
            return;
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Review newReview = new Review(reviews.size() + 1, rating, author, title, description, room, reviewType, "Show".equals(reviewType) || "Both".equals(reviewType) ? showName : null);
        newReview.setTimestamp(timestamp);
        reviews.add(0, newReview);

        populateReviewsGrid();
        updateSummary();

        titleField.clear();
        authorField.clear();
        descriptionArea.clear();
        roomComboBox.getSelectionModel().clearSelection();
        reviewTypeComboBox.getSelectionModel().clearSelection();
        showNameField.clear();
        showNameContainer.setVisible(false);
        showNameContainer.setManaged(false);
        ratingSpinner.getValueFactory().setValue(3);
        addReviewForm.setVisible(true);

        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(addReviewForm, reviewsHeader, reviewsGrid);
    }

    @FXML
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            if (deltaY > 0) {
                scaleFactor += 0.1;
            } else if (deltaY < 0) {
                scaleFactor -= 0.1;
            }

            scaleFactor = Math.max(0.5, Math.min(2.0, scaleFactor));

            mainBorderPane.setScaleX(scaleFactor);
            mainBorderPane.setScaleY(scaleFactor);

            scrollPane.setFitToWidth(scaleFactor >= 1.0);
            scrollPane.setFitToHeight(false);

            event.consume();
        }
    }
}