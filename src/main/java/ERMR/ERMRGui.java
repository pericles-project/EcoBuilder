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
package ERMR;

import gui.EcoBuilder;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static gui.EcoBuilder.DIALOG_ID;
import static gui.EcoBuilder.STYLESHEET;

/**
 * A dialogue for the configuration of the ERMR setting. The scenario triples can be send to the ERMR's triple store
 * once the connection is configured.
 */
public class ERMRGui extends GridPane {
    private Button close = new Button("Close");
    private Button save = new Button("Save configuration");
    private TextField repository = new TextField();
    private TextField uri = new TextField();
    private TextField user = new TextField();
    private PasswordField password = new PasswordField();
    private Text repositoryText = new Text("Repository:");
    private Text uriText = new Text("ERMR URI:");
    private Text userText = new Text("User:");
    private Text passwordText = new Text("Password:");

    public ERMRGui(EcoBuilder ecoBuilder) {
        setId(DIALOG_ID);
        int x = 0;
        int y = 1;
        setConstraints(repositoryText, x, y, 2, 1);
        y++;
        setConstraints(repository, x, y, 2, 1);
        y++;
        setConstraints(uriText, x, y, 2, 1);
        y++;
        setConstraints(uri, x, y, 2, 1);
        y++;
        setConstraints(userText, x, y, 2, 1);
        y++;
        setConstraints(user, x, y, 2, 1);
        y++;
        setConstraints(passwordText, x, y, 2, 1);
        y++;
        setConstraints(password, x, y, 2, 1);
        y++;
        setConstraints(close, x, y, 1, 1);
        x++;
        setConstraints(save, x, y, 1, 1);
        getChildren().addAll(repository, uri, user, password, close, save, repositoryText,
                uriText, userText, passwordText);
        Scene scene = new Scene(this, 250, 300);
        scene.getStylesheets().add(STYLESHEET);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Configure ERMR");
        stage.setScene(scene);
        stage.show();
        close.setOnAction(e -> stage.hide());
        save.setOnAction(e -> configure(stage));
        Configuration.repository = "EcoBuilder";
        repository.setText(Configuration.repository);
        uri.setText(Configuration.url);
        user.setText(Configuration.user);
        password.setText(Configuration.password);
    }

    private void configure(Stage stage) {
        Configuration.url = uri.getText();
        Configuration.repository = repository.getText();
        Configuration.user = user.getText();
        String pw = password.getText();
        if (pw.length() > 100) {
            pw = pw.substring(0, 100);
        }
        Configuration.password = pw;
        stage.hide();
    }
}
