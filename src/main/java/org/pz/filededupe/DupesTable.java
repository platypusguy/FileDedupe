/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.util.*;

/**
 * Data structure to hold checksums and the filenames they correspond to
 * @author alb
 */

public class DupesTable {

    /**
     *  the main data structure, with a checksum (Long)
     *  pointing to an ArrayList of filenames.
     */
    private final HashMap<Long, ArrayList<String>> dupesTable;

    public DupesTable() {
        dupesTable = new HashMap<>();
    }

    /**
     * Inserts a new file and checksum; if the checksum is
     * already in the table, then the filename is added to the
     * list of files with that checksum (which indicates a duplicate)
     * @param filename  the filename to add
     * @param checksum  the file checksum to add
     */
    public void insertFile( String filename, Long checksum ) {
        ArrayList tableEntry = dupesTable.get( checksum );
        if( tableEntry == null ) {  // not found yet in table
            ArrayList<String> entry = new ArrayList<>();
            entry.add( filename );
            dupesTable.put( checksum, entry );
        }
        else {
            tableEntry.add( filename );
        }
    }

    public Set<Long> getKeySet() {
        return dupesTable.keySet();
    }

    public ArrayList<String> getEntry( long key ) {
        return dupesTable.get( key );
    }
}
