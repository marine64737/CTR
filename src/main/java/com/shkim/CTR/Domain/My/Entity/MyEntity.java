package com.shkim.CTR.Domain.My.Entity;

import java.time.LocalDateTime;

public record MyEntity(
    int id,
    int userId,
    int problemId,
    LocalDateTime start_time,
    LocalDateTime end_time,
    int status,
    String code,
    String memo,
    boolean nonvisible,
    int memory,
    int time,
    int platform
) {
}
