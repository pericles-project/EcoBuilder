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

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class SCAPEGuidance extends AbstractModel {

    public static final String NS = "http://purl.org/DP/guidance#";
    private static OntModel m_model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM, null);

    public SCAPEGuidance() {
        super(NS, "This is the integration of the SCAPE project's guidance policy model into the DEM. The guidance " +
                "model is described in detail at http://wiki.opf-labs.org/display/SP/Policy+Elements");
        this.model = m_model;
    }

    public static String getURI() {
        return NS;
    }

    public static final OntClass authenticity = m_model.createClass("http://purl.org/DP/guidance#Authenticity");
    public static final OntClass functionalPreservation = m_model.createClass("http://purl.org/DP/guidance#FunctionalPreservation");
    public static final OntClass bitPreservation = m_model.createClass("http://purl.org/DP/guidance#BitPreservation");
    public static final OntClass metadata = m_model.createClass("http://purl.org/DP/guidance#Metadata");
    public static final OntClass organisation = m_model.createClass("http://purl.org/DP/guidance#Organisation");
    public static final OntClass standards = m_model.createClass("http://purl.org/DP/guidance#Standards");
    public static final OntClass access = m_model.createClass("http://purl.org/DP/guidance#Access");
    public static final OntClass rights = m_model.createClass("http://purl.org/DP/guidance#Rights");
    public static final OntClass trustworthyDigitalRepositories = m_model.createClass("http://purl.org/DP/guidance#TrustworthyDigitalRepositories");

    @Override
    public void createModelEntities() {
    }

    @Override
    public void createModelRelations() {
    }
}
