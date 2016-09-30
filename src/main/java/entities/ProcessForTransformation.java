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
 * A ProT is the third process of the process triple {ProMV,ProEV,ProT} which is recommended to be modelled as
 * process implementation of a policy. A ProT operates on entity level and transforms an entity from an invalid
 * state into a valid state regarding the constrains of a policy. ProT gets the entity input from ProEV in case of the
 * occurrence of an invalid entity. A ProT can be an aggregated resource of sub-ProT.
 */
public class ProcessForTransformation extends Process {

    public ProcessForTransformation(ScenarioModel scenario, String identifier) {
        super(scenario, identifier, ProcessModel.processForTransformation);
    }

    public static class ProcessForTransformationTemplate extends ProcessTemplate {

        public ProcessForTransformationTemplate(ProcessModel changeModel) {
            super(changeModel, "Process For Transformation",
                    "A ProT is the third process of the process triple "
                            + "{ProMV,ProEV,ProT} which is recommended to be modelled as "
                            + "process implementation of a policy. A ProT operates on "
                            + "entity level and transforms an entity from an invalid "
                            + "state into a valid state regarding the constrains of a "
                            + "policy. ProT gets the entity input from ProEV in case of the "
                            + "occurrence of an invalid entity. A ProT can be an aggregated "
                            + "resource of sub-ProT.", CoreModel.process);
        }

        @Override
        public ProcessForTransformation createEntity(ScenarioModel model, String ID) {
            return new ProcessForTransformation(model, ID);
        }
    }
}
