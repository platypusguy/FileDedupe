/*
 * Looks for duplicate files based on CRC-32 checksumming.
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2017-20 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package org.pz.filededupe;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Accepts a path and walks the entire path (including subdirectories)
 * and creates a list of all files in the path.
 *
 * All exceptions are caught in this class and the user message is emitted here.
 * 
 * @author alb
 */
class DirFileListMaker
{
    ArrayList<Path> go( Path dir, boolean scanSubDirs ) {
        if( dir == null || dir.toString().isEmpty() )
            throw( new InvalidParameterException(
                "Error: Directory to process is null or empty in " +
                    this.getClass().getSimpleName()));

        ArrayList<Path> fileSet;
        try {
            //Files.walk's second param gives depth of subdirs to search
            // Integer.MAX_VALUE means, search all subdirectories
            fileSet =
                Files.walk( dir, scanSubDirs? 0 :Integer.MAX_VALUE )
                    .filter( p -> p.toFile().isFile() )
                    .peek(System.out::println)
                    .collect( Collectors.toCollection( ArrayList::new ));
        } catch( Throwable t ) {
            System.err.println("Exception creating fileset in " +
                                     this.getClass().getSimpleName());
            //return an empty ArrayList in case of error
            return( new ArrayList<>(0) );
        }
        return( fileSet );
    }
}
