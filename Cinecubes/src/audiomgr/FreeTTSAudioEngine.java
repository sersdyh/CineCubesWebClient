package audiomgr;

import java.io.File;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

public class FreeTTSAudioEngine extends AudioEngine {

	/**
	 * @uml.property  name="voiceName"
	 */
	private String voiceName;
	/**
	 * @uml.property  name="voiceManager"
	 * @uml.associationEnd  
	 */
	private VoiceManager voiceManager;
	/**
	 * @uml.property  name="voice"
	 * @uml.associationEnd  
	 */
	private Voice voice;
	/**
	 * @uml.property  name="sfap"
	 * @uml.associationEnd  
	 */
	private SingleFileAudioPlayer sfap;

	/**
	 * @uml.property  name="lexicon"
	 */
	private final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
	/**
	 * @uml.property  name="rand"
	 */
	private final java.util.Random rand = new java.util.Random();

	public FreeTTSAudioEngine() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AudioMgr.AudioEngine#InitializeVoiceEngine()
	 */
	@Override
	public void InitializeVoiceEngine() {
		voiceName = "kevin16"; // the only usable general purpose voice

		System.setProperty("com.sun.speech.freetts.voice.defaultAudioPlayer",
				"com.sun.speech.freetts.audio.SingleFileAudioPlayer");

		voiceManager = VoiceManager.getInstance();

		voice = voiceManager.getVoice(voiceName);
		voice.allocate();
	}

	// alex: unuse code like test
	public static void listAllVoices() {
		System.out.println();
		System.out.println("All voices available:");
		VoiceManager voiceManager = VoiceManager.getInstance();
		Voice[] voices = voiceManager.getVoices();
		for (int i = 0; i < voices.length; i++) {
			System.out.println("    " + voices[i].getName() + " ("
					+ voices[i].getDomain() + " domain)");
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
			sfap = new SingleFileAudioPlayer(FileNameOfSound,
					javax.sound.sampled.AudioFileFormat.Type.WAVE);
			voice.setAudioPlayer(sfap);
			voice.speak(textTobeSound);
			sfap.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	//alex: no use get or set methods
	/**
	 * @return  the voiceName
	 * @uml.property  name="voiceName"
	 */
	public String getVoiceName() {
		return voiceName;
	}

	/**
	 * @param voiceName  the voiceName to set
	 * @uml.property  name="voiceName"
	 */
	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}

	/**
	 * @return  the voiceManager
	 * @uml.property  name="voiceManager"
	 */
	public VoiceManager getVoiceManager() {
		return voiceManager;
	}

	/**
	 * @param voiceManager  the voiceManager to set
	 * @uml.property  name="voiceManager"
	 */
	public void setVoiceManager(VoiceManager voiceManager) {
		this.voiceManager = voiceManager;
	}

	/**
	 * @return  the voice
	 * @uml.property  name="voice"
	 */
	public Voice getVoice() {
		return voice;
	}

	/**
	 * @param voice  the voice to set
	 * @uml.property  name="voice"
	 */
	public void setVoice(Voice voice) {
		this.voice = voice;
	}

	/**
	 * @return  the sfap
	 * @uml.property  name="sfap"
	 */
	public SingleFileAudioPlayer getSfap() {
		return sfap;
	}

	/**
	 * @param sfap  the sfap to set
	 * @uml.property  name="sfap"
	 */
	public void setSfap(SingleFileAudioPlayer sfap) {
		this.sfap = sfap;
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