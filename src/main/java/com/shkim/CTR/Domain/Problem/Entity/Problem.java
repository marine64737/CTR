package com.shkim.CTR.Domain.Problem.Entity;

import java.util.List;

public record Problem(long id, int problemId, String titleKo, boolean isSolvable, boolean isPartial,
                      int acceptedUserCount, int level, int votedUserCount, boolean sprout, boolean givesNoRating,
                      boolean isLevelLocked, double averageTries, boolean official, List<ProblemTag> tags,
                      int platform, String url) {
}
