package taskmgr;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;

public class TaskIntro extends Task {

	public TaskIntro() {
		super();
	}

	@Override
    public void generateSubTasks(CubeBase cubeBase,CubeQuery cubequery,
    		SubTask OriginSbTsk, String measure){
    	addNewSubTask();
	}
	

}
