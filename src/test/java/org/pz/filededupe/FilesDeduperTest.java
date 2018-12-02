/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015-19 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import org.junit.Test;

import static org.junit.Assert.fail;

public class FilesDeduperTest {
    @Test( expected = NullPointerException.class )
    public void testNulls() {
        new FilesDeduper( null );
        fail( "Should have thrown NPE in " + this.getClass().getSimpleName() );
    }
}
