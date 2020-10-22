# FileDedupe
Utility to list duplicate files in one or more directories. 

## What it is

FileDedupe is a utility that checks one or more directories for duplicate files. Just run it with a list of directories on the command line. The default is to check all subdirectories. This can be controlled (see below). The output is a text file, which is written to stdout consists of the name of files that have duplicates. The file is given followed by its duplicates.

An article on this utility and how it was designed and written appears in [Oracle's Java Magazine](https://blogs.oracle.com/javamagazine/the-joy-of-writing-command-line-utilities-finding-duplicate-files-part-1)

Version 1.0 used a brute-force approach of running checksums on every file in the user-specified directories and then comparing the checksums to identify duplicates. This worked well, but was slow. 

Version 2.0 uses comparisons of file sizes to greatly reduce the number of files that require checksums. It runs 9x-11x faster on the test directories. Use this version for your own needs. The optimization that delivered this benefit is described in [this article in Oracle's Java Magazine](https://blogs.oracle.com/javamagazine/the-joy-of-writing-command-line-utilities-part-2-the-souped-up-way-to-find-duplicate-files)

## How to run
FileDedupe is written in Java 8. To run it, run the JAR file with the directory or directories to scan for duplicates. Note that directory of `.` is supported.
Options:

`-nosubdirs` this flag prevents FileDedupe from checking subdirectories for duplicates.

`-tofile` this flag saves a file with the duplicates in current user directory with filename format "dupes-yyyy-MM-dd---HH-mm-ss.txt"

`-help` or `-h` or `--h`: shows this usage information

So, to run the utility on in the current directory:

`java -jar filededupe-2.0.jar .`

## Testing
The tests included here generate code coverage of 80%. And FileDedupe has been tested repeatedly on directories of more than 600,000 files. 

## Thanks
Thanks to Oracle's _Java Magazine_ for publishing the articles on this utility. 

Thanks to JetBrains for supporting open source by providing a license to [IntelliJ IDEA](https://www.jetbrains.com/idea/), which is an IDE that I have used since version 3.5.


