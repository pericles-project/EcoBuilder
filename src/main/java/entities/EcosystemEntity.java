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
package entities;

import LRMv2.LRM_semantic_versioning_schema;
import LRMv2.LRM_static_schema;
import models.CoreModel;
import models.ScenarioModel;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDFS;

import java.io.Serializable;

/**
 * To be extended by any ecosystem Resource.
 */
public class EcosystemEntity implements Individual, Serializable {
    transient private final Individual individual;
    transient protected final ScenarioModel model;
    // The abstract type of this concrete resource:
    transient public final Template template;
    public final String name;


    /**
     * The individuals will belong to the scenario model.
     *
     * @param scenario The scenario model.
     * @param name     The name is added as label to the individual. An ID is created from the name by sanitization.
     * @param template The template for this individual entity.
     */
    public EcosystemEntity(ScenarioModel scenario, String name, Template template) {
        this.model = scenario;
        this.name = template.resource.getLocalName();
        this.template = template;
        // This ensures that the sub-model of the template class will be imported by the scenario model:
        scenario.importModel(template.model);
        this.individual = scenario.model.createIndividual(
                scenario.namespace + model.sanitizeName(name), template);
        addLabel(name);
    }

    /**
     * Creates an anonymous individual of the given class and adds it to the scenario model.
     *
     * @param scenario The scenario model contains the individual entities which are created by the user.
     * @param entity   The entity that should be added to the scenario model.
     */
    public EcosystemEntity(ScenarioModel scenario, Template entity) {
        this.model = scenario;
        this.name = entity.resource.getLocalName();
        this.template = entity;
        scenario.importModel(template.model);
        this.individual = scenario.model.createIndividual(template);
        addLabel(name);
    }

    /**
     * Creates an individual of an ecosystem entity.
     *
     * @param scenario   The scenario model
     * @param identifier The ID of the individual
     */
    public EcosystemEntity(ScenarioModel scenario, String identifier) {
        this(scenario, identifier, CoreModel.ecosystemEntity);
    }


    public void id(String id) {
        addProperty(LRM_static_schema.identification,
                model.model.createIndividual(LRM_static_schema.Identity).addProperty(LRM_static_schema.definition, id));
    }

    public void name(String name) {
        setLabel(name);
    }

    // lrm:label points from Version to VersionVector?
    // major,minor,micro point from VersionedResource to int
    // //[1.2.0:3]/35
    // TODO: currently label and major, minor etc are at version ontology...
    // import
    // public void version(int major,int minor,int micro, int variant, int
    // stamp){
    // addRelationTarget(LRM_semantic_versioning_schema.version,
    // ecosystem.model.createIndividual(LRM_semantic_versioning_schema.Version)
    // .addRelationTarget(LRM_semantic_versioning_schema.label, ));
    // }

    public void version(String version) {
        if (version.equals("")) {
            return;
        }
        addProperty(LRM_semantic_versioning_schema.version,
                model.model.createIndividual(LRM_semantic_versioning_schema.Version)
                        .addProperty(LRM_static_schema.definition, version));
    }

    public void describedBy(String text) {
        if (text.equals("")) {
            return;
        }
        addProperty(LRM_static_schema.specification, model.model.createIndividual(LRM_static_schema.Description)
                .addProperty(LRM_static_schema.definition, text));
    }

    public void intention(String text) {
        addProperty(LRM_static_schema.intention, model.model.createIndividual(LRM_static_schema.Description)
                .addProperty(LRM_static_schema.definition, text));
    }

    @Override
    public String toString() {
        return name;
    }

    public void addLabel(String label) {
        if (label.equals("")) {
            return;
        }
        addLabel(label, "en");
    }

    public void setLabel(String label) {
        if (label.equals("")) {
            return;
        }
        setLabel(label, "en");
    }

    // WRAPPPER:

    public void addComment(String comment) {
        addProperty(RDFS.comment, model.model.createLiteral(comment, "en"));
    }

    public Node asNode() {
        return individual.asNode();
    }

    public OntModel getOntModel() {
        return individual.getOntModel();
    }

    public void setOntClass(Resource cls) {
        individual.setOntClass(cls);
    }

