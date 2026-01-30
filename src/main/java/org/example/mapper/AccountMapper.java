package org.example.mapper;

import org.example.domain.Account;
import org.example.model.AccountDTO;

import java.util.ArrayDeque;

public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        return new AccountDTO(account.isActiveCard(), account.getAvailableLimit());
    }

    public static Account toDomain(AccountDTO accountDTO) {
        return new Account(accountDTO.activeCard(), accountDTO.availableLimit(), new ArrayDeque<>());
    }
}
