package org.example.mapper;

import org.example.model.AccountDTO;
import org.example.model.TransactionDTO;

public class MapperWrapper {
    private AccountDTO account;
    private TransactionDTO transaction;

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public TransactionDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionDTO transaction) {
        this.transaction = transaction;
    }
}
