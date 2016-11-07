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
package relations;

import LRMv2.LRM_static_schema;
import entities.Template;
import models.AbstractModel;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a relation in the model. In contrast to {@link gui.RelationView} it is a relation which is
 * already added to an {@link AbstractModel} and it wraps around an associated ontology {@link ObjectProperty}.
 */
public class DEMRelation extends Relation implements ObjectProperty {
    /**
     * The model to which this relation belongs
     */
    transient public final AbstractModel model;
    /**
     * The ontology property associated with this relation
     */
    public transient final ObjectProperty property;
    /**
     * A list of entities from which this relation can point.
     */
    transient private Set<Template> templateDomains = new HashSet<>();
    /**
     * A list of entities to which this relation can point.
     */
    transient private Set<Template> templateRanges = new HashSet<>();


    /**
     * Relations are created using the builder pattern.
     *
     * @param builder a {@link RelationBuilder}
     */
    public DEMRelation(RelationBuilder builder) {
        super(builder.name, builder.description);
        this.model = builder.model;
        this.property = model.model.createObjectProperty(model.namespace + AbstractModel.sanitizeName(name));
        builder.domains.forEach(this::addDomain);
        builder.ranges.forEach(this::addRange);
        if (builder.description != null) {
            addDescription(builder.description);
        }
        if (builder.inverse != null) {
            addInverse(builder.inverse);
        }
        if (builder.superRelation != null) {
            addSuperProperty(builder.superRelation);
        }
        addLabel(name);
        this.model.relations.add(this);
    }

    protected void addInverse(Relation inverse) {
        this.inverse = inverse;
        this.inverse.inverse = this;
        if (inverse instanceof DEMRelation) {
            addProperty(OWL.inverseOf, (DEMRelation) inverse);
            addInverseOf((DEMRelation) inverse);
            ((DEMRelation) inverse).addProperty(OWL.inverseOf, this);
            ((DEMRelation) inverse).addInverseOf(this);
        }
    }

    /**
     * Adds a resource to the range list (list of possible relation templateRanges).
     *
     * @param range The range resource
     */
    public void addRange(Resource range) {
        property.addRange(range);
        if (range instanceof Template) {
            addTarget((Template) range);
        }
    }

    /**
     * Recursively add all child classes of the passed entity class as possible templateRanges of the relation.
     */
    public void addTarget(Template target) {
        templateRanges.add(target);
        for (Template child : target.getChildren()) {
            addTarget(child);
        }
    }

    public void addDomain(Resource domain) {
        if (domain instanceof Template) {
            templateDomains.add((Template) domain);
        }
        property.addDomain(domain);
    }

    /**
     * Add a description of this relation
     *
     * @param description of this relation
     */
    public void addDescription(String description) {
        addProperty(LRM_static_schema.specification, model.model.createIndividual(LRM_static_schema.Description)
                .addProperty(LRM_static_schema.definition, description));
    }

    public void addLabel(String label) {
        addProperty(RDFS.label, model.model.createLiteral(label, "en"));
    }

    public Set<Template> getParentTemplateDomains() {
        return this.templateDomains;
    }

    public Set<Template> getParentTemplateRanges() {
        return this.templateRanges;
    }


    public Set<Template> getAllTemplateDomains() {
        Set<Template> all = new HashSet<>();
        all.addAll(templateDomains);
        all.addAll(getChildren(templateDomains));
        return all;
    }

    public Set<Template> getAllTemplateRanges() {
        Set<Template> all = new HashSet<>();
        all.addAll(templateRanges);
        all.addAll(getChildren(templateRanges));
        return all;
    }

    private Set<Template> getChildren(Set<Template> parents) {
        Set<Template> childs = new HashSet<>();
        for (Template template : parents) {
            childs.addAll(recursivelyAddChilds(template));
        }
        return childs;
    }

    private Set<Template> recursivelyAddChilds(Template template) {
        Set<Template> childs = new HashSet<>();
        for (Template child : template.getChildren()) {
            childs.add(child);
            childs.addAll(recursivelyAddChilds(child));
        }
        return childs;
    }

    @Override
    public String toString() {
        return name;
    }

    // Property Wrapper classes:
    public AnonId getId() {
        return property.getId();
    }

