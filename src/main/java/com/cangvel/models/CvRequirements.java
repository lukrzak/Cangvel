package com.cangvel.models;

import java.util.Set;

public record CvRequirements(
        Set<String> requiredKeywords,
        Set<String> optionalKeywords,
        boolean containsProfilePictureRequirement,
        float acceptedThreshold) {
}
