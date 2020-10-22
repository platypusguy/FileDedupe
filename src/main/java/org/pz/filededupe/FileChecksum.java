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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * Reads a file and computes its checksum
 * @author alb
 */
public class FileChecksum {

    private final String filename;

    public FileChecksum( File f ) {
        filename = f.getPath();
    }
    
    public FileChecksum( Path p ) {
        this( p.toFile());
    }

    public FileChecksum( String s ) { filename = s; }
    
    /**
     * Does the actual checksum calculation. Note that the only I/O is to
     * read the file all the way through.
     *
     * @return the checksum computed for the file; on error, an error code
     * If an error occurs, the error message is printed to stderr here, so
     * it need not be reported to the user later.
     * @throws IOException in event of a problem reading the file
     */
    public long calculate() throws IOException {
        FileInputStream file;

        try {
            file = new FileInputStream( filename );
        } catch( FileNotFoundException e ) {
            System.err.println( "Error: File " + filename + " not found." );
            throw( new IOException( e.toString() ));
        }

        CheckedInputStream check = new CheckedInputStream( file, new CRC32() );
        BufferedInputStream in = new BufferedInputStream( check );

        try {
            while( in.read() != -1 ) {
                // Read file in completely
            }
            in.close();
        } catch( IOException e ) {
            System.err.println( "Error reading file: " + filename );
            check.close();
            in.close();
            file.close();
            throw( new IOException( e.toString() ));
        }

        return(check.getChecksum().getValue());
    }
}
