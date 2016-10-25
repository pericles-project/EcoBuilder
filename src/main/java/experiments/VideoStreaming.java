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

import LRMv2.LRM_static_schema;
import entities.*;
import entities.Process;
import models.CoreModel;
import models.InfrastructureModel;
import models.ScenarioModel;
import relations.DEMRelation;
import relations.RelationBuilder;

/**
 * This example from the media domain illustrates how to create own customised templates to fit for
 * specific scenario needs.
 */
public class VideoStreaming extends Experiment {
    /* Abstract resources */
    private Codec codec;
    private ComputerNetwork computerNetwork;
    private NetworkPlayer networkPlayer;
    private VideoStream videoStream;
    private VideoSource videoSource;
    private VideoSteamUserCommunity baseUserCommunity;
    /* Abstract relations */
    private StreamingServer streamingServer;
    private DEMRelation hasResolution;
    private DEMRelation hasFramerate;
    private DEMRelation hasVideoDatarate;
    private DEMRelation hasMaxVideoDatarate;
    private DEMRelation supportsCodec;
    private DEMRelation hasCodec;
    private DEMRelation hasFilename;
    private DEMRelation isKindOfNetwork;
    private DEMRelation hasNetworkBandwidth;
    private DEMRelation hasNetworkBandwidthMeasured;
    /* Concrete resources */
    private EcosystemEntity H264Level3baseline;
    private EcosystemEntity H264level41high;
    private EcosystemEntity H264level42high10;
    private EcosystemEntity MPEG2MP_ML_PAL;
    private EcosystemEntity MPEG2MP_ML_NTSC;
    private EcosystemEntity MPEG2MP_HL;
    private EcosystemEntity myStreamingServer;
    private EcosystemEntity myNetworkPlayer;
    private EcosystemEntity videoStreamSample;
    private EcosystemEntity fiberglasNetwork;
    private EcosystemEntity wifiNetwork;
    private EcosystemEntity videoSourceSample;
    /* Concrete scenario templates */
    private Process playbackProcess;
    private EcosystemEntity smoothPolicy;
    private EcosystemEntity qualityPlaybackPolicy;
    private EcosystemEntity artistUserCommunity;
    private EcosystemEntity curatorUserCommunity;
    private EcosystemEntity conservatorUserCommunity;
    private EcosystemEntity generalPublicUserCommunity;

    public VideoStreaming() {
        super("Video-streaming-example");
        createAbstractResources();
        createAbstractProperties();
        createConcreteResources();
        createConcreteEcosystemEntities();
        createDependencies();
    }

    private void createAbstractResources() {
        baseUserCommunity = new VideoSteamUserCommunity(scenario);
        codec = new Codec(scenario);
        streamingServer = new StreamingServer(scenario);
        computerNetwork = new ComputerNetwork(scenario);
        networkPlayer = new NetworkPlayer(scenario);
        videoStream = new VideoStream(scenario);
        videoSource = new VideoSource(scenario);
        baseUserCommunity = new VideoSteamUserCommunity(scenario);
    }

    private void createAbstractProperties() {
        hasResolution = new RelationBuilder(scenario, "hasResolution", codec).create();
        hasFramerate = new RelationBuilder(scenario, "hasFramerate", codec).create();
        hasVideoDatarate = new RelationBuilder(scenario, "hasVideoDatarate", codec).create();
        hasMaxVideoDatarate = new RelationBuilder(scenario,
                "hasMaxVideoDatarate", codec).create();
        supportsCodec = new RelationBuilder(scenario, "supportsCodec", codec).create();
        hasCodec = new RelationBuilder(scenario, "hasCodec", codec).create();
        hasFilename = new RelationBuilder(scenario, "hasFilename", CoreModel.digitalObject).create();
        isKindOfNetwork = new RelationBuilder(scenario, "isKindOfNetwork", CoreModel.ecosystemEntity).create();
        hasNetworkBandwidth = new RelationBuilder(scenario,
                "hasNetworkBandwidth", CoreModel.ecosystemEntity).create();
        hasNetworkBandwidthMeasured = new RelationBuilder(scenario,
                "hasNetworkBandwidthMeasured", CoreModel.ecosystemEntity).create();
    }

