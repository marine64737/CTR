package com.shkim.CTR.question;

import java.util.List;

public record ProblemTag(String key, int bojTagId, int problemCount, List<ProblemTagNameTranslated> displayNames){
}
