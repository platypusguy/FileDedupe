/*
 * Project looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2015-19 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;

import static org.junit.Assert.*;

/**
 * @author alb
 */

public class FileChecksumTest {

    private File file;

    /* This folder and files created in it will be deleted after tests are
     * run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /* executed before every test */
    @Before
    public void setUp() {
        try {
            file = folder.newFile("testfile.txt");
        }
        catch( IOException ioe ) {
            System.err.println( "unable to create test file in temporary folder in FileChecksumTest");
        }
    }

    /**
     * Create an empty temp file and test that its checksum is zero
     */
    @Test
    public void calculateOnEmptyFile() {
        FileChecksum fc = new FileChecksum( file );
        long result = fc.calculate();
        assertEquals(0L, result);
    }

    /**
     * Create a non-empty temp file and test that its checksum is non-zero
     */
    @Test
    public void calculateOnNonEmptyFile() {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("alb");
            writer.close();
        } catch( IOException ioe ) {
            System.err.println( "error writing to test file in FileChecksumTest" );
            fail( "failed due to IOException writing to test file");
        }

        FileChecksum fc = new FileChecksum( file );
        long result = fc.calculate();
        assertNotEquals(0L, result);
    }

    
}