package lancaster.controller;

import lancaster.ui.FullCalendarView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lancaster.ui.RevenueTrackingUI;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

/**
 * A controller for a navigation pane in a venue management application.
 * Manages buttons to switch between views like booking calendars, revenue dashboards,
 * and placeholder screens for future features, providing a central hub for venue staff.
 */
public class SelectionPaneController implements Initializable {

    @FXML
    private StackPane mainContainer;

    @FXML
    private Button btnBooking;

    @FXML
    private Button btnCalendar;

    @FXML
    private Button btnReview;

    @FXML
    private Button btnRevenue;

    @FXML
    private Button btnSeating;

    private FullCalendarView bookingCalendarView;
    private FullCalendarView regularCalendarView;
    private RevenueTrackingUI revenueTrackingUI;
    private Node reviewPane;
    private Node seatingPane;
    private Node homePane;  // original welcome pane

    // Style constants
    private final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: transparent;";
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: rgba(46, 204, 64, 0.15); -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: #2ECC40;";

    /**
     * Initializes the navigation pane, setting up button actions and preloading views.
     *
     * @param url            the location used to resolve relative paths
     * @param resourceBundle the resources for this controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Store the original welcome pane
        homePane = mainContainer.getChildren().get(0);

        // Remove onAction attributes from FXML and set them programmatically
        setupButtonActions();

        // Pre-initialise views that we'll need later
        initializeViews();

        // Start with home page selected (no button highlighted)
        resetButtonStyles();
    }

    /**
     * Sets up button actions to switch between different views when clicked.
     */
    private void setupButtonActions() {
        btnBooking.setOnAction(event -> showBookingCalendar());
        btnCalendar.setOnAction(event -> showRegularCalendar());
        btnReview.setOnAction(event -> showReviewPane());
        btnRevenue.setOnAction(event -> showRevenueTracking());
        btnSeating.setOnAction(event -> showSeatingPane());
    }

    /**
     * Prepares all views (calendars, revenue dashboard, placeholders) for quick switching.
     */
    private void initializeViews() {
        bookingCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        regularCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        revenueTrackingUI = new RevenueTrackingUI();

        // Initialise placeholders for review and seating panes
        createPlaceholderPanes();
    }

    /**
     * Creates placeholder panes for features (review, seating) not yet implemented.
     */
    private void createPlaceholderPanes() {
        // creates review placeholder
        VBox reviewPlaceholder = new VBox();
        reviewPlaceholder.setStyle("-fx-background-color: #122023;");
        reviewPlaceholder.setAlignment(javafx.geometry.Pos.CENTER);
        reviewPlaceholder.setSpacing(20);

        javafx.scene.control.Label reviewTitle = new javafx.scene.control.Label("Review Management");
        reviewTitle.setFont(new javafx.scene.text.Font("Cambria", 32));
        reviewTitle.setTextFill(javafx.scene.paint.Color.WHITE);

        javafx.scene.control.Label reviewSubtitle = new javafx.scene.control.Label("This feature is coming soon");
        reviewSubtitle.setFont(new javafx.scene.text.Font("Cambria", 18));
        reviewSubtitle.setTextFill(javafx.scene.paint.Color.web("#2ECC40"));

        reviewPlaceholder.getChildren().addAll(reviewTitle, reviewSubtitle);
        reviewPane = reviewPlaceholder;

        // creates seating placeholder
        VBox seatingPlaceholder = new VBox();
        seatingPlaceholder.setStyle("-fx-background-color: #122023;");
        seatingPlaceholder.setAlignment(javafx.geometry.Pos.CENTER);
        seatingPlaceholder.setSpacing(20);

        javafx.scene.control.Label seatingTitle = new javafx.scene.control.Label("Seating Arrangement");
        seatingTitle.setFont(new javafx.scene.text.Font("Cambria", 32));
        seatingTitle.setTextFill(javafx.scene.paint.Color.WHITE);

        javafx.scene.control.Label seatingSubtitle = new javafx.scene.control.Label("This feature is coming soon");
        seatingSubtitle.setFont(new javafx.scene.text.Font("Cambria", 18));
        seatingSubtitle.setTextFill(javafx.scene.paint.Color.web("#2ECC40"));

        seatingPlaceholder.getChildren().addAll(seatingTitle, seatingSubtitle);
        seatingPane = seatingPlaceholder;
    }

    // navigation methods

    /**
     * Displays the home (welcome) page in the main content area.
     */
    public void showHomePage() {
        mainContainer.getChildren().setAll(homePane);
        resetButtonStyles();
    }

    /**
     * Switches to the booking calendar view for scheduling events.
     */
    @FXML
    private void showBookingCalendar() {
        mainContainer.getChildren().setAll(bookingCalendarView.getCalendarView());
        setActiveButton(btnBooking);
    }

    /**
     * Switches to the regular calendar view for general date browsing.
     */
    private void showRegularCalendar() {
        mainContainer.getChildren().setAll(regularCalendarView.getCalendarView());
        setActiveButton(btnCalendar);
    }

    /**
     * Switches to the review management placeholder pane.
     */
    private void showReviewPane() {
        mainContainer.getChildren().setAll(reviewPane);
        setActiveButton(btnReview);
    }

    /**
     * Switches to the revenue tracking dashboard for financial insights.
     */
    @FXML
    private void showRevenueTracking() {
        mainContainer.getChildren().setAll(revenueTrackingUI);
        setActiveButton(btnRevenue);
    }

    /**
     * Switches to the seating arrangement placeholder pane.
     */
    private void showSeatingPane() {
        mainContainer.getChildren().setAll(seatingPane);
        setActiveButton(btnSeating);
    }

    // Button styling helpers

    /**
     * Highlights the selected button and resets others to default style.
     *
     * @param button the button to mark as active
     */
    private void setActiveButton(Button button) {
        resetButtonStyles();
        button.setStyle(BUTTON_ACTIVE_STYLE);
    }

    /**
     * Resets all navigation buttons to their default (inactive) style.
     */
    private void resetButtonStyles() {
        Button[] buttons = {btnBooking, btnCalendar, btnReview, btnRevenue, btnSeating};
        for (Button btn : buttons) {
            btn.setStyle(BUTTON_DEFAULT_STYLE);
        }
    }
}
