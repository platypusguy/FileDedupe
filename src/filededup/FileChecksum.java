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
import java.util.zip.CheckedInputStream;
import java.util.zip.CRC32;

/**
 * Reads a File and computes the checksum
 * @author alb
 */
public class FileChecksum {
    
    private BufferedInputStream bis;
    private CheckedInputStream chis;
    private long fLength;
    
    private final int TEN_MB = 1_0485_760;  // size of the buffer for CRC
    
    public FileChecksum( File f ) {
        try {
            chis = 
                new CheckedInputStream(new FileInputStream( f ), new CRC32());
            bis = new BufferedInputStream( chis, TEN_MB );
        }
        catch( Throwable t ){
            ;   //TODO: do something in case of error.
        }
    }
    
    /**
     * @return the checksum computed for the file; on error, an error code    
     */
    public long calculate() {
        try {
            for( long i=0L; i < fLength; i++ ) {
                bis.read();
            }
            return( chis.getChecksum().getValue()); 
        }
        catch( Throwable t ) {
            return( Status.FILE_ERROR );
        }
    }
}
