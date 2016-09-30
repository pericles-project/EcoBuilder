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
 * The requirement levels as defined in RFC 2119. This can be used to define what the desired level
 * of compliance of a policy is. (must, must not, required, shall, shall not, should,
 * should not, recommended, may, optional)
 */
public class RequirementLevel extends EcosystemEntity {
    public RequirementLevel(ScenarioModel model, String identifier) {
        super(model, identifier, PolicyModel.requirementLevel);
    }

    public void isRequirementLevelOf(Policy policy) {
        policy.addProperty(PolicyModel.hasRequirementLevel, this);
    }

    public enum ReqLevel {
        MUST("MUST"),
        MUST_NOT("MUST NOT"),
        REQUIRED("REQUIRED"),
        SHALL("SHALL"),
        SHALL_NOT("SHALL NOT"),
        SHOULD("SHOULD"),
        SHOULD_NOT("SHOULD NOT"),
        RECOMMENDED("RECOMMENDED"),
        MAY("MAY"),
        OPTIONAL("OPTIONAL");

        String level;

        ReqLevel(String level) {
            this.level = level;
        }
    }

    public static class RequirementLevelTemplate extends Template {
        public RequirementLevelTemplate(PolicyModel policyModel) {
            super(policyModel, "Requirement Level", CoreModel.ecosystemEntity);
            addDescription("The requirement levels as defined in RFC 2119. This can be used to define what the desired level " +
                    "of compliance of a policy is. (must, must not, required, shall, shall not, should, " +
                    "should not, recommended, may, optional)");
            addSuperClass(LRM_static_schema.Description);
        }

        public RequirementLevel createRequirementLevel(ScenarioModel scenario, ReqLevel compliance) {
            return new RequirementLevel(scenario, compliance.level);
        }

        @Override
        public RequirementLevel createEntity(ScenarioModel model, String ID) {
            return new RequirementLevel(model, ID);
        }
    }
}