    public boolean hasURI(String uri) {
        return property.hasURI(uri);
    }

    public String getURI() {
        return property.getURI();
    }

    public Statement getRequiredProperty(Property p) {
        return property.getRequiredProperty(p);
    }

    public Statement getProperty(Property p) {
        return property.getProperty(p);
    }

    public StmtIterator listProperties(Property p) {
        return property.listProperties(p);
    }

    public StmtIterator listProperties() {
        return property.listProperties();
    }

    public Resource addLiteral(Property p, boolean o) {
        return property.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, long o) {
        return property.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, char o) {
        return property.addLiteral(p, o);
    }

    public Resource addLiteral(Property value, double d) {
        return property.addLiteral(value, d);

    }

    public Resource addLiteral(Property value, float d) {
        return property.addLiteral(value, d);

    }

    public Resource addLiteral(Property p, Object o) {
        return property.addLiteral(p, o);
    }

    public Resource addLiteral(Property p, Literal o) {
        return property.addLiteral(p, o);
    }

    public Resource addProperty(Property p, String o) {
        return property.addProperty(p, o);

    }

    public Resource addProperty(Property p, String o, String l) {
        return property.addProperty(p, o, l);

    }

    public Resource addProperty(Property p, String lexicalForm, RDFDatatype datatype) {
        return property.addProperty(p, lexicalForm, datatype);

    }

    public Resource addProperty(Property p, RDFNode o) {
        return property.addProperty(p, o);

    }

    public boolean hasProperty(Property p) {
        return property.hasProperty(p);

    }

    public boolean hasLiteral(Property p, boolean o) {
        return property.hasLiteral(p, o);

    }

    public boolean hasLiteral(Property p, long o) {
        return property.hasLiteral(p, o);

    }

    public boolean hasLiteral(Property p, char o) {
        return property.hasLiteral(p, o);

    }

    public boolean hasLiteral(Property p, double o) {
        return property.hasLiteral(p, o);

    }

    public boolean hasLiteral(Property p, float o) {
        return property.hasLiteral(p, o);

    }

    public boolean hasLiteral(Property p, Object o) {
        return property.hasLiteral(p, o);

    }

    public boolean hasProperty(Property p, String o) {
        return property.hasProperty(p, o);

    }

    public boolean hasProperty(Property p, String o, String l) {
        return property.hasProperty(p, o, l);

    }

    public boolean hasProperty(Property p, RDFNode o) {
        return property.hasProperty(p, o);

    }

    public Resource removeProperties() {
        return property.removeProperties();

    }

    public Resource removeAll(Property p) {
        return property.removeAll(p);

    }

    public Resource begin() {
        return property.begin();

    }

    public Resource abort() {
        return property.abort();

    }

    public Resource commit() {
        return property.commit();

    }

    public Resource getPropertyResourceValue(Property p) {
        return property.getPropertyResourceValue(p);

    }

    public boolean isAnon() {
        return property.isAnon();

    }

    public boolean isLiteral() {
        return property.isLiteral();

    }

    public boolean isURIResource() {
        return property.isURIResource();

    }

    public boolean isResource() {
        return property.isResource();

    }

    public <T extends RDFNode> T as(Class<T> view) {
        return property.as(view);

    }

    public <T extends RDFNode> boolean canAs(Class<T> view) {
        return property.canAs(view);

    }

    public Model getModel() {
        return property.getModel();

    }

    public Object visitWith(RDFVisitor rv) {
        return property.visitWith(rv);

    }

    public Resource asResource() {
        return property.asResource();

    }

    public Literal asLiteral() {
        return property.asLiteral();

    }

    public Node asNode() {
        return property.asNode();

    }

    public boolean isProperty() {
        return property.isProperty();

    }

    public String getNameSpace() {
        return property.getNameSpace();

    }

    public Property inModel(Model m) {
        return property.inModel(m);

    }

    public String getLocalName() {
        return property.getLocalName();

    }

    public int getOrdinal() {
        return property.getOrdinal();

    }

    public OntModel getOntModel() {
        return property.getOntModel();
    }

    public void setSuperProperty(Property prop) {
        property.setSuperProperty(prop);
    }

