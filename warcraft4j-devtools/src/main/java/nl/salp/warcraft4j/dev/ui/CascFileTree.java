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

import nl.salp.warcraft4j.casc.CascService;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CascFileTree implements TreeSelectionListener {
    private final ExecutionContext context;
    private final CascFileTreeModel treeModel;
    private final JTree tree;

    public CascFileTree(ExecutionContext context) {
        this.context = context;
        treeModel = new CascFileTreeModel(context.getCascService());
        tree = new JTree(treeModel);
        tree.addTreeSelectionListener(this);
    }

    public void update(CascService service) {
        if (service != null) {
            treeModel.update(service);
        }
    }

    public JComponent getComponent() {
        return tree;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String oldPath = Optional.ofNullable(e.getOldLeadSelectionPath())
                .map(TreePath::getPath)
                .map(Stream::of)
                .map(s -> s.map(Object::toString)
                        .collect(Collectors.joining("\\")))
                .orElse("<none>");
        String newPath = Optional.ofNullable(e.getPath())
                .map(TreePath::getPath)
                .map(Stream::of)
                .map(s -> s.map(Object::toString)
                        .collect(Collectors.joining("\\")))
                .orElse("<none>");

        context.getLogger().debug("Changed selection from {} to {}", oldPath, newPath);
    }
}
