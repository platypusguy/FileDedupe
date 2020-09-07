/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.util.*;

/**
 * Class to print the duplicates to stdout
 * @author alb
 */

public class DupesOutput {

    public DupesOutput() {}

    /**
     * The class that does the work
     * @param dupes the table of file checksums, which shows the duplicate files
     * @return the number of duplicates found
     */
    public int showDupes(  LongStringListTable dupes ) {

        int dupesCount = 0;

        // get a set of all the keys (which are checksums)
        Set<Long> keys = dupes.getKeySet();

        // go down the list looking for checksums with more than one associated file
        for( Long key : keys ) {
            ArrayList<String> paths = dupes.getEntry( key );
            if( paths.size() > 1) {
                dupesCount += paths.size();
                System.out.println( "These files are the same:");
                paths.sort( null );
                for( String filepath : paths) {
                    System.out.println( "\t" + filepath );
                }
                System.out.println();
            }
        }
        return( dupesCount );
    }
}
