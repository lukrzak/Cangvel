package com.cangvel.models;

import lombok.Data;

@Data
public class CvEvaluation {
    private float requirementFulfillment;
    private boolean isAccepted;

    public CvEvaluation(float requirementFulfillment, boolean isAccepted) {
        this.requirementFulfillment = requirementFulfillment;
        this.isAccepted = isAccepted;
    }
}
