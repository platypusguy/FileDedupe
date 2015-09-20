/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package filededup;

import java.io.File;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * De-duplicates a directory
 * @author alb (Andrew Binstock)
 */
public class DirDeduper {
    File dir;
    String origPath;
    int i = 0;
    
    public DirDeduper( String pathToDir ) {
        origPath = pathToDir;
        dir = new File( pathToDir );
    }
    
    public int go()
    {
        if( ! dir.isDirectory() ) {
            System.err.println( "Error: " + origPath + " is not a directory" );
            return( Status.FILE_ERROR );
        }
        
        Path path = FileSystems.getDefault().getPath( origPath );

        try (
            Stream<Path> entries = Files.walk( path )
                                        .filter( p-> p.toFile().isFile())
                                        .peek(p->treadle(p)))
//                                        .peek(System.out::println))
        {
            Object[] entriesArray = entries.toArray();
       
//                 Files.walk( path )
//                      .filter( p-> p.toFile().isFile()).toArray(); 
//            
            for( Object f: entriesArray )
                System.out.println( f.toString() );
////            { 
//                Object allFiles[] = entries.toArray();
//                for( Object f : allFiles ) {
//                    if( new File( f.toString() ).isFile())
//                        System.out.println( f.toString() );
//                }
            System.out.println( "Count: " + i + " files");
            }
            catch( Throwable t ) {
                //TODO
            }
            return( 0 );
        }
    public void treadle( Path p ) {
        i++;
    }
}