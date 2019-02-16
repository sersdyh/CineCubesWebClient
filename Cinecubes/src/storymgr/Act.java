package storymgr;

import java.util.ArrayList;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import taskmgr.SubTask;
import taskmgr.Task;
import taskmgr.TaskActI;
import taskmgr.TaskActII;
import taskmgr.TaskIntro;
import taskmgr.TaskOriginal;
import taskmgr.TaskSummary;
import tetxtmgr.TextExtraction;
import tetxtmgr.TextExtractionPPTX;
import exctractionmethod.SqlQuery;


public class Act {

	
	private TextExtraction txtMgr;
	/**
	 * @uml.property  name="id"
	 */
	private int id;
    /**
	 * @uml.property  name="episodes"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="StoryMgr.Episode"
	 */
    private ArrayList<Episode> Episodes;
    /**
	 * @uml.property  name="tsk"
	 * @uml.associationEnd  
	 */
    private Task tsk;
    /**
	 * @uml.property  name="creationTime"
	 */
    private long creationTime;

    /**
	 * @uml.property  name="actHighlights"
	 */
    private  String ActHighlights; 
             
    public Act(int id){
    	txtMgr = new TextExtractionPPTX();
    	this.id =id;
    	Episodes=new ArrayList<Episode>();
    	ActHighlights="";
    	creationTime = System.nanoTime();
    }

	public int getSizeOfEpisodes() {
		return Episodes.size();
	}

	public Episode getEpisode(int i){
		return Episodes.get(i);
	}
	
	public int getNumEpisodes(){
		return Episodes.size();
	}	
	 public long getTimeCreation() {
		 return creationTime;
	 }
	
	/**
	 * @return  the id
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}
	
	/* for Debug Reason*/
	public String toString(){
		return "Act id:"+String.valueOf(id)+"\n# Episodes:"
				+String.valueOf(this.getNumEpisodes());
	}
	
	public void doIntroTask(CubeQuery cubequery, boolean isAudioOn,  CubeManager CubeManager){
		tsk = new TaskIntro();
		tsk.addCubeQuery(cubequery);
		tsk.generateSubTasks(CubeManager.getCubeBase(),cubequery,null,"");
		Slide tmpslide=new Slide();
		Episodes.add(tmpslide);
		tmpslide.createSlideIntro(isAudioOn,txtMgr, tsk.getCubeQuery(0)); 
		creationTime = System.nanoTime() - creationTime;
	}
  	
	public SubTask doOriginalTask(CubeQuery cubequery, boolean isAudioOn,  CubeManager CubeManager){
		tsk =new TaskOriginal();
		tsk.addCubeQuery(cubequery);
		tsk.generateSubTasks(CubeManager.getCubeBase(),cubequery, null, "");
		constructOriginalActEpidoses();
		SubTask OriginSbTsk = tsk.getLastSubTask();
		if (OriginSbTsk.getExtractionMethod().getResult().getResultArray() == null) {
			System.err.println("Your query does not have result. Try again!");
			System.exit(2);
		}
		ActHighlights += ((Slide) getEpisode(0)).createSlideOriginal(isAudioOn, txtMgr, tsk.getCubeQuery(0));
		creationTime = System.nanoTime() - creationTime;
		return OriginSbTsk;
	}
	
	public void constructOriginalActEpidoses() {
		Slide newSlide = new Slide();
		SubTask subtsk = tsk.getSubTask(0);
		CubeQuery currentCubeQuery = tsk.getCubeQuery(0);
       	newSlide.addCubeQuery(currentCubeQuery); 
    	String[] extraPivot = new String[2];
        extraPivot[0] = "";
        extraPivot[1] = "";
		
	    /*====== Compute Pivot Table =======*/
    	newSlide.computePivotTable(subtsk.getExtractionMethod().getRowPivot(),
	    		 			  subtsk.getExtractionMethod().getColPivot(),
	    		 			  subtsk.getExtractionMethod().getResultArray(),
	    		 			  extraPivot);
	    Episodes.add(newSlide);
	   /*====== Calculate Highlights =======*/
    	newSlide.calculateHighlights(subtsk);
    	/*====== Compute Color Table =======*/
    	newSlide.computeColorTable(); 
    	/*====== Calculate domination Highlights =======*/
    	long start_creation_domination = System.nanoTime();
		newSlide.addTimeComputeHighlights(System.nanoTime() - start_creation_domination);
	}

