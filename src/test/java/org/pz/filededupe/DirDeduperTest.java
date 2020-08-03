/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2018-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class DirDeduperTest {

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test (expected = NullPointerException.class)
    public void testNullPathToDir() {
        new DirDeduper().go( null, false, new DupeTable() );
        fail("Expected a NullPointerException to be thrown in " +
            this.getClass().getSimpleName());
    }

    @Test (expected = InvalidPathException.class)
    public void testNonDirectoryPath() {
        new DirDeduper().go( "", false, new DupeTable() );
        fail("Expected a InvalidPathException to be thrown in " +
            this.getClass().getSimpleName());
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
        DirDeduper dd = new DirDeduper();
        assertFalse( dd.go( createdSubFolder.getPath(), true, new DupeTable() ) );

        String output = os.toString();
        assertTrue( output.contains( "contains no files" ));

        // restore stdout
        System.setOut( originalStdout );
    }

    /**
     * A directory with 1 file should print to stdout that 1 file was found and return false,
     * that is, that there were no duplicates found. This tests both things when subdirs are scanned
     */
    @Test
    public void testDirectoryWith1FileWithSubdirs() {

        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        try {
            folder.newFile( "singlefile" );
        } catch( IOException ioe ) {
            fail( "IOException in " + this.getClass().getSimpleName() );
        }

        DupeTable dupeTable = new DupeTable();
        String directory =  folder.getRoot().getPath();
        boolean noSubdirs = false;
        DirDeduper dd = new DirDeduper();
        boolean dupesFound = dd.go( directory, noSubdirs, dupeTable);
        assertFalse( dupesFound );

        String output = os.toString();
        assertTrue( output.contains( "Number of files found to check: 1" ));

        // restore stdout
        System.setOut( originalStdout );
    }

    /**
     * A directory with 1 file should print to stdout that 1 file was found and return false,
     * that is, that there were no duplicates found. This tests both things when no subdirs are scanned.
     */
    @Test
    public void testDirectoryWith1FileWithNoSubdirs() {  //TODO: This test fails, but the previous one passes
                                                         // only difference is -nosubdirs flag
                                                         // Also see the TODO item in next test.
                                                         // If not quickly solved, create issue.

        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        try {
            folder.newFile( "singlefile" );
        } catch( IOException ioe ) {
            fail( "IOException in " + this.getClass().getSimpleName() );
        }

        DupeTable dupeTable = new DupeTable();
        String directory =  folder.getRoot().getPath();
        boolean noSubdirs = true;
        DirDeduper dd = new DirDeduper();
        boolean dupesFound = dd.go( directory, noSubdirs, dupeTable);
        assertFalse( dupesFound );

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

        DupeTable dupeTable = new DupeTable();
        String directory =  folder.getRoot().getPath();
        boolean noSubdirs = false;         //TODO: This test fails when = true, but it should pass.
                                           // see note in previous test
        DirDeduper dd = new DirDeduper();
        boolean dupesFound = dd.go( directory, noSubdirs, dupeTable);
        assertTrue( dupesFound );

        String output = os.toString();
        assertTrue( output.contains( "Number of files found to check: 2" ));
        assertTrue( output.contains( "These files are the same:" ));
        assertTrue( output.contains( "zeroLengthFile1" ));
        assertTrue( output.contains( "zeroLengthFile2" ));

        // restore stdout
        System.setOut( originalStdout );
    }

}