package org.example.model;

import java.util.List;

public record ResponseDTO(
        Object account,
        List<String> violations
) {
}
