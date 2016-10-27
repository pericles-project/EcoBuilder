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

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

/**
 * Connects to the PERICLES Entity Registry and Model Repository (ERMR) to create model repositories and to submit the
 * scenario Digital Ecosystem Model instances.
 */
public class ERMRConnection {
    /**
     * Sends a turtle file containing the representation of a scenario model to the ERMR, and creates the needed
     * model repository, if it is not existing yet.
     *
     * @param turtleFile the turtle file
     */
    public void send(File turtleFile) {
        try {
            sendScenarioModel(turtleFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Does some https magic to send the scenario triples to ERMR's triple store.
     */
    private boolean sendScenarioModel(File turtleFile) throws Exception {
        setSSLContext();
        if (!createRepository()) {  // create if it doesn't exist yet
            System.err.println("Couldn't create repository");
            return false;
        }
        HttpsURLConnection connection = getConnection(Configuration.getSubmissionURL());
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/turtle");
        connection.setDoOutput(true); // use the connection to output the turtle file
        connection.connect();
        FileInputStream inputStream = new FileInputStream(turtleFile);
        DataOutputStream request = new DataOutputStream(connection.getOutputStream());
        byte[] buffer = new byte[0xFFF];
        int byteCount; // number of bytes read into buffer
        while ((byteCount = inputStream.read(buffer)) != -1) {
            request.write(buffer, 0, byteCount);
        }
        inputStream.close();
        request.flush();
        request.close();
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        return responseCode == 201;
    }


    /**
     * This will create the repository, if it doesn't exist yet.
     */
    private boolean createRepository() throws IOException {
        if (repositoryExists()) {
            return true;
        } // else create repository:
        HttpsURLConnection connection = getConnection(Configuration.getRepositoryURL());
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.connect();
        int responseCode = connection.getResponseCode();
        connection.disconnect();
        return responseCode == 201;
    }

    /**
     * Checks if the currently configured repository already exists.
     *
     * @return flag, if the repository already exists
     */
    private boolean repositoryExists() {
        boolean exists = false;
        try {
            HttpsURLConnection connection = getConnection(Configuration.getAPIURL());
            connection.setRequestMethod("GET");
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.connect();
            InputStream response = connection.getInputStream();
            Scanner scanner = new Scanner(response).useDelimiter("\\A");
            String repositoryList = scanner.hasNext() ? scanner.next() : "";
            String pattern = "\"title\": \"" + Configuration.repository + "\"";
            exists = repositoryList.contains(pattern);
            response.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return exists;
    }

    /**
     * Creates an TLS connection to the URL.
     *
     * @param url
     * @return
     * @throws IOException
     */
    private HttpsURLConnection getConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        setCredentials(connection);
        setHostnameVerifier(connection);
        connection.setUseCaches(false);
        return connection;
    }

    /**
     * FIXME: enable certificate check, once the ERMR has a valid certificate.
     *
     * @param connection
     */
    private void setHostnameVerifier(HttpsURLConnection connection) {
        connection.setHostnameVerifier((s, sslSession) -> true);
    }

    /**
     * Sets username and password to log in to ERMR
     *
     * @param connection
     */
    private void setCredentials(HttpsURLConnection connection) {
        connection.setRequestProperty("username", Configuration.user);
        connection.setRequestProperty("password", Configuration.password);
    }

    /**
     * Sets the SSL Context
     *
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private void setSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(new KeyManager[0], new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new SecureRandom());
        SSLContext.setDefault(context);
    }
}
