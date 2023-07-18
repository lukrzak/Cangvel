package com.cangvel.models;

import com.cangvel.models.requirements.Requirement;

import java.util.Set;

public record CvRequirements(
        Set<String> requiredKeywords,
        Set<String> optionalKeywords,
        Set<Requirement> requirements,
        float acceptedThreshold) {
}
