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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.AbstractModel;
import models.CoreModel;
import models.DEM;
import models.ScenarioModel;
import relations.CustomRelation;
import relations.DEMRelation;
import relations.Relation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is the scenario panel which contains the model views with the templates, created entities and relations.
 */
public class ScenarioPanel extends GridPane implements Serializable {
    transient public final EcoBuilder ecoBuilder;
    transient private final Text title = new Text("Scenario Model");
    private final ModelView scenarioModel;
    public final Set<ModelView> models = new HashSet<>();
    public final Set<CustomTemplateView> customTemplates = new HashSet<>();
    public final Set<CustomRelation> customRelations = new HashSet<>();
    public Set<AbstractModel> toBeImported = new HashSet<>();
    private transient TextArea dvaLabel = new TextArea("The Digital Video Artwork (DVA) ontology introduces concepts for modelling video artworks." +
            "Importing the DVA ontology introduces new relations also for existing entities:");
    private transient Button addDVA = new Button("Import DVA ontology");
    public boolean addedDVA = false;

    public ScenarioPanel(EcoBuilder ecoBuilder) {
        this.ecoBuilder = ecoBuilder;
        addDVA.setOnAction(e -> addDVA());
        dvaLabel.setEditable(false);
        dvaLabel.setMaxHeight(60);
        dvaLabel.setPrefHeight(60);
        dvaLabel.setWrapText(true);
        setId(EcoBuilder.SCENARIO_ID);
        title.setId(EcoBuilder.TITLE_ID);
        getStyleClass().add(EcoBuilder.PANE);
        setConstraints(title, 0, 0);
        getChildren().add(title);
        int i = 1;
        for (AbstractModel model : DEM.getModels()) {
            DEMModelView view = new DEMModelView(model, this);
            models.add(view);
            setConstraints(view, 0, i);
            getChildren().add(view);
            i++;
        }
        transferRelationDomains();
        transferParents(models);
        scenarioModel = new ModelView(this, ScenarioModel.PREFIX, ScenarioModel.DESCRIPTION);
        models.add(scenarioModel);
        setConstraints(scenarioModel, 0, i);
        i++;
        setConstraints(dvaLabel, 0, i);
        i++;
        setConstraints(addDVA, 0, i);
        getChildren().addAll(scenarioModel, dvaLabel, addDVA);
    }

    /**
     * The TemplateView ranges and domains need to be updated (These are used also by the custom relations later):
     * <p>
     * For each domainTemplate and rangeTemplate in the {@link DEMRelation}, add the corresponding domain or range
     * to the {@link Relation} domain/range set.
     */
    private void transferRelationDomains() {
        models.stream().filter(model -> model instanceof DEMModelView).forEach(model -> {
            transferDomainsAndRanges((DEMModelView) model);
        });
    }

    private void transferDomainsAndRanges(DEMModelView modelView) {
        for (Relation relation : modelView.model.relations) {
            for (Template domain : ((DEMRelation) relation).getParentTemplateDomains()) {
                TemplateView domainTemplateView = getTemplateView(domain.name);
                if (domainTemplateView != null) {
                    relation.addDomain(domainTemplateView);
                } else {
                    System.err.println("Couldn't find domain template view " + domain.name);
                }
            }
            for (Template range : ((DEMRelation) relation).getParentTemplateRanges()) {
                TemplateView rangeTemplateView = getTemplateView(range.name);
                if (rangeTemplateView != null) {
                    relation.addRange(rangeTemplateView);
                } else {
                    System.err.println("Couldn't find range template view " + range.name);
                }
            }
        }
    }

    private void transferParents(Set<ModelView> models) {
        models.stream().filter(model -> model instanceof DEMModelView).forEach(model -> {
            transferParents((DEMModelView) model);
        });
    }

    private void transferParents(DEMModelView model) {
        for (RadioTemplate radioTemplate : model.childTemplateViews) {
            DEMTemplateView templateView = (DEMTemplateView) radioTemplate.templateView;
            for (Template parent : templateView.template.parents) {
                TemplateView parentView = getTemplateView(parent.name);
                templateView.parentTemplates.add(parentView);
            }
        }
    }

    /**
     * Imports the Digital Video Artwork domain ontology.
     */
    public void addDVA() {
        DEM.addedDVA = true;
        addedDVA = true;
        DEMModelView dvaModel = new DEMModelView(DEM.DVA_MODEL, this);
        models.add(dvaModel);
        int i = getChildren().indexOf(addDVA);
        setConstraints(dvaModel, 0, i);
        getChildren().remove(addDVA);
        getChildren().add(dvaModel);
        transferRelationDomains();
        transferParents(models);
    }

