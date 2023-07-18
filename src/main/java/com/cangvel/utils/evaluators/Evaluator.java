package com.cangvel.utils.evaluators;

import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;
import com.cangvel.models.PdfData;

public interface Evaluator {
    CvEvaluation evaluateCvFile(CvRequirements requirements);
}
