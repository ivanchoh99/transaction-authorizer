package org.example.model;

public record AccountDTO(
        boolean activeCard,
        Long availableLimit) {
}

