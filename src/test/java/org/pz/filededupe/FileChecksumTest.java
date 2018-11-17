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

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author alb
 */

public class FileChecksumTest {

    private File file;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Before
    public void setUp() {
        try {
            file = folder.newFile("testfile.txt");
        }
        catch( IOException ioe ) {
            System.err.println( "unable to create test file in temporary folder in FileChecksumTest");
        }
    }

    @Test
    public void calculateOnEmptyFile() {
        FileChecksum fc = new FileChecksum( file );
        long result = fc.calculate();
        assertEquals(0, result);
    }

//    @Test
//    public void calculateOnNonEmptyFile() {
//        file.
//        FileChecksum fc = new FileChecksum( file );
//        long result = fc.calculate();
//        assertEquals(0, result);
//    }
}