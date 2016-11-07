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

import LRMv2.LRM_static_schema;
import entities.AggregatedPolicy.AggregatedPolicyTemplate;
import entities.*;
import entities.ImplementationState.ImplementationStateTemplate;
import entities.MetaPolicy.MetaPolicyTemplate;
import entities.PolicyStatement.PolicyStatementTemplate;
import entities.QualityAssurance.QualityAssuranceTemplate;
import entities.RequirementLevel.RequirementLevelTemplate;
import org.apache.jena.vocabulary.RDF;
import relations.DEMRelation;
import relations.RelationBuilder;

public class PolicyModel extends AbstractModel {
    /**
     * The abstract ecosystem templates. These are the abstract entity "classes"
     * of the ontology, which serve to be templates for the creation of real
     * existing "instances". See also the {@link entities} package.
     */
    public static AggregatedPolicyTemplate aggregatedPolicy;
    public static MetaPolicyTemplate metaPolicy;
    public static RequirementLevelTemplate requirementLevel;
    public static PolicyStatementTemplate policyStatement;
    public static PolicyLevel.PolicyLevelTemplate policyLevel;
    public static QualityAssuranceTemplate qualityAssurance;
    public static QualityAssuranceMethod.QualityAssuranceMethodTemplate qualityAssuranceMethod;
    public static UnitTest.UnitTestTemplate unitTest;
    public static ManualDependencyCheck.ManualDependencyCheckTemplate manualDependencyCheck;
    public static ImplementationStateTemplate implementationState;
    public static Risk.RiskTemplate risk;

    public static DEMRelation hasPolicyStatement;
    public static DEMRelation hasPolicyType;
    public static DEMRelation hasPolicyLevel;
    public static DEMRelation hasQA;
    public static DEMRelation classification;
    public static DEMRelation hasMetaPolicy;
    public static DEMRelation ensuresQACriterion;
    public static DEMRelation hasProcessSequence;
    public static DEMRelation hasLevel;
    public static DEMRelation hasRequirementLevel;
    public static DEMRelation format;
    public static DEMRelation language;
    public static DEMRelation hasImplementationState;
    public static DEMRelation hasRisk;
    public static DEMRelation validFrom;
    public static DEMRelation validTo;
    public static DEMRelation verifies;
    public static DEMRelation hasValidationStatus;
    public static DEMRelation hasImplementation;
    public static DEMRelation verifiedBy;
    public static DEMRelation assuresQualityOf;

    public PolicyModel() {
        super("DEM-Policy", "The policy model provides entities for a more detailed policy and " +
                "process modelling.\n" +
                "This is the implementation of the PERICLES Policy- and QA-Model as developed by Fabio Corubolo " +
                "(University of Liverpool) et. al.. The theoretical model is described in detail in PERICLES " +
                "deliverable D5.3. http://pericles-project.eu/deliverables");
    }

    @Override
    public void createModelEntities() {
        aggregatedPolicy = new AggregatedPolicyTemplate(this);
        metaPolicy = new MetaPolicyTemplate(this);
        qualityAssurance = new QualityAssuranceTemplate(this);
        qualityAssuranceMethod = new QualityAssuranceMethod.QualityAssuranceMethodTemplate(this);
        unitTest = new UnitTest.UnitTestTemplate(this);
        manualDependencyCheck = new ManualDependencyCheck.ManualDependencyCheckTemplate(this);
        requirementLevel = new RequirementLevelTemplate(this);
        policyStatement = new PolicyStatementTemplate(this);
        policyLevel = new PolicyLevel.PolicyLevelTemplate(this);
        implementationState = new ImplementationState.ImplementationStateTemplate(this);
        risk = new Risk.RiskTemplate(this);
    }

