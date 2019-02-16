package tetxtmgr;

import highlightmgr.Highlight;
import restsrv.JsonQueue;

import java.awt.Color;
import java.util.ArrayList;
import cubemanager.cubebase.CubeQuery;

public class TextExtractionPPTX extends TextExtraction {

    public TextExtractionPPTX(){
    	super();
    }
   
    public String createTextForOriginalAct1(CubeQuery cubeQuery,String max, String min){
    	String dimensionText="Here, you can see the answer of the original query. You have specified ";
    	String maxText="You can observe the results in this table. We highlight the largest value" + max;
    	String minText="and the lowest value" + min;
    	dimensionText += cubeQuery.getSigmaTextForOriginalAct1();
    	dimensionText += ". We report on " + cubeQuery.getAggregateFunction() +
    			" of " + cubeQuery.getListMeasure().get(0).getName() + " grouped by ";
    	dimensionText += cubeQuery.getGammaTextForOriginalAct1();

		return (dimensionText+" .\n" + maxText + minText).replace("  ", " ");
    }
    
    public String createTextForOriginalAct2(ArrayList<String[]> Gamma,ArrayList<String[]> Sigma,String[][] Result){
    	String dimensionText="In this slide we remind you the result of the original query. \nNow we are going to explain the internal breakdown of " +
    			"this result ";
    	dimensionText+="by drilling down its grouper dimensions. ";
    	dimensionText+="\n In the first of the following two slides we will drill-in dimension "+Gamma.get(1)[0]+"."+Gamma.get(1)[1]+". ";
    	dimensionText+="Then we will drill-in dimension "+Gamma.get(0)[0]+"."+Gamma.get(0)[1]+". ";

		return dimensionText.replace("  ", " ").replace("_dim.", " at ").replace("_dim", "").replace("lvl", "level ");
    }
    
    public String createTextForAct1(CubeQuery cubeQuery,ArrayList<String[]>
    	SigmaCompareTo,	String[][] Result, String max, String min,
    	int diffGamma){    	
    	String txtNotes="In this graphic, we put the original request in context by comparing the value";
    	String[] gammaChange = cubeQuery.getGammaExpressions().get(diffGamma);
    	String[] sigmaOriginal=SigmaCompareTo.get(this.getIndexOfSigma(SigmaCompareTo, gammaChange[0]));
    	
    	txtNotes+=" "+sigmaOriginal[2].replace("*", "ALL")+" for "+sigmaOriginal[0].replace("_dim.lvl"," at level ")+" with its sibling values. We highlight the reference cells with bold, the highest value";
    	txtNotes+= max + "and the lowest value";
    	txtNotes+= min +"We calculate the "+ cubeQuery.getAggregateFunction() +" of "+ cubeQuery.getListMeasure().get(0).getName()+" while fixing ";
    	int j=0;
    	for(String[] sigma:cubeQuery.getSigmaExpressions()){
    		if(j>0) txtNotes+=", ";
    		if(j==cubeQuery.getSigmaExpressions().size()-1) txtNotes+=" and ";
    		txtNotes+=sigma[0].replace("_dim.lvl"," at level ")+" to be equal to '"+sigma[2].replace("*", "ALL")+"'";
    		j++;
    	}
    	txtNotes+=".";
		return txtNotes.replace("  ", " ");
    	
    }
    
    public String createTextForAct2(ArrayList<String[]> GammaCompareTo,
    								ArrayList<String[]> SigmaCompareTo,
    								String[][] Result,
    								int diffGamma,
    								String Aggregate,
    								String Measure,
    								int num_values_drill_in, 
    								String[] currentGamma){
    	int current_lvl=Integer.parseInt(currentGamma[1].split("lvl")[1]);
    	String txtNotes="In this slide, we expand dimension "+currentGamma[0]+" by drilling down from level "+(current_lvl+1)+" to level "+current_lvl+". ";
    	
    	txtNotes+="For each cell we show both the "+Aggregate+" of "+Measure+" and the number of tuples that correspond to it in parentheses. ";
    	
		return txtNotes.replace("  ", " ").replace("_dim.", " at ").replace("_dim", "").replace("lvl", "level ");
    }
    
    public String createTxtForDominatedRowColumns(String[][] PivotTable,Color[][] ColorTbl,ArrayList<Highlight> htable,boolean showRows,boolean showColumns){
    	
    	int max_index=0;
    	int min_index=1;
    
    	String textToReturn="We highlight the "+htable.get(2).valuePerColor[min_index]+" lowest values in "+htable.get(0).mincolor_name+" and the "
    						+htable.get(2).valuePerColor[max_index]+" largest in "+htable.get(1).maxcolor_name+" color.\n";
    	String ret_value="Some interesting findings include:\n";
    	int length_ret_value=ret_value.length();
    	if(showColumns){
    		ret_value+=createTxtForColumnsDominate(PivotTable,htable.get(2));
    	}
    	if(showRows){
    		ret_value+=createTxtForRowsDominate(PivotTable,htable.get(2));
    	}
    	if(length_ret_value==ret_value.length()) ret_value="";
    	return (textToReturn+ret_value).replace("  ", " ");
    }
    
