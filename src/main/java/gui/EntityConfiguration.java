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

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import relations.CustomRelation;
import relations.Relation;

import static gui.EcoBuilder.SUB_TITLE;

public class EntityConfiguration {
    private final Pane configurationPane;
    private final ScenarioPanel scenarioPane;
    private final CustomRelationCreation customRelationCreation;
    private final ComboBox<Relation> relationBox = new ComboBox<>();
    private final ComboBox<EntityView> existingEntitiesBox = new ComboBox<>();
    private final ComboBox<TargetView> entitiesFromTemplatesBox = new ComboBox<>();
    private final Text configurationTitle = new Text("Entity Configuration");
    private final Text nameLabel = new Text("Name: ");
    private final Text versionLabel = new Text("Version: ");
    private final Text descriptionLabel = new Text("Description: ");
    private final Text subTitle = new Text("Chose either an existing entity as target:");
    private final Text subTitle2 = new Text("Or create a new entity as target:");
    private final Text subTitle3 = new Text("You can also enter a text as target:");
    private final Text subTitle4 = new Text("Or create a custom relation:");
    private final Text relationBoxLabel = new Text("Select a Relation: ");
    private final Text relationTitle = new Text("Create a relation from this entity");
    private final TextArea relationDescription = new TextArea();
    private final Button saveChanges = new Button("Save Changes");
    private final Button addRelationToExistingEntity = new Button("Add Relation");
    private final Button createNewEntityFromTemplate = new Button("Create New Entity");
    private final Button addText = new Button("Add Text");
    private final Button customRelation = new Button("Create Custom Relation");
    private final Separator separator = new Separator();
    private final Separator separator2 = new Separator();
    private final TextArea descriptionArea = new TextArea();
    private Relation selectedRelation;
    private EntityView loadedEntity;
    private TextField nameField;
    private TextField versionField;
    private TextField valueTarget = new TextField();

    public EntityConfiguration(Pane configurationPane, ScenarioPanel scenarioPane) {
        this.configurationPane = configurationPane;
        this.scenarioPane = scenarioPane;
        this.customRelationCreation = new CustomRelationCreation(scenarioPane);
        setIDs();
        configureElements();
        addListeners();
    }

    private void setIDs() {
        configurationTitle.setId(SUB_TITLE);
        relationTitle.setId(SUB_TITLE);
        addRelationToExistingEntity.setId(EcoBuilder.ADD_BUTTON_ID);
        createNewEntityFromTemplate.setId(EcoBuilder.ADD_BUTTON_ID);
        addText.setId(EcoBuilder.ADD_BUTTON_ID);
        customRelation.setId(EcoBuilder.ADD_BUTTON_ID);
        existingEntitiesBox.setId(EcoBuilder.BOX);
        entitiesFromTemplatesBox.setId(EcoBuilder.BOX);
        relationBox.setId(EcoBuilder.BOX);
    }

    private void configureElements() {
        descriptionArea.setWrapText(true);
        relationDescription.setEditable(false);
        relationDescription.setMaxHeight(80);
        relationDescription.setPrefHeight(80);
        relationDescription.setWrapText(true);
        existingEntitiesBox.setButtonCell(new EntityViewListCell());
        existingEntitiesBox.setCellFactory(p -> new EntityViewListCell());
    }

    /**
     * Load the configuration panel of an entity
     *
     * @param entity The entity for which the configuration options will be loaded.
     */
    public void loadConfiguration(final EntityView entity) {
        this.loadedEntity = entity;
        configurationPane.getChildren().clear();
        scenarioPane.ecoBuilder.loadInformation("Selected Entity: " + entity.name, "Configure the selected " +
                "entity or add a relation form this entity.\n");
        nameField = new TextField(entity.name);
        versionField = new TextField(entity.version);
        descriptionArea.setText(entity.description);
        updateRelationConfiguration();
        setConstraints();
        addAll();
    }

    private void updateRelationConfiguration() {
        entitiesFromTemplatesBox.getItems().clear();
        existingEntitiesBox.getItems().clear();
        valueTarget.setText("");
        relationDescription.setText("");
        fillRelationBox();
    }

    private void addListeners() {
        saveChanges.setOnAction(e -> {
            saveChanges();
        });
        addRelationToExistingEntity.setOnAction(event -> {
            EntityView selectedTarget = existingEntitiesBox.getSelectionModel().getSelectedItem();
            if (selectedTarget != null) {
                loadedEntity.addRelationTarget(selectedRelation, selectedTarget);
            }
        });
        relationBox.valueProperty().addListener((observable, oldRelation, selectedRelation) -> {
            updateSelectedRelation(oldRelation, selectedRelation);
        });
        createNewEntityFromTemplate.setOnAction(event -> {
            createNewEntityAsTarget();
        });
        addText.setOnAction(event -> loadedEntity.addValueRelation(selectedRelation, valueTarget.getText()));
        customRelation.setOnAction(event -> customRelationCreation.createCustomRelation(this));
    }

    /**
     * This is called by the {@link CustomRelationCreation} dialog, once a {@link CustomRelation} was created.
     * The relation has to be added.
     *
     * @param customRelation the created relation
     */
    public void createCustomRelation(CustomRelation customRelation) {
        if (customRelation == null) {
            return;
        }
        scenarioPane.customRelations.add(customRelation);
        fillRelationBox();
        if (relationBox.getItems().contains(customRelation)) {
            relationBox.getSelectionModel().select(customRelation);
            relationDescription.setText(customRelation.description);
        }
    }

