package lancaster.model;

public class Account {

    private int accountID;
    private String username;
    private String password;
    private String role;

    public Account(int accountID, String username, String password, String role){
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
