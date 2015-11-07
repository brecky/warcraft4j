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
package nl.salp.warcraft4j.dev.ui;

import nl.salp.warcraft4j.casc.CascFile;
import org.apache.commons.collections4.iterators.IteratorEnumeration;

import javax.swing.tree.TreeNode;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.Collectors;

import static nl.salp.warcraft4j.util.DataTypeUtil.byteArrayToHexString;
import static nl.salp.warcraft4j.util.DataTypeUtil.toByteArray;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CascFileTreeNode implements TreeNode, Comparable<CascFileTreeNode> {
    private final String name;
    private final CascFile file;
    private final List<CascFileTreeNode> children;
    private final WeakReference<CascFileTreeNode> parent;

    public CascFileTreeNode(String name) {
        this(name, null, null);
    }

    public CascFileTreeNode(CascFile file) {
        this(null, file, null);
    }

    public CascFileTreeNode(String name, CascFileTreeNode parent) {
        this(name, null, parent);
    }

    public CascFileTreeNode(CascFile file, CascFileTreeNode parent) {
        this(null, file, parent);
    }

    public CascFileTreeNode(String name, CascFile file, CascFileTreeNode parent) {
        if (isEmpty(name) && file == null) {
            throw new IllegalArgumentException("Unable to create a CascFileTreeNode with both a null name and file.");
        }
        this.name = Optional.ofNullable(name).orElse(getFilename(file));
        this.file = file;
        this.children = new ArrayList<>();
        this.parent = new WeakReference<>(parent);
    }

    public String getName() {
        return name;
    }

    public boolean isFolder() {
        return file == null;
    }

    public boolean isFile() {
        return file != null;
    }

    public Optional<CascFile> getFile() {
        return Optional.ofNullable(file);
    }

    public List<CascFileTreeNode> getAllChildren() {
        return Collections.unmodifiableList(children);
    }

    public Optional<CascFileTreeNode> getChild(String name) {
        Optional<CascFileTreeNode> entry;
        if (isEmpty(name) || isFile()) {
            entry = Optional.empty();
        } else {
            entry = children.stream()
                    .filter(c -> name.equalsIgnoreCase(c.getName()))
                    .findAny();
        }
        return entry;
    }

    public List<CascFileTreeNode> getChildFolders() {
        return children.stream()
                .filter(CascFileTreeNode::isFolder)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<CascFileTreeNode> getChildFiles() {
        return children.stream()
                .filter(CascFileTreeNode::isFile)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Optional<CascFileTreeNode> addChild(CascFile file) {
        Optional<CascFileTreeNode> entry;
        if (file != null && isFolder()) {
            entry = getChild(getFilename(file));
            if (!entry.isPresent()) {
                CascFileTreeNode newEntry = new CascFileTreeNode(file, this);
                if (children.add(newEntry)) {
                    entry = Optional.of(newEntry);
                } else {
                    entry = Optional.empty();
                }
            }
        } else {
            entry = Optional.empty();
        }
        return entry;
    }

    public Optional<CascFileTreeNode> addChild(String folder) {
        Optional<CascFileTreeNode> entry;
        if (isNotEmpty(folder) && isFolder()) {
            entry = getChild(folder);
            if (!entry.isPresent()) {
                CascFileTreeNode newEntry = new CascFileTreeNode(folder, this);
                if (children.add(newEntry)) {
                    entry = Optional.of(newEntry);
                } else {
                    entry = Optional.empty();
                }
            }
        } else {
            entry = Optional.empty();
        }
        return entry;
    }

    private static String getFilename(CascFile file) {
        return Optional.ofNullable(file)
                .map(CascFile::getFilename)
                .map(name -> name.orElse(byteArrayToHexString(toByteArray(file.getFilenameHash()))))
                .orElse(null);
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return Optional.of(childIndex)
                .filter(i -> i >= 0)
                .filter(i -> i < children.size())
                .map(children::get)
                .orElse(null);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent.get();
    }

    @Override
    public int getIndex(TreeNode node) {
        return Optional.ofNullable(node)
                .map(children::indexOf)
                .orElse(-1);
    }

    @Override
    public boolean getAllowsChildren() {
        return isFolder();
    }

    @Override
    public boolean isLeaf() {
        return isFile();
    }

    @Override
    public Enumeration children() {
        return new IteratorEnumeration<>(children.iterator());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && CascFileTreeNode.class.isAssignableFrom(obj.getClass()) && getName().equals(((CascFileTreeNode) obj).getName());
    }

    @Override
    public int compareTo(CascFileTreeNode other) {
        int cmp;
        if (other == null) {
            cmp = -1;
        } else if (isFile() && other.isFolder()) {
            cmp = 1;
        } else if (isFolder() && other.isFile()) {
            cmp = -1;
        } else {
            cmp = getName().compareTo(other.getName());
        }
        return cmp;
    }
}