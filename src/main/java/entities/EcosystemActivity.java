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
import models.AbstractModel;
import models.CoreModel;
import models.ScenarioModel;

import java.util.Date;

import static entities.EcosystemEvent.EventType.*;

/**
 * An activity executed by an EcosystemAgent. Add a specific activity class from the LRM as second base class to
 * specify the activity type, e.g. lrm:ActivityStarted, lrm:ActivityStopped, lrm:ActivitySuspended, lrm:ActivityResumed.
 */
public class EcosystemActivity extends EcosystemEntity {
    public EcosystemActivity(ScenarioModel model, String identifier, Template resource) {
        super(model, identifier, resource);
    }

    public EcosystemActivity(ScenarioModel model, String identifier) {
        super(model, identifier, CoreModel.ecosystemActivity);
    }

    public void executedBy(EcosystemAgent agent) {
        agent.execute(this);
    }

    public void triggeredBy(EcosystemEvent event) {
        event.triggerdBy(this);
    }

    public void usesResource(EcosystemEntity entity) {
        addProperty(LRM_dynamic_schema.used, entity);
    }

    public void start(EcosystemAgent executingAgent) {
        addProperty(LRM_dynamic_schema.executedBy, executingAgent);
        start();
    }

    public void stop(EcosystemAgent executingAgent) {
        addProperty(LRM_dynamic_schema.executedBy, executingAgent);
        stop();
    }

    public void suspend(EcosystemAgent executingAgent) {
        addProperty(LRM_dynamic_schema.executedBy, executingAgent);
        suspend();
    }

    public void resume(EcosystemAgent executingAgent) {
        addProperty(LRM_dynamic_schema.executedBy, executingAgent);
        resume();
    }

    public void start() {
        new EcosystemEvent(model, name + "-started", STARTED);
        addProperty(LRM_dynamic_schema.startingTime, new Date().toString());
    }

    public void stop() {
        new EcosystemEvent(model, name + "-stopped", STOPPED);
        // TODO: relation shouldn't refer to string, but to TemporalDescription
        addProperty(LRM_dynamic_schema.endingTime, new Date().toString());
    }

    public void suspend() {
        new EcosystemEvent(model, name + "-suspended", SUSPENDED);
    }

    public void resume() {
        new EcosystemEvent(model, name + "-resumed", RESUMED);
    }

    public static class ActivityTemplate extends Template {
        public ActivityTemplate(CoreModel coreModel) {
            this(coreModel, "Ecosystem Activity",
                    "An activity executed by an EcosystemAgent. Add a specific activity class "
                            + "from the LRM as second base class to specify the activity type, e.g. "
                            + "lrm:ActivityStarted, lrm:ActivityStopped, lrm:ActivitySuspended, lrm:ActivityResumed.",
                    CoreModel.ecosystemEntity);
        }

        public ActivityTemplate(AbstractModel model, String name, String comment, Template parent) {
            super(model, name, comment, parent);
            addSuperClass(LRM_dynamic_schema.ExogenousActivity);
        }

        @Override
        public EcosystemActivity createEntity(ScenarioModel model, String ID) {
            return new EcosystemActivity(model, ID);
        }
    }
}
