package com.bankmgmt.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankmgmt.app.entity.Account;
import com.bankmgmt.app.service.BankService;

@RestController
public class BankController {
    @Autowired
    private BankService bankService;

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = bankService.createAccount(account.getAccountHolderName(), 0.0);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = bankService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable int id) {
        Account account = bankService.getAccountById(id);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/{id}/deposit")
    public ResponseEntity<String> depositMoney(@PathVariable int id, @RequestParam double amount) {
        Boolean success = bankService.depositMoney(id, amount);
        if (success) {
            return new ResponseEntity<>("Deposit successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/{id}/withdraw")
    public ResponseEntity<String> withdrawMoney(@PathVariable int id, @RequestParam double amount) {
        boolean success = bankService.withdrawMoney(id, amount);
        if (success) {
            return new ResponseEntity<>("WithDrawal Sucessful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<String> transferMoney(@RequestParam int fromAccountId, @RequestParam int toAccountId,
            @RequestParam double amount) {
        boolean success = bankService.transferMoney(fromAccountId, toAccountId, amount);
        if (success) {
            return new ResponseEntity<>("Transfer successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Transfer failed", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable int id) {
        boolean deleted = bankService.deleteAccount(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
