package com.cangvel.utils.evaluators;

import com.cangvel.models.CvData;
import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;

public interface Evaluator {

    CvEvaluation evaluateCvFile(CvRequirements requirements, CvData cvData);

    float calculateEvaluationValue(CvRequirements requirements, CvData cvData);
}
