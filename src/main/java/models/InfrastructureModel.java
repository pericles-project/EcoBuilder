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
package models;

import entities.AutomaticAgent;
import entities.AutomatisedActivity;
import entities.HardwareAgent;
import entities.InfrastructureComponent.InfrastructureComponentTemplate;
import entities.ServiceInterface.ServiceInterfaceTemplate;
import entities.SoftwareAgent;
import relations.DEMRelation;
import relations.RelationBuilder;

public class InfrastructureModel extends AbstractModel {
    /**
     * The abstract ecosystem templates. These are the abstract entity "classes"
     * of the ontology, which serve to be templates for the creation of real
     * existing "instances". See also the {@link entities} package.
     */
    public static InfrastructureComponentTemplate infrastructureComponent;
    public static ServiceInterfaceTemplate serviceInterface;
    public static AutomaticAgent.AutomaticAgentTemplate automaticAgent;
    public static HardwareAgent.HardwareAgentTemplate hardwareAgent;
    public static SoftwareAgent.SoftwareAgentTemplate softwareAgent;
    public static AutomatisedActivity.AutomatisedActivityTemplate automatisedActivity;

    public static DEMRelation isUsedBy;
    public static DEMRelation providesAccessTo;
    public static DEMRelation accessedVia;
    public static DEMRelation runsOn;

    public InfrastructureModel() {
        super("DEM-Infrastructure",
                "The infrastructure model contains a collection of entities for " +
                        "modelling the digital ecosystem infrastructure.");
    }

    @Override
    public void createModelEntities() {
        infrastructureComponent = new InfrastructureComponentTemplate(this);
        serviceInterface = new ServiceInterfaceTemplate(this);
        automaticAgent = new AutomaticAgent.AutomaticAgentTemplate(this);
        hardwareAgent = new HardwareAgent.HardwareAgentTemplate(this);
        softwareAgent = new SoftwareAgent.SoftwareAgentTemplate(this);
        automatisedActivity = new AutomatisedActivity.AutomatisedActivityTemplate(this);
    }

    @Override
    public void createModelRelations() {
        isUsedBy = new RelationBuilder(this, "isUsedBy")
                .comment("This relation can be used to express that an agent uses an interface to access technical " +
                        "services, as well as that a community uses such an interface.")
                .domain(serviceInterface).range(CoreModel.ecosystemAgent).create();
        providesAccessTo = new RelationBuilder(this, "providesAccessTo")
                .comment("The service interface provides access to digital objects, technical services, processes, " +
                        "or infrastructure components as hardware- or softwareagents.")
                .domain(serviceInterface).range(CoreModel.digitalObject).range(CoreModel.technicalService)
                .range(CoreModel.process).range(infrastructureComponent).create();
        accessedVia = new RelationBuilder(this, "accessedVia")
                .comment("This component is accessed via a service interface.").domain(CoreModel.digitalObject)
                .domain(CoreModel.process).domain(CoreModel.technicalService).domain(infrastructureComponent)
                .range(serviceInterface).inverse(providesAccessTo).create();
        runsOn = new RelationBuilder(this, "runsOn").comment("A software or hardware agent runs on a server.")
                .domain(automaticAgent).range(CoreModel.technicalService).create();
    }
}
