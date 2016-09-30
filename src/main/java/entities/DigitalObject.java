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
import models.ProcessModel;
import models.CoreModel;
import models.InfrastructureModel;
import models.ScenarioModel;

/**
 * A Digital Object represents any item that is available digitally.
 * It can be an aggregation of other digital objects.
 */
public class DigitalObject extends EcosystemEntity {
    public DigitalObject(ScenarioModel model, String name) {
        super(model, name, CoreModel.digitalObject);
    }

    public DigitalObject(ScenarioModel model, String name, Template template) {
        super(model, name, template);
    }

    public void accessedVia(ServiceInterface SInterface) {
        addProperty(InfrastructureModel.accessedVia, SInterface);
    }

    public void hasAuthor(HumanAgent author) {
        addProperty(ProcessModel.hasAuthor, author);
    }

    public void hasPart(DigitalObject digitalObject) {
        digitalObject.addProperty(LRM_static_schema.partOf, this);
        addProperty(LRM_static_schema.hasPart, digitalObject);
    }

    public void hasPath(String path) {
        addProperty(LRM_static_schema.url, model.model.createIndividual(LRM_static_schema.Location)
                .addProperty(LRM_static_schema.definition, path));
    }

    public void setChecksum(String checksum) {
        addProperty(CoreModel.checksum, checksum);
    }

    public void partOf(DigitalObject digitalObject) {
        digitalObject.hasPart(this);
    }

    public void partOf(TechnicalService technicalService) {
        technicalService.hasPart(this);
    }

    public void derivedFrom(DigitalObject digitalObject) {
        addProperty(CoreModel.derivedFromObject, digitalObject);
    }

    public static class DigitalObjectTemplate extends Template {
        public DigitalObjectTemplate(CoreModel coreModel) {
            super(coreModel, "Digital Object", CoreModel.ecosystemEntity);
            addDescription("A Digital Object represents any item that is available digitally. "
                    + "It can be an aggregation of other digital objects.");
            addSuperClass(LRM_static_schema.DigitalResource);
            addSuperClass(LRM_static_schema.AggregatedResource);
        }

        public DigitalObjectTemplate(ScenarioModel scenario, String id, String comment) {
            super(scenario, id, CoreModel.digitalObject);
            addDescription(comment);
            addSuperClass(LRM_static_schema.DigitalResource);
            addSuperClass(LRM_static_schema.AggregatedResource);
        }

        @Override
        public DigitalObject createEntity(ScenarioModel model, String ID) {
            return new DigitalObject(model, ID);
        }
    }
}
