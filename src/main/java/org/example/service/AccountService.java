package org.example.service;

import org.example.domain.Account;
import org.example.domain.Transaction;
import org.example.models.AccountDTO;
import org.example.models.ResponseDTO;
import org.example.models.TransactionDTO;
import org.example.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    public Account account;

    public synchronized ResponseDTO initAccount(AccountDTO accountDTO) {
        List<String> violations = new ArrayList<>();
        if(account != null) {
            violations.add(Constants.ACCOUNT_INITIALIZED_MESSAGE);
            return new ResponseDTO(account.isActiveCard(), account.getAvailableLimit(), violations);
        }
        account = new Account(accountDTO);
        return new ResponseDTO(account.isActiveCard(), account.getAvailableLimit(), violations);
    }

    public synchronized ResponseDTO processTransaction(TransactionDTO transactionDTO) {
        List<String> violations = new ArrayList<>();
        if(account == null) {
            violations.add(Constants.ACCOUNT_NOT_INITIALIZED_MESSAGE);
            return new ResponseDTO(false,0, violations);
        }
        Transaction  transaction = new Transaction(transactionDTO);
        return account.processTransaction(transaction);
    }
}