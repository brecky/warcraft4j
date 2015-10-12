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
package nl.salp.warcraft4j.util.unsafe;

import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link UnsafeHelper}
 *
 * @author Barre Dijkstra
 */
public class UnsafeHelperTest {
    private static final int INT_VALUE_UNINITIALISED = 0;
    private static final int INT_VALUE = 1;

    private UnsafeHelper unsafeHelper;

    @Before
    public void setUp() {
        unsafeHelper = new UnsafeHelper();
    }

    @Test
    public void shouldCreateInstanceWithoutCallingConstructor() throws InstantiationException {
        TestType directInstance = unsafeHelper.createInstanceDirect(TestType.class);
        TestType normalInstance = new TestType();

        assertEquals("Directly created instance does not have an uninitialised value set.", INT_VALUE_UNINITIALISED, directInstance.getValue());
        assertEquals("Directly created instance does not have an static value set.", INT_VALUE, directInstance.getStaticValue());
        assertEquals("Normally created instances has unitialised value set.", INT_VALUE, normalInstance.getValue());
        assertNotEquals("Normally and directly created instances have same value set.", normalInstance.getValue(), directInstance.getValue());
    }

    @Test
    public void shouldDetermineMemoryAddressSize() {
        if (Unsafe.ADDRESS_SIZE == 8) {
            assertTrue("8 byte memory address size reported as non 64-bit", unsafeHelper.isMemoryAddress64bit());
            assertFalse("8 byte memory address size reported as 32-bit", unsafeHelper.isMemoryAddress32bit());
        } else if (Unsafe.ADDRESS_SIZE == 4) {
            assertTrue("8 byte memory address size reported as non 32-bit", unsafeHelper.isMemoryAddress32bit());
            assertFalse("8 byte memory address size reported as 64-bit", unsafeHelper.isMemoryAddress64bit());
        }
        assertEquals(unsafeHelper.getMemoryAddressSize(), Unsafe.ADDRESS_SIZE);
    }

    @Test
    public void shouldGetFieldFromType() {
        assertTrue("Unable to get instance field.", unsafeHelper.getField(TestType.class, "value").isPresent());
        assertTrue("Unable to get static field.", unsafeHelper.getField(TestType.class, "STATIC_VALUE").isPresent());
    }

    @Test
    public void shouldGetFieldFromParentType() {
        assertTrue("Unable to get parent field.", unsafeHelper.getField(TestType.class, "parentValue").isPresent());
    }

    @Test
    public void shouldNotGetFieldForNullValues() {
        Class<?> type = null;
        assertFalse("Field retrieved for null type.", unsafeHelper.getField(type, "value").isPresent());
        assertFalse("Non-existing field retrieved for null type.", unsafeHelper.getField(TestType.class, "nonExisting").isPresent());
    }

    @Test
    public void shouldGetStaticFieldFromType() {
        assertTrue("Unable to get static field.", unsafeHelper.getStaticField(TestType.class, "STATIC_VALUE").isPresent());
        assertFalse("Instance field retrieved.", unsafeHelper.getStaticField(TestType.class, "value").isPresent());
    }

    @Test
    public void shouldNotGetStaticFieldForNullValues() {
        Class<?> type = null;
        assertFalse("Static field retrieved for null type.", unsafeHelper.getStaticField(type, "STATIC_VALUE").isPresent());
        assertFalse("Object field retrieved for null type.", unsafeHelper.getStaticField(type, "value").isPresent());
        assertFalse("Non-existing static field retrieved for type.", unsafeHelper.getStaticField(TestType.class, "nonExisting").isPresent());
    }

    @Test
    public void shouldDetermineStaticField() {
        Field staticField = unsafeHelper.getStaticField(TestType.class, "STATIC_VALUE")
                .orElseThrow(() -> new AssertionError("Static field not found"));
        Field objectField = unsafeHelper.getObjectField(TestType.class, "value")
                .orElseThrow(() -> new AssertionError("Object field not found"));

        assertTrue("Static field not reported static.", unsafeHelper.isStaticField(staticField));
        assertFalse("Object field reported static.", unsafeHelper.isStaticField(objectField));
    }

    @Test
    public void shouldGetObjectFieldFromType() {
        assertTrue("Unable to get object field.", unsafeHelper.getObjectField(TestType.class, "value").isPresent());
        assertFalse("Static field retrieved.", unsafeHelper.getObjectField(TestType.class, "STATIC_VALUE").isPresent());
    }

    @Test
    public void shouldGetObjectFieldFromObject() {
        TestType instance = new TestType();
        assertTrue("Unable to get object field.", unsafeHelper.getObjectField(instance, "value").isPresent());
        assertFalse("Static field retrieved.", unsafeHelper.getObjectField(instance, "STATIC_VALUE").isPresent());
    }

    @Test
    public void shouldNotGetObjectFieldForNullValues() {
        TestType instance = null;
        Class<?> type = null;
        assertFalse("Object field retrieved for null object.", unsafeHelper.getObjectField(instance, "value").isPresent());
        assertFalse("Static field retrieved for null object.", unsafeHelper.getObjectField(instance, "STATIC_VALUE").isPresent());
        assertFalse("Object field retrieved for null type.", unsafeHelper.getObjectField(type, "value").isPresent());
        assertFalse("Static field retrieved for null type.", unsafeHelper.getObjectField(type, "STATIC_VALUE").isPresent());
        assertFalse("Non-existing object field retrieved for object.", unsafeHelper.getObjectField(new TestType(), "nonExisting").isPresent());
        assertFalse("Non-existing object field retrieved for type.", unsafeHelper.getObjectField(TestType.class, "nonExisting").isPresent());
    }

    @Test
    public void shouldDetermineObjectField() {
        Field objectField = unsafeHelper.getObjectField(TestType.class, "value")
                .orElseThrow(() -> new AssertionError("Object field not found"));
        Field staticField = unsafeHelper.getStaticField(TestType.class, "STATIC_VALUE")
                .orElseThrow(() -> new AssertionError("Static field not found"));

        assertTrue("Object field not reported as object field.", unsafeHelper.isObjectField(objectField));
        assertFalse("Static field reported as object field.", unsafeHelper.isObjectField(staticField));
    }

    @Test
    public void shouldGetStaticFieldOffset() {
        assertNotEquals("No offset retrieved for static field.", UnsafeHelper.INVALID_OFFSET, unsafeHelper.getStaticFieldOffset(TestType.class, "STATIC_VALUE"));
        assertEquals("Offset retrieved for object field.", UnsafeHelper.INVALID_OFFSET, unsafeHelper.getStaticFieldOffset(TestType.class, "value"));
    }

    @Test
    public void shouldGetObjectFieldOffset() {
        TestType instance = new TestType();
        assertNotEquals("No offset retrieved for object field.", UnsafeHelper.INVALID_OFFSET, unsafeHelper.getFieldOffset(instance, "value"));
        assertEquals("Offset retrieved for static field.", UnsafeHelper.INVALID_OFFSET, unsafeHelper.getFieldOffset(instance, "STATIC_VALUE"));
    }

    private static class ParentType {
        private final int parentValue = INT_VALUE;

        public final int getParentValue() {
            return parentValue;
        }
    }

    private static class TestType extends ParentType {
        private static final int STATIC_VALUE = INT_VALUE;
        private final int value;

        public TestType() {
            this.value = INT_VALUE;
        }

        public TestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public int getStaticValue() {
            return STATIC_VALUE;
        }
    }
}
