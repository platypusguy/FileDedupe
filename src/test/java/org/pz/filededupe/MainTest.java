/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 11 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Test variety of ways to call the program
 * @author alb
 */
public class MainTest {

    @Test
    public void mainPrintsCopyrightAndUsageWhenNoArgsArePassedToMain()
    {
        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setOut( ps );

        // pass in empty args.
        Main main = new Main();
        String[] emptyArgs = {};
        try {
            main.main(emptyArgs);
        }
        catch( Throwable t ) {
            fail( t.getMessage() );
        }

        String output = os.toString();
        assertTrue( output.startsWith( "FileDedupe v." ));
        assertTrue( output.contains( "FileDedupe finds duplicate" ));

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
        String[] helpArg = { "-h" };
        try {
            main.main(helpArg);
        }
        catch( Throwable t ) {
            fail( t.getMessage() );
        }

        String output = os.toString();
        assertTrue( output.contains( "finds duplicate files" ));

        // restore stdout
        System.setOut( originalStdout );
    }

    @Test
    public void mainPrintsErrorMsgIfInvalidArgPassedToMain()
    {
        PrintStream originalStdout = System.out;

        // capture stdout
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream( os );
        System.setErr( ps );

        // pass in invalid dirs.
        Main main = new Main();

        try {
            String[] invalidDirs = { "-invalidArg" };
            main.main( invalidDirs );
        }
        catch( Throwable t ) {
            fail( t.getMessage() );
        }

        String output = os.toString();
        assertTrue( output.startsWith( "Invalid command:" ));

        // restore stdout
        System.setOut( originalStdout );
    }
        //TODO: Write test for only a dash option other than -h being passed
}
