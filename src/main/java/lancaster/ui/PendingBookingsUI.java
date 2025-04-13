package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lancaster.model.BookingDetails;

import java.util.List;

/**
 * The PendingBookingsUI class is responsible for displaying all pending bookings in the application.
 * <p>
 * This class constructs a user interface for showing pending booking details,
 * provides options to add more bookings, submit all bookings, or clear all bookings.
 * It uses various UI utility methods to create and style the required components.
 * </p>
 *
 * @see BookingManager
 * @see BookingTypeSelectionUI
 * @see UIUtils
 */
public class PendingBookingsUI {
    private StackPane mainView;
    private Node calendarView;
    private AnchorPaneNode anchorPaneNode;
    private BookingManager bookingManager;

    /**
     * Constructs a PendingBookingsUI instance with the necessary UI components and booking manager.
     *
     * @param mainView       the main container pane that holds the pending bookings UI
     * @param calendarView   the node representing the calendar view for navigation purposes
     * @param anchorPaneNode the anchor pane node providing additional context or UI features
     * @param bookingManager the manager that handles booking operations and booking data
     */
    public PendingBookingsUI(StackPane mainView, Node calendarView, AnchorPaneNode anchorPaneNode, BookingManager bookingManager) {
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.anchorPaneNode = anchorPaneNode;
        this.bookingManager = bookingManager;
    }

    /**
     * Displays the pending bookings screen.
     * <p>
     * This method constructs the UI for the pending bookings page, which includes:
     * <ul>
     *   <li>A header with navigation options.</li>
     *   <li>A list of booking cells, each representing a pending booking.</li>
     *   <li>Buttons that allow the user to add more bookings, submit all bookings (after conflict checks), or clear all bookings.</li>
     * </ul>
     * If a booking has any conflicts, its cell is styled differently to alert the user.
     * </p>
     */
    public void show() {
        BorderPane pendingPage = new BorderPane();
        pendingPage.setPadding(new Insets(30));
        pendingPage.setPrefSize(800, 600);
        pendingPage.setStyle("-fx-background-color: #122023;");

        HBox header = UIUtils.createHeader("Pending Bookings",
                e -> new BookingTypeSelectionUI(mainView, calendarView, anchorPaneNode, bookingManager).show(),
                bookingManager.getBookingGroup());
        pendingPage.setTop(header);

        VBox bookingsList = new VBox(15);
        bookingsList.setPadding(new Insets(30));
        bookingsList.setMaxWidth(600);
        bookingsList.setAlignment(Pos.CENTER);

        for (int i = 0; i < bookingManager.getBookingGroup().getBookings().size(); i++) {
            BookingDetails booking = bookingManager.getBookingGroup().getBookings().get(i);
            HBox bookingCell = UIUtils.createBookingCell(booking, i, bookingManager.getBookingGroup(), () -> show());
            if (bookingManager.isBookingConflicting(booking)) {
                bookingCell.setStyle("-fx-background-color: #e66b7e; -fx-border-color: #122023; -fx-border-width: 1px; " +
                        "-fx-padding: 10px 20px; -fx-background-radius: 5px;");
                bookingCell.setAlignment(Pos.CENTER);
            }
            bookingsList.getChildren().add(bookingCell);
        }

        StackPane centerWrapper = new StackPane(bookingsList);
        centerWrapper.setAlignment(Pos.CENTER);
        pendingPage.setCenter(centerWrapper);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0));

        Button addMoreButton = new Button("Add More");
        addMoreButton.setPrefSize(150, 40);
        addMoreButton.setStyle("-fx-font-size: 16px;");
        addMoreButton.setOnAction(e -> new BookingTypeSelectionUI(mainView, calendarView, anchorPaneNode, bookingManager).show());

        Button submitAllButton = new Button("Submit All");
        submitAllButton.setPrefSize(150, 40);
        submitAllButton.setStyle("-fx-font-size: 16px;");
        submitAllButton.setOnAction(e -> {
            if (bookingManager.hasConflicts()) {
                UIUtils.showAlert("Error", "There are conflicting bookings. Please resolve these conflicts before submitting.");
            } else {
                bookingManager.getBookingGroup().submitAll();
                UIUtils.showAlert("Success", "All bookings have been submitted.");
                mainView.getChildren().setAll(calendarView);
            }
        });

        Button clearAllButton = new Button("Clear All");
        clearAllButton.setPrefSize(150, 40);
        clearAllButton.setStyle("-fx-font-size: 16px;");
        clearAllButton.setOnAction(e -> {
            bookingManager.getBookingGroup().getBookings().clear();
            new BookingTypeSelectionUI(mainView, calendarView, anchorPaneNode, bookingManager).show();
        });

        buttonBox.getChildren().addAll(addMoreButton, submitAllButton, clearAllButton);
        pendingPage.setBottom(buttonBox);

        mainView.getChildren().setAll(pendingPage);
    }
}