    @Override
    public void createModelRelations() {
        hasPolicyStatement = new RelationBuilder(this, "hasPolicyStatement", CoreModel.policy).comment("Refers to the policy statement.")
                .range(policyStatement).superRelation(LRM_static_schema.describedBy).create();
        hasPolicyType = new RelationBuilder(this, "hasPolicyType", CoreModel.policy).comment("Links a Policy entity to a Policy Type.")
                .superRelation(LRM_static_schema.describedBy).create();
        hasPolicyLevel = new RelationBuilder(this, "hasPolicyLevel", CoreModel.policy).comment("Links a Policy entity to a Policy Level.")
                .range(policyLevel).superRelation(LRM_static_schema.describedBy).create();
        hasValidationStatus = new RelationBuilder(this, "hasValidationStatus", CoreModel.policy)
                .comment("Links a Policy entity to a Validation Status.")
                .superRelation(LRM_static_schema.describedBy).create();
        hasQA = new RelationBuilder(this, "addQACriterion", CoreModel.policy)
                .comment("Refers to a quality assurance criterion for a policy.")
                .range(qualityAssurance).create();
        classification = new RelationBuilder(this, "classification", CoreModel.policy)
                .comment("Type of policy (TBC), such as: data management, format, access, ...").create();
        ensuresQACriterion = new RelationBuilder(this, "ensuresQACriterion", CoreModel.process)
                .comment("Links to a QA criterion, which is ensured by this process.")
                .range(qualityAssurance).create();
        hasProcessSequence = new RelationBuilder(this, "hasProcessSequence", CoreModel.process)
                .comment("Ordered sequence of processes which implement a policy").domain(aggregatedPolicy)
                .create();
        // TODO: allow RDF.Seq as range & right handling at the GUI
        hasProcessSequence.addRange(RDF.Seq);
        hasLevel = new RelationBuilder(this, "complianceLevel", requirementLevel).comment("Describes the level of compliance")
                .superRelation(LRM_static_schema.describedBy).create();
        hasRequirementLevel = new RelationBuilder(this, "hasRequirementLevel", CoreModel.policy)
                .comment("The Policy has a requirement level.")
                .range(requirementLevel).create();
        format = new RelationBuilder(this, "format", policyStatement)
                .comment("Formal format: if so what language used; or non formal (free text)")
                .create();
        language = new RelationBuilder(this, "language", policyStatement)
                .comment("The language used for the criteria definition (natural, ReAL, SWRL, etc.)").create();
        hasImplementationState = new RelationBuilder(this, "hasImplementationState", CoreModel.policy)
                .comment("Current state of the policy: How well the policy is currently implemented.")
                .range(implementationState).create();
        hasMetaPolicy = new RelationBuilder(this, "hasMetaPolicy", CoreModel.policy).comment("The policy has a meta policy.")
                .range(metaPolicy).create();
        hasRisk = new RelationBuilder(this, "hasRisk", CoreModel.policy).comment("The policy has a risk.")
                .range(risk).create();
        validFrom = new RelationBuilder(this, "validFrom", CoreModel.policy).comment("Defines a start date of a policy.")
                .create();
        validTo = new RelationBuilder(this, "validTo", CoreModel.policy).comment("Defines an expire date of a policy.")
                .create();
        verifies = new RelationBuilder(this, "verifies", qualityAssurance)
                .comment("A quality assurance method verifies that a quality assurance criterion or test is " +
                        "fulfilled regarding the related ecosystem entity.")
                .domain(qualityAssuranceMethod).range(CoreModel.ecosystemEntity).create();
        hasImplementation = new RelationBuilder(this, "hasImplementation", qualityAssurance)
                .comment("Links a Quality Assurance Criterion to a Quality Assurance method that implements the " +
                        "criterion").range(qualityAssuranceMethod).create();
        verifiedBy = new RelationBuilder(this, "verifiedBy", manualDependencyCheck)
                .comment("Links a Manual Dependency Check to the Agent that executes the manual qa verification.")
                .range(CoreModel.ecosystemAgent).create();
        assuresQualityOf = new RelationBuilder(this, "assuresQualityOf", qualityAssurance)
                .comment("The Quality Assurance definition assures the quality of a designated Policy linked via " +
                        "this relation.").range(CoreModel.policy).create();
    }
}
