# FileDedupe
Utility to list duplicate files in one or more directories. 

## What it is

FileDedupe is a utility that checks one or more directories for duplicate files. Just run it with a list of directories on the command line. The default is to check all subdirectories. This can be controlled (see below). The output is a text file, which is written to stdout consists of the name of files that have duplicates. The file is given followed by its duplicates.

An article on this utility and how it was designed and written appears in [Oracle's Java Magazine](https://blogs.oracle.com/javamagazine/the-joy-of-writing-command-line-utilities-finding-duplicate-files-part-1)

## How to run
FileDedupe is written in Java 8. To run it, run the JAR file with the directory or directories to scan for duplicates. Note that directory of `.` is supported.
Options:

`-nosubdirs` this flag prevents FileDedupe from checking subdirectories for duplicates.

`-help` or `-h`: shows this usage information

## Testing
The tests included here generate code coverage of 90%. And FileDedupe has been tested repeatedly on directories of more than 600,000 files. 

## Thanks
Thanks to Oracle's _Java Magazine_ for running the articles on this utility. 

Thanks to JetBrains for supporting open source by providing a license to [IntelliJ IDEA](https://www.jetbrains.com/idea/), which is an IDE that I have used since version 3.5.


