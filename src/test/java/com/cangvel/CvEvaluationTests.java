package com.cangvel;

import com.cangvel.models.CvEvaluation;
import com.cangvel.models.CvRequirements;
import com.cangvel.models.requirements.ContainsAtLeastNumberOfWords;
import com.cangvel.models.requirements.ContainsProfilePictureRequirement;
import com.cangvel.utils.analysers.FileContentAnalyser;
import com.cangvel.utils.analysers.PdfFileContentAnalyser;
import com.cangvel.utils.evaluators.DefaultEvaluator;
import com.cangvel.utils.evaluators.Evaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CvEvaluationTests {

    // Set of test files
    private final File textFile = new File("./src/test/java/com/cangvel/files/only_text.pdf");

    private final File shortTextFile = new File("./src/test/java/com/cangvel/files/short_text.pdf");

    private final File textWithImageFile = new File("./src/test/java/com/cangvel/files/text_with_image.pdf");

    // Predefined requirements
    CvRequirements requirements1 = new CvRequirements(Set.of("Lorem"), Set.of("aaa", "ipsum"), Set.of(), 0.6F);

    CvRequirements requirements2 = new CvRequirements(Set.of("Lorem"), Set.of("aaa", "ipsum"),
            Set.of(new ContainsProfilePictureRequirement()), 0.6F);

    CvRequirements requirements3 = new CvRequirements(Set.of("aaaaa", "bbbbb"), Set.of("ccccc"),
            Set.of(new ContainsAtLeastNumberOfWords(5)), 0.25F);

    FileContentAnalyser analyser = new PdfFileContentAnalyser(Set.of("pdf"));

    Evaluator evaluatorForTextFile;

    Evaluator evaluatorForShortTextFile;

    Evaluator evaluatorForTextWithImageFile;


    @BeforeEach
    public void setup() {
        evaluatorForTextFile = new DefaultEvaluator();
        evaluatorForTextWithImageFile = new DefaultEvaluator();
        evaluatorForShortTextFile = new DefaultEvaluator();
    }

    @Test
    @DisplayName("Test default evaluator")
    public void testDefaultEvaluatorWithTextOnlyFile() throws IOException {
        testEvaluator(evaluatorForTextFile.evaluateCvFile(requirements1, analyser.getPdfData(textFile)), 0.66f, true);
        testEvaluator(evaluatorForTextFile.evaluateCvFile(requirements2, analyser.getPdfData(textFile)), 0.50f, false);
        testEvaluator(evaluatorForTextFile.evaluateCvFile(requirements3, analyser.getPdfData(textFile)), 0.25f, true);

        testEvaluator(evaluatorForTextWithImageFile.evaluateCvFile(requirements1, analyser.getPdfData(textWithImageFile)), 0.66f, true);
        testEvaluator(evaluatorForTextWithImageFile.evaluateCvFile(requirements2, analyser.getPdfData(textWithImageFile)), 0.75f, true);
        testEvaluator(evaluatorForTextWithImageFile.evaluateCvFile(requirements3, analyser.getPdfData(textWithImageFile)), 0.25f, true);

        testEvaluator(evaluatorForShortTextFile.evaluateCvFile(requirements2, analyser.getPdfData(shortTextFile)), 0.00f, false);
        testEvaluator(evaluatorForShortTextFile.evaluateCvFile(requirements3, analyser.getPdfData(shortTextFile)), 0.00f, false);
    }

    private void testEvaluator(CvEvaluation evaluation, float requirementsFulfillment, boolean isAccepted) {
        assertNotNull(evaluation, "Evaluation should not be null");
        assertEquals(evaluation.requirementFulfillment(), requirementsFulfillment, 0.01, "Wrong percentage of fulfilled requirements");
        assertEquals(evaluation.isAccepted(), isAccepted);
    }
}
