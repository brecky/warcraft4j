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
package nl.salp.warcraft4j.clientdata.util.hash;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;

import static nl.salp.warcraft4j.clientdata.util.io.DataTypeUtil.toByteArray;
import static org.junit.Assert.assertEquals;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Lookup3HashTest {
    @Test
    public void shouldHash32bLe() {
        byte[] value = StringUtils.getBytesUtf8("Four score and seven years ago");
        assertEquals("17770551", Lookup3Hash.littleEndian32bit().hashHexString(value));
    }
}
