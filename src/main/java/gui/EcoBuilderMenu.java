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
package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * The (file) menu at the top of the application.
 * You can add further menus here, e.g. help.
 */
public class EcoBuilderMenu extends MenuBar {
    public EcoBuilderMenu(final EcoBuilder gui) {
        Menu fileMenu = new Menu("File");
        Menu ermrMenu = new Menu("Repository");
        MenuItem saveScenario = new MenuItem("Generate Your Scenario Model");
        MenuItem saveDEM = new MenuItem("Generate DEM");
        MenuItem saveExamples = new MenuItem("Generate Examples");
        MenuItem openProject = new MenuItem("Open Project");
        MenuItem saveProject = new MenuItem("Save Project");
        MenuItem configure = new MenuItem("Configure");
        MenuItem send = new MenuItem("Send");
        saveDEM.setOnAction(e -> gui.saveDEM());
        saveScenario.setOnAction(e -> gui.saveScenario());
        saveExamples.setOnAction(e -> gui.saveExamples());
        saveProject.setOnAction(e -> gui.saveProject());
        openProject.setOnAction(e -> gui.loadProject());
        send.setOnAction(e -> gui.sendToERMR());
        configure.setOnAction(e -> gui.configuteERMR());
        fileMenu.getItems().addAll(saveScenario, saveDEM, saveExamples, new SeparatorMenuItem(), openProject, saveProject);
        ermrMenu.getItems().addAll(configure, send);
        getMenus().addAll(fileMenu, ermrMenu);
    }
}