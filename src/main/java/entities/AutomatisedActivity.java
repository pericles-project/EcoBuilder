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
import models.CoreModel;
import models.InfrastructureModel;
import models.ScenarioModel;

/**
 * An automatised activity is an ecosystem activity which is executed by a Hard- or SoftwareAgent.
 */
public class AutomatisedActivity extends EcosystemActivity {

    public AutomatisedActivity(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, InfrastructureModel.automatisedActivity);
    }

    public static class AutomatisedActivityTemplate extends ActivityTemplate {
        public AutomatisedActivityTemplate(InfrastructureModel model) {
            super(model, "Automatised Activity",
                    "An automatised activity is an ecosystem activity which is executed by a Hard- or SoftwareAgent.",
                    CoreModel.ecosystemActivity);
            addProperty(LRM_dynamic_schema.executedBy, InfrastructureModel.automaticAgent);
        }

        @Override
        public AutomatisedActivity createEntity(ScenarioModel model, String ID) {
            return new AutomatisedActivity(model, ID);
        }

	/*
     * TODO: implement started/stopped
	 * 
	 * public OntResource activityStarted; public OntResource
	 * activityStopped; public OntResource activitySuspended; public
	 * OntResource activityResumed; public OntResource activityLifeEvent;
	 */
        // public EcosystemIndividual createConcreteResource(String identifier,
        // EcosystemIndividual automaticAgent) {
        // EcosystemIndividual resource = createConcreteResource(identifier);
        // resource.addRelationTarget(LRM_dynamic_schema.executedBy, automaticAgent);
        // // create event here?
        // return resource;
        // }
    }
}
