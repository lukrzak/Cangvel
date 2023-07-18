package com.cangvel.utils.evaluators;

import com.cangvel.models.CvData;
import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;

import java.util.Set;
import java.util.stream.Collectors;

public class DefaultEvaluator implements Evaluator{

    private final CvData cv;
    private Set<String> requiredWordsFoundInFile;
    private Set<String> optionalWordsFoundInFile;

    public DefaultEvaluator(CvData cv) {
        this.cv = cv;
    }

    @Override
    public CvEvaluation evaluateCvFile(CvRequirements requirements) {
        requiredWordsFoundInFile = getKeywordsIncludedInFile(requirements.requiredKeywords());
        optionalWordsFoundInFile = getKeywordsIncludedInFile(requirements.optionalKeywords());
        float evaluation = calculateEvaluationValue(requirements);
        boolean isAccepted = evaluation >= requirements.acceptedThreshold();

        return new CvEvaluation(evaluation, isAccepted, requiredWordsFoundInFile, optionalWordsFoundInFile);
    }

    @Override
    public float calculateEvaluationValue(CvRequirements requirements) {
        int totalAmountOfKeywordsInRequirements = requirements.requiredKeywords().size() + requirements.optionalKeywords().size();
        int totalAmountOfFoundKeywordsInFile = requiredWordsFoundInFile.size() + optionalWordsFoundInFile.size();
        byte fulfillsProfilePictureRequirement = (byte) (cv.hasImage() ? 1 : 0);

        return requirements.containsProfilePictureRequirement()
                ? (float) (totalAmountOfFoundKeywordsInFile + fulfillsProfilePictureRequirement) / (totalAmountOfKeywordsInRequirements + 1)
                : (float) totalAmountOfFoundKeywordsInFile / totalAmountOfKeywordsInRequirements;
    }

    private Set<String> getKeywordsIncludedInFile(Set<String> keywords){
        Set<String> lowercaseKeywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return this.cv.words().stream()
                .filter(lowercaseKeywords::contains)
                .collect(Collectors.toSet());
    }
}
