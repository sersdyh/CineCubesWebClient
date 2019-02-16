package highlightmgr;

import java.util.ArrayList;

public class HighlightDominationColumn extends Highlight {
	
	public ArrayList<String> helpValues2;
	
	public HighlightDominationColumn() {
		super();
		DominateMax=new ArrayList<Integer>();
		DominateMin=new ArrayList<Integer>();
		DominateMiddle=new ArrayList<Integer>();
	}
	
	public void execute(String[][] PivotTable){
		
		
	 	for(int i=0; i<PivotTable.length; i++){
    		for(int j=0; j<PivotTable[i].length; j++){
    			PivotTable[i][j] = PivotTable[i][j].replace(",", ".");
    		}
    	}
   	   	
		int max_index=0;
    	int min_index=1;
    	int middle_index=2;
    	DominationColor=new Integer[PivotTable[0].length][3];
    	valuePerColor[0]=valuePerColor[1]=valuePerColor[2]=0;
    	
		for(int i=0;i<PivotTable[0].length;i++){
    		for(int j=0;j<3;j++) DominationColor[i][j]=0;
    	}
    	
    	for(int i=0;i<PivotTable.length;i++){
    		for(int j=0;j<PivotTable[i].length;j++){
    			Float currentValue;
    			boolean in_middle=true;
    			if(PivotTable[i][j].equals("-")) continue;
    			try{
    				String[] x=PivotTable[i][j].split("\\(");
    				currentValue=Float.parseFloat(x[0].trim());  
    			}
    			catch(Exception ex){
    				try{
    					currentValue=Float.parseFloat(PivotTable[i][j].trim());
    				}
    				catch(Exception ex1){
    					continue;
    				}
    			}
    			for(int k=0;k<semanticValue.size();k++){
    				if(Float.parseFloat(semanticValue.get(k))==currentValue){
    					DominationColor[j][max_index]++;
    					valuePerColor[max_index]++;
    					in_middle=false;
    				}
    			}
    			if(in_middle){
    				for(int k=0;k<helpValues2.size();k++){
    					if(Float.parseFloat(helpValues2.get(k))==currentValue){
    						DominationColor[j][min_index]++;
    						valuePerColor[min_index]++;
    						in_middle=false;
	    				}
	    			}
	    			if(in_middle){
	    				DominationColor[j][middle_index]++;
	    				valuePerColor[middle_index]++;
	    			}
    			}
    		}
    	}
    	
    	findDomination(DominationColor,max_index,min_index,middle_index,DominateMax,DominateMin,DominateMiddle);
    }
	
	private void findDomination(Integer[][] DominationColor,int max_index,int min_index,int middle_index,ArrayList<Integer> DominateMax,ArrayList<Integer> DominateMin,ArrayList<Integer> DominateMiddle){
    	max_appearance_per_color[max_index]=maxValueInTableColumn(DominationColor,max_index);
    	max_appearance_per_color[min_index]=maxValueInTableColumn(DominationColor,min_index);
    	max_appearance_per_color[middle_index]=maxValueInTableColumn(DominationColor,middle_index);
    	DominateMax.clear();
    	DominateMin.clear();
    	DominateMiddle.clear();
    	for(int i=0;i<DominationColor.length;i++){
			if(DominationColor[i][max_index]==max_appearance_per_color[max_index] && max_appearance_per_color[max_index]!=1) DominateMax.add(i);
			if(DominationColor[i][min_index]==max_appearance_per_color[min_index] && max_appearance_per_color[min_index]!=1) DominateMin.add(i);
			if(DominationColor[i][middle_index]==max_appearance_per_color[middle_index] && max_appearance_per_color[middle_index]!=1) DominateMiddle.add(i);
    	}
    }
	
	private int maxValueInTableColumn(Integer[][] table,int column){
    	int max_value=table[0][column];
    	for(int i=1;i<table.length;i++){
    		if(max_value<table[i][column]) max_value=table[i][column];
    	}
    	return max_value;
    }
	
}
