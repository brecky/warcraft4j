# Overview
CASC ( _Content Addressable Storage Container_ )is basically a virtual file system implemented by Blizzard for storing game data.
First implemented for Heroes of the Storm and later adopted as CASCv2 for World of Warcraft with the alpha version of Warlords of Draenor and Diablo 3 with Patch 2.2.0.

CASC seems to be implemented with some of the following principles in mind:
 * data integrity through strong use of hashing (on multiple levels)
 * streaming installation to allow for background installation while running a game (through the full availability of files in the NGDP CDN).
 * File/data seek performance (and basic obfuscation,at least since CASCv2, where they removed the any identifiable file name) by using hashes instead of identifiable names for files.

The rest of this file focusses on CASCv2 since CASCv1 is no longer used and primarily on the World of Warcraft implementation. 

# Key files

## .build.config
Main entry point for

## Install

Installation file descriptor mapping file names to hashes and size. Probably used to look up files in the _.data_ files.

### Structure

    <FileHeader>
    <EntryHeader>[FileHeader.entries]
    <Entry>[FileHeader.entries]

### File Header

    name         | data type | offset | length (b) | description
    -------------+-----------+--------+------------+-------------------------------
    magic string | char[2]   | 0x0    | 2          | always "IN" 
    unknown      | uint32    | 0x2    | 4          | unknown 
    entries      | uint32    | 0x6    | 4          | number of entries in the file 
 
### Entry Header

    name         | data type | offset | length (b) | description
    -------------+-----------+--------+------------+-------------------------------
    flag name    | char[var] |        | var        | Flag name for the entry (variable length) 
    flag type    | uint16    |        | 2          | The value shared amongst flags. (P.e. languages are '3', regions are '5', architecture type is '0'.
    file flags   | byte[28]  |        | 28         | Bit array represented in hex form where each bit represents an entry in the file. An enabled bit implies that the named by the flag is active.

### Entry

    name         | data type | offset | length (b) | description
    -------------+-----------+--------+------------+-------------------------------
    file name    | char[var] |        | var        | The name of the file (variable length) 
    hash         | char[16]  |        | 16         | The hash of the file (unknown whether it's compressed or uncompressed). Probably MD5, might be a Jenkins' hash of the name as well. To be tested.
    size         | byte[28]  |        | 28         | The size of the file in bytes.

