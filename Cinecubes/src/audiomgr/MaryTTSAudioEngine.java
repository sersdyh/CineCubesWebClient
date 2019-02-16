package audiomgr;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;

public class MaryTTSAudioEngine extends AudioEngine {

	/**
	 * @uml.property  name="lexicon"
	 */
	private final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
	/**
	 * @uml.property  name="rand"
	 */
	private final java.util.Random rand = new java.util.Random();
	/**
	 * @uml.property  name="mTTS"
	 * @uml.associationEnd  
	 */
	private MaryInterface MTTS;

	public MaryTTSAudioEngine() {
		super();
	}

	@Override
	public void InitializeVoiceEngine() {
		try {
			MTTS = new LocalMaryInterface();
			MTTS.setVoice("cmu-slt-hsmm");
		} catch (MaryConfigurationException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AudioMgr.AudioEngine#CreateSound(java.lang.String, java.lang.String)
	 */
	@Override
	public void CreateAudio(String textTobeSound, String FileNameOfSound) {
		try {

			File output = new File(FileNameOfSound + ".wav");
			MTTS.setOutputType("AUDIO");
			MTTS.setOutputTypeParams("WAVE");
			MTTS.setStreamingAudio(true);

			AudioInputStream audio = MTTS.generateAudio(textTobeSound);
			AudioSystem.write(audio, AudioFileFormat.Type.WAVE, output);
		} catch (Exception e) {

		}
	}

	public String randomIdentifier() {
		StringBuilder builder = new StringBuilder();
		while (builder.toString().length() == 0) {
			int length = rand.nextInt(5) + 5;
			for (int i = 0; i < length; i++)
				builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
			if ((new File("audio/" + builder.toString() + ".wav")).exists())
				builder = new StringBuilder();
		}
		return builder.toString();
	}

}