    public Profile getProfile() {
        return property.getProfile();
    }

    public void addSuperProperty(Property prop) {
        property.addSuperProperty(prop);
    }

    public boolean isOntLanguageTerm() {
        return property.isOntLanguageTerm();
    }

    public OntProperty getSuperProperty() {
        return property.getSuperProperty();
    }

    public void setSameAs(Resource res) {
        property.setSameAs(res);
    }

    public ExtendedIterator<? extends OntProperty> listSuperProperties() {
        return property.listSuperProperties();
    }

    public void addSameAs(Resource res) {
        property.addSameAs(res);
    }

    public ExtendedIterator<? extends OntProperty> listSuperProperties(boolean direct) {
        return property.listSuperProperties(direct);
    }

    public OntResource getSameAs() {
        return property.getSameAs();
    }

    public ExtendedIterator<? extends Resource> listSameAs() {
        return property.listSameAs();
    }

    public boolean hasSuperProperty(Property prop, boolean direct) {
        return property.hasSuperProperty(prop, direct);
    }

    public boolean isSameAs(Resource res) {
        return property.isSameAs(res);
    }

    public void removeSuperProperty(Property prop) {
        property.removeSuperProperty(prop);
    }

    public void removeSameAs(Resource res) {
        property.removeSameAs(res);
    }

    public void setSubProperty(Property prop) {
        property.setSubProperty(prop);
    }

    public void setDifferentFrom(Resource res) {
        property.setDifferentFrom(res);
    }

    public void addSubProperty(Property prop) {
        property.addSubProperty(prop);
    }

    public void addDifferentFrom(Resource res) {
        property.addDifferentFrom(res);
    }

    public OntProperty getSubProperty() {
        return property.getSubProperty();
    }

    public OntResource getDifferentFrom() {
        return property.getDifferentFrom();
    }

    public ExtendedIterator<? extends OntProperty> listSubProperties() {
        return property.listSubProperties();
    }

    public ExtendedIterator<? extends Resource> listDifferentFrom() {
        return property.listDifferentFrom();
    }

    public ExtendedIterator<? extends OntProperty> listSubProperties(boolean direct) {
        return property.listSubProperties(direct);
    }

    public boolean isDifferentFrom(Resource res) {
        return property.isDifferentFrom(res);
    }

    public void removeDifferentFrom(Resource res) {
        property.removeDifferentFrom(res);
    }

    public boolean hasSubProperty(Property prop, boolean direct) {
        return property.hasSubProperty(prop, direct);
    }

    public void setSeeAlso(Resource res) {
        property.setSeeAlso(res);
    }

    public void addSeeAlso(Resource res) {
        property.addSeeAlso(res);
    }

    public void removeSubProperty(Property prop) {
        property.removeSubProperty(prop);
    }

    public Resource getSeeAlso() {
        return property.getSeeAlso();
    }

    public void setDomain(Resource res) {
        property.setDomain(res);
    }

    public ExtendedIterator<RDFNode> listSeeAlso() {
        return property.listSeeAlso();
    }

    public boolean hasSeeAlso(Resource res) {
        return property.hasSeeAlso(res);
    }

    public OntResource getDomain() {
        return property.getDomain();
    }

    public void removeSeeAlso(Resource res) {
        property.removeSeeAlso(res);
    }

    public ExtendedIterator<? extends OntResource> listDomain() {
        return property.listDomain();
    }

    public void setIsDefinedBy(Resource res) {
        property.setIsDefinedBy(res);
    }

    public boolean hasDomain(Resource res) {
        return property.hasDomain(res);
    }

    public void addIsDefinedBy(Resource res) {
        property.addIsDefinedBy(res);
    }

    public void removeDomain(Resource cls) {
        property.removeDomain(cls);
    }

    public Resource getIsDefinedBy() {
        return property.getIsDefinedBy();
    }

    public void setRange(Resource res) {
        property.setRange(res);
    }

    public ExtendedIterator<RDFNode> listIsDefinedBy() {
        return property.listIsDefinedBy();
    }

    public boolean isDefinedBy(Resource res) {
        return property.isDefinedBy(res);
    }

    public void removeDefinedBy(Resource res) {
        property.removeDefinedBy(res);
    }

