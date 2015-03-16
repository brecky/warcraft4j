/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package nl.salp.warcraft4j.files.blte;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class BlteFile {
    /*
Format
	4  Byte - 'BLTE' magic
	4  Byte - CompressedDataOffset in bytes [BE]

	if(CompressedDataOffset > 0) {
		2  Byte - Unknown, [BE] probably
		2  Byte - Number of chunks [BE]
		for(Number of chunks) {
			4  Byte - Compressed chunk size in bytes [BE]
			4  Byte - Decompressed chunk size in bytes [BE]
			16 Byte - Chunk MD5 Checksum (of compressed chunk data I think)
		}

		for(Number of chunks) {
			[CompressedDataBlock]
		}
	} else {
		[CompressedDataBlock]
	}

[CompressedDataBlock] format
	1  Byte - Compression type of [Data] (Currently using - 'N': No compression, 'Z': Deflate (ZLib))
	[Data]  - Length: Compressed size minus 1 byte

     */
}
