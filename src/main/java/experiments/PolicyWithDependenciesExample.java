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
import entities.*;
import entities.Process;
import models.PolicyModel;

/**
 * This is a slightly changed version of the Policy Change example.
 * It has a focus on a more detailed description of the Policy, and
 * introduces Dependencies, instead of Processes.
 */
public class PolicyWithDependenciesExample extends Experiment {
    /* Ecosystem Communities */
    private Community museum;
    private Community websiteVisitors;
    private HumanAgent policyManager;
    private HumanAgent websiteAuthor;
    private HumanAgent visitor;
    /* Technical Ecosystem Infrastructure */
    private TechnicalService policyStore;
    private TechnicalService archive;
    private TechnicalService webServer;
    private SoftwareAgent policyEditor;
    private ServiceInterface editorInterface;
    private ServiceInterface website;
    private ServiceInterface uploadForm;
    private DigitalObject museumLogo;
    private DigitalObject ownershipAnnotation;
    private DigitalObject image;
    /* Ecosystem Policy Management */
    private Policy badPolicy;
    private Policy policy_V1;
    private Policy policy_V2;
    private AggregatedProcess processOAConfirm;
    private ProcessForEntityValidation logoCheck;
    private Process addLogo;
    private ProcessForTransformation addOA;
    private ProcessForTransformation save;
    private AggregatedProcess processOALogoConfirm;
    private QualityAssurance currentState;
    private QualityAssurance uploadState;
    /* Ecosystem Change Management */
    private HumanActivity policyChangeActivity;
    private HumanActivity changeAffectedProcesses;
    /* Dependencies */
    private Significance websiteSignificance;
    private EcosystemDependency websiteDependency;
    private EcosystemDependency webserverDependency;

    public PolicyWithDependenciesExample() {
        super("Policy-with-dependency-example");
        /* Start state: */
        createCommunities();
        createTechnicalInfrastructure();
        createPolicyVersion1();
        /* Run the scenario: */
        changePolicy();
        changeDependentProcesses();
        createDependencies();
    }

    private void createCommunities() {
        museum = new Community(scenario, "Museum");
        websiteVisitors = new Community(scenario, "Website Visitors");
        policyManager = new HumanAgent(scenario, "Policy Manager");
        websiteAuthor = new HumanAgent(scenario, "Website Author");
        visitor = new HumanAgent(scenario, "Visitor");
        policyManager.isMemberOf(museum);
        websiteAuthor.isMemberOf(museum);
        visitor.isMemberOf(websiteVisitors);
    }

    private void createTechnicalInfrastructure() {
        policyStore = new TechnicalService(scenario, "Policy Store");
        archive = new TechnicalService(scenario, "Archive System");
        webServer = new TechnicalService(scenario, "Web Server");
        policyEditor = new SoftwareAgent(scenario, "Policy Editor");
        editorInterface = new ServiceInterface(scenario, "Editor Interface");
        website = new ServiceInterface(scenario, "Website");
        uploadForm = new ServiceInterface(scenario, "Upload Form");
        policyEditor.partOf(policyStore);
        editorInterface.partOf(policyStore);
        editorInterface.providesAccessTo(policyEditor);
        website.partOf(webServer);
        uploadForm.partOf(webServer);
        website.isUsedBy(websiteVisitors);
        uploadForm.isUsedBy(websiteAuthor);
        webServer.accessedVia(website);
        museumLogo = new DigitalObject(scenario, "MuseumLogo");
        ownershipAnnotation = new DigitalObject(scenario,
                "Ownership Annotation");
        image = new DigitalObject(scenario, "Image on webiste");
        museum.owns(policyStore);
        museum.owns(archive);
        museum.owns(website);
        museum.owns(museumLogo);
        museum.owns(ownershipAnnotation);
        museum.owns(image);
    }

