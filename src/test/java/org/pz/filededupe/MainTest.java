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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test variety of ways to call the program
 * @author alb
 */
public class MainTest {

    @Test
    public void mainPrintsCopyrightWhenNoArgsArePassedToMain()
    {
        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        // pass in empty args.
        Main main = new Main();
        String emptyArgs[] = {};
        try {
            main.main(emptyArgs);
        }
        catch( Throwable t ) {
            fail( t.getMessage() );
        };

        String output = os.toString();
        assertTrue( output.startsWith("FileDedupe v."));

        // restore stdout
        System.setOut( originalStdout );
    }

    @Test
    public void mainPrintsUsageIfhPassedToMain()
    {
        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        // pass in empty args.
        Main main = new Main();
        String helpArg[] = { "-h" };
        try {
            main.main(helpArg);
        }
        catch( Throwable t ) {
            fail( t.getMessage() );
        };

        String output = os.toString();
        assertTrue( output.contains( "finds duplicate files"));

        // restore stdout
        System.setOut( originalStdout );
    }

    @Test
    public void mainPrintsErrorMsgIfTooManyArgsPassedToMain()
    {
        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        // pass in empty args.
        Main main = new Main();
        String manyArgs[] = { "aaa", "bbb", "ccc", "ddd", "eee", "fff", "ggg" };
        try {
            main.main(manyArgs);
        }
        catch( Throwable t ) {
            fail( t.getMessage() );
        };

        String output = os.toString();
        assertTrue( output.contains( "Invalid command." ));

        // restore stdout
        System.setOut( originalStdout );
    }
}