    private void createConcreteResources() {
        H264Level3baseline = codec.createConcreteCodec("H264Level3baseline",
                "720x576p", 25, 10);
        H264level41high = codec.createConcreteCodec("H264level41high",
                "1920x1080p", 30, 62.5);
        H264level42high10 = codec.createConcreteCodec("H264level42high10",
                "1920x1080p", 60, 150);
        MPEG2MP_ML_PAL = codec.createConcreteCodec("MPEG2MP_ML_PAL",
                "720x576i", 25, 15);
        MPEG2MP_ML_NTSC = codec.createConcreteCodec("MPEG2MP_ML_NTSC",
                "720x480i", 30, 15);
        MPEG2MP_HL = codec.createConcreteCodec("MPEG2MP_HL", "1920x1080p",
                30, 80);
        myStreamingServer = streamingServer
                .createConcreteIndividual("myStreamingServer");
        myStreamingServer.addProperty(supportsCodec, H264Level3baseline);
        myStreamingServer.addProperty(supportsCodec, H264level41high);
        myStreamingServer.addProperty(supportsCodec, H264level42high10);
        myStreamingServer.addProperty(supportsCodec, MPEG2MP_ML_PAL);
        myStreamingServer.addProperty(supportsCodec, MPEG2MP_ML_NTSC);
        myStreamingServer.addProperty(supportsCodec, MPEG2MP_HL);
        myNetworkPlayer = networkPlayer
                .createConcreteIndividual("myNetworkPlayer");
        myNetworkPlayer.addProperty(supportsCodec, H264Level3baseline);
        myNetworkPlayer.addProperty(supportsCodec, H264level41high);
        myNetworkPlayer.addProperty(supportsCodec, MPEG2MP_ML_PAL);
        videoStreamSample = videoStream
                .createConcreteIndividual("VideoStreamSample");
        videoStreamSample.addProperty(hasCodec, H264level41high);
        videoStreamSample.addProperty(hasMaxVideoDatarate, "45 Mbit/s");

        fiberglasNetwork = computerNetwork.createConcreteResource(
                "FiberglasNetwork", "Fiberglas", "10 Gbit/s", "9.7 Gbit/s");
        wifiNetwork = computerNetwork.createConcreteResource(
                "PhysicalConnectionWifi", "802.11n WiFi", "300 Mbit/s",
                "90 Mbit/s");
        wifiNetwork
                .addComment("Physical connection of the network streaming client to the computer network.");
        videoSourceSample = videoSource
                .createConcreteIndividual("VideoSourceSample");
        videoSourceSample.addProperty(hasCodec, H264level41high);
        videoSourceSample.addLiteral(hasFilename, "sample.mkv");
    }

    private void createConcreteEcosystemEntities() {
        playbackProcess = new Process(scenario, "VideoStreamPlaybackProcess");
        playbackProcess
                .addComment("The process chain from reading the video source until the rendering of the video stream.");
        smoothPolicy = new Policy(scenario, "SmoothPlaybackOfVideoStream");
        smoothPolicy
                .addComment("The video must be able to play without stutter.");
        qualityPlaybackPolicy = new Policy(scenario, "AcceptableQualityOfPlayback");
        qualityPlaybackPolicy
                .addComment("The quality of the playback must meet a certain standard, e.g. from the artist.");
        artistUserCommunity = baseUserCommunity
                .createConcreteIndividual("Artist");
        artistUserCommunity
                .addComment("The one who is responsible for the content and often pretend the viewing setup.");
        curatorUserCommunity = baseUserCommunity
                .createConcreteIndividual("Curator");
        curatorUserCommunity
                .addComment("Group who is responsible to setup and arrange an exhibition.");
        conservatorUserCommunity = baseUserCommunity
                .createConcreteIndividual("Conservator");
        conservatorUserCommunity
                .addComment("Group who is responsible for making decisions on altering the artwork for sustaining the artwork.");
        generalPublicUserCommunity = baseUserCommunity
                .createConcreteIndividual("GeneralPublic");
        generalPublicUserCommunity
                .addComment("The target audience for the artwork which visits the exhibition.");
    }

