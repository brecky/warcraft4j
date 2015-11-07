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
package nl.salp.warcraft4j.dev.casc.dbc;

import nl.salp.warcraft4j.casc.cdn.CdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.cdn.DataReaderProvider;
import nl.salp.warcraft4j.casc.cdn.local.FileDataReaderProvider;
import nl.salp.warcraft4j.casc.cdn.local.LocalCdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.local.LocalCdnCascContext;
import nl.salp.warcraft4j.fileformat.dbc.DbcFile;
import nl.salp.warcraft4j.fileformat.dbc.DbcHeader;
import nl.salp.warcraft4j.fileformat.dbc.DbcStringTable;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.util.DataTypeUtil;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcPrinter {
    private final DevToolsConfig config;
    private final CdnCascContext cascContext;
    private final CdnCascConfig cdnCascConfig;
    private final DataReaderProvider dataReaderProvider;

    public DbcPrinter(DevToolsConfig config) throws Exception {
        this.config = config;
        this.dataReaderProvider = new FileDataReaderProvider();
        this.cdnCascConfig = new LocalCdnCascConfig(this.config, this.dataReaderProvider);
        this.cascContext = new LocalCdnCascContext(config);
    }

    public void print(String filename) {
        if (cascContext.isRegisteredData(filename)) {
            Supplier<DataReader> dataReader = () -> cascContext.getFileDataReader(filename);
            long hashcode = cascContext.getHash(filename).orElseThrow(() -> new IllegalArgumentException("No hashcode found for filename " + filename));
            DbcFile dbcFile = new DbcFile(hashcode, filename, dataReader);
            printSummary(dbcFile);
        } else if (cascContext.isRegistered(filename)) {
            System.out.println("No data available in CASC for DBC file " + filename);
        } else {
            System.out.println("Unknown file " + filename);
        }
    }

    public void printSummary(DbcFile dbcfile) {
        System.out.println(format("DbcFile (filename: %s, hash: %d) [", dbcfile.getFilename(), dbcfile.getFilenameHash()));

        DbcHeader header = dbcfile.getHeader();
        System.out.println(format("    magic string: %s", header.getMagicString()));
        System.out.println(format("    header size : %d bytes", header.getHeaderSize()));
        if (header.isDbcVersion2Supported()) {
            System.out.println(format("    build number: %d", header.getBuildNumber()));
            System.out.println(format("    timestamp   : %d", header.getTimestampLastWritten()));
        }
        if (header.isExtenededDbcVersion2Supported()) {
            System.out.println(format("    locale      : %d", header.getLocale()));
            System.out.println(format("    unknown[4]  : %s", DataTypeUtil.byteArrayToHexString(header.getUnknown())));
        }
        System.out.println(format("    entry       : ["));
        System.out.println(format("        count : %d", header.getEntryCount()));
        System.out.println(format("        fields: %d", header.getEntryFieldCount()));
        System.out.println(format("        size  : %d bytes", header.getEntrySize()));
        if (header.isExtenededDbcVersion2Supported()) {
            System.out.println(format("        min id: %d", header.getMinimumEntryId()));
            System.out.println(format("        max id: %d", header.getMaximumEntryId()));
        }
        System.out.println(format("        block : ["));
        System.out.println(format("            size  : %d bytes", header.getEntryBlockSize()));
        System.out.println(format("            offset: %d", header.getEntryBlockStartingOffset()));
        System.out.println(format("        ]"));
        System.out.println(format("    ]"));
        DbcStringTable stringTable = dbcfile.getStringTable();
        if (stringTable.getNumberOfEntries() > 0) {
            System.out.println(format("    stringTable : ["));
            System.out.println(format("        entries : %d", stringTable.getNumberOfEntries()));
            if (header.isDbcVersion2Supported()) {
                System.out.println(format("        hashcode: %s", DataTypeUtil.byteArrayToHexString(DataTypeUtil.toByteArray(header.getStringTableBlockHash()))));
            }
            if (header.isDbcVersion1Supported()) {
                System.out.println(format("        block   : ["));
                System.out.println(format("            size  : %d bytes", header.getStringTableBlockSize()));
                System.out.println(format("            offset: %d", header.getStringTableStartingOffset()));
                System.out.println(format("        ]"));
            }
        }
        System.out.println(format("    ]"));
        System.out.println(format("]"));
    }

    public static void main(String... args) throws Exception {
        DbcPrinter printer = new DbcPrinter(DevToolsConfig.fromFile("w4j_devtools.config"));
        String[] files = new String[] {
                "DBFilesClient\\ArmorLocation.dbc",
                "DBFilesClient\\AttackAnimKits.dbc",
                "DBFilesClient\\AttackAnimTypes.dbc",
                "DBFilesClient\\BankBagSlotPrices.dbc",
                "DBFilesClient\\BannedAddOns.dbc",
                "DBFilesClient\\BattlePetAbilityEffect.db2",
                "DBFilesClient\\BattlePetAbilityState.db2",
                "DBFilesClient\\BattlePetAbilityTurn.db2",
                "DBFilesClient\\BattlePetBreedQuality.db2",
                "DBFilesClient\\BattlePetBreedState.db2",
                "DBFilesClient\\BattlePetEffectProperties.db2",
                "DBFilesClient\\BattlePetSpeciesState.db2",
                "DBFilesClient\\BattlePetSpeciesXAbility.db2"
        };
        Stream.of(files).forEach(printer::print);
    }
}
