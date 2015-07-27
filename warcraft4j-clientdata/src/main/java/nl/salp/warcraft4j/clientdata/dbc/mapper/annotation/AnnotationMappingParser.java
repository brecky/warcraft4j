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
package nl.salp.warcraft4j.clientdata.dbc.mapper.annotation;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.DbcUtil;
import nl.salp.warcraft4j.clientdata.dbc.mapper.DbcFileMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcReference;

import static java.lang.String.format;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
public class AnnotationMappingParser {
    private final Class<? extends DbcEntry>[] entryTypes;

    public AnnotationMappingParser(Supplier<Class<? extends DbcEntry>[]> entryTypesSupplier) {
        this.entryTypes = entryTypesSupplier.get();
    }

    public DbcFileMapping[] parseMappings() {
        return Stream.of(entryTypes).map(AnnotationMappingParser::parse).toArray(AnnotationDbcFileMapping[]::new);
    }

    private static DbcFileMapping parse(Class<? extends DbcEntry> entryType) {
        DbcMapping mapping = DbcUtil.getAnnotation(entryType, DbcMapping.class)
                                 .orElseThrow(() -> new RuntimeException(format("No DBC mapping present for class %s", entryType.getName())));
        DbcType dbcType = DbcUtil.getDbcType(entryType).orElseThrow(() -> new RuntimeException(format("No DBC type present for class %s", entryType.getName())));
        AnnotationDbcFieldMapping[] fieldMappings = DbcUtil.getFieldMappings(entryType, true).stream().map(AnnotationMappingParser::parse)
                                                        .toArray(AnnotationDbcFieldMapping[]::new);
        return new AnnotationDbcFileMapping(entryType, mapping.file(), dbcType, fieldMappings);
    }

    private static AnnotationDbcFieldMapping parse(Field field) {
        DbcFieldMapping mapping = DbcUtil.getFieldMapping(field)
                                      .orElseThrow(() -> new RuntimeException(format("No DBC type present for field %s#%s", field.getName(), field.getDeclaringClass().getName())));
        DbcType referencedType = null;
        Optional<DbcReference> reference = DbcUtil.getAnnotation(field, DbcReference.class);
        if (reference.isPresent()) {
            referencedType = reference.get().type();
        }
        return new AnnotationDbcFieldMapping(field.getName(), referencedType, mapping);
    }
}
