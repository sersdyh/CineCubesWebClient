package taskmgr;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;

public class TaskSummary extends Task {

	public TaskSummary() {
		super();
	}

	@Override
    public void generateSubTasks(CubeBase cubeBase,CubeQuery cubequery,
    		SubTask OriginSbTsk, String measure){
		this.addNewSubTask();
	}



}