	public Act doTaskActI(CubeQuery cubequery, SubTask OriginSbTsk ,boolean isAudioOn,  CubeManager CubeManager,String measure){
		ArrayList<Slide> slideToEnd = new ArrayList<Slide>();
		tsk =new TaskActI();
		tsk.generateSubTasks(CubeManager.getCubeBase(), cubequery, OriginSbTsk, measure);
		constructActIEpidoses();
		slideToEnd = setupTextAct1(cubequery, isAudioOn);
		creationTime = System.nanoTime() - creationTime;
		if (slideToEnd.size() > 0) {
			Act newAct = new Act(3);
			newAct.setupTextAct3(slideToEnd);
			return newAct;
		}
		return null;
	}
	
	private ArrayList<Slide> setupTextAct1(CubeQuery origCubeQuery, boolean isAudioOn) {
		ArrayList<Integer> numSlideToRemove = new ArrayList<Integer>();
		ArrayList<Slide> slideToEnd = new ArrayList<Slide>();
		boolean ActHasWriteHiglights = false;
		for (int j = 0; j < getNumEpisodes(); j++) {
			Slide currentSlide = (Slide) getEpisode(j);
			if (j == 0) {
				currentSlide.createSlideAct1(isAudioOn);
			} else {
				if (currentSlide.checkDifferenceFromOrigin(-1)) {
					String addToNotes = currentSlide.createNotes(txtMgr,origCubeQuery );
					if (ActHasWriteHiglights == false && addToNotes.length() > 0) {
						ActHasWriteHiglights = true;
						ActHighlights +=  "@First, we tried to put the original result in context, by comparing its defining values with similar ones.\n\t";
					}
					ActHighlights += currentSlide.addNotes(addToNotes,isAudioOn );
				} else {
					slideToEnd.add(currentSlide);
					numSlideToRemove.add(j);
					currentSlide.createSlideAct1(origCubeQuery);
				}
			}
		}
		return slideToEnd;

	}
	
	private void setupTextAct3(  ArrayList<Slide>  slideToEnd){
		Slide newSlide = new Slide("","Auxiliary slides for Act I","", System.nanoTime());
		Episodes.add(newSlide);
		for (int k = 0; k < slideToEnd.size(); k++) {
			Episodes.remove(slideToEnd.get(k));
			Episodes.add(slideToEnd.get(k));
		}
		slideToEnd.clear();
	}
	
	public void doTaskActII(CubeQuery cubequery,SubTask OriginSbTsk ,boolean isAudioOn,  CubeManager CubeManager, String measure){
		tsk = new TaskActII();	
		tsk.generateSubTasks(CubeManager.getCubeBase(), cubequery, OriginSbTsk, measure);
		if (tsk.getNumSubTasks() > 2) {
			constructActIIEpidoses(tsk.getSubTaskList() , tsk.getCubeQueriesList()) ;
			setupTextAct2(cubequery,OriginSbTsk, isAudioOn);
		}
		creationTime = System.nanoTime() - creationTime;
	}
	
