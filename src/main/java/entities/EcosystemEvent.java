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
import models.ProcessModel;
import models.CoreModel;
import models.ScenarioModel;

/**
 * An event triggered by an EcosystemActivity. Add a more specific LRM event class as second base class to describe the
 * event type, in more detail e.g. lrm:RecurringEvent, lrm:ChangeEvent, lrm:CreationEvent, etc.
 */
public class EcosystemEvent extends EcosystemEntity {

    public enum EventType {
        STARTED, STOPPED, SUSPENDED, RESUMED
    }

    public EcosystemEvent(ScenarioModel model, String identifier, EventType type) {
        this(model, identifier);
        switch (type) {
            case STARTED:
                addOntClass(LRM_dynamic_schema.ActivityStarted);
                break;
            case STOPPED:
                addOntClass(LRM_dynamic_schema.ActivityStopped);
                break;
            case SUSPENDED:
                addOntClass(LRM_dynamic_schema.ActivitySuspended);
                break;
            case RESUMED:
                addOntClass(LRM_dynamic_schema.ActivityResumed);
                break;
        }
    }

    public EcosystemEvent(ScenarioModel model, String identifier) {
        super(model, identifier, ProcessModel.ecosystemEvent);

    }

    public void isConsequenceOf(EcosystemEvent event) {
        addProperty(LRM_dynamic_schema.isConsequenceOf, event);
    }

    public void triggerdBy(EcosystemActivity activity) {
        activity.addProperty(LRM_dynamic_schema.triggeredBy, this);
        // addRelationTarget(LRM_dynamic_schema.triggers, this);
    }

    public void byAgent(EcosystemAgent agent) {
        agent.triggerEvent(this);
    }

    public static class EventTemplate extends Template {
        public EventTemplate(ProcessModel changeModel) {
            super(changeModel, "Ecosystem Event", CoreModel.ecosystemEntity);
            addDescription("An event triggered by an EcosystemActivity. Add a more specific "
                    + "LRM event class as second base class to describe the event type, "
                    + "in more detail e.g. lrm:RecurringEvent, lrm:ChangeEvent, lrm:CreationEvent, etc.");
            addSuperClass(LRM_dynamic_schema.ExogenousEvent);
        }

        @Override
        public EcosystemEvent createEntity(ScenarioModel model, String ID) {
            return new EcosystemEvent(model, ID);
        }
    }
}
