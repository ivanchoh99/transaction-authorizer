package org.example.mapper;

import org.example.domain.Transaction;
import org.example.model.AccountDTO;

public class MapperWrapper {
    private AccountDTO account;
    private Transaction transaction;

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
