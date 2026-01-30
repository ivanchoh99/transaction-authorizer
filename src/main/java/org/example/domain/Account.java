package org.example.domain;

import org.example.model.RegisterResult;
import org.example.util.Constants;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public record Account(
        boolean activeCard,
        long availableLimit,
        Deque<Transaction> transactions) {

    public boolean isActiveCard() {
        return activeCard;
    }

    public long getAvailableLimit() {
        return availableLimit;
    }

    public RegisterResult processTransaction(Transaction transactionToProcess) {
        Deque<Transaction> filterHistory = this.cleanTransactions(transactionToProcess.time());
        List<String> violations = validate(transactionToProcess, filterHistory);

        if (!violations.isEmpty()) {
            return new RegisterResult(this, violations);
        }
        long newAmount = this.availableLimit - transactionToProcess.amount();
        filterHistory.addLast(transactionToProcess);
        Account nextState = new Account(this.isActiveCard(), newAmount, filterHistory);
        return new RegisterResult(nextState, List.of());
    }

    private List<String> validate(Transaction transactionToProcess, Deque<Transaction> filterHistory) {
        List<String> violations = new ArrayList<>();
        if (!activeCard) {
            violations.add(Constants.CARD_NO_ACTIVE_MESSAGE);
        }
        if (availableLimit < transactionToProcess.amount()) {
            violations.add(Constants.INSUFFICIENT_BALANCE_MESSAGE);
        }
        if (numberTransactionInLimitTime(filterHistory)) {
            violations.add(Constants.TRANSACTION_LIMIT_IN_TIME_MESSAGE);
        }
        if (repeatedTransactionInLimitTime(transactionToProcess, filterHistory)) {
            violations.add(Constants.TRANSACTION_REPEATED);
        }
        return violations;
    }

    private boolean numberTransactionInLimitTime(Deque<Transaction> filterHistory) {
        return filterHistory.size() >= Constants.LIMIT_TRANSACTION_IN_TIME;
    }

    private boolean repeatedTransactionInLimitTime(Transaction transactionToProcess, Deque<Transaction> filterHistory) {
        return filterHistory.stream()
                .anyMatch(transaction ->
                        transaction.amount() == transactionToProcess.amount() &&
                                transaction.merchant().equals(transactionToProcess.merchant()));
    }

    private Deque<Transaction> cleanTransactions(Instant referenceTime) {
        Instant limitTime = referenceTime.minusSeconds(Constants.LIMIT_TIME_SECONDS);
        Deque<Transaction> newHistory = new ArrayDeque<>(this.transactions);
        while (!newHistory.isEmpty() && newHistory.peekFirst().time().isBefore(limitTime)) {
            newHistory.pollFirst();
        }
        return newHistory;
    }
}
