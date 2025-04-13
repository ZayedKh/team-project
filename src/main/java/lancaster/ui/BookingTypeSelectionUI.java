package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Provides a user interface for selecting the booking type.
 * <p>
 * This class displays a UI with options for single-day and multi-day bookings.
 * It allows the user to navigate back to the calendar view or proceed to specific booking screens.
 * </p>
 */
public class BookingTypeSelectionUI {
    private StackPane mainView;
    private Node calendarView;
    private AnchorPaneNode anchorPaneNode;
    private BookingManager bookingManager;

    /**
     * Constructs a new {@code BookingTypeSelectionUI} instance.
     *
     * @param mainView      the main {@link StackPane} that serves as the container for the UI.
     * @param calendarView  the {@link Node} representing the calendar view.
     * @param anchorPaneNode the {@link AnchorPaneNode} associated with the current booking context.
     * @param bookingManager the {@link BookingManager} managing bookings.
     */
    public BookingTypeSelectionUI(StackPane mainView, Node calendarView, AnchorPaneNode anchorPaneNode, BookingManager bookingManager) {
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.anchorPaneNode = anchorPaneNode;
        this.bookingManager = bookingManager;
    }

    /**
     * Displays the booking type selection screen.
     * <p>
     * This method sets up a {@link BorderPane} layout with a header containing a back button and title,
     * as well as a central area offering buttons for single-day and multi-day booking options.
     * Clicking the corresponding button navigates to the respective booking screen.
     * </p>
     */
    public void show() {
        BorderPane typeScreen = new BorderPane();
        typeScreen.setPadding(new Insets(30));
        typeScreen.setPrefSize(800, 600);
        typeScreen.setStyle("-fx-background-color: #122023;");

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));

        Button backButton = new Button("Back");
        backButton.setPrefSize(100, 30);
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(e -> mainView.getChildren().setAll(calendarView));

        Label titleLabel = new Label("Select Booking Type");
        StackPane titleWrapper = new StackPane();
        titleWrapper.setStyle("-fx-background-color: #2ECC40; -fx-padding: 10px 25px; -fx-background-radius: 5px;");
        titleWrapper.setPadding(new Insets(8));
        titleWrapper.getChildren().add(titleLabel);
        titleLabel.setStyle("-fx-font-size: 20px;");

        header.getChildren().addAll(backButton, titleWrapper);
        typeScreen.setTop(header);

        VBox options = new VBox(20);
        options.setAlignment(Pos.CENTER);

        Button singleBookingButton = new Button("Single Day Booking");
        singleBookingButton.setPrefSize(200, 50);
        singleBookingButton.setStyle("-fx-font-size: 16px;");
        singleBookingButton.setOnAction(e -> new SingleBookingUI(mainView, calendarView, anchorPaneNode, bookingManager).showSingleBookingScreen());

        Button multiDayBookingButton = new Button("Multi-day Booking");
        multiDayBookingButton.setPrefSize(200, 50);
        multiDayBookingButton.setStyle("-fx-font-size: 16px;");
        multiDayBookingButton.setOnAction(e -> new MultiDayBookingUI(mainView, calendarView, anchorPaneNode, bookingManager).showMultiDayBookingScreen());

        options.getChildren().addAll(singleBookingButton, multiDayBookingButton);
        typeScreen.setCenter(options);

        mainView.getChildren().setAll(typeScreen);
    }
}
