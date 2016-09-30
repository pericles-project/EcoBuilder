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

import entities.*;
import entities.Process;

/**
 * This is an example from the Digital Video Artwork domain. It was created before we integrated the DVA ontology
 * into the EcoBuilder. Therefore this example has to be updated to use the DVA ontology!
 */
public class DigitalVideoArtworkExample extends Experiment {
    public DigitalVideoArtworkExample() {
        super("Digital-video-artwork-example");
        Scenario narrative1 = new Scenario(scenario, "Narrative 1");
        narrative1.describedBy("Detect Inconsistency in Container's Metadata (no Aspect Ratio Information)");

        Purpose scenarioPurpose1 = new Purpose(scenario, "Scenario Purpose 1");
        scenarioPurpose1.describedBy("Detect whether a container’s metadata carry the aspect ratio information " +
                "(i.e. 4:3, 16:9, 21:9) of a digital video, which is necessary for the consistent playback of " +
                "video files. It is possible that some types of containers (e.g. AVI) do not include information " +
                "on the aspect ratio value of the digital video, even though this information may be known by the " +
                "(human) creators of the files.");
        scenarioPurpose1.purposeOf(narrative1);

        SoftwareAgent mediaPlayer = new SoftwareAgent(scenario, "Media Player");
        DigitalObject aspectRatio = new DigitalObject(scenario, "Aspect Ratio");

        DigitalObject aviContainer = new DigitalObject(scenario, "AVI Container");
        DigitalObject video1 = new DigitalObject(scenario, "Digital Video 1");
        video1.partOf(aviContainer);

        DigitalObject matroskaContainer = new DigitalObject(scenario, "Matroska Container");
        DigitalObject video2 = new DigitalObject(scenario, "Digital Video 2");
        video2.partOf(matroskaContainer);

        Policy artistIntentPolicy = new Policy(scenario, "Artist Rob Smith intent for video he produced");
        HumanAgent bob = new HumanAgent(scenario, "BobTheCurator");
        bob.hasRole(new Role(scenario, "Curator"));
        HumanAgent rob = new HumanAgent(scenario, "RobTheArtist");
        rob.hasRole(new Role(scenario, "Artist"));

        artistIntentPolicy.addStatement("All videos in my artworks to be played back as initially intended", "non-formal", "en-uk");

        artistIntentPolicy.responsiblePerson(bob);
        artistIntentPolicy.constraints(video1);
        artistIntentPolicy.constraints(video2);
        artistIntentPolicy.classification("Access");

        QualityAssurance fileQA = new QualityAssurance(scenario, "onFileChange");
        fileQA.verifies(video1);
        fileQA.verifies(video2);


        fileQA.describedBy("This QA criterion will validate that the file formatis compatible with the videos by " +
                "calling the validation rules defined in narrative 1. The model is still not complete");
        QualityAssurance playerQA = new QualityAssurance(scenario, "onVideoPlayerChange");
        playerQA.verifies(video1);
        playerQA.verifies(video2);
        playerQA.verifies(mediaPlayer);
        playerQA.describedBy("This QA criterion will validate that the vidoe player is compatible with the videos by " +
                "calling the validation rules defined in narrative 2. The model is still not complete");

        artistIntentPolicy.hasMandator(rob);
        artistIntentPolicy.hasRequirementsLevel(RequirementLevel.ReqLevel.MUST);
        artistIntentPolicy.addQACriterion(fileQA);
        artistIntentPolicy.addQACriterion(playerQA);
        artistIntentPolicy.describedBy("The artist Rob Smith expressed his desire on 1.1.2011.");


        HumanAgent user = new HumanAgent(scenario, "User");
        QualityAssurance aspectRatioConsistency = new QualityAssurance(scenario, "Aspect Ratio Consistency");

        Process playback1 = new Process(scenario, "Playback 1");
        playback1.hasInput(mediaPlayer);
        playback1.hasInput(video1);

        Process playback2 = new Process(scenario, "Playback 2");
        playback2.hasInput(mediaPlayer);
        playback2.hasInput(video2);

        aspectRatioConsistency.verifies(playback1);
        aspectRatioConsistency.verifies(playback2);
        aspectRatioConsistency.verifies(aspectRatio);
        aspectRatioConsistency.verifies(mediaPlayer);
        aspectRatioConsistency.verifies(video1);
        aspectRatioConsistency.verifies(video2);

        SoftwareAgent spinValidator = new SoftwareAgent(scenario, "SPIN Validator");
        TechnicalService server = new TechnicalService(scenario, "Server");
        server.hasPart(spinValidator);
        ServiceInterface userInterface = new ServiceInterface(scenario, "SPIN User Interface");
        userInterface.providesAccessTo(spinValidator);
        userInterface.isUsedBy(user);

        DigitalObject model = new DigitalObject(scenario, "Model");
        Process modelSubmission = new Process(scenario, "Model Sumbission");
        modelSubmission.describedBy("User submits model to SPIN Validator.");
        modelSubmission.hasInput(model);
        modelSubmission.hasInput(spinValidator);
        user.execute(modelSubmission);

        AggregatedProcess validationProcess = new AggregatedProcess(scenario, "Validation Process");
        Process validation = new Process(scenario, "Validation");
        Process userResponse = new Process(scenario, "User Response");
        userResponse.describedBy("Validator responds to User with the classifications for the video.");
        validationProcess.hasSubProcess(validation);
        validationProcess.hasSubProcess(userResponse);

        DigitalObject response = new DigitalObject(scenario, "Response");
        DigitalObject explanatoryMessages = new DigitalObject(scenario, "Explanatory Messages");
        DigitalObject classification = new DigitalObject(scenario, "Classification");
        explanatoryMessages.partOf(response);
        classification.partOf(response);

        Annotation warning = new Annotation(scenario, "Warning");
        warning.describedBy("Player cannot detect the actual aspect ratio of the digital video and it will apply a default value instead.");
        warning.annotates(video1);
        Annotation success = new Annotation(scenario, "Valid");
        warning.annotates(video2);

        validationProcess.hasInput(video1);
        validationProcess.hasInput(video2);
        validationProcess.hasOutput(response);
        validationProcess.hasOutput(warning);
        validationProcess.hasOutput(success);

        spinValidator.execute(validationProcess);

        AggregatedProcess userReaction = new AggregatedProcess(scenario, "User Reaction");
        Process readMessages = new Process(scenario, "Read Messages");
        readMessages.hasInput(explanatoryMessages);
        userReaction.hasSubProcess(readMessages);
        Process problemIdentification = new Process(scenario, "Problem Identification");
        problemIdentification.hasInput(video1);
        userReaction.hasSubProcess(problemIdentification);

        Process requestAR = new Process(scenario, "Request Aspect Ration");
        requestAR.describedBy("Requests the missing aspect ratio information from repository");

        TechnicalService TMS = new TechnicalService(scenario, "TMS");
        TMS.describedBy("Repository");

        requestAR.hasInput(TMS);
        requestAR.hasOutput(aspectRatio);
        userReaction.hasSubProcess(requestAR);

        Process setAR = new Process(scenario, "Set Aspect Ratio");
        setAR.hasInput(aspectRatio);
        setAR.hasInput(mediaPlayer);
        setAR.executedBy(user);

        user.execute(userReaction);

        /*
        Narrative 1 is the scenario and therefore a view on a sub set of entities of a hugh model. This can be used to
        distinguish between different scenario views, e.g. for introducing narrative 2 to the same model.
         */
        narrative1.hasPart(user);
        narrative1.hasPart(mediaPlayer);
        narrative1.hasPart(aspectRatio);
        narrative1.hasPart(video1);
        narrative1.hasPart(video2);
        narrative1.hasPart(aviContainer);
        narrative1.hasPart(matroskaContainer);
        narrative1.hasPart(aspectRatioConsistency);
        narrative1.hasPart(playback1);
        narrative1.hasPart(playback2);
        narrative1.hasPart(spinValidator);
        narrative1.hasPart(server);
        narrative1.hasPart(userInterface);
        narrative1.hasPart(model);
        narrative1.hasPart(modelSubmission);
        narrative1.hasPart(validationProcess);
        narrative1.hasPart(validation);
        narrative1.hasPart(userResponse);
        narrative1.hasPart(response);
        narrative1.hasPart(explanatoryMessages);
        narrative1.hasPart(classification);
        narrative1.hasPart(warning);
        narrative1.hasPart(success);
        narrative1.hasPart(userReaction);
        narrative1.hasPart(readMessages);
        narrative1.hasPart(problemIdentification);
        narrative1.hasPart(requestAR);
        narrative1.hasPart(TMS);
        narrative1.hasPart(setAR);
    }
}
