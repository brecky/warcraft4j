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

package nl.salp.warcraft4j.battlenet.api.wow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class HunterPetDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("creature")
    private int creature;
    @JsonProperty("selected")
    private boolean selected;
    @JsonProperty("slot")
    private int slot;
    @JsonProperty("spec")
    private HunterPetSpecDTO spec;
    @JsonProperty("calcSpec")
    private String calcSpec;
    @JsonProperty("familyId")
    private int familyId;
    @JsonProperty("familyName")
    private String familyName;

    public String getName() {
        return name;
    }

    public int getCreature() {
        return creature;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getSlot() {
        return slot;
    }

    public HunterPetSpecDTO getSpec() {
        return spec;
    }

    public String getCalcSpec() {
        return calcSpec;
    }

    public int getFamilyId() {
        return familyId;
    }

    public String getFamilyName() {
        return familyName;
    }
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
