/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import com.google.common.collect.TreeMultimap;

import java.util.NavigableSet;

/**
 * Wrapper for the checksum table
 * @author alb (Andrew Binstock @platypusguy)
 */
public class DupeTable {

    private final TreeMultimap<Long, String> chksumTable;

    public DupeTable() {
        chksumTable = TreeMultimap.create();
    }

    public void insertFile( String filename, long chksum ) {
        chksumTable.put( chksum, filename );
    }

    public int getSize() {
        return( chksumTable.size() );
    }

    public NavigableSet<Long> getKeySet() {
        return chksumTable.keySet();
    }

    public NavigableSet<String> getEntry( long key ) {
         return chksumTable.get( key );
    }

}
