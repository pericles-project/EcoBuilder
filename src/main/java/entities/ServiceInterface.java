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

import models.InfrastructureModel;
import models.ScenarioModel;

/**
 * The interface of a technical system. Ecosystem agents use a service interface to access a technical system.
 */
public class ServiceInterface extends InfrastructureComponent {

    public ServiceInterface(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, InfrastructureModel.serviceInterface);
    }

    public void isUsedBy(Community community) {
        addProperty(InfrastructureModel.isUsedBy, community);
    }

    public void isUsedBy(EcosystemAgent agent) {
        addProperty(InfrastructureModel.isUsedBy, agent);
    }

    public void providesAccessTo(DigitalObject digitalObject) {
        addProperty(InfrastructureModel.providesAccessTo, digitalObject);
    }

    public void providesAccessTo(TechnicalService technicalService) {
        addProperty(InfrastructureModel.providesAccessTo, technicalService);
    }

    public void providesAccessTo(Process process) {
        addProperty(InfrastructureModel.providesAccessTo, process);
    }

    public void providesAccessTo(InfrastructureComponent component) {
        component.accessedVia(this);
    }

    public void providesAccessTo(AutomaticAgent component) {
        addProperty(InfrastructureModel.providesAccessTo, component);
    }

    public static class ServiceInterfaceTemplate extends InfrastructureComponentTemplate {
        public ServiceInterfaceTemplate(InfrastructureModel refinementsModel) {
            super(refinementsModel, "Service Interface",
                    "The interface of a technical system. Ecosystem agents use "
                            + "a service interface to access a technical system.", InfrastructureModel.infrastructureComponent);
        }

        @Override
        public ServiceInterface createEntity(ScenarioModel model, String ID) {
            return new ServiceInterface(model, ID);
        }
    }
}
