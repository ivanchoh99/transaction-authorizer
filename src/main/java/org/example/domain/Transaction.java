package org.example.domain;

import java.time.Instant;

public record Transaction
        (String merchant,
         long amount,
         Instant time) {

}
