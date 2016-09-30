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
package DomainOntology;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import entities.Template;
import models.*;
import relations.DEMRelation;
import relations.Relation;
import relations.RelationBuilder;

/**
 * This class is a wrapper around the Digital Video Artwork ontology which was developed by CERTH
 * during the PERICLES project.
 *
 * @author Auto-generated by schemagen on 01 Jul 2016 15:17
 */
public class DVAWrapper extends AbstractModel {
    /**
     * <p>The RDF model that holds the vocabulary terms</p>
     */
    private static Model m_model = ModelFactory.createDefaultModel();

    /**
     * <p>The namespace of the vocabulary as a string</p>
     */
    public static final String NS = "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#";

    /**
     * <p>The namespace of the vocabulary as a string</p>
     *
     * @see #NS
     */
    public static String getURI() {
        return NS;
    }

    public DVAWrapper() {
        super(NS, "DVA", "The Digital Video Artwork Domain Ontology");
    }

    /**
     * <p>The namespace of the vocabulary as a resource</p>
     */
    public static Resource NAMESPACE = m_model.createResource(NS);

    public static Template AccessActivity;
    public static Template AccessRightsDependency;
    public static Template AcquisitionActivity;
    /**
     * <p>E.g. NTSC, PAL, etc.The video color encoding system, if the video originates
     * from analog television broadcasts.</p>
     */
    public static Template AnalogBroadcastStandard;
    public static Template ArchivingCost;
    public static Template Artist;
    /**
     * <p>E.g. 16:9, 4:3, etc.The aspect ratio of an image describes the proportional
     * relationship between its width and its height. It is commonly expressed as
     * two numbers separated by a colon, as in 16:9.</p>
     */
    public static Template AspectRatio;
    public static Template AssessmentActivity;
    public static Template AssistantCurator;
    public static Template AudioCodec;
    public static Template AudioStream;
    /**
     * <p>The bitrate is the data rate for a video file. Video data rates are given
     * in bits per second.</p>
     */
    public static Template BitRate;
    public static Template ChromaFormat;
    public static Template Codec;
    public static Template CodecDescription;
    /**
     * <p>E.g. H.264/MPEG-4 AVCAlso known as coding format or compression format.</p>
     */
    public static Template CodingStandard;
    /**
     * <p>A color space is a specific organization of colors. It allows for reproducible
     * representations of color, in both analog and digital representations. A color
     * space may be arbitrary, with particular colors assigned to a set of physical
     * color swatches and corresponding assigned names or numbers.E.g. YUV, RGB,
     * etc.</p>
     */
    public static Template ColourSpaceType;
    /**
     * <p>E.g. lossy, lossless or uncompressedThe type of video compression.</p>
     */
    public static Template CompressionType;
    public static Template Conservator;
    public static Template Container;
    public static Template CopyActivity;
    public static Template Cost;
    public static Template CreationActivity;
    public static Template Creator;
    public static Template DestructionActivity;
    public static Template DigitalPreservationManager;
    public static Template DigitalVideo;
    public static Template DigitalVideoArt;
    public static Template DisplayActivity;
    public static Template DisplayCost;
    public static Template DisplayDevice;
    public static Template Document;
    public static Template DocumentationActivity;
    public static Template EditingSoftware;
    /**
     * <p>E.g. hard, prerendered, soft, etc.The type of embedment used to attach a subtitle
     * stream to a video.</p>
     */
    public static Template EmbeddingType;
    public static Template Equipment;
    public static Template EquipmentCost;
    public static Template EquipmentDependency;
    public static Template ErrorItem;
    public static Template ExaminationActivity;
    public static Template FileDependency;
    /**
     * <p>Frame rate, also known as frame frequency, is the frequency (rate) at which
     * an imaging device produces unique consecutive images called frames. The term
     * applies equally well to film and video cameras, computer graphics, and motion
     * capture systems. Frame rate is most often expressed in frames per second (FPS)
     * and is also expressed in progressive scan monitors as hertz (Hz).E.g. 60 FPS</p>
     */
    public static Template FrameRate;
    public static Template Group;
    public static Template HDD;
    public static Template HardwareDependency;
    public static Template LTO;
    public static Template LoanActivity;
    public static Template MaintenanceActivity;
    public static Template MediaPlayer;
    public static Template MigrationActivity;
    public static Template Monitor;
    public static Template OpticalDisc;
    public static Template Organization;
    public static Template Person;
    public static Template PlaybackActivity;
    public static Template PlaybackDependency;
    public static Template PlayerDependency;
    public static Template Projector;
    /**
     * <p>E.g. Broadcast range or full range.The type of range a video is created for.</p>
     */
    public static Template RangeType;
    public static Template Registrar;
    public static Template Representative;
    /**
     * <p>The audio sample rate is the number of samples of audio carried per second,
     * measured in Hz or kHz (one kHz being 1 000 Hz). For example, 44 100 samples
     * per second can be expressed as either 44 100 Hz, or 44.1 kHz.E.g. 96 kHzE.g.
     * 74.25 MHzThe video sample rate of a digital video format determines how often
     * the light intensity of each video line is sampled.</p>
     */
    public static Template SampleRate;
    /**
     * <p>Progressive or interlaced scan</p>
     */
    public static Template ScanType;
    /**
     * <p>'BT.709', 'BT.601', etc.Sets of standards standardize the format of a video
     * stream.</p>
     */
    public static Template SetOfStandards;
    public static Template ShootingDevice;
    public static Template Size;
    public static Template SoftwareDependency;
    public static Template StaffMember;
    public static Template StorageActivity;
    public static Template StorageDevice;
    public static Template Stream;
    public static Template SubtitleStream;
    /**
     * <p>The file format of subtitles.E.g. SubRip, SubViewer, etc.</p>
     */
    public static Template SubtitleTextFormat;
    public static Template Supplier;
    public static Template Technician;
    public static Template VideoCodec;
    /**
     * <p>E.g. 4A video codec level is a specified set of constrains that indicate
     * a degree of required decoder performance for a profile.</p>
     */
    public static Template VideoCodecLevel;
    /**
     * <p>The video codec profile is a set of capabilities and constrains apllied in
     * the encoder. It allows the decoder to recognize the requirements to decode
     * a specific stream.E.g. baseline profile, extended profile, saver profile, high
     * profile, etc.</p>
     */
    public static Template VideoCodecProfile;
    public static Template VideoDescription;
    /**
     * <p>Measurement carried out according to the video quality metric.</p>
     */
    public static Template VideoQualityMeasurement;
    /**
     * <p>Process or software used to measure the quality of a video.E.g. VQM</p>
     */
    public static Template VideoQualityMetric;
    public static Template VideoStream;
    public static Template WarningItem;

