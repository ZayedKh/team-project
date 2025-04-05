package lancaster.model;

public class Client {

    private int clientID;
    private String productionCompany;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String postcode;
    private String billingName;
    private String billingAddress;

    /**
     * Constructor for client
     * @param clientID          ID of the stored client
     * @param productionCompany The production company handling the event, this may be null
     *                          if client is not for a stage event
     * @param name              Name of client
     * @param email             Email address of the client
     * @param phoneNumber       Phone number of the client
     * @param address           Address of client
     * @param city              City of the client address
     * @param postcode          Postcode of client address
     * @param billingName       Name of the payee
     * @param billingAddress    Address of the payee
     */
    public Client(int clientID, String productionCompany, String name, String email,
                  String phoneNumber, String address, String city, String postcode,
                  String billingName, String billingAddress){

        this.clientID = clientID;
        this.productionCompany = productionCompany;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.billingName = billingName;
        this.billingAddress = billingAddress;
    }

    public int getClientID() {
        return clientID;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getBillingName() {
        return billingName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }
}
