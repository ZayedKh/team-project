package lancaster.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lancaster.ui.FullCalendarView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lancaster.ui.RevenueTrackingUI;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

/**
 * Controller class for managing the selection pane in the Lancaster application.
 * <p>
 * This controller handles navigation between different UI modules including booking,
 * calendar views, review management, revenue tracking, seating arrangements, venue calendars,
 * and daily sheet views. It initializes views, sets up button actions, and manages active
 * styling for navigation buttons.
 * </p>
 */
public class SelectionPaneController implements Initializable {

    @FXML
    private StackPane mainContainer;      // Main container that holds the active view.

    @FXML
    private Button btnBooking;            // Button to navigate to the booking view.

    @FXML
    private Button btnCalendar;           // Button to navigate to the regular calendar view.

    @FXML
    private Button btnReview;             // Button to navigate to the review management view.

    @FXML
    private Button btnRevenue;            // Button to navigate to the revenue tracking view.

    @FXML
    private Button btnSeating;            // Button to navigate to the seating arrangements view.

    @FXML
    private Button btnVenue;              // Button to navigate to the venue calendar view.

    @FXML
    private Button btnDailySheet;         // Button to navigate to the daily sheet view.

    private FullCalendarView bookingCalendarView;      // Calendar view for booking-related scheduling.
    private FullCalendarView regularCalendarView;      // Calendar view for regular event scheduling.
    private FullCalendarView dailySheetCalenderView;     // Calendar view specifically for daily sheets.
    private RevenueTrackingUI revenueTrackingUI;         // UI component for revenue tracking.
    private Node reviewPane;                             // Node representing the review pane.
    private Node seatingPane;                            // Node representing the seating pane.
    private Node homePane;                               // Node representing the home pane.
    private Node smallHallSeatingPane;                   // Node for seating layout of the small hall.
    private Node theaterSeatingPane;                     // Node for seating layout of the theater.
    private Node roomLayoutPane;                         // Node for the room layout view.
    private BorderPane combinedSeatingPane;              // Combined pane that holds various seating options.

    // Button style constants.
    private final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: transparent;";
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: rgba(46, 204, 64, 0.15); -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: #2ECC40;";
    private final String TOGGLE_BUTTON_STYLE = "-fx-background-color: #122023; -fx-text-fill: white; -fx-border-color: #2ECC40; -fx-border-width: 1;";
    private final String TOGGLE_BUTTON_SELECTED_STYLE = "-fx-background-color: #2ECC40; -fx-text-fill: white; -fx-border-color: #2ECC40; -fx-border-width: 1;";

