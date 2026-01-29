package org.example.models;

import com.fasterxml.jackson.annotation.JsonRootName;

public record AccountDTO(
        boolean activeCard,
        Long availableLimit) {
}