    private void createPolicyVersion1() {
        policy_V1 = new Policy(scenario, "PublishImagePolicy_V1");
        policy_V1.id("108137120-98503-13091");
        policy_V1.name("Ownership at published photos");
        policy_V1.describedBy("This policy restricts the publication of images on the museum website." +
                "Images have to be supplemented by an ownership annotation, before they are uploaded to the website.");
        policy_V1.intention("The intention of this policy is to improve the understanding of the image ownership.");
        policy_V1.addStatement(" All images on the website must have an ownership annotation.");
        policy_V1.responsiblePerson(policyManager);
        policy_V1.version("1.0");
        currentState = new QualityAssurance(scenario, "Website Has Valid State");
        uploadState = new QualityAssurance(scenario, "Image Has Valid State Before Upload");
        policy_V1.addQACriterion(currentState);
        policy_V1.addQACriterion(uploadState);
        policy_V1.classification("Content Handling");
        policy_V1.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        processOAConfirm = new AggregatedProcess(scenario, "Add OA and save image");
        processOAConfirm.describedBy("Add ownsership annotation to the photo and save the resulting image on the website.");
        processOAConfirm.version("1.0");
        addOA = new ProcessForTransformation(scenario,
                "Add Ownership Annotation");
        addOA.hasInput(ownershipAnnotation);
        addOA.hasInput(CoreModel.digitalObject);
        addOA.describedBy("Add ownership annotation to photo.");
        save = new ProcessForTransformation(scenario, "Save");
        save.describedBy("Save photo on website.");
        save.hasInput(CoreModel.digitalObject);
        save.hasInput(uploadForm);
        processOAConfirm.hasSubProcess(addOA);
        processOAConfirm.hasSubProcess(save);
        policy_V1.isEnforcedBy(processOAConfirm);
        ImplementationState state = PolicyModel.implementationState.createImplementationState(scenario, ImplementationState.State.IMPLEMENTED);
        state.isStateOf(policy_V1);
        policy_V1.addStatement("The policy constrains the current website state and the state of new content.");
        policy_V1.constraints(website);
        policy_V1.constraints(image);
        badPolicy = new Policy(scenario, "Images Untouched");
        badPolicy.describedBy("Images must not be altered.");
        badPolicy.hasRequirementsLevel(RequirementLevel.ReqLevel.OPTIONAL);
        policy_V1.conflictDetectedWith(badPolicy);
        policy_V1.hasConflictIDAttribute("Policy Conflict");
    }

    private void changePolicy() {
        policyChangeActivity = new HumanActivity(scenario,
                "PolicyChangeActivity");
        changeAffectedProcesses = new HumanActivity(scenario,
                "ChangeAffectedProcesses");
        policyManager.execute(policyChangeActivity);
        policyChangeActivity.start();
        policy_V2 = new Policy(scenario, "PublishImagePolicy_V2");
        policy_V2.describedBy("This is the next version of the image publication policy. Additionally to the ownership" +
                " annotation each image on the website needs an attached logo!");
        policy_V2.addStatement("All photos on the website must have an ownership annotation and the Museum logo.");
        policy_V2.responsiblePerson(policyManager);
        policy_V2.version("2.0");
        policy_V2.replaces(policy_V1);
        policyChangeActivity.stop();
    }

    private void changeDependentProcesses() {
        policyManager.execute(changeAffectedProcesses);
        changeAffectedProcesses.start();
        logoCheck = new ProcessForEntityValidation(scenario, "Logo check");
        logoCheck.describedBy("Check if the photo has Museum logo attached.");
        addLogo = new Process(scenario, "Add Logo");
        addLogo.describedBy("Adds a logo to a photo.");
        addLogo.hasInput(museumLogo);
        addLogo.hasInput(CoreModel.digitalObject);
        addLogo.hasOutput(CoreModel.digitalObject);
        processOALogoConfirm = new AggregatedProcess(scenario, "Publish image on webiste");
        processOALogoConfirm.describedBy("Add ownsership annotation and logo to the photo and save the resulting photo on the website.");
        processOALogoConfirm.version("2.0");
        processOALogoConfirm.hasSubProcess(addOA);
        processOALogoConfirm.hasSubProcess(addLogo);
        processOALogoConfirm.hasSubProcess(save);
        policy_V2.isEnforcedBy(processOALogoConfirm);
        changeAffectedProcesses.stop();
    }

    private void createDependencies() {
        websiteSignificance = new Significance(scenario, "Significance Of The Website", website, websiteVisitors);
        websiteSignificance.hasValue(10);
        websiteSignificance.addComment("The website is significant for the website visitors");
        websiteDependency = new EcosystemDependency(scenario, "Website Dependency", save, website);
        websiteDependency.describedBy("The upload Process is dependent on the website.");

        webserverDependency = new EcosystemDependency(scenario, "Webserver Dependency", website, webServer);
        webserverDependency.describedBy("The website is dependent on the webserver.");
    }
}
