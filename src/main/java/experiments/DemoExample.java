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

/**
 * This is a simple example from the arts domain.
 */
public class DemoExample extends Experiment {
    private TechnicalService archiveRepository;
    private InfrastructureComponent applicationLayer;
    private InfrastructureComponent processGroup;
    private InfrastructureComponent executionLayer;
    private InfrastructureComponent storage;
    private Community designatedCommunity;
    private HumanAgent acquisitions;
    private HumanAgent technician;
    private HumanAgent conservation;
    private HumanAgent admin;
    private HumanAgent display;
    private DigitalObject art;
    private DigitalObject SIP;
    private DigitalObject AIP;
    private DigitalObject DIP;
    private DigitalObject CMD;
    private Query query;
    private DigitalObject queryResult;
    private DigitalObject metadata;
    private DigitalObject specification;
    private Process acquire;
    private Process disseminate;
    private Process analysisProcess;
    private Process changeVideoFormatProcess;
    private Process changeVideoPlayerProcess;
    private Process migrationProcess;
    private Process disseminationProcess;

    public DemoExample() {
        super("Demo_example");
        archiveRepository = new TechnicalService(scenario, "Archive Repository");
        archiveRepository.addComment("A parent body to which all infrastructure and processes belong which represents " +
                "the repository as a whole - data is input, stored and retrieved conceptually from the repository by the " +
                "human agents via contained infrastructure and process components.");
        archiveRepository.version("1.0.0");

        applicationLayer = new InfrastructureComponent(scenario, "Administration and Application Layer");
        applicationLayer.addComment("Administration and Application Layer is a set of components for managing, directing " +
                "the interaction of components and agents within the repository - this includes managing the entities " +
                "within the repository and supporting human agent facing applications.");
        applicationLayer.version("1.0.0");
        applicationLayer.partOf(archiveRepository);

        processGroup = new InfrastructureComponent(scenario, "Process Group");
        processGroup.addComment("The process group is a collection of processes which are represented in the repository " +
                "for carrying out operations as directed by the admin layer when instructed to do so by a human agent. " +
                "These processes can be added to and removed as the repository evolves over time. The process group will " +
                "have representation in the administration layer.");
        processGroup.version("1.0.0");
        processGroup.partOf(archiveRepository);

        executionLayer = new InfrastructureComponent(scenario, "Execution Layer");
        executionLayer.addComment("Execution Layer is where analysis, modification and packaging of data items occurs. " +
                "This is a container which represents all executable elements which will be called through processes to " +
                "carry out the functions required.");
        executionLayer.version("1.0.0");
        executionLayer.partOf(archiveRepository);

        storage = new InfrastructureComponent(scenario, "Storage");
        storage.addComment("The Storage is where all data under control will be located, to access or analyse a " +
                "workflow/process must be used via the administration layer.");
        storage.version("1.0.0");
        storage.partOf(archiveRepository);

        designatedCommunity = new Community(scenario, "Designated Community");

        acquisitions = new HumanAgent(scenario, "Acquisitions");
        acquisitions.addComment("A person who deals with the initial acquisition phase and data gathering for an artwork");
        acquisitions.isMemberOf(designatedCommunity);

        technician = new HumanAgent(scenario, "Technician");
        technician.addComment("A person who deals with the technical aspects of archiving, modifying and updating the " +
                "artworks under control.");
        technician.isMemberOf(designatedCommunity);

        conservation = new HumanAgent(scenario, "Conservation");
        conservation.addComment("A person who plans,monitors and implements policy and process for the preservation of " +
                "artworks and metadata.");
        conservation.isMemberOf(designatedCommunity);

        admin = new HumanAgent(scenario, "Admin");
        admin.addComment("A person who acts as a system administrator for an archive, to monitor and update the software " +
                "and hardware infrastructure as required.");
        admin.isMemberOf(designatedCommunity);

        display = new HumanAgent(scenario, "Display");
        display.addComment("A person who wishes to create a display version or installation of an artwork");
        display.isMemberOf(designatedCommunity);

        art = new DigitalObject(scenario, "Art");
        art.addComment("A new artwork in a raw format to be added to the archive");

        SIP = new DigitalObject(scenario, "SIP");
        SIP.addComment("Submission Information Package - This is the pre-ingested packaged form of an Art object. This " +
                "should bundle the work and metadata available into a package for processing.");

        AIP = new DigitalObject(scenario, "AIP");
        AIP.addComment("Archive Information Package - This is the package format to be used for storage and internal " +
                "transfer in the archive.");

        DIP = new DigitalObject(scenario, "DIP");
        DIP.addComment("Dissemination Information Package - This is the package format to be used for accessing " +
                "materials from outside the repository, so when an artwork has to be displayed for example.");

        CMD = new DigitalObject(scenario, "CMD");
        CMD.addComment("Command Object - this represents a command from an agent or activity which instructs another " +
                "operation to take place.");

        query = new Query(scenario, "Query");
        query.addComment("Query objects should contain a target class(es) of objects which need to be filtered upon, " +
                "it should contain the filter criteria and result specification");

        queryResult = new DigitalObject(scenario, "Query Result");
        queryResult.addComment("Query result objects should contain the results of a query in the format complying with " +
                "the query specification");

        metadata = new DigitalObject(scenario, "Metadata");
        specification = new DigitalObject(scenario, "Specification");

        acquire = new Process(scenario, "Acquire");
        acquire.addComment("Human orientated task to allow an new artwork to be pre-loaded to the repository before " +
                "acquisition takes place in order to facilitate analysis and storage preparation. Will allow input of " +
                "an artwork to be prepared as an SIP.");
        acquire.version("1.0.0");
        acquire.runsOn(archiveRepository);
        acquire.hasInput(art);
        acquire.hasInput(metadata);
        acquire.hasOutput(SIP);
        acquire.hasLink("pericles.eu/basic-dva/entity/process/preacq?form=bpmn");

        disseminate = new Process(scenario, "Disseminate");
        disseminate.addComment("Human orientated task to allow query and specification of an artwork to be extracted " +
                "and transformed for display purposes");
        disseminate.version("1.0.0");
        disseminate.runsOn(archiveRepository);
        disseminate.hasInput(query);
        disseminate.hasInput(metadata);
        disseminate.hasInput(specification);
        disseminate.hasOutput(DIP);
        disseminate.hasLink("pericles.eu/basic-dva/entity/process/diss?form=bpmn");
        analysisProcess = new Process(scenario, "Analysis Process");
        changeVideoFormatProcess = new Process(scenario, "Change Video Format Process");
        changeVideoPlayerProcess = new Process(scenario, "Change Video Player Process");
        migrationProcess = new Process(scenario, "Migration Process");
        disseminationProcess = new Process(scenario, "Dissemination Process");
    }
}
