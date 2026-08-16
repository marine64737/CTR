package com.shkim.CTR.Domain.My.DTO;

import java.util.List;

public record HomeDTO(
        List<SolvedProblemsDTO> problems,
        List<UnsolvedProblemsDTO> notSolved,
        int solvedCount,
        int totalCount
) { }
