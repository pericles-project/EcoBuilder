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
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;
import models.AbstractModel;
import models.CoreModel;
import models.ScenarioModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is an abstract entity that holds some common relations that are inherited by all ecosystem entities.
 * Abstract ecosystem resources should extend this abstract base class so that a
 * concrete resource can be created from them in a defined way.
 */
public class Template implements OntClass, Serializable {
    // The model to which this entity template belongs:
    public final AbstractModel model;
    // The Jena ontology class representing this entity template:
    transient public final OntClass resource;
    // The template which is a super class of this template. The parent is null for the EcosystemEntity base template.
    transient public Set<Template> parents = new HashSet<>();
    // List of all subclasses which inherit from this class:
    transient private List<Template> children = new ArrayList<Template>();
    transient public String description;

    public final String name;

    public Template(AbstractModel model, String name, String description, Template parent) {
        this(model, name, parent);
        addDescription(description);
    }

    /**
     * Loading of existing resources for wrapper models:
     */
    public Template(AbstractModel model, Resource resource, Template parent) {
        this(model, resource.getLocalName(), parent);
    }

    public Template(AbstractModel model, String name, Template parent) {
        this.model = model;
        this.resource = model.model.createClass(model.namespace + CoreModel.sanitizeName(name));
        this.resource.addSuperClass(LRM_static_schema.ExogenousResource);
        this.resource.addSuperClass(LRM_semantic_versioning_schema.VersionedResource);
        model.templates.add(this);
        this.name = CoreModel.sanitizeName(name);
        if (parent != null) {
            addSuperClass(parent);
            this.resource.addSuperClass(parent);
        }
    }

    /**
     * Create an entity instance of the template:
     */
    public EcosystemEntity createEntity(ScenarioModel model, String ID) {
        return new EcosystemEntity(model, ID);
    }

    /**
     * Get the list of classes which inherit from this class.
     *
     * @return
     */
    public List<Template> getChildren() {
        return children;
    }

    /**
     * Adds an entity which inherits from this entity to the list of child entities.
     * All relations that can be used from this entity can also be used from the child.
     *
     * @param child
     */
    public void addChild(Template child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return resource.getLocalName();
    }

    public void addLabel(String label) {
        addProperty(RDFS.label, model.model.createLiteral(label, "en"));
    }

    public void addDescription(String description) {
        this.description = description;
        addProperty(LRM_static_schema.specification, model.model.createIndividual(LRM_static_schema.Description)
                .addProperty(LRM_static_schema.definition, description));
    }

    public void addComment(String comment) {
        addProperty(RDFS.comment, model.model.createLiteral(comment, "en"));
    }

    // Resource wrapper:
    public boolean isAnon() {
        return resource.isAnon();
    }

    public boolean isLiteral() {
        return resource.isLiteral();
    }

    public boolean isURIResource() {
        return resource.isURIResource();
    }

    public boolean isResource() {
        return resource.isResource();
    }

    public <T extends RDFNode> T as(Class<T> view) {
        return resource.as(view);
    }

    public <T extends RDFNode> boolean canAs(Class<T> view) {
        return resource.canAs(view);
    }

    public Model getModel() {
        return resource.getModel();
    }

    public Object visitWith(RDFVisitor rv) {
        return resource.visitWith(rv);
    }

    public Resource asResource() {
        return resource.asResource();
    }

    public Literal asLiteral() {
        return resource.asLiteral();
    }

    public Node asNode() {
        return resource.asNode();
    }

    public AnonId getId() {
        return resource.getId();
    }

    public Resource inModel(Model m) {
        return resource.inModel(m);
    }

    public boolean hasURI(String uri) {
        return resource.hasURI(uri);
    }

    public String getURI() {
        return resource.getURI();
    }

    public String getNameSpace() {
        return resource.getNameSpace();
    }

    public String getLocalName() {
        return resource.getLocalName();
    }

    public Statement getRequiredProperty(Property p) {
        return resource.getRequiredProperty(p);
    }

    public Statement getProperty(Property p) {
        return resource.getProperty(p);
    }

    public StmtIterator listProperties(Property p) {
        return resource.listProperties(p);
    }

    public StmtIterator listProperties() {
        return resource.listProperties();
    }

    public Resource addLiteral(Property p, boolean o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, long o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, char o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property value, double d) {
        return resource.addLiteral(value, d);
    }

    public Resource addLiteral(Property value, float d) {
        return resource.addLiteral(value, d);
    }

    public Resource addLiteral(Property p, Object o) {
        return resource.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, Literal o) {
        return resource.addLiteral(p, o);
    }

    public Resource addProperty(Property p, String o) {
        return resource.addProperty(p, o);
    }

    public Resource addProperty(Property p, String o, String l) {
        return resource.addProperty(p, o, l);
    }