    public String createTxtForColumnsDominate(String[][] PivotTable,Highlight htable){
    	int max_index=0;
    	int min_index=1;
    	
    	String txt="";
    	if(htable.DominateMax!=null){
	    	for(Integer index :htable.DominateMax ){
	    			txt+="Column "+PivotTable[0][index]+" has "+htable.DominationColor[index][max_index]+" of the "+htable.valuePerColor[max_index]+" highest values.\n";
	    	}
    	}
    	if(htable.DominateMin!=null){
	    	for(Integer index :htable.DominateMin ){	    	
	    			txt+="Column "+PivotTable[0][index]+" has "+htable.DominationColor[index][min_index]+" of the "+htable.valuePerColor[min_index]+" lowest values.\n";
	    	}
    	}
    	return txt.replace("  ", " ");
    }
    
    public String createTxtForRowsDominate(String[][] PivotTable,Highlight htable){
    	int max_index=0;
    	int min_index=1;
    
    	String txt="";
    	for(Integer index :htable.DominateMax ){
    			txt+="Row "+PivotTable[index][0]+" has "+htable.DominationColor[index][max_index]+" of the "+htable.valuePerColor[max_index]+" highest values.\n";
    	}
    	
    	for(Integer index :htable.DominateMin ){
    			txt+="Row "+PivotTable[index][0]+" has "+htable.DominationColor[index][min_index]+" of the "+htable.valuePerColor[min_index]+" lowest values.\n";
    	}
    	return txt.replace("  ", " ");
    }
    
  
    
    public String createTxtForIntroSlide(CubeQuery cubeQuery ) {
    	String retTxt="This is a report on the "+ cubeQuery.getAggregateFunction()+" of "+cubeQuery.getListMeasure().get(0).getName()+" when ";
    	retTxt += cubeQuery.getSigmaTextForIntro();
    	retTxt+=". We will start by answering the original query and we complement the result with contextualization and detailed analyses.";

    	return retTxt.replace("  ", " ").replace("\n", "").replace("\r", "");
    }
    
    /*version 1*/
    public String createTxtComparingToSiblingColumn(String[][] PivotTable,Highlight htable){
    	
    	String numOfcases=String.valueOf(PivotTable.length-1);
    	String ret_value="";
    	int ColumnOfOurValue=htable.bold;
    	if(htable.countHigher==null) return "";
    	if(htable.countHigher.length==2){
    		ret_value="Compared to its sibling we observe that in ";
    		for(int j=0;j<htable.countHigher.length;j++){
    			if(j!=ColumnOfOurValue-1){
        			if(htable.countHigher[j]>0) {
            			ret_value+=String.valueOf(htable.countHigher[j])+" out of "+numOfcases+" cases "+htable.helpValues[ColumnOfOurValue-1]+" has higher value than "+htable.helpValues[j]+".\n";
            			if(htable.countLower[j]>0 || htable.countEqual[j]>0 || htable.nullValues[j]>0)  ret_value+="In ";
            		}
            		
            		if(htable.countLower[j]>0){
            			ret_value+=String.valueOf(htable.countLower[j])+" out of "+numOfcases+" cases "+htable.helpValues[ColumnOfOurValue-1]+" has lower value than "+htable.helpValues[j]+".\n";
            			if(htable.countEqual[j]>0 || htable.nullValues[j]>0) ret_value+="In ";;
            		}
            		
            		if(htable.countEqual[j]>0){
            			ret_value+=String.valueOf(htable.countLower[j])+" out of "+numOfcases+" cases "+htable.helpValues[ColumnOfOurValue-1]+" has equal value than "+htable.helpValues[j]+".\n";
            			if(htable.nullValues[j]>0) ret_value+="In ";
            		}
            		
            		if(htable.nullValues[j]>0){
            			ret_value+=String.valueOf(htable.nullValues[j])+" out of "+numOfcases+" cases "+htable.helpValues[j]+" has null value.\n";
            		}
    			}
    		}
    	}
    	else{
    		ret_value="Compared to its sibling we observe the following:\n";
    		for(int j=0;j<htable.countHigher.length;j++){
    			if(j!=ColumnOfOurValue-1){
    				if(htable.countHigher[j]>0) {
            			ret_value+="In "+String.valueOf(htable.countHigher[j])+" out of "+numOfcases+" cases "+htable.helpValues[ColumnOfOurValue-1]+" has higher value than "+htable.helpValues[j]+".\n";
            		}
            		
            		if(htable.countLower[j]>0){
            			ret_value+="In "+String.valueOf(htable.countLower[j])+" out of "+numOfcases+" cases "+htable.helpValues[ColumnOfOurValue-1]+" has lower value than "+htable.helpValues[j]+".\n";
            		}
            		
            		if(htable.countEqual[j]>0){
            			ret_value+="In "+String.valueOf(htable.countEqual[j])+" out of "+numOfcases+" cases "+htable.helpValues[ColumnOfOurValue-1]+" has equal value than "+htable.helpValues[j]+".\n";            			
            		}
            		
            		if(htable.nullValues[j]>0){
            			ret_value+="In "+String.valueOf(htable.nullValues[j])+" out of "+numOfcases+" cases "+htable.helpValues[j]+" has null value.\n";
            		}
    			}
    		}
    	}
    	
    	return ret_value.replace("  ", " ");
    }
    
