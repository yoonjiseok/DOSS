package com.example.moveon.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatus {
    PENDING("pending"),
    COMPLETE("complete");

    private final String status;
}
