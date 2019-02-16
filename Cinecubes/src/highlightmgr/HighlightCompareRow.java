package highlightmgr;

public class HighlightCompareRow extends Highlight {
		
	public HighlightCompareRow() {
		super();
	}
		
	public void execute(String[][] PivotTable){
		countHigher=new int[PivotTable.length-1];
	 	countLower=new int[PivotTable.length-1];
	 	countEqual=new int[PivotTable.length-1];
    	nullValues=new int[PivotTable.length-1];
    	helpValues=new String[PivotTable.length-1];
    	
     	for(int i=0; i<PivotTable.length; i++){
    		for(int j=0; j<PivotTable[i].length; j++){
    			PivotTable[i][j] = PivotTable[i][j].replace(",", ".");
    		}
    	}
    		    	
    	for(int i=1;i<PivotTable.length;i++){
    		countHigher[i-1]=0;
    		countLower[i-1]=0;
    		countEqual[i-1]=0;
    		helpValues[i-1]=PivotTable[i][0];
    		if(i!=this.bold){
	    		for(int j=1;j<PivotTable[0].length;j++){
	    			if(!PivotTable[i][j].equals("-") && !PivotTable[this.bold][j].equals("-")){	    				
		    			if(Double.parseDouble(PivotTable[this.bold][j])>Double.parseDouble(PivotTable[i][j])){
		    				countHigher[i-1]++;
		    			}
		    			else if(Double.parseDouble(PivotTable[this.bold][j])<Double.parseDouble(PivotTable[i][j])){
							countLower[i-1]++;
		    			}
		    			else countEqual[i-1]++;
	    			}
	    			else nullValues[i-1]++;
	    		}
    		}
    		else {
    			for(int j=1;j<PivotTable[0].length;j++) {
    				if(PivotTable[i][j].equals("-")){
    					nullValues[i-1]++;
    				}
    			}
    		}
    	}
    }
}
