package org.example.service;

import org.example.domain.Account;
import org.example.domain.Transaction;
import org.example.mapper.AccountMapper;
import org.example.model.AccountDTO;
import org.example.model.NoInitAccount;
import org.example.model.RegisterResult;
import org.example.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AccountService {

    private final AtomicReference<Account> accountRef = new AtomicReference<>(null);

    public RegisterResult initAccount(AccountDTO dto) {
        Account newAccount = AccountMapper.toDomain(dto);
        boolean success = accountRef.compareAndSet(null, newAccount);
        if (success) {
            return new RegisterResult(dto, List.of());
        }
        List<String> violations = new ArrayList<>();
        violations.add(Constants.ACCOUNT_INITIALIZED_MESSAGE);
        Account existingAccount = accountRef.get();
        return new RegisterResult(AccountMapper.toDTO(existingAccount), violations);
    }

    public RegisterResult processTransaction(Transaction transaction) {
        List<String> violations = new ArrayList<>();
        while (true) {
            Account currentState = accountRef.get();
            if (currentState == null) {
                violations.add(Constants.ACCOUNT_NOT_INITIALIZED_MESSAGE);
                return new RegisterResult(new NoInitAccount(), violations);
            }

            RegisterResult result = currentState.processTransaction(transaction);
            if (accountRef.compareAndSet(currentState, (Account) result.account())) {
                return new RegisterResult(AccountMapper.toDTO(currentState), violations);
            }
        }
    }

    public AtomicReference<Account> getAccountRef() {
        return accountRef;
    }
}