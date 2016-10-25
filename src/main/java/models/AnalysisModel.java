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

import LRMv2.LRM_static_schema;
import entities.*;
import entities.Error;
import entities.Query.QueryTemplate;
import entities.Significance.SignificanceTemplate;
import relations.DEMRelation;
import relations.RelationBuilder;

public class AnalysisModel extends AbstractModel {
    /**
     * The abstract ecosystem templates. These are the abstract entity "classes"
     * of the ontology, which serve to be templates for the creation of real
     * existing "instances". See also the {@link entities} package.
     */
    public static WeightedRelation.WeightedRelationTemplate weightedRelation;
    public static Annotation.AnnotationTemplate annotation;
    public static Purpose.PurposeTemplate purpose;
    public static Scenario.ScenarioTemplate scenario;
    public static EcosystemDependency.DependencyTemplate ecosystemDependency;
    public static Significance.SignificanceTemplate significance;
    public static SemanticDrift.SemanticDriftTemplate semanticDrift;
    public static QueryTemplate query;
    public static Error.ErrorTemplate error;
    public static Warning.WarningTemplate warning;
    public static Tag.TagTemplate tag;
    public static Cost.CostTemplate cost;

    public static DEMRelation hasAnnotation;
    public static DEMRelation annotates;
    public static DEMRelation hasPurpose;
    public static DEMRelation purposeOf;
    public static DEMRelation hasValue;
    public static DEMRelation calculatedBy;
    public static DEMRelation taggedBy;
    public static DEMRelation tags;
    public static DEMRelation hasCost;
    public static DEMRelation costOf;
    public static DEMRelation hasSignificance;

    public AnalysisModel() {
        super("DEM-Analysis", "The analysis model provides entities to support the analysis of " +
                "the Digital Ecosystem Model.");
    }

    @Override
    public void createModelEntities() {
        weightedRelation = new WeightedRelation.WeightedRelationTemplate(this);
        annotation = new Annotation.AnnotationTemplate(this);
        purpose = new Purpose.PurposeTemplate(this);
        scenario = new Scenario.ScenarioTemplate(this);
        ecosystemDependency = new EcosystemDependency.DependencyTemplate(this);
        significance = new SignificanceTemplate(this);
        semanticDrift = new SemanticDrift.SemanticDriftTemplate(this);
        query = new QueryTemplate(this);
        error = new Error.ErrorTemplate(this);
        warning = new Warning.WarningTemplate(this);
        tag = new Tag.TagTemplate(this);
        cost = new Cost.CostTemplate(this);
    }

    @Override
    public void createModelRelations() {
        hasAnnotation = new RelationBuilder(this, "hasAnnotation", CoreModel.ecosystemEntity)
                .comment("The ecosystem entity has an annotation.")
                .range(annotation).create();
        annotates = new RelationBuilder(this, "annotates", annotation).comment("Annotates an ecosystem entity.")
                .range(CoreModel.ecosystemEntity).inverse(hasAnnotation).create();
        hasPurpose = new RelationBuilder(this, "hasPurpose", CoreModel.ecosystemEntity).comment("The ecosystem entity has a specific purpose.")
                .range(purpose).superRelation(LRM_static_schema.intention).create();
        purposeOf = new RelationBuilder(this, "purposeOf", purpose).comment("The purpose refers to an ecosystem entity.")
                .range(CoreModel.ecosystemEntity).inverse(hasPurpose).create();
        hasValue = new RelationBuilder(this, "hasValue", weightedRelation).comment("Assigns a weight to a weighted relation.")
                .create();
        calculatedBy = new RelationBuilder(this, "calculatedBy", weightedRelation)
                .comment("The value of the weighted relation was calculated or measured by the referred entity.")
                .range(CoreModel.ecosystemEntity).create();
        taggedBy = new RelationBuilder(this, "taggedBy", CoreModel.ecosystemEntity)
                .comment("This entity is tagged by a tag.")
                .range(tag).create();
        tags = new RelationBuilder(this, "tags", tag)
                .comment("This tag tags the referred entities")
                .range(CoreModel.ecosystemEntity).inverse(taggedBy).create();
        costOf = new RelationBuilder(this, "costOf", cost).comment("This value is the cost value of the referred entity.")
                .range(CoreModel.ecosystemEntity).create();
        hasCost = new RelationBuilder(this, "hasCost", CoreModel.ecosystemEntity).comment("This entity has a cost value")
                .range(cost).inverse(costOf).create();
        hasSignificance = new RelationBuilder(this, "hasSignificance", CoreModel.ecosystemEntity).comment("This enity has a significance")
                .range(significance).create();

        //TODO: relation inheritance
//        weightedRelation.addPossibleRelation(LRM_static_schema.from_property, CoreModel.ecosystemEntity);
//        weightedRelation.addPossibleRelation(LRM_static_schema.to_property, CoreModel.ecosystemEntity);
    }
}
