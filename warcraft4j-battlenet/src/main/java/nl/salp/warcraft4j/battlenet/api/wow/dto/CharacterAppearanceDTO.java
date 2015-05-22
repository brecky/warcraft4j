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
 * Character appearance DTO, which is an optional snippet of the {@link CharacterDTO}.
 * <p/>
 * <pre>
 * "appearance": {
 *     "faceVariation": int,
 *     "skinColor": int,
 *     "hairVariation": int,
 *     "hairColor": int,
 *     "featureVariation": int,
 *     "showHelm": boolean,
 *     "showCloak": boolean
 * }
 * </pre>
 *
 * @author Barre Dijkstra
 */
public class CharacterAppearanceDTO {
    @JsonProperty("faceVariation")
    private int faceVariation;
    @JsonProperty("skinColor")
    private int skinColor;
    @JsonProperty("hairVariation")
    private int hairVariation;
    @JsonProperty("hairColor")
    private int hairColor;
    @JsonProperty("featureVariation")
    private int featureVariation;
    @JsonProperty("showHelm")
    private boolean showHelm;
    @JsonProperty("showCloak")
    private boolean showCloak;

    public int getFaceVariation() {
        return faceVariation;
    }

    public void setFaceVariation(int faceVariation) {
        this.faceVariation = faceVariation;
    }

    public int getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(int skinColor) {
        this.skinColor = skinColor;
    }

    public int getHairVariation() {
        return hairVariation;
    }

    public void setHairVariation(int hairVariation) {
        this.hairVariation = hairVariation;
    }

    public int getHairColor() {
        return hairColor;
    }

    public void setHairColor(int hairColor) {
        this.hairColor = hairColor;
    }

    public int getFeatureVariation() {
        return featureVariation;
    }

    public void setFeatureVariation(int featureVariation) {
        this.featureVariation = featureVariation;
    }

    public boolean isShowHelm() {
        return showHelm;
    }

    public void setShowHelm(boolean showHelm) {
        this.showHelm = showHelm;
    }

    public boolean isShowCloak() {
        return showCloak;
    }

    public void setShowCloak(boolean showCloak) {
        this.showCloak = showCloak;
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
