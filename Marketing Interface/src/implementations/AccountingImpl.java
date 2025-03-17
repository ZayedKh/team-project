package implementations;

import interfaces.AccountingInterface;

public class AccountingImpl implements AccountingInterface {
    @Override
    public void displayIncomeReport() {
        System.out.println("Displaying income report...");
    }
}