    public boolean isAnon() {
        return individual.isAnon();
    }

    public boolean isLiteral() {
        return individual.isLiteral();
    }

    public Profile getProfile() {
        return individual.getProfile();
    }

    public boolean isURIResource() {
        return individual.isURIResource();
    }

    public boolean isResource() {
        return individual.isResource();
    }

    public void addOntClass(Resource cls) {
        individual.addOntClass(cls);
    }

    public boolean isOntLanguageTerm() {
        return individual.isOntLanguageTerm();
    }

    public <T extends RDFNode> T as(Class<T> view) {
        return individual.as(view);
    }

    public OntClass getOntClass() {
        return individual.getOntClass();
    }

    public AnonId getId() {
        return individual.getId();
    }

    public void setSameAs(Resource res) {
        individual.setSameAs(res);
    }

    public <T extends RDFNode> boolean canAs(Class<T> view) {
        return individual.canAs(view);
    }

    public Resource inModel(Model m) {
        return individual.inModel(m);
    }

    public void addSameAs(Resource res) {
        individual.addSameAs(res);
    }

    public boolean hasURI(String uri) {
        return individual.hasURI(uri);
    }

    public String getURI() {
        return individual.getURI();
    }

    public Model getModel() {
        return individual.getModel();
    }

    public OntResource getSameAs() {
        return individual.getSameAs();
    }

    public OntClass getOntClass(boolean direct) {
        return individual.getOntClass(direct);
    }

    public String getNameSpace() {
        return individual.getNameSpace();
    }


    public String getLocalName() {
        return individual.getLocalName();
    }


    public ExtendedIterator<? extends Resource> listSameAs() {
        return individual.listSameAs();
    }


    public Object visitWith(RDFVisitor rv) {
        return individual.visitWith(rv);
    }


    public boolean equals(Object o) {
        return individual.equals(o);
    }


    public Resource asResource() {
        return individual.asResource();
    }


    public boolean isSameAs(Resource res) {
        return individual.isSameAs(res);
    }


    public Literal asLiteral() {
        return individual.asLiteral();
    }


    public void removeSameAs(Resource res) {
        individual.removeSameAs(res);
    }


    public Statement getRequiredProperty(Property p) {
        return individual.getRequiredProperty(p);
    }


    public <T extends OntClass> ExtendedIterator<T> listOntClasses(boolean direct) {
        return individual.listOntClasses(direct);
    }


    public void setDifferentFrom(Resource res) {
        individual.setDifferentFrom(res);
    }


    public boolean hasOntClass(Resource ontClass, boolean direct) {
        return individual.hasOntClass(ontClass, direct);
    }


    public Statement getProperty(Property p) {
        return individual.getProperty(p);
    }


    public void addDifferentFrom(Resource res) {
        individual.addDifferentFrom(res);
    }


    public StmtIterator listProperties(Property p) {
        return individual.listProperties(p);
    }


    public OntResource getDifferentFrom() {
        return individual.getDifferentFrom();
    }


    public boolean hasOntClass(Resource ontClass) {
        return individual.hasOntClass(ontClass);
    }


    public StmtIterator listProperties() {
        return individual.listProperties();
    }


    public ExtendedIterator<? extends Resource> listDifferentFrom() {
        return individual.listDifferentFrom();
    }


    public boolean hasOntClass(String uri) {
        return individual.hasOntClass(uri);
    }


    public Resource addLiteral(Property p, boolean o) {
        return individual.addLiteral(p, o);
    }


    public boolean isDifferentFrom(Resource res) {
        return individual.isDifferentFrom(res);
    }


    public void removeOntClass(Resource ontClass) {
        individual.removeOntClass(ontClass);
    }


    public Resource addLiteral(Property p, long o) {
        return individual.addLiteral(p, o);
    }


    public void removeDifferentFrom(Resource res) {
        individual.removeDifferentFrom(res);
    }


    public Resource addLiteral(Property p, char o) {
        return individual.addLiteral(p, o);
    }


    public void setSeeAlso(Resource res) {
        individual.setSeeAlso(res);
    }


    public Resource addLiteral(Property value, double d) {
        return individual.addLiteral(value, d);
    }


