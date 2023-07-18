package com.cangvel.models;

import java.util.Set;

public record CvEvaluation(
        float requirementFulfillment,
        boolean isAccepted,
        Set<String>foundRequiredKeywords,
        Set<String> foundOptionalKeywords
) {
}
