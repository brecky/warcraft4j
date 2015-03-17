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

package nl.salp.warcraft4j;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Version implements Serializable, Comparable<Version> {
    private static final String DATE_MASK = "yyyy-MM-dd";
    private static final String VERSION_MASK = "Version %d.%d.%d-b%d [%s] (%s)";
    private final String branch;
    private final int majorVersion;
    private final int contentLevelVersion;
    private final int patchLevelVersion;
    private final int build;
    private final Date releaseDate;

    public Version(int majorVersion, int contentLevelVersion, int patchLevelVersion, int build, String branch, Date releaseDate) {
        this.majorVersion = majorVersion;
        this.contentLevelVersion = contentLevelVersion;
        this.patchLevelVersion = patchLevelVersion;
        this.build = build;
        this.releaseDate = releaseDate;
        this.branch = branch;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getContentLevelVersion() {
        return contentLevelVersion;
    }

    public int getPatchLevelVersion() {
        return patchLevelVersion;
    }

    public int getBuild() {
        return build;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public String toString() {
        String releaseDate = new SimpleDateFormat(DATE_MASK).format(this.releaseDate);
        return String.format(VERSION_MASK, majorVersion, contentLevelVersion, patchLevelVersion, build, branch, releaseDate);
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && Version.class.isAssignableFrom(obj.getClass())) {
            Version other = (Version) obj;
            eq = (majorVersion == other.majorVersion)
                    && (contentLevelVersion == other.contentLevelVersion)
                    && (patchLevelVersion == other.patchLevelVersion)
                    && (build == other.build);
        }
        return eq;
    }

    @Override
    public int compareTo(Version other) {
        int cmp;
        if (other == null) {
            cmp = 1;
        } else {
            if (majorVersion != other.majorVersion) {
                cmp = (majorVersion - other.majorVersion);
            } else if (contentLevelVersion != other.contentLevelVersion) {
                cmp = (contentLevelVersion - other.contentLevelVersion);
            } else if (patchLevelVersion != other.patchLevelVersion) {
                cmp = patchLevelVersion - other.patchLevelVersion;
            } else if (build != other.build) {
                cmp = build - other.build;
            } else {
                cmp = 0;
            }
        }
        return cmp;
    }
}
