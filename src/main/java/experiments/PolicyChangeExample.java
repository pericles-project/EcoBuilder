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
 * VERSION 1
 *
 * This is an example initialisation of an Ecosystem Model created with the Ecosystem Modelling Component.
 * It models the Policy Change Scenario, as described here:
 * https://projects.gwdg.de/projects/pericles_eu/wiki/WP6-Test_PolicyChange
 *
 * 28.07.2015
 */
package experiments;

import models.CoreModel;
import entities.AggregatedProcess;
import entities.Community;
import entities.DigitalObject;
import entities.HumanActivity;
import entities.HumanAgent;
import entities.Policy;
import entities.Process;
import entities.ProcessForEntityValidation;
import entities.ProcessForModelValidation;
import entities.ProcessForTransformation;
import entities.ServiceInterface;
import entities.SoftwareAgent;
import entities.TechnicalService;

/**
 * This example encompasses a fictive scenario from the arts domain, in which a policy changes.
 */
public class PolicyChangeExample extends Experiment {
    /* Ecosystem Communities */
    private Community tate;
    private Community websiteVisitors;
    private HumanAgent policyManager;
    private HumanAgent websiteAuthor;
    private HumanAgent visitor;
    /* Technical Ecosystem Infrastructure */
    private TechnicalService policyStore;
    private TechnicalService archive;
    private TechnicalService webServer;
    private SoftwareAgent policyEditor;
    private SoftwareAgent MICE;
    private SoftwareAgent processCompiler;
    private ServiceInterface editorInterface;
    private ServiceInterface MICEInterface;
    private ServiceInterface website;
    private ServiceInterface uploadForm;
    private DigitalObject tateLogo;
    private DigitalObject ownershipAnnotation;
    /* Ecosystem Policy Management */
    private Policy polAv1;
    private AggregatedProcess processTriple;
    private ProcessForModelValidation proAEIAv1;
    private ProcessForEntityValidation proEPVAv1;
    private AggregatedProcess proAAv1;
    private ProcessForTransformation addOA;
    private ProcessForTransformation save;
    private Policy polAv2;
    private ProcessForEntityValidation logoCheck;
    private AggregatedProcess proEPVAv2;
    private Process addLogo;
    private AggregatedProcess proAAv2;
    private AggregatedProcess processTriple2;
    /* Ecosystem Change Management */
    private HumanActivity policyChangeActivity;
    private HumanActivity checkAffectionsWithMICE;
    private HumanActivity changeAffectedProcesses;

    public PolicyChangeExample() {
        super("Policy-change-example");
        /* Start state: */
        scenarioCommunities();
        technicalEcosystemInfrastructure();
        scenarioPolicyManagement();
        scenarioChangeManagement();
        /* Run the scenario: */
        policyChange();
        processChange();
    }

    private void scenarioCommunities() {
        tate = new Community(scenario, "Tate");
        websiteVisitors = new Community(scenario, "Website Visitors");
        policyManager = new HumanAgent(scenario, "Policy Manager");
        websiteAuthor = new HumanAgent(scenario, "Website Author");
        visitor = new HumanAgent(scenario, "Visitor");
        policyManager.isMemberOf(tate);
        websiteAuthor.isMemberOf(tate);
        visitor.isMemberOf(websiteVisitors);
    }

    private void technicalEcosystemInfrastructure() {
        policyStore = new TechnicalService(scenario, "Policy Store");
        archive = new TechnicalService(scenario, "Archive System");
        webServer = new TechnicalService(scenario, "Web Server");
        policyEditor = new SoftwareAgent(scenario, "Policy Editor");
        MICE = new SoftwareAgent(scenario, "MICE");
        processCompiler = new SoftwareAgent(scenario, "Process Compiler");
        editorInterface = new ServiceInterface(scenario, "Editor Interface");
        website = new ServiceInterface(scenario, "Website");
        uploadForm = new ServiceInterface(scenario, "Upload Form");
        MICEInterface = new ServiceInterface(scenario, "MICE Inteface");
        policyEditor.partOf(policyStore);
        editorInterface.partOf(policyStore);
        editorInterface.providesAccessTo(policyEditor);
        MICE.partOf(webServer);
        MICEInterface.providesAccessTo(MICE);
        processCompiler.partOf(webServer);
        website.partOf(webServer);
        uploadForm.partOf(webServer);
        website.isUsedBy(websiteVisitors);
        uploadForm.isUsedBy(websiteAuthor);
        MICEInterface.isUsedBy(policyManager);
        webServer.accessedVia(website);
        tateLogo = new DigitalObject(scenario, "TateLogo");
        ownershipAnnotation = new DigitalObject(scenario,
                "Ownership Annotation");
        tate.owns(policyStore);
        tate.owns(archive);
        tate.owns(website);
        tate.owns(tateLogo);
        tate.owns(ownershipAnnotation);
    }

