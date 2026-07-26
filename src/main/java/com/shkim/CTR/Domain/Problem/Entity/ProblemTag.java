package com.shkim.CTR.Domain.Problem.Entity;

import java.util.List;

public record ProblemTag(String key, int bojTagId, int problemCount, List<ProblemTagNameTranslated> displayNames){
}
