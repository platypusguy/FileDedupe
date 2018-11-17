/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8
 *
 * Copyright (c) 2015 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.io.File;

/**
 * Does the actual comparison of two Java Files. First checks that the files
 * are the same size. If they are, it does a CRC-32 checksum on each one and
 * compares the results.
 * 
 * @author alb
 */
public class FileDeduper {
  
    private final File inFile1, inFile2;
    
    public FileDeduper( File f1, File f2 ) {
        inFile1 = f1;
        inFile2 = f2;
    }
    
    public int compare() {
        if( inFile1.length() != inFile2.length() ) {
            return( Status.FILES_DIFFERENT );
        }
        else try {
           
            long f1CheckSum = new FileChecksum( inFile1 ).calculate();
            long f2CheckSum = 0L;
              
            if( f1CheckSum != Status.FILE_ERROR ) {
                f2CheckSum = new FileChecksum( inFile2 ).calculate();
            }
            
            if( f1CheckSum == Status.FILE_ERROR || 
                f2CheckSum == Status.FILE_ERROR )                                
                    return( Status.FILE_ERROR );
            
            return( f1CheckSum == f2CheckSum ? 
                        Status.FILES_SAME : Status.FILES_DIFFERENT );
            
        } catch( Throwable t ) {
            return( Status.FILE_ERROR );
        }
    }      
}
