package com.shkim.CTR;

import java.util.List;

public record Problem(int problemId, String titleKo, List<Title> titles, boolean isSolvable, boolean isPartial,
                      int acceptedUserCount, int level, int votedUserCount, boolean sprout, boolean givesNoRating,
                      boolean isLevelLocked, double averageTries, boolean official, List<Tags> tags) {
}
