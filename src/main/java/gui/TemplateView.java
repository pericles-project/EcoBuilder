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
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TemplateView extends GridPane implements Serializable {
    public ModelView parentModel;
    transient private final Button add;
    public String name;
    public String description;
    public Set<EntityView> childEntities = new HashSet<>();
    public Set<TemplateView> parentTemplates = new HashSet<>();

    public TemplateView(String name, String description, ModelView parentModel) {
        this.parentModel = parentModel;
        this.name = name;
        this.description = description;
        getStyleClass().add(EcoBuilder.PANE);
        setId(EcoBuilder.TEMPLATE_ID);
        final Hyperlink link = new Hyperlink(this.toString());
        link.setOnAction(e -> loadInformation());
        add = new Button("New Entity");
        add.setId(EcoBuilder.ADD_BUTTON_ID);
        add.setOnAction(e -> addEntity());
        setConstraints(link, 0, 0);
        setConstraints(add, 0, 1);
        getChildren().addAll(link, add);
    }

    /**
     * Load information about this template into the graphical information view, once the
     * template is selected.
     */
    private void loadInformation() {
        EcoBuilder ecoBuilder = parentModel.scenario.ecoBuilder;
        ecoBuilder.loadInformation("Template Information: " + name, description);
        ecoBuilder.templateConfiguration.loadConfiguration(this);
    }

    public EntityView addEntity() {
        EntityView entity = new EntityView(this);
        setConstraints(entity, 0, childEntities.size() + 1);
        setConstraints(add, 0, childEntities.size() + 2); //The add button after all entities
        getChildren().add(entity);
        childEntities.add(entity);
        return entity;
    }

    public void remove(EntityView entity) {
        this.getChildren().remove(entity);
        childEntities.remove(entity);
        setConstraints(add, 0, childEntities.size() + 2);
        int entityIndex = 1;
        for (EntityView view : childEntities) {
            setConstraints(view, 0, entityIndex);
            entityIndex++;
        }
    }

    @Override
    public String toString() {
        return "Template: " + name;
    }
}
