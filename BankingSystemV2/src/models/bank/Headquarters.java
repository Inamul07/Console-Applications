package models.bank;

import java.util.*;

public class Headquarters extends Bank {

    private final Map<Integer, Branch> branches = new HashMap<>();

    public Headquarters(int bankId, String bankName) {
        super(bankId, bankName);
    }

    public void addBranch(int bankId, String bankName) {
        if(bankId == this.getBankId()) {
            throw new IllegalArgumentException("Branch cannot have same ID as Headquarters");
        }
        Branch branch = new Branch(bankId, bankName, this);
        branches.put(bankId, branch);
    }

    public Branch getBranch(int bankId) {
        if(!branches.containsKey(bankId)) {
            throw new NoSuchElementException("Bank with Id: " + bankId + ", not found");
        }
        return branches.get(bankId);
    }

    public void viewAllBranches() {
        System.out.printf("%-15s %-15s\n", "Bank Id", "Bank Name");
        for(Branch branch: branches.values()) {
            System.out.println(branch);
        }
        System.out.println();
    }
}
