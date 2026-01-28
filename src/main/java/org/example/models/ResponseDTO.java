package org.example.models;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value = "account")
public record ResponseDTO(
        boolean activeCard,
        long availableLimit,
        List<String> violations
) {
}
