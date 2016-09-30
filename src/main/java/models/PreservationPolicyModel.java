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

import entities.*;

public class PreservationPolicyModel extends AbstractModel {
    public static DigitalPreservationPolicy.DigitalPreservationPolicyTemplate digitalPreservationPolicy;
    public static GuidancePolicyAccess.GuidancePolicyAccessTemplate guidancePolicyAccess;
    public static GuidancePolicyTrustworthyDigitalRepositories.GuidancePolicyAuditAndCertificationTemplate guidancePolicyTrustworthyDigitalRepositories;
    public static GuidancePolicyAuthenticity.GuidancePolicyAuthenticityTemplate guidancePolicyAuthenticity;
    public static GuidancePolicyBitPreservation.GuidancePolicyBitPreservationTemplate guidancePolicyBitPreservation;
    public static GuidancePolicyFunctionalPreservation.GuidancePolicyFunctionalPreservationTemplate guidancePolicyFunctionalPreservation;
    public static GuidancePolicyMetadata.GuidancePolicyMetadataTemplate guidancePolicyMetadata;
    public static GuidancePolicyOrganisation.GuidancePolicyOrganisationTemplate guidancePolicyOrganisation;
    public static GuidancePolicyRights.GuidancePolicyRightsTemplate guidancePolicyRights;
    public static GuidancePolicyStandards.GuidancePolicyStandardsTemplate guidancePolicyStandards;

    public PreservationPolicyModel() {
        super("http://www.pericles-project.eu/ns/DEM-preservation#", "SCAPE-Preservation", "This is the integration of the SCAPE project's guidance policy model into the DEM. The guidance " +
                "model is described in detail at http://wiki.opf-labs.org/display/SP/Policy+Elements");
        importModel(DEM.POLICY_MODEL);
    }

    @Override
    public void createModelEntities() {
        digitalPreservationPolicy = new DigitalPreservationPolicy.DigitalPreservationPolicyTemplate(this);
        guidancePolicyAccess = new GuidancePolicyAccess.GuidancePolicyAccessTemplate(this);
        guidancePolicyTrustworthyDigitalRepositories = new GuidancePolicyTrustworthyDigitalRepositories.GuidancePolicyAuditAndCertificationTemplate(this);
        guidancePolicyAuthenticity = new GuidancePolicyAuthenticity.GuidancePolicyAuthenticityTemplate(this);
        guidancePolicyBitPreservation = new GuidancePolicyBitPreservation.GuidancePolicyBitPreservationTemplate(this);
        guidancePolicyFunctionalPreservation = new GuidancePolicyFunctionalPreservation.GuidancePolicyFunctionalPreservationTemplate(this);
        guidancePolicyMetadata = new GuidancePolicyMetadata.GuidancePolicyMetadataTemplate(this);
        guidancePolicyOrganisation = new GuidancePolicyOrganisation.GuidancePolicyOrganisationTemplate(this);
        guidancePolicyRights = new GuidancePolicyRights.GuidancePolicyRightsTemplate(this);
        guidancePolicyStandards = new GuidancePolicyStandards.GuidancePolicyStandardsTemplate(this);
    }

    @Override
    public void createModelRelations() {
    }
}
