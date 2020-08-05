/*
 * Looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.util.*;

public class DupesTable {

    private HashMap<Long, ArrayList<String>> dupesTable;

    public DupesTable() {
        dupesTable = new HashMap<>();
    }

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

    public int getSize() { return dupesTable.size(); }

    public Set<Long> getKeySet() {
        return dupesTable.keySet();
    }

    public ArrayList<String> getEntry( long key ) {
        return dupesTable.get( key );
    }
}