    /*version 1*/
    public String createTxtComparingToSiblingRow(String[][] PivotTable,Highlight httable){
    	
    	String numOfcases=String.valueOf(PivotTable[0].length-1);
    	String ret_value="";
    	if(httable.countHigher==null) return ret_value="";
    	if(httable.countHigher.length==2){
    		ret_value="Compared to its sibling we observe that in ";
    		int length_ret_value=ret_value.length();
    		for(int j=0;j<httable.countHigher.length;j++){
    			if(j!=httable.bold-1){
        			if(httable.countHigher[j]>0) {
            			ret_value+=String.valueOf(httable.countHigher[j])+" out of "+numOfcases+" cases "+httable.helpValues[httable.bold-1]+" has a higher value than "+httable.helpValues[j]+".\n";
            			if(httable.countLower[j]>0 || httable.countEqual[j]>0 || httable.nullValues[j]>0) ret_value+="In ";
            		}
            		
            		if(httable.countLower[j]>0){
            			ret_value+=String.valueOf(httable.countLower[j])+" out of "+numOfcases+" cases "+httable.helpValues[httable.bold-1]+" has a lower value than "+httable.helpValues[j]+".\n";
            			if(httable.countEqual[j]>0 || httable.nullValues[j]>0) ret_value+="In ";
            		}
            		
            		if(httable.countEqual[j]>0){
            			ret_value+=String.valueOf(httable.countEqual[j])+" out of "+numOfcases+" cases "+httable.helpValues[httable.bold-1]+" has an equal value to "+httable.helpValues[j]+".\n";
            			if(httable.nullValues[j]>0) ret_value+="In ";
            		}
            		
            		if(httable.nullValues[j]>0){
            			ret_value+=String.valueOf(httable.nullValues[j])+" out of "+numOfcases+" cases "+httable.helpValues[j]+" has null value.\n";
            		}
    			}
    		}
    		if(ret_value.length()==length_ret_value) ret_value="";
    	}
    	else{
    		ret_value="Compared to its sibling we observe the following:\n";
    		int length_ret_value=ret_value.length();
    		for(int j=0;j<httable.countHigher.length;j++){
    			if(j!=httable.bold-1){
        			if(httable.countHigher[j]>0) {
            			ret_value+="In "+String.valueOf(httable.countHigher[j])+" out of "+numOfcases+" cases "+httable.helpValues[httable.bold-1]+" has a higher value than "+httable.helpValues[j]+".\n";
            		}
            		
            		if(httable.countLower[j]>0){
            			ret_value+="In "+String.valueOf(httable.countLower[j])+" out of "+numOfcases+" cases "+httable.helpValues[httable.bold-1]+" has a lower value than "+httable.helpValues[j]+".\n";
            		}
            		
            		if(httable.countEqual[j]>0){
            			ret_value+="In "+String.valueOf(httable.countEqual[j])+" out of "+numOfcases+" cases "+httable.helpValues[httable.bold-1]+" has an equal value to "+httable.helpValues[j]+".\n";
            		}
            		
            		if(httable.nullValues[j]>0){
            			ret_value+="In "+String.valueOf(httable.nullValues[j])+" out of "+numOfcases+" cases "+httable.helpValues[j]+" has null value.\n";
            		}
    			}
    		}
    		if(ret_value.length()==length_ret_value) ret_value="";
    	}
    	
    	return ret_value.replace("  ", " ");
    }
    
   
    private int getIndexOfSigma(ArrayList<String[]> sigmaExpressions,String gamma_dim) {
		int ret_value=-1;
		int i=0;
		for(String[] sigma : sigmaExpressions ){
			if(sigma[0].split("\\.")[0].equals(gamma_dim)) ret_value=i;
			i++;
		}
		return ret_value;
	}
}
