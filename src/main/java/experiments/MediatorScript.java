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

import LRMv2.LRM_static_schema;
import entities.DigitalObject;
import entities.HumanAgent;
import entities.ServiceInterface;
import entities.TechnicalService;
import models.ScenarioModel;
import relations.DEMRelation;
import relations.RelationBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;

/**
 * This is an example for a mediator script, which extracts information about a digital objects and its environment,
 * and inserts the extracted information into the related DEM entities.
 */
public class MediatorScript extends Experiment {
    private DEMRelation lastModified = new RelationBuilder(scenario, "last modified").create();
    private DEMRelation lastAccess = new RelationBuilder(scenario, "last access").create();
    private DEMRelation creationTime = new RelationBuilder(scenario, "creation time").create();
    private DEMRelation fileType = new RelationBuilder(scenario, "file type").create();
    private DEMRelation fileSize = new RelationBuilder(scenario, "file size").create();
    private DEMRelation fileKey = new RelationBuilder(scenario, "file key").create();
    private DEMRelation fileGroupOwner = new RelationBuilder(scenario, "file group owner").create();

    private DEMRelation userLanguage = new RelationBuilder(scenario, "user language").create();
    private DEMRelation userTimezone = new RelationBuilder(scenario, "user timezone").create();
    private DEMRelation osVersion = new RelationBuilder(scenario, "os version").create();
    private DEMRelation osName = new RelationBuilder(scenario, "os name").create();

    private ReportFileTemplate reportFileTemplate;
    private ResearcherSystemTemplate researcherSystemTemplate;

    public MediatorScript() {
        super("Mediator_script_example");
        try {
            createScenario();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createScenario() throws IOException {
        reportFileTemplate = new ReportFileTemplate();
        researcherSystemTemplate = new ResearcherSystemTemplate();
        File todayFile = new File("reportToday");
        boolean created = todayFile.createNewFile();
        File yesterdayFile = new File("reportYesterday");
        created = created & yesterdayFile.createNewFile();
        if (!created) {
            return;
        }
        ReportFileTemplate.ReportFile reportYesterday
                = reportFileTemplate.createConcreteReport("Scientific Report for yesterday"
                , yesterdayFile.toPath());
        ReportFileTemplate.ReportFile reportToday
                = reportFileTemplate.createConcreteReport("Scientific Report for today"
                , todayFile.toPath());
        ResearcherSystemTemplate.ResearcherSystem system
                = researcherSystemTemplate.createConcreteSystem("Local Machine");
        reportToday.partOf(system);
        reportYesterday.partOf(system);
        HumanAgent scientist = new HumanAgent(scenario, System.getProperty("user.name"));
        scientist.owns(reportToday);
        scientist.owns(reportYesterday);
        scientist.owns(system);
        ServiceInterface terminal = new ServiceInterface(scenario, "Terminal");
        terminal.partOf(system);
        terminal.isUsedBy(scientist);
        terminal.providesAccessTo(reportToday);
        terminal.providesAccessTo(reportYesterday);
        todayFile.delete();
        yesterdayFile.delete();
    }

    /**
     * Template for report files
     */
    private class ReportFileTemplate extends DigitalObject.DigitalObjectTemplate {
        public ReportFileTemplate() {
            super(scenario, "Scientific Report", "Template class for scientific reports");
        }

        public ReportFile createConcreteReport(String identifier, Path path) {
            ReportFile file = createEntity(scenario, identifier);
            PosixFileAttributes posixAttributes = null;
            try {
                posixAttributes = Files.readAttributes(path, PosixFileAttributes.class);
            } catch (IOException e) {
                System.err.println("The mediator script example won't work correctly on your machine due to missing privileges.");
                return file;
            }
            String type = "other";
            if (posixAttributes.isRegularFile()) {
                type = "regular_file";
            } else if (posixAttributes.isDirectory()) {
                type = "directory";
            } else if (posixAttributes.isSymbolicLink()) {
                type = "symbolic_link";
            }
            String modified = posixAttributes.lastModifiedTime().toString();
            String access = posixAttributes.lastAccessTime().toString();
            String time = posixAttributes.creationTime().toString();
            String size = "" + posixAttributes.size();
            String key = posixAttributes.fileKey().toString();
            String groupOwner = posixAttributes.group().getName();
            file.addProperty(lastModified, modified);
            file.addProperty(lastAccess, access);
            file.addProperty(creationTime, time);
            file.addProperty(fileType, type);
            file.addProperty(fileSize, size);
            file.addProperty(fileKey, key);
            file.addProperty(fileGroupOwner, groupOwner);
            return file;
        }

        public class ReportFile extends DigitalObject {
            public ReportFile(ScenarioModel model, String name) {
                super(model, name, reportFileTemplate);
            }
        }

        @Override
        public ReportFile createEntity(ScenarioModel model, String ID) {
            return new ReportFile(model, ID);
        }
    }

    private class ResearcherSystemTemplate extends TechnicalService.TechnicalServiceTemplate {
        public ResearcherSystemTemplate() {
            super(scenario, "Researcher System", "The computer of the researcher.");
        }

        public ResearcherSystem createConcreteSystem(String identifier) {
            ResearcherSystem system = new ResearcherSystem(scenario, identifier);
            String user_language = System.getProperty("user.language");
            String user_timezone = System.getProperty("user.timezone");
            String os_version = System.getProperty("os.version");
            String os_name = System.getProperty("os.name");
            system.addProperty(userLanguage, user_language);
            system.addProperty(userTimezone, user_timezone);
            system.addProperty(osVersion, os_version);
            system.addProperty(osName, os_name);
            return system;
        }

        @Override
        public TechnicalService createEntity(ScenarioModel model, String ID) {
            TechnicalService technicalService = new TechnicalService(model, ID, this);
            technicalService.addOntClass(LRM_static_schema.ConcreteResource);
            technicalService.addProperty(LRM_static_schema.realizes, this);
            return technicalService;
        }

        public class ResearcherSystem extends TechnicalService {
            public ResearcherSystem(ScenarioModel model, String identifier) {
                super(model, identifier, researcherSystemTemplate);
            }
        }
    }
}
