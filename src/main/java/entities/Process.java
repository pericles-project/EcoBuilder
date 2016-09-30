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

import LRMv2.LRM_dynamic_schema;
import com.hp.hpl.jena.rdf.model.Resource;
import models.*;

/**
 * Entity in the Digital Ecosystem which represents processes.
 * <p>
 * A process is a description of linked steps on how to transform an input to a certain output.
 */
public class Process extends EcosystemActivity {

    public Process(ScenarioModel scenario, String identifier, Template template) {
        super(scenario, identifier, template);
    }

    public Process(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, CoreModel.process);
    }

    public void accessedVia(ServiceInterface SInterface) {
        addProperty(InfrastructureModel.accessedVia, SInterface);
    }

    public void isImplementationOf(Policy policy) {
        policy.isEnforcedBy(this);
    }

    public void manages(EcosystemEntity entity) {
        addProperty(CoreModel.manages, entity);
    }

    public void runsOn(TechnicalService technicalService) {
        addProperty(CoreModel.runsOn, technicalService);
    }

    public void setFunction(String function) {
        addProperty(CoreModel.function, function);
    }

    public void hasInput(Resource entity) {
        addProperty(CoreModel.hasInput, entity);
    }

    public void hasOutput(Resource entity) {
        addProperty(CoreModel.hasOutput, entity);
    }

    public void hasInput(String entity) {
        addProperty(CoreModel.hasInput, entity);
    }

    public void hasOutput(String entity) {
        addProperty(CoreModel.hasOutput, entity);
    }

    public void hasInputSlot(Slot slot) {
        addProperty(ProcessModel.hasInputSlot, slot);
    }

    public void hasOutputSlot(Slot slot) {
        addProperty(ProcessModel.hasOutputSlot, slot);
    }

    public void hasLink(String link) {
        addProperty(CoreModel.hasLink, link);
    }

    public void ensuresQACriterion(QualityAssurance criterion) {
        addProperty(PolicyModel.ensuresQACriterion, criterion);
    }

    public void isExecutedBy(EcosystemAgent agent) {
        addProperty(LRM_dynamic_schema.executedBy, agent);
        agent.addProperty(LRM_dynamic_schema.executes, this);
    }

    public void setImplementation(Implementation implementation) {
        addProperty(ProcessModel.hasImplementation, implementation);
    }

    public void setAutomationState(String state) {
        addProperty(ProcessModel.hasAutomationState, state);
    }

    public void setAutomationState(AutomationState state) {
        addProperty(ProcessModel.hasAutomationState, state.state);
    }

    public enum AutomationState {
        MANUAL("MANUAL"),
        SEMI_AUTOMATED("SEMI_AUTOMATED"),
        AUTOMATED("AUTOMATED");
        protected String state;

        AutomationState(String state) {
            this.state = state;
        }
    }

    public static class ProcessTemplate extends ActivityTemplate {
        public ProcessTemplate(CoreModel coreModel) {
            this(coreModel, "Process",
                    "A process is a description of linked steps on how to transform an input to a certain output.", CoreModel.ecosystemActivity);
        }

        public ProcessTemplate(AbstractModel model, String name, String comment, Template parent) {
            super(model, name, comment, parent);
        }

        @Override
        public Process createEntity(ScenarioModel model, String ID) {
            return new Process(model, ID);
        }
    }
}