    public static DEMRelation accessesResource;
    public static DEMRelation acquiresResource;
    public static DEMRelation acquisitionActivityPerformedBy;
    public static DEMRelation acquisitionActivityPerformedByProvider;
    public static DEMRelation acquisitionActivityPerformedToReceiver;
    public static DEMRelation activityLocation;
    public static DEMRelation createsResource;
    public static DEMRelation destroysResource;
    public static DEMRelation displaysResource;
    public static DEMRelation documentsResource;
    public static DEMRelation hasAnalogBroadcastStandard;
    public static DEMRelation hasAspectRatio;
    public static DEMRelation hasAudioCodec;
    public static DEMRelation hasAudioStream;
    public static DEMRelation hasBitRate;
    public static DEMRelation hasChromaFormat;
    public static DEMRelation hasCodec;
    public static DEMRelation hasCodingStandard;
    public static DEMRelation hasColourSpaceType;
    public static DEMRelation hasCompressionType;
    public static DEMRelation hasContainer;
    public static DEMRelation hasCopyInput;
    public static DEMRelation hasCopyOutput;
    public static DEMRelation hasDate;
    public static DEMRelation hasEmbeddingType;
    public static DEMRelation hasFrameRate;
    public static DEMRelation hasFrameSize;
    public static DEMRelation hasPlaybackDuration;
    public static DEMRelation hasRangeType;
    public static DEMRelation hasSampleRate;
    public static DEMRelation hasScanType;
    public static DEMRelation hasSetOfStandards;
    public static DEMRelation hasSize;
    public static DEMRelation hasStream;
    public static DEMRelation hasSubtitleStream;
    public static DEMRelation hasSubtitleTextFormat;
    public static DEMRelation hasVideoCodec;
    public static DEMRelation hasVideoCodecLevel;
    public static DEMRelation hasVideoCodecProfile;
    public static DEMRelation hasVideoQualityMeasurement;
    public static DEMRelation hasVideoQualityMetric;
    public static DEMRelation hasVideoStream;
    public static DEMRelation hasYUVSampleRange;
    public static DEMRelation isBorrowerInLoanActivity;
    public static DEMRelation isCopyOf;
    public static DEMRelation isLenderInLoanActivity;
    public static DEMRelation isProviderInAcquisitionActivity;
    public static DEMRelation isReceiverInAcquisitionActivity;
    public static DEMRelation loanActivityPerformedBy;
    public static DEMRelation loanActivityPerformedFromAgent;
    public static DEMRelation loanActivityPerformedToAgent;
    public static DEMRelation loansResource;
    public static DEMRelation maintainsResource;
    public static DEMRelation migratesFromResource;
    public static DEMRelation migratesToResource;
    public static DEMRelation performedBy;
    public static DEMRelation performs;
    public static DEMRelation performsAcquisitionActivity;
    public static DEMRelation performsLoanActivity;
    public static DEMRelation storesResource;
    public static DEMRelation atTime;
    public static DEMRelation hasDescription;
    public static DEMRelation hasStatusFlag;
    public static DEMRelation hasUnit;
    public static DEMRelation hasValue;
    public static DEMRelation hasVersion;
    public static DEMRelation hasWarningText;
    public static DEMRelation includesAspectRatio;
    public static DEMRelation includesMetadata;
    public static DEMRelation value;

