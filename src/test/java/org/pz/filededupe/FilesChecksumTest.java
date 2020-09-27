/*
 * Project looks for duplicate files based on CRC-32 checksumming.
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test the routine that drives the checksumming of individual files
 * @author alb
 */
public class FilesChecksumTest {

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
     *  Then run all three through the checksumming driver. The result should be a
     *  checksum table with two entries (since two files will have the same checksum,
     *  due to identical content).
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
            System.err.println( "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }

        ArrayList<String> fileList  = new ArrayList<String>();
        fileList.add( file1.getAbsolutePath() );
        fileList.add( file2.getAbsolutePath() );
        fileList.add( file3.getAbsolutePath() );

        LongStringListTable checksumTable = new LongStringListTable();

        FilesChecksum fschksum =
            new FilesChecksum( fileList, checksumTable );
        fschksum.go();

        // The table should contain two entries: that is, two checksums--
        // one checksum will have two files attached to it, the other
        // checksum will have onl file3 attached to it.
        assertEquals( 2, checksumTable.getKeySet().size() );


    }

}
