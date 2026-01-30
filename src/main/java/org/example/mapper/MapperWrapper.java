package org.example.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.models.AccountDTO;
import org.example.models.TransactionDTO;

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
