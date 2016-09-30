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
 * This example from the art domain is based on the
 * "Transfer of a Born-Digital Archive Collection to Tate Archive" scenario, as
 * described here:
 * <p/>
 * https://projects.gwdg.de/projects/pericles-public/wiki/TBD_
 */
public class TransferScenario extends Experiment {
    /*
   * ".. collection process of the material necessary for the acquisition of the
   * archive ..."
   */
    public TransferScenario() {
        super("Tate-transfer-to-archive-scenario");
        // Model the initial state of the scenario:
        createStartState();
        // Execute the steps of the collection Process:
        requestArchiveAquisitionFile();
        findAgreement();
        updateFiles();
        metadataExtraction();
        packaging();
        storing();
    }

    private void createStartState() {
        /*
         * Note: The overall collection process would be build together from its
		 * sub-processes by the Model Compiler. At this example we execute the
		 * sub-processes instead, to test the scenario CORE_MODEL entity creation.
		 *
		 * Model the scenario policy and process management:
		 */
        // Note: "fictional" policy
        agreeBeforeAquire.classification("Aquisition Policy");
        agreeBeforeAquire
                .describedBy("Create a legal agreement with the donor before the aquisition of material to the storage.");
        agreeBeforeAquire.isEnforcedBy(findAgreementProcess);
        agreeBeforeAquire.hasMandator(tate);
        agreeBeforeAquire.responsiblePerson(archivist);
        agreeBeforeAquire.hasRequirementsLevel(ReqLevel.SHOULD);
        ImplementationState state = PolicyModel.implementationState.createImplementationState(scenario, ImplementationState.State.IMPLEMENTED);
        state.isStateOf(agreeBeforeAquire);
//                .setLifecycleInformation("Policy should be applied before aquisition.");
        agreeBeforeAquire.version("1.0");
        agreeBeforeAquire.targetCommunity(tate);
        // process:
        AggregatedProcess collectionProcess = new AggregatedProcess(scenario,
                "Collection Process");
        collectionProcess.hasSubProcess(requestProcess);
        collectionProcess.hasSubProcess(findAgreementProcess);
        collectionProcess.hasSubProcess(updateFiles);
        updateFiles.hasSubProcess(updateDigitalDepositeForm);
        updateFiles.hasSubProcess(updateArchiveAcquisitionFile);
        collectionProcess.hasSubProcess(metadataExtraction);
        collectionProcess.hasSubProcess(normalisation);
        collectionProcess.hasSubProcess(packaging);
        collectionProcess.hasSubProcess(storing);
        // Model the scenario community:
        archivist.isMemberOf(tate);
        donor.isMemberOf(artists);
        Role donorRole = new Role(scenario, "ArchiveMaterialDonor");
        donorRole.describedBy("Donor of archive material.");
        donor.hasRole(donorRole);
        Community archivists = new Community(scenario, "Archivists");
        Role archivistRole = new Role(scenario, "archivist");
        archivistRole.describedBy("People who archive material.");
        archivists.hasRole(archivistRole);
        archivist.isMemberOf(archivists);
        // Model the technical scenario infrastructure:
        TechnicalService archiveInfrastructure = new TechnicalService(
                scenario, "ArchiveInfrastructure");
        archiveInfrastructure.hasPart(dataManagementSystem);
        archiveInfrastructure.hasPart(storageSubSystem);
        dataManagementSystem.hasPart(managementSystemInterface);
        dataManagementSystem.hasPart(archiveAquisistionFile_v1);
        dataManagementSystem.hasPart(digitalDepositeForm_v1);
        dataManagementSystem.hasPart(dataManagement);
        managementSystemInterface.providesAccessTo(archiveAquisistionFile_v1);
        managementSystemInterface.providesAccessTo(digitalDepositeForm_v1);
        managementSystemInterface.providesAccessTo(requestProcess);
        managementSystemInterface.isUsedBy(archivists);
        storageSubSystem.hasPart(storageSystemInterface);
        storageSystemInterface.isUsedBy(archivists);
        archiveAquisistionFile_v1.id("ID-123");
        archiveAquisistionFile_v1.version("1.0");
        digitalDepositeForm_v1.version("1.0");
    }

