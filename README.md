#Warcraft4J - Java World of Warcraft API
The aim of this API is to provide developers a toolbox for implementing World of Warcraft related tools by providing a Java model for the World of Warcraft entities,
as well as APIs for parsing data, analysing data and basic analysis/theorycrafting algorithms.
 

## Usage
If you are looking for a simple API to implement functionality such as the WoW patch diffs, creating a WoW content driven website (such as wowhead.com again)
 or an API to take care of all the scaffolding while writing a theorycrafting tool, this might be the API for you.
If you're looking to develop hacks (you suck, seriously. and why look at Java?!?), alter the game to give you xyz benefits, or implement a WoW clone (again... Java?!?) this API will not provide anything you're looking.
Neither does this API provide an out-of-the-box working application or example application, actual work to make use of this API is required ;-)

## Development timelines and API stability
This API is in _very_ early stages of development and will (most certainly) undergo major refactoring, not excluding removal of modules, deletion of interfaces and rewrites of interfaces.

Given the complexity of the project and the underlying model, the fact that there's a single developer who does this in his spare time, as well as the goals of the project, it will take a while before this API even reaches a semi-stable alpha state.

#Project modules
The project is set up as a multi-module project which allows for minimising the amount of classes included dependencies to the functionality required.

Most modules, where applicable, will have a _nl.salp.warcraft4j.{module}.guice_ package containing a Google Guice module for binding the module. 

##Core Warcraft4J modules
The modules in this category are common modules that will be used by most other modules.

 * **warcraft4j-core** - Core Warcraft4J classes and general purpose utilities used by other modules (such as hashing implementations, data utilities, etc.)
 * **warcraft4j-model** - The domain model of Warcraft4J, providing a Java abstraction of the World of Warcraft entities.

##Casc modules
The modules in this category are CASC ( _Content Addressable Storage Container_ ) implementations for reading CASC based files from various sources. 

 * **warcraft4j-casc** - API for reading a CASC ( _Content Addressable Storage Container_ ), plain API and does not contain CASC parsing/reading logic.
 * **warcraft4j-casc-cdn** - CASC implementation for reading data from a CDN or a local installation. 
 * **warcraft4j-casc-dataparser** - Dataparser for parsing various file formats contained in CASC. 
 * **warcraft4j-casc-neo4j** - CASC implementation for storing/reading CASC related (meta-) data from Neo4J.

##Battle.NET modules
The modules in this category provide access to the Battle.NET API.

 * **warcraft4j-battlenet** - Blizzard Battle.NET API wrapper.

##Utility modules
The modules in this category are utility modules for I/O, database access, data manipulation ,etc.

 * **warcraft4j-io** - General I/O library used by other modules for reading and parsing data (including unsigned values and data endianess switching).
 * **warcraft4j-io-file** - File implementation of _warcraft4j-io_.
 * **warcraft4j-io-http** - HTTP implementation of _warcraft4j-io_.
 * **warcraft4j-io-neo4j** - Neo4J wrapper.

##Tooling and development support modules
The modules in this category are a collection of utilities and applications to support development, data analysis and so forth.

 * **warcraft4j-devtools** - Collection of various tools to support developers (such as analysing raw data, calculate data graphs, etc.)

#Further reading
Various (un)related documentation can be found under /resources/documentation

#Licensing
The project is licensed under the Apache License version 2.0, which is available in the LICENSE file and at http://www.apache.org/licenses/LICENSE-2.0.

The licenses of all libraries in use, both compile-time and runtime, can be found in the ./licenses/ directory and should be compatible with the project license.

*In case of any oversights, incompatibilities, etc. please contact project lead so adjustments can be made to resolve the eventual problems.*