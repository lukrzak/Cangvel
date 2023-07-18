package com.cangvel.models;

import com.cangvel.models.requirements.Requirement;

import java.util.Set;

public record CvEvaluation(
        float requirementFulfillment,
        boolean isAccepted,
        Set<String> foundRequiredKeywords,
        Set<String> foundOptionalKeywords,
        Set<Requirement> fulfilledRequirements
) {
}