    /*
     * The archive acquisition file is requested by ID to the data management
     * system, which will have a link to the package stored in the storage
     * subsystem.
     */
    private void requestArchiveAquisitionFile() {
        requestProcess.hasInput("ArchiveAcquisitionFile-ID");
        requestProcess.hasOutput(archiveAquisistionFile_v1);
        requestProcess.hasLink("BPMN/RequestProcess.BPMN");
        requestProcess.setFunction("getArchiveAcquisitionFile(String ID)");
        requestProcess
                .describedBy("Gets the archive aquisition file from the data management system.");
        requestProcess.runsOn(dataManagementSystem);
        archivist.execute(requestProcess);
    }

    private void findAgreement() {
        findAgreementProcess.hasInput(archivist);
        findAgreementProcess.hasInput(donor);
        findAgreementProcess.hasOutput(agreement);
        findAgreementProcess.hasLink("BPMN/FindAgreement.BPMN");
        findAgreementProcess
                .setFunction("findAgreementProcess(HumanAgent archivist, HumanAgent artist)");
        findAgreementProcess
                .describedBy("The archivist and the donor agree on the aquisition proceeding.");
        archivist.execute(findAgreementProcess);
    }

    /*
     * Then, the Digital Deposit Form is updated and the Deed of Gift is added
     * to the file.
     *
     * "... update of the Digital Deposit Form with the agreements between the
     * archivist and the donor, and the addition of a Deed of Gift (a legal
     * document) into the archive acquisition file."
     */
    private void updateFiles() {
        /*
         * Note: There would be a new version of the digital deposit form
		 * instead of a new entity, if the whole PERICLES architecture would be
		 * involved. (With versioning and timestamps by LRM services)
		 */
        updateDigitalDepositeForm.hasInput(digitalDepositeForm_v1);
        updateDigitalDepositeForm.hasInput(agreement);
        updateDigitalDepositeForm.hasOutput(digitalDepositeForm_v2);
        updateDigitalDepositeForm.hasLink("BPMN/UpdateDigitalDepositForm.BPMN");
        updateDigitalDepositeForm
                .setFunction("updateDigitalDepositForm(DigitalObject oldForm,DigitalObject agreement)");
        updateDigitalDepositeForm
                .describedBy("Update of the Digital Deposit Form with the agreements between the archivist and the donor.");
        digitalDepositeForm_v2.hasPart(digitalDepositeForm_v1);
        digitalDepositeForm_v2.hasPart(agreement);
        digitalDepositeForm_v2.version("2.0");

        updateArchiveAcquisitionFile.hasInput(archiveAquisistionFile_v1);
        updateArchiveAcquisitionFile.hasInput(deedOfGift);
        updateArchiveAcquisitionFile.hasOutput(archiveAquisistionFile_v2);
        updateArchiveAcquisitionFile
                .hasLink("BPMN/UpdateArchiveAcquisitionFile.BPMN");
        updateArchiveAcquisitionFile
                .setFunction("updateArchiveAcquisitionFile(DigitalObject oldFile, DigitalObject deedOfGift)");
        updateArchiveAcquisitionFile
                .describedBy("Enters the deed of gift into the archive acquisition file.");

        archiveAquisistionFile_v2.hasPart(archiveAquisistionFile_v1);
        archiveAquisistionFile_v2.hasPart(deedOfGift);
        archiveAquisistionFile_v2.version("2.0");
        archiveAquisistionFile_v2.id("ID-123"); // same as v1

        // Will call both sub-processes:
        archivist.execute(updateFiles);
    }

    /*
     * The metadata of the updated file is extracted and normalised to an agreed
     * format.
     */
    private void metadataExtraction() {
        metadataExtraction.hasInput(archiveAquisistionFile_v2);
        metadataExtraction.hasOutput(metadata_v1);
        metadataExtraction.hasLink("BPMN/MetadataExtraction.BPMN");
        metadataExtraction
                .setFunction("extractMetadata(DigitalObject archiveAquisitionFile)");
        metadataExtraction.describedBy("Extracts the metadata of the file.");
        metadata_v1.version("1.0");

        normalisation.hasInput(metadata_v1);
        normalisation.hasOutput(metadata_v2);
        normalisation.hasLink("BPMN/MetadataNormalisation.BPMN");
        normalisation.setFunction("normalise(DigitalObject metadata)");
        normalisation.describedBy("Normalises metadata to an agreed format.");
        metadata_v2.version("2.0");

        archivist.execute(metadataExtraction);
        archivist.execute(normalisation);
    }

