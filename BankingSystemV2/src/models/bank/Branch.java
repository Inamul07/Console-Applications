package models.bank;

import database.Database;

public class Branch extends Bank {

    private final Headquarters headquarters;

    public Branch(int bankId, String bankName, Headquarters branchOf) {
        super(bankId, bankName);
        this.headquarters = branchOf;
    }

    public void printHeadquartersInfo() {
        System.out.printf("%-15s %-15s\n", "Bank Id", "Bank Name");
        System.out.println(headquarters);
    }
}
