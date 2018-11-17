/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015-19 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import com.google.common.collect.*;

/**
 * De-duplicates a directory
 * @author alb (Andrew Binstock)
 */
class DirDeduper {
    private File dir;
    private String origPath;
    private TreeMultimap<Long, String> chksumTable;
    private boolean duplicatesFound = false;


    /**
     * Constructor, requires only the directory path
     * @param pathToDir  the directory path
     */
    DirDeduper(String pathToDir) {
        origPath = Objects.requireNonNull( pathToDir );
        dir = new File( pathToDir );
    }

    /**
     * Main method: validates directory, gets paths of all files (incl. subidrectories), 
     *   creates and loads the table of checksums for the files, and then prints out duplicates.
     * @return  Status.OK or Status.FILE_ERROR in event of error
     */
    int go() {

        if( !dir.isDirectory() ) {
            System.err.println("Error: " + origPath + " is not a directory");
            return( Status.FILE_ERROR );
        }

        // create a list of all the files in the directory and its subdirectories
        Path path = FileSystems.getDefault().getPath(origPath);
        ArrayList<Path> fileSet = new DirFileListMaker().go( path );
        if( fileSet.isEmpty() ) {
            System.err.println("Error: Directory " + origPath + " contains no files");
            return( Status.FILE_ERROR );
        }

        System.out.println("Number of files found to check: " + fileSet.size());

        // create the table for the checksums
        chksumTable = TreeMultimap.create();

        // calculate checksum for every file in fileSet and insert it into a hash table
        fileSet.forEach( this::updateChecksums );

        System.out.println( "Number of files checked: " + chksumTable.size() );
        NavigableSet<Long> keys = chksumTable.keySet();
        for( Long key : keys ) {
            NavigableSet<String> paths = chksumTable.get( key );
            if( paths.size() > 1) {
                duplicatesFound = true;
                System.out.println( "These files are the same:");
                for( String filepath : paths) {
                    System.out.println( "\t" + filepath );
                }
                System.out.println();
            }
        }

        if( ! duplicatesFound ) {
            System.out.println( "No duplicate files found in or below " + origPath );
        }

        return( Status.OK );
    }

    /**
     * Calculates the checksum for afile and puts in the chksumTable.
     * @param p the filepath for the file to checksum
     * @return  not clear this is needed.
     */
    Path updateChecksums( Path p ) {
        long chksum = new FileChecksum( p ).calculate();
        chksumTable.put( chksum, p.toString() );
        System.out.println( "checksum: " + chksum + " file: " + p );
        return p;
    }
}