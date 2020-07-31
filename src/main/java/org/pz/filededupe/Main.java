/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 11 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.util.*;

/**
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
        if( argList.contains( "-h") || argList.contains( "-help")) {
            showUsage();
            return;
        }

        LinkedList<String> dirs = new LinkedList<>();
        for( String arg : args ) {
            if( !arg.startsWith("-") ) {
                dirs.add( arg );
            }
            else {
                if( arg.equalsIgnoreCase( "-nosubdirs" )) {
                    nosubdirs = true;
                }
                else {
                    System.err.println( "Invalid command: " + arg );
                }
            }
        }

        // Create the dupe table, where file checksums are stored
        DupeTable dupeTable = new DupeTable();

        // Create the directory deduping engine
        DirDeduper dirDeduper = new DirDeduper();

        // Call the deduping engine on each specified directory
        if( dirs.size() > 0 ) {
            for( String dir : dirs )
                dirDeduper.go( dir, nosubdirs, dupeTable );
        }
        else {  //happens only if a single dash option other than -h is specified
            System.err.println( "Error: no directory specified. Exiting");
        }
    }

    private static void printCopyright()
    {
        System.out.println(
            "FileDedupe v.0.3 (c) Copyright 2017-20 Andrew Binstock. All rights reserved.\n" );
    }

    private static void showUsage() {

        System.out.println(
            "FileDedupe finds duplicate files in a diretory or disk drive.\n" +
            "arguments: one or more directories to process;\n" +
            "           -nosubdirs do not check subdirectories (default: checks all subdirs)" +
            "           -h or -help prints this message"
        );
    }
}
