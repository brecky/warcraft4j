#1 data.XXX Files
##1.1 Summary
All data.XXX files (from now on called 'data files') are simple file containers.
They contain bare files only, no indexing information. All contained files are stored as a whole sequential binary block with a short header (30 bytes).
Due to technical limitations (confer the IDX file addressing scheme), no data file can be larger than 2^30 = 1,073,741,824 bytes.
However, there can be up to 2^10 = 1,024 data files resulting in a total addressable amount of 1TiB of compressed data (including overhead).

##1.2 File format
###1.2.1 Global format
	[Block] [Block] [Block] .... [Block]

###1.2.2 [Block] format
	16 Byte - MD5 Checksum (not sure of what, probably the header?)
	4  Byte - Size of entire block (including above checksum) in bytes [LE]
	10 Byte - Unknown
	[Data]  -  Length: Size of entire block minus 30 bytes