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
package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.Serializable;

public class RangeValueView extends GridPane implements Serializable {
    public String value;
    transient public RelationView parentRelation;

    public RangeValueView(String value, RelationView parentProperty) {
        if (value != null) {
            this.value = value;
        } else {
            this.value = "add value";
        }
        this.parentRelation = parentProperty;
        parentProperty.childValueEntities.add(this);
        setId(EcoBuilder.TARGET_ENTITY_ID);
        Button remove = new Button("X");
        remove.setId(EcoBuilder.REMOVE_BUTTON_ID);
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                removeTarget();
            }
        });
        Text text = new Text(value);
        setConstraints(text, 0, 0);
        setConstraints(remove, 1, 0);
        getChildren().addAll(text, remove);
    }

    private void removeTarget() {
        parentRelation.removeValueTarget(this);
    }

    @Override
    public String toString() {
        return value;
    }
}
