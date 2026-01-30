package org.example;

import org.example.input.Consumer;
import org.example.service.AccountService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        Consumer consumer = new Consumer(accountService);
        try {
            consumer.consume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}