    /**
     * The user has clicked on the Button to create a custom template. This method will create a custom template from
     * teh input given by the user.
     *
     * @param parents     superclass of the custom template selected by the user
     * @param name        name of the custom template specified by the user
     * @param description description of the custom template specified by the user
     */
    public CustomTemplateView createCustomTemplate(Set<TemplateView> parents, String name, String description) {
        if (name == null || name.equals("") || name.equals(" ")) {
            return null;
        }
        CustomTemplateView customTemplate = new CustomTemplateView(parents, name, description, scenarioModel);
        customTemplates.add(customTemplate);
        scenarioModel.addTemplateView(customTemplate);
        scenarioModel.useTemplateView(customTemplate);

        return customTemplate;
    }

    public Relation getRelation(String relationName) {
        for (ModelView model : models) {
            if (model instanceof DEMModelView) {
                for (Relation relation : ((DEMModelView) model).model.relations) {
                    if (relation.name.equals(relationName)) {
                        return relation;
                    }
                }
            } else { // Scenario model
                for (Relation relation : customRelations) {
                    if (relation.name.equals(relationName)) {
                        return relation;
                    }
                }
            }
        }
        return null;
    }

    public EntityView getEntity(String templateName, String entityName) {
        TemplateView templateView = getTemplateView(templateName);
        if (templateView == null) {
            return null;
        }
        for (EntityView entity : templateView.childEntities) {
            if (entity.name.equals(CoreModel.sanitizeName(entityName))) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Get the template view if you only have the name.
     *
     * @param name
     * @return
     */
    public TemplateView getTemplateView(String name) {
        for (ModelView model : models) {
            for (RadioTemplate radioTemplate : model.childTemplateViews) {
                if (radioTemplate.templateView.name.equals(name)) {
                    return radioTemplate.templateView;
                }
            }
        }
        return null;
    }

    /**
     * Returns a model for a given prefix
     *
     * @param prefix the prefix of the model
     * @return the model
     */
    public ModelView getModel(String prefix) {
        for (ModelView model : models) {
            if (model instanceof DEMModelView) {
                if (((DEMModelView) model).model.prefix.equals(prefix)) {
                    return model;
                }
            }
        }
        return scenarioModel;
    }

    /**
     * Returns all existing relations.
     *
     * @return set of all existing relations
     */
    public Set<Relation> getAllRelations() {
        Set<Relation> relations = new HashSet<>();
        relations.addAll(customRelations);
        for (AbstractModel model : DEM.getModels()) {
            relations.addAll(model.relations);
        }
        return relations;
    }

    /**
     * Returns a set of all relations for which the template is a domain.
     *
     * @param templateView view representing the template
     * @return set of possible relations usable "from" this template
     */
    public Set<Relation> getPossibleRelations(TemplateView templateView) {
        Set<Relation> possibleRelations = new HashSet<>();
        for (Relation relation : getAllRelations()) {
            possibleRelations.addAll(relation.getAllDomains(this).stream()
                    .filter(domain -> domain == templateView).map(domain -> relation).collect(Collectors.toList()));
        }
        return possibleRelations;
    }

    /**
     * Returns all existing templates
     *
     * @return A set of all existing templates.
     */
    public Set<TemplateView> getAllTemplates() {
        Set<TemplateView> templates = new HashSet<>();
        for (ModelView model : models) {
            templates.addAll(model.childTemplateViews.stream()
                    .map(radioTemplate -> radioTemplate.templateView).collect(Collectors.toList()));
        }
        return templates;
    }

    /**
     * Cleans the scenario model from all custom templates which are currently added.
     */
    public void cleanScenarioModel() {
        customRelations.clear();
        customTemplates.clear();
        ModelView scenarioModel = getScenarioModel();
        if (scenarioModel == null) {
            System.err.println("No scenario model found...");
            return;
        }
        scenarioModel.childTemplateViews.clear();
        while (scenarioModel.getChildren().size() > 1) {
            scenarioModel.getChildren().remove(1);
        }
        scenarioModel.templateIndex = 1;
    }

    protected ModelView getScenarioModel() {
        for (ModelView model : models) {
            if (!(model instanceof DEMModelView)) {
                return model;
            }
        }
        return null;
    }

    /**
     * A relation was used in the scenario. Therefore the model of the relation has to be imported by the scenario.
     *
     * @param relation
     */
    public void importRelationsModel(Relation relation) {
        for (AbstractModel model : DEM.getModels()) {
            for (Relation modelRelation : model.relations) {
                if (modelRelation == relation) {
                    toBeImported.add(model);
                }
            }
        }
    }
}
