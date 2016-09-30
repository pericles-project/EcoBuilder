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
import models.CoreModel;
import models.InfrastructureModel;
import models.ScenarioModel;

/**
 * A Technical Service is a infrastructure consisting of hard- and software components and service interfaces. A
 * Technical Service provides services for the ecosystem, which can be accessed by its service interfaces. A technical
 * service consists of infrastructure components, digital objects, processes, and other technical services.
 */
public class TechnicalService extends EcosystemEntity {

    public TechnicalService(ScenarioModel model, String identifier, Template template) {
        super(model, identifier, template);
    }

    public TechnicalService(ScenarioModel model, String identifier) {
        super(model, identifier, CoreModel.technicalService);
    }

    public void accessedVia(ServiceInterface SInterface) {
        addProperty(InfrastructureModel.accessedVia, SInterface);
    }

    public void hasPart(DigitalObject digitalObject) {
        hasPart((EcosystemEntity) digitalObject);
    }

    public void hasPart(Process process) {
        hasPart((EcosystemEntity) process);
    }

    public void hasPart(TechnicalService service) {
        hasPart((EcosystemEntity) service);
    }

    public void hasPart(InfrastructureComponent component) {
        hasPart((EcosystemEntity) component);
    }

    private void hasPart(EcosystemEntity entity) {
        entity.addProperty(LRM_static_schema.partOf, this);
        addProperty(LRM_static_schema.hasPart, entity);
    }

    public void hasPart(AutomaticAgent agent) {
        agent.addProperty(LRM_static_schema.partOf, this);
        addProperty(LRM_static_schema.hasPart, agent);
    }

    public void partOf(TechnicalService service) {
        service.hasPart(this);
    }

    public static class TechnicalServiceTemplate extends Template {
        public TechnicalServiceTemplate(CoreModel coreModel) {
            super(coreModel, "Technical Service", CoreModel.ecosystemEntity);
            addDescription("A Technical Service is a infrastructure consisting of "
                    + "hard- and software components and service interfaces. A "
                    + "Technical Service provides services for the ecosystem, "
                    + "which can be accessed by its service interfaces. A technical "
                    + "service consists of infrastructure components, digital objects, "
                    + "processes, and other technical services.");
            addSuperClass(LRM_static_schema.AggregatedResource);
        }

        public TechnicalServiceTemplate(ScenarioModel scenario, String name, String comment) {
            super(scenario, name, CoreModel.technicalService);
            addDescription(comment);
            addSuperClass(LRM_static_schema.AggregatedResource);
        }

        @Override
        public TechnicalService createEntity(ScenarioModel model, String ID) {
            return new TechnicalService(model, ID);
        }
    }

}