    public void addSeeAlso(Resource res) {
        individual.addSeeAlso(res);
    }


    public Resource addLiteral(Property value, float d) {
        return individual.addLiteral(value, d);
    }


    public Resource getSeeAlso() {
        return individual.getSeeAlso();
    }


    public Resource addLiteral(Property p, Object o) {
        return individual.addLiteral(p, o);
    }


    public ExtendedIterator<RDFNode> listSeeAlso() {
        return individual.listSeeAlso();
    }


    public Resource addLiteral(Property p, Literal o) {
        return individual.addLiteral(p, o);
    }


    public boolean hasSeeAlso(Resource res) {
        return individual.hasSeeAlso(res);
    }


    public Resource addProperty(Property p, String o) {
        return individual.addProperty(p, o);
    }


    public void removeSeeAlso(Resource res) {
        individual.removeSeeAlso(res);
    }


    public Resource addProperty(Property p, String o, String l) {
        return individual.addProperty(p, o, l);
    }


    public void setIsDefinedBy(Resource res) {
        individual.setIsDefinedBy(res);
    }


    public Resource addProperty(Property p, String lexicalForm, RDFDatatype datatype) {
        return individual.addProperty(p, lexicalForm, datatype);
    }


    public void addIsDefinedBy(Resource res) {
        individual.addIsDefinedBy(res);
    }


    public Resource getIsDefinedBy() {
        return individual.getIsDefinedBy();
    }


    public Resource addProperty(Property p, RDFNode o) {
        return individual.addProperty(p, o);
    }

    public ExtendedIterator<RDFNode> listIsDefinedBy() {
        return individual.listIsDefinedBy();
    }


    public boolean hasProperty(Property p) {
        return individual.hasProperty(p);
    }


    public boolean hasLiteral(Property p, boolean o) {
        return individual.hasLiteral(p, o);
    }


    public boolean isDefinedBy(Resource res) {
        return individual.isDefinedBy(res);
    }


    public boolean hasLiteral(Property p, long o) {
        return individual.hasLiteral(p, o);
    }


    public void removeDefinedBy(Resource res) {
        individual.removeDefinedBy(res);
    }


    public boolean hasLiteral(Property p, char o) {
        return individual.hasLiteral(p, o);
    }


    public void setVersionInfo(String info) {
        individual.setVersionInfo(info);
    }


    public boolean hasLiteral(Property p, double o) {
        return individual.hasLiteral(p, o);
    }


    public boolean hasLiteral(Property p, float o) {
        return individual.hasLiteral(p, o);
    }


    public void addVersionInfo(String info) {
        individual.addVersionInfo(info);
    }


    public boolean hasLiteral(Property p, Object o) {
        return individual.hasLiteral(p, o);
    }


    public String getVersionInfo() {
        return individual.getVersionInfo();
    }


    public boolean hasProperty(Property p, String o) {
        return individual.hasProperty(p, o);
    }


    public ExtendedIterator<String> listVersionInfo() {
        return individual.listVersionInfo();
    }


    public boolean hasProperty(Property p, String o, String l) {
        return individual.hasProperty(p, o, l);
    }


    public boolean hasVersionInfo(String info) {
        return individual.hasVersionInfo(info);
    }


    public boolean hasProperty(Property p, RDFNode o) {
        return individual.hasProperty(p, o);
    }


    public void removeVersionInfo(String info) {
        individual.removeVersionInfo(info);
    }


    public Resource removeProperties() {
        return individual.removeProperties();
    }


    public void setLabel(String label, String lang) {
        individual.setLabel(label, lang);
    }


    public Resource removeAll(Property p) {
        return individual.removeAll(p);
    }


    public Resource begin() {
        return individual.begin();
    }


    public Resource abort() {
        return individual.abort();
    }


    public void addLabel(String label, String lang) {
        individual.addLabel(label, lang);
    }


    public Resource commit() {
        return individual.commit();
    }


    public Resource getPropertyResourceValue(Property p) {
        return individual.getPropertyResourceValue(p);
    }


    public void addLabel(Literal label) {
        individual.addLabel(label);
    }