    /**
     * Initializes the selection pane controller after FXML fields are injected.
     * <p>
     * This method sets up the home pane from the main container, configures button actions,
     * initializes all the related UI views (calendars, seating, revenue), and resets navigation
     * button styles to the default state.
     * </p>
     *
     * @param url The location used to resolve relative paths for the root object, or {@code null} if unknown.
     * @param resourceBundle The resource bundle used to localize the root object, or {@code null} if not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homePane = mainContainer.getChildren().get(0);
        setupButtonActions();
        initializeViews();
        resetButtonStyles();
    }

    /**
     * Sets up actions for each navigation button.
     * <p>
     * When a button is clicked, the corresponding UI view is loaded into the main container,
     * and the button is styled as active.
     * </p>
     */
    private void setupButtonActions() {
        btnBooking.setOnAction(event -> showBookingPane());
        btnCalendar.setOnAction(event -> showRegularCalendar());
        btnReview.setOnAction(event -> {
            try {
                showReviewPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnRevenue.setOnAction(event -> showRevenueTracking());
        btnSeating.setOnAction(event -> showSeatingPane());
        btnVenue.setOnAction(event -> showVenueCalendar());
        btnDailySheet.setOnAction(event -> showDailySheetCalendar());
    }

    /**
     * Initializes the various views used in the application.
     * <p>
     * This includes setting up the booking calendar, regular calendar, daily sheet calendar,
     * revenue tracking UI, and seating layouts.
     * </p>
     */
    private void initializeViews() {
        bookingCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        regularCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        dailySheetCalenderView = new FullCalendarView(YearMonth.now(), mainContainer, homePane, true);
        revenueTrackingUI = new RevenueTrackingUI();
        // Optionally create placeholder panes if needed:
        // createPlaceholderPanes();
        initializeSeatingPanes();
    }

    /**
     * Creates placeholder panes for review and seating, displaying a "coming soon" message.
     * <p>
     * These placeholders provide basic feedback as a fallback.
     * </p>
     */
    private void createPlaceholderPanes() {
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

    /**
     * Loads the home page view into the main container.
     * <p>
     * This method resets the main container's children to only display the home pane, and resets button styles.
     * </p>
     */
    public void showHomePage() {
        mainContainer.getChildren().setAll(homePane);
        resetButtonStyles();
    }

    /**
     * Initializes seating-related panes by loading FXML files.
     * <p>
     * Attempts to load seating layouts from external FXML resources. In case of failure, a seating placeholder is created.
     * </p>
     */
    private void initializeSeatingPanes() {
        try {
            FXMLLoader smallHallLoader = new FXMLLoader(getClass().getResource("/lancaster/ui/SmallHallSeating.fxml"));
            smallHallSeatingPane = smallHallLoader.load();

            FXMLLoader theaterLoader = new FXMLLoader(getClass().getResource("/lancaster/ui/TheaterSeatingLayout.fxml"));
            theaterSeatingPane = theaterLoader.load();

            FXMLLoader roomLoader = new FXMLLoader(getClass().getResource("/lancaster/ui/RoomLayout.fxml"));
            roomLayoutPane = roomLoader.load();

            createCombinedSeatingPane();
        } catch (IOException e) {
            e.printStackTrace();
            createSeatingPlaceholder();
        }
    }

    /**
     * Creates a combined seating pane that allows the user to toggle between different seating layouts.
     * <p>
     * This method creates a BorderPane containing toggle buttons for "Small Hall", "Main Hall", and "Rooms".
     * The user can switch between seating layouts, and the default selection is set to "Small Hall".
     * </p>
     */
    private void createCombinedSeatingPane() {
        combinedSeatingPane = new BorderPane();
        combinedSeatingPane.setStyle("-fx-background-color: #122023;");

        // Create toggle buttons for seating options.
        ToggleButton smallHallButton = new ToggleButton("Small Hall");
        ToggleButton mainHallButton = new ToggleButton("Main Hall");
        ToggleButton roomsButton = new ToggleButton("Rooms");

        ToggleGroup hallGroup = new ToggleGroup();
        smallHallButton.setToggleGroup(hallGroup);
        mainHallButton.setToggleGroup(hallGroup);
        roomsButton.setToggleGroup(hallGroup);

        // Apply initial styles.
        smallHallButton.setStyle(TOGGLE_BUTTON_STYLE);
        mainHallButton.setStyle(TOGGLE_BUTTON_STYLE);
        roomsButton.setStyle(TOGGLE_BUTTON_STYLE);

        // Add listeners for toggle button selection.
        smallHallButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                combinedSeatingPane.setCenter(smallHallSeatingPane);
                smallHallButton.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
                mainHallButton.setStyle(TOGGLE_BUTTON_STYLE);
                roomsButton.setStyle(TOGGLE_BUTTON_STYLE);
            }
        });

        mainHallButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                combinedSeatingPane.setCenter(theaterSeatingPane);
                mainHallButton.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
                smallHallButton.setStyle(TOGGLE_BUTTON_STYLE);
                roomsButton.setStyle(TOGGLE_BUTTON_STYLE);
            }
        });

        roomsButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                combinedSeatingPane.setCenter(roomLayoutPane);
                roomsButton.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
                smallHallButton.setStyle(TOGGLE_BUTTON_STYLE);
                mainHallButton.setStyle(TOGGLE_BUTTON_STYLE);
            }
        });

        // Create container for the toggle buttons.
        HBox buttonBox = new HBox(20, smallHallButton, mainHallButton, roomsButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setPadding(new javafx.geometry.Insets(10));
        buttonBox.setStyle("-fx-background-color: #0A1517;");

        combinedSeatingPane.setBottom(buttonBox);

        // Set default selection.
        smallHallButton.setSelected(true);
        combinedSeatingPane.setCenter(smallHallSeatingPane);
        smallHallButton.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
    }

    /**
     * Creates a seating placeholder pane that is displayed if seating layouts fail to load.
     * <p>
     * This placeholder informs the user that seating arrangements could not be loaded.
     * </p>
     */
    private void createSeatingPlaceholder() {
        VBox seatingPlaceholder = new VBox();
        seatingPlaceholder.setStyle("-fx-background-color: #122023;");
        seatingPlaceholder.setAlignment(javafx.geometry.Pos.CENTER);
        seatingPlaceholder.setSpacing(20);

        javafx.scene.control.Label seatingTitle = new javafx.scene.control.Label("Seating Arrangement");
        seatingTitle.setFont(new javafx.scene.text.Font("Cambria", 32));
        seatingTitle.setTextFill(javafx.scene.paint.Color.WHITE);

        javafx.scene.control.Label seatingSubtitle = new javafx.scene.control.Label("Failed to load seating layouts");
        seatingSubtitle.setFont(new javafx.scene.text.Font("Cambria", 18));
        seatingSubtitle.setTextFill(javafx.scene.paint.Color.web("#2ECC40"));

        seatingPlaceholder.getChildren().addAll(seatingTitle, seatingSubtitle);
        combinedSeatingPane = new BorderPane(seatingPlaceholder);
    }

    /**
     * Loads and displays the booking pane by switching the main container's content.
     * <p>
     * Loads the booking FXML resource and sets it as the active view in the main container.
     * Also, the booking button is styled as active.
     * </p>
     */
    @FXML
    public void showBookingPane() {
        try {
            Parent reviewPane = FXMLLoader.load(getClass().getResource("/lancaster/ui/booking.fxml"));
            mainContainer.getChildren().setAll(reviewPane);
            setActiveButton(btnBooking);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the regular calendar view.
     * <p>
     * Sets the regular calendar view as the main view and marks the calendar button as active.
     * </p>
     */
    public void showRegularCalendar() {
        mainContainer.getChildren().setAll(regularCalendarView.getCalendarView());
        setActiveButton(btnCalendar);
    }

    /**
     * Displays the daily sheet calendar view.
     * <p>
     * Sets the daily sheet calendar view as the main view and marks the daily sheet button as active.
     * </p>
     */
    @FXML
    private void showDailySheetCalendar() {
        mainContainer.getChildren().setAll(dailySheetCalenderView.getCalendarView());
        setActiveButton(btnDailySheet);
    }

    /**
     * Loads and displays the review pane.
     * <p>
     * Loads the Review FXML resource, sets it as the main content, and marks the review button as active.
     * </p>
     *
     * @throws IOException if the FXML resource cannot be loaded.
     */
    private void showReviewPane() throws IOException {
        try {
            Parent reviewPane = FXMLLoader.load(getClass().getResource("/lancaster/ui/Review.fxml"));
            mainContainer.getChildren().setAll(reviewPane);
            setActiveButton(btnReview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the revenue tracking view.
     * <p>
     * Sets the revenue tracking UI as the active content and styles the revenue button as active.
     * </p>
     */
    private void showRevenueTracking() {
        mainContainer.getChildren().setAll(revenueTrackingUI);
        setActiveButton(btnRevenue);
    }

    /**
     * Displays the seating pane containing combined seating layouts.
     * <p>
     * Sets the combined seating pane as the main content and marks the seating button as active.
     * </p>
     */
    private void showSeatingPane() {
        mainContainer.getChildren().setAll(combinedSeatingPane);
        setActiveButton(btnSeating);
    }

    /**
     * Loads and displays the venue calendar view.
     * <p>
     * Loads the VenueCalendar FXML resource, configures its controller, and sets the view as active.
     * Marks the venue button as active.
     * </p>
     */
    private void showVenueCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/VenueCalendar.fxml"));
            Node venueCalendarView = loader.load();
            VenueCalendarController venueController = loader.getController();
            venueController.setSelectionPaneController(this);
            mainContainer.getChildren().setAll(venueCalendarView);
            setActiveButton(btnVenue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the active style on a given button and resets all others to their default style.
     * <p>
     * This helper method clears the active styling from all navigation buttons and applies
     * the active style to the specified button.
     * </p>
     *
     * @param button The button to be styled as active.
     */
    private void setActiveButton(Button button) {
        resetButtonStyles();
        button.setStyle(BUTTON_ACTIVE_STYLE);
    }

    /**
     * Resets all navigation buttons to their default style.
     * <p>
     * This helper method iterates over all navigation buttons and applies the default styling.
     * </p>
     */
    private void resetButtonStyles() {
        Button[] buttons = {btnBooking, btnCalendar, btnReview, btnRevenue, btnSeating, btnVenue, btnDailySheet};
        for (Button btn : buttons) {
            btn.setStyle(BUTTON_DEFAULT_STYLE);
        }
    }
}
