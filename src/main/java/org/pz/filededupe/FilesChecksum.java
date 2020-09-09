/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Simple class that takes an ArrayList of filenames,
 * runs a checksum on the files and puts the result into
 * a table.
 *
 * @author alb
 */
public class FilesChecksum {

    LongStringListTable checksumTable;
    ArrayList<String> filenames;

    public FilesChecksum(ArrayList<String> files, LongStringListTable table) {
        filenames = Objects.requireNonNull( files );
        checksumTable = Objects.requireNonNull( table );
    }

    /**
     *  Calls the checksum calculation routine for each file in the ArrayList of filenames
     *  and inserts the checksum and the filename into the checksumTable.
     */
    public void go() {
        for( String file : filenames ) {
            try {
                checksumTable.insertEntry(file, new FileChecksum(file).calculate());
            } catch( IOException ioe ) {
                continue; // error messages has already been shown to user; continue with the loop.
            }
        }
    }
}
