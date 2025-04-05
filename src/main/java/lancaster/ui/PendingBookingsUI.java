package lancaster.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PendingBookingsUI {
    private StackPane mainView;
    private Node calendarView;
    private AnchorPaneNode anchorPaneNode;
    private BookingManager bookingManager;

    public PendingBookingsUI(StackPane mainView, Node calendarView, AnchorPaneNode anchorPaneNode, BookingManager bookingManager) {
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.anchorPaneNode = anchorPaneNode;
        this.bookingManager = bookingManager;
    }

    public void show() {
        BorderPane pendingPage = new BorderPane();
        pendingPage.setPadding(new Insets(30));
        pendingPage.setPrefSize(800, 600);
        pendingPage.setStyle("-fx-background-color: #122023;");

        HBox header = UIUtils.createHeader("Pending Bookings", e -> new BookingTypeSelectionUI(mainView, calendarView, anchorPaneNode, bookingManager).show(), bookingManager.getBookingGroup());
        pendingPage.setTop(header);

        VBox bookingsList = new VBox(15);
        bookingsList.setPadding(new Insets(30));
        bookingsList.setMaxWidth(600);
        bookingsList.setAlignment(Pos.CENTER);

        for (int i = 0; i < bookingManager.getBookingGroup().getBookings().size(); i++) {
            var booking = bookingManager.getBookingGroup().getBookings().get(i);
            HBox bookingCell = UIUtils.createBookingCell(booking, i, bookingManager.getBookingGroup(), () -> show()); // Added bookingManager.getBookingGroup()
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
            bookingManager.getBookingGroup().submitAll();
            UIUtils.showAlert("Success", "All bookings have been submitted.");
            mainView.getChildren().setAll(calendarView);
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