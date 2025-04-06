package lancaster.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    @FXML
    private Button btnVenue;

    private FullCalendarView bookingCalendarView;
    private FullCalendarView regularCalendarView;
    private RevenueTrackingUI revenueTrackingUI;
    private Node reviewPane;
    private Node seatingPane;
    private Node homePane;

    private final String BUTTON_DEFAULT_STYLE = "-fx-background-color: transparent; -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: transparent;";
    private final String BUTTON_ACTIVE_STYLE = "-fx-background-color: rgba(46, 204, 64, 0.15); -fx-text-fill: white; -fx-border-width: 0 0 0 5; -fx-border-color: #2ECC40;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homePane = mainContainer.getChildren().get(0);
        setupButtonActions();
        initializeViews();
        resetButtonStyles();
    }

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
    }

    private void initializeViews() {
        bookingCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        regularCalendarView = new FullCalendarView(YearMonth.now(), mainContainer, homePane);
        revenueTrackingUI = new RevenueTrackingUI();
        createPlaceholderPanes();
    }

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

    public void showHomePage() {
        mainContainer.getChildren().setAll(homePane);
        resetButtonStyles();
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

    public void showRegularCalendar() {
        mainContainer.getChildren().setAll(regularCalendarView.getCalendarView());
        setActiveButton(btnCalendar);
    }


    /**
     * Switches to the review management placeholder pane.
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

    private void showRevenueTracking() {
        mainContainer.getChildren().setAll(revenueTrackingUI);
        setActiveButton(btnRevenue);
    }

    private void showSeatingPane() {
        mainContainer.getChildren().setAll(seatingPane);
        setActiveButton(btnSeating);
    }
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
    // Button styling helpers
    private void setActiveButton(Button button) {
        resetButtonStyles();
        button.setStyle(BUTTON_ACTIVE_STYLE);
    }

    private void resetButtonStyles() {
        Button[] buttons = {btnBooking, btnCalendar, btnReview, btnReport, btnSeating, btnVenue};
        for (Button btn : buttons) {
            btn.setStyle(BUTTON_DEFAULT_STYLE);
        }
    }
}