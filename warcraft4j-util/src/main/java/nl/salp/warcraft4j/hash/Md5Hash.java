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

package nl.salp.warcraft4j.hash;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * {@link Hash} implementation for MD5 hashes.
 *
 * @author Barre Dijkstra
 */
public class Md5Hash implements Hash {
    @Override
    public byte[] hash(byte[] data) {
        return DigestUtils.md5(data);
    }

    @Override
    public byte[] hash(String data) {
        return hash(StringUtils.getBytesUtf8(data));
    }

    @Override
    public String hashHexString(byte[] data) {
        return Hex.encodeHexString(hash(data));
    }

    @Override
    public String hashHexString(String data) {
        return hashHexString(StringUtils.getBytesUtf8(data));
    }
}
