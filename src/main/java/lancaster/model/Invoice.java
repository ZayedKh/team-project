package lancaster.model;

public class Invoice {

    private int invoiceID;
    private Event event;
    private int price;
    private boolean paid;

    /**
     * Constructor for an invoice
     * @param invoiceID ID for an invoice
     * @param event     The event that is being paid for
     * @param price     The price of the event
     * @param paid      Has the invoice been paid?
     */
    public Invoice(int invoiceID, Event event, int price, boolean paid){
        this.invoiceID = invoiceID;
        this.event = event;
        this.price = price;
        this.paid = paid;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public Event getEvent() {
        return event;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPaid() {
        return paid;
    }
}
