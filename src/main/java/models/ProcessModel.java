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

import entities.AggregatedProcess.AggregatedProcessTemplate;
import entities.AtomicProcess;
import entities.EcosystemEvent.EventTemplate;
import entities.HumanActivity.HumanActivityTemplate;
import entities.HumanAgent.HumanAgentTemplate;
import entities.Implementation;
import entities.ProcessForEntityValidation.ProcessForEntityValidationTemplate;
import entities.ProcessForModelValidation.ProcessForModelValidationTemplate;
import entities.ProcessForTransformation.ProcessForTransformationTemplate;
import entities.Role;
import entities.Slot;
import relations.DEMRelation;
import relations.RelationBuilder;

public class ProcessModel extends AbstractModel {
    /**
     * The abstract ecosystem templates. These are the abstract entity "classes"
     * of the ontology, which serve to be templates for the creation of real
     * existing "instances". See also the {@link entities} package.
     */
    public static EventTemplate ecosystemEvent;
    public static AggregatedProcessTemplate aggregatedProcess;
    public static AtomicProcess.AtomicProcessTemplate atomicProcess;
    public static HumanAgentTemplate humanAgent;
    public static HumanActivityTemplate humanActivity;
    public static ProcessForEntityValidationTemplate processForEntityValidation;
    public static ProcessForModelValidationTemplate processForModelValidation;
    public static ProcessForTransformationTemplate processForTransformation;
    public static Role.RoleTemplate role;
    public static Implementation.ImplementationTemplate implementation;
    public static Slot.SlotTemplate slot; //TODO: distinguish between input and output slots

    public static DEMRelation responsible;
    public static DEMRelation hasAuthor;
    public static DEMRelation hasRole;
    public static DEMRelation hasAutomationState;
    public static DEMRelation hasImplementation;
    public static DEMRelation hasMimeType;
    public static DEMRelation hasInputSlot;
    public static DEMRelation hasOutputSlot;
    public static DEMRelation passesOutputTo;
    public static DEMRelation receivesInputFrom;
    public static DEMRelation slotType;
    public static DEMRelation processFlow;
    public static DEMRelation dataFlow;
    public static DEMRelation personOwns;
    public static DEMRelation ownedByPerson;
    public static DEMRelation isOptional;
    public static DEMRelation implementationType;

    public ProcessModel() {
        super("DEM-Process", "The process model provides entities for a more detailed " +
                "process modelling. The basic principles for process modelling are imported from the " +
                "Linked Resource Model.");
    }

    @Override
    public void createModelEntities() {
        ecosystemEvent = new EventTemplate(this);
        aggregatedProcess = new AggregatedProcessTemplate(this);
        atomicProcess = new AtomicProcess.AtomicProcessTemplate(this);
        humanAgent = new HumanAgentTemplate(this);
        humanActivity = new HumanActivityTemplate(this);
        processForEntityValidation = new ProcessForEntityValidationTemplate(this);
        processForModelValidation = new ProcessForModelValidationTemplate(this);
        processForTransformation = new ProcessForTransformationTemplate(this);
        role = new Role.RoleTemplate(this);
        implementation = new Implementation.ImplementationTemplate(this);
        slot = new Slot.SlotTemplate(this);
    }

    @Override
    public void createModelRelations() {
        responsible = new RelationBuilder(this, "responsible", CoreModel.policy)
                .comment("Refers to the responsible person for the application of the policy.")
                .range(humanAgent).create();
        hasAuthor = new RelationBuilder(this, "hasAuthor", CoreModel.digitalObject).comment("Defines the author of the Digital Object.")
                .range(humanAgent).create();
        hasRole = new RelationBuilder(this, "hasRole", CoreModel.community)
                .comment("Assigns a role to a community or to an agent.")
                .domain(CoreModel.ecosystemAgent).range(role).create();
        hasAutomationState = new RelationBuilder(this, "hasAutomationState", CoreModel.process)
                .comment("Links an Automation State to a Process.").create();
        hasImplementation = new RelationBuilder(this, "hasImplementation", CoreModel.process)
                .comment("Links an implementation entity to a Process.")
                .range(implementation).create();
        hasMimeType = new RelationBuilder(this, "hasMimeType", implementation)
                .comment("Defines the mime type of an Implementation.").create();
        hasInputSlot = new RelationBuilder(this, "hasInputSlot", slot).comment("Specifies an output slot of a process.").create();
        hasOutputSlot = new RelationBuilder(this, "hasOutputSlot", slot).comment("Specifies an input slot of a process.").create();
        slotType = new RelationBuilder(this, "slotType", slot)
                .comment("The entity template / type which instances can be passed to this process slot")
                .create();
        processFlow = new RelationBuilder(this, "processFlow", aggregatedProcess)
                .comment("Order of execution of sub-processes of an aggregated process")
                .create();
        dataFlow = new RelationBuilder(this, "dataFlow", aggregatedProcess)
                .comment("Data connections between sub-processes in an aggregated process.")
                .create();
        passesOutputTo = new RelationBuilder(this, "passesOutputTo", slot)
                .comment("The output of a typed process output slot is passed to a process input slot of the same type.")
                .range(slot).create();
        receivesInputFrom = new RelationBuilder(this, "receivesInputFrom", slot)
                .comment("The typed process input slot receives the output from an output slot of the same type.")
                .range(slot).create();
        ownedByPerson = new RelationBuilder(this, "ownedByPerson", CoreModel.ecosystemEntity)
                .comment("A human agent can own ecosystem entities.")
                .range(ProcessModel.humanAgent).create();
        personOwns = new RelationBuilder(this, "personOwns", humanAgent)
                .comment("A human agent can own ecosystem entities.")
                .range(CoreModel.ecosystemEntity).inverse(ProcessModel.ownedByPerson).create();
        isOptional = new RelationBuilder(this, "isOptional", slot)
                .comment("Input slots can be optional, whereas output slots are always mandatory.")
                .create(); //TODO: Just for input slots
        implementationType = new RelationBuilder(this, "implementationType", implementation)
                .comment("The type format of the implementation, e.g. BPMN.").create();
    }
}
