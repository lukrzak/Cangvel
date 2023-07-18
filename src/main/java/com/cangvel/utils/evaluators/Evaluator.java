package com.cangvel.utils.evaluators;

import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;

public interface Evaluator {
    CvEvaluation evaluateCvFile(CvRequirements requirements);
    float calculateEvaluationValue(CvRequirements requirements);
}
