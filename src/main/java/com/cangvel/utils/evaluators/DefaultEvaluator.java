package com.cangvel.utils.evaluators;

import com.cangvel.models.CvData;
import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;
import com.cangvel.models.contidions.Requirement;

import java.util.Set;
import java.util.stream.Collectors;

public class DefaultEvaluator implements Evaluator{

    private final CvData cv;
    private Set<String> requiredWordsFoundInFile;
    private Set<String> optionalWordsFoundInFile;
    private Set<Requirement> fulfilledRequirements;

    public DefaultEvaluator(CvData cv) {
        this.cv = cv;
    }

    @Override
    public CvEvaluation evaluateCvFile(CvRequirements requirements) {
        requiredWordsFoundInFile = getKeywordsIncludedInFile(requirements.requiredKeywords());
        optionalWordsFoundInFile = getKeywordsIncludedInFile(requirements.optionalKeywords());
        fulfilledRequirements = getFulfilledRequirements(requirements.requirements());

        float evaluation = calculateEvaluationValue(requirements);
        boolean isAccepted = evaluation >= requirements.acceptedThreshold();

        return new CvEvaluation(evaluation, isAccepted, requiredWordsFoundInFile, optionalWordsFoundInFile, fulfilledRequirements);
    }

    @Override
    public float calculateEvaluationValue(CvRequirements requirements) {
        int totalKeywordsAndRequirementsMet = requiredWordsFoundInFile.size()
                + optionalWordsFoundInFile.size()
                + fulfilledRequirements.size();
        int totalKeywordsAndRequirements = requirements.requiredKeywords().size()
                + requirements.optionalKeywords().size()
                + requirements.requirements().size();

        return (float) totalKeywordsAndRequirementsMet / totalKeywordsAndRequirements;
    }

    private Set<String> getKeywordsIncludedInFile(Set<String> keywords){
        Set<String> lowercaseKeywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return this.cv.words().stream()
                .filter(lowercaseKeywords::contains)
                .collect(Collectors.toSet());
    }

    private Set<Requirement> getFulfilledRequirements(Set<Requirement> requirements){
        return requirements.stream()
                .filter(r -> r.checkRequirement(cv))
                .collect(Collectors.toSet());
    }
}
