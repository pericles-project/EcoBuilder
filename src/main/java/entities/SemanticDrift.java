/*
 * Copyright 2016 Anna Eggers - Göttingen State and University Library
 * The work has been developed in the PERICLES Project by Members of the PERICLES Consortium.
 * This project has received funding from the European Union’s Seventh Framework Programme for research, technological
 * development and demonstration under grant agreement no FP7- 601138 PERICLES.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at:   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including without
 * limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTIBITLY, or FITNESS FOR A PARTICULAR
 * PURPOSE. In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise,
 * unless required by applicable law or agreed to in writing, shall any Contributor be liable for damages, including
 * any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this
 * License or out of the use or inability to use the Work.
 * See the License for the specific language governing permissions and limitation under the License.
 */
package entities;

import LRMv2.LRM_static_schema;
import models.AnalysisModel;
import models.ScenarioModel;
import org.apache.jena.rdf.model.Resource;

/**
 * This weighted relation points from an Ecosystem Entity to an earlier version of the same Ecosystem Entity, or to
 * another Ecosystem Entity with huge semantic similarities. It is used to express the semantic drift between these two
 * entities.
 */
public class SemanticDrift extends WeightedRelation {

    public SemanticDrift(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, AnalysisModel.semanticDrift);
    }


    public SemanticDrift(ScenarioModel model, String identifier, Resource fromResource, Resource toResource) {
        super(model, identifier, AnalysisModel.semanticDrift);
        from(fromResource);
        to(toResource);
    }

    public static class SemanticDriftTemplate extends WeightedRelationTemplate {
        public SemanticDriftTemplate(AnalysisModel analysisModel) {
            super(analysisModel, "Semantic Drift",
                    "This weighted relation points from an Ecosystem Entity to an earlier version of the same " +
                            "Ecosystem Entity, or to another Ecosystem Entity with huge semantic similarities. " +
                            "It is used to express the semantic drift between these two entities.", AnalysisModel.weightedRelation);
            addSuperClass(LRM_static_schema.AbstractResource);
            addSuperClass(LRM_static_schema.Description);
        }

        @Override
        public SemanticDrift createEntity(ScenarioModel model, String ID) {
            return new SemanticDrift(model, ID);
        }
    }
}
