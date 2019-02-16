package storymgr;

import highlightmgr.Highlight;
import highlightmgr.HighlightDominationColumn;
import highlightmgr.HighlightDominationRow;
import highlightmgr.HighlightMax;
import highlightmgr.HighlightMin;

import java.util.ArrayList;

import audiomgr.Audio;
import taskmgr.SubTask;

public abstract class Episode {
    
    protected ArrayList<SubTask> subTask;
    protected Visual visual;   
    protected Audio audio;
    protected ArrayList<Highlight> highlight;
    protected long timeComputeHighlights;
    
	public void addTimeComputeHighlights(long time ){
		timeComputeHighlights += time;
	}
    

	public long getTimeComputeHighlights(){
		return timeComputeHighlights;
	}
    
    public Episode(){    	
    	audio=new Audio();
    	subTask=new ArrayList<SubTask>();
    	setHighlight(new ArrayList<Highlight>());
    	timeComputeHighlights=0;
    }
	
	public ArrayList<SubTask> getSubTasks() {
		return subTask;
	}

	public void setSubTasks(ArrayList<SubTask> subtask) {
		subTask=subtask;
	}

	public void addSubTask(SubTask subtask) {
		subTask.add(subtask);
	}

	public ArrayList<Highlight> getHighlight() {
		return highlight;
	}

	public void setHighlight(ArrayList<Highlight> highlight) {
		this.highlight = highlight;
	}

	public String getHighlightMaxValue(){
		String maxText = "";
		if(highlight.get(0).semanticValue.size()>1)
    		maxText+="s";
		maxText += " with " + highlight.get(0).maxcolor_name + " "; 
		return maxText;
	}
	
	public String getHighlightMinValue(){
		String minText = "";
		if(highlight.get(1).semanticValue.size()>1)
    		minText += "s";
		minText += " with " + highlight.get(0).mincolor_name + " color. "; 
		return minText;
	}
	
	public boolean checkDifferenceFromOrigin(int value){
		return subTask.get(0).getDifferenceFromOrigin(0) == value;
	}
	
	public boolean checkCountOfSubTask(){
		return subTask.get(0).getDifferencesFromOrigin().size() > 1;
	}

	
	abstract public void computeColorTable();
	
	abstract public Visual getVisual();
	
	abstract public String[][] createResultArray();
	
	
	public void createHighlightEpisode(){
		highlight.clear();
		if (getSubTasks().get(0).getDifferencesFromOrigin().size() > 1) {

			HighlightMin hlmin = new HighlightMin();
			HighlightMax hlmax = new HighlightMax();
			HighlightDominationColumn hldomcol = new HighlightDominationColumn();
			highlight.add(hlmin);
			highlight.add(hlmax);
			highlight.add(hldomcol);
			String[][] allResult = createResultArray();
			this.timeComputeHighlights = System.nanoTime();
			hlmin.execute(allResult);
			hlmax.execute(allResult);
			hldomcol.semanticValue = hlmax.semanticValue;
			hldomcol.helpValues2 = hlmin.semanticValue;
			hldomcol.execute(((Tabular) getVisual()).getPivotTable());
			timeComputeHighlights = System.nanoTime()  - timeComputeHighlights;
				
		} else {
			HighlightMin hlmin = new HighlightMin();
			HighlightMax hlmax = new HighlightMax();
			highlight.add(hlmin);
			highlight.add(hlmax);
			this.timeComputeHighlights = System.nanoTime();
			hlmin.execute(getSubTasks().get(0)
					.getExtractionMethod().getResultArray());
			hlmax.execute(getSubTasks().get(0)
					.getExtractionMethod().getResultArray());
			timeComputeHighlights = System.nanoTime()  - timeComputeHighlights;
		}
		computeColorTable();   	
	}
	
	public void calculateHighlights(SubTask subTask){
	   	HighlightMin hlmin = new HighlightMin();
    	HighlightMax hlmax = new HighlightMax();
    	HighlightDominationRow hldomrow = new HighlightDominationRow();
    	HighlightDominationColumn hldomcol = new HighlightDominationColumn();
	    addSubTask(subTask);
	    getHighlight().add(hlmin);
	    getHighlight().add(hlmax);
	    getHighlight().add(hldomcol);
	    getHighlight().add(hldomrow);
		timeComputeHighlights = System.nanoTime();
    	hlmin.execute(subTask.getExtractionMethod().getResultArray());
    	hlmax.execute(subTask.getExtractionMethod().getResultArray());
    	
    	hldomcol.semanticValue = hlmax.semanticValue;
    	hldomcol.helpValues2 = hlmin.semanticValue;
    	hldomcol.execute(getVisual().getPivotTable());
    	
    	hldomrow.semanticValue = hlmax.semanticValue;
    	hldomrow.helpValues2 = hlmin.semanticValue;
    	hldomrow.execute(getVisual().getPivotTable());
    	timeComputeHighlights = System.nanoTime()  - timeComputeHighlights;
	}

}
