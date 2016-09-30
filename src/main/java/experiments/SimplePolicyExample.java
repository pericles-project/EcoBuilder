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
package experiments;

import entities.*;
import entities.Process;
import models.PolicyModel;

/**
 * This example demonstrates the instantiation of a policy entity.
 */
public class SimplePolicyExample extends Experiment {

    public SimplePolicyExample() {
        super("Simple-policy-example");
        Policy policy = new Policy(scenario, "Example Policy");
        policy.id("THIS_IS_THE_EXAMPLE_UUID_12323213");
        policy.name("An example policy");
        policy.version("0.9");
        policy.describedBy(
                "This policy is there to show the creation of a policy individual at the ecosystem CORE_MODEL.");
        policy.intention("The purpose is to discuss the policy entity");
        policy.addStatement("This would be a more detailed policy statement.");
        QualityAssurance criterionA = new QualityAssurance(scenario, "Quality assurance criterion A");
        QualityAssurance criterionB = new QualityAssurance(scenario, "example QA criterion B");
        policy.addQACriterion(criterionA);
        policy.addQACriterion(criterionB);
        policy.classification("Just an example");

        Community company = new Community(scenario, "Company A");
        policy.hasMandator(company);
        HumanAgent admin = new HumanAgent(scenario, "System Administrator");
        admin.isMemberOf(company);
        policy.responsiblePerson(admin);
        Process policyEnforcingProcess = new Process(scenario, "policy Enforcing Process");
        policy.isEnforcedBy(policyEnforcingProcess);

        policy.hasRequirementsLevel(RequirementLevel.ReqLevel.SHOULD);
        ImplementationState state = PolicyModel.implementationState.createImplementationState(scenario, ImplementationState.State.IMPLEMENTED);
        state.isStateOf(policy);
        policy.addStatement("Policy should be applied during development");
        Policy badPolicy = new Policy(scenario, "bad Policy");
        policy.conflictDetectedWith(badPolicy);
        DigitalObject constraindedObject = new DigitalObject(scenario, "Example DO");
        policy.constraints(constraindedObject);
    }
}
