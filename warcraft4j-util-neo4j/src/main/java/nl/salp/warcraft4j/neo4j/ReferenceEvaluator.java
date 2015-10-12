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
package nl.salp.warcraft4j.neo4j;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;

import java.util.function.Predicate;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class ReferenceEvaluator implements Evaluator {
    private final int maxDegree;
    private final Predicate<Integer> degreePredicate;
    private final Label label;

    public ReferenceEvaluator(Label label) {
        this(0, null, label);
    }

    public ReferenceEvaluator(int maxDegree, Label label) {
        this(maxDegree, null, label);
    }

    public ReferenceEvaluator(Predicate<Integer> degreePredicate, Label label) {
        this(0, degreePredicate, label);
    }

    public ReferenceEvaluator(int maxDegree, Predicate<Integer> degreePredicate, Label label) {
        this.maxDegree = maxDegree;
        this.degreePredicate = degreePredicate;
        this.label = label;
    }

    @Override
    public Evaluation evaluate(Path path) {
        Evaluation eval;

        if (maxDegree != 0 && path.length() > maxDegree) {
            eval = Evaluation.EXCLUDE_AND_PRUNE;
        } else if ((degreePredicate == null || degreePredicate.test(path.length())) && path.endNode().hasLabel(label)) {
            eval = Evaluation.INCLUDE_AND_CONTINUE;
        } else {
            eval = Evaluation.EXCLUDE_AND_CONTINUE;
        }
        return eval;
    }
}
