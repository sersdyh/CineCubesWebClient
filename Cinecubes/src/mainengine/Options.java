package mainengine;

public class Options {
	private boolean Audio;
	private boolean Word;
	
	public Options(){
		Audio = true;
		Word = true;
	}
	
	public void setWord(boolean wo){
		Word = wo;
	}
	
	public void setAudio(boolean au){
		Audio = au;
	}
	
	public boolean getWord(){
		return Word;
	}
	
	public boolean getAudio(){
		return Audio;
	}
}