    public String getLabel(String lang) {
        return individual.getLabel(lang);
    }


    public ExtendedIterator<RDFNode> listLabels(String lang) {
        return individual.listLabels(lang);
    }


    public boolean hasLabel(String label, String lang) {
        return individual.hasLabel(label, lang);
    }


    public boolean hasLabel(Literal label) {
        return individual.hasLabel(label);
    }


    public void removeLabel(String label, String lang) {
        individual.removeLabel(label, lang);
    }


    public void removeLabel(Literal label) {
        individual.removeLabel(label);
    }


    public void setComment(String comment, String lang) {
        individual.setComment(comment, lang);
    }


    public void addComment(String comment, String lang) {
        individual.addComment(comment, lang);
    }


    public void addComment(Literal comment) {
        individual.addComment(comment);
    }


    public String getComment(String lang) {
        return individual.getComment(lang);
    }


    public ExtendedIterator<RDFNode> listComments(String lang) {
        return individual.listComments(lang);
    }


    public boolean hasComment(String comment, String lang) {
        return individual.hasComment(comment, lang);
    }


    public boolean hasComment(Literal comment) {
        return individual.hasComment(comment);
    }


    public void removeComment(String comment, String lang) {
        individual.removeComment(comment, lang);
    }


    public void removeComment(Literal comment) {
        individual.removeComment(comment);
    }


    public void setRDFType(Resource cls) {
        individual.setRDFType(cls);
    }


    public void addRDFType(Resource cls) {
        individual.addRDFType(cls);
    }


    public Resource getRDFType() {
        return individual.getRDFType();
    }


    public Resource getRDFType(boolean direct) {
        return individual.getRDFType(direct);
    }


    public ExtendedIterator<Resource> listRDFTypes(boolean direct) {
        return individual.listRDFTypes(direct);
    }


    public boolean hasRDFType(Resource ontClass, boolean direct) {
        return individual.hasRDFType(ontClass, direct);
    }


    public boolean hasRDFType(Resource ontClass) {
        return individual.hasRDFType(ontClass);
    }


    public void removeRDFType(Resource cls) {
        individual.removeRDFType(cls);
    }


    public boolean hasRDFType(String uri) {
        return individual.hasRDFType(uri);
    }


    public int getCardinality(Property p) {
        return individual.getCardinality(p);
    }


    public void setPropertyValue(Property property, RDFNode value) {
        individual.setPropertyValue(property, value);
    }


    public RDFNode getPropertyValue(Property property) {
        return individual.getPropertyValue(property);
    }


    public NodeIterator listPropertyValues(Property property) {
        return individual.listPropertyValues(property);
    }


    public void removeProperty(Property property, RDFNode value) {
        individual.removeProperty(property, value);
    }


    public void remove() {
        individual.remove();
    }


    public OntProperty asProperty() {
        return individual.asProperty();
    }


    public AnnotationProperty asAnnotationProperty() {
        return individual.asAnnotationProperty();
    }


    public ObjectProperty asObjectProperty() {
        return individual.asObjectProperty();
    }


    public DatatypeProperty asDatatypeProperty() {
        return individual.asDatatypeProperty();
    }


    public Individual asIndividual() {
        return individual.asIndividual();
    }


    public OntClass asClass() {
        return individual.asClass();
    }

    public Ontology asOntology() {
        return individual.asOntology();
    }

    public DataRange asDataRange() {
        return individual.asDataRange();
    }

    public AllDifferent asAllDifferent() {
        return individual.asAllDifferent();
    }

    public boolean isProperty() {
        return individual.isProperty();
    }

    public boolean isAnnotationProperty() {
        return individual.isAnnotationProperty();
    }

    public boolean isObjectProperty() {
        return individual.isObjectProperty();
    }

    public boolean isDatatypeProperty() {
        return individual.isDatatypeProperty();
    }

    public boolean isIndividual() {
        return individual.isIndividual();
    }

    public boolean isClass() {
        return individual.isClass();
    }

    public boolean isOntology() {
        return individual.isOntology();
    }

    public boolean isDataRange() {
        return individual.isDataRange();
    }

    public boolean isAllDifferent() {
        return individual.isAllDifferent();
    }
}
