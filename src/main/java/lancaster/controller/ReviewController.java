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
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Controller class for managing reviews in the Lancaster application.
 * <p>
 * This class handles the display, sorting, and addition of reviews. It is responsible
 * for initializing UI components for review presentation, loading review data from the database,
 * populating review cards in a grid, and managing user interactions such as form submission, sorting,
 * and zooming via scroll events.
 * </p>
 */
public class ReviewController implements Initializable {

    @FXML
    private ScrollPane scrollPane;           // Scroll pane that wraps the main content allowing for scrolling and zooming.

    @FXML
    private BorderPane mainBorderPane;       // Main layout container for the review interface.

    @FXML
    private VBox centerVBox;                 // Vertical box used for centering the primary content.

    @FXML
    private GridPane reviewsGrid;            // Grid pane used to display review cards.

    @FXML
    private ComboBox<String> sortComboBox;   // Combo box for selecting the sort criteria for reviews.

    @FXML
    private Button addReviewButton;          // Button that toggles the visibility of the review submission form.

    @FXML
    private VBox addReviewForm;              // Form container for adding new reviews.

    @FXML
    private TextField titleField;            // Text field for entering the review title.

    @FXML
    private TextField authorField;           // Text field for entering the review author.

    @FXML
    private TextArea descriptionArea;        // Text area for entering the review description.

    @FXML
    private ComboBox<String> roomComboBox;   // Combo box for selecting the room associated with the review.

    @FXML
    private ComboBox<String> reviewTypeComboBox;  // Combo box for selecting the review type (e.g., Show, Venue, or Both).

    @FXML
    private VBox showNameContainer;          // Container holding the input field for the show name.

    @FXML
    private TextField showNameField;         // Text field for entering the name of the show (if applicable).

    @FXML
    private Spinner<Integer> ratingSpinner;  // Spinner control for selecting a numerical rating value.

    @FXML
    private Button submitReviewButton;       // Button to submit the new review.

    @FXML
    private Label averageRatingLabel;        // Label displaying the average rating of all reviews.

    @FXML
    private Label totalReviewsLabel;         // Label displaying the total number of reviews.

    private ObservableList<Review> reviews;  // Observable list containing Review objects for display.

    private double scaleFactor = 1.0;        // Scale factor used for zooming the main content.

    private HBox reviewsHeader;              // Header component of the reviews section.

    /**
     * Initializes the ReviewController after the FXML fields have been injected.
     * <p>
     * This method initializes the reviews list, loads sample review data from the database,
     * configures UI components including the rating spinner, sort and room combo boxes, and sets
     * up the event handlers for sorting and zooming. It also arranges the layout by adding
     * the header and review grid to the center container.
     * </p>
     *
     * @param location  The URL location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resources The resource bundle used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reviews = FXCollections.observableArrayList();
        // Retrieve the header node from the center VBox
        reviewsHeader = (HBox) centerVBox.getChildren().get(0);
        try {
            initializeSampleData();  // Load initial review data from the database.
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);  // Wrap and rethrow exceptions for higher-level handling.
        }
        // Configure rating spinner: values from 1 to 5 with a default of 3.
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        ratingSpinner.setValueFactory(valueFactory);

        // Populate sort options and set default sort to "Most Recent".
        sortComboBox.getItems().addAll("Most Recent", "Highest Rated", "Lowest Rated");
        sortComboBox.setValue("Most Recent");

        // Populate room selection options.
        roomComboBox.getItems().addAll(
                "Main Hall",
                "Small Hall",
                "The Green Room",
                "Brontë Boardroom",
                "Dickens Den",
                "Poe Parlor",
                "Globe Room",
                "Chekhov Chamber"
        );

        // Populate review type options.
        reviewTypeComboBox.getItems().addAll("Show", "Venue", "Both");
        addReviewForm.setVisible(false);  // Initially hide the review form.
        centerVBox.getChildren().clear();  // Clear center container.
        centerVBox.getChildren().addAll(reviewsHeader, reviewsGrid);  // Add header and review grid back into center.
        populateReviewsGrid();  // Display reviews in the grid.
        updateSummary();  // Update summary information such as total reviews and average rating.

        // Set up sort selection event: sorts reviews based on selected criteria and refreshes the grid.
        sortComboBox.setOnAction(event -> {
            String selectedSort = sortComboBox.getValue();
            if (selectedSort.equals("Highest Rated")) {
                reviews.sort((r1, r2) -> Integer.compare(r2.getRating(), r1.getRating()));  // Sort reviews descending by rating.
            } else if (selectedSort.equals("Lowest Rated")) {
                reviews.sort((r1, r2) -> Integer.compare(r1.getRating(), r2.getRating()));  // Sort reviews ascending by rating.
            } else {
                reviews.sort((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp()));  // Sort reviews by most recent timestamp.
            }
            populateReviewsGrid();  // Refresh the grid after sorting.
        });

        // Set up zoom handling via mouse scroll events on the main border pane.
        mainBorderPane.setOnScroll(this::handleScroll);
    }

    /**
     * Initializes sample review data by fetching it from the database and assigning timestamps.
     * <p>
     * This method retrieves reviews using the {@link DBUtils#getReviews()} method, and assigns a
     * timestamp to each review.
     * </p>
     *
     * @throws SQLException           If a database access error occurs.
     * @throws IOException            If an I/O error occurs during data retrieval.
     * @throws ClassNotFoundException If the required database driver class is not found.
     */
    private void initializeSampleData() throws SQLException, IOException, ClassNotFoundException {
        LocalDateTime baseTime = LocalDateTime.now();  // Base time for generating timestamps.
        DBUtils db = new DBUtils();  // Create a database utility instance.
        reviews.addAll(db.getReviews());  // Fetch reviews from the database.

        // Assign timestamps to each review, with each timestamp 10 minutes apart.
        for (int i = 0; i < reviews.size(); i++) {
            LocalDateTime reviewTime = baseTime.minusMinutes(i * 10);
            String timestamp = reviewTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            reviews.get(i).setTimestamp(timestamp);
        }
    }

