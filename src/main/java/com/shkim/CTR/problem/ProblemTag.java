package com.shkim.CTR.problem;

import java.util.List;

public record ProblemTag(String key, int bojTagId, int problemCount, List<ProblemTagNameTranslated> displayNames){
}
