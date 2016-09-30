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
import models.PolicyModel;
import models.ScenarioModel;

/**
 * Level of a policy, e.g. guidance, procedure, control, etc.
 */
public class PolicyLevel extends EcosystemEntity {
    public PolicyLevel(ScenarioModel model, String identifier) {
        super(model, identifier, PolicyModel.policyLevel);
    }

    public void isLevelOf(Policy policy) {
        policy.addProperty(PolicyModel.hasPolicyLevel, this);
    }

    public enum LevelOfPolicy {
        GUIDANCE("GUIDANCE", "High level principles and general objectives driving an organisation, the most abstract level of policy."),
        PROCEDURE("PROCEDURE", "Lower level policies that gives detail of how the policy is implemented without strong dependencies on the infrastructure"),
        CONTROL("CONTROL", "Low level description of the policy that includes reference to the specific infrastructure.");

        String level;
        String description;

        LevelOfPolicy(String level, String description) {
            this.level = level;
            this.description = description;
        }
    }

    public static class PolicyLevelTemplate extends Template {
        public PolicyLevelTemplate(PolicyModel policyModel) {
            super(policyModel, "Policy Level", CoreModel.ecosystemEntity);
            addDescription("Level of a policy, e.g. guidance, procedure, control, etc..");
            addSuperClass(LRM_static_schema.Description);
        }

        public PolicyLevel createPolicyLevel(ScenarioModel scenario, LevelOfPolicy level) {
            PolicyLevel policyLevel = new PolicyLevel(scenario, level.level);
            policyLevel.describedBy(level.description);
            return policyLevel;
        }

        @Override
        public PolicyLevel createEntity(ScenarioModel model, String ID) {
            return new PolicyLevel(model, ID);
        }
    }
}