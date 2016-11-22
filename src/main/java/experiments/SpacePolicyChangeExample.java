package experiments;

import LRMv2.LRM_dynamic_schema;
import LRMv2.LRM_static_schema;
import entities.*;
import models.AbstractModel;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.*;

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
    private static String SPINTEXT = "# baseURI: http://www.pericles-project.eu/ns/spinrules\n" +
            "# imports: http://www.pericles-project.eu/ns/DEM-Scenario#\n" +
            "# imports: http://spinrdf.org/spl\n" +
            "\n" +
            "@prefix : <http://www.pericles-project.eu/ns/spinrules#> .\n" +
            "@prefix DEM-Scenario: <http://www.pericles-project.eu/ns/DEM-Scenario#> .\n" +
            "@prefix arg: <http://spinrdf.org/arg#> .\n" +
            "@prefix owl: <http://www.w3.org/2002/07/owl#> .\n" +
            "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
            "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n" +
            "@prefix sp: <http://spinrdf.org/sp#> .\n" +
            "@prefix spin: <http://spinrdf.org/spin#> .\n" +
            "@prefix spl: <http://spinrdf.org/spl#> .\n" +
            "@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .\n" +
            "\n" +
            "<http://www.pericles-project.eu/ns/DEM-Core#DigitalObject>\n" +
            "  spin:rule [\n" +
            "      rdf:type sp:Construct ;\n" +
            "      sp:templates (\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"delta\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate <http://xrce.xerox.com/LRM#changedBy> ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"resource\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object <http://xrce.xerox.com/LRM#RDF-Delta> ;\n" +
            "            sp:predicate rdf:type ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"delta\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"deletion_statement\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate <http://xrce.xerox.com/LRM#deletion> ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"delta\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"insertion_statement\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate <http://xrce.xerox.com/LRM#insertion> ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"delta\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object rdf:Statement ;\n" +
            "            sp:predicate rdf:type ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"deletion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object DEM-Scenario:releaseState ;\n" +
            "            sp:predicate rdf:predicate ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"deletion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"state\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate rdf:object ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"deletion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object rdf:Statement ;\n" +
            "            sp:predicate rdf:type ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"insertion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object DEM-Scenario:releaseState ;\n" +
            "            sp:predicate rdf:predicate ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"insertion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object \"public\" ;\n" +
            "            sp:predicate rdf:object ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"insertion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "        ) ;\n" +
            "      sp:where (\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"state\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate DEM-Scenario:releaseState ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"resource\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Filter ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:eq ;\n" +
            "                sp:arg1 [\n" +
            "                    sp:varName \"state\" ;\n" +
            "                  ] ;\n" +
            "                sp:arg2 \"private\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object <http://www.pericles-project.eu/ns/DEM-Core#DigitalObject> ;\n" +
            "            sp:predicate rdf:type ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"resource\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"dateCreated\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate DEM-Scenario:dateCreated ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"resource\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object <http://www.pericles-project.eu/ns/DEM-Core#Policy> ;\n" +
            "            sp:predicate rdf:type ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"policy\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            sp:object [\n" +
            "                sp:varName \"hrs\" ;\n" +
            "              ] ;\n" +
            "            sp:predicate DEM-Scenario:Releasedatepolicytimebeforerelease ;\n" +
            "            sp:subject [\n" +
            "                sp:varName \"policy\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Bind ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:day ;\n" +
            "                sp:arg1 [\n" +
            "                    rdf:type sp:sub ;\n" +
            "                    sp:arg1 [\n" +
            "                        rdf:type sp:now ;\n" +
            "                      ] ;\n" +
            "                    sp:arg2 [\n" +
            "                        sp:varName \"dateCreated\" ;\n" +
            "                      ] ;\n" +
            "                  ] ;\n" +
            "              ] ;\n" +
            "            sp:variable [\n" +
            "                sp:varName \"days\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Bind ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:hours ;\n" +
            "                sp:arg1 [\n" +
            "                    rdf:type sp:sub ;\n" +
            "                    sp:arg1 [\n" +
            "                        rdf:type sp:now ;\n" +
            "                      ] ;\n" +
            "                    sp:arg2 [\n" +
            "                        sp:varName \"dateCreated\" ;\n" +
            "                      ] ;\n" +
            "                  ] ;\n" +
            "              ] ;\n" +
            "            sp:variable [\n" +
            "                sp:varName \"hours\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Bind ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:add ;\n" +
            "                sp:arg1 [\n" +
            "                    rdf:type sp:mul ;\n" +
            "                    sp:arg1 [\n" +
            "                        sp:varName \"days\" ;\n" +
            "                      ] ;\n" +
            "                    sp:arg2 24 ;\n" +
            "                  ] ;\n" +
            "                sp:arg2 [\n" +
            "                    sp:varName \"hours\" ;\n" +
            "                  ] ;\n" +
            "              ] ;\n" +
            "            sp:variable [\n" +
            "                sp:varName \"ageInHours\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Filter ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:ge ;\n" +
            "                sp:arg1 [\n" +
            "                    sp:varName \"ageInHours\" ;\n" +
            "                  ] ;\n" +
            "                sp:arg2 [\n" +
            "                    sp:varName \"hrs\" ;\n" +
            "                  ] ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Bind ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:bnode ;\n" +
            "              ] ;\n" +
            "            sp:variable [\n" +
            "                sp:varName \"delta\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Bind ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:bnode ;\n" +
            "              ] ;\n" +
            "            sp:variable [\n" +
            "                sp:varName \"deletion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "          [\n" +
            "            rdf:type sp:Bind ;\n" +
            "            sp:expression [\n" +
            "                rdf:type sp:bnode ;\n" +
            "              ] ;\n" +
            "            sp:variable [\n" +
            "                sp:varName \"insertion_statement\" ;\n" +
            "              ] ;\n" +
            "          ]\n" +
            "        ) ;\n" +
            "    ] ;\n" +
            ".\n" +
            "<http://www.pericles-project.eu/ns/spinrules>\n" +
            "  rdf:type owl:Ontology ;\n" +
            "  spin:imports <http://topbraid.org/spin/owlrl-all> ;\n" +
            "  owl:imports DEM-Scenario: ;\n" +
            "  owl:imports <http://spinrdf.org/spl> ;\n" +
            "  owl:versionInfo \"Created with TopBraid Composer\" ;\n" +
            ".\n" +
            "spin:rule\n" +
            "  spin:rulePropertyMaxIterationCount 1 ;\n" +
            ".\n";

    private Policy dataFormatPolicy;
    private Policy dataReleasePolicy;
    private ServiceInterface webPortal;
    private DigitalObject seviriImages;
    private Significance mediumRisk06;
    public SpacePolicyChangeExample() {
        super("Space-policy-change-example");

        // add the spin rule import
        scenario.ontology.getModel().setNsPrefix("spin", "http://spinrdf.org/spin#");
        Resource demScenario = scenario.ontology.getModel().createResource("http://www.pericles-project.eu/ns/DEM-Scenario#");
        Property spinImports = scenario.ontology.getModel().createProperty("http://spinrdf.org/spin#imports");
        RDFNode spinURL = scenario.ontology.getModel().createResource("http://topbraid.org/spin/owlrl-all");
        Statement spin = scenario.ontology.getModel().createStatement(demScenario, spinImports, spinURL);
        scenario.ontology.getModel().add(spin);



        // Entities EUMETSAT
        dataFormatPolicy = new Policy(scenario, "Data Format");
        PolicyStatement hdfStatement = new PolicyStatement(scenario, "DataInHDF");
        hdfStatement.addProperty(LRM_static_schema.definition, "Data Format must be HDF.");
        hdfStatement.isStatementOf(dataFormatPolicy);

        dataReleasePolicy = new Policy(scenario, "Release date policy");
        dataReleasePolicy.addComment("Data available to general public after X time");
        PolicyStatement dataReleaseStatement = new PolicyStatement(scenario, "Meteosat_24h");
        dataReleaseStatement.addProperty(LRM_static_schema.definition, "Meteosat Data and Derived Products older than 24 hours are distributed on request from the EUMETSAT Data Archive in digital " +
                "and graphical form via the associated operational service in formats which represent both full and partial spatial coverage as well as both full " +
                "and partial spatial resolution.");

        dataReleaseStatement.language("non-formal");
        dataReleaseStatement.format("en.uk");
        dataReleaseStatement.isStatementOf(dataReleasePolicy);

        //dataReleasePolicy.addStatement("Meteosat Data and Derived Products older than 24 hours are distributed on request from the EUMETSAT Data Archive in digital " +
        //        "and graphical form via the associated operational service in formats which represent both full and partial spatial coverage as well as both full " +
        //        "and partial spatial resolution", "Natural language","en.uk");
        PolicyStatement dataReleaseStatementGeneral = new PolicyStatement(scenario, "Meteosat_General");
        dataReleaseStatementGeneral.addProperty(LRM_static_schema.definition, "Meteosat Data and Derived Products older than (time_before_release) hours are distributed on request [...] as both full and partial spatial resolution");
        dataReleaseStatementGeneral.language("en.uk");
        dataReleaseStatementGeneral.format("non-formal");
        dataReleaseStatementGeneral.isStatementOf(dataReleasePolicy);


        //dataReleasePolicy.addStatement("Meteosat Data and Derived Products older than (time_before_release) hours are distributed on request [...] as both full and partial spatial resolution","Natural Language", "en/uk");
        //dataReleasePolicy.addStatement(SPINTEXT, "SPIN");

        PolicyStatement dataReleaseSPIN = new PolicyStatement(scenario, "SPIN_RULE");
        dataReleaseSPIN.language("SPIN");
        dataReleaseSPIN.format("formal");
        dataReleaseSPIN.addProperty(LRM_static_schema.definition, SPINTEXT);
        dataReleaseSPIN.isStatementOf(dataReleasePolicy);

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

        int n=1;
        DigitalObject dummy1 = new DigitalObject(scenario, "SEVIRI Image "+n);
        dummy1.hasPath("https://141.5.100.67/api/cdmi/eumetsatdata/SEVRIImage"+n+".jpg");
        dummy1.partOf(seviriImages);
        dummy1.addProperty(releaseState, "private");
        dummy1.addProperty(dateCreated, ResourceFactory.createTypedLiteral(creationDate.format(DATEFORMAT), XSDDatatype.XSDdateTime));
        n++;

        creationDate=creationDate.plusHours(2);
        DigitalObject dummy2 = new DigitalObject(scenario, "SEVIRI Image "+n);
        dummy2.hasPath("https://141.5.100.67/api/cdmi/eumetsatdata/SEVRIImage"+n+".jpg");
        dummy2.partOf(seviriImages);
        dummy2.addProperty(releaseState, "private");
        dummy2.addProperty(dateCreated, ResourceFactory.createTypedLiteral(creationDate.format(DATEFORMAT), XSDDatatype.XSDdateTime));
        creationDate=creationDate.plusHours(2);
        n++;


        DigitalObject dummy3 = new DigitalObject(scenario, "SEVIRI Image "+n++);
        dummy3.hasPath("https://141.5.100.67/api/cdmi/eumetsatdata/SEVRIImage"+n+".jpg");
        dummy3.partOf(seviriImages);
        dummy3.addProperty(releaseState, "public");
        dummy3.addProperty(dateCreated, ResourceFactory.createTypedLiteral(creationDate.format(DATEFORMAT), XSDDatatype.XSDdateTime));
        creationDate=creationDate.plusHours(2);
        n++;


        DigitalObject dummy4 = new DigitalObject(scenario, "SEVIRI Image "+n++);
        dummy4.hasPath("https://141.5.100.67/api/cdmi/eumetsatdata/SEVRIImage"+n+".jpg");
        dummy4.partOf(seviriImages);
        dummy4.addProperty(releaseState, "public");
        dummy4.addProperty(dateCreated, ResourceFactory.createTypedLiteral(creationDate.format(DATEFORMAT), XSDDatatype.XSDdateTime));

    }
}
