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

package nl.salp.warcraft4j.wowclient.model;

import nl.salp.warcraft4j.wowclient.databaseclient.Dbc;
import nl.salp.warcraft4j.wowclient.databaseclient.datatype.DbcDataTypes;
import nl.salp.warcraft4j.wowclient.databaseclient.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
@Dbc("ItemSubClass.dbc")
public class ItemSubClass {
    @DbcField(column = 1, dataType = DbcDataTypes.INT32)
    private int id;
    @DbcField(column = 2, dataType = DbcDataTypes.INT32)
    private int classId;
    @DbcField(column = 3, dataType = DbcDataTypes.INT32)
    private int subClassId;
    @DbcField(column = 4, dataType = DbcDataTypes.INT32)
    private int prerequisiteProficiency;
    @DbcField(column = 5, dataType = DbcDataTypes.INT32)
    private int postrequisiteProficiency;
    @DbcField(column = 6, dataType = DbcDataTypes.INT32)
    private int flags;
    @DbcField(column = 7, dataType = DbcDataTypes.INT32)
    private int displayFlags;
    @DbcField(column = 8, dataType = DbcDataTypes.INT32)
    private int weaponParrySeq;
    @DbcField(column = 9, dataType = DbcDataTypes.INT32)
    private int weaponReadySeq;
    @DbcField(column = 10, dataType = DbcDataTypes.INT32)
    private int weaponAttackSeq;
    @DbcField(column = 11, dataType = DbcDataTypes.INT32)
    private int weaponSwingSize;
    @DbcField(column = 12, dataType = DbcDataTypes.STRING_REFERENCE)
    private String displayName;
    @DbcField(column = 13, dataType = DbcDataTypes.STRING_REFERENCE)
    private String verboseName;

    public ItemSubClass() {
    }

    public ItemSubClass(int id, int classId, int subClassId, int prerequisiteProficiency, int postrequisiteProficiency, int flags, int displayFlags, int weaponParrySeq, int
            weaponReadySeq, int weaponAttackSeq, int weaponSwingSize, String displayName, String verboseName) {
        this.id = id;
        this.classId = classId;
        this.subClassId = subClassId;
        this.prerequisiteProficiency = prerequisiteProficiency;
        this.postrequisiteProficiency = postrequisiteProficiency;
        this.flags = flags;
        this.displayFlags = displayFlags;
        this.weaponParrySeq = weaponParrySeq;
        this.weaponReadySeq = weaponReadySeq;
        this.weaponAttackSeq = weaponAttackSeq;
        this.weaponSwingSize = weaponSwingSize;
        this.displayName = displayName;
        this.verboseName = verboseName;
    }

    public int getId() {
        return id;
    }

    public int getClassId() {
        return classId;
    }

    public int getSubClassId() {
        return subClassId;
    }

    public int getPrerequisiteProficiency() {
        return prerequisiteProficiency;
    }

    public int getPostrequisiteProficiency() {
        return postrequisiteProficiency;
    }

    public int getFlags() {
        return flags;
    }

    public int getDisplayFlags() {
        return displayFlags;
    }

    public int getWeaponParrySeq() {
        return weaponParrySeq;
    }

    public int getWeaponReadySeq() {
        return weaponReadySeq;
    }

    public int getWeaponAttackSeq() {
        return weaponAttackSeq;
    }

    public int getWeaponSwingSize() {
        return weaponSwingSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getVerboseName() {
        return verboseName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
