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
import models.CoreModel;
import models.ScenarioModel;
import org.apache.jena.rdf.model.Resource;

/**
 * This entity corresponds to a weighted edge in the graph. It can also be used to weight entities by pointing
 * the \"from\" and the \"to\" to the same entity.
 */
public class WeightedRelation extends EcosystemEntity {

    public WeightedRelation(ScenarioModel scenario, String identifier, Template template) {
        super(scenario, identifier, template);
    }

    private WeightedRelation(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, AnalysisModel.weightedRelation);
    }

    public WeightedRelation(ScenarioModel model, String identifier, Resource fromResource, Resource toResource) {
        super(model, identifier, AnalysisModel.weightedRelation);
        from(fromResource);
        to(toResource);
    }

    public void from(Resource resource) {
        addProperty(LRM_static_schema.from_property, resource);
    }

    public void to(Resource resource) {
        addProperty(LRM_static_schema.to_property, resource);
    }

    public void hasAnnotation(Annotation annotation) {
        annotation.annotates(this);
    }

    public void hasValue(float value) {
        addProperty(AnalysisModel.hasValue, "" + value);
    }

    public static class WeightedRelationTemplate extends Template {
        public WeightedRelationTemplate(AnalysisModel analysisModel) {
            this(analysisModel, "Weighted Relation", "This entity corresponds to a weighted edge" +
                    " in the graph. It can also be used to weight entities by pointing the \"from\" and the \"to\" " +
                    "to the same entity.", CoreModel.ecosystemEntity);
        }

        public WeightedRelationTemplate(AnalysisModel analysisModel, String name, String comment, Template parent) {
            super(analysisModel, name, parent);
            addDescription(comment);
            addSuperClass(LRM_static_schema.Dependency);
        }

        @Override
        public WeightedRelation createEntity(ScenarioModel model, String ID) {
            return new WeightedRelation(model, ID);
        }
    }
}
