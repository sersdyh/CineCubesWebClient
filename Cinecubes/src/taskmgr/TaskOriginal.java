package taskmgr;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import exctractionmethod.ExtractionMethod;

public class TaskOriginal extends Task {

	public TaskOriginal() {
		super();
	}

	@Override
    public void generateSubTasks(CubeBase cubeBase,CubeQuery cubequery,
    		SubTask OriginSbTsk, String measure){
    	addNewSubTask();
        getLastSubTask().createSubTask(cubeQuery.get(0),cubeBase);   
	}

}
