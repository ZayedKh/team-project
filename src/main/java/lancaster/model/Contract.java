package lancaster.model;

public class Contract {

    private int contractID;
    private Client client;
    private Booking booking;
    private boolean approved;

    /**
     * Constructor for a contract
     * @param contractID    ID of contract
     * @param client        The client that books the show
     * @param booking       The booking the client has asked to book
     * @param approved      Has the contract been approved by both sides
     */
    public Contract(int contractID, Client client, Booking booking, boolean approved){
        this.contractID = contractID;
        this.client = client;
        this.booking = booking;
        this.approved = approved;
    }

    public int getContractID() {
        return contractID;
    }

    public Client getClient() {
        return client;
    }

    public Booking getBooking() {
        return booking;
    }

    public boolean isApproved() {
        return approved;
    }

}
