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

    private Set<String> requiredWordsFoundInFile;

    private Set<String> optionalWordsFoundInFile;

    private Set<Requirement> fulfilledRequirements;

    /**
     * Analyses cv, based on received requirements and passed data as CvRequirements and CvData as arguments,
     * respectively.
     *
     * @param requirements Object that represents requirements to rate CV.
     * @param cvData       Object with analysed data from file.
     * @return Object with CV evaluation.
     */
    @Override
    public CvEvaluation evaluateCvFile(CvRequirements requirements, CvData cvData) {
        requiredWordsFoundInFile = getKeywordsIncludedInFile(requirements.requiredKeywords(), cvData);
        optionalWordsFoundInFile = getKeywordsIncludedInFile(requirements.optionalKeywords(), cvData);
        fulfilledRequirements = getFulfilledRequirements(requirements.requirements(), cvData);

        float evaluation = calculateEvaluationValue(requirements, cvData);
        boolean isAccepted = evaluation >= requirements.acceptedThreshold();
        log.info("Is accepted: " + isAccepted + ". Evaluation: " + evaluation);

        return new CvEvaluation(evaluation, isAccepted, requiredWordsFoundInFile, optionalWordsFoundInFile, fulfilledRequirements);
    }

    /**
     * Returns evaluation presented as float value (0.0 - 1.0).
     *
     * @param requirements Requirements for cv to evaluate on.
     * @return rate of CV as float type.
     */
    @Override
    public float calculateEvaluationValue(CvRequirements requirements, CvData cvData) {
        int totalKeywordsAndRequirementsMet = requiredWordsFoundInFile.size()
                + optionalWordsFoundInFile.size()
                + fulfilledRequirements.size();
        int totalKeywordsAndRequirements = requirements.requiredKeywords().size()
                + requirements.optionalKeywords().size()
                + requirements.requirements().size();

        return (float) totalKeywordsAndRequirementsMet / totalKeywordsAndRequirements;
    }

    private Set<String> getKeywordsIncludedInFile(Set<String> keywords, CvData cvData) {
        Set<String> lowercaseKeywords = keywords.stream().map(String::toLowerCase).collect(Collectors.toSet());
        return cvData.words().stream()
                .filter(lowercaseKeywords::contains)
                .collect(Collectors.toSet());
    }

    private Set<Requirement> getFulfilledRequirements(Set<Requirement> requirements, CvData cvData) {
        return requirements.stream()
                .filter(r -> r.checkRequirement(cvData))
                .collect(Collectors.toSet());
    }
}
