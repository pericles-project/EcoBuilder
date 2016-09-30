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
import entities.Community.CommunityTemplate;
import entities.DigitalObject.DigitalObjectTemplate;
import entities.EcosystemActivity;
import entities.EcosystemAgent;
import entities.Policy.PolicyTemplate;
import entities.Process.ProcessTemplate;
import entities.TechnicalService.TechnicalServiceTemplate;
import entities.Template;
import relations.DEMRelation;
import relations.RelationBuilder;

/**
 * The Core Model is the base DEM model imported by all sub DEM models. It is a template model and therefore implemented
 * as Singleton, as it should exist only once.
 * <p>
 * The Core Model provides the five basic DEM entities (policy, community, technical service, process, and digital
 * object) and a few other base entities.
 */
public class CoreModel extends AbstractModel {
    /**
     * The abstract ecosystem templates. These are the abstract entity "classes"
     * of the ontology, which serve to be templates for the creation of real
     * existing "instances". See also the {@link entities} package.
     */
    public static Template ecosystemEntity;
    public static EcosystemActivity.ActivityTemplate ecosystemActivity;
    public static EcosystemAgent.AgentTemplate ecosystemAgent;
    public static PolicyTemplate policy;
    public static ProcessTemplate process;
    public static DigitalObjectTemplate digitalObject;
    public static CommunityTemplate community;
    public static TechnicalServiceTemplate technicalService;

    /*
     * Core model relations
     */
    public static DEMRelation ownedByCommunity;
    public static DEMRelation managedBy;
    public static DEMRelation hasMandator;
    public static DEMRelation targetCommunity;
    public static DEMRelation replaces;
    public static DEMRelation manages;
    public static DEMRelation runsOn;
    public static DEMRelation function;
    public static DEMRelation hasInput;
    public static DEMRelation hasOutput;
    public static DEMRelation hasLink;
    public static DEMRelation communityOwns;
    public static DEMRelation hasConflictIDAttribute;
    public static DEMRelation constrains;
    public static DEMRelation replacedBy;
    public static DEMRelation checksum;
    public static DEMRelation isEnforcedBy;
    public static DEMRelation isImplementationOf;
    public static DEMRelation derivedFromPolicy;
    public static DEMRelation derivedFromObject;

    public CoreModel() {
        super("DEM-Core", "The core model of the Digital Ecosystem Model contains the five saver " +
                "entities Digital Object, Community, Technical Service, Policy and Process.\n" +
                "The use of the core model is obligatory, whereas the other DEM-sub-models will be imported " +
                "only if they are needed for a more detailed modelling.");
    }

    @Override
    public void createModelEntities() {
        ecosystemEntity = new Template(this, "Ecosystem Entity", null);
        ecosystemEntity.addDescription("This is an abstract template entity that holds some common relations that are " +
                "inherited by all ecosystem entities.");
        ecosystemActivity = new EcosystemActivity.ActivityTemplate(this);
        ecosystemAgent = new EcosystemAgent.AgentTemplate(this);
        policy = new PolicyTemplate(this);
        process = new ProcessTemplate(this);
        digitalObject = new DigitalObjectTemplate(this);
        community = new CommunityTemplate(this);
        technicalService = new TechnicalServiceTemplate(this);
    }

    @Override
    public void createModelRelations() {
        hasMandator = new RelationBuilder(this, "hasMandator")
                .comment("The entity that mandates or generates the policy. The mandator could be also a reference " +
                        "to a legal requirement (in case that a policy is mandated by a legal requirement) or a directive.")
                .domain(policy).range(ecosystemEntity).create();
        ownedByCommunity = new RelationBuilder(this, "ownedByCommunity").comment("A community can own ecosystem entities.")
                .domain(ecosystemEntity).range(community).create();
        communityOwns = new RelationBuilder(this, "communityOwns").comment("A group of humans can own ecosystem entities.")
                .domain(community).range(ecosystemEntity).inverse(ownedByCommunity).create();
        managedBy = new RelationBuilder(this, "managedBy").comment("This entity is managed by a process.")
                .domain(ecosystemEntity).range(community).create();
        targetCommunity = new RelationBuilder(this, "targetCommunity")
                .comment("The community for which the policy has been designed.")
                .domain(policy).range(community).create();
        replaces = new RelationBuilder(this, "replaces")
                .comment("Refers to an old policy which is replaced by this policy.")
                .domain(policy).range(policy).create();
        manages = new RelationBuilder(this, "manages").comment("A Process can manage all ecosystem entities.")
                .domain(process).range(ecosystemEntity).inverse(managedBy).create();
        runsOn = new RelationBuilder(this, "runsOn")
                .comment("Relation to the technical service infrastructure on which the process is executed.")
                .domain(process).range(technicalService).create();
        function = new RelationBuilder(this, "function")
                .comment("The function the process performs, or its purpose.").domain(process).create();
        hasInput = new RelationBuilder(this, "hasInput").comment("The input of the process.").domain(process)
                .range(ecosystemEntity).create();
        hasOutput = new RelationBuilder(this, "hasOutput").comment("The output of the process.").domain(process)
                .range(ecosystemEntity).create();
        hasLink = new RelationBuilder(this, "hasLink").comment("Link to the BPMN implementation.").domain(process)
                .create();
        hasConflictIDAttribute = new RelationBuilder(this, "hasConflictIDAttribute")
                .comment("Attribute for conflicting policies detection").domain(policy).create();
        constrains = new RelationBuilder(this, "constrains")
                .comment("Refers to an ecosystem entity which is constrained by the policy.")
                .domain(policy).range(ecosystemEntity).create();
        replacedBy = new RelationBuilder(this, "replacedBy").comment("Refers to a policy which replaces this policy.")
                .domain(policy).range(policy).inverse(replaces).create();
        checksum = new RelationBuilder(this, "checksum").comment("The checksum of a digital object.")
                .domain(digitalObject).create();
        isEnforcedBy = new RelationBuilder(this, "isEnforcedBy")
                .comment("A Process can be the implementation of a Policy.")
                .domain(process).range(process).superRelation(LRM_static_schema.implementedBy).create();
        isImplementationOf = new RelationBuilder(this, "isImplementationOf")
                .comment("A Policy can be implemented by a Process.").domain(process).range(policy)
                .inverse(isEnforcedBy).create();
        derivedFromPolicy = new RelationBuilder(this, "derivedFromPolicy")
                .comment("A Policy can be derived from a higher level policy. It is therewith not a sub policy of " +
                        "the higher level policy, but a derivation of it.")
                .domain(policy).range(policy).create();
        derivedFromObject = new RelationBuilder(this, "derivedFromObject")
                .comment("A Digital Object can be derived from another Digital Object. Then it is not a sub object, " +
                        "but a derivation.")
                .domain(digitalObject).range(digitalObject).create();
    }
}