    private void scenarioPolicyManagement() {
        polAv1 = new Policy(scenario, "Pol-A.v1");
        polAv1.addStatement(" All photos on the website must have an ownership annotation.");
        polAv1.responsiblePerson(policyManager);
        polAv1.version("1.0");

        processTriple = new AggregatedProcess(scenario, "ProcessTriple");
        proAEIAv1 = new ProcessForModelValidation(scenario, "ProAEI-A.v1");
        proAEIAv1.describedBy("Take all the photos published on the website.");
        proAEIAv1.version("1.0");
        proEPVAv1 = new ProcessForEntityValidation(scenario, "ProEPV-A.v1");
        proEPVAv1.describedBy("Check if the photo has ownership annotation.");
        proEPVAv1.hasInput(CoreModel.digitalObject);
        proAAv1 = new AggregatedProcess(scenario, "ProA-A.v1");
        proAAv1.describedBy("Add ownsership annotation to the photo and save the resulting photo on the website.");
        proAAv1.version("1.0");
        addOA = new ProcessForTransformation(scenario,
                "Add Ownership Annotation");
        addOA.hasInput(ownershipAnnotation);
        addOA.hasInput(CoreModel.digitalObject);
        addOA.describedBy("Add ownership annotation to photo.");
        save = new ProcessForTransformation(scenario, "Save");
        save.describedBy("Save photo on website.");
        save.hasInput(CoreModel.digitalObject);
        save.hasInput(uploadForm);

        proAAv1.hasSubProcess(addOA);
        proAAv1.hasSubProcess(save);
        processTriple.hasSubProcess(proAEIAv1);
        processTriple.hasSubProcess(proEPVAv1);
        processTriple.hasSubProcess(proAAv1);
        polAv1.isEnforcedBy(processTriple);
    }

    private void scenarioChangeManagement() {
        policyChangeActivity = new HumanActivity(scenario,
                "PolicyChangeActivity");
        checkAffectionsWithMICE = new HumanActivity(scenario,
                "CheckAffectionsWithMICEActivity");
        changeAffectedProcesses = new HumanActivity(scenario,
                "ChangeAffectedProcesses");
    }

    private void policyChange() {
        policyManager.execute(policyChangeActivity);
        policyChangeActivity.start();
        polAv2 = new Policy(scenario, "Pol-A.v2");
        polAv2.addStatement("All photos on the website must have an ownership annotation and the Tate logo.");
        polAv2.responsiblePerson(policyManager);
        polAv2.version("2.0");
        polAv2.replaces(polAv1);
        policyChangeActivity.stop();
    }

    private void processChange() {
        policyManager.execute(checkAffectionsWithMICE);
        checkAffectionsWithMICE.start();
        checkAffectionsWithMICE.stop();
        policyManager.execute(changeAffectedProcesses);
        changeAffectedProcesses.start();
        logoCheck = new ProcessForEntityValidation(scenario, "Logo check");
        logoCheck.describedBy("Check if the photo has Tate logo attached.");
        proEPVAv2 = new AggregatedProcess(scenario, "ProEPV-A.v2");
        proEPVAv2
                .describedBy("Check if the photo has ownership annotation and Tate logo attached.");
        proEPVAv2.hasInput(CoreModel.digitalObject);
        proEPVAv2.hasSubProcess(proEPVAv1);
        proEPVAv2.hasSubProcess(logoCheck);
        addLogo = new Process(scenario, "Add Logo");
        addLogo.describedBy("Adds a logo to a photo.");
        addLogo.hasInput(tateLogo);
        addLogo.hasInput(CoreModel.digitalObject);
        addLogo.hasOutput(CoreModel.digitalObject);
        proAAv2 = new AggregatedProcess(scenario, "ProA-A.v2");
        proAAv1.describedBy("Add ownsership annotation and logo to the photo and save the resulting photo on the website.");
        proAAv2.version("2.0");
        proAAv2.hasSubProcess(addOA);
        proAAv2.hasSubProcess(addLogo);
        proAAv2.hasSubProcess(save);
        processTriple2 = new AggregatedProcess(scenario, "ProcessTriplev2");
        processTriple2.hasSubProcess(proAEIAv1);
        processTriple2.hasSubProcess(proEPVAv2);
        processTriple2.hasSubProcess(proAAv2);
        polAv2.isEnforcedBy(processTriple2);
        changeAffectedProcesses.stop();
    }
}
