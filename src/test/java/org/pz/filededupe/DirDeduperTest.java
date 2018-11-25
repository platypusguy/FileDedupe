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
        DirDeduper dd = new DirDeduper( "");
        fail("Expected an InvalidPathException to be thrown in " + this.getClass().getSimpleName());
    }

}