    public Resource addProperty(Property p, String lexicalForm, RDFDatatype datatype) {
        return resource.addProperty(p, lexicalForm, datatype);
    }

    public Resource addProperty(Property p, RDFNode o) {
        return resource.addProperty(p, o);
    }

    public boolean hasProperty(Property p) {
        return resource.hasProperty(p);
    }

    public boolean hasLiteral(Property p, boolean o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, long o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, char o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, double o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, float o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasLiteral(Property p, Object o) {
        return resource.hasLiteral(p, o);
    }

    public boolean hasProperty(Property p, String o) {
        return resource.hasProperty(p, o);
    }

    public boolean hasProperty(Property p, String o, String l) {
        return resource.hasProperty(p, o, l);
    }

    public boolean hasProperty(Property p, RDFNode o) {
        return resource.hasProperty(p, o);
    }

    public Resource removeProperties() {
        return resource.removeProperties();
    }

    public Resource removeAll(Property p) {
        return resource.removeAll(p);
    }

    public Resource begin() {
        return resource.begin();
    }

    public Resource abort() {
        return resource.abort();
    }

    public Resource commit() {
        return resource.commit();
    }

    public Resource getPropertyResourceValue(Property p) {
        return resource.getPropertyResourceValue(p);
    }

    public OntModel getOntModel() {
        return resource.getOntModel();
    }

    public Profile getProfile() {
        return resource.getProfile();
    }

    public boolean isOntLanguageTerm() {
        return resource.isOntLanguageTerm();
    }

    public void addSameAs(Resource res) {
        resource.addSameAs(res);
    }

    public OntResource getSameAs() {
        return resource.getSameAs();
    }

    public void setSameAs(Resource res) {
        resource.setSameAs(res);
    }

    public ExtendedIterator<? extends Resource> listSameAs() {
        return resource.listSameAs();
    }

    public boolean isSameAs(Resource res) {
        return resource.isSameAs(res);
    }

    public void removeSameAs(Resource res) {
        resource.removeSameAs(res);
    }

    public void addDifferentFrom(Resource res) {
        resource.addDifferentFrom(res);
    }

    public OntResource getDifferentFrom() {
        return resource.getDifferentFrom();
    }

    public void setDifferentFrom(Resource res) {
        resource.setDifferentFrom(res);
    }

    public ExtendedIterator<? extends Resource> listDifferentFrom() {
        return resource.listDifferentFrom();
    }

    public boolean isDifferentFrom(Resource res) {
        return resource.isDifferentFrom(res);
    }

    public void removeDifferentFrom(Resource res) {
        resource.removeDifferentFrom(res);
    }

    public void addSeeAlso(Resource res) {
        resource.addSeeAlso(res);
    }

    public Resource getSeeAlso() {
        return resource.getSeeAlso();
    }

    public void setSeeAlso(Resource res) {
        resource.setSeeAlso(res);
    }

    public ExtendedIterator<RDFNode> listSeeAlso() {
        return resource.listSeeAlso();
    }

    public boolean hasSeeAlso(Resource res) {
        return resource.hasSeeAlso(res);
    }

    public void removeSeeAlso(Resource res) {
        resource.removeSeeAlso(res);
    }

    public void addIsDefinedBy(Resource res) {
        resource.addIsDefinedBy(res);
    }

    public Resource getIsDefinedBy() {
        return resource.getIsDefinedBy();
    }

    public void setIsDefinedBy(Resource res) {
        resource.setIsDefinedBy(res);
    }

    public ExtendedIterator<RDFNode> listIsDefinedBy() {
        return resource.listIsDefinedBy();
    }

    public boolean isDefinedBy(Resource res) {
        return resource.isDefinedBy(res);
    }

    public void removeDefinedBy(Resource res) {
        resource.removeDefinedBy(res);
    }

    public void addVersionInfo(String info) {
        resource.addVersionInfo(info);
    }

    public String getVersionInfo() {
        return resource.getVersionInfo();
    }

    public void setVersionInfo(String info) {
        resource.setVersionInfo(info);
    }

    public ExtendedIterator<String> listVersionInfo() {
        return resource.listVersionInfo();
    }

    public boolean hasVersionInfo(String info) {
        return resource.hasVersionInfo(info);
    }

    public void removeVersionInfo(String info) {
        resource.removeVersionInfo(info);
    }

    public void setLabel(String label, String lang) {
        resource.setLabel(label, lang);
    }

    public void addLabel(String label, String lang) {
        resource.addLabel(label, lang);
    }

    public void addLabel(Literal label) {
        resource.addLabel(label);
    }

    public String getLabel(String lang) {
        return resource.getLabel(lang);
    }

    public ExtendedIterator<RDFNode> listLabels(String lang) {
        return resource.listLabels(lang);
    }

    public boolean hasLabel(String label, String lang) {
        return resource.hasLabel(label, lang);
    }

    public boolean hasLabel(Literal label) {
        return resource.hasLabel(label);
    }

    public void removeLabel(String label, String lang) {
        resource.removeLabel(label, lang);
    }

    public void removeLabel(Literal label) {
        resource.removeLabel(label);
    }

    public void setComment(String comment, String lang) {
        resource.setComment(comment, lang);
    }

    public void addComment(String comment, String lang) {
        resource.addComment(comment, lang);
    }

    public void addComment(Literal comment) {
        resource.addComment(comment);
    }

    public String getComment(String lang) {
        if (resource != null) {
            return resource.getComment(lang);
        } else {
            return "";
        }
    }

    public ExtendedIterator<RDFNode> listComments(String lang) {
        return resource.listComments(lang);
    }

    public boolean hasComment(String comment, String lang) {
        return resource.hasComment(comment, lang);
    }

    public boolean hasComment(Literal comment) {
        return resource.hasComment(comment);
    }

    public void removeComment(String comment, String lang) {
        resource.removeComment(comment, lang);
    }

    public void removeComment(Literal comment) {
        resource.removeComment(comment);
    }

    public void addRDFType(Resource cls) {
        resource.addRDFType(cls);
    }

    public Resource getRDFType() {
        return resource.getRDFType();
    }

    public void setRDFType(Resource cls) {
        resource.setRDFType(cls);
    }

    public Resource getRDFType(boolean direct) {
        return resource.getRDFType(direct);
    }

    public ExtendedIterator<Resource> listRDFTypes(boolean direct) {
        return resource.listRDFTypes(direct);
    }

    public boolean hasRDFType(Resource ontClass, boolean direct) {
        return resource.hasRDFType(ontClass, direct);
    }

    public boolean hasRDFType(Resource ontClass) {
        return resource.hasRDFType(ontClass);
    }

    public void removeRDFType(Resource cls) {
        resource.removeRDFType(cls);
    }

    public boolean hasRDFType(String uri) {
        return resource.hasRDFType(uri);
    }

    public int getCardinality(Property p) {
        return resource.getCardinality(p);
    }

    public void setPropertyValue(Property property, RDFNode value) {
        resource.setPropertyValue(property, value);
    }

    public RDFNode getPropertyValue(Property property) {
        return resource.getPropertyValue(property);
    }

    public NodeIterator listPropertyValues(Property property) {
        return resource.listPropertyValues(property);
    }

    public void removeProperty(Property property, RDFNode value) {
        resource.removeProperty(property, value);
    }

    public void remove() {
        resource.remove();
    }

    public OntProperty asProperty() {
        return resource.asProperty();
    }

    public AnnotationProperty asAnnotationProperty() {
        return resource.asAnnotationProperty();
    }

    public ObjectProperty asObjectProperty() {
        return resource.asObjectProperty();
    }

    public DatatypeProperty asDatatypeProperty() {
        return resource.asDatatypeProperty();
    }

    public Individual asIndividual() {
        return resource.asIndividual();
    }

    public OntClass asClass() {
        return resource.asClass();
    }

    public Ontology asOntology() {
        return resource.asOntology();
    }

    public DataRange asDataRange() {
        return resource.asDataRange();
    }

    public AllDifferent asAllDifferent() {
        return resource.asAllDifferent();
    }

    public boolean isProperty() {
        return resource.isProperty();
    }

    public boolean isAnnotationProperty() {
        return resource.isAnnotationProperty();
    }

    public boolean isObjectProperty() {
        return resource.isObjectProperty();
    }

    public boolean isDatatypeProperty() {
        return resource.isDatatypeProperty();
    }

    public boolean isIndividual() {
        return resource.isIndividual();
    }

    public boolean isClass() {
        return resource.isClass();
    }

    public boolean isOntology() {
        return resource.isOntology();
    }

    public boolean isDataRange() {
        return resource.isDataRange();
    }

    public boolean isAllDifferent() {
        return resource.isAllDifferent();
    }

    public void addSuperClass(Template parent) {
        parent.addChild(this);
        this.parents.add(parent);
        resource.addSuperClass(parent);
    }

    public void addSuperClass(Resource superClass) {
        resource.addSuperClass(superClass);
    }

    public OntClass getSuperClass() {
        return resource.getSuperClass();
    }

    public void setSuperClass(Resource cls) {
        resource.setSuperClass(cls);
    }

    public ExtendedIterator<OntClass> listSuperClasses() {
        return resource.listSuperClasses();
    }

    public ExtendedIterator<OntClass> listSuperClasses(boolean direct) {
        return resource.listSuperClasses(direct);
    }

    public boolean hasSuperClass(Resource cls) {
        return resource.hasSuperClass(cls);
    }

    public boolean hasSuperClass() {
        return resource.hasSuperClass();
    }

    public boolean hasSuperClass(Resource cls, boolean direct) {
        return resource.hasSuperClass(cls, direct);
    }

    public void removeSuperClass(Resource cls) {
        resource.removeSuperClass(cls);
    }

    public void addSubClass(Resource cls) {
        resource.addSubClass(cls);
    }

    public OntClass getSubClass() {
        return resource.getSubClass();
    }

    public void setSubClass(Resource cls) {
        resource.setSubClass(cls);
    }

    public ExtendedIterator<OntClass> listSubClasses() {
        return resource.listSubClasses();
    }

    public ExtendedIterator<OntClass> listSubClasses(boolean direct) {
        return resource.listSubClasses(direct);
    }

    public boolean hasSubClass(Resource cls) {
        return resource.hasSubClass(cls);
    }

    public boolean hasSubClass() {
        return resource.hasSubClass();
    }

    public boolean hasSubClass(Resource cls, boolean direct) {
        return resource.hasSubClass(cls, direct);
    }

    public void removeSubClass(Resource cls) {
        resource.removeSubClass(cls);
    }

    public void addEquivalentClass(Resource cls) {
        resource.addEquivalentClass(cls);
    }

    public OntClass getEquivalentClass() {
        return resource.getEquivalentClass();
    }

    public void setEquivalentClass(Resource cls) {
        resource.setEquivalentClass(cls);
    }

    public ExtendedIterator<OntClass> listEquivalentClasses() {
        return resource.listEquivalentClasses();
    }

    public boolean hasEquivalentClass(Resource cls) {
        return resource.hasEquivalentClass(cls);
    }

    public void removeEquivalentClass(Resource cls) {
        resource.removeEquivalentClass(cls);
    }

    public void addDisjointWith(Resource cls) {
        resource.addDisjointWith(cls);
    }

    public OntClass getDisjointWith() {
        return resource.getDisjointWith();
    }

    public void setDisjointWith(Resource cls) {
        resource.setDisjointWith(cls);
    }

    public ExtendedIterator<OntClass> listDisjointWith() {
        return resource.listDisjointWith();
    }

    public boolean isDisjointWith(Resource cls) {
        return resource.isDisjointWith(cls);
    }

    public void removeDisjointWith(Resource cls) {
        resource.removeDisjointWith(cls);
    }

    public ExtendedIterator<OntProperty> listDeclaredProperties() {
        return resource.listDeclaredProperties();
    }

    public ExtendedIterator<OntProperty> listDeclaredProperties(boolean direct) {
        return resource.listDeclaredProperties(direct);
    }

    public boolean hasDeclaredProperty(Property p, boolean direct) {
        return resource.hasDeclaredProperty(p, direct);
    }

    public ExtendedIterator<? extends OntResource> listInstances() {
        return resource.listInstances();
    }

    public ExtendedIterator<? extends OntResource> listInstances(boolean direct) {
        return resource.listInstances(direct);
    }

    public Individual createIndividual() {
        return resource.createIndividual();
    }

    public Individual createIndividual(String uri) {
        return resource.createIndividual(uri);
    }

    public void dropIndividual(Resource individual) {
        resource.dropIndividual(individual);
    }

    public boolean isHierarchyRoot() {
        return resource.isHierarchyRoot();
    }

    public EnumeratedClass asEnumeratedClass() {
        return resource.asEnumeratedClass();
    }

    public UnionClass asUnionClass() {
        return resource.asUnionClass();
    }

    public IntersectionClass asIntersectionClass() {
        return resource.asIntersectionClass();
    }

    public ComplementClass asComplementClass() {
        return resource.asComplementClass();
    }

    public Restriction asRestriction() {
        return resource.asRestriction();
    }

    public boolean isEnumeratedClass() {
        return resource.isEnumeratedClass();
    }

    public boolean isUnionClass() {
        return resource.isUnionClass();
    }

    public boolean isIntersectionClass() {
        return resource.isIntersectionClass();
    }

    public boolean isComplementClass() {
        return resource.isComplementClass();
    }

    public boolean isRestriction() {
        return resource.isRestriction();
    }

    public EnumeratedClass convertToEnumeratedClass(RDFList individuals) {
        return resource.convertToEnumeratedClass(individuals);
    }

    public IntersectionClass convertToIntersectionClass(RDFList classes) {
        return resource.convertToIntersectionClass(classes);
    }

    public UnionClass convertToUnionClass(RDFList classes) {
        return resource.convertToUnionClass(classes);
    }

    public ComplementClass convertToComplementClass(Resource cls) {
        return resource.convertToComplementClass(cls);
    }

    public Restriction convertToRestriction(Property prop) {
        return resource.convertToRestriction(prop);
    }
}
