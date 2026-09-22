package com.fastship.platformapi.dto;

import java.time.Instant;

public record ApiError(
        int status,
        String errorCode,
        String message,
        String path,
        Instant timestamp
){
}
