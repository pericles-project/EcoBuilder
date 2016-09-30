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
import models.*;

/**
 * A policy is a plan that defines the desired state inside a digital ecosystem. A policy describes the 'what'
 * (guidelines) and not the 'how' (implementation).
 */
public class Policy extends EcosystemEntity {

    /**
     * @param scenario
     * @param identifier
     * @param template
     */
    public Policy(ScenarioModel scenario, String identifier, Template template) {
        super(scenario, identifier, template);
    }

    public Policy(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, CoreModel.policy);
    }

    public void hasRequirementsLevel(RequirementLevel level) {
        addProperty(PolicyModel.hasRequirementLevel, level);
    }

    public void hasRequirementsLevel(RequirementLevel.ReqLevel level) {
        hasRequirementsLevel(level.level);
    }

    public void hasRequirementsLevel(String level) {
        addProperty(PolicyModel.hasRequirementLevel, level);
    }

    public void hasType(TypeOfPolicy type) {
        hasType(type.type);
    }

    public void hasType(String type) {
        addProperty(PolicyModel.hasPolicyType, type);
    }

    public PolicyStatement addStatement(String text) {
        PolicyStatement statement = PolicyModel.policyStatement.getInstance(model);
        statement.addProperty(LRM_static_schema.definition, text);
        addProperty(PolicyModel.hasPolicyStatement, statement);
        return statement;
    }

    public PolicyStatement addStatement(String text, String format) {
        PolicyStatement statement = addStatement(text);
        statement.format(format);
        return statement;
    }

    public PolicyStatement addStatement(String text, String format, String language) {
        PolicyStatement statement = addStatement(text, format);
        statement.language(language);
        return statement;
    }

    public void addQACriterion(QualityAssurance criterion) {
        addProperty(PolicyModel.hasQA, criterion);
    }

    public void classification(String classification) {
        addProperty(PolicyModel.classification, classification);
    }

    public void hasMandator(EcosystemEntity mandator) {
        addProperty(CoreModel.hasMandator, mandator);
    }

    public void responsiblePerson(HumanAgent admin) {
        addProperty(ProcessModel.responsible, admin);
    }

    public void conflictDetectedWith(EcosystemEntity resource) {
        addProperty(CoreModel.hasConflictIDAttribute, resource);
    }

    public void constraints(EcosystemEntity entity) {
        addProperty(CoreModel.constrains, entity);
    }

    public void targetCommunity(Community community) {
        addProperty(CoreModel.targetCommunity, community);
    }

    public void replaces(Policy policy) {
        addProperty(CoreModel.replaces, policy);
    }

    public void hasConflictIDAttribute(String conflict) {
        addProperty(CoreModel.hasConflictIDAttribute, conflict);
    }

    public void isEnforcedBy(Process process) {
        addProperty(CoreModel.isEnforcedBy, process);
        process.addProperty(CoreModel.isImplementationOf, this);
    }

    public void isValid(boolean valid) {
        if (valid) {
            addProperty(PolicyModel.hasValidationStatus, "VALID");
        } else {
            addProperty(PolicyModel.hasValidationStatus, "NOT_VALID");
        }
    }

    public void isImplementedBy(Process process) {
        isEnforcedBy(process);
    }

    public void derivedFrom(Policy policy) {
        addProperty(CoreModel.derivedFromPolicy, policy);
    }

    public enum TypeOfPolicy {
        MANDATORY("MANDATORY"),
        LEGAL_REQUIREMENT("LEGAL_REQUIREMENT"),
        ASPIRATIONAL("ASPIRATIONAL"),
        BUSINESS_DRIVEN("BUSINESS_DRIVEN");

        String type;

        TypeOfPolicy(String type) {
            this.type = type;
        }
    }

    public static class PolicyTemplate extends Template {
        public PolicyTemplate(CoreModel coreModel) {
            this(coreModel, "Policy",
                    "A policy is a plan that defines the desired state inside "
                            + "a digital ecosystem. A policy describes the 'what' "
                            + "(guidelines) and not the 'how' (implementation).", CoreModel.ecosystemEntity);
        }

        public PolicyTemplate(AbstractModel model, String name, String comment, Template parent) {
            super(model, name, parent);
            addDescription(comment);
            addSuperClass(LRM_static_schema.Plan);
            addSuperClass(LRM_static_schema.AbstractResource);
        }

        @Override
        public Policy createEntity(ScenarioModel model, String ID) {
            return new Policy(model, ID);
        }
    }
}
