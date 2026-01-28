package org.example.models;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonRootName(value = "transaction")
public record Transaction(
        String merchant,
        String amount,
        LocalDateTime time) {
}
