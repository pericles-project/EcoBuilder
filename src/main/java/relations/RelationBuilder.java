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

import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.Resource;
import models.AbstractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Using the builder pattern to create {@link DEMRelation}s.
 * <p>
 * Use: new RelationBuilder(model,name).domain(domain).range(range). ... .create();
 */
public class RelationBuilder {
    /**
     * The sub model to which the relation belongs.
     */
    protected AbstractModel model;
    /**
     * Name of the relation
     */
    protected String name;
    /**
     * The description is mostly a describing string.
     */
    protected String description;
    /**
     * A list of domain entities. These are the from or subject entities of the relation.
     */
    protected List<Resource> domains = new ArrayList<Resource>();
    /**
     * A list of range entities. These are the to or object entities of the relation.
     */
    protected List<Resource> ranges = new ArrayList<Resource>();
    /**
     * If the relation is bidirectional, then a link to the inverse relation will be saved here:
     */
    protected Relation inverse = null;
    /**
     * If the relation has a parent relation from which it inherits, then the parent is saved here:
     */
    protected OntProperty superRelation = null;

    /**
     * Constructor of the builder. Model and name are obligatory to create DEM relations.
     *
     * @param model Model to which the relation belongs
     * @param name  name of the relation
     */
    public RelationBuilder(AbstractModel model, String name) {
        this.model = model;
        this.name = name;
        OntProperty property = model.model.getOntProperty(model.namespace + AbstractModel.sanitizeName(name));
        if (property != null) {
            domains.add(property.getDomain());
            ranges.add(property.getRange());
        }
    }

    /**
     * An optional description string, which is mostly a description of the relation.
     *
     * @param comment The description
     * @return the builder
     */
    public RelationBuilder comment(String comment) {
        this.description = comment;
        return this;
    }

    /**
     * Add an entity to the list of domain entities
     *
     * @param domain the entity to be added
     * @return the builder
     */
    public RelationBuilder domain(Resource domain) {
        this.domains.add(domain);
        return this;
    }

    /**
     * Add an entity to the list of range entities
     *
     * @param range the entity to be added
     * @return the builder
     */
    public RelationBuilder range(Resource range) {
        this.ranges.add(range);
        return this;
    }

    /**
     * Define a relation which is the inverse relation of this relation
     *
     * @param inverse the inverse relation
     * @return the builder
     */
    public RelationBuilder inverse(Relation inverse) {
        this.inverse = inverse;
        return this;
    }

    /**
     * Add a relation which is the super relation of this relation. This is how relation inheritance is implemented.
     *
     * @param superRelation the super relation
     * @return the builder
     */
    public RelationBuilder superRelation(OntProperty superRelation) {
        this.superRelation = superRelation;
        return this;
    }

    /**
     * Builder method: Create the relation with the predefined configuration.
     *
     * @return the created relation
     */
    public DEMRelation create() {
        return new DEMRelation(this);
    }
}