	public void setupTextAct2( CubeQuery origCubeQuery, SubTask origSubtsk, boolean isAudioOn) {
		boolean ActHasWriteHiglights = false;
		for (int j = 0; j < getNumEpisodes(); j++) {
			Slide currentSlide = (Slide) getEpisode(j);
			if (j == 0) {
				currentSlide.createSlideAct2();
			} else if (currentSlide.checkCountOfSubTask()) {
				SubTask subtsk = currentSlide.getSubTasks().get(0);
				CubeQuery currentCubeQuery = currentSlide. getCubeQuery(0);
				Tabular tbl = (Tabular) currentSlide.getVisual();
				subtsk.createHightLightTable(tbl.getPivotTable(), tbl.getColorTable());
				long start_creation_domination = System.nanoTime();
				currentSlide.addTimeComputeHighlights(System.nanoTime() - start_creation_domination);
				String add_to_notes = "";
				if (currentSlide.checkDifferenceFromOrigin(-4)) {
					add_to_notes = currentSlide.createSlideAct2("Rows", txtMgr, origCubeQuery,
							origSubtsk.getExtractionMethod().getResult().getColPivot().size());
				} else if (currentSlide.checkDifferenceFromOrigin(-5)) {
					add_to_notes = currentSlide.createSlideAct2("Columns", txtMgr, origCubeQuery,
							origSubtsk.getExtractionMethod().getResult().getColPivot().size());
				}
				if (ActHasWriteHiglights == false && add_to_notes.length() > 0) {
					ActHasWriteHiglights = true;
					 ActHighlights +=("Then we analyzed the results by drilling down one level in the hierarchy.\n\t\n\t");
				}
				try {

					 ActHighlights +=( "##When we drilled down "
							+ currentCubeQuery.getGammaExpressions()
									.get(currentSlide.getSubTasks().get(0)
											.getDifferenceFromOrigin(2))[0]
									.replace("_dim", ", ")
							+ " we observed the following facts:\n"
							+ "~~"
							+ add_to_notes.split(":")[1].replace("\n", "\n\t")
									.replace("\t\t", "\t"));
					 ActHighlights = ActHighlights.replace("  ", " ");
				} catch (Exception ex) {
					// Do Nothing
				}
			} else {
				currentSlide.createSlideAct2(txtMgr);
			}
			if (isAudioOn) 
				currentSlide.addAudioToEpisode();
		}
	}
	
	public void doSummaryTask(ArrayList<Act> acts, boolean isAudioOn,  CubeManager CubeManager){
		tsk = new TaskSummary();
		tsk.generateSubTasks(CubeManager.getCubeBase(), null, null, "");
		Slide tmpslide=new Slide();
		tmpslide.createSlideEnd(isAudioOn, composeActHighlights(acts));
		Episodes.add(tmpslide);
		creationTime = System.nanoTime() - creationTime;
	}
	
	private String composeActHighlights(ArrayList<Act> acts) {
		String notesFromAct = "In this slide we summarize our findings.";
		for (Act actItem : acts) {
			if (actItem.ActHighlights.length() > 0) {
				if ( notesFromAct.length() > 0)
					 notesFromAct += "@";
				notesFromAct += actItem.ActHighlights;
			}
			notesFromAct = notesFromAct.replace("\n\n\n", "\n")
					.replace("\n\n", "\n").replace("\n\t\n", "\n\t");
		}
		notesFromAct = notesFromAct.replace("\n\n\n", "\n")
				.replace("\n\n", "\n").replace("\t", "").replace("\r", "");
		return notesFromAct;
	}

	
	public void addHighightToEpisode(){
		for (int i = 1; i < getNumEpisodes(); i++) {
			Slide currentSlide = (Slide) getEpisode(i);
			currentSlide.createHighlightEpisode();
		}
	}
	
