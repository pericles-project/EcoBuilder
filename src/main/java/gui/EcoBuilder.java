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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import saver.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EcoBuilder extends Application {
    public static final String TITLE_ID = "title";
    public static final String SCENARIO_ID = "scenario";
    public static final String MODEL_ID = "model";
    public static final String TEMPLATE_ID = "template";
    public static final String ENTITY_ID = "entity";
    public static final String RELATION_ID = "relation";
    public static final String TARGET_ENTITY_ID = "target_entity";
    public static final String DIALOG_ID = "dialog";
    public static final String REMOVE_BUTTON_ID = "remove-button";
    public static final String ADD_BUTTON_ID = "add-button";
    public static final String SUB_TITLE = "sub-title";
    public static final String BOX = "box";
    public static final String PANE = "pane";
    private static final String CONFIGURATION_ID = "configuration";
    public static final String STYLESHEET = "style.css";

    private final GridPane configurationPane = new GridPane();
    public ScenarioPanel scenarioPane;

    private final EcoBuilderMenu menu = new EcoBuilderMenu(this);
    private final Separator separator = new Separator();

    private final SystemTrayIcon trayIcon;
    private final TextArea informationArea = new TextArea();
    private Text informationTitle = new Text("Information Area");
    // Was the scenario saved? :
    public boolean saved = false;

    // Utility classes for saving the models:
    public final DEMSaver demSaver;
    public final ExperimentsSaver experimentsSaver;
    public final ScenarioSaver scenarioSaver;
    public final ProjectSaver projectSaver;
    public final ProjectLoader projectLoader;

    // Classes for the configuration
    public final EntityConfiguration entityConfiguration;
    protected final TemplateConfiguration templateConfiguration;
    protected final ModelConfiguration modelConfiguration;

    public static void main(String[] args) {
        launch();
    }

    /**
     * Start the application GUI.
     *
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        informationTitle.setId(SUB_TITLE);
        informationArea.setWrapText(true);
        informationArea.setEditable(false);
        informationArea.setMaxHeight(120);
        informationArea.setPrefHeight(120);
        configurationPane.getStyleClass().add(EcoBuilder.PANE);
        configurationPane.setId(CONFIGURATION_ID);
        GridPane rightPane = new GridPane();
        rightPane.getStyleClass().add(EcoBuilder.PANE);
        setConstraints();
        rightPane.getChildren().addAll(informationTitle, informationArea, separator, configurationPane);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(scenarioPane);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        HBox hbox = new HBox();
        hbox.setPrefSize(1280, 770);
        hbox.setMinSize(1280, 770);
        hbox.getChildren().addAll(scrollPane, rightPane);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(menu, hbox);
        loadWelcomeInformation();
        Scene scene = new Scene(vbox, 1280, 800);
        scene.getStylesheets().add(STYLESHEET);
        primaryStage.setOnCloseRequest(event -> closeDialog());
        primaryStage.setTitle("EcoBuilder - Create a Digital Ecosystem Model for your Scenario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setConstraints() {
        GridPane.setConstraints(informationTitle, 0, 0);
        GridPane.setConstraints(informationArea, 0, 1);
        GridPane.setConstraints(separator, 0, 2);
        GridPane.setConstraints(configurationPane, 0, 3);
    }

    public EcoBuilder() {
        this.trayIcon = new SystemTrayIcon(this);
        experimentsSaver = new ExperimentsSaver(this);
        demSaver = new DEMSaver(this);
        scenarioSaver = new ScenarioSaver(this);
        projectSaver = new ProjectSaver(this);
        projectLoader = new ProjectLoader(this);
        scenarioPane = new ScenarioPanel(this);
        entityConfiguration = new EntityConfiguration(configurationPane, scenarioPane);
        templateConfiguration = new TemplateConfiguration(configurationPane, scenarioPane);
        modelConfiguration = new ModelConfiguration(configurationPane);
    }

    /**
     * Load a welcome message.
     */
    private void loadWelcomeInformation() {
        loadInfoText("Welcome to the EcoBuilder", "Welcome to the EcoBuilder!\nThis GUI will assist you in creating Digital Ecosystem Models.\n\n" +
                "Your Scenario is empty at start. Start modelling by adding entities to your scenario.");
    }

    /**
     * Load text information into the information area.
     *
     * @param title Title of the information
     * @param text  The information to be displayed
     */
    public void loadInfoText(String title, String text) {
        informationTitle.setText(title);
        informationArea.setText(text);
    }

    public File saveScenario() {
        return scenarioSaver.save();
    }

    public void saveDEM() {
        demSaver.save();
    }

    public void saveExamples() {
        experimentsSaver.save();
    }

    public File saveProject() {
        return projectSaver.save();
    }

    public void loadProject() {
        projectLoader.load();
    }

    /**
     * Loads information into the information area at the right side of the GUI.
     *
     * @param title       Title of the information
     * @param description the information
     */
    public void loadInformation(String title, String description) {
        informationTitle.setText(title);
        informationArea.setText(description);
    }

    /**
     * Asks the user if she really wants to exit without saving.
     */
    public void closeDialog() {
        if (!saved) {
            if (scenarioPane.customRelations.size() > 0 || scenarioPane.customTemplates.size() > 0) {
                new CloseDialog();
            }
            scenarioPane.getAllTemplates()
                    .stream().filter(template -> template.childEntities.size() > 0).forEach(template -> {
                new CloseDialog();
            });
        }
        exit();
    }

    /**
     * Exits the application.
     */
    private void exit() {
        SystemTray systemTray = SystemTray.getSystemTray();
        systemTray.remove(trayIcon.icon);
        Platform.exit();
    }

    /**
     * A dialog to ask the user if the project should be saved before closing the application.
     */
    private class CloseDialog {
        private Text title = new Text("Save project before exit?");
        private Button no = new Button("No");
        private Button yes = new Button("Yes");

        private CloseDialog() {
            GridPane dialog = new GridPane();
            dialog.setId(DIALOG_ID);
            GridPane.setConstraints(title, 0, 0, 3, 1);
            GridPane.setConstraints(no, 1, 1, 1, 1);
            GridPane.setConstraints(yes, 2, 1, 1, 1);
            dialog.getChildren().addAll(title, no, yes);
            Scene scene = new Scene(new VBox(dialog), 200, 100);
            scene.getStylesheets().add(STYLESHEET);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("Create custom relation");
            stage.setScene(scene);
            stage.show();
            no.setOnAction(e -> {
                stage.hide();
                exit();
            });
            yes.setOnAction(e -> {
                projectSaver.save();
                stage.hide();
                exit();
            });
        }
    }
}
