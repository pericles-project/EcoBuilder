package experiments;

import ERMR.Configuration;
import ERMR.ERMRConnection;

import java.io.*;


/**
 * This programm will output the space policy change demo model to a file
 * and upload it to ERMR.
 * Created by Johannes on 15.11.16.
 *
 * Run it from command line with
 * java -cp EcoBuilder-v1_1-jar-with-dependencies.jar experiments.SpacePolicyChangeConnector PATH ERMR_URL ERMR_REPOSIORY_NAME ERMR_USERNAME ERMR_PASSWWORD
 *
 */
public class SpacePolicyChangeConnector {
    Experiment spaceExperiment;
    // the path where the model shoud be saved
    private String path = "/tmp/";
    private String ermrURL;
    private String ermrRepository;
    private String ermrUsername;
    private String ermrPassword;

    public SpacePolicyChangeConnector(String path, String ermrURL, String ermrRepository, String ermrUsername, String ermrPassword) {
        this.path = path;
        this.ermrURL = ermrURL;
        this.ermrRepository = ermrRepository;
        this.ermrUsername = ermrUsername;
        this.ermrPassword = ermrPassword;

        spaceExperiment = new SpacePolicyChangeExample();

        File turtleFile = new File(path+spaceExperiment.name+".ttl");
        System.out.println("Saving model to " + turtleFile.getAbsolutePath());
        StringWriter outputWriter = new StringWriter();
        try {
            spaceExperiment.scenario.model.write(outputWriter, "TURTLE", "http://www.pericles-project.eu/ns");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String output = outputWriter.toString();
        // Somehow Jena seems to leave in the escape characters from Strings so the text stays \" instead of ".
        // this is an ugly hack to fix the Strings.
        output = output.replace("\\\"", "\"");
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
        System.out.println("Sending the model to ERMR.");
        // send to ERMR
        sendToERMR(turtleFile);

    }

    private void sendToERMR(File scenario) {
        Configuration.setCredentials(ermrRepository, ermrURL, ermrUsername, ermrPassword);
        ERMRConnection connection = new ERMRConnection();
        connection.send(scenario);

    }


    public static void main (String args[]) {
        System.out.println("SpacePolicyChange Demo Helper. Writes the model to file and sends it to ERMR.");
        if (null == args || args.length != 5) {
            System.out.println("Error! no parameter given or wrong number of arguments.");
            System.out.println("Usage: java experiments.SpacePolicyChangeConnector PATH ERMR_URL ERMR_REPOSIORY_NAME ERMR_USERNAME ERMR_PASSWWORD");
            System.out.println("PATH is the path where the output file is saved - please add a trailing slash, ERMR_URL is the URL to ERMR, " +
                    "ERMR_REPOSITORY is the name of the repository where the triples are stored, ERMR_USERNAME and ERMR_PASSWORD is a valid ERMR user.");
            System.exit(1);
        } else {
            new SpacePolicyChangeConnector(args[0], args[1], args[2], args[3], args[4]);
            System.out.println("Done.");
            System.exit(0);
        }

    }

}
