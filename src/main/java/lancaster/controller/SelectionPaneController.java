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
 * Controller class for managing the selection pane navigation in a JavaFX application.
 */
public class SelectionPaneController implements Initializable {

    @FXML private StackPane mainContainer;  // Main content container for switching views
    @FXML private Button btnBooking;        // Button for booking view
    @FXML private Button btnCalendar;       // Button for regular calendar view
    @FXML private Button btnReview;         // Button for review management view
    @FXML private Button btnRevenue;        // Button for revenue tracking view
    @FXML private Button btnSeating;        // Button for seating arrangement view
    @FXML private Button btnVenue;          // Button for venue calendar view
    @FXML private Button btnDailySheet;     // Button for daily sheet calendar view

    // View instances
    private FullCalendarView bookingCalendarView;     // Calendar view for booking
    private FullCalendarView regularCalendarView;     // Regular calendar view
    private FullCalendarView dailySheetCalenderView;  // Daily sheet calendar view
    private RevenueTrackingUI revenueTrackingUI;      // Revenue tracking UI
    private Node reviewPane;                         // Review management pane
    private Node seatingPane;                        // Seating arrangement pane
    private Node homePane;                           // Home/default pane
    private Node smallHallSeatingPane;               // Small hall seating layout
    private Node theaterSeatingPane;                 // Theater seating layout
    private Node roomLayoutPane;                     // Room layout pane
    private BorderPane combinedSeatingPane;          // Combined seating view with toggle buttons


    // Button styling constants
    private final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: transparent;";
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: rgba(46, 204, 64, 0.15); -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: #2ECC40;";
    private final String TOGGLE_BUTTON_STYLE = "-fx-background-color: #122023; -fx-text-fill: white; -fx-border-color: #2ECC40; -fx-border-width: 1;";
    private final String TOGGLE_BUTTON_SELECTED_STYLE = "-fx-background-color: #2ECC40; -fx-text-fill: white; -fx-border-color: #2ECC40; -fx-border-width: 1;";

    /**
     * Initializes the controller after its root element has been processed.
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homePane = mainContainer.getChildren().get(0);  // Set initial home pane
        setupButtonActions();
        initializeViews();
        resetButtonStyles();
    }

    /**
     * Sets up action event handlers for navigation buttons.
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
     * Initializes all view components used in the selection pane.
     */
    private void initializeViews() {
        bookingCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        regularCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        dailySheetCalenderView = new FullCalendarView(YearMonth.now(), mainContainer, homePane, true);
        revenueTrackingUI = new RevenueTrackingUI();
        // createPlaceholderPanes();  // Commented out in original code
        initializeSeatingPanes();
    }

    /**
     * Creates placeholder panes for review and seating views (currently unused).
     */
    private void createPlaceholderPanes() {
        // Review placeholder
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

        // Seating placeholder
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
     * Displays the home page view.
     */
    public void showHomePage() {
        mainContainer.getChildren().setAll(homePane);
        resetButtonStyles();
    }

    /**
     * Initializes seating layout panes by loading FXML files.
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
            createSeatingPlaceholder();  // Fallback in case of loading failure
        }
    }

    /**
     * Creates a combined seating pane with toggle buttons for different layouts.
     */
    private void createCombinedSeatingPane() {
        combinedSeatingPane = new BorderPane();
        combinedSeatingPane.setStyle("-fx-background-color: #122023;");

        // Create toggle buttons for seating options
        ToggleButton smallHallButton = new ToggleButton("Small Hall");
        ToggleButton mainHallButton = new ToggleButton("Main Hall");
        ToggleButton roomsButton = new ToggleButton("Rooms");

        ToggleGroup hallGroup = new ToggleGroup();
        smallHallButton.setToggleGroup(hallGroup);
        mainHallButton.setToggleGroup(hallGroup);
        roomsButton.setToggleGroup(hallGroup);

        // Apply initial styles
        smallHallButton.setStyle(TOGGLE_BUTTON_STYLE);
        mainHallButton.setStyle(TOGGLE_BUTTON_STYLE);
        roomsButton.setStyle(TOGGLE_BUTTON_STYLE);

        // Add listeners for toggle button selection
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

        // Create button container
        HBox buttonBox = new HBox(20, smallHallButton, mainHallButton, roomsButton);
        roomsButton.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                combinedSeatingPane.setCenter(roomLayoutPane);
                roomsButton.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
                smallHallButton.setStyle(TOGGLE_BUTTON_STYLE);
                mainHallButton.setStyle(TOGGLE_BUTTON_STYLE);
            }
        });

        // Create button container
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.setPadding(new javafx.geometry.Insets(10));
        buttonBox.setStyle("-fx-background-color: #0A1517;");

        combinedSeatingPane.setBottom(buttonBox);

        // Set default selection
        smallHallButton.setSelected(true);
        combinedSeatingPane.setCenter(smallHallSeatingPane);
        smallHallButton.setStyle(TOGGLE_BUTTON_SELECTED_STYLE);
    }

    /**
     * Creates a fallback placeholder for seating view if FXML loading fails.
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
     * Switches to the booking calendar view for scheduling events.
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
     */
    public void showRegularCalendar() {
        mainContainer.getChildren().setAll(regularCalendarView.getCalendarView());
        setActiveButton(btnCalendar);
    }

    /**
     * Displays the daily sheet calendar view.
     */
    @FXML
    private void showDailySheetCalendar() {
        mainContainer.getChildren().setAll(dailySheetCalenderView.getCalendarView());
        setActiveButton(btnDailySheet);
    }

    /**
     * Switches to the review management view.
     * @throws IOException if the FXML file cannot be loaded
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
     */
    private void showRevenueTracking() {
        mainContainer.getChildren().setAll(revenueTrackingUI);
        setActiveButton(btnRevenue);
    }

    /**
     * Displays the combined seating arrangement view.
     */
    private void showSeatingPane() {
        mainContainer.getChildren().setAll(combinedSeatingPane);
        setActiveButton(btnSeating);
    }

    /**
     * Displays the venue calendar view.
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
     * Sets the active style for the specified button.
     * @param button The button to set as active
     */
    private void setActiveButton(Button button) {
        resetButtonStyles();
        button.setStyle(BUTTON_ACTIVE_STYLE);
    }

    /**
     * Resets all navigation buttons to their default style.
     */
    private void resetButtonStyles() {
        Button[] buttons = {btnBooking, btnCalendar, btnReview, btnRevenue, btnSeating, btnVenue, btnDailySheet};
        for (Button btn : buttons) {
            btn.setStyle(BUTTON_DEFAULT_STYLE);
        }
    }
}