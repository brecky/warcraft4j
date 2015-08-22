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
package nl.salp.warcraft4j.dev.casc.neo4j.model;

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.DataTypeUtil;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.data.casc.ContentChecksum;
import nl.salp.warcraft4j.data.casc.FileKey;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
abstract class Neo4jCascEntry extends Neo4jEntry {
    protected Neo4jCascEntry(Node node, Label label) throws IllegalArgumentException {
        super(node, label);
    }

    protected final Optional<FileKey> getFileKey(CascProperty property) {
        return node()
                .filter(n -> n.hasProperty(property.getName()))
                .map(n -> (String) n.getProperty(property.getName()))
                .map(k -> k.substring(0, 18))
                .map(DataTypeUtil::hexStringToByteArray)
                .map(FileKey::new);
    }

    protected final void setFileKey(CascProperty property, FileKey fileKey) {
        Optional<FileKey> fk = Optional.ofNullable(fileKey);
        fk.filter(f -> f.length() > 0)
                .map(FileKey::toHexString)
                .ifPresent(f -> getNode().setProperty(property.getName(), f));
        if (!fk.isPresent()) {
            getNode().removeProperty(property.getName());
        }
    }

    protected final Optional<ContentChecksum> getContentChecksum(CascProperty property) {
        return getByteArray(property)
                .map(ContentChecksum::new);
    }

    protected final void setContentChecksum(CascProperty property, ContentChecksum checksum) {
        Optional<ContentChecksum> cs = Optional.ofNullable(checksum);
        cs.filter(c -> c.length() > 0)
                .map(ContentChecksum::toHexString)
                .ifPresent(c -> getNode().setProperty(property.getName(), c));
        if (!cs.isPresent()) {
            remove(property);
        }
    }

    public final Optional<String> getWowVersion() {
        return getString(CascProperty.WOW_VERSION);
    }

    protected final void setWowVersion(String version) {
        setString(CascProperty.WOW_VERSION, version);
    }

    public final Optional<Branch> getWowBranch() {
        return node()
                .filter(n -> n.hasProperty(CascProperty.WOW_BRANCH.getName()))
                .map(n -> (String) n.getProperty(CascProperty.WOW_BRANCH.getName()))
                .flatMap(Branch::getBranch);
    }

    protected final void setWowBranch(Branch branch) {
        if (branch == null) {
            remove(CascProperty.WOW_BRANCH);
        } else {
            getNode().setProperty(CascProperty.WOW_BRANCH.getName(), branch.name());
        }
    }

    public final Optional<Region> getWowRegion() {
        return node()
                .filter(n -> n.hasProperty(CascProperty.WOW_REGION.getName()))
                .map(n -> (String) n.getProperty(CascProperty.WOW_REGION.getName()))
                .flatMap(Region::getRegion);
    }

    protected final void setWowRegion(Region region) {
        if (region == null) {
            remove(CascProperty.WOW_REGION);
        } else {
            getNode().setProperty(CascProperty.WOW_REGION.getName(), region.name());
        }
    }

    public final Optional<Locale> getWowLocale() {
        return node()
                .filter(n -> n.hasProperty(CascProperty.WOW_LOCALE.getName()))
                .map(n -> (String) n.getProperty(CascProperty.WOW_LOCALE.getName()))
                .flatMap(Locale::getLocale);
    }

    protected final void setWowLocale(Locale locale) {
        if (locale == null) {
            remove(CascProperty.WOW_LOCALE);
        } else {
            getNode().setProperty(CascProperty.WOW_LOCALE.getName(), locale.name());
        }
    }
}
