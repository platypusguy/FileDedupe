/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe.outputinterfaces;

import org.pz.filededupe.LongStringListTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Abstract class that contains method for iterate and output duplicates according to given parameters
 *
 * @author alb
 * @author dsaraiva
 */
abstract class DupesOutput {

    protected DupesOutput() {}

    /**
     * Print duplicates and final count using given consumers
     *
     * @param dupes source of duplicates
     * @param dupesConsumer responsible to print duplicates
     * @param finalCountConsumer responsible to print the final count of duplicates
     */
    protected void output(LongStringListTable dupes, Consumer<List<String>> dupesConsumer,
                       Consumer<Integer> finalCountConsumer) {
        AtomicInteger dupesCount = new AtomicInteger(0);
        dupes.getKeySet().stream() // get a set of all the keys (which are checksums)
                .map(dupes::getEntry) // go down the list looking for checksums with more than one associated file
                .filter(paths -> paths.size() > 1)
                .map(this::sortPaths) // sort paths for each group of files
                .peek(paths -> dupesCount.getAndAdd(paths.size()))
                .forEach(dupesConsumer);
        finalCountConsumer.accept(dupesCount.get());
    }

    private ArrayList<String> sortPaths(ArrayList<String> paths) {
        //sort the paths before showing them. null means use the natural order of sorting
        //which for Strings is ascending, using standard String comparison
        paths.sort(null);
        return paths;
    }
}