    /**
     * The relation box contains all relations which can have the selected entity as domain. This method fills the box
     * once a new entity is selected.
     */
    private void fillRelationBox() {
        relationBox.getItems().clear();
        relationBox.getItems().addAll(scenarioPane.getPossibleRelations(loadedEntity.parentTemplate));
    }

    private void addAll() {
        configurationPane.getChildren().addAll(configurationTitle, nameLabel, nameField, versionLabel, versionField,
                descriptionLabel, descriptionArea, saveChanges, separator, relationTitle, relationBoxLabel, relationBox,
                subTitle,
                subTitle2, existingEntitiesBox, addRelationToExistingEntity, relationDescription,
                entitiesFromTemplatesBox, createNewEntityFromTemplate,
                subTitle3, valueTarget, addText, separator2, subTitle4, customRelation);
    }

    private void setConstraints() {
        GridPane.setConstraints(configurationTitle, 0, 0, 2, 1);
        GridPane.setConstraints(nameLabel, 0, 1, 1, 1);
        GridPane.setConstraints(nameField, 1, 1, 1, 1);
        GridPane.setConstraints(versionLabel, 0, 2, 1, 1);
        GridPane.setConstraints(versionField, 1, 2, 1, 1);
        GridPane.setConstraints(descriptionLabel, 0, 3, 2, 1);
        GridPane.setConstraints(descriptionArea, 0, 4, 2, 1);
        GridPane.setConstraints(saveChanges, 0, 5, 2, 1);
        GridPane.setConstraints(separator, 0, 6, 2, 1);
        GridPane.setConstraints(relationTitle, 0, 7, 2, 1);
        GridPane.setConstraints(relationBoxLabel, 0, 8, 1, 1);
        GridPane.setConstraints(relationBox, 1, 8, 1, 1);
        GridPane.setConstraints(relationDescription, 0, 9, 2, 1);
        GridPane.setConstraints(subTitle, 0, 10, 1, 1);
        GridPane.setConstraints(existingEntitiesBox, 1, 10, 1, 1);
        GridPane.setConstraints(addRelationToExistingEntity, 1, 11, 2, 1);
        GridPane.setConstraints(subTitle2, 0, 12, 1, 1);
        GridPane.setConstraints(entitiesFromTemplatesBox, 1, 12, 1, 1);
        GridPane.setConstraints(createNewEntityFromTemplate, 1, 13, 1, 1);
        GridPane.setConstraints(subTitle3, 0, 14, 1, 1);
        GridPane.setConstraints(valueTarget, 1, 14, 1, 1);
        GridPane.setConstraints(addText, 1, 15, 1, 1);
        GridPane.setConstraints(separator2, 0, 16, 2, 1);
        GridPane.setConstraints(subTitle4, 0, 17, 1, 1);
        GridPane.setConstraints(customRelation, 1, 17, 1, 1);
    }

    /**
     * A new relationLabel was selected
     * 1. the list of existing target entities has to be updated.
     * 2. the list of possible templates has to be updated.
     *
     * @param oldRelation      The name of the relation which was selected before
     * @param selectedRelation the currently selected relation
     */
    private void updateSelectedRelation(Relation oldRelation, Relation selectedRelation) {
        if (selectedRelation == null || oldRelation == selectedRelation) {
            return;
        }
        this.selectedRelation = selectedRelation;
        relationDescription.setText(selectedRelation.description);
        updateExistingEntitiesBox(this.selectedRelation);
        entitiesFromTemplatesBox.getItems().clear();
        for (TemplateView range : selectedRelation.getAllRanges(scenarioPane)) {
            entitiesFromTemplatesBox.getItems().add(new TargetView(range));
        }
    }

    /**
     * The entity configuration was changed and the user clicked the button to save the changes.
     */
    private void saveChanges() {
        loadedEntity.updateEntity(nameField.getText(), versionField.getText(), descriptionArea.getText());
        scenarioPane.ecoBuilder.loadInformation("Entity Configuration Saved", "The entity configuration was saved.");
    }

    /**
     * The user has clicked the button to createNewEntityFromTemplate a new entity from the selected target template. The entity will be
     * used as target for the selected relation.
     */
    private void createNewEntityAsTarget() {
        if (entitiesFromTemplatesBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        TemplateView templateView = entitiesFromTemplatesBox.getSelectionModel().getSelectedItem().templateView;
        EntityView newCreatedEntity = templateView.addEntity();
        loadedEntity.addRelationTarget(selectedRelation, newCreatedEntity);
        templateView.parentModel.useTemplateView(templateView);
    }

    /**
     * A relation was selected therefore the box which contains the possible targets of this relation has to be
     * updated.
     * This method adds all entities which are created from templates in the range of the relation ot the box.
     *
     * @param selectedRelation the selected relation
     */
    private void updateExistingEntitiesBox(Relation selectedRelation) {
        if (selectedRelation == null) {
            System.err.println("No relation selected.");
            return;
        }
        existingEntitiesBox.getItems().clear();
        for (TemplateView view : selectedRelation.getAllRanges(scenarioPane)) {
            existingEntitiesBox.getItems().addAll(view.childEntities);
        }
        existingEntitiesBox.getItems().remove(loadedEntity); // ensure that the loaded entity is not in the target list
    }

    private class EntityViewListCell extends ListCell<EntityView> {
        @Override
        protected void updateItem(EntityView item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.name);
            }
        }
    }
}
