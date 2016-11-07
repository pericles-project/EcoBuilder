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

import LRMv2.LRM_dynamic_schema;
import LRMv2.LRM_static_schema;
import entities.Template;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import relations.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static models.DEM.*;

/**
 * Base class for the ecosystem ontologies including the DEM sub models, the scenario model and the LRM wrapper.
 * <p>
 * Imports all necessary models from which this model inherits.
 * <p>
 * First all model entities are created. Afterwards the model relations are created, which link from the firstly created
 * entities.
 * <p>
 */
public abstract class AbstractModel implements Serializable {
    public final String prefix; // Alias for the namespace
    transient public final String description; // Description of the model
    transient public final String namespace;
    transient public OntModel model;
    transient public final Ontology ontology;
    transient protected final List<AbstractModel> imports = new ArrayList<>();
    transient public final List<Template> templates = new ArrayList<>();
    transient public final List<Relation> relations = new ArrayList<>();

    public AbstractModel(String prefix, String description) {
        this(getNamespace(prefix), prefix, description);
    }

    /**
     * Constructor of a model. It will create the ontology model with its resources and relations.
     *
     * @param namespace Namespace of the model
     * @param prefix    Prefix of the model
     */
    public AbstractModel(String namespace, String prefix, String description) {
        this.namespace = namespace;
        this.prefix = prefix;
        this.description = description;
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        model.setNsPrefix(prefix, namespace);
        ontology = model.createOntology(namespace);
        importLRM();
        createModelEntities();
        createModelRelations();
    }

    /**
     * Imports the LRM static and dynamic model. All DEM-ontologies inherit from the LRM.
     */
    private void importLRM() {
        if (this instanceof LRM_static_schema || this instanceof LRM_dynamic_schema) {
            return;
        }
        // All DEM models import the static and the dynamic LRM:
        importModel(LRM_STATIC);
        importModel(LRM_DYNAMIC);
        if (this instanceof CoreModel) {
            return;
        }
        // All DEM sub ontologies import the core model:
        importModel(CORE_MODEL);
    }

    /**
     * Imports a new model to this model.
     *
     * @param newImport the model to be imported.
     */
    public void importModel(AbstractModel newImport) {
        if (!imports.contains(newImport)) {
            model.setNsPrefix(newImport.prefix, newImport.namespace);
            ontology.addImport(newImport.ontology);
            imports.add(newImport);
        }
    }

    /**
     * To be implemented by the sub classes: Creation of the model entities.
     */
    public abstract void createModelEntities();

    /**
     * To be implemented by the sub classes: Creation of the model relations.
     * The relations have to be created after the entities, because they refer to the existing entities.
     */
    public abstract void createModelRelations();

    /**
     * Utility function ot create an appropriate namespace string for a pericles DEM sub ontology.
     *
     * @param prefix prefix of the ontology
     * @return the models namespace
     */
    private static String getNamespace(String prefix) {
        return "http://www.pericles-project.eu/ns/" + prefix + "#";
    }

    /**
     * The user can input any kind of string for a resource name. The resources uri is generated from this input name,
     * but all non alphabetically characters are removed first.
     *
     * @param userInput The name suggested by the user
     * @return the sanitized name
     */
    public static String sanitizeName(String userInput) {
        String whitelistedChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] input = userInput.toCharArray();
        String output = "";
        for (int i = 0; i < input.length; i++) {
            if (whitelistedChars.contains("" + input[i])) {
                output += input[i];
            }
        }
        return output;
    }

    @Override
    public String toString() {
        return namespace;
    }
}
