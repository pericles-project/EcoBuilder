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

import entities.Template;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ModelView extends GridPane implements Serializable {
    transient protected final ScenarioPanel scenario;
    /**
     * All entity templates which were in use for the scenario
     */
    public Set<RadioTemplate> childTemplateViews = new HashSet<>();
    public String prefix;
    transient protected String description;
    protected transient int templateIndex = 1;

    public ModelView(ScenarioPanel scenario, String prefix, String description) {
        this.scenario = scenario;
        this.prefix = prefix;
        this.description = description;
        getStyleClass().add(EcoBuilder.PANE);
        setId(EcoBuilder.MODEL_ID);
        final Hyperlink name = new Hyperlink(prefix + " Model");
        name.setId(EcoBuilder.SUB_TITLE);
        name.setOnAction(e -> loadModelInformation());
        setConstraints(name, 0, 0);
        getChildren().add(name);
    }

    /**
     * Is called by {@link DEMModelView} to create a {@link RadioTemplate} for each {@link Template} in the
     * {@link models.AbstractModel}.
     *
     * @param templateView
     */
    public void addTemplateView(TemplateView templateView) {
        RadioTemplate item = new RadioTemplate(templateView, this);
        childTemplateViews.add(item);
    }

    /**
     * The model view manages a {@link RadioTemplate} for each available template of the model. They manage which
     * templates are used in a given scenario.
     *
     * This method will return the corresponding radio template with the given name.
     * @param name Name of the template
     * @return
     */
    public RadioTemplate getRadioTemplate(String name) {
        for (RadioTemplate radioTemplate : childTemplateViews) {
            if (radioTemplate.templateView.name.equals(name)) {
                return radioTemplate;
            }
        }
        return null;
    }

    /**
     * The {@link RadioTemplate} radio button of a template was selected at the model configuration panel to load the
     * template into the scenario panel for the creation of entities.
     *
     * @param templateView
     */
    public RadioTemplate useTemplateView(TemplateView templateView) {
        if (!getChildren().contains(templateView)) {
            setConstraints(templateView, 0, templateIndex);
            getChildren().add(templateView);
            templateIndex++;
        }
        for (RadioTemplate radioTemplate : childTemplateViews) {
            if (radioTemplate.templateView == templateView) {
                radioTemplate.setSelected(true);
                return radioTemplate;
            }
        }
        return null;
    }

    /**
     * Loads the information about the model into the information area.
     */
    private void loadModelInformation() {
        scenario.ecoBuilder.loadInformation("Model Information: " + prefix, description);
        scenario.ecoBuilder.modelConfiguration.loadConfiguration(this);
    }

    public void removeTemplate(String name) {
        for (RadioTemplate radioTemplate : childTemplateViews) {
            if (radioTemplate.templateView.name.equals(name)) {
                getChildren().remove(radioTemplate.templateView);
                break;
            }
        }
        // Update the index of all templates in the list to prevent holes.
        templateIndex = 1;
        childTemplateViews.stream().filter(ToggleButton::isSelected).forEach(radioTemplate -> {
            setConstraints(radioTemplate.templateView, 0, templateIndex);
            templateIndex++;
        });
    }


    @Override
    public String toString() {
        return prefix;
    }

}
