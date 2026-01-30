package org.example.mapper;

import org.example.domain.Account;
import org.example.model.AccountDTO;

public class AccountMapper {

    public static AccountDTO toAccountDTO(Account account) {
        return new AccountDTO(account.isActiveCard(), account.getAvailableLimit());
    }

    public static Account toAccount(AccountDTO accountDTO) {
        return new Account(accountDTO.activeCard(), accountDTO.availableLimit());
    }
}
