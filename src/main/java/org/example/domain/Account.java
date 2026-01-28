package org.example.domain;

import org.example.models.AccountDTO;
import org.example.models.Transaction;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Account {

    private boolean activeCard;
    private long availableLimit;
    private ConcurrentLinkedQueue<Transaction> transactions;

    public Account(AccountDTO accountDTO) {
        activeCard = accountDTO.activeCard();
        availableLimit = accountDTO.availableLimit();
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public void updateAvailableLimit(Long transactionAmount) {
        if (availableLimit > transactionAmount) {
            availableLimit -= transactionAmount;
        }
    }



    public void changeCardStatus(boolean activeCard) {
        this.activeCard = activeCard;
    }
}
