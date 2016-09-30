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

public class CERNExample extends Experiment {
    /**
     * <h1>Policy driven Digital Ecosystem (DE) modelling based on the CERN scenario</h1>
     * This example is an implementation of the CERN example, as described in PERICLES deliverable 5.3.
     * It further exemplifies policy based modelling.
     */
    public CERNExample() {
        super("CERN-example");
        // Policy driven modelling starts by defining the most abstract high level policies of the DE:
        GuidancePolicyRights confidentialPolicy = new GuidancePolicyRights(scenario, "Confidentiality");
        confidentialPolicy.describedBy("Confidential data must not be stored outside the organisation.");
        confidentialPolicy.id("0");

        GuidancePolicyBitPreservation preservationPolicy = new GuidancePolicyBitPreservation(scenario, "Preservation");
        preservationPolicy.describedBy("Relevant experimental data must be preserved.");
        preservationPolicy.id("1");

        // The derived policies are defined, and the derivations declared:
        DigitalPreservationPolicy experimentalPreservationPolicy = new DigitalPreservationPolicy(scenario,
                "Public Data Preservation");
        experimentalPreservationPolicy.describedBy("Experimental data produced in non confidential experiments must be " +
                "preserved in at least X internal copies and in Y external (in trusted external organisations) copies.");
        experimentalPreservationPolicy.id("2");
        experimentalPreservationPolicy.derivedFrom(preservationPolicy);

        Policy internalDataPolicy = new Policy(scenario, "Internal Data Preservation");
        internalDataPolicy.describedBy("Data from experiment type VSE must be preserved " +
                "in X internal copies, and must not be shared with external organisations");
        internalDataPolicy.id("3");
        internalDataPolicy.derivedFrom(preservationPolicy);
        internalDataPolicy.derivedFrom(confidentialPolicy);

        Policy externalCopiesPolicy = new Policy(scenario, "External Copies Policy");
        externalCopiesPolicy.describedBy("Maintain Y external copies of the experimental data.");
        externalCopiesPolicy.id("4");
        externalCopiesPolicy.derivedFrom(experimentalPreservationPolicy);

        Policy internalCopiesPolicy = new Policy(scenario, "Internal Copies");
        internalCopiesPolicy.describedBy("Maintain X internal copies of of data from experiment EX.");
        internalCopiesPolicy.id("5");
        internalCopiesPolicy.derivedFrom(internalDataPolicy);
        internalCopiesPolicy.derivedFrom(experimentalPreservationPolicy);

        GuidancePolicyAccess usabilityPolicy = new GuidancePolicyAccess(scenario, "Usability Policy");
        usabilityPolicy.describedBy("Experimental data must be usable by its user communities.");
        usabilityPolicy.id("6");

        GuidancePolicyAccess metadataPolicy = new GuidancePolicyAccess(scenario, "Metadata Indexing Policy");
        metadataPolicy.describedBy("Files will be indexed with relevant metadata");

        GuidancePolicyAccess availabilityPolicy = new GuidancePolicyAccess(scenario, "Availability Policy");
        availabilityPolicy.describedBy("Project results will be made public after 4 years.");

        // Define the quality assurance for the policies:
        QualityAssurance checksums = new QualityAssurance(scenario, "Checksums");
        checksums.describedBy("Checksum of data must be valid.");
        checksums.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        checksums.assuresQualityOf(externalCopiesPolicy);

        QualityAssurance tapeDustSensorsQA = new QualityAssurance(scenario, "Tape Dust Sensors State");
        tapeDustSensorsQA.describedBy("The tape dust sensors must be in a valid state");
        tapeDustSensorsQA.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        tapeDustSensorsQA.assuresQualityOf(internalCopiesPolicy);

        QualityAssurance tapeDustSensorsCheckSums = new QualityAssurance(scenario, "Tape Data Check Sums");
        tapeDustSensorsCheckSums.describedBy("Every year validate tape data checksums.");
        tapeDustSensorsCheckSums.derivedFrom(checksums);
        tapeDustSensorsCheckSums.derivedFrom(tapeDustSensorsQA);

        QualityAssuranceMethod sampleValidation = new QualityAssuranceMethod(scenario, "Check Confidentiality");
        sampleValidation.describedBy("Search samples of confidential data in external institutions and on the internet");
        sampleValidation.isImplementationOf(confidentialPolicy);

        // Implementation of quality assurance definitions:
        QualityAssuranceMethod checksumVerification = new QualityAssuranceMethod(scenario, "Checksum Calculation");
        checksumVerification.describedBy("Validate data checksums for external organisation slices.");
        checksumVerification.isImplementationOf(checksums);

        QualityAssuranceMethod verifyTapeDustCheckSums = new QualityAssuranceMethod(scenario, "Verify Tape Data Check Sums");
        verifyTapeDustCheckSums.describedBy("Verify the check sum of the tape data.");
        verifyTapeDustCheckSums.isImplementationOf(tapeDustSensorsCheckSums);

        QualityAssuranceMethod copyNumberCheck = new QualityAssuranceMethod(scenario, "Copy Number Check");
        copyNumberCheck.describedBy("Verify that there are X copies of the data in the tape libraries");
        copyNumberCheck.isImplementationOf(internalCopiesPolicy);

        QualityAssuranceMethod tapeDustSensorsVerification = new QualityAssuranceMethod(scenario, "Tape Dust Sensors Verification");
        tapeDustSensorsVerification.describedBy("Check if tape dust sensors reveal issues with tapes;" +
                " the sensors will report if there is an issue with the quality of the air in the tape libraries, that could damage existing tapes.");
        // see slides: https://indico.cern.ch/event/346931/contributions/817854/attachments/684698/940516/dust_sensor_HEPIX.pdf");
        tapeDustSensorsVerification.isImplementationOf(tapeDustSensorsQA);

        QualityAssuranceMethod continuousIntegration = new QualityAssuranceMethod(scenario, "Continuous Integration");
        continuousIntegration.describedBy("Continuous Integration ensures that the external data remains usable " +
                "by the community");
        continuousIntegration.isImplementationOf(usabilityPolicy);

        QualityAssuranceMethod changeManagement = new QualityAssuranceMethod(scenario, "Assure Up-To-Date Processing");
        changeManagement.describedBy("Assures that always the newest version of the calculation data is processed.");
        changeManagement.isImplementationOf(usabilityPolicy);

        // Model the entities which are affected by the policies:
        // Digital Objects:
        DigitalObject rawDatasets = new DigitalObject(scenario, "Raw Datasets");
        rawDatasets.describedBy("The raw and unprocessed data.");

        DigitalObject filteredDatasets = new DigitalObject(scenario, "Valid Raw Datasets");
        filteredDatasets.describedBy("The raw, unprocessed and valid data. Invalid data is filtered out.");
        filteredDatasets.derivedFrom(rawDatasets);

        DigitalObject datasets = new DigitalObject(scenario, "Datasets");
        datasets.describedBy("Intermediate and final datasets of the experiment.");
        datasets.derivedFrom(filteredDatasets);

        DigitalObject filters = new DigitalObject(scenario, "Filters");
        filters.describedBy("The data that is necessary to filter experimental data.");

        DigitalObject tapeCatalog = new DigitalObject(scenario, "Tape Catalog");
        tapeCatalog.describedBy("A catalog of all internal data which is stored on the tapes. This data is necessary to locate where data is stored on tape");

        DigitalObject externalCatalog = new DigitalObject(scenario, "External Data Catalog");
        externalCatalog.describedBy("Catalog data for external data.");

        DigitalObject vmImage = new DigitalObject(scenario, "VM Image");
        vmImage.describedBy("An image of a virtual machine for the processing and calculation software which is " +
                "needed for the scientific experiment.");

        DigitalObject instrumentInformation = new DigitalObject(scenario, "Instrument Information");
        instrumentInformation.describedBy("Information about the instruments of the experiment, as calibration " +
                "information, and and environment description.");
        instrumentInformation.partOf(vmImage);

        DigitalObject environmentInformation = new DigitalObject(scenario, "Processing Environment Information");
        environmentInformation.describedBy("A description of the experiments environment, and information about " +
                "the instrument SW.");
        environmentInformation.partOf(instrumentInformation);

        DigitalObject calibrationData = new DigitalObject(scenario, "Calibration Data");
        calibrationData.describedBy("The parameters used to calibrate the instrument for the experiment.");
        calibrationData.partOf(environmentInformation);

        DigitalObject environmentDescription = new DigitalObject(scenario, "Processing Environment Description");
        environmentDescription.describedBy("A description of the processing environment");
        environmentDescription.partOf(environmentInformation);

        DigitalObject instrumentDocumentation = new DigitalObject(scenario, "Instrument Documentation");
        instrumentDocumentation.describedBy("The documentation of the instrument.");
        instrumentDocumentation.partOf(instrumentInformation);

        DigitalObject calibrationTestResults = new DigitalObject(scenario, "Calibration Test Results");
        calibrationTestResults.describedBy("The results of the calibration test executed to verify the calibration " +
                "parameters of the instrument.");
        calibrationTestResults.partOf(instrumentInformation);

        DigitalObject dataSlice = new DigitalObject(scenario, "Data Slice");
        dataSlice.describedBy("A 20% data slice of the original dataset for better handling.");

        // Technical Services:
        TechnicalService departementServer = new TechnicalService(scenario, "Departement Server");
        departementServer.describedBy("The departements development server.");

        TechnicalService integrationServer = new TechnicalService(scenario, "Continuous Integration Server");
        integrationServer.describedBy("A Jenkins server for continuous integration.");
        integrationServer.partOf(departementServer);

        TechnicalService gitService = new TechnicalService(scenario, "Versioning Repository");
        gitService.describedBy("A git versioning repository.");
        gitService.partOf(departementServer);

        TechnicalService tapeStorage = new TechnicalService(scenario, "Tape Storage");
        tapeStorage.describedBy("The Server which provides the storage for tapes.");

        TechnicalService datacenterStorage = new TechnicalService(scenario, "Data Center Storage");
        datacenterStorage.describedBy("ERMR data center storage service.");

        // Software and Hardware Agents:
        SoftwareAgent calibrationSW = new SoftwareAgent(scenario, "Calibration and processing SW");
        calibrationSW.describedBy("The Software for the calibration of the instruments, and for the processing of the " +
                "experimental data.");
        calibrationSW.runsOn(integrationServer);

        HardwareAgent instrument = new HardwareAgent(scenario, "Instrument");
        instrument.describedBy("Instrument for Biological Nanoimaging, Macromolecular crystallography.");

        HardwareAgent tapeDustSensors = new HardwareAgent(scenario, "Tape Dust Sensors");
        tapeDustSensors.describedBy("The tape dust sensors.");

        // Technical Infrastructure:
        ServiceInterface tapeStorageInterface = new ServiceInterface(scenario, "Tape Storage Interface");
        tapeStorageInterface.describedBy("The user interface of the tape storage.");
        tapeStorage.hasPart(tapeStorageInterface);
        tapeStorageInterface.providesAccessTo(tapeCatalog);
        tapeStorageInterface.providesAccessTo(filteredDatasets);

        ServiceInterface instrumentInterface = new ServiceInterface(scenario, "Instrument Interface");
        instrumentInterface.describedBy("The user interface of the instruments.");
        instrumentInterface.providesAccessTo(instrument);
        instrumentInterface.providesAccessTo(instrumentInformation);

        // Communities:
        Community internalScientists = new Community(scenario, "Internal Scientists");
        internalScientists.describedBy("The scientists which are part of the institution.");

        Community externalScientists = new Community(scenario, "External Scientists");
        externalScientists.describedBy("Scientists which are not part of the institution.");

        // Link the entities to the policies, and to the QA definitions
        confidentialPolicy.constraints(datasets); // ID 0
        confidentialPolicy.constraints(internalScientists);
        preservationPolicy.constraints(datasets); // ID 1
        experimentalPreservationPolicy.constraints(datasets); // ID 2
        internalDataPolicy.constraints(tapeCatalog); // ID 3
        internalDataPolicy.constraints(datasets);
        externalCopiesPolicy.constraints(externalCatalog); // ID 4
        externalCopiesPolicy.constraints(dataSlice);
        externalCopiesPolicy.constraints(gitService);
        externalCopiesPolicy.addQACriterion(checksums);
        internalCopiesPolicy.constraints(tapeCatalog); // ID 5
        internalCopiesPolicy.addQACriterion(tapeDustSensorsCheckSums);
        usabilityPolicy.constraints(integrationServer); // ID 6
        usabilityPolicy.targetCommunity(externalScientists);
        metadataPolicy.constraints(filteredDatasets);
        metadataPolicy.constraints(dataSlice);
        availabilityPolicy.constraints(filteredDatasets);

        // Model processes:
        Process experiment = new Process(scenario, "Experiment");
        experiment.describedBy("The modelled scientific experiment.");
        experiment.hasOutput(rawDatasets);

        Process filtering = new Process(scenario, "Filtering");
        filtering.describedBy("This process executes algorithms to identify and filter out invalid raw data.");
        filtering.hasInput(rawDatasets);
        filtering.hasOutput(filteredDatasets);

        Process moveData = new Process(scenario, "Move Data");
        moveData.describedBy("Move data from experiment equipment to computer center.");
        moveData.hasInput(filteredDatasets);
        moveData.hasInput(instrument);
        moveData.hasInput(datacenterStorage);
        moveData.hasOutput(filteredDatasets);

        Process calibrateData = new Process(scenario, "Calibrate Data");
        calibrateData.describedBy("Calibrate and process data.");
        calibrateData.hasInput(instrumentInformation);
        calibrateData.hasInput(calibrationSW);
        calibrateData.hasInput(filteredDatasets);
        calibrateData.hasOutput(datasets);

        Process copyData = new Process(scenario, "Copy Data");
        copyData.describedBy("Copy data to internal tape repository.");
        copyData.isImplementationOf(internalCopiesPolicy);
        copyData.isImplementationOf(preservationPolicy);
        copyData.hasInput(tapeStorage);
        copyData.hasInput(tapeCatalog);
        copyData.hasOutput(tapeCatalog);

        Process manualMove = new Process(scenario, "Manual Move");
        manualMove.describedBy("Manual: Move data to new tapes (every 3 years).");

        Process createVM = new Process(scenario, "Create VMs");
        createVM.describedBy("Create virtual machines based on the processing environment and used software.");
        createVM.isExecutedBy(calibrationSW);
        createVM.hasInput(instrumentInformation);
        createVM.hasOutput(vmImage);
        createVM.isImplementationOf(usabilityPolicy);

        Process splitData = new Process(scenario, "Split Data");
        splitData.describedBy("Split data in 20% parts.");
        splitData.isImplementationOf(externalCopiesPolicy);
        splitData.hasInput(filteredDatasets);
        splitData.hasOutput(dataSlice);

        Process shipData = new Process(scenario, "Ship Data");
        shipData.describedBy("Send data to external organisations.");
        shipData.isImplementationOf(externalCopiesPolicy);
        shipData.hasInput(dataSlice);
        shipData.hasOutput(externalCatalog);
        internalDataPolicy.constraints(shipData);

        // Linking QA methods to verified entities:
        checksumVerification.verifies(dataSlice);
        verifyTapeDustCheckSums.verifies(tapeDustSensors);
        copyNumberCheck.verifies(tapeCatalog);
        copyNumberCheck.verifies(tapeStorage);
        copyNumberCheck.verifies(filteredDatasets);
        tapeDustSensorsVerification.verifies(tapeDustSensors);
        tapeDustSensorsVerification.verifies(tapeStorage);
        continuousIntegration.verifies(integrationServer);
        changeManagement.verifies(calibrationData);
        changeManagement.verifies(instrumentInformation);
        changeManagement.verifies(calibrateData);

        // Additional Dependencies:
        EcosystemDependency dependency = new EcosystemDependency(scenario, "Scientist Dependency");
        dependency.from(usabilityPolicy);
        dependency.to(externalScientists);
        dependency.describedBy("External scientists are needed to verify the understandability of the data.");

        // Analysis part (scenarios, purposes, annotations, weighted edges for model analysis):
        Scenario processingScenario = new Scenario(scenario, "Processing Scenario");
        Scenario preservationScenario = new Scenario(scenario, "Preservation Scenario");
        // Both scenarios include all versions of the experimental data:
        processingScenario.hasPart(rawDatasets);
        preservationScenario.hasPart(rawDatasets);
        processingScenario.hasPart(filteredDatasets);
        preservationScenario.hasPart(filteredDatasets);
        processingScenario.hasPart(datasets);
        preservationScenario.hasPart(datasets);
        // The processing scenario has a focus on the data processing:
        processingScenario.hasPart(experiment);
        processingScenario.hasPart(filtering);
        processingScenario.hasPart(moveData);
        processingScenario.hasPart(calibrateData);
        processingScenario.hasPart(filters);
        processingScenario.hasPart(gitService);
        processingScenario.hasPart(calibrationData);
        processingScenario.hasPart(metadataPolicy);
        processingScenario.hasPart(vmImage);
        processingScenario.hasPart(instrumentInformation);
        processingScenario.hasPart(environmentInformation);
        processingScenario.hasPart(calibrationData);
        processingScenario.hasPart(environmentDescription);
        processingScenario.hasPart(instrumentDocumentation);
        processingScenario.hasPart(calibrationTestResults);
        processingScenario.hasPart(dataSlice);
        processingScenario.hasPart(calibrationSW);
        processingScenario.hasPart(instrument);
        processingScenario.hasPart(tapeDustSensors);
        processingScenario.hasPart(tapeStorageInterface);
        processingScenario.hasPart(instrumentInterface);
        processingScenario.hasPart(internalScientists);
        processingScenario.hasPart(externalScientists);
        processingScenario.hasPart(manualMove);
        processingScenario.hasPart(createVM);
        processingScenario.hasPart(splitData);
        processingScenario.hasPart(shipData);

        // The preservation scenario has a focus on the preservation policies and processes:
        preservationScenario.hasPart(preservationPolicy);
        preservationScenario.hasPart(experimentalPreservationPolicy);
        preservationScenario.hasPart(internalDataPolicy);
        preservationScenario.hasPart(externalCopiesPolicy);
        preservationScenario.hasPart(internalCopiesPolicy);
        preservationScenario.hasPart(metadataPolicy);
        preservationScenario.hasPart(checksums);
        preservationScenario.hasPart(tapeDustSensorsQA);
        preservationScenario.hasPart(tapeDustSensorsCheckSums);
        preservationScenario.hasPart(checksumVerification);
        preservationScenario.hasPart(verifyTapeDustCheckSums);
        preservationScenario.hasPart(copyNumberCheck);
        preservationScenario.hasPart(tapeDustSensorsVerification);
        preservationScenario.hasPart(tapeCatalog);
        preservationScenario.hasPart(externalCatalog);
        preservationScenario.hasPart(tapeStorage);
        preservationScenario.hasPart(datacenterStorage);
        preservationScenario.hasPart(internalScientists);

        Scenario confidentialityScenario = new Scenario(scenario, "Confidentiality Scenario");
        confidentialityScenario.hasPart(confidentialPolicy);
        confidentialityScenario.hasPart(internalScientists);
        confidentialityScenario.hasPart(sampleValidation);
        confidentialityScenario.hasPart(rawDatasets);
        confidentialityScenario.hasPart(filteredDatasets);
        confidentialityScenario.hasPart(datasets);

        Scenario usabilityScenario = new Scenario(scenario, "Usability Scenario");
        usabilityScenario.hasPart(usabilityPolicy);
        usabilityScenario.hasPart(externalScientists);
        usabilityScenario.hasPart(internalScientists);
        usabilityScenario.hasPart(integrationServer);
        usabilityScenario.hasPart(continuousIntegration);
        usabilityScenario.hasPart(integrationServer);
        usabilityScenario.hasPart(filteredDatasets);
        usabilityScenario.hasPart(datasets);
        usabilityScenario.hasPart(dataSlice);

        //Define the purposes of the two saver scenarios:
        Purpose processingScenarioPurpose = new Purpose(scenario, "Data Processing");
        processingScenarioPurpose.purposeOf(processingScenario);
        processingScenarioPurpose.describedBy("Investigation of the experimental data processing flow, and analysis " +
                "of the whole processing workflow for optimisation, documentation and problem solving.");

        Purpose preservationScenarioPurpose = new Purpose(scenario, "Preservation");
        preservationScenarioPurpose.purposeOf(preservationScenario);
        preservationScenarioPurpose.describedBy("Investigate the preservation aspects of the CERN example model, " +
                "especially the analysis of the policy compliance, and the identification of preservation related " +
                "problems of the workflows. ");

        // Defining significance values for analysis purposes:
        Significance highPresSig = new Significance(scenario, "High Preservation Significance");
        highPresSig.to(preservationScenarioPurpose);
        highPresSig.from(preservationPolicy);
        highPresSig.from(datasets);
        highPresSig.hasValue(1);

        Significance mediumPresSig = new Significance(scenario, "Normal Preservation Significance");
        mediumPresSig.to(preservationScenarioPurpose);
        mediumPresSig.from(dataSlice);
        mediumPresSig.from(splitData);
        mediumPresSig.hasValue(0.6f);

        Significance lowPresSig = new Significance(scenario, "Low Preservation Significance");
        lowPresSig.to(preservationScenarioPurpose);
        lowPresSig.from(createVM);
        lowPresSig.hasValue(0.2f);

        //Add annotations to the entities:
        Annotation internal = new Annotation(scenario, "@internal");
        Annotation external = new Annotation(scenario, "@external");
        Annotation experimentAnnotation = new Annotation(scenario, "@experiment");
        Annotation parameter1 = new Annotation(scenario, "@parameter1=true");
        Annotation quality = new Annotation(scenario, "@quality=high");
    }
}
