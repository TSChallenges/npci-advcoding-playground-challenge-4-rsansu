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
    public Account createAccount(String accountHolderName, String accountType, String email) {
        if (accountHolderName == null || accountHolderName.isEmpty()) {
            throw new IllegalArgumentException("Account Holder Name cannot be null or empty.");
        }
        if (accountType == null || accountType.isEmpty()) {
            throw new IllegalArgumentException("Account Type cannot be null or empty.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (accounts.stream().anyMatch(account -> account.getEmail().equals(email))) {
            throw new IllegalArgumentException("Email must be unique.");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (!accountType.equals("SAVINGS") && !accountType.equals("CURRENT")) {
            throw new IllegalArgumentException("Account type must be either SAVINGS or CURRENT.");
        }
        Account newAccount = new Account(currentId++, accountHolderName, 0.0, accountType, email);
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
            validateAmount(amount);
            account.setBalance(account.getBalance() + amount);
            return true;
        }
        return false;
    }

    // Method to Withdraw Money from the specified account id
    public boolean withdrawMoney(int accountId, double amount) {
        Account account = getAccountById(accountId);
        if (account != null) {
            validateAmount(amount);
            if (account.getBalance() >= amount) {
                account.setBalance(account.getBalance() - amount);
                return true;
            } else {
                throw new IllegalArgumentException("Insufficient balance.");
            }
        }
        return false;
    }

    // Method to Transfer Money from one account to another
    public boolean transferMoney(int fromAccountId, int toAccountId, double amount) {
        Account fromAccount = getAccountById(fromAccountId);
        Account toAccount = getAccountById(toAccountId);
        if (fromAccount != null && toAccount != null) {
            validateAmount(amount);
            if (fromAccount.getBalance() >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);
                return true;
            } else {
                throw new IllegalArgumentException("Insufficient balance.");
            }
        }
        return false;
    }

    // Method to Delete Account given an account id
    public boolean deleteAccount(int accountId) {
        return accounts.removeIf(account -> account.getId() == accountId);
    }

    // Method to validate amount
    public void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }
}
