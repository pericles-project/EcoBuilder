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

import models.ProcessModel;
import models.CoreModel;
import models.ScenarioModel;

/**
 * A ProMV is the first process of the process triple {ProMV,ProEV,ProT} which is recommended to be modelled as
 * process implementation of a policy. A ProMV operates on CORE_MODEL level. Its purpose is to get the set of entities which
 * are constrained by a policy from the CORE_MODEL, and to pass each entity into a ProEV. Therefore the ProMV is called in case of
 * policy changes, or after a huge CORE_MODEL process. A ProMV can be an aggregated resource of sub-ProMV.
 */
public class ProcessForModelValidation extends Process {

    public ProcessForModelValidation(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, ProcessModel.processForModelValidation);
    }

    public static class ProcessForModelValidationTemplate extends ProcessTemplate {
        public ProcessForModelValidationTemplate(ProcessModel processModel) {
            super(processModel, "Process For Model Validation",
                    "A ProMV is the first process of the process triple "
                            + "{ProMV,ProEV,ProT} which is recommended to be modelled as "
                            + "process implementation of a policy. A ProMV operates on "
                            + "CORE_MODEL level. Its purpose is to get the set of entities which "
                            + "are constrained by a policy from the CORE_MODEL, and to pass each "
                            + "entity into a ProEV. Therefore the ProMV is called in case of "
                            + "policy changes, or after a huge CORE_MODEL process. A ProMV can be an "
                            + "aggregated resource of sub-ProMV.", CoreModel.process);
        }

        @Override
        public ProcessForModelValidation createEntity(ScenarioModel model, String ID) {
            return new ProcessForModelValidation(model, ID);
        }
    }
}
