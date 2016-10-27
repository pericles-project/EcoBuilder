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
package models;

import entities.EcosystemEntity;
import entities.Template;
import gui.*;
import relations.CustomRelation;
import relations.DEMRelation;
import relations.RelationBuilder;

import java.util.Hashtable;
import java.util.Set;

/**
 * The ontology which keeps the individuals belonging to the scenario created by the user. It is created from the
 * {@link ScenarioPanel} directly before the scenario is saved by the {@link saver.ScenarioSaver}.
 */
public class ScenarioModel extends AbstractModel {
    public static final String PREFIX = "DEM-Scenario";
    public static final String DESCRIPTION = "The model which keeps the entity instances created by the user.";
    private Hashtable<CustomRelation, DEMRelation> createdCustomRelations = new Hashtable<>();
    private Hashtable<CustomTemplateView, Template> createdCustomTemplates = new Hashtable<>();

    public ScenarioModel(Set<AbstractModel> toBeImported) {
        super(ScenarioModel.PREFIX, ScenarioModel.DESCRIPTION);
        toBeImported.forEach(this::importModel);
    }

    /**
     * This method is used by the graphical interface to parse the {@link ScenarioPanel} into a scenario model.
     * <p/>
     * Each model is considered separately, so that implementation details can be regarded. The {@link ScenarioModel} is
     * created at save time, and not before, to allow the user to make a modifications before the ontology is generated.
     *
     * @param panel The panel manages the copy of the user created scenario.
     */
    public void createModelFromPanel(ScenarioPanel panel) {
        // Create all custom Templates:
        panel.customTemplates.forEach(this::createCustomTemplate);
        // Create all custom Relations:
        panel.customRelations.forEach(this::createCustomRelation);
        for (ModelView modelView : panel.models) {
            if (modelView instanceof DEMModelView) {
                // Create all entities, which are from DEM Templates:
                createDEMEntities(modelView);
            } else {
                // Create all entities, which are from Custom Templates:
                createScenarioEntities(modelView);
            }
        }
        // Create the relations between all entities:
        panel.models.forEach(this::createModelRelations);
    }

    /**
     * Creates Templates from the custom created template views
     *
     * @param customTemplate
     */
    private void createCustomTemplate(CustomTemplateView customTemplate) {
        TemplateView parent = customTemplate.parentTemplates.iterator().next();
        if (parent instanceof DEMTemplateView) {
            Template template = new Template(this, customTemplate.name, ((DEMTemplateView) parent).template);
            template.addDescription(customTemplate.description);
            createdCustomTemplates.put(customTemplate, template);
            importModel(((DEMModelView) parent.parentModel).model);
        }
    }

    private void createDEMEntities(ModelView model) {
        model.childTemplateViews.stream()
                .filter(radioTemplate -> radioTemplate.templateView instanceof DEMTemplateView).forEach(radioTemplate -> {
            createEntities((DEMTemplateView) radioTemplate.templateView);
        });
    }

    private void createScenarioEntities(ModelView scenarioModelView) {
        for (RadioTemplate radioTemplate : scenarioModelView.childTemplateViews) {
            CustomTemplateView customTemplateView = (CustomTemplateView) radioTemplate.templateView;
            for (EntityView entityView : customTemplateView.childEntities) {
                entityView.entity = new EcosystemEntity(this, entityView.name, createdCustomTemplates.get(customTemplateView));
            }
        }
    }

    /**
     * Creates all relations of the scenario model.
     *
     * @param model
     */
    public void createModelRelations(ModelView model) {
        for (RadioTemplate radioTemplate : model.childTemplateViews) {
            createRelations(radioTemplate.templateView);
        }
    }

    /**
     * Creates individuals (represented by the {@link EcosystemEntity} class) from the entity templates. The references
     * to the created individuals are also saved at the tree representations to be able to create the references from
     * and to the correct individuals afterwards.
     *
     * @param template
     */
    private void createEntities(DEMTemplateView template) {
        for (EntityView entityView : template.childEntities) {
            EcosystemEntity entity = new EcosystemEntity(this, entityView.name, template.template);
            entityView.entity = entity;
            entity.describedBy(entityView.description);
            entity.version(entityView.version);
        }
    }

    /**
     * Creates the relations which point from and to individuals
     *
     * @param template
     */
    private void createRelations(TemplateView template) {
        for (EntityView entity : template.childEntities) {
            for (RelationView relationView : entity.childRelations) {
                if (relationView.relation instanceof DEMRelation) {
                    createDEMRelation(entity, relationView);
                } else if (relationView.relation instanceof CustomRelation) {
                    DEMRelation demRelation = createdCustomRelations.get(relationView.relation);
                    createCustomRelation(entity, relationView, demRelation);
                }
            }
        }
    }

    private void createCustomRelation(EntityView entity, RelationView relationView, DEMRelation demRelation) {
        for (TargetEntityView objectEntity : relationView.childTargetEntities) {
            entity.entity.addProperty(demRelation.property, objectEntity.sameEntity.entity);
        }
        for (RangeValueView rangeValue : relationView.childValueEntities) {
            entity.entity.addProperty(demRelation.property, rangeValue.value);
        }
    }

    private void createDEMRelation(EntityView entity, RelationView relationView) {
        for (TargetEntityView objectEntity : relationView.childTargetEntities) {
            entity.entity.addProperty(((DEMRelation) relationView.relation).property, objectEntity.sameEntity.entity);
        }
        for (RangeValueView rangeValue : relationView.childValueEntities) {
            entity.entity.addProperty(((DEMRelation) relationView.relation).property, rangeValue.value);
        }
    }

    private void createCustomRelation(CustomRelation customRelation) {
        Template firstDomain = null;
        for (TemplateView domain : customRelation.getParentDomains()) {
            if (domain instanceof DEMTemplateView) {
                firstDomain = ((DEMTemplateView) domain).template;
                break;
            }
        }
        if(firstDomain == null){
            return;
        }
        customRelation.getParentDomains().remove(firstDomain);
        RelationBuilder builder = new RelationBuilder(this, customRelation.name, firstDomain)
                .comment(customRelation.description);
        for (TemplateView domain : customRelation.getParentDomains()) {
            //At the moment a custom relation can only have DEMTemplateViews as domain and range. (Not custom templates)
            if (domain instanceof DEMTemplateView) {
                builder = builder.domain(((DEMTemplateView) domain).template);
            }
        }
        for (TemplateView range : customRelation.getParentRanges()) {
            if (range instanceof DEMTemplateView) {
                builder = builder.range(((DEMTemplateView) range).template);
            }
        }
        DEMRelation demRelation = builder.create();
        createdCustomRelations.put(customRelation, demRelation);
    }

    @Override
    public void createModelEntities() {
    }

    @Override
    public void createModelRelations() {
    }
}
