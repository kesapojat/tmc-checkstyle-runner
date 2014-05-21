package fi.helsinki.cs.tmc.stylerunner.validation;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import fi.helsinki.cs.tmc.stylerunner.CheckstyleRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import static org.junit.Assert.*;

public class CheckstyleResultTest {

    @Test
    public void shouldConvertJsonToCheckstyleResult() throws CheckstyleException, IOException {

        final File testProject = new File("test-projects/invalid/trivial");
        final CheckstyleRunner runner = new CheckstyleRunner(testProject);

        final File outputFile = new File("target/output.txt");
        runner.run(outputFile);

        final Scanner scanner = new Scanner(outputFile);

        final StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        scanner.close();
        outputFile.delete();

        final CheckstyleResult result = CheckstyleResult.build(builder.toString());

        final List<ValidationError> errors = result.getValidationErrors().get(new File(testProject.getAbsolutePath(),
                                                                              "src/Trivial.java"));

        assertFalse(result.getValidationErrors().isEmpty());
        assertEquals("[/home/kennyhei/kesapojat/tmc-checkstyle-runner/test-projects/invalid/trivial/src/Trivial.java]",
                     result.getValidationErrors().keySet().toString());

        assertFalse(result.getValidationErrors().isEmpty());
        assertEquals(2, errors.size());

        String expected = "method def modifier at indentation level 5 not at correct indentation, 4";

        assertEquals(4, errors.get(0).getLine());
        assertEquals(0, errors.get(0).getColumn());
        assertEquals(expected, errors.get(0).getMessage());

        expected = "method def child at indentation level 9 not at correct indentation, 8";

        assertEquals(5, errors.get(1).getLine());
        assertEquals(0, errors.get(1).getColumn());
        assertEquals(expected, errors.get(1).getMessage());
    }
}
