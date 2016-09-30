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
/*
 * Single resource example for D5.3 change management
 *
 *
 * This is an example initialisation of an Ecosystem Model created with the Ecosystem Modelling Component.
 * It's a model for the EUMETSAT data policy on release of data described in detail here:
 * https://docs.google.com/document/d/1_5iEI4pRONHcxhG0RFsztMo3sAQcIImxWTvk-JgFKR8/edit#heading=h.u5kafpd6u2hz
 *
 * 3.08.2016
 * Fabio Corubolo
 */
package experiments;

import entities.*;
import entities.Process;
import models.CoreModel;
import relations.DEMRelation;
import relations.RelationBuilder;

/**
 * This is an example form the space science EUMETSAT domain.
 *
 * Meteosat Data and Derived Products older than [time_before_release] hours are distributed on request
 * from the EUMETSAT Data Archive in digital and graphical form via the associated operational service
 * in formats which represent both full and partial spatial coverage as well as both full and partial
 * spatial resolution.
 */
public class ChangeManagementExample extends Experiment {
    /* Ecosystem Communities */
    private Community authorisedOrganisations;
    private Community websiteVisitors;
    private Community EUMETSAT;
    private HumanAgent policyManager;
    private HumanAgent visitor;
    /* Technical Ecosystem Infrastructure */
    private TechnicalService publicRepository;
    private TechnicalService privateRepository;
    private ServiceInterface publicWebsite;
    private ServiceInterface privateWebsite;
    private DigitalObject DOdata;
    /* Ecosystem Policy Management */
    private Policy METSATPolicy14;
    private Process move;
    private Process partialRes;
    EcosystemDependency appliesTo;
    EcosystemDependency uses;
    EcosystemDependency uses2;
    DEMRelation URL = new RelationBuilder(scenario, "URL").create();
    DEMRelation type = new RelationBuilder(scenario, "type").create();
    DEMRelation creationTime = new RelationBuilder(scenario, "creationTime").create();

    public ChangeManagementExample() {
        super("Change-management-EUMETSAT-example");
        /* Start state: */
        scenarioCommunities();
        technicalEcosystemInfrastructure();
        scenarioPolicyManagement();
    }

    private void scenarioCommunities() {
        authorisedOrganisations = new Community(scenario, "AuthorisedOrganisations");
        websiteVisitors = new Community(scenario, "GeneralWebsiteVisitors");
        EUMETSAT = new Community(scenario, "GeneralWebsiteVisitors");
        policyManager = new HumanAgent(scenario, "Policy Manager");
        visitor = new HumanAgent(scenario, "Visitor");
        policyManager.isMemberOf(EUMETSAT);
        visitor.isMemberOf(websiteVisitors);
    }

    private void technicalEcosystemInfrastructure() {
        publicRepository = new TechnicalService(scenario, "Public data repository");
        privateRepository = new TechnicalService(scenario, "Private data repository");
        privateRepository.addProperty(URL, "http://eumetsat/private");
        publicRepository.addProperty(URL, "http://eumetsat/public");

        publicWebsite = new ServiceInterface(scenario, "Public Website");
        privateWebsite = new ServiceInterface(scenario, "Private Website");
        publicWebsite.isUsedBy(websiteVisitors);
        privateWebsite.isUsedBy(authorisedOrganisations);

        DOdata = new DigitalObject(scenario, "Metadata");
        EUMETSAT.owns(publicRepository);
        EUMETSAT.owns(privateRepository);
        authorisedOrganisations.owns(DOdata);
    }

    private void scenarioPolicyManagement() {
        METSATPolicy14 = new Policy(scenario, "DataReleasePolicyPG14");
        METSATPolicy14.addStatement(" Meteosat Data and Derived Products older than [time_before_release] hours are " +
                "distributed on request from the EUMETSAT Data Archive in digital and graphical " +
                "form via the associated operational service in formats which represent both full " +
                "and partial spatial coverage as well as both full and partial spatial resolution", "non-formal", "en");
        METSATPolicy14.addStatement(" Change 0 : create the dependency \n" +
                "Precondition: New DOdata created\n" +
                "Impact: if ( type==Meteosat data && Location == Private repository->URL)\n" +
                "Create dependency “appliesTo” between this policy  and\n" +
                "New DOdata;\n" +
                "Create time trigger for X hours.\n", "non-formal", "en");

        METSATPolicy14.responsiblePerson(policyManager);
        METSATPolicy14.version("1.0");
        METSATPolicy14.hasMandator(EUMETSAT);
        METSATPolicy14.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        METSATPolicy14.hasType(Policy.TypeOfPolicy.MANDATORY);
        DEMRelation time_before_release = new RelationBuilder(scenario, "time_before_release").create();
        METSATPolicy14.addProperty(time_before_release, "24");
        METSATPolicy14.addProperty(URL, "http://eumetsat/private");
        METSATPolicy14.addProperty(type, "Meteosat data");
        METSATPolicy14.addProperty(creationTime, "2016-08-03T15:15:34.602Z");

        move = new Process(scenario, "Move");
        move.describedBy("Mova data from one repository to another");
        move.hasInput(CoreModel.digitalObject);
        move.hasInput(privateRepository);
        move.hasInput(publicRepository);

        partialRes = new ProcessForTransformation(scenario, "PartialRes");
        partialRes.describedBy("Create a partial resolution version of the data");
        partialRes.hasInput(CoreModel.digitalObject);
        partialRes.hasInput(privateRepository);
        partialRes.hasInput(publicRepository);

        appliesTo = new EcosystemDependency(scenario, "policy applies to");
        appliesTo.from(METSATPolicy14);
        appliesTo.to(DOdata);
        appliesTo.describedBy("This dependency identifies the Digital Objects that are target of the policy. It can be instantiated by " +
                "The policy itself; the criteria for generating the dependency is 'if ( type==Meteosat data && Location == Private repository->URL)" +
                "Create dependency “appliesTo” between this policy  and" +
                "New DOdata;"
        );
        DEMRelation change0 = new RelationBuilder(scenario, "change0, TimeTrigger delta reported").create();

        appliesTo.addProperty(change0, "\n" +
                "Precondition: TimeTrigger delta reported\n" +
                "Impact: if (DOdata->location = private repository->URL) and  (DOdata->time - creation time) >policy->X\n" +
                "Move(DOdata); PartialRes (DOdata);\n"
        );
        DEMRelation change1 = new RelationBuilder(scenario, "change1, change in repository URL").create();

        appliesTo.addProperty(change1, "\n" +
                "Precondition: change in Repository url (delta)\n" +
                "Impact: for all DOdata where location = old URL:\n" +
                "\t\tUpdate location = new URL"
        );
        uses = new EcosystemDependency(scenario, "Process use dependency");
        uses.from(appliesTo);
        uses.to(partialRes);
        uses.describedBy("This dependency indicates that a the dependency uses the specific process in its rule for change management");
        uses2 = new EcosystemDependency(scenario, "Process use dependency");
        uses2.from(appliesTo);
        uses2.to(partialRes);
        uses2.describedBy("This dependency indicates that a the dependency uses the specific process in its rule for change management");
    }
}