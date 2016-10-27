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

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This is a data structure to manage the configuration for the connection to the ERMR (Entity Registry and Model Repository).
 * Configure the connection to ERMR.
 */
public class Configuration {
    private static final String API = "/api/triple/";

    protected static String repository = "EcoBuilder";
    protected static String url = "";
    protected static String user = "";
    protected static String password = "";

    /**
     * Create ERMR repository with: PUT <root URI>/api/triple/<NewRepositoryName>
     *
     * @return
     * @throws MalformedURLException
     */
    protected static URL getRepositoryURL() {
        try {
            return new URL(url + API + repository);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static URL getAPIURL(){
        try {
            return new URL(url + API);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static URL getSubmissionURL() {
        try {
            return new URL(url + API + repository + "/statements");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValid() {
        return !(empty(repository) || empty(url) || empty(user) || empty(password));
    }

    private static boolean empty(String string) {
        return string == null || string.equals("") || string.equals(" ");
    }
}
