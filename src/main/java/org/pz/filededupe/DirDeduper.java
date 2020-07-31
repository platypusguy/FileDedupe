/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 11 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
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

    private String origPath;
    private boolean subdirs;
    private DupeTable chksumTable;
    private boolean duplicatesFound = false;

//    /**
//     * @param pathToDir directory to scan
//     * @param subdirFlag scan the subdirectories as well?
//     */
    public DirDeduper() {};
//
//    DirDeduper(  ) {
//        origPath = Objects.requireNonNull( pathToDir );
//        subdirs = subdirFlag;
//        chksumTable = table;
//
//        File dir = new File( pathToDir );
//        if( !dir.isDirectory() ) {
//            throw( new InvalidPathException(
//                pathToDir, "Error: " + pathToDir + " is not a directory" ));
//        }
//    }

    /**
     * Main method: validates directory, gets paths of all files (incl. subidrectories), 
     *   creates and loads the table of checksums for the files, and then prints out duplicates.
     * @param pathToDir directory to scan
     * @param subdirFlag scan the subdirectories as well?
     * @return  boolean: duplicates found/not found
     */
    boolean go( String pathToDir, boolean subdirFlag, DupeTable table) {

        origPath = Objects.requireNonNull( pathToDir );
        subdirs = subdirFlag;
        chksumTable = table;

        File dir = new File( pathToDir );
        if( !dir.isDirectory() ) {
            throw( new InvalidPathException(
                pathToDir, "Error: " + pathToDir + " is not a directory" ));
        }

        // create a list of all the files in the directory and its subdirectories
        Path path = FileSystems.getDefault().getPath( origPath );
        ArrayList<Path> fileSet = new DirFileListMaker().go( path, subdirs );
        if( fileSet.isEmpty() ) {
            System.out.println("Directory " + origPath + " contains no files");
            return( false );
        }

        System.out.println("Number of files found to check: " + fileSet.size());

        // calculate checksum for every file in fileSet and insert it into a hash table
        fileSet.forEach( this::updateChecksums );

        System.out.println( "Number of files checked: " + chksumTable.getSize() );

        NavigableSet<Long> keys = chksumTable.getKeySet();

        for( Long key : keys ) {
            NavigableSet<String> paths = chksumTable.getEntry( key );
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

        return( duplicatesFound );
    }

    /**
     * Calculates the checksum for a file and puts in the chksumTable.
     * @param p the filepath for the file to checksum
     * @return  not clear this is needed.
     */
    Path updateChecksums( Path p ) {
        long chksum = new FileChecksum( p ).calculate();
        chksumTable.insertFile( p.toString(), chksum );
        System.out.println( "checksum: " + chksum + " file: " + p );
        return p;
    }
}