/*
 * Project looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2015-19 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.io.*;
import java.nio.file.Paths;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

/**
 * @author alb
 */

public class FileChecksumTest {

    private File file;

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
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
            String className = this.getClass().getCanonicalName();
            System.err.println( "error creating temporary test file in " +  this.getClass().getSimpleName() );
        }
    }

    /**
     * Run the checksum on a non-existent file. Should write error message to stderr.
     */
    @Test
    public void calculateOnNonExistentFile() {

        PrintStream originalStderr = System.err;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setErr( ps );

        FileChecksum fc = new FileChecksum( Paths.get( "nonexistentfile.txt" ));
        long checksum = fc.calculate();
        assertTrue( os.toString().startsWith( "Error: File nonexistentfile.txt not found" ));
        assertEquals( Status.FILE_ERROR, checksum );

        // restore stderr
        System.setErr( originalStderr );
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
            System.err.println( "error writing to test file in " +
                                 this.getClass().getCanonicalName() );
            fail( "failed due to IOException writing to test file");
        }

        FileChecksum fc = new FileChecksum( file );
        long result = fc.calculate();
        assertNotEquals(0L, result);
    }

    
}