/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.io.File;
import java.util.Collections;
import java.util.Objects;

/**
 * Convert command-line args, which are the file names, to Files
 * and then call the file-comparison routine, and output the result 
 * of the file comparison.
 * 
 * @author alb
 */
public class FilesDeduper {
    
    private String[] fileNames;
    
    public FilesDeduper( String[] args ) {
        fileNames = Objects.requireNonNull( args );
    }
    
    public void go() {
        if( fileNames != null & fileNames.length == 2 ) {
            File f1 = new File( fileNames[0] );
            File f2 = new File( fileNames[1] );
            
            if( !f1.isFile() || !f2.isFile() ) {
                System.out.println( "Error: " + fileNames[0] + " or " +
                                    fileNames[1] +  " is missing or " +
                                    "an invalid file." );
                return;
            }
            
            int i = new FileDeduper( f1, f2 ).compare();
            switch( i ) {
                case Status.FILES_DIFFERENT:
                    System.out.println( "Files are different" ); break;
                case Status.FILES_SAME:
                    System.out.println( "Files are the same" ); break;
                default:
                    System.out.println( "An error occurred comparing files" );
            }
        }
        else {
            System.out.println( 
                "Error: You did not specify two valid file names." );
        }
    }
}
