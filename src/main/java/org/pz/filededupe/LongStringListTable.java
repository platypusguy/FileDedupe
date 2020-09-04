/*
 * Looks for duplicate files (based on content) in directory structures.
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Data structure that has key of long integers and values consisting
 * of ArrayLists of Strings.
 *
 * @author alb (@platypusguy)
 */
public class LongStringListTable {

    /**
     *  the main data structure, with a key consisting of a long
     *  and a value consisting of an ArrayList of filenames.
     */
    private final HashMap<Long, ArrayList<String>> table;

    public LongStringListTable() {
        table = new HashMap<>();
    }

    /**
     * Inserts a new filename and a numeric value associated
     * with the file (size, cheksum, etc.) This numeric is
     * the key to the entry, an ArrayList of associated filenames
     * is the value.
     *   If a matching numeric key already exists in the table,
     * then the filename is added to the list of files associated
     * with the numeric key.
     *
     * @param filename  the filename to add
     * @param numeric  the numeric associated with the filename
     */
    public void insertEntry(String filename, Long numeric ) {
        ArrayList tableEntry = table.get( numeric );
        if( tableEntry == null ) {  // not found yet in table
            ArrayList<String> entry = new ArrayList<>();
            entry.add( filename );
            table.put( numeric, entry );
        }
        else {
            tableEntry.add( filename );
        }
    }

    public Set<Long> getKeySet() {
        return table.keySet();
    }

    public ArrayList<String> getEntry( long key ) {
        return table.get( key );
    }
}