	public void constructActIIEpidoses(ArrayList<SubTask> subTasks, ArrayList<CubeQuery> cubeQueries) {
		SubTask origSubtsk =subTasks.get(1);
		CubeQuery origCubeQuery = cubeQueries.get(1);
		for (int j = 0; j < subTasks.size(); j++) {
			if (j == 1)
				continue;
			SubTask subtsk = subTasks.get(j);
			SqlQuery currentSqlQuery = ((SqlQuery) subtsk.getExtractionMethod());
			CubeQuery currentCubeQuery =  cubeQueries.get(j);
			Slide newSlide = new Slide();			//go to episode
									//

			if ((currentSqlQuery.getResultArray() != null)) {
				newSlide.addCubeQuery(currentCubeQuery);
				String[] extraPivot = createExtraPivot(subtsk, origSubtsk, origCubeQuery);
				/* ====== Compute Pivot Table ======= */
				
				newSlide. computePivotTable (subtsk.getExtractionMethod().getRowPivot(),
  		 			  subtsk.getExtractionMethod().getColPivot(),
  		 			  subtsk.getExtractionMethod().getResultArray(),
  		 			  extraPivot);
				if (subtsk.getDifferencesFromOrigin().size() > 0
						&& (subtsk.getDifferencesFromOrigin().get(0) == -4 ||
						subtsk.getDifferencesFromOrigin().get(0) == -5)
						&& subtsk.getDifferencesFromOrigin().get(1) > 0) {

					/* ====== Combine Subtask and Pivot Table ======= */
					Slide tmpSlide = (Slide) getEpisode(getNumEpisodes() - 1);
					tmpSlide.copySlide(newSlide,currentCubeQuery,subtsk);
				} else {
					newSlide.addSubTask(subtsk);
					Episodes.add(newSlide);
				}
			} else if (currentSqlQuery.getTitleosColumns() != null
					&& currentSqlQuery.getTitleosColumns().contains("Act")) {
				newSlide.createNewSlide(currentCubeQuery, subtsk,
		    			currentSqlQuery.getTitleosColumns());
				Episodes.add(newSlide);
			}
		}
		addHighightToEpisode();
	
	}
	
	public String[] createExtraPivot(SubTask subtsk,SubTask origSubtsk, CubeQuery origCubeQuery){
		String[] extraPivot = new String[2];
		extraPivot[0] = "";
		extraPivot[1] = "";
		
		if (subtsk.getDifferencesFromOrigin().size() > 0
				&& subtsk.getDifferencesFromOrigin().get(0) == -4) {
			extraPivot[0] = String.valueOf(subtsk.getDifferencesFromOrigin().get(0));
			extraPivot[1] = origSubtsk.getExtractionMethod().getRowPivot().
					toArray()[subtsk.getDifferencesFromOrigin().get(1)].toString();
		}
		if (subtsk.getDifferencesFromOrigin().size() > 0
				&& subtsk.getDifferencesFromOrigin().get(0) == -5) {
			extraPivot[0] = String.valueOf(subtsk.getDifferencesFromOrigin().get(0));
			extraPivot[1] = origCubeQuery.getValueFromColPivot(subtsk.getDifferencesFromOrigin().get(1));
		}
		return extraPivot;
}
	
	 public void constructActIEpidoses() {			
			for(int j = 0; j< tsk.getNumSubTasks(); j++){
	    		if( j == 1 ) 
	    			continue;
				SubTask subtsk= tsk.getSubTask(j);
	    		SqlQuery currentSqlQuery=((SqlQuery)subtsk.getExtractionMethod());
	    		CubeQuery currentCubeQuery = tsk.getCubeQuery(j);
	    		Slide newSlide=new Slide();		
			    
			    if((currentSqlQuery.getResultArray()!=null)){
			    	/*====== Compute Pivot Table =======*/
			    	String[] extraPivot=new String[2];
			        extraPivot[0]="";
			        extraPivot[1]="";
			        
			 	   newSlide.computePivotTable(subtsk.getExtractionMethod().getRowPivot(),
			 	    		 			  subtsk.getExtractionMethod().getColPivot(),
			 	    		 			  subtsk.getExtractionMethod().getResultArray(),
			 	    		 			  extraPivot);
			 	  Episodes.add(newSlide);
			 	 newSlide.addHighLight(subtsk, currentCubeQuery, tsk.getCubeQuery(1));
			    } else if (j == 0) {
			    	newSlide.createNewSlide(currentCubeQuery, subtsk,
			    			currentSqlQuery.getTitleosColumns());
			    	Episodes.add(newSlide);
			    }
			    
			}
		}
	
}
