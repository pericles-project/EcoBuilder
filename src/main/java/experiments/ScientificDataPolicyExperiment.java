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
import models.CoreModel;
import models.PolicyModel;

import java.util.UUID;

/**
 * Scientific data management policy example
 *
 * @author Fabio Corubolo - University of Liverpool - corubolo@gmail.com
 *         <p/>
 *         This is the description of a scenario based on real storage of data from scientific experiments from nuclear physics
 *         based on its description in an evaluation meeting.
 *         The description covers a policy for data management, and makes usage of both internal and external services.
 *         Furhter details on the scenario are avalaible in the T5.3 google drive folder and can be provided on request
 */
public class ScientificDataPolicyExperiment extends Experiment {

    public ScientificDataPolicyExperiment() {
        super("Scientific-data-policy-experiment");
        //general policy definition
        Policy abstractPolicy = new Policy(scenario, "Experimental data preservation policy");

        abstractPolicy.id(UUID.randomUUID().toString());
        abstractPolicy.name("Policy for management of experimental");
        abstractPolicy.version("0.1");
        abstractPolicy.describedBy(
                "This policy is expressing a guideline for the correct management and preservation of experimental results. ");
        abstractPolicy.intention("The purpose of this policy is to guarantee that experimental data is safely preserved");
        abstractPolicy.addStatement("Relevant experimental data must be preserved");

        Policy policy = new Policy(scenario, "Experimental data preservation policy");
        policy.id(UUID.randomUUID().toString());
        policy.name("Policy for management of experimental data from physics experiments");
        policy.version("0.1");
        policy.describedBy(
                "This policy is expressing a guideline for the correct management and preservation of experimental results from accellerator experiments.");
        policy.intention("The purpose of this policy is to guarantee that experimental data that passes initial selection will be always preserved in at least 2 copies");
        policy.addStatement("Given experimental data produced " +
                "in experiment X, and Y,  that will pass the initial selection process, " +
                "at least one permanent internal copy (BFNRO) " +
                "and one external copy (held in one or more trusted external organisations) for data that passes initial selection has to be maintained."
        );
        policy.classification("Bit preservation");// SEE reference to the areas for preseravtion policies defined in SCAPE
        Community company = new Community(scenario, "Big Fictional Nuclear Research Organization (BFNRO)");
        policy.hasMandator(company);
        HumanAgent admin = new HumanAgent(scenario, "Research Data Head System Administrator");
        admin.isMemberOf(company);
        policy.responsiblePerson(admin);

        QualityAssurance criterionA = new QualityAssurance(scenario, "Validate internal copies of experimental data X");
        criterionA.setComment("Description: given X, access and validate data consistency of the internal copy of the data", "EN");
        policy.addQACriterion(criterionA);
        QualityAssurance criterionb = new QualityAssurance(scenario, "Validate external copies of experimental data");
        criterionb.setComment("Description: given X, access list of external storage of results; ask holding organisations to validate data consistency of the data", "EN");
        policy.addQACriterion(criterionb);

        // Definition of the high level topic from SCAPE for preservation policies
        // TODO : import http://purl.org/DP/control-policy  and add the relative relation
        HumanAgent georgePolicyManager = new HumanAgent(scenario, "George Black");
        HumanAgent johnTheScientist = new HumanAgent(scenario, "John Smith");
        johnTheScientist.hasRole(new Role(scenario, "Research scientist"));
        johnTheScientist.hasRole(new Role(scenario, "Data producer"));
        georgePolicyManager.hasRole(new Role(scenario, "System administrator"));
        georgePolicyManager.hasRole(new Role(scenario, "Policy manager"));

        Process policyEnforcingProcess = new Process(scenario, "DataStorageProcess");
        DigitalObject experimentalResults = new DigitalObject(scenario, "Results from the experiment (container)");
        DigitalObject rawData = new DigitalObject(scenario, "Raw Results from the experiment");
        DigitalObject calibrationParameters = new DigitalObject(scenario, "Calibration parameters for the experiment");
        DigitalObject selectionParameters = new DigitalObject(scenario, "Data selection parameters for the experiment");
        experimentalResults.hasPart(rawData);
        experimentalResults.hasPart(calibrationParameters);
        experimentalResults.hasPart(selectionParameters);
        experimentalResults.hasAuthor(johnTheScientist);
        policy.constraints(experimentalResults);
        HumanActivity runExperiment = new HumanActivity(scenario, "Experiment run on TBBM (The BigBang Machine) on 1.1.2016", johnTheScientist);

        Community scientist = new Community(scenario, "Scientists");
        policy.responsiblePerson(georgePolicyManager);
        policy.targetCommunity(scientist);
        SoftwareAgent rawDataThresholder = new SoftwareAgent(scenario, "Software that performs initial selection of the data to discart invalid results");
        ServiceInterface tapeStorageServiceInterface = new ServiceInterface(scenario, "The internal tape storage interface");
        TechnicalService tapeStorage = new TechnicalService(scenario, "Tape storage service");
        ServiceInterface externalStorageInterface = new ServiceInterface(scenario, "The external storage interface");
        SoftwareAgent splitter = new SoftwareAgent(scenario, "Software that splits results into slices, for distribution");
        AggregatedProcess storeResults = new AggregatedProcess(scenario, "Process to store the results in the storage areas");
        tapeStorageServiceInterface.providesAccessTo(tapeStorage);
        company.hasMember(johnTheScientist);
        company.hasMember(georgePolicyManager);
        company.owns(experimentalResults);
        storeResults.hasInput(experimentalResults);
        storeResults.hasInput(CoreModel.digitalObject);
        storeResults.addComment("Process that will store the results according to the policy ");
        policy.targetCommunity(scientist);
        policy.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        ImplementationState state = PolicyModel.implementationState.createImplementationState(scenario, ImplementationState.State.IMPLEMENTED);
        state.isStateOf(policy);
        policy.addStatement("Policy should be applied during development");
    }
}