    /**
     * Populates the reviews grid with review cards created from the current list of reviews.
     * <p>
     * This method clears any existing content from the grid, creates a review card for each review,
     * and adds them to the grid. Cards display details such as the author's name, rating in stars, description,
     * review type, show name (if applicable), and timestamp.
     * </p>
     */
    private void populateReviewsGrid() {
        reviewsGrid.getChildren().clear();  // Remove any existing review cards.
        int row = 0;
        int col = 0;

        for (Review review : reviews) {
            // Create a styled review card.
            VBox reviewCard = new VBox(5);
            reviewCard.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #4CAF50; -fx-border-width: 1; -fx-border-radius: 5;");
            reviewCard.setPrefWidth(595);
            reviewCard.setMinHeight(200);

            // Author label.
            Label authorLabel = new Label(review.getAuthor());
            authorLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

            // Rating stars display.
            HBox ratingBox = new HBox(2);
            for (int i = 0; i < review.getRating(); i++) {
                Label star = new Label("★");
                star.setStyle("-fx-text-fill: gold; -fx-font-size: 14px;");
                ratingBox.getChildren().add(star);
            }

            // Description label.
            Label descriptionLabel = new Label(review.getDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.setStyle("-fx-text-fill: black;");

            // Review type label.
            Label reviewTypeLabel = new Label("Review Type: " + review.getReviewType());
            reviewTypeLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");

            // Conditionally include show name if it is provided.
            Label showNameLabel = new Label();
            if (review.getShowName() != null && !review.getShowName().isEmpty()) {
                showNameLabel.setText("Show: " + review.getShowName());
                showNameLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");
            }

            // Timestamp label.
            Label timestampLabel = new Label("Submitted: " + review.getTimestamp());
            timestampLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 12px;");

            // Assemble the review card with appropriate components.
            if (review.getShowName() != null && !review.getShowName().isEmpty()) {
                reviewCard.getChildren().addAll(authorLabel, ratingBox, descriptionLabel, reviewTypeLabel, showNameLabel, timestampLabel);
            } else {
                reviewCard.getChildren().addAll(authorLabel, ratingBox, descriptionLabel, reviewTypeLabel, timestampLabel);
            }
            reviewsGrid.add(reviewCard, col, row);  // Add card to grid in specified column and row.

            col++;
            if (col == 2) {  // Move to the next row after two columns are filled.
                col = 0;
                row++;
            }
        }
    }

    /**
     * Updates the summary labels for total reviews and average rating.
     * <p>
     * The total number of reviews is derived from the size of the reviews list,
     * while the average rating is calculated from each review's rating value. The results are then formatted and displayed.
     * </p>
     */
    private void updateSummary() {
        int totalReviews = reviews.size();  // Compute total number of reviews.
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);  // Calculate average rating; default to 0.0 if no reviews exist.
        totalReviewsLabel.setText(totalReviews + " Total Reviews");
        averageRatingLabel.setText(String.format("%.1f", averageRating));  // Format average rating to one decimal place.
    }

    /**
     * Toggles the visibility of the add review form.
     * <p>
     * This method shows or hides the review submission form and reconfigures the center container layout accordingly.
     * When hiding the form, input fields are reset to their default values.
     * </p>
     */
    @FXML
    private void toggleAddReviewForm() {
        boolean willBeVisible = !addReviewForm.isVisible();  // Determine new visibility state.
        addReviewForm.setVisible(willBeVisible);

        centerVBox.getChildren().clear();  // Clear current center content.
        if (willBeVisible) {
            centerVBox.getChildren().addAll(addReviewForm, reviewsHeader, reviewsGrid);  // Display form above review header and grid.
        } else {
            centerVBox.getChildren().addAll(reviewsHeader, reviewsGrid);  // Display only review header and grid.
        }

        // Reset form fields when the form is hidden.
        if (!willBeVisible) {
            titleField.clear();
            authorField.clear();
            descriptionArea.clear();
            roomComboBox.getSelectionModel().clearSelection();
            reviewTypeComboBox.getSelectionModel().clearSelection();
            showNameField.clear();
            showNameContainer.setVisible(false);
            showNameContainer.setManaged(false);
            ratingSpinner.getValueFactory().setValue(3);  // Reset spinner to default rating.
        }
    }

