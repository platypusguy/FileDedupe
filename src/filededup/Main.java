/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package filededup;

import java.io.File;

/**
 * @author alb (Andrew Binstock)
 */
public class Main {

    /**
     * @param args the command-line arguments. They can be:
     *    * a single directory, in which case all files in the
     *      directory and subdirectories are analyzed
     *    * two filenames, in which case the files are compared for equality
     *    -help/-h which prints usage instructions
     */
    public static void main( final String[] args ) 
    {  
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
            DirDeduper dirDedupe = new DirDeduper( args[0] );
            dirDedupe.go();
        }
        else 
        if( args.length == 2 ) {
            FilesDeduper filesDedupe = new FilesDeduper( args );
            filesDedupe.go();
        }
        else {
            System.out.println( "Invalid command. This might help:" );
            showUsage();
        }
    }
        
    static void showUsage() {
        System.out.println( "Usage explanation to go here" ); //TODO: add text
    }
}
