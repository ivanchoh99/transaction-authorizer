package org.example.model;

import java.util.List;

public record RegisterResult(
        Object account,
        List<String> violations
) {
}
