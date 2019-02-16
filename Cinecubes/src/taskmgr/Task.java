package taskmgr;

import java.util.ArrayList;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;

/**
 * @author  Asterix
 */
public abstract class Task {
    
    protected ArrayList<SubTask> subTasks;
    protected ArrayList<CubeQuery> cubeQuery;
     
    public Task(){
    	subTasks=new ArrayList<SubTask>();
    	cubeQuery=new ArrayList<CubeQuery>();
    }
   
    public void addNewSubTask(){
    	subTasks.add(new SubTask());
    };
	
    public ArrayList<SubTask> getSubTaskList(){
    	return subTasks;
    }
    
    public ArrayList<CubeQuery> getCubeQueriesList(){
    	return cubeQuery;
    }
    
	public int getNumSubTasks(){
    	return subTasks.size();
    }
    
    public SubTask getSubTask(int i){
    	return subTasks.get(i);
    }
    
    public SubTask getLastSubTask(){
    	return getSubTask(getNumSubTasks()-1);
    }
    
    public ArrayList<SubTask> getSubTasks() {
		return subTasks;
	}
	
	public void setSubTasks(ArrayList<SubTask> arrayList) {
		this.subTasks = arrayList;
	};
    
	public void addCubeQuery(CubeQuery newCubeQuery){
		cubeQuery.add(newCubeQuery);
	}
	
	public CubeQuery getCubeQuery(int id){
		return cubeQuery.get(id);
	}
	
	
	public void addSubTask(CubeQuery cubequery, int difference, int replace, long strTime, CubeBase cubeBase) {
		addNewSubTask();
		cubeQuery.add(cubequery);
    	getLastSubTask().createSubTask(cubequery, difference, replace,  strTime,  cubeBase);
	}
	
    public abstract void generateSubTasks(CubeBase cubeBase,CubeQuery cubequery, SubTask OriginSbTsk, String measure);
}
