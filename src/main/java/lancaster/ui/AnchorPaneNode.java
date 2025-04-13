package lancaster.ui;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.PopupWindow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;

/**
 * A custom {@code AnchorPane} that encapsulates a calendar view and facilitates booking management.
 * <p>
 * This class extends {@link AnchorPane} to provide a custom UI component that displays a calendar view,
 * handles mouse events for style changes, and allows users to select a booking type via the booking manager.
 * </p>
 */
public class AnchorPaneNode extends AnchorPane {

    private LocalDate date;
    private StackPane mainView;
    private Node calendarView;
    private BookingManager bookingManager;

    /**
     * Constructs a new {@code AnchorPaneNode} with the specified main view, calendar view, and additional child nodes.
     * <p>
     * The constructor initializes the layout's size and default styles, sets up mouse event handlers for hover and click behavior,
     * and initializes the booking manager.
     * </p>
     *
     * @param mainView     the main {@link StackPane} that serves as the container for major UI components.
     * @param calendarView the {@link Node} representing the calendar view.
     * @param children     additional child nodes to be added to this {@code AnchorPaneNode}.
     */
    public AnchorPaneNode(StackPane mainView, Node calendarView, Node... children) {
        super(children);
        this.mainView = mainView;
        this.calendarView = calendarView;
        this.bookingManager = new BookingManager();

        this.setPrefSize(120, 100);
        this.setMinSize(100, 80);

        String originalStyle = "-fx-background-color: #e0ffe4; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";
        String hoverStyle = "-fx-background-color: #F8F8F8; -fx-border-color: #122023; -fx-border-width: 1px;"
                + " -fx-background-radius: 5px; -fx-border-radius: 3px;";

        this.setOnMouseEntered(e -> this.setStyle(hoverStyle));
        this.setOnMouseExited(e -> this.setStyle(originalStyle));
        this.setOnMouseClicked(e -> showBookingTypeSelection());
    }

    /**
     * Displays the booking type selection UI.
     * <p>
     * When the {@code AnchorPaneNode} is clicked, this method is invoked to show the booking type selection interface,
     * passing the main view, calendar view, this node, and its booking manager.
     * </p>
     */
    public void showBookingTypeSelection() {
        new BookingTypeSelectionUI(mainView, calendarView, this, bookingManager).show();
    }

    /**
     * Returns the currently assigned date for the node.
     *
     * @return the date associated with this node.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date for the node.
     *
     * @param date the date to assign to this node.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the booking manager associated with this node.
     *
     * @return the {@link BookingManager} instance used by this node.
     */
    public BookingManager getBookingManager() {
        return bookingManager;
    }
}
