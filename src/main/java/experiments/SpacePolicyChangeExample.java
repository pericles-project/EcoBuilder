package experiments;

import LRMv2.LRM_dynamic_schema;
import com.hp.hpl.jena.ontology.OntProperty;
import entities.*;
import models.AbstractModel;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.time.format.DateTimeFormatter.*;

/**
 * Created by Johannes on 12.10.16.
 *
 * needs to be placed withing the experiments package of EcoBuilder and following must be added to saver.ExperimensSaver:
 * method save:
 *          saveExperiment(new SpacePolicyChangeExample());
 * Then start the GUI and go to File - Generate Examples. In the selected folder you will find a ScenarioSpacepolicychangeexampleXXX.ttl
 *
 */
public class SpacePolicyChangeExample extends Experiment {

    private Policy dataFormatPolicy;
    private Policy dataReleasePolicy;
    private ServiceInterface webPortal;
    private DigitalObject seviriImages;
    private Significance mediumRisk06;
    public SpacePolicyChangeExample() {
        super("Space-policy-change-example");

        // Entities EUMETSAT
        dataFormatPolicy = new Policy(scenario, "Data Format");
        dataFormatPolicy.addStatement("Data Format must be HDF.");
        dataReleasePolicy = new Policy(scenario, "Release date policy");
        dataReleasePolicy.addComment("Data available to general public after X time");
        dataReleasePolicy.addStatement("Meteosat Data and Derived Products older than 24 hours are distributed on request from the EUMETSAT Data Archive in digital " +
                "and graphical form via the associated operational service in formats which represent both full and partial spatial coverage as well as both full " +
                "and partial spatial resolution", "Natural language","en.uk");
        dataReleasePolicy.addStatement("Meteosat Data and Derived Products older than (time_before_release) hours are distributed on request [...] as both full and partial spatial resolution","Natural Language", "en/uk");
        dataReleasePolicy.addStatement("executable SPIN-Rule text here, TODO", "SPIN");// TODO: add SPIN rule here
        dataReleasePolicy.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        dataReleasePolicy.hasType(Policy.TypeOfPolicy.MANDATORY);
        dataReleasePolicy.addProperty(dataReleasePolicy.getOntModel().createOntProperty(dataReleasePolicy.getURI()+ AbstractModel.sanitizeName("time_before_release")),"24");
        webPortal = new ServiceInterface(scenario, "EUMETSAT Web Portal");
        seviriImages = new DigitalObject(scenario, "SEVIRI Images");

        mediumRisk06 = new Significance(scenario, "Medium Risk");

        EcosystemEntity hdf = new EcosystemEntity(scenario,
                "HDF Format");
        hdf.addOntClass(LRM_dynamic_schema.Specification);
        hdf.addComment("HDF");

        // relations
        webPortal.providesAccessTo(seviriImages);
        dataReleasePolicy.constraints(seviriImages);
        dataFormatPolicy.constraints(hdf);
        seviriImages.addProperty(LRM_dynamic_schema.specifiedBy, hdf);
        mediumRisk06.hasValue(0.6f);

        mediumRisk06.from(hdf);
        mediumRisk06.to(seviriImages);

        // Create a few dummy DO instances
        OntProperty releaseState = scenario.model.createOntProperty("http://www.pericles-project.eu/ns/DEM-Scenario#releaseState");
        OntProperty dateCreated = scenario.model.createOntProperty("http://www.pericles-project.eu/ns/DEM-Scenario#dateCreated");

        ZonedDateTime creationDate = ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC"));
        DateTimeFormatter DATEFORMAT = ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

        DigitalObject dummy1 = new DigitalObject(scenario, "SEVIRI Image 1");
        dummy1.partOf(seviriImages);
        dummy1.addProperty(releaseState, "private");
        dummy1.addProperty(dateCreated, creationDate.format(DATEFORMAT));

        creationDate=creationDate.plusHours(2);
        DigitalObject dummy2 = new DigitalObject(scenario, "SEVIRI Image 2");
        dummy2.partOf(seviriImages);
        dummy2.addProperty(releaseState, "private");
        dummy2.addProperty(dateCreated, creationDate.format(DATEFORMAT));
        creationDate=creationDate.plusHours(2);

        DigitalObject dummy3 = new DigitalObject(scenario, "SEVIRI Image 3");
        dummy3.partOf(seviriImages);
        dummy3.addProperty(releaseState, "public");
        dummy3.addProperty(dateCreated, creationDate.format(DATEFORMAT));
        creationDate=creationDate.plusHours(2);

        DigitalObject dummy4 = new DigitalObject(scenario, "SEVIRI Image 4");
        dummy4.partOf(seviriImages);
        dummy4.addProperty(releaseState, "public");
        dummy4.addProperty(dateCreated, creationDate.format(DATEFORMAT));


    }
}
