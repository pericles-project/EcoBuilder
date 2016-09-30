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

import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ModelConfiguration {

    private final Pane configurationPane;

    public ModelConfiguration(Pane configurationPane) {
        this.configurationPane = configurationPane;
    }

    /**
     * Configure which templates of the model to use
     *
     * @param modelView
     */
    public void loadConfiguration(ModelView modelView) {
        configurationPane.getChildren().removeAll(configurationPane.getChildren());
        Text title = new Text("Template Selection for " + modelView.prefix + " Model");
        title.setId(EcoBuilder.SUB_TITLE);
        Text description = new Text("Select the entity templates to use them in the scenario:");
        ListView<RadioTemplate> templateSelectionList = new ListView<>();
        for (RadioTemplate radioTemplate : modelView.childTemplateViews) {
            templateSelectionList.getItems().add(radioTemplate);
        }
        templateSelectionList.setMinWidth(550);
        templateSelectionList.setPrefWidth(550);
        GridPane.setConstraints(title, 0, 0);
        GridPane.setConstraints(description, 0, 1);
        GridPane.setConstraints(templateSelectionList, 0, 2);
        configurationPane.getChildren().addAll(title, description, templateSelectionList);
    }
}
