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

package nl.salp.warcraft4j.wowclient.dbc.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to wire a field or setter-method to a DBC field.
 *
 * @author Barre Dijkstra
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface DbcField {
    /** The (optional) name of the field, {@code default ""}. */
    String name() default "";

    /** The column of the field in the DBC file. */
    int column();

    /** Number of times the field occurs concurrently ({@code &gt; 1 implies an array of the field}), {@code default 1}. */
    int numberOfEntries() default 1;

    /** The length of the field in bytes, {@code default: 1}. */
    int length() default 1;

    /** The DBC data type of the field. */
    DbcDataType dataType();
}
