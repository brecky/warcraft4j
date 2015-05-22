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

package nl.salp.warcraft4j.clientdata.cdn;

import nl.salp.warcraft4j.Region;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * CDN host information that provides all hosts and the path for a region.
 *
 * @author Barre Dijkstra
 */
public class CdnHost {
    /** The region. */
    private Region region;
    /** The path. */
    private String path;
    /** The CDN hosts. */
    private String[] hosts;

    /**
     * Create a new CDN host information object.
     *
     * @param region The region the CDN information is for.
     * @param path   The base path on the host(s) for the CDN.
     * @param hosts  The host(s) of the CDN.
     */
    public CdnHost(Region region, String path, String... hosts) {
        if (region == null) {
            throw new IllegalArgumentException("Unable to create CDN host information with no region specified.");
        }
        if (isEmpty(path)) {
            throw new IllegalArgumentException("Unable to create CDN host information with no path specified.");
        }
        if (hosts == null || hosts.length == 0) {
            throw new IllegalArgumentException("Unable to create CDN host information with no host entries.");
        }
        this.region = region;
        this.path = path;
        this.hosts = hosts;
    }

    /**
     * Get the region the CDN is for.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Get the base path on the host(s) for the CDN.
     *
     * @return The path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Get all hosts that are listed for the region CDN.
     *
     * @return The hosts.
     */
    public String[] getHosts() {
        return hosts;
    }

    /**
     * Get the (first) host for the region CDN.
     *
     * @return The host.
     */
    public String getHost() {
        return hosts[0];
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
