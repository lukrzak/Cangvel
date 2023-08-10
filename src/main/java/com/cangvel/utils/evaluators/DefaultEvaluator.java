package com.cangvel.utils.evaluators;

import com.cangvel.models.CvData;
import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;
import com.cangvel.models.requirements.Requirement;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Log4j2
public class DefaultEvaluator implements Evaluator {

    private CvData cv;

    private Set<String> requiredWordsFoundInFile;

    private Set<String> optionalWordsFoundInFile;

    private Set<Requirement> fulfilledRequirements;

    @Override
    public CvEvaluation evaluateCvFile(CvRequirements requirements, CvData cvData) {
        cv = cvData;
        requiredWordsFoundInFile = getKeywordsIncludedInFile(requirements.requiredKeywords());
        optionalWordsFoundInFile = getKeywordsIncludedInFile(requirements.optionalKeywords());
        fulfilledRequirements = getFulfilledRequirements(requirements.requirements());

        float evaluation = calculateEvaluationValue(requirements);
        boolean isAccepted = evaluation >= requirements.acceptedThreshold();
        log.info("Is accepted: " + isAccepted + ". Evaluation: " + evaluation);

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

    private Set<String> getKeywordsIncludedInFile(Set<String> keywords) {
        Set<String> lowercaseKeywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return cv.words().stream()
                .filter(lowercaseKeywords::contains)
                .collect(Collectors.toSet());
    }

    private Set<Requirement> getFulfilledRequirements(Set<Requirement> requirements) {
        return requirements.stream()
                .filter(r -> r.checkRequirement(cv))
                .collect(Collectors.toSet());
    }
}
