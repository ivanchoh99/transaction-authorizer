package org.example.models;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.time.LocalDateTime;

public record TransactionDTO(
        String merchant,
        long amount,
        String time) {
}
