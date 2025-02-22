package com.bankmgmt.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bankmgmt.app.entity.Account;

@Service
public class BankService {

    private final List<Account> accounts = new ArrayList<>();
    private Integer currentId = 1;

    // Method to Create a new Account
    public Account createAccount(String accountHolderName, double initialBalance ) {
        Account newAccount = new Account(currentId++, accountHolderName, "defaultType", initialBalance,
                "defaultStatus");
        accounts.add(newAccount);
        return newAccount;
    }

    // Method to Get All Accounts
    public List<Account> getAllAccounts() {
        return accounts;
    }

    // Method to Get Account by ID
    public Account getAccountById(int accountId) {
        return accounts.stream()
                .filter(account -> account.getId() == accountId)
                .findFirst()
                .orElse(null);
    }

    // Method to Deposit Money to the specified account id
    public boolean depositMoney(int accountId, double amount) {
        Account account = getAccountById(accountId);
        if (account != null) {
            account.setBalance(account.getBalance() + amount);
            return true;
        }
        return false;
    }

    // Method to Withdraw Money from the specified account id
    public boolean withdrawMoney(int accountId, double amount) {
        Account account = getAccountById(accountId);
        if (account != null && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return true;
        }
        return false;
    }

    // Method to Transfer Money from one account to another
    public boolean transferMoney(int fromAccountId, int toAccountId, double amount) {
        if (withdrawMoney(fromAccountId, amount)) {
            return depositMoney(toAccountId, amount);
        }
        return false;
    }

    // Method to Delete Account given an account id
    public boolean deleteAccount(int accountId) {
        return accounts.removeIf(account -> account.getId() == accountId);
    }
}
