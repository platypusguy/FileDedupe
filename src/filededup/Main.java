/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015-8 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package filededup;

/**
 * @author alb (Andrew Binstock)
 */
public class Main {

    /**
     * @param args the command-line arguments. They can be:
     *    * a single directory, in which case all files in the
     *      directory and subdirectories are analyzed
     *    * two filenames, in which case the files are compared for equality
     *    * -help/-h which prints usage instructions
     */
    public static void main( final String[] args ) 
    {
        printCopyright();
        
        if( args.length == 0 ) {
            showUsage();
            return;
        }
        
        if( args[0].equalsIgnoreCase( "-h" ) ||
            args[0].equalsIgnoreCase( "-help" ) ) {
                showUsage();
                return;
        }
    
        if( args.length == 1 ) {
            new DirDeduper( args[0] ).go();
        }
        else 
        if( args.length == 2 ) {
            new FilesDeduper( args ).go();
        }
        else {
            System.out.println( "Invalid command. This might help:" );
            showUsage();
        }
    }

    private static void printCopyright()
    {
        System.out.println(
            "FileDedupe v.0.2 (c) Copyright 2017-8, Andrew Binstock. All rights reserved.\n" );
    }

    private static void showUsage() {

        System.out.println(
            "FileDedupe finds duplicate files in a diretory or disk drive.\n" +
            "arguments: 1 argument is the directory to process (which includes all subdirectories);\n" +
            "           2 arguments are two files to compare to evaluate whether they're duplicates\n" +
            "           -h or -help prints this message"
        );
    }
}