    @Override
    public void createModelEntities() {
        AccessActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AccessActivity"), ProcessModel.humanActivity);
        AccessRightsDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AccessRightsDependency"), AnalysisModel.ecosystemDependency);
        AcquisitionActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AcquisitionActivity"), ProcessModel.humanActivity);
        AnalogBroadcastStandard = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AnalogBroadcastStandard"), CoreModel.policy);
        ArchivingCost = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ArchivingCost"), AnalysisModel.weightedRelation);
        Artist = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Artist"), ProcessModel.humanAgent);
        AspectRatio = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AspectRatio"), CoreModel.ecosystemEntity);
        AssessmentActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AssessmentActivity"), ProcessModel.humanActivity);
        AssistantCurator = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AssistantCurator"), ProcessModel.humanAgent);
        AudioCodec = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AudioCodec"), InfrastructureModel.softwareAgent);
        AudioStream = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#AudioStream"), CoreModel.digitalObject);
        BitRate = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#BitRate"), CoreModel.ecosystemEntity);
        ChromaFormat = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ChromaFormat"), CoreModel.ecosystemEntity);
        Codec = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Codec"), CoreModel.digitalObject);
        CodecDescription = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#CodecDescription"), CoreModel.ecosystemEntity);
        CodingStandard = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#CodingStandard"), CoreModel.policy);
        ColourSpaceType = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ColourSpaceType"), CoreModel.ecosystemEntity);
        CompressionType = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#CompressionType"), CoreModel.ecosystemEntity);
        Conservator = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Conservator"), ProcessModel.humanAgent);
        Container = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Container"), CoreModel.digitalObject);
        CopyActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#CopyActivity"), ProcessModel.humanActivity);
        Cost = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Cost"), AnalysisModel.weightedRelation);
        CreationActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#CreationActivity"), ProcessModel.humanActivity);
        Creator = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Creator"), ProcessModel.humanAgent);
        DestructionActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DestructionActivity"), ProcessModel.humanActivity);
        DigitalPreservationManager = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DigitalPreservationManager"), ProcessModel.humanAgent);
        DigitalVideo = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DigitalVideo"), CoreModel.digitalObject);
        DigitalVideoArt = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DigitalVideoArt"), CoreModel.digitalObject);
        DisplayActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DisplayActivity"), ProcessModel.humanActivity);
        DisplayCost = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DisplayCost"), AnalysisModel.weightedRelation);
        DisplayDevice = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DisplayDevice"), InfrastructureModel.hardwareAgent);
        Document = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Document"), CoreModel.digitalObject);
        DocumentationActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#DocumentationActivity"), ProcessModel.humanActivity);
        EditingSoftware = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#EditingSoftware"), InfrastructureModel.softwareAgent);
        EmbeddingType = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#EmbeddingType"), CoreModel.ecosystemEntity);
        Equipment = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Equipment"), InfrastructureModel.infrastructureComponent);
        EquipmentCost = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#EquipmentCost"), AnalysisModel.weightedRelation);
        EquipmentDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#EquipmentDependency"), CoreModel.ecosystemEntity);
        ErrorItem = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ErrorItem"), AnalysisModel.error);
        ExaminationActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ExaminationActivity"), ProcessModel.humanActivity);
        FileDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#FileDependency"), AnalysisModel.ecosystemDependency);
        FrameRate = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#FrameRate"), CoreModel.ecosystemEntity);
        Group = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Group"), CoreModel.ecosystemEntity);
        HDD = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#HDD"), InfrastructureModel.infrastructureComponent);
        HardwareDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#HardwareDependency"), AnalysisModel.ecosystemDependency);
        LTO = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#LTO"), CoreModel.ecosystemEntity);
        LoanActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#LoanActivity"), ProcessModel.humanActivity);
        MaintenanceActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#MaintenanceActivity"), ProcessModel.humanActivity);
        MediaPlayer = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#MediaPlayer"), InfrastructureModel.softwareAgent);
        MigrationActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#MigrationActivity"), ProcessModel.humanActivity);
        Monitor = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Monitor"), CoreModel.ecosystemEntity);
        OpticalDisc = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#OpticalDisc"), CoreModel.ecosystemEntity);
        Organization = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Organization"), CoreModel.community);
        Person = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Person"), ProcessModel.humanAgent);
        PlaybackActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#PlaybackActivity"), ProcessModel.humanActivity);
        PlaybackDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#PlaybackDependency"), AnalysisModel.ecosystemDependency);
        PlayerDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#PlayerDependency"), AnalysisModel.ecosystemDependency);
        Projector = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Projector"), InfrastructureModel.hardwareAgent);
        RangeType = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#RangeType"), CoreModel.ecosystemEntity);
        Registrar = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Registrar"), ProcessModel.humanAgent);
        Representative = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Representative"), ProcessModel.humanAgent);
        SampleRate = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#SampleRate"), CoreModel.ecosystemEntity);
        ScanType = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ScanType"), CoreModel.ecosystemEntity);
        SetOfStandards = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#SetOfStandards"), PolicyModel.aggregatedPolicy);
        ShootingDevice = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#ShootingDevice"), InfrastructureModel.hardwareAgent);
        Size = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Size"), CoreModel.ecosystemEntity);
        SoftwareDependency = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#SoftwareDependency"), AnalysisModel.ecosystemDependency);
        StaffMember = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#StaffMember"), ProcessModel.humanAgent);
        StorageActivity = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#StorageActivity"), ProcessModel.humanActivity);
        StorageDevice = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#StorageDevice"), InfrastructureModel.infrastructureComponent);
        Stream = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Stream"), CoreModel.digitalObject);
        SubtitleStream = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#SubtitleStream"), CoreModel.digitalObject);
        SubtitleTextFormat = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#SubtitleTextFormat"), CoreModel.ecosystemEntity);
        Supplier = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Supplier"), ProcessModel.humanAgent);
        Technician = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#Technician"), ProcessModel.humanAgent);
        VideoCodec = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoCodec"), InfrastructureModel.softwareAgent);
        VideoCodecLevel = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoCodecLevel"), CoreModel.ecosystemEntity);
        VideoCodecProfile = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoCodecProfile"), CoreModel.ecosystemEntity);
        VideoDescription = new Template(this, m_model.createResource("https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoDescription"), CoreModel.ecosystemEntity);
        VideoQualityMeasurement = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoQualityMeasurement"), PolicyModel.qualityAssuranceMethod);
        VideoQualityMetric = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoQualityMetric"), PolicyModel.qualityAssurance);
        VideoStream = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#VideoStream"), CoreModel.digitalObject);
        WarningItem = new Template(this, m_model.createResource(
                "https://dl.dropboxusercontent.com/u/27469926/dva_t.owl#WarningItem"), AnalysisModel.warning);
    }

