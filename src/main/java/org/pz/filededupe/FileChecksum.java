/*
 * Project looks for duplicate files based on CRC-32 checksumming. 
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 *
 * This class computes the checksum for a given file.
 */
package org.pz.filededupe;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.CheckedInputStream;
import java.util.zip.CRC32;

/**
 * Reads a file and computes its checksum
 * @author alb
 */
public class FileChecksum {

    private String filename;
    final static Integer FILE_ERROR = 1;
    
    FileChecksum( File f ) {
        filename = f.getPath();
    }
    
    FileChecksum( Path p ) {
        this( p.toFile());
    }
    
    /**
     * Does the actual checksum calculation. Note that the only I/O is to
     * read the file all the way through.
     *
     * @return the checksum computed for the file; on error, an error code
     * If an error occurs, the error message is printed to stderr here, so
     * it need not be reported to the user later.
     */
    long calculate() {
        FileInputStream file;

        try {
            file = new FileInputStream(filename);
        } catch( FileNotFoundException e ) {
            System.err.println( "Error: File " + filename + " not found.");
            return( FILE_ERROR );    //TODO: rethrow the exception rather than return err
        }

        CheckedInputStream check = new CheckedInputStream(file, new CRC32());
        BufferedInputStream in =  new BufferedInputStream(check);

        try {
            while (in.read() != -1) {
                // Read file in completely
            }
            in.close();
        } catch( IOException e ) {
            System.err.println( "Error reading file: " + filename);
            return( FILE_ERROR );  //TODO: rethrow the exception rather than return err
        }

        return(check.getChecksum().getValue());
    }
}