    /*
     * Then both, archive file and metadata, are packaged together.
     */
    private void packaging() {
        packaging.hasInput(archiveAquisistionFile_v2);
        packaging.hasInput(metadata_v2);
        packaging.hasOutput(resultingPackage);
        packaging.hasLink("BPMN/Packaging.BPMN");
        packaging
                .setFunction("pack(DigitalObject file, DigitalObject metadata)");
        packaging
                .describedBy("Creates a packages of the aquisition file and its metadata.");

        archivist.execute(packaging);
    }

    /*
     * The resulting package is stored in a storage system and a notification to
     * the data management regarding this update is sent.
     */
    private void storing() {
        storing.hasInput(resultingPackage);
        storing.hasOutput(updateNotification);
        storing.hasLink("BPMN/Storing.BPMN");
        storing.setFunction("store(DigitalObject package)");
        storing.describedBy("Stores a package to the storage system.");
        storing.runsOn(storageSubSystem);
        archivist.execute(storing);
        dataManagement.triggeredBy(updateNotification);
    }

    private final Policy agreeBeforeAquire = new Policy(
            scenario, "Agreement Policy");
    private final Process requestProcess = new Process(scenario,
            "Request Archive Aquisition File");
    private final Process findAgreementProcess = new Process(scenario,
            "Create Agreement");
    private final AggregatedProcess updateFiles = new AggregatedProcess(
            scenario, "Update Files");
    private final Process updateDigitalDepositeForm = new Process(scenario,
            "Update Digital Deposite Form");
    private final Process updateArchiveAcquisitionFile = new Process(scenario,
            "Update Archive Aquisition File");
    private final Process metadataExtraction = new Process(scenario,
            "Metadata Extraction");
    private final ProcessForTransformation normalisation = new ProcessForTransformation(
            scenario, "Normalisation");
    private final Process packaging = new Process(scenario, "Packaging");
    private final Process storing = new Process(scenario, "Storing");
    private final Process dataManagement = new Process(scenario,
            "DataManagement");

    private final Community tate = new Community(scenario, "Tate");
    private final Community artists = new Community(scenario, "Artists");

    private final HumanAgent archivist = new HumanAgent(scenario, "Archivist");
    private final HumanAgent donor = new HumanAgent(scenario, "Donor");

    private final DigitalObject archiveAquisistionFile_v1 = new DigitalObject(
            scenario, "First Version Of Archive Aquisistion File");
    private final DigitalObject archiveAquisistionFile_v2 = new DigitalObject(
            scenario, "Second Version Of Archive Aquisistion File");
    private final DigitalObject digitalDepositeForm_v1 = new DigitalObject(
            scenario, "First Version Of Digital Deposite Form");
    private final DigitalObject digitalDepositeForm_v2 = new DigitalObject(
            scenario, "Second Version Of Digital Deposite Form");

    private final DigitalObject agreement = new DigitalObject(scenario,
            "Agreement");
    private final DigitalObject deedOfGift = new DigitalObject(scenario,
            "Deed Of Gift");
    private final DigitalObject metadata_v1 = new DigitalObject(scenario,
            "First Version Of Metadata");
    private final DigitalObject metadata_v2 = new DigitalObject(scenario,
            "Second Version Of Metadata");
    private final DigitalObject resultingPackage = new DigitalObject(scenario,
            "Package");

    private final TechnicalService dataManagementSystem = new TechnicalService(
            scenario, "Data Management System");
    private final TechnicalService storageSubSystem = new TechnicalService(
            scenario, "Storage Sub System");

    private final ServiceInterface managementSystemInterface = new ServiceInterface(
            scenario, "Data Management System Interface");
    private final ServiceInterface storageSystemInterface = new ServiceInterface(
            scenario, "Storage System Interface");

    private final EcosystemEvent updateNotification = new EcosystemEvent(
            scenario, "Update Notification");
}
