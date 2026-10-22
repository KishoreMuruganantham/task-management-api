package com.springboot.taskapi.dto.response;

import java.util.Date;

public record TokenDto(
        String token,
        Date expiryTime,
        String issueTime
) {
}
