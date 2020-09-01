/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Data structure to hold file sizes and the filenames that are of that size
 * @author alb
 */

public class FileSizesTable {

    /**
     *  the main data structure, with a file size (Long)
     *  pointing to an ArrayList of filenames.
     */
    private final HashMap<Long, ArrayList<String>> sizesTable;

    public FileSizesTable() {
        sizesTable = new HashMap<>();
    }

    /**
     * Inserts a new file and its size; if the size is
     * already in the table, then the filename is added to the
     * list of files with that file size (which indicates
     * the possibility of a duplicate file)
     * @param filename  the filename to add
     * @param size  the file size to add
     */
    public void insertFile( String filename, Long size ) {
        ArrayList tableEntry = sizesTable.get( size );
        if( tableEntry == null ) {  // not found yet in table
            ArrayList<String> entry = new ArrayList<>();
            entry.add( filename );
            sizesTable.put( size, entry );
        }
        else {
            tableEntry.add( filename );
        }
    }

    public Set<Long> getKeySet() {
        return sizesTable.keySet();
    }

    public ArrayList<String> getEntry( long key ) {
        return sizesTable.get( key );
    }
}
