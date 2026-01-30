package org.example.domain;

import org.example.mapper.AccountMapper;
import org.example.model.AccountDTO;
import org.example.model.ResponseDTO;
import org.example.util.Constants;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Account {

    private boolean activeCard;
    private long availableLimit;
    private final Deque<Transaction> transactions = new ArrayDeque<>();

    public boolean isActiveCard() {
        return activeCard;
    }

    public void setActiveCard(boolean activeCard) {
        this.activeCard = activeCard;
    }

    public long getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(long availableLimit) {
        this.availableLimit = availableLimit;
    }

    public Account(boolean activeCard, long availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    public synchronized ResponseDTO processTransaction(Transaction transactionToProcess) {
        this.cleanTransactions(transactionToProcess.getTime());
        List<String> violations = validate(transactionToProcess);
        if (violations.isEmpty()) {
            transactions.add(transactionToProcess);
            availableLimit -= transactionToProcess.getAmount();
        }
        return new ResponseDTO(AccountMapper.toAccountDTO(this), violations);
    }

    private List<String> validate(Transaction transactionToProcess) {
        List<String> violations = new ArrayList<>();
        if (!activeCard) {
            violations.add(Constants.CARD_NO_ACTIVE_MESSAGE);
        }
        if (availableLimit < transactionToProcess.getAmount()) {
            violations.add(Constants.INSUFFICIENT_BALANCE_MESSAGE);
        }
        if (numberTransactionInLimitTime()) {
            violations.add(Constants.TRANSACTION_LIMIT_IN_TIME_MESSAGE);
        }
        if (repeatedTransactionInLimitTime(transactionToProcess)) {
            violations.add(Constants.TRANSACTION_REPEATED);
        }
        return violations;
    }

    private boolean numberTransactionInLimitTime() {
        return transactions.size() >= Constants.LIMIT_TRANSACTION_IN_TIME;
    }

    private boolean repeatedTransactionInLimitTime(Transaction transactionToProcess) {
        return transactions.stream().anyMatch(transaction ->
                transaction.getAmount() == transactionToProcess.getAmount() &&
                        transaction.getMerchant().equals(transactionToProcess.getMerchant()));
    }

    private void cleanTransactions(Instant referenceTime) {
        Instant limitTime = referenceTime.minusSeconds(Constants.LIMIT_TRANSACTION_IN_TIME);
        while (!transactions.isEmpty() && transactions.peekFirst().getTime().isBefore(limitTime)) {
            transactions.pollFirst();
        }
    }
}
