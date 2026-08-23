package com.shkim.CTR.Domain.My.DTO;

import java.time.LocalDateTime;

public record SolveProblemDTO(int id, int pid, LocalDateTime st, LocalDateTime end, long duration, long hour, int status, int memory, int time) {
}