    public void setVersionInfo(String info) {
        property.setVersionInfo(info);
    }

    public void addVersionInfo(String info) {
        property.addVersionInfo(info);
    }

    public OntResource getRange() {
        return property.getRange();
    }

    public String getVersionInfo() {
        return property.getVersionInfo();
    }

    public ExtendedIterator<? extends OntResource> listRange() {
        return property.listRange();
    }

    public ExtendedIterator<String> listVersionInfo() {
        return property.listVersionInfo();
    }

    public boolean hasRange(Resource res) {
        return property.hasRange(res);
    }

    public boolean hasVersionInfo(String info) {
        return property.hasVersionInfo(info);
    }

    public void removeVersionInfo(String info) {
        property.removeVersionInfo(info);
    }

    public void removeRange(Resource cls) {
        property.removeRange(cls);
    }

    public void setLabel(String label, String lang) {
        property.setLabel(label, lang);
    }

    public void setEquivalentProperty(Property prop) {
        property.setEquivalentProperty(prop);
    }

    public void addLabel(String label, String lang) {
        property.addLabel(label, lang);
    }

    public void addEquivalentProperty(Property prop) {
        property.addEquivalentProperty(prop);
    }

    public void addLabel(Literal label) {
        property.addLabel(label);
    }

    public OntProperty getEquivalentProperty() {
        return property.getEquivalentProperty();
    }

    public String getLabel(String lang) {
        return property.getLabel(lang);
    }

    public ExtendedIterator<? extends OntProperty> listEquivalentProperties() {
        return property.listEquivalentProperties();
    }

    public ExtendedIterator<RDFNode> listLabels(String lang) {
        return property.listLabels(lang);
    }

    public boolean hasEquivalentProperty(Property prop) {
        return property.hasEquivalentProperty(prop);
    }

    public boolean hasLabel(String label, String lang) {
        return property.hasLabel(label, lang);
    }

    public void removeEquivalentProperty(Property prop) {
        property.removeEquivalentProperty(prop);
    }

    public boolean hasLabel(Literal label) {
        return property.hasLabel(label);
    }

    public void removeLabel(String label, String lang) {
        property.removeLabel(label, lang);
    }

    public void setInverseOf(Property prop) {
        property.setInverseOf(prop);
    }

    public void removeLabel(Literal label) {
        property.removeLabel(label);
    }

    public void addInverseOf(Property prop) {
        property.addInverseOf(prop);
    }

    public void setComment(String comment, String lang) {
        property.setComment(comment, lang);
    }

    public OntProperty getInverseOf() {
        return property.getInverseOf();
    }

    public void addComment(String comment, String lang) {
        property.addComment(comment, lang);
    }

    public ExtendedIterator<? extends OntProperty> listInverseOf() {
        return property.listInverseOf();
    }

    public void addComment(Literal comment) {
        property.addComment(comment);
    }

    public String getComment(String lang) {
        return property.getComment(lang);
    }

    public void removeInverseProperty(Property prop) {
        property.removeInverseProperty(prop);
    }

    public ExtendedIterator<RDFNode> listComments(String lang) {
        return property.listComments(lang);
    }

    public FunctionalProperty asFunctionalProperty() {
        return property.asFunctionalProperty();
    }

    public boolean hasComment(String comment, String lang) {
        return property.hasComment(comment, lang);
    }

    public DatatypeProperty asDatatypeProperty() {
        return property.asDatatypeProperty();
    }

    public boolean hasComment(Literal comment) {
        return property.hasComment(comment);
    }

    public void removeComment(String comment, String lang) {
        property.removeComment(comment, lang);
    }

    public ObjectProperty asObjectProperty() {
        return property.asObjectProperty();
    }

    public void removeComment(Literal comment) {
        property.removeComment(comment);
    }

    public TransitiveProperty asTransitiveProperty() {
        return property.asTransitiveProperty();
    }

    public void setRDFType(Resource cls) {
        property.setRDFType(cls);
    }

    public InverseFunctionalProperty asInverseFunctionalProperty() {
        return property.asInverseFunctionalProperty();
    }

    public void addRDFType(Resource cls) {
        property.addRDFType(cls);
    }

