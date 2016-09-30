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

/**
 * In contrast to a {@link EntityView} this class represents the object of a triple, displayed
 * at the tree as a child of a {@link RelationView} node.
 */
public class TargetEntityView extends GridPane implements Serializable {
    /* The 'sameEntity' links to the entity node which represents exactly the same entity as this one. */
    public EntityView sameEntity;
    transient public final RelationView parentRelation;
    transient private final Hyperlink name;

    public TargetEntityView(EntityView sameEntity, RelationView relation) {
        this.sameEntity = sameEntity;
        this.parentRelation = relation;
        setId(EcoBuilder.TARGET_ENTITY_ID);
        name = new Hyperlink(sameEntity.name);
        name.setOnAction(event -> loadConfiguration());
        Button remove = new Button("X");
        remove.setId(EcoBuilder.REMOVE_BUTTON_ID);
        remove.setOnAction(e -> parentRelation.removeTarget(this));
        setConstraints(name, 0, 0);
        setConstraints(remove, 1, 0);
        getChildren().addAll(name, remove);
    }

    private void loadConfiguration() {
        parentRelation.parentEntity.parentTemplate.parentModel.scenario.ecoBuilder.entityConfiguration.loadConfiguration(sameEntity);
    }

    @Override
    public String toString() {
        return sameEntity.name;
    }

    public void update() {
        name.setText(sameEntity.name);
    }
}
