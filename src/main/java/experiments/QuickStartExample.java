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
import entities.RequirementLevel.ReqLevel;
import entities.Process;
import models.PolicyModel;

/**
 * This is the example which is used at the GitHub quick start guide.
 */
public class QuickStartExample extends Experiment {

    public QuickStartExample() {
        super("Quick-start-example");
        DigitalObject importantFile = new DigitalObject(scenario, "ImportantFile");
        importantFile.version("1.0");
        TechnicalService server = new TechnicalService(scenario, "Server");
        ServiceInterface webInterface = new ServiceInterface(scenario, "WebInterface");
        server.hasPart(importantFile);
        server.hasPart(webInterface);
        webInterface.providesAccessTo(importantFile);

        Community company = new Community(scenario, "Company");
        Community artists = new Community(scenario, "Artists");
        company.owns(server);
        HumanAgent admin = new HumanAgent(scenario, "SystemAdministrator");
        HumanAgent artist = new HumanAgent(scenario, "Artist");
        artist.isMemberOf(artists);
        admin.isMemberOf(company);
        Role dataCreator = new Role(scenario, "DataCreators");
        dataCreator.describedBy("Persons who create Digital Objects");
        artist.hasRole(dataCreator);
        importantFile.hasAuthor(artist);

        Policy policy = new Policy(scenario, "AccessibilityPolicy");
        policy.describedBy("All important files should be "
                + "accessible via the web interface.");
        QualityAssurance accessibility = new QualityAssurance(scenario, "ServerAccessible");
        QualityAssurance availability = new QualityAssurance(scenario, "FileAvailable");
        policy.addQACriterion(accessibility);
        policy.addQACriterion(availability);
        policy.hasMandator(company);
        policy.responsiblePerson(admin);
        policy.hasRequirementsLevel(ReqLevel.MUST);
        ImplementationState state = PolicyModel.implementationState.createImplementationState(scenario, ImplementationState.State.IMPLEMENTED);
        state.isStateOf(policy);
        policy.constraints(importantFile);
        policy.constraints(webInterface);
        policy.constraints(server);

        AggregatedProcess aggProcess = new AggregatedProcess(scenario,
                "AccessibilityEnsurance");
        Process testServerAccessibility = new Process(scenario,
                "TestServerAccessibility");
        Process testFileAvailability = new Process(scenario,
                "TestFileAvailability");
        Process startServer = new Process(scenario, "StartServer");
        Process copyFile = new Process(scenario, "CopyFile");
        testServerAccessibility.hasLink("tests/serverAccessibility");
        startServer.runsOn(server);
        copyFile.hasInput(importantFile);
        aggProcess.hasSubProcess(testServerAccessibility);
        aggProcess.hasSubProcess(testFileAvailability);
        aggProcess.hasSubProcess(startServer);
        aggProcess.hasSubProcess(copyFile);
        policy.isEnforcedBy(aggProcess);

        Policy maintenancePolicy = new Policy(scenario,
                "MaintenanceWork");
        maintenancePolicy.describedBy("For server maintenance it could be necessary "
                        + "to shut the server down for a short length of time.");
        policy.conflictDetectedWith(maintenancePolicy);

        DigitalObject artBook = new DigitalObject(scenario, "ArtBook");
        DigitalObject introduction = new DigitalObject(scenario,
                "Introduction");
        artBook.hasPart(introduction);
        artBook.hasPart(importantFile);
    }
}
