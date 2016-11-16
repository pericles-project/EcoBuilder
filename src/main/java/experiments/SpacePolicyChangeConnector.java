package experiments;

import ERMR.Configuration;
import ERMR.ERMRConnection;

import java.io.*;


/**
 * Created by Johannes on 15.11.16.
 *
 */
public class SpacePolicyChangeConnector {
    Experiment spaceExperiment;
    // Default http://www.pericles-project.eu/ns/
    // this is a temporary solution to place the ontology files for this experiment on an arbitary server
    private String namespace = "http://c102-086.cloud.gwdg.de/";
    // the path where the model shoud be saved
    private String path = "/tmp/";

    public SpacePolicyChangeConnector() {
        spaceExperiment = new SpacePolicyChangeExample();

        File turtleFile = new File(path+spaceExperiment.name+".ttl");
        StringWriter outputWriter = new StringWriter();
        try {
            spaceExperiment.scenario.model.write(outputWriter, "TURTLE", namespace);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // this is a ugly hack to temporary replace the name space
        String output = outputWriter.toString();
        output = output.replace("http://www.pericles-project.eu/", namespace);
        output = output.replace("http://xrce.xerox.com/", namespace+"ns/");

        //System.out.println(output);

        // write it to file
        FileWriter fw = null;
        try {
            fw = new FileWriter(turtleFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(output);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send to ERMR Not tested yet
        //sendToERMR(turtleFile);

    }

    private void sendToERMR(File scenario) {
        Configuration.setCredentials("MyRepository", "https://localhost", "user", "password");
        ERMRConnection connection = new ERMRConnection();
        connection.send(scenario);

    }


    public static void main (String args[]) {
        new SpacePolicyChangeConnector();
    }

}
