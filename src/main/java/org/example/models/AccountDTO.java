package org.example.models;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.UUID;

@JsonRootName(value = "account")
public record AccountDTO(
        boolean activeCard,
        Long availableLimit) {
}

