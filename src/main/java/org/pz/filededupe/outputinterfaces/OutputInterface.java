package org.pz.filededupe.outputinterfaces;

import org.pz.filededupe.LongStringListTable;

/**
 * Contract for output interfaces to implement
 *
 * @author dsaraiva
 */
public interface OutputInterface {

    String NUMBER_OF_DUPLICATES_FOUND = "Number of duplicates found: ";
    String THESE_FILES_ARE_THE_SAME = "These files are the same:";

    /**
     * Output duplications
     *
     * @param dupes duplications source
     */
    void output(LongStringListTable dupes);
}