Map information
===============
.wmo
----
.wmo files contain world map objects.
There are 2 different .wmo file types:
 - .wmo root files which lists textures (.blp), doodads (.m2 and .mdx), etc. and orientation for .wmo groups
 - .wmo group files which ontain 3d model data for one unit in the world map object.

.adt
----
ADT files contain terrain and object information for map tiles. They have a chunked file structure.
Versions: ADT/v18, ADT/v22, ADT/v23

.wdt
----
.wdt files specify exactly which map tiles are present in a world, if any, and can also reference a "global" .wmo file. They have a chunked file structure.

Models & Rendering
==================

.blp
----
.blp files store textures with precalculated mipmaps.
Can contain various texture types (mostly S3TC compressed; DXT1, DXT3, DXT5).

.bls
----
.bls files specify specific instructions ot the video card as to how to render parts of the world and how to do certain effects.
.bls files can be found under Shaders\Pixel as well as Shaders\Vertex. They are refernced from .wfx files as well as directly from WoW.exe, so there is no client database pointing to them.

There are different types of shaders.

 * Vertex shaders: arbvp1, arbvp1_cg12, vs_1_1, vs_2_0, vs_3_0
 * Pixel shaders: arbfp1, nvrc, nvts, ps_1_1, ps_1_4, ps_2_0, ps_3_0

.lit
====
.lit files are obsolete files storing lighting information

Video, Music & Sound
====================
.avi
----
TODO

.mp3
----
TODO

.ogg
----
TODO

Client-side Database
====================
.dbc
----
Client side database files

.db2
----
Version 2 of the client side databases (cataclysm and later)

.adb
----
TODO

Scripting / addons
==================
.lua
----
TODO

.wtf
----
TODO

Other / various
===============
.html
-----
TODO

.ini
----
TODO

.xml
----
TODO

.xsd
----
TODO

TODO
====

    .lst
    .toc
    .anim
    .blob
    .bone
    .m2
    .phys
    .sbt
    .sig
    .skin
    .tex
    .ttf
    .txt
    .wdl
    .wfx
    .what
    .zmp
    signaturefile
