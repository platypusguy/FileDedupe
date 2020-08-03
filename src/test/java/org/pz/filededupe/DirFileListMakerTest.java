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
import java.nio.file.Path;
import java.util.ArrayList;
import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class DirFileListMakerTest {

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    DirFileListMaker dflm = new DirFileListMaker();

    @Test (expected = InvalidParameterException.class)
    public void testNullPath() {
        dflm.go( null, true );
        fail("Expected an InvalidParameterException to be thrown");
    }

    @Test (expected = InvalidParameterException.class)
    public void testEmptyPath() {
        Path p = new File("").toPath();
        dflm.go( p, true );
        fail("Expected an InvalidParameterException to be thrown");
    }

    @Test
    public void testWithOneFileInDirectory() {
        // This class goes through a directory and creates an ArrayList of all the files
        // This test checks that a directory of one file gets a single entry in the ArrayList

        try {
            folder.newFile("testfile.txt");
        }
        catch( IOException ioe ) {
            System.err.println(
                "error creating temporary test file in " + this.getClass().getSimpleName() );
        }

        ArrayList<Path> fileset = dflm.go( folder.getRoot().toPath(), false );
        assertEquals( 1L, fileset.size() * 1L);
    }
}
