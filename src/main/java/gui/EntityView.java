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

import entities.EcosystemEntity;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import models.CoreModel;
import relations.Relation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EntityView extends GridPane implements Serializable {
    public TemplateView parentTemplate;

    public EcosystemEntity entity;

    public String name = "";
    public String description = "";
    public String version = "";

    public Set<RelationView> childRelations = new HashSet<>();

    transient private final HBox buttonBox = new HBox();
    transient private final Button removeEntity = new Button("x");
    transient private final Text relationLabel = new Text("Relation");
    transient private final Text targetLabel = new Text("Target Entity");

    transient private final Hyperlink nameText;

    public EntityView(final TemplateView parentTemplate) {
        this.parentTemplate = parentTemplate;
        this.name = "new" + parentTemplate.name;
        nameText = new Hyperlink(name);
        buttonBox.getChildren().add(removeEntity);
        buttonBox.setId("hbox");
        configure();
        setConstraints();
        addElements();
    }

    private void addElements() {
        getChildren().addAll(nameText, buttonBox, relationLabel, targetLabel);
    }

    private void configure() {
        nameText.setOnAction(event -> loadConfiguration());
        setId(EcoBuilder.ENTITY_ID);
        getStyleClass().add(EcoBuilder.PANE);
        removeEntity.setId(EcoBuilder.REMOVE_BUTTON_ID);
        removeEntity.setOnAction(e -> removeThisEntity());
    }

    private void setConstraints() {
        setConstraints(nameText, 0, 0, 3, 1);
        setConstraints(buttonBox, 3, 0);
        setConstraints(relationLabel, 1, 1);
        setConstraints(targetLabel, 2, 1);
    }

    private void loadConfiguration() {
        parentTemplate.parentModel.scenario.ecoBuilder.entityConfiguration.loadConfiguration(this);
    }

    /**
     * Remove this entity from the scenario
     */
    private void removeThisEntity() {
        parentTemplate.remove(this);
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * This method is called, if the user pressed the X-Button at the {@link RelationView} to removeValueTarget the relation. It
     * removes the whole relation view with all targets.
     *
     * @param relation
     */
    public void removeRelation(RelationView relation) {
        childRelations.remove(relation);
        getChildren().remove(relation);
        int relationIndex = 1;
        for (RelationView view : childRelations) {
            setConstraints(view, 1, relationIndex, 2, 1);
            relationIndex++;
        }
    }

    /**
     * Create the relation triple by adding the selected relation and the selected target entity to the entity.
     * This triple only exists in the view, but will be loaded to the ontology once the model is saved.
     */
    public void addRelationTarget(Relation relation, EntityView target) {
        if (target == null || relation == null) {
            System.err.println("Error: Adding relation " + relation + " to target " + target);
            return;
        }
        // If there is already a relation of this type, add the new relation to the existing view
        for (RelationView relationView : childRelations) {
            if (relationView.relation.name.equals(relation.name)) {
                // If there is a relation of this type, check if the target is already there --> then return
                for (TargetEntityView targetEntityView : relationView.childTargetEntities) {
                    if (targetEntityView.sameEntity == target) {
                        return;
                    }
                }
                relationView.addTarget(target);
                if (relation.inverse != null) {
                    createTheInverse(target, relation.inverse);
                }
                return;
            }
        }
        addNewRelation(relation, target);
    }

    /**
     * OBJECT: This entity
     * <p>
     * 1. check if the subject already has an {@link RelationView} for the predicate
     * 2. --> Otherwise create new RelationView
     * 3. Then create the relation
     *
     * @param subject   SUBJECT     (target)
     * @param predicate PREDICATE   (relation.inverse)
     */
    private void createTheInverse(EntityView subject, Relation predicate) {
        for (RelationView inverseView : subject.childRelations) {
            if (inverseView.relation == predicate) {
                // The relation view already exists, add another target:
                inverseView.addTarget(this);
                return;
            }
        }
        // The relation view doesn't exist yet. Create new one:
        RelationView inverseRelation = new RelationView(predicate, subject, this);
        subject.addRelationView(inverseRelation);
    }

    private RelationView addNewRelation(Relation relation, EntityView target) {
        RelationView relationView = new RelationView(relation, this, target);
        addRelationView(relationView);
        if (relation.inverse != null) {
            createTheInverse(target, relation.inverse);
        }
        return relationView;
    }

    /**
     * Add a relation target value
     *
     * @param relation
     * @param value
     */
    public void addValueRelation(Relation relation, String value) {
        if (value == null || value.trim().equals("") || relation == null) {
            return;
        }
        value = value.trim();
        // If there is already a relation of this type, add the new relation to the existing view
        for (RelationView relationView : childRelations) {
            if (relationView.relation.name.equals(relation.name)) {
                relationView.addValueTarget(value);
                return;
            }
        }
        RelationView relationView = new RelationView(relation, this, value);
        addRelationView(relationView);
    }

    /**
     * Adds a new relation view to an entity view.
     *
     * @param relationView
     */
    public void addRelationView(RelationView relationView) {
        setConstraints(relationView, 1, childRelations.size() + 1, 2, 1);
        childRelations.add(relationView);
        getChildren().add(relationView);
        parentTemplate.parentModel.scenario.importRelationsModel(relationView.relation);
    }


    /**
     * Especially if the name of an entity was changed, the name has to be updated everywhere in the GUI.
     *
     * @param name
     * @param version
     * @param description
     */
    public void updateEntity(String name, String version, String description) {
        this.name = CoreModel.sanitizeName(name);
        this.version = version;
        this.description = description;
        this.updateView();
        // Update the name of this entity, where the entity is a target!
        for (ModelView model : parentTemplate.parentModel.scenario.models) {
            for (RadioTemplate radioTemplate : model.childTemplateViews) {
                for (EntityView entityView : radioTemplate.templateView.childEntities) {
                    for (RelationView relation : entityView.childRelations) {
                        relation.childTargetEntities.stream().filter(target -> target.sameEntity == this)
                                .forEach(TargetEntityView::update);
                    }
                }
            }
        }
    }

    public void updateView() {
        nameText.setText(name);
    }

}
