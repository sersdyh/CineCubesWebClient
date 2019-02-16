package storymgr;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
 
public class StoryMgr {
    
    private Story story;
	private String act_story_time;
	
    public StoryMgr() {
    	act_story_time = "";
    }
       
    public void createStory(CubeQuery cubequery, boolean isAudioOn,
    		long startTime , CubeManager CubeManager, String measure) {
    	long initial_time = System.nanoTime();
		if (cubequery == null)
			cubequery = CubeManager.createDefaultCubeQuery();
		story=new Story();
    	act_story_time += "Init Time\t" + (System.nanoTime() - initial_time)
				+ "\n";
		story.createActs(cubequery,isAudioOn, CubeManager,measure);
    }
          
    public Story getStory(){
    	return story;
    }
        
    public void setStoryTime(String time){
    	act_story_time += time;
    }
    
  }
