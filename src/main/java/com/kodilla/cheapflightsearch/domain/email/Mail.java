package com.kodilla.cheapflightsearch.domain.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Mail {
    private final String mailTo;
    private final String mailToCc;
    private final String subject;
    private final String message;
}
