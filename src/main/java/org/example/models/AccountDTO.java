package org.example.models;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "account")
public record AccountDTO(
        boolean activeCard,
        Long availableLimit) {
}

