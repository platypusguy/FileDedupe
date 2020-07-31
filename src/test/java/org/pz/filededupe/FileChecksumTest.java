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
            System.err.println( "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }
    }

    /**
     * Run the checksum on a non-existent file. Should write error message to stderr.
     */
    @Test
    public void calculateOnNonExistentFile() {

        PrintStream originalStderr = System.err;

        // capture stderr
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setErr( ps );

        FileChecksum fc = new FileChecksum( Paths.get( "nonexistentfile.txt" ));
        try {
            fc.calculate();
        }
        catch( IOException ignored ) {
            //do nothing, as we want to cause the exception and check the error message
        }
        assertTrue( os.toString().startsWith( "Error: File nonexistentfile.txt not found" ));

        // restore stderr
        System.setErr( originalStderr );
    }

    /**
     * Create an empty temp file and test that its checksum is zero
     */
    @Test
    public void calculateOnEmptyFile() {
        FileChecksum fc = new FileChecksum( file );
        try {
            long result = fc.calculate();
            assertEquals(0L, result);
        }
        catch( IOException e ) {
            fail( "Unexpected exception in test " +
                  this.getClass().getSimpleName() );
        }
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
        try {
            long result = fc.calculate();
            assertNotEquals(0L, result);
        }
        catch( IOException e ) {
            fail( "Unexpected exception in test " +
                this.getClass().getSimpleName() );
        }
    }
}