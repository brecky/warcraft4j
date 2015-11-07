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
import nl.salp.warcraft4j.casc.CascService;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

import static nl.salp.warcraft4j.util.DataTypeUtil.byteArrayToHexString;
import static nl.salp.warcraft4j.util.DataTypeUtil.toByteArray;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CascFileTreeModel implements TreeModel {
    private final Set<TreeModelListener> modelListeners;
    private CascFileTreeNode root;

    public CascFileTreeModel(CascService cascService) {
        this.modelListeners = new HashSet<>();
        update(cascService);
    }

    public void update(CascService cascService) {
        this.root = new CascFileTreeNode("/");
        cascService.getAllCascFiles().forEach(this::add);
        modelListeners.forEach(l -> l.treeStructureChanged(new TreeModelEvent(this, new TreePath(root))));
    }

    private void add(CascFile file) {
        String fullFilename = file.getFilename()
                .orElse("<unknown>\\" + byteArrayToHexString(toByteArray(file.getFilenameHash())));
        CascFileTreeNode folder = root;

        StringTokenizer tokenizer = new StringTokenizer(fullFilename, "\\", false);
        while (tokenizer.hasMoreTokens()) {
            String element = tokenizer.nextToken();
            if (isNotEmpty(element)) {
                if (tokenizer.hasMoreTokens()) {
                    folder = folder.addChild(element)
                            .orElse(folder);
                } else {
                    folder = folder.addChild(file)
                            .orElse(folder);
                }
            }
        }
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return toTreeNode(parent)
                .map(n -> n.getChildAt(index))
                .orElse(null);
    }

    @Override
    public int getChildCount(Object parent) {
        return toTreeNode(parent)
                .map(CascFileTreeNode::getChildCount)
                .orElse(0);
    }

    @Override
    public boolean isLeaf(Object node) {
        return toTreeNode(node)
                .map(CascFileTreeNode::isFile)
                .orElse(false);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // Ignore.
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return toTreeNode(parent)
                .map(n -> n.getIndex(toTreeNode(child).orElse(null)))
                .orElse(-1);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        if (l != null) {
            modelListeners.add(l);
        }
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        if (l != null) {
            modelListeners.remove(l);
        }
    }

    private Optional<CascFileTreeNode> toTreeNode(Object node) {
        return Optional.ofNullable(node)
                .filter(n -> CascFileTreeNode.class.isAssignableFrom(n.getClass()))
                .map(CascFileTreeNode.class::cast);
    }
}
