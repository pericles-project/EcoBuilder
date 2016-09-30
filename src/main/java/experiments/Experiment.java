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
package experiments;

import models.ScenarioModel;

import java.util.HashSet;

/**
 * This class serves as base class for CORE_MODEL instantiations. If you want to
 * create a CORE_MODEL instance, then create a class which inherits from this
 * class.
 * <p>
 * If an experiment should be created using the EcoBuilder GUI, then add it to the {@link saver.ExperimentsSaver}.
 */
public abstract class Experiment {
    public static ScenarioModel scenario;
    public final String name;

    public Experiment(String name) {
        this.name = name;
        scenario = new ScenarioModel(new HashSet<>());
    }
}
