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
    public static HumanAgentTemplate humanAgent;
    public static HumanActivityTemplate humanActivity;
    public static ProcessForEntityValidationTemplate processForEntityValidation;
    public static ProcessForModelValidationTemplate processForModelValidation;
    public static ProcessForTransformationTemplate processForTransformation;
    public static Role.RoleTemplate role;
    public static Implementation.ImplementationTemplate implementation;
    public static Slot.SlotTemplate slot;

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
    public static DEMRelation slotNumber;
    public static DEMRelation slotTemplate;
    public static DEMRelation processSequence;
    public static DEMRelation personOwns;
    public static DEMRelation ownedByPerson;

    public ProcessModel() {
        super("DEM-Process", "The process model provides entities for a more detailed " +
                "process modelling. The basic principles for process modelling are imported from the " +
                "Linked Resource Model.");
    }

    @Override
    public void createModelEntities() {
        ecosystemEvent = new EventTemplate(this);
        aggregatedProcess = new AggregatedProcessTemplate(this);
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
        responsible = new RelationBuilder(this, "responsible")
                .comment("Refers to the responsible person for the application of the policy.")
                .domain(CoreModel.policy).range(humanAgent).create();
        hasAuthor = new RelationBuilder(this, "hasAuthor").comment("Defines the author of the Digital Object.")
                .domain(CoreModel.digitalObject).range(humanAgent).create();
        hasRole = new RelationBuilder(this, "hasRole")
                .comment("Assigns a role to a community or to an agent.")
                .domain(CoreModel.community).domain(CoreModel.ecosystemAgent).range(role).create();
        hasAutomationState = new RelationBuilder(this, "hasAutomationState")
                .comment("Links an Automation State to a Process.").domain(CoreModel.process).create();
        hasImplementation = new RelationBuilder(this, "hasImplementation")
                .comment("Links an implementation entity to a Process.").domain(CoreModel.process)
                .range(implementation).create();
        hasMimeType = new RelationBuilder(this, "hasMimeType")
                .comment("Defines the mime type of an Implementation.").domain(implementation).create();
        hasInputSlot = new RelationBuilder(this, "hasInputSlot").comment("Specifies an output slot of a process.")
                .domain(slot).create();
        hasOutputSlot = new RelationBuilder(this, "hasOutputSlot").comment("Specifies an input slot of a process.")
                .domain(slot).create();
        slotNumber = new RelationBuilder(this, "slotNumber")
                .comment("The number of the slot as input or output parameter of the process.").domain(slot).create();
        slotTemplate = new RelationBuilder(this, "slotTemplate")
                .comment("The entity template / type which instances can be passed to this process slot")
                .domain(slot).create();
        processSequence = new RelationBuilder(this, "processSequence")
                .comment("Order of execution of sub-processes of an aggregated process").domain(aggregatedProcess)
                .create();
        passesOutputTo = new RelationBuilder(this, "passesOutputTo")
                .comment("The output of a typed process output slot is passed to a process input slot of the same type.")
                .domain(slot).range(slot).create();
        receivesInputFrom = new RelationBuilder(this, "receivesInputFrom")
                .comment("The typed process input slot receives the output from an output slot of the same type.")
                .domain(slot).range(slot).create();
        ownedByPerson = new RelationBuilder(this, "ownedByPerson")
                .comment("A human agent can own ecosystem entities.")
                .domain(CoreModel.ecosystemEntity).range(ProcessModel.humanAgent).create();
        personOwns = new RelationBuilder(this, "personOwns")
                .comment("A human agent can own ecosystem entities.")
                .domain(humanAgent).range(CoreModel.ecosystemEntity).inverse(ProcessModel.ownedByPerson).create();
    }
}
