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

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import relations.CustomRelation;

import java.util.*;
import java.util.stream.Collectors;

import static gui.EcoBuilder.*;

public class CustomRelationCreation {
    private final ScenarioPanel scenarioPanel;
    private final Text relationName = new Text("Relation name: ");
    private final Text description = new Text("Description: ");
    private final Text title = new Text("Select all entities TO and FROM which the relation can point (Choose core model -> EcosystemEntity for all)");
    private final Text from = new Text("FROM");
    private final Separator separator = new Separator();
    private final Text to = new Text("TO");
    private final TextField nameField = new TextField();
    private final TextArea descriptionArea = new TextArea();
    private final Button createRelation = new Button("Create");
    private final Hashtable<String, ListView<String>> fromBoxes = new Hashtable<>();
    private final Hashtable<String, ListView<String>> toBoxes = new Hashtable<>();
    private final List<Text> fromText = new ArrayList<>();
    private final List<Text> toText = new ArrayList<>();

    public CustomRelationCreation(ScenarioPanel scenarioPanel) {
        this.scenarioPanel = scenarioPanel;
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(true);
        separator.setOrientation(Orientation.VERTICAL);
        from.setId(SUB_TITLE);
        to.setId(SUB_TITLE);
        createRelation.setId(ADD_BUTTON_ID);
        createBoxes();
        setConstraints();
    }

    /**
     * Creates the hashtables which store the from and to boxes.
     */
    private void createBoxes() {
        for (ModelView model : scenarioPanel.models) {
            if (model instanceof DEMModelView) {
                ListView<String> fromBox = new ListView<>();
                ListView<String> toBox = new ListView<>();
                fromBoxes.put(model.prefix, fromBox);
                toBoxes.put(model.prefix, toBox);
                toBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                fromBox.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }
        }
        for (String modelPrefix : fromBoxes.keySet()) {
            Text text = new Text(modelPrefix);
            fromText.add(text);
        }
        for (String modelPrefix : toBoxes.keySet()) {
            Text text = new Text(modelPrefix);
            toText.add(text);
        }
    }

    /**
     * Opens a dialog to createRelation a custom relation
     *
     * @param entityConfiguration
     */
    protected void createCustomRelation(EntityConfiguration entityConfiguration) {
        fillBoxes();
        GridPane dialog = new GridPane();
        dialog.setId(DIALOG_ID);
        for (ListView<String> fromBox : fromBoxes.values()) {
            dialog.getChildren().add(fromBox);
        }
        for (ListView<String> toBox : toBoxes.values()) {
            dialog.getChildren().add(toBox);
        }
        for (Text text : fromText) {
            dialog.getChildren().add(text);
        }
        for (Text text : toText) {
            dialog.getChildren().add(text);
        }
        dialog.getChildren().addAll(relationName, nameField, description, descriptionArea, title, from, to, createRelation);
        ScrollPane scrollPane = new ScrollPane(dialog);
        Scene scene = new Scene(new VBox(scrollPane), 900, 800);
        scene.getStylesheets().add(STYLESHEET);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Create custom relation");
        stage.setScene(scene);
        stage.show();
        createRelation.setOnAction(e -> {
            CustomRelation relation = createRelation();
            if (relation != null) {
                stage.hide();
                entityConfiguration.createCustomRelation(relation);
            }
        });
    }

    private void fillBoxes() {
        for (ListView<String> fromBox : fromBoxes.values()) {
            fromBox.getItems().clear();
        }
        for (ListView<String> toBox : toBoxes.values()) {
            toBox.getItems().clear();
        }
        for (String prefix : fromBoxes.keySet()) {
            ModelView model = scenarioPanel.getModel(prefix);
            ListView<String> fromBox = fromBoxes.get(prefix);
            for (RadioTemplate radioTemplate : model.childTemplateViews) {
                fromBox.getItems().add(radioTemplate.templateView.name);
            }
        }
        for (String prefix : toBoxes.keySet()) {
            ModelView model = scenarioPanel.getModel(prefix);
            ListView<String> toBox = toBoxes.get(prefix);
            for (RadioTemplate radioTemplate : model.childTemplateViews) {
                toBox.getItems().add(radioTemplate.templateView.name);
            }
        }
    }

    private CustomRelation createRelation() {
        String name = nameField.getText();
        if (name == null || name.equals("")) {
            scenarioPanel.ecoBuilder.loadInformation("Error", "The relation that you wanted to createRelation has no name. Specify " +
                    "a name first!");
            return null;
        }
        Set<TemplateView> domains = new HashSet<>();
        Set<TemplateView> ranges = new HashSet<>();
        for (ListView<String> fromBox : fromBoxes.values()) {
            domains.addAll(fromBox.getSelectionModel().getSelectedItems().stream().map(scenarioPanel::getTemplateView).collect(Collectors.toList()));
        }
        for (ListView<String> toBox : toBoxes.values()) {
            ranges.addAll(toBox.getSelectionModel().getSelectedItems().stream().map(scenarioPanel::getTemplateView).collect(Collectors.toList()));
        }
        if (domains.size() == 0 || ranges.size() == 0) {
            System.err.println("Not all necessary entities selected.");
            return null;
        }
        String desc = "";
        if (descriptionArea.getText() != null & !descriptionArea.getText().equals("")) {
            desc = descriptionArea.getText();
        }
        return new CustomRelation(name, desc, domains, ranges);
    }

    private void setConstraints() {
        int y = 0;
        GridPane.setConstraints(relationName, 0, y, 1, 1);
        GridPane.setConstraints(nameField, 1, y, 2, 1);
        y++;
        GridPane.setConstraints(description, 0, y, 3, 1);
        y++;
        GridPane.setConstraints(descriptionArea, 0, y, 3, 1);
        y++;
        GridPane.setConstraints(title, 0, y, 3, 1);
        y++;

        int start = y;
        GridPane.setConstraints(from, 0, y, 1, 1);
        y++;
        for (Text text : fromText) {
            GridPane.setConstraints(text, 0, y, 2, 1);
            y++;
            GridPane.setConstraints(fromBoxes.get(text.getText()), 0, y, 1, 1);
            y++;
        }

        GridPane.setConstraints(separator, 1, start, 1, y - start);

        y = start;
        GridPane.setConstraints(to, 2, y, 1, 1);
        y++;
        for (Text text : toText) {
            GridPane.setConstraints(text, 2, y, 2, 1);
            y++;
            GridPane.setConstraints(toBoxes.get(text.getText()), 2, y, 1, 1);
            y++;
        }

        GridPane.setConstraints(createRelation, 0, y, 2, 1);
    }
}
