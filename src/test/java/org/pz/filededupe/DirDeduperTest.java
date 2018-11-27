/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015-19 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.InvalidPathException;

import static org.junit.Assert.*;

public class DirDeduperTest {

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test (expected = NullPointerException.class)
    public void testNullPathToDir() {
        DirDeduper dd = new DirDeduper( null );
        fail("Expected an NullPointerException to be thrown in " + this.getClass().getSimpleName());
    }

    @Test (expected = InvalidPathException.class)
    public void testNonDirectoryPath() {
        DirDeduper dd = new DirDeduper( "" );
        fail("Expected an InvalidPathException to be thrown in " + this.getClass().getSimpleName());
    }

    /**
     * An empty directory should generate a message on stdout and return false from go(), indicating
     * that no duplicates were found. Both things are tested here.
     */
    @Test
    public void testEmptyDirectory() {

        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        // create empty subdirectory in temporary directory, and pass it to go()
        File createdSubFolder = null;
        try {
            createdSubFolder = folder.newFolder("subfolder");
        } catch( IOException ioe ) {
            fail( "IOException in " + this.getClass().getSimpleName() );
        }
        DirDeduper dd = new DirDeduper( createdSubFolder.getPath() );
        assertFalse( dd.go() );

        String output = os.toString();
        assertTrue( output.contains( "contains no files" ));

        // restore stdout
        System.setOut( originalStdout );
    }

    /**
     * A directory with 1 file should print to stdout that 1 file was found and return false, that is,
     * that there were no duplicates found. This tests both things.
     */
    @Test
    public void testDirectoryWith1File() {

        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        try {
            folder.newFile("singlefile");
        } catch( IOException ioe ) {
            fail( "IOException in " + this.getClass().getSimpleName() );
        }

        DirDeduper dd = new DirDeduper( folder.getRoot().getPath() );
        assertFalse( dd.go() );

        String output = os.toString();
        assertTrue( output.contains( "Number of files found to check: 1" ));

        // restore stdout
        System.setOut( originalStdout );
    }

    /**
     * A directory with 2 zero-length files should print to stdout that 2 files were found and
     * return true, that is, that the files were duplicates. This tests both things.
     */
    @Test
    public void testDirectoryWith2ZeroLengthFiles() {
        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        try {
            folder.newFile("zeroLengthFile1");
            folder.newFile("zeroLengthFile2");
        } catch( IOException ioe ) {
            fail( "IOException in " + this.getClass().getSimpleName() );
        }

        DirDeduper dd = new DirDeduper( folder.getRoot().getPath() );
        assertTrue( dd.go() );

        String output = os.toString();
        assertTrue( output.contains( "Number of files found to check: 2" ));
        assertTrue( output.contains( "These files are the same:" ));
        assertTrue( output.contains( "zeroLengthFile1" ));
        assertTrue( output.contains( "zeroLengthFile2" ));

        // restore stdout
        System.setOut( originalStdout );
    }

}