    public SymmetricProperty asSymmetricProperty() {
        return property.asSymmetricProperty();
    }

    public Resource getRDFType() {
        return property.getRDFType();
    }

    public FunctionalProperty convertToFunctionalProperty() {
        return property.convertToFunctionalProperty();
    }

    public DatatypeProperty convertToDatatypeProperty() {
        return property.convertToDatatypeProperty();
    }

    public Resource getRDFType(boolean direct) {
        return property.getRDFType(direct);
    }

    public ObjectProperty convertToObjectProperty() {
        return property.convertToObjectProperty();
    }

    public TransitiveProperty convertToTransitiveProperty() {
        return property.convertToTransitiveProperty();
    }

    public ExtendedIterator<Resource> listRDFTypes(boolean direct) {
        return property.listRDFTypes(direct);
    }

    public InverseFunctionalProperty convertToInverseFunctionalProperty() {
        return property.convertToInverseFunctionalProperty();
    }

    public SymmetricProperty convertToSymmetricProperty() {
        return property.convertToSymmetricProperty();
    }

    public boolean hasRDFType(Resource ontClass, boolean direct) {
        return property.hasRDFType(ontClass, direct);
    }

    public boolean isFunctionalProperty() {
        return property.isFunctionalProperty();
    }

    public boolean isDatatypeProperty() {
        return property.isDatatypeProperty();
    }

    public boolean hasRDFType(Resource ontClass) {
        return property.hasRDFType(ontClass);
    }

    public boolean isObjectProperty() {
        return property.isObjectProperty();
    }

    public boolean isTransitiveProperty() {
        return property.isTransitiveProperty();
    }

    public boolean isInverseFunctionalProperty() {
        return property.isInverseFunctionalProperty();
    }

    public void removeRDFType(Resource cls) {
        property.removeRDFType(cls);
    }

    public boolean isSymmetricProperty() {
        return property.isSymmetricProperty();
    }

    public boolean hasRDFType(String uri) {
        return property.hasRDFType(uri);
    }

    public OntProperty getInverse() {
        return property.getInverse();
    }

    public int getCardinality(Property p) {
        return property.getCardinality(p);
    }

    public void setPropertyValue(Property p, RDFNode value) {
        property.setPropertyValue(p, value);
    }

    public ExtendedIterator<? extends OntProperty> listInverse() {
        return property.listInverse();
    }

    public RDFNode getPropertyValue(Property p) {
        return property.getPropertyValue(p);
    }

    public boolean hasInverse() {
        if (inverse != null) {
            return true;
        } else {
            return false;
        }
    }

    public ExtendedIterator<? extends OntClass> listDeclaringClasses() {
        return property.listDeclaringClasses();
    }

    public NodeIterator listPropertyValues(Property p) {
        return property.listPropertyValues(p);
    }

    public ExtendedIterator<? extends OntClass> listDeclaringClasses(boolean direct) {
        return property.listDeclaringClasses(direct);
    }

    public void removeProperty(Property p, RDFNode value) {
        property.removeProperty(p, value);
    }

    public void remove() {
        property.remove();
    }

    public ExtendedIterator<Restriction> listReferringRestrictions() {
        return property.listReferringRestrictions();
    }

    public OntProperty asProperty() {
        return property.asProperty();
    }

    public AnnotationProperty asAnnotationProperty() {
        return property.asAnnotationProperty();
    }

    public Individual asIndividual() {
        return property.asIndividual();
    }

    public OntClass asClass() {
        return property.asClass();
    }

    public Ontology asOntology() {
        return property.asOntology();
    }

    public DataRange asDataRange() {
        return property.asDataRange();
    }

    public AllDifferent asAllDifferent() {
        return property.asAllDifferent();
    }

    public boolean isAnnotationProperty() {
        return property.isAnnotationProperty();
    }

    public boolean isIndividual() {
        return property.isIndividual();
    }

    public boolean isClass() {
        return property.isClass();
    }

    public boolean isOntology() {
        return property.isOntology();
    }

    public boolean isDataRange() {
        return property.isDataRange();
    }

    public boolean isAllDifferent() {
        return property.isAllDifferent();
    }

    public boolean isInverseOf(Property prop) {
        return property.isInverseOf(prop);
    }

}
