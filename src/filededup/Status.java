/*
 * Looks for duplicate files based on CRC-32 checksumming. 
 * Project requires JDK 8 or later.
 *
 * Copyright (c) 2015 by Andrew Binstock. All rights reserved.
 * Licensed under the Creative Commons Attribution, Share Alike license
 * (CC BY-SA). Consult: https://creativecommons.org/licenses/by-sa/4.0/
 */

package filededup;

/**
 * Status codes used by file-comparison routines
 * 
 * @author alb
 */
public class Status {
    public static final int FILES_SAME = 0;
    public static final int FILES_DIFFERENT = 1;
    public static final int FILE_ERROR = -1;    
}
