package com.cangvel.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CvEvaluation {
    private float requirementFulfillment;
    private boolean isAccepted;
    private final Set<String> foundRequiredKeywords;
    private final Set<String> foundOptionalKeywords;

    public CvEvaluation(float acceptanceThreshold, float requirementFulfillment, Set<String> foundRequiredKeywords, Set<String> foundOptionalKeywords) {
        this.requirementFulfillment = requirementFulfillment;
        this.foundRequiredKeywords = foundRequiredKeywords;
        this.foundOptionalKeywords = foundOptionalKeywords;
        this.isAccepted = requirementFulfillment >= acceptanceThreshold;
    }
}