    private void createDependencies() {
        new EcosystemDependency(scenario, "DepVideoStreamingServer2VideoSource", myStreamingServer, videoSourceSample);
        new EcosystemDependency(scenario, "DepVideoStream2Network", videoStreamSample, fiberglasNetwork);
//        new DependencyRuleBased(scenario, "DepNetworkPlayer2NetworkConnection", myNetworkPlayer, wifiNetwork);
        new EcosystemDependency(scenario, "DepNetworkPlayer2VideoStream", videoStreamSample, myNetworkPlayer);
        new EcosystemDependency(scenario, "DepPlaybackprocess2Smoothpolicy", playbackProcess, smoothPolicy);
        new EcosystemDependency(scenario, "DepPlaybackprocess2Qualityplaybackpolicy", playbackProcess, qualityPlaybackPolicy);
//        All user communities related to video streaming are dependent on the playback process.
        new EcosystemDependency(scenario, "DepUC2Playbackprocess", baseUserCommunity, playbackProcess);
    }

    private class Codec extends Template {
        public Codec(ScenarioModel scenario) {
            super(scenario, "Codec", InfrastructureModel.softwareAgent);
            addDescription("Base class for video codecs");
        }

        public EcosystemEntity createConcreteCodec(String identifier, String resolution, int framerate, double datarate) {
            EcosystemEntity codec = createConcreteIndividual(identifier);
            codec.addLiteral(hasResolution, resolution);
            codec.addLiteral(hasFramerate, framerate);
            codec.addLiteral(hasVideoDatarate, datarate);
            return codec;
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            return createEntity(scenario, identifier);
        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model,
                    ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }


    private class StreamingServer extends Template {
        public StreamingServer(ScenarioModel scenario) {
            super(scenario, "StreamingServer", InfrastructureModel.softwareAgent);
            addDescription("The network streaming server software component");
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            EcosystemEntity resource = new EcosystemEntity(scenario,
                    identifier, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;

        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model, ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }

    private class NetworkPlayer extends Template {
        public NetworkPlayer(ScenarioModel scenario) {
            super(scenario, "NetworkPlayer", InfrastructureModel.softwareAgent);
            addDescription("The network player for a streaming server");
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            EcosystemEntity resource = new EcosystemEntity(scenario,
                    identifier, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model, ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }

    private class ComputerNetwork extends Template {
        public ComputerNetwork(ScenarioModel scenario) {
            super(scenario, "ComputerNetwork", CoreModel.technicalService);
            addDescription("The computer network backbone in general");
        }

        public EcosystemEntity createConcreteResource(String identifier,
                                                      String kind, String bandwidth, String measured) {
            EcosystemEntity concreteIndividual = createConcreteIndividual(identifier);
            concreteIndividual.addLiteral(isKindOfNetwork, kind);
            concreteIndividual.addLiteral(hasNetworkBandwidth, bandwidth);
            concreteIndividual
                    .addLiteral(hasNetworkBandwidthMeasured, measured);
            return concreteIndividual;
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            EcosystemEntity resource = new EcosystemEntity(scenario,
                    identifier, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model, ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }

    private class VideoStream extends Template {
        public VideoStream(ScenarioModel scenario) {
            super(scenario, "VideoStream", CoreModel.digitalObject);
            addDescription("the video stream which is streamed to the network player");
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            EcosystemEntity resource = new EcosystemEntity(scenario,
                    identifier, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model, ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }

    private class VideoSource extends Template {
        public VideoSource(ScenarioModel scenario) {
            super(scenario, "VideoSource", CoreModel.digitalObject);
            addDescription("The source for the video stream");
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            EcosystemEntity resource = new EcosystemEntity(scenario,
                    identifier, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model, ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }

    private class VideoSteamUserCommunity extends Template {
        public VideoSteamUserCommunity(ScenarioModel scenario) {
            super(scenario, "VideoStreamUserCommunity", CoreModel.community);
            addDescription("Abstract base user community for all user communities which are of relevance for video streaming.");
        }

        public EcosystemEntity createConcreteIndividual(String identifier) {
            EcosystemEntity resource = new EcosystemEntity(scenario, identifier, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }

        @Override
        public EcosystemEntity createEntity(ScenarioModel model, String ID) {
            EcosystemEntity resource = new EcosystemEntity(model, ID, this);
            resource.addOntClass(LRM_static_schema.ConcreteResource);
            resource.addProperty(LRM_static_schema.realizes, this);
            return resource;
        }
    }
}
