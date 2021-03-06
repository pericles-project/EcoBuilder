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
package saver;

import gui.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import relations.CustomRelation;
import relations.Relation;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class loads saved EcoBuilder projects from files into the program. It reconstructs the serialised scenarios.
 */
public class ProjectLoader {
    private final EcoBuilder ecoBuilder;

    public ProjectLoader(EcoBuilder ecoBuilder) {
        this.ecoBuilder = ecoBuilder;
    }

    /**
     * Loads the saved project from a file.
     */
    public void load() {
        File projectFile = openDialog();
        if (projectFile == null) {
            return;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(projectFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ScenarioPanel savedScenario = (ScenarioPanel) objectInputStream.readObject();
            cleanModels();
            loadProject(savedScenario);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Cannot load this file!");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("Cannot load this file!");
                }
            }
        }
    }

    private File openDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project File");
        return fileChooser.showOpenDialog(new Stage());
    }

    /**
     * Cleans the currently loaded models, to prepare them for loading the saved entities.
     */
    public void cleanModels() {
        for (ModelView model : ecoBuilder.scenarioPane.models) {
            for (RadioTemplate radioTemplate : model.childTemplateViews) {
                Iterator<EntityView> iterator = radioTemplate.templateView.childEntities.iterator();
                radioTemplate.templateView.removeAllChildren();
                radioTemplate.setSelected(false);
                radioTemplate.selected = false;
                model.removeTemplate(radioTemplate.templateView.name);
            }
        }
    }

    /**
     * Loads the saved scenario from a de-serialised {@link ScenarioPanel}.
     */
    private void loadProject(ScenarioPanel scenario) {
        loadDVAModelImport(scenario);
        loadModels(scenario);
        loadCustomRelations(scenario);
        loadRelations(scenario);
    }

    /**
     * Was the DVA Model imported? If yes, then import it at the loaded scenario.
     *
     * @param scenario
     */
    private void loadDVAModelImport(ScenarioPanel scenario) {
        if (scenario.addedDVA) {
            ecoBuilder.scenarioPane.addDVA();
        }
    }

    /**
     * Load all models.
     *
     * @param scenario
     */
    private void loadModels(ScenarioPanel scenario) {
        for (ModelView savedModel : scenario.models) {
            if (savedModel instanceof DEMModelView) {
                loadDEMModel((DEMModelView) savedModel);
            } else {
                loadScenarioModel(savedModel);
            }
        }
    }

    /**
     * Load the models that are part of the DEM.
     *
     * @param savedModel
     */
    private void loadDEMModel(DEMModelView savedModel) {
        ecoBuilder.scenarioPane.models.stream()
                .filter(destinationModel -> savedModel.prefix.equals(destinationModel.prefix))
                .forEach(destinationModel -> {
                    loadDEMModel((DEMModelView) destinationModel, savedModel);
                });
    }

    /**
     * Loads all Templates, Entities, Relations, and Targets from a saved source model into the destination model,
     * which is the model that is currently present.
     *
     * @param destination The model to be loaded
     * @param source      The saved model from the file
     */
    private void loadDEMModel(DEMModelView destination, DEMModelView source) {
        source.childTemplateViews.stream().filter(sourceRadioTemplate -> sourceRadioTemplate.selected || sourceRadioTemplate.isSelected()).forEach(sourceRadioTemplate -> {
            RadioTemplate destinationRadioTemplate = destination.getRadioTemplate(sourceRadioTemplate.templateView.name);
            destination.useTemplateView(destinationRadioTemplate.templateView);
            loadTemplateView(destinationRadioTemplate.templateView, sourceRadioTemplate.templateView);
        });
    }

    /**
     * Scenario Model containing the custom templates.
     * <p>
     * For the scenario model the destination model doesn't contain
     * the templates, yet. They have to be created from the scenario
     * source model.
     */
    private void loadScenarioModel(ModelView scenarioSourceModel) {
        ecoBuilder.scenarioPane.cleanScenarioModel();
        for (RadioTemplate sourceRadioTemplate : scenarioSourceModel.childTemplateViews) {
            Set<TemplateView> parents = new HashSet<>();
            for (TemplateView parent : sourceRadioTemplate.templateView.parentTemplates) {
                parents.add(ecoBuilder.scenarioPane.getTemplateView(parent.name));
            }
            CustomTemplateView customTemplateView = ecoBuilder.scenarioPane.createCustomTemplate(parents, sourceRadioTemplate.templateView.name, sourceRadioTemplate.templateView.description);
            loadTemplateView(customTemplateView, sourceRadioTemplate.templateView);
        }
    }

    /**
     * Load the configuration of the source template into the destination template including child entities
     *
     * @param destinationTemplate
     * @param sourceTemplate
     */
    private void loadTemplateView(TemplateView destinationTemplate, TemplateView sourceTemplate) {
        for (EntityView sourceEntity : sourceTemplate.childEntities) {
            EntityView destinationEntity = destinationTemplate.addEntity();
            loadEntityView(destinationEntity, sourceEntity);
        }
    }

    /**
     * Loads the configuration of a source entity into the destination entity.
     *
     * @param destinationEntity
     * @param sourceEntity
     */
    private void loadEntityView(EntityView destinationEntity, EntityView sourceEntity) {
        destinationEntity.updateEntity(sourceEntity.name, sourceEntity.version, sourceEntity.description);
        for (RelationView sourceRelation : sourceEntity.childRelations) {
            for (RangeValueView sourceTarget : sourceRelation.childValueEntities) {
                Relation relation = ecoBuilder.scenarioPane.getRelation(sourceRelation.relation.name);
                destinationEntity.addValueRelation(relation, sourceTarget.value);
            }
            // Note that the entity target relations are created after all models are loaded! Therefore not here! See: loadRelations()
        }
    }

    private void loadCustomRelations(ScenarioPanel sourceScenario) {
        for (CustomRelation sourceRelation : sourceScenario.customRelations) {
            Set<TemplateView> domains = new HashSet<>();
            Set<TemplateView> ranges = new HashSet<>();
            for (TemplateView range : sourceRelation.getParentRanges()) {
                String rangeName = range.name;
                ranges.add(ecoBuilder.scenarioPane.getTemplateView(rangeName));
            }
            for (TemplateView domain : sourceRelation.getParentDomains()) {
                String domainName = domain.name;
                domains.add(ecoBuilder.scenarioPane.getTemplateView(domainName));
            }
            CustomRelation destinationRelation = new CustomRelation(sourceRelation.name, sourceRelation.description, domains, ranges);
            ecoBuilder.scenarioPane.customRelations.add(destinationRelation);
        }
    }

    /**
     * Loading of the entity targets, after the loading of the models.
     * This has to be done in a second step, because the entities already have to be loaded
     * to create the relations which point to them.
     *
     * @param sourceScenario
     */
    private void loadRelations(ScenarioPanel sourceScenario) {
        for (ModelView sourceModel : sourceScenario.models) {
            for (RadioTemplate sourceRadioTemplate : sourceModel.childTemplateViews) {
                for (EntityView sourceEntity : sourceRadioTemplate.templateView.childEntities) {
                    for (RelationView sourceRelation : sourceEntity.childRelations) {
                        for (TargetEntityView sourceTarget : sourceRelation.childTargetEntities) {
                            Relation relation = ecoBuilder.scenarioPane.getRelation(sourceRelation.relation.name);
                            loadRelation(relation, sourceEntity, sourceTarget);
                        }
                    }
                }
            }
        }
    }

    private void loadRelation(Relation relation, EntityView sourceEntity, TargetEntityView sourceTarget) {
        EntityView subject = ecoBuilder.scenarioPane.getEntity(sourceEntity.parentTemplate.name, sourceEntity.name);
        EntityView object = ecoBuilder.scenarioPane.getEntity(sourceTarget.sameEntity.parentTemplate.name, sourceTarget.sameEntity.name);
        if (subject == null || object == null || relation == null) {
            System.err.println("Error: Create relation [" + subject + ", " + relation + ", " + object + "]");
            return;
        }
        subject.addRelationTarget(relation, object);
    }
}
