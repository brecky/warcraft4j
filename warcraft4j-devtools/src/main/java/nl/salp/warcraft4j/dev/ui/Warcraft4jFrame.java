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

import javax.swing.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Warcraft4jFrame {
    private final ExecutionContext context;
    private final JFrame frame;

    public Warcraft4jFrame(ExecutionContext context) {
        this.context = context;
        frame = new JFrame("Warcraft4J Development Tools");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(new JScrollPane(new CascFileTree(context).getComponent()));

        frame.pack();
        frame.setVisible(true);
    }
}