    /**
     * Handles changes in the review type selection.
     * <p>
     * Based on the selected review type, this method shows or hides the show name input field.
     * For review types "Show" or "Both", the show name container is made visible;
     * otherwise, it is hidden and the field is cleared.
     * </p>
     */
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

    /**
     * Handles the submission of a new review.
     * <p>
     * This method validates all required input fields, displays alerts if any required field is missing,
     * creates a new Review object with the provided data, and adds it to the top of the reviews list.
     * It then updates the reviews grid and summary information, and resets the form fields afterward.
     * </p>
     */
    @FXML
    private void handleAddReview() {
        // Retrieve and trim input values.
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String description = descriptionArea.getText().trim();
        String room = roomComboBox.getValue();
        String reviewType = reviewTypeComboBox.getValue();
        String showName = showNameField.getText().trim();
        Integer rating = ratingSpinner.getValue();

        // Validation checks for empty fields.
        boolean isTitleEmpty = title == null || title.isEmpty();
        boolean isAuthorEmpty = author == null || author.isEmpty();
        boolean isRoomEmpty = room == null || room.trim().isEmpty();
        boolean isReviewTypeEmpty = reviewType == null || reviewType.trim().isEmpty();
        boolean isShowNameEmpty = showName == null || showName.isEmpty();
        boolean isRatingEmpty = rating == null;

        // Check required fields: title, author, room, review type, and rating.
        if (isTitleEmpty || isAuthorEmpty || isRoomEmpty || isReviewTypeEmpty || isRatingEmpty) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Required Fields");
            alert.setHeaderText("All Required Fields Must Be Filled");
            alert.setContentText("Please fill in the following fields: Title, Author, Room, Review Type, Rating");
            alert.showAndWait();
            return;
        }

        // Ensure show name is provided for review types "Show" or "Both".
        if (("Show".equals(reviewType) || "Both".equals(reviewType)) && isShowNameEmpty) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing Show Name");
            alert.setHeaderText("Show Name Required");
            alert.setContentText("Please enter the name of the show for a Show or Both review type.");
            alert.showAndWait();
            return;
        }

        // Create a new Review object with current timestamp.
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Review newReview = new Review(reviews.size() + 1, rating, author, title, description, room, reviewType,
                "Show".equals(reviewType) || "Both".equals(reviewType) ? showName : null);
        newReview.setTimestamp(timestamp);
        reviews.add(0, newReview);  // Add new review at the beginning of the list.

        // Update review grid and summary labels.
        populateReviewsGrid();
        updateSummary();

        // Reset form fields.
        titleField.clear();
        authorField.clear();
        descriptionArea.clear();
        roomComboBox.getSelectionModel().clearSelection();
        reviewTypeComboBox.getSelectionModel().clearSelection();
        showNameField.clear();
        showNameContainer.setVisible(false);
        showNameContainer.setManaged(false);
        ratingSpinner.getValueFactory().setValue(3);
        addReviewForm.setVisible(true);  // Keep form visible after submission.

        centerVBox.getChildren().clear();
        centerVBox.getChildren().addAll(addReviewForm, reviewsHeader, reviewsGrid);  // Rebuild the center layout.
    }

    /**
     * Handles scroll events to implement zooming functionality on the main content.
     * <p>
     * When the user scrolls while holding the Control key, the content scales in or out.
     * The scale factor is constrained between 0.5 and 2.0 and is applied to the main border pane.
     * Additionally, the scroll pane settings are adjusted based on the zoom level.
     * </p>
     *
     * @param event The {@link ScrollEvent} triggered by user scrolling.
     */
    @FXML
    private void handleScroll(ScrollEvent event) {
        if (event.isControlDown()) {  // Zoom behavior is triggered only when the Control key is pressed.
            double deltaY = event.getDeltaY();
            if (deltaY > 0) {
                scaleFactor += 0.1;  // Zoom in.
            } else if (deltaY < 0) {
                scaleFactor -= 0.1;  // Zoom out.
            }

            // Constrain the scale factor within a reasonable range.
            scaleFactor = Math.max(0.5, Math.min(2.0, scaleFactor));

            // Apply the scaling factor to the main border pane.
            mainBorderPane.setScaleX(scaleFactor);
            mainBorderPane.setScaleY(scaleFactor);

            // Adjust the scroll pane settings based on the current scale factor.
            scrollPane.setFitToWidth(scaleFactor >= 1.0);
            scrollPane.setFitToHeight(false);

            event.consume();  // Consume the event to prevent default scrolling behavior.
        }
    }
}
