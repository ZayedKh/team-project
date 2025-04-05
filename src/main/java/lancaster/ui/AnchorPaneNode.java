package lancaster.ui;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.PopupWindow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;

public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;
    private StackPane mainView;
    private Node calendarView;
    private BookingManager bookingManager;



    public AnchorPaneNode(StackPane mainView, Node calendarView, Node... children) {
        super(children);
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.bookingManager = new BookingManager();

        this.setPrefSize(120, 100);
        this.setMinSize(100, 80);

        String originalStyle = "-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";
        String hoverStyle = "-fx-background-color: #FFFFFF; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";

        this.setOnMouseEntered(e -> this.setStyle(hoverStyle));
        this.setOnMouseExited(e -> this.setStyle(originalStyle));
        this.setOnMouseClicked(e -> showBookingTypeSelection()); // Calls the new method
    }

    // Added public method to show booking type selection
    public void showBookingTypeSelection() {
        new BookingTypeSelectionUI(mainView, calendarView, this, bookingManager).show();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BookingManager getBookingManager() {
        return bookingManager;
    }
}