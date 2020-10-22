package org.pz.filededupe.outputinterfaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.pz.filededupe.outputinterfaces.TestHelper.getFixedClock;
import static org.pz.filededupe.outputinterfaces.TestHelper.getLongStringListTable_OneDuplicate;
import static org.pz.filededupe.outputinterfaces.TestHelper.getLongStringListTable_Stress;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pz.filededupe.FileChecksum;
import org.pz.filededupe.LongStringListTable;

/**
 * Test html output
 *
 * @author dsaraiva
 */
public class HtmlOutputTest {

    private static final String TEST_RESOURCES_FILE_EXPECTED = "src/test/resources/EXPECTED_CONTENT.html";
    private static final String RESULT_FILENAME = "dupes-2020-10-25---22-10-00.html";

    @Before
    public void initTestMethod() throws InterruptedException {
        // this will guarantee that filenames will be different between test methods
        Thread.sleep(1000);
    }

    @After
    public void afterTestMethod() {
        Path resultFilePath = Paths.get(RESULT_FILENAME);
        // delete created file during test
        if (resultFilePath.toFile().exists()) {
            resultFilePath.toFile().delete();
        }
    }

    @Test
    public void testHtmlFileCreation() {
        LongStringListTable dupes = getLongStringListTable_OneDuplicate();
        Clock fixedClock = getFixedClock();
        new HtmlOutput(fixedClock).output(dupes);

        File file = Paths.get(RESULT_FILENAME).toFile();
        assertTrue(file.exists());
    }

    @Test
    public void testHtmlFileContent() throws IOException {
        LongStringListTable dupes = getLongStringListTable_OneDuplicate();
        Clock fixedClock = getFixedClock();
        new HtmlOutput(fixedClock).output(dupes);

        File file = Paths.get(RESULT_FILENAME).toFile();
        assertTrue(file.exists());

        // check that the contents of the file is equal to what is expected
        FileChecksum checksumExpected = new FileChecksum(new File(TEST_RESOURCES_FILE_EXPECTED));
        FileChecksum checksumActual = new FileChecksum(file);
        assertEquals(checksumExpected.calculate(), checksumActual.calculate());
    }

    @Test
    public void testFilename() {
        LongStringListTable dupes = getLongStringListTable_OneDuplicate();
        Clock fixedClock = getFixedClock();
        new HtmlOutput(fixedClock).output(dupes);

        File file = Paths.get(RESULT_FILENAME).toFile();
        assertTrue(file.exists());
        assertEquals(file.getName(), RESULT_FILENAME);

    }

    /**
     * The goal is to test if the {@link HtmlOutput} is able to cope with a big number of files and duplicates
     * - dupes expected 100_000
     * - number of files 1 000 000
     *
     * The duplicated files were added into groups between 2 and 5 files (configured in {@link TestHelper}).
     * The groups were formed randomly.
     *
     * NOTE: by "files" in this test its not meant as the physical files but as the filenames stored in
     * {@link LongStringListTable}
     */
    @Test
    public void testStress() throws IOException {
        int dupesExpected = 100_000;
        int numberFiles = 1_000_000;
        LongStringListTable dupes = getLongStringListTable_Stress(dupesExpected, numberFiles);
        Clock fixedClock = getFixedClock();
        new HtmlOutput(fixedClock).output(dupes);

        File file = Paths.get(RESULT_FILENAME).toFile();
        assertTrue(file.exists());

        Stream<String> lines = Files.lines(Paths.get(RESULT_FILENAME));
        assertEquals(lines.filter(str -> str.contains("<a href=")).count(), dupesExpected);

        lines = Files.lines(Paths.get(RESULT_FILENAME));
        assertTrue(lines
                .anyMatch(str -> str.contains("<p>Number of duplicates found: " + dupesExpected +"</p>")));
    }
}
