package org.pz.filededupe.interfaces;

import org.pz.filededupe.LongStringListTable;

/**
 * Contract for output interfaces to implement
 *
 * @author dsaraiva
 */
public interface OutputInterface {

    /**
     * Output duplications
     *
     * @param dupes duplications source
     */
    void output(LongStringListTable dupes);
}