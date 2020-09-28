/*
 * Project looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;



/**
 * Test the routine that checks input files for their sizes
 * @author alb
 */
public class FileSizerTest {

    private File file1, file2, file3;

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /* executed before every test */
    @Before
    public void setUp() {
        try {
            file1 = folder.newFile("testfile1.txt");
            file2 = folder.newFile("testfile2.txt");
            file3 = folder.newFile("testfile3.txt");
        }
        catch( IOException ioe ) {
            System.err.println( "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }
    }

    /**
     *  Write the same content to two files, and something different to a third file.
     *  Then run all three through the FileSizer. The result should be a  filesize
     *  table with two entries (since two files will have the same file size,
     *  due to identical content, they'll be placed in one filesize entry).
     */
    @Test
    public void test3FilesToBeChecksummed2ofWhichAreDupes() {

        //write out data to the test files
        try {

            FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
            BufferedWriter bw1 = new BufferedWriter(fw1);
            bw1.write( "content for file1 and file2");
            bw1.close();

            FileWriter fw2 = new FileWriter(file2.getAbsoluteFile());
            BufferedWriter bw2 = new BufferedWriter(fw2);
            bw2.write( "content for file1 and file2");
            bw2.close();

            FileWriter fw3 = new FileWriter(file3.getAbsoluteFile());
            BufferedWriter bw3 = new BufferedWriter(fw3);
            bw3.write( "content for file3");
            bw3.close();
        }
        catch( IOException ioe ) {
            System.err.println( "Error creating temporary test files in " +
                this.getClass().getSimpleName() );
        }

        LongStringListTable filesizeTable = new LongStringListTable();

        FileSizer fsizer = new FileSizer();
        fsizer.loadFileSizes(
            folder.getRoot().getAbsolutePath(), true, filesizeTable );


        // The table should contain two entries: that is, two file sizes--
        // one file size will have two files attached to it, the other
        // filesize will have only file3 attached to it.
        assertEquals( 2, filesizeTable.getKeySet().size() );

    }
}