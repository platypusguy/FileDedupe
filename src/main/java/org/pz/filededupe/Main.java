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
 * @author alb (Andrew Binstock)
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
        printCopyright();
        
        if( args.length == 0 ) {
            showUsage();
            return;
        }

        LinkedList<String> dirs = new LinkedList<>();
        int i;
        for( i = 0; i < args.length; i++ ) {
            if( args[i].startsWith( "-" ))
                break;
            else {
                dirs.add( args[i]);
            }
        }

        if( args[i].equalsIgnoreCase("-h") ||
            args[i].equalsIgnoreCase( "-help")) {
            showUsage();
            return;
        }

        boolean nosubdirs = false;
        if( args[i].equalsIgnoreCase( "-nosubdirs" )) {
            nosubdirs = true;
        }
        else{
            System.err.println("Invalid command: " + args[i]);
            System.err.println("This might help:");
            showUsage();
            return;
        }

        if( dirs.size() > 0 ) {
            for( String dir : dirs )
                new DirDeduper(dir, nosubdirs).go();
        }
    }

    private static void printCopyright()
    {
        System.out.println(
            "FileDedupe v.0.3 (c) Copyright 2017-20, Andrew Binstock. All rights reserved.\n" );
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
