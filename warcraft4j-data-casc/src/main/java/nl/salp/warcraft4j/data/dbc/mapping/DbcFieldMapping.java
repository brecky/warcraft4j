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

package nl.salp.warcraft4j.data.dbc.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping a DBC field.
 * <p>
 *     Annotate
 * </p>
 *
 * @author Barre Dijkstra
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DbcFieldMapping {
    /**
     * The order in which this field should be parsed.
     *
     * @return The order.
     */
    int order();

    /**
     * The number of entries (concurrent) of the field (1 for single entry, any value &gt; 1 being an array of the type).
     * <p/>
     * Default: {@code 1}
     *
     * @return The length in bytes.
     */
    int numberOfEntries() default 1;

    /**
     * The length of the field in bytes, where a length of &lt;= 0 defaults to the length of the data type used.
     * <p/>
     * Default: {@code 0}
     *
     * @return The length in bytes.
     */
    int length() default 0;

    /**
     * Get the data type for the field.
     *
     * @return The data type.
     */
    DbcDataType dataType();

    /**
     * Flag indicating if the field is padding.
     * <p/>
     * Default: {@code false}
     *
     * @return {@code true} if the field is for data padding (and thus should not be parsed).
     */
    boolean padding() default false;

    /**
     * Flag indicating if the meaning of the field is known.
     * <p/>
     * Default: {@code true}
     *
     * @return {@code true} if the meaning of the field is known.
     */
    boolean knownMeaning() default true;

    /**
     * The explicit endianess of the field in the data file, superseding the data type default.
     * <p/>
     * {@code -1} for little endian, {@code 0} for field type default or {@code 1} for big endian.
     * <p/>
     * Default: {@code 0}
     *
     * @return The endianess.
     */
    int endianess() default 0;
}
