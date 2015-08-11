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
package nl.salp.warcraft4j.data.casc;

import nl.salp.warcraft4j.Checksum;
import nl.salp.warcraft4j.DataTypeUtil;

import java.util.Arrays;

import static java.lang.String.format;
import static nl.salp.warcraft4j.DataTypeUtil.byteArrayToHexString;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FileKey extends Checksum {
    private static final int FILEKEY_LENGTH = 9;
    private final byte[] fileKey;
    private final int hash;


    public FileKey(byte[] checksum) {
        super(checksum);
        if (getChecksum().length > FILEKEY_LENGTH) {
            fileKey = trim(FILEKEY_LENGTH).getChecksum();
        } else if (getChecksum().length == FILEKEY_LENGTH) {
            fileKey = checksum;
        } else {
            throw new IllegalArgumentException(format("Unable to create a 9 byte filekey from a %d byte array.", checksum.length));
        }
        hash = DataTypeUtil.hash(fileKey);
    }

    public byte[] getFileKey() {
        return fileKey;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && FileKey.class.isAssignableFrom(obj.getClass())) {
            eq = Arrays.equals(fileKey, ((FileKey) obj).fileKey);
        }
        return eq;
    }

    @Override
    public String toString() {
        return byteArrayToHexString(fileKey);
    }
}