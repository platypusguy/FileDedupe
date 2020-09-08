/*
 * Scans directories (and optionally subdirectories) for all files and looks up their size
 *
 * Copyright (c) 2020 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */
package org.pz.filededupe;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Engine that runs through a directory (and optionally subdirectories),
 * extracts all the file names and size of the files and puts both into
 * a table
 *
 * @author alb
 */
public class FileSizer {

    public FileSizer() {}

    /**
     * Creates a lits of all files in a directory (and, optionally its subdirectories),
     * gets the length of the files and puts the length and filename into a table.
     * @param dir  the directory to scan
     * @param nosubdirs  toggle saying whether to visit subdirectories
     * @param sizesTable the table that holds the file sizes and the corresponding filenames
     */
    public void loadFileSizes(String dir, boolean nosubdirs, LongStringListTable sizesTable) {
        ArrayList<Path> files =
            new DirFileListMaker().go( FileSystems.getDefault().getPath( dir ), nosubdirs);

        files.forEach(  //for each file get the file length and add it to sizesTable
            f-> {
                sizesTable.insertEntry( f.toString(), f.toFile().length() );
                System.out.println( f.toString() +": "+ f.toFile().length());
            }
        );
        //note: we don't worry about an empty directory here.
    }
}
