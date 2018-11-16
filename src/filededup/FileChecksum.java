/*
 * Project looks for duplicate files based on CRC-32 checksumming. 
 *
 * Copyright (c) 2015 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 *
 * This class computes the checksum for a given file.
 */
package filededup;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.CheckedInputStream;
import java.util.zip.CRC32;

/**
 * Reads a File and computes the checksum
 * @author alb
 */
public class FileChecksum {

    private String filename;
    
    private final int TEN_MB = 10_485_760;  // size of the buffer for CRC
    
    FileChecksum( File f ) {
        filename = f.getPath();
    }
    
    FileChecksum( Path p ) {
        this( p.toFile());
    }
    
    /**
     * @return the checksum computed for the file; on error, an error code    
     */
    long calculate() {
        FileInputStream file = null;
        try {
            file = new FileInputStream(filename);
        } catch( FileNotFoundException e ) {
            e.printStackTrace(); //TODO
        }

        CheckedInputStream check =
            new CheckedInputStream(file, new CRC32());
        BufferedInputStream in =
            new BufferedInputStream(check);
        try {
            while (in.read() != -1) {
                // Read file in completely
            }
            in.close();
        } catch( IOException e ) {
            e.printStackTrace(); //TODO
        }

        return(check.getChecksum().getValue());
    }
}
