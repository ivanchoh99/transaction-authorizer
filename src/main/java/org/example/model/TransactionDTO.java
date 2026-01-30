package org.example.model;

public record TransactionDTO(
        String merchant,
        long amount,
        String time) {
}
