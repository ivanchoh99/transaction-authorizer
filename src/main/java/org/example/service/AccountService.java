package org.example.service;

import org.example.domain.Account;
import org.example.domain.Transaction;
import org.example.mapper.AccountMapper;
import org.example.model.AccountDTO;
import org.example.model.NoInitAccount;
import org.example.model.ResponseDTO;
import org.example.model.TransactionDTO;
import org.example.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    public Account account;

    public synchronized ResponseDTO initAccount(AccountDTO accountDTO) {
        List<String> violations = new ArrayList<>();
        if(account != null) {
            violations.add(Constants.ACCOUNT_INITIALIZED_MESSAGE);
            return new ResponseDTO(AccountMapper.toAccountDTO(this.account), violations);
        }
        account = AccountMapper.toAccount(accountDTO);
        return new ResponseDTO(AccountMapper.toAccountDTO(this.account), violations);
    }

    public synchronized ResponseDTO processTransaction(TransactionDTO transactionDTO) {
        List<String> violations = new ArrayList<>();
        if(account == null) {
            violations.add(Constants.ACCOUNT_NOT_INITIALIZED_MESSAGE);
            return new ResponseDTO(new NoInitAccount(), violations);
        }
        Transaction  transaction = new Transaction(transactionDTO);
        return account.processTransaction(transaction);
    }
}