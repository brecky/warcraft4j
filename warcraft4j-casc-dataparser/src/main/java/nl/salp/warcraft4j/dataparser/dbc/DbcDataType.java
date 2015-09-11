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
package nl.salp.warcraft4j.dataparser.dbc;

/**
 * DBC data type.
 *
 * @author Barre Dijkstra
 */
public enum DbcDataType {
    /** 8-bit signed byte. */
    BYTE,
    /** 8-bit unsigned integer. */
    UINT8,
    /** 16-bit signed integer. */
    INT16,
    /** 16-bit unsigned integer. */
    UINT16,
    /** 32-bit signed integer. */
    INT32,
    /** 32-bit unsigned integer. */
    UINT32,
    /** 64-bit signed integer. */
    INT64,
    /** 64-bit unsigned integer. */
    UINT64,
    /** 32-bit signed floating point. */
    FLOAT,
    /** 64-bit signed floating point. */
    DOUBLE,
    /** String. */
    STRING,
    /** StringTable reference. */
    STRINGTABLE_REFERENCE,
    /** Boolean (TODO 8-bit or 32-bit??). */
    BOOLEAN;
}
