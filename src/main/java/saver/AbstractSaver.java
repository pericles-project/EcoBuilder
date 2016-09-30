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
package saver;

import com.hp.hpl.jena.ontology.OntModel;
import gui.EcoBuilder;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.CoreModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract base class to be inherited by model saving classes.
 */
public abstract class AbstractSaver {
    protected static File projectOutputDirectory;
    protected final String TURTLE = "TURTLE";
    protected final String XML = "RDF/XML-ABBREV";
    protected final String TTL = ".ttl";
    protected final String OWL = ".owl";
    protected final String subDirectoryName;
    protected File turtleSubDirectory;
    protected File xmlSubDirectory;
    protected EcoBuilder ecoBuilder;

    public AbstractSaver(EcoBuilder ecoBuilder, String subDirectoryName) {
        this.ecoBuilder = ecoBuilder;
        this.subDirectoryName = subDirectoryName;
    }

    public abstract File save();

    /**
     * Create the sub output directories which are specific for the implementing saver.
     */
    private void createSubDirectories() {
        File subDirectory = new File(projectOutputDirectory.getAbsolutePath() + File.separator + subDirectoryName + File.separator);
        subDirectory.mkdir();
        turtleSubDirectory = new File(subDirectory.getAbsolutePath() + File.separator + "turtle" + File.separator);
        xmlSubDirectory = new File(subDirectory.getAbsolutePath() + File.separator + "owl" + File.separator);
        turtleSubDirectory.mkdir();
        xmlSubDirectory.mkdir();
    }

    /**
     * Lets the user chose a project output directory.
     * <p>
     *
     * @return Were the directories created, or has the user canceled?
     */
    public boolean saveAsDialogue() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose an output directory");
        File directory = directoryChooser.showDialog(new Stage());
        if (directory == null) {
            return false;
        }
        if (directory.getName().equals("DEM")) {
            projectOutputDirectory = directory;
            return true;
        }
        for (File child : directory.listFiles()) {
            if (child != null && child.getName().equals("DEM")) {
                projectOutputDirectory = child;
                return true;
            }
        }
        projectOutputDirectory = new File(directory.getPath() + File.separator + "DEM" + File.separator);
        return projectOutputDirectory.mkdir();
    }

    /**
     * Creates the file name suffix which consists of the current date and the file format.
     */
    protected String getSuffix(String language) {
        String format = "";
        if (language.equals(XML)) {
            format = OWL;
        } else if (language.equals(TURTLE)) {
            format = TTL;
        }
        return "_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date()) + format;
    }

    /**
     * Save the model in XML as well as in TURTLE.
     *
     * @param model The model to be saved
     */
    protected File saveModel(OntModel model) {
        return saveModel(model, "scenario");
    }

    protected File saveModel(OntModel model, String experimentName) {
        if (!createOutputDirectories()) {
            return null; // Output dirs couldn't be created.
        }
        File turtleFile = new File(turtleSubDirectory.getAbsolutePath() + File.separator + CoreModel.sanitizeName(experimentName) + getSuffix(TURTLE));
        File xmlFile = new File(xmlSubDirectory.getAbsolutePath() + File.separator + CoreModel.sanitizeName(experimentName) + getSuffix(XML));
        try {
            model.write(new FileOutputStream(turtleFile), TURTLE);
            model.write(new FileOutputStream(xmlFile), XML);
        } catch (FileNotFoundException e1) {
            return null;
        }
        return turtleFile;
    }


    /**
     * Creates the required output directories, if not yet existing.
     */
    protected boolean createOutputDirectories() {
        // already created and valid:
        if (projectOutputDirectory != null && xmlSubDirectory != null && turtleSubDirectory != null && projectOutputDirectory.isDirectory() && xmlSubDirectory.isDirectory() && turtleSubDirectory.isDirectory()) {
            return true;
        }
        // create project output directory:
        else if (projectOutputDirectory == null || !projectOutputDirectory.isDirectory()) {
            if (!saveAsDialogue()) {
                return false;
            } else {
                createSubDirectories();
                return true;
            }
        } else if (turtleSubDirectory == null || xmlSubDirectory == null || !turtleSubDirectory.isDirectory() || !xmlSubDirectory.isDirectory()) {
            createSubDirectories();
            return true;
        }
        return false;
    }
}
