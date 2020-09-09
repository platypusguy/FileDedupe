/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.util.*;

/**
 * The main line. All processing starts here.
 * @author alb (Andrew Binstock @platypusguy)
 */
public class Main {

    /**
     * @param args the command-line arguments. They can be:
     *    * one or more directories to scan for duplicate files
     *    * -nosubdirs which says don't check subdirectories
     *    * -help/-h which prints usage instructions
     */
    public static void main( final String[] args )
    {
        // default is to visit subdirectories
        boolean nosubdirs = false;

        printCopyright();
        
        if( args.length == 0 ) {
            showUsage();
            return;
        }

        List<String> argList = Arrays.asList(args);
        if( argList.contains( "-h" )  || argList.contains( "-help" ) ||
            argList.contains( "--h" ) || argList.contains( "--help" )) {
            showUsage();
            return;
        }

        LinkedList<String> dirs = new LinkedList<>();
        for( String arg : args ) {
            if( !arg.startsWith("-") ) {
                dirs.add( arg );
            }
            else {
                if( arg.equalsIgnoreCase( "-nosubdirs" ) ||
                    arg.equalsIgnoreCase( "--nosubdirs")) {
                    nosubdirs = true;
                }
                else {
                    System.err.println( "Invalid command: " + arg );
                }
            }
        }

        // Create the sizes table, where the file sizes are stored
        LongStringListTable sizesTable = new LongStringListTable();

        // Create the filesize retrieval engine
        FileSizer fileSizer = new FileSizer();

        // Get the file sizes for all files in each specified directory
        if( dirs.size() > 0 ) {
            for( String dir : dirs )
                fileSizer.loadFileSizes( dir, nosubdirs, sizesTable );
        }
        else {  //happens only if a single dash option other than -h is specified
            System.err.println( "Error: no directory specified. Exiting" );
        }

        // Create the dupe table, where file checksums are stored
        LongStringListTable dupesTable = new LongStringListTable();

        // sizesTable now holds all the filenames and the corresponding file sizes
        sizesTable.getFilenames().stream() // get the lists of files for each size
            .filter( s -> s.size() > 1 )   // filter for lists of more than 1 file for a given size
            .forEach( s -> new FilesChecksum( s, dupesTable ).go() );  // checksum those files

        // Scan the dupesTable and print out all duplicates to stdout
        DupesOutput dupesList = new DupesOutput();
        int dupesCount = dupesList.showDupes( dupesTable );

        System.out.println( "Number of duplicates found: " + dupesCount );
    }


    /**
     * Simply prints the copyright
     */
    private static void printCopyright() {
        System.out.println(
            "FileDedupe v. 2.0 (c) Copyright 2017-20 Andrew Binstock. All rights reserved.\n" );
    }

    /**
     * Explains program usage
     */
    private static void showUsage() {

        System.out.println(
            "FileDedupe finds duplicate files in a diretory or disk drive.\n" +
            "arguments: one or more directories to process;\n" +
            "           -nosubdirs do not check subdirectories (default: checks all subdirs)\n" +
            "           -h or -help prints this message"
        );
    }
}
