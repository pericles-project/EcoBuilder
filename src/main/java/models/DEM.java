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

import DomainOntology.DVAWrapper;
import LRMv2.LRM_dynamic_schema;
import LRMv2.LRM_static_schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Main manager of the different DEM template models. The models must exist only once! Therefore the DEM class is
 * implemented as Singleton.
 * <p>
 * The Digital Ecosystem Model builds on top of the The Linked Resource Model (LRM).
 * <p>
 */
public class DEM {
    // The DEM imports the LRM static and dynamic schema:
    public static final LRM_static_schema LRM_STATIC = new LRM_static_schema();
    public static final LRM_dynamic_schema LRM_DYNAMIC = new LRM_dynamic_schema();
    // The Core entities - This model is obligatory:
    public static final CoreModel CORE_MODEL = new CoreModel();
    // Policies and Quality Assurance:
    public static final PolicyModel POLICY_MODEL = new PolicyModel();
    // Process and change modelling:
    public static final ProcessModel PROCESS_MODEL = new ProcessModel();
    // Entities to model the technical infrastructure in more detail:
    public static final InfrastructureModel INFRASTRUCTURE_MODEL = new InfrastructureModel();
    // Entities for arbitrary graph analysis:
    public static final AnalysisModel ANALYSIS_MODEL = new AnalysisModel();
    // SCAPE Guidance - Preservation extension of the Policy Model:
    public static final PreservationPolicyModel PRESERVATION_POLICY_MODEL = new PreservationPolicyModel();

    // Digital Video Artwork Domain Ontology:
    public static final DVAWrapper DVA_MODEL = new DVAWrapper();

    public static boolean addedDVA = false;

    /**
     * The constructor is private, as the class is a Singleton. The template models must exist only once!
     */
    private DEM() {
    }

    public static List<AbstractModel> getModels() {
        List<AbstractModel> models = new ArrayList<AbstractModel>();
        models.add(CORE_MODEL);
        models.add(POLICY_MODEL);
        models.add(PROCESS_MODEL);
        models.add(INFRASTRUCTURE_MODEL);
        models.add(ANALYSIS_MODEL);
        models.add(PRESERVATION_POLICY_MODEL);
        if (addedDVA) {
            models.add(DVA_MODEL);
        }
        return models;
    }
}
