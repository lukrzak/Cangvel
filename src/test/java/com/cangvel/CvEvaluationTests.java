package com.cangvel;

import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;
import com.cangvel.utils.analysers.FileContentAnalyser;
import com.cangvel.utils.analysers.PdfFileContentAnalyser;
import com.cangvel.utils.evaluators.DefaultEvaluator;
import com.cangvel.utils.evaluators.Evaluator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CvEvaluationTests {

    // Predefined requirements
    CvRequirements requirements1 = new CvRequirements(Set.of("Lorem"), Set.of("aaa", "ipsum"), false, 0.6F);
    CvRequirements requirements2 = new CvRequirements(Set.of("Lorem"), Set.of("aaa", "ipsum"), true, 0.6F);
    CvRequirements requirements3 = new CvRequirements(Set.of("aaaaa", "bbbbb"), Set.of("ccccc"), false, 0.6F);

    // Set of test files
    private final File textFile = new File("./src/test/java/com/cangvel/files/only_text.pdf");
    private final File textWithImageFile = new File("./src/test/java/com/cangvel/files/text_with_image.pdf");

    FileContentAnalyser analyser = new PdfFileContentAnalyser(Set.of("pdf"));
    Evaluator evaluatorForTextFile;
    Evaluator evaluatorForTextWithImageFile;

    @BeforeEach
    public void setup(){
        try{
            this.evaluatorForTextFile = new DefaultEvaluator(analyser.getPdfData(textFile));
            this.evaluatorForTextWithImageFile = new DefaultEvaluator(analyser.getPdfData(textWithImageFile));
        }
        catch (IOException e){
            System.err.println("Cannot evaluate file file");
        }
    }

    @Test
    @DisplayName("Test default evaluator")
    public void testDefaultEvaluatorWithTextOnlyFile(){
        List<CvRequirements> requirements = new ArrayList<>(List.of(requirements1, requirements2, requirements3));
        // expected results sets for cv with text only and text with profile picture
        List<Float> requirementsFulfilmentForTextOnly = new ArrayList<>(List.of(0.50f, 0.50f, 0.00f));
        List<Float> requirementsFulfilmentForTextAndImage = new ArrayList<>(List.of(0.50f, 0.75f, 0.00f));
        List<Boolean> acceptanceForTextOnly = new ArrayList<>(List.of(false, false, false));
        List<Boolean> acceptanceForTextAndImage = new ArrayList<>(List.of(false, true, false));

        for(int i = 0; i < requirements.size(); i++){
            testEvaluator(
                    evaluatorForTextFile.evaluateCvFile(requirements.get(i)),
                    requirementsFulfilmentForTextOnly.get(i),
                    acceptanceForTextOnly.get(i));
            testEvaluator(
                    evaluatorForTextWithImageFile.evaluateCvFile(requirements.get(i)),
                    requirementsFulfilmentForTextAndImage.get(i),
                    acceptanceForTextAndImage.get(i));
        }

    }

    public void testEvaluator(CvEvaluation evaluation, float requirementsFulfillment, boolean isAccepted){
        assertNotNull(evaluation, "Evaluation should not be null");
        assertEquals(evaluation.getRequirementFulfillment(), requirementsFulfillment, 0.01, "Wrong percentage of fulfilled requirements");
        assertEquals(evaluation.isAccepted(), isAccepted);
    }
}
