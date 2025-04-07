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
import lancaster.utils.DBUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for managing user reviews.
 * Displays, sorts, and allows the creation of reviews for rooms and shows.
 */
public class ReviewController implements Initializable {

    @FXML private ScrollPane scrollPane;
    @FXML private BorderPane mainBorderPane;
    @FXML private VBox centerVBox;
    @FXML private GridPane reviewsGrid;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private Button addReviewButton;
    @FXML private VBox addReviewForm;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> reviewTypeComboBox;
    @FXML private VBox showNameContainer;
    @FXML private TextField showNameField;
    @FXML private Spinner<Integer> ratingSpinner;
    @FXML private Button submitReviewButton;
    @FXML private Label averageRatingLabel;
    @FXML private Label totalReviewsLabel;

    private ObservableList<Review> reviews;
    private double scaleFactor = 1.0;
    private HBox reviewsHeader;

    /**
     * Called automatically when the scene is loaded.
     * Initializes UI controls, event handlers, and loads review data.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reviews = FXCollections.observableArrayList();
        reviewsHeader = (HBox) centerVBox.getChildren().get(0);

        try {
            initializeSampleData(); // Load initial reviews from database
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Setup spinner for rating 1-5
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);

        // Sorting options for reviews
        sortComboBox.getItems().addAll("Most Recent", "Highest Rated", "Lowest Rated");
        sortComboBox.setValue("Most Recent");

        // Populate room and review type dropdowns
        roomComboBox.getItems().addAll(
                "Main Hall", "Small Hall", "The Green Room", "Brontë Boardroom",
                "Dickens Den", "Poe Parlor", "Globe Room", "Chekhov Chamber"
        );
        reviewTypeComboBox.getItems().addAll("Show", "Venue", "Both");

        // Hide add review form initially
        addReviewForm.setVisible(false);

        // Reset the main layout to only show header and review grid
        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(reviewsHeader, reviewsGrid);

        populateReviewsGrid(); // Add reviews to grid
        updateSummary(); // Update total/average stats

        // Sort change listener
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

        // Allow zooming via Ctrl + scroll
        mainBorderPane.setOnScroll(this::handleScroll);
    }

    /**
     * Loads reviews from database and sets a relative timestamp.
     */
    private void initializeSampleData() throws SQLException, IOException, ClassNotFoundException {
        LocalDateTime baseTime = LocalDateTime.now();
        DBUtils db = new DBUtils();
        reviews.addAll(db.getReviews());

        // Assign timestamps in reverse order
        for (int i = 0; i < reviews.size(); i++) {
            LocalDateTime reviewTime = baseTime.minusMinutes(i * 10);
            String timestamp = reviewTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            reviews.get(i).setTimestamp(timestamp);
        }
    }

    /**
     * Renders the list of reviews in a responsive grid layout.
     */
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

    /**
     * Updates the average rating and total number of reviews.
     */
    private void updateSummary() {
        int totalReviews = reviews.size();
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        totalReviewsLabel.setText(totalReviews + " Total Reviews");
        averageRatingLabel.setText(String.format("%.1f", averageRating));
    }

    /**
     * Toggle visibility of the add review form.
     */
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

        // Clear fields when hiding
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

    /**
     * Show/hide show name field based on review type.
     */
    @FXML
    private void handleReviewTypeChange() {
        String selectedType = reviewTypeComboBox.getValue();
        boolean showField = "Show".equals(selectedType) || "Both".equals(selectedType);
        showNameContainer.setVisible(showField);
        showNameContainer.setManaged(showField);
        if (!showField) showNameField.clear();
    }

    /**
     * Validates and adds a new review to the list.
     */
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

        // Show validation alert if required fields are missing
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

        // Add new review
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Review newReview = new Review(
                reviews.size() + 1, rating, author, title, description, room, reviewType,
                "Show".equals(reviewType) || "Both".equals(reviewType) ? showName : null
        );
        newReview.setTimestamp(timestamp);
        reviews.add(0, newReview);

        populateReviewsGrid();
        updateSummary();

        // Clear form fields and keep form open
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

    /**
     * Enables zooming in/out with Ctrl + scroll gesture.
     */
    @FXML
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            double deltaY = event.getDeltaY();
            scaleFactor += (deltaY > 0) ? 0.1 : -0.1;
            scaleFactor = Math.max(0.5, Math.min(2.0, scaleFactor));

            mainBorderPane.setScaleX(scaleFactor);
            mainBorderPane.setScaleY(scaleFactor);

            scrollPane.setFitToWidth(scaleFactor >= 1.0);
            scrollPane.setFitToHeight(false);

            event.consume();
        }
    }
}
