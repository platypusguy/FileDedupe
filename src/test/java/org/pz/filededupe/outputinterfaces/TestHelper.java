package org.pz.filededupe.outputinterfaces;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.pz.filededupe.LongStringListTable;

/**
 *
 * Provides utility methods for {@link HtmlOutputTest}
 *
 * @author dsaraiva
 */
class TestHelper {

    private static final int MIN_DUPES_GROUP = 2;
    private static final int MAX_DUPES_GROUP = 5;

    static LongStringListTable getLongStringListTable_OneDuplicate() {
        LongStringListTable dupes = new LongStringListTable();
        addDuplicated(dupes, 2);
        dupes.insertEntry("another_filename", 2L);
        return dupes;
    }

    static LongStringListTable getLongStringListTable_Stress(int countDupes, int numberFiles) {
        LongStringListTable dupes = new LongStringListTable();
        addMassDupes(countDupes, dupes);
        addMassNonDuplicatedFiles(countDupes, numberFiles, dupes);
        return dupes;
    }

    static Clock getFixedClock() {
        return Clock.fixed(Instant.parse("2020-10-25T22:10:00Z"), ZoneOffset.UTC);
    }

    private static void addMassNonDuplicatedFiles(int startingGroupFrom, int numberFiles, LongStringListTable dupes) {
        // in the end its adding with same file name, but the numeric part is different and that's what
        // its needed to not have duplicated files
        LongStream.rangeClosed(startingGroupFrom + 1, numberFiles)
                .forEach( i -> dupes.insertEntry(createFilename(i, 1), i ));
    }

    private static void addMassDupes(int dupesNumber, LongStringListTable dupes) {
        int amountWritten = 0;
        int duplicationNumber;
        while (amountWritten < dupesNumber && canStillWriteDupes(dupesNumber, amountWritten)) {
            duplicationNumber = getRandomNumberInRange(MIN_DUPES_GROUP, MAX_DUPES_GROUP);
            addDuplicated(dupes, duplicationNumber);

            amountWritten += duplicationNumber;
        }
        // add the rest of dupes
        int countMissDupes = dupesNumber - amountWritten;
        if (countMissDupes <= MAX_DUPES_GROUP) {
            addDuplicated(dupes, countMissDupes);
        } else {
            addDuplicated(dupes, MIN_DUPES_GROUP);
            addDuplicated(dupes, countMissDupes - MIN_DUPES_GROUP);
        }
    }

    private static boolean canStillWriteDupes(int countDupes, int amountWritten) {
        return countDupes - amountWritten >= MIN_DUPES_GROUP + MAX_DUPES_GROUP;
    }

    private static int getRandomNumberInRange(int inclusiveLow, int inclusiveHigh) {
        List<Integer> list =
                IntStream.rangeClosed(inclusiveLow, inclusiveHigh).boxed().collect(Collectors.toList());
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Adds a duplicated to table with the following format (consider amountOfEntries equal to 3)
     * and that the table is empty (size is equal to 0):
     * (numeric, {"file1_of_group_1", "file2_of_group_1", "file3_of_group_1"} }
     */
    private static void addDuplicated(LongStringListTable dupes, int amountOfEntries) {
        long numeric = dupes.getKeySet().size() + 1L;
        List<String> filenames = getAvailableFilenames(numeric);

        IntStream.rangeClosed(1, amountOfEntries)
                .forEach(i -> dupes.insertEntry(filenames.get(i-1), numeric));
    }

    private static List<String> getAvailableFilenames(long numberOfGroup) {
        return IntStream
                .rangeClosed(1, MAX_DUPES_GROUP)
                .mapToObj(n -> createFilename(numberOfGroup, n))
                .collect(Collectors.toList());
    }

    private static String createFilename(long numberOfGroup, int fileNumber) {
        return "file" + fileNumber + "_of_group" + numberOfGroup;
    }
}
