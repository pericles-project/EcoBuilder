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

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

public class TemplateConfiguration {
    private final Pane configurationPane;
    private final ScenarioPanel scenarioPane;

    public TemplateConfiguration(Pane configurationPane, ScenarioPanel scenarioPane) {
        this.configurationPane = configurationPane;
        this.scenarioPane = scenarioPane;
    }

    public void loadConfiguration(TemplateView templateView) {
        configurationPane.getChildren().removeAll(configurationPane.getChildren());
        Text title = new Text("Selected " + templateView);
        title.setId(EcoBuilder.SUB_TITLE);
        Text parent = new Text("");
        if (templateView.parentTemplates != null && templateView.parentTemplates.size() > 0) {
            parent = new Text("This template is a subclass of the templates: " + templateView.parentTemplates);
        }
        Text label = new Text("\nYou can create an own customised subclass template for your scenario.");
        Text nameLable = new Text("Entity Name:");
        TextField nameField = new TextField();
        Text descriptionLabel = new Text("Entity Description:");
        TextArea description = new TextArea();
        description.setWrapText(true);
        Button add = new Button("Create Custom Template");
        Set<TemplateView> parents = new HashSet<>();
        parents.add(templateView);
        add.setOnAction(e -> scenarioPane.createCustomTemplate(parents, nameField.getText(), description.getText()));
        GridPane.setConstraints(title, 0, 0);
        GridPane.setConstraints(parent, 0, 1);
        GridPane.setConstraints(label, 0, 2);
        GridPane.setConstraints(nameLable, 0, 3);
        GridPane.setConstraints(nameField, 0, 4);
        GridPane.setConstraints(descriptionLabel, 0, 5);
        GridPane.setConstraints(description, 0, 6);
        GridPane.setConstraints(add, 0, 7);
        configurationPane.getChildren().addAll(title, parent, label, nameLable, nameField, descriptionLabel, description, add);
    }
}