    @Override
    public void createModelRelations() {
        accessesResource = new RelationBuilder(this, "accessesResource").create();
        acquiresResource = new RelationBuilder(this, "acquiresResource").create();
        acquisitionActivityPerformedBy = new RelationBuilder(this, "acquisitionActivityPerformedBy").create();
        acquisitionActivityPerformedByProvider = new RelationBuilder(this, "acquisitionActivityPerformedByProvider").create();
        acquisitionActivityPerformedToReceiver = new RelationBuilder(this, "acquisitionActivityPerformedToReceiver").create();
        activityLocation = new RelationBuilder(this, "activityLocation").create();
        createsResource = new RelationBuilder(this, "createsResource").create();
        destroysResource = new RelationBuilder(this, "destroysResource").create();
        displaysResource = new RelationBuilder(this, "displaysResource").create();
        documentsResource = new RelationBuilder(this, "documentsResource").create();
        hasAnalogBroadcastStandard = new RelationBuilder(this, "hasAnalogBroadcastStandard").create();
        hasAspectRatio = new RelationBuilder(this, "hasAspectRatio").create();
        hasAudioCodec = new RelationBuilder(this, "hasAudioCodec").create();
        hasAudioStream = new RelationBuilder(this, "hasAudioStream").create();
        hasBitRate = new RelationBuilder(this, "hasBitRate").create();
        hasChromaFormat = new RelationBuilder(this, "hasChromaFormat").create();
        hasCodec = new RelationBuilder(this, "hasCodec").create();
        hasCodingStandard = new RelationBuilder(this, "hasCodingStandard").create();
        hasColourSpaceType = new RelationBuilder(this, "hasColourSpaceType").create();
        hasCompressionType = new RelationBuilder(this, "hasCompressionType").create();
        hasContainer = new RelationBuilder(this, "hasContainer").create();
        hasCopyInput = new RelationBuilder(this, "hasCopyInput").create();
        hasCopyOutput = new RelationBuilder(this, "hasCopyOutput").create();
        hasDate = new RelationBuilder(this, "hasDate").create();
        hasEmbeddingType = new RelationBuilder(this, "hasEmbeddingType").create();
        hasFrameRate = new RelationBuilder(this, "hasFrameRate").create();
        hasFrameSize = new RelationBuilder(this, "hasFrameSize").create();
        hasPlaybackDuration = new RelationBuilder(this, "hasPlaybackDuration").create();
        hasRangeType = new RelationBuilder(this, "hasRangeType").create();
        hasSampleRate = new RelationBuilder(this, "hasSampleRate").create();
        hasScanType = new RelationBuilder(this, "hasScanType").create();
        hasSetOfStandards = new RelationBuilder(this, "hasSetOfStandards").create();
        hasSize = new RelationBuilder(this, "hasSize").create();
        hasStream = new RelationBuilder(this, "hasStream").create();
        hasSubtitleStream = new RelationBuilder(this, "hasSubtitleStream").create();
        hasSubtitleTextFormat = new RelationBuilder(this, "hasSubtitleTextFormat").create();
        hasVideoCodec = new RelationBuilder(this, "hasVideoCodec").create();
        hasVideoCodecLevel = new RelationBuilder(this, "hasVideoCodecLevel").create();
        hasVideoCodecProfile = new RelationBuilder(this, "hasVideoCodecProfile").create();
        hasVideoQualityMeasurement = new RelationBuilder(this, "hasVideoQualityMeasurement").create();
        hasVideoQualityMetric = new RelationBuilder(this, "hasVideoQualityMetric").create();
        hasVideoStream = new RelationBuilder(this, "hasVideoStream").create();
        hasYUVSampleRange = new RelationBuilder(this, "hasYUVSampleRange").create();
        isBorrowerInLoanActivity = new RelationBuilder(this, "isBorrowerInLoanActivity").create();
        isCopyOf = new RelationBuilder(this, "isCopyOf").create();
        isLenderInLoanActivity = new RelationBuilder(this, "isLenderInLoanActivity").create();
        isProviderInAcquisitionActivity = new RelationBuilder(this, "isProviderInAcquisitionActivity").create();
        isReceiverInAcquisitionActivity = new RelationBuilder(this, "isReceiverInAcquisitionActivity").create();
        loanActivityPerformedBy = new RelationBuilder(this, "loanActivityPerformedBy").create();
        loanActivityPerformedFromAgent = new RelationBuilder(this, "loanActivityPerformedFromAgent").create();
        loanActivityPerformedToAgent = new RelationBuilder(this, "loanActivityPerformedToAgent").create();
        loansResource = new RelationBuilder(this, "loansResource").create();
        maintainsResource = new RelationBuilder(this, "maintainsResource").create();
        migratesFromResource = new RelationBuilder(this, "migratesFromResource").create();
        migratesToResource = new RelationBuilder(this, "migratesToResource").create();
        performedBy = new RelationBuilder(this, "performedBy").create();
        performs = new RelationBuilder(this, "performs").create();
        performsAcquisitionActivity = new RelationBuilder(this, "performsAcquisitionActivity").create();
        performsLoanActivity = new RelationBuilder(this, "performsLoanActivity").create();
        storesResource = new RelationBuilder(this, "storesResource").create();
        atTime = new RelationBuilder(this, "atTime").create();
        hasDescription = new RelationBuilder(this, "hasDescription").create();
        hasStatusFlag = new RelationBuilder(this, "hasStatusFlag").create();
        hasUnit = new RelationBuilder(this, "hasUnit").create();
        hasValue = new RelationBuilder(this, "hasValue").create();
        hasVersion = new RelationBuilder(this, "hasVersion").create();
        hasWarningText = new RelationBuilder(this, "hasWarningText").create();
        includesAspectRatio = new RelationBuilder(this, "includesAspectRatio").create();
        includesMetadata = new RelationBuilder(this, "includesMetadata").create();
        value = new RelationBuilder(this, "value").create();

        for (Relation relation : this.relations) {
            if (relation instanceof DEMRelation) {
                for (Template template : this.templates) {
                    ((DEMRelation) relation).addDomain(template);
                    ((DEMRelation) relation).addRange(template);
                }
            }
        }
    }
}
