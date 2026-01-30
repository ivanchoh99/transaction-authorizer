package org.example.domain;

import org.example.model.TransactionDTO;

import java.time.Instant;

public class Transaction {
    private final String merchant;
    private final long amount;
    private final Instant time;

    public Transaction(TransactionDTO transactionDTO) {
        merchant = transactionDTO.merchant();
        amount = transactionDTO.amount();
        time = Instant.parse(transactionDTO.time());
    }

    public Instant getTime() {
        return time;
    }

    public long getAmount() {
        return amount;
    }

    public String getMerchant() {
        return merchant;
    }
}
