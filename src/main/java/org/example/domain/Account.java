package org.example.domain;

import org.example.models.AccountDTO;
import org.example.models.ResponseDTO;
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

    public Account(AccountDTO accountDTO) {
        activeCard = accountDTO.activeCard();
        availableLimit = accountDTO.availableLimit();
    }

    public synchronized ResponseDTO processTransaction(Transaction transactionToProcess) {
        Instant timeBefore = Instant.now().minusSeconds(Constants.LIMIT_TIME_SECONDS);
        this.cleanTransactions(timeBefore);
        List<String> violations = validate(transactionToProcess, timeBefore);
        if (violations.isEmpty()) {
            transactions.add(transactionToProcess);
            availableLimit -= transactionToProcess.getAmount();
        }
        return new ResponseDTO(activeCard, availableLimit, violations);
    }

    private List<String> validate(Transaction transactionToProcess, Instant timeBefore) {
        List<String> violations = new ArrayList<>();
        if (!activeCard) {
            violations.add(Constants.CARD_NO_ACTIVE_MESSAGE);
        }
        if (availableLimit < transactionToProcess.getAmount()) {
            violations.add(Constants.INSUFFICIENT_BALANCE_MESSAGE);
        }
        if (violations.isEmpty()) {
            if (numberTransactionInLimitTime(timeBefore)) {
                violations.add(Constants.TRANSACTION_LIMIT_IN_TIME_MESSAGE);
            }
            if (repeatedTransactionInLimitTime(transactionToProcess, timeBefore)) {
                violations.add(Constants.TRANSACTION_REPEATED);
            }
        }
        return violations;
    }

    private boolean numberTransactionInLimitTime(Instant timeBefore) {
        return transactions.size() > Constants.LIMIT_TRANSACTION_IN_TIME;
    }

    private boolean repeatedTransactionInLimitTime(Transaction transactionToProcess, Instant timeBefore) {
        return transactions.stream().anyMatch(transaction ->
                transaction.getAmount() == transactionToProcess.getAmount() &&
                        transaction.getMerchant().equals(transactionToProcess.getMerchant()));
    }

    private void cleanTransactions(Instant timeBefore) {
        while (!transactions.isEmpty() && transactions.peekFirst().getTime().isBefore(timeBefore)) {
            transactions.pollFirst();
        }
    }
}
