package highlightmgr;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeSet;

public class HighlightTable extends Highlight {
	
	
	public ArrayList<String> maxValues;
	public ArrayList<String> minValues;
	public ArrayList<String> middleValues;
	public ArrayList<Integer> index_checked;
	public ArrayList<Integer> max_index;
	public ArrayList<Integer> min_index;	
	
	/* To compare Columns */
	public int[] countHigherPerColumn;
	public int[] countLowerPerColumn;
	public int[] countEqualPerColumn;
	public int[] nullValuesPerColumn;
	public String[] valuesOfColumn;
	
	/* To compare Rows */
	public int[] countHigherPerRow;
	public int[] countLowerPerRow;
	public int[] countEqualPerRow;
	public int[] nullValuesPerRow;
	public String[] valuesOfRow;
		
	/*for Domination of Rows and Columns*/
	public ArrayList<Integer> rowsDominateMax;
	public ArrayList<Integer> rowsDominateMin;
	public ArrayList<Integer> rowsDominateMiddle;
	public ArrayList<Integer> colsDominateMax;
	public ArrayList<Integer> colsDominateMin;
	public ArrayList<Integer> colsDominateMiddle;
	public Integer[] max_appearance_per_color;
	public Integer[] valuePerColor;
	public Integer[][] rowsDominationColor;
	public Integer[][] colsDominationColor;
	
	/* For Colors*/
	
	public int boldColumn;
	public int boldRow;
	
	
	public HighlightTable(){
		super();
		maxValues=new ArrayList<String>();
    	minValues=new ArrayList<String>();
    	middleValues=new ArrayList<String>();
    	index_checked=new ArrayList<Integer>();
    	max_index=new ArrayList<Integer>();
    	min_index=new ArrayList<Integer>();
    	
    	maxcolor=Color.RED;
    	maxcolor_name="red";
    	mincolor=Color.blue;
    	mincolor_name="blue";
    	middlecolor=Color.black;
    	middlecolor_name="black";
    	boldColumn=-1;
    	boldRow=-1;
    	
    	rowsDominateMax=new ArrayList<Integer>();
    	rowsDominateMin=new ArrayList<Integer>();
    	rowsDominateMiddle=new ArrayList<Integer>();
    	colsDominateMax=new ArrayList<Integer>();
    	colsDominateMin=new ArrayList<Integer>();
    	colsDominateMiddle=new ArrayList<Integer>();
    	max_appearance_per_color=new Integer[3];
    	valuePerColor=new Integer[3];
	}
	
	public void execute(String[][] table){
		
	}
	
	public void setBoldColumn(TreeSet<String> Columns,String nameColumnToBold){
		for(int j=0;j<Columns.size();j++){
			if(("'"+Columns.toArray()[j].toString()+"'").equals(nameColumnToBold)) boldColumn=j+1;
		}
	}
	
	public void setBoldRow(TreeSet<String> Rows,String nameRowToBold){
		for(int i=0;i<Rows.size();i++){
			if(("'"+Rows.toArray()[i].toString()+"'").equals(nameRowToBold)) boldRow=i+1;
		}
	}
	
	public void createMinHightlight(String[][] table){
		if(table==null) return;
		int num_of_msrs_in_table=table.length-2;    	
    	int minLenght=(int) Math.floor(num_of_msrs_in_table*0.25);
    	
    	String[] tmp_minValues=new String[minLenght];
    	int[] tmp_indexValues=new int[minLenght];
    	DecimalFormat df = new DecimalFormat("#.##");
    	df.setMinimumFractionDigits(2);
    	
    	for(int j=0;j<minLenght;j++) tmp_indexValues[j]=0;
    	
    	for(int j=2;j<num_of_msrs_in_table+2;j++){
    		for(int k=0;k<minLenght;k++){
    			if(returnConditionForMaxMin(tmp_indexValues[k],tmp_minValues[k],table[j][2],0) && isTableMaxValue(tmp_minValues,tmp_minValues[k])){
    				tmp_minValues[k]=df.format(Float.parseFloat(table[j][2]));
    				tmp_indexValues[k]=j;
    				break;
    			}
    		}
    	 }
    	
    	for(int j=0;j<minLenght;j++) {
    		minValues.add(tmp_minValues[j]);
    		index_checked.add(tmp_indexValues[j]);
    		min_index.add(tmp_indexValues[j]);
    	}
	}
	
	public void createMaxHightlight(String[][] table){
		if(table==null) return;
		int num_of_msrs_in_table=table.length-2;
    	int maxLenght=(int) Math.floor(num_of_msrs_in_table*0.25);
    	 	
        String[] tmp_Values=new String[maxLenght];
    	int[] tmp_indexValues=new int[maxLenght];
    	DecimalFormat df = new DecimalFormat("#.##");
		 df.setMinimumFractionDigits(2);
		
		 for(int j=0;j<maxLenght;j++) tmp_indexValues[j]=0;
		 for(int j=2 ; j<num_of_msrs_in_table+2;j++ ){
			for(int k=0;k<maxLenght;k++) {
				if(returnConditionForMaxMin(tmp_indexValues[k],tmp_Values[k],table[j][2],1)  && isTableMinValue(tmp_Values,tmp_Values[k])){
					tmp_Values[k]=df.format(Float.parseFloat(table[j][2]));
					tmp_indexValues[k]=j;
					break;
				}
			}
		 }
    	for(int j=0;j<maxLenght;j++) {
    		maxValues.add(tmp_Values[j]);
    		index_checked.add(tmp_indexValues[j]);
    		max_index.add(tmp_indexValues[j]);
    	}
	}
	
	public void createMiddleHightlight(String[][] table){
		if(table==null) return;
		DecimalFormat df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
    	for(int i=2;i<table.length;i++){
    		if(!index_checked.contains(i)){
    			String toadd=df.format(Float.parseFloat(table[i][2]));
    			middleValues.add(toadd);
    		}
    	}
	}
	
	public void findDominatedRowsColumns(String[][] PivotTable,Color[][] ColorTbl){
    	   	   	
    	int max_index=0;
    	int min_index=1;
    	int middle_index=2;    
    	rowsDominationColor=new Integer[ColorTbl.length][3];
    	colsDominationColor=new Integer[ColorTbl[0].length][3];
    	valuePerColor[0]=valuePerColor[1]=valuePerColor[2]=0;
    	for(int i=0;i<PivotTable.length;i++){
    		for(int j=0;j<3;j++) rowsDominationColor[i][j]=0;
    	}
    	
		for(int i=0;i<PivotTable[0].length;i++){
    		for(int j=0;j<3;j++) colsDominationColor[i][j]=0;
    	}
    	
    	for(int i=0;i<ColorTbl.length;i++){
    		for(int j=0;j<ColorTbl[i].length;j++){
    			if(ColorTbl[i][j].equals(this.maxcolor)) {
    				rowsDominationColor[i][max_index]++;
    				colsDominationColor[j][max_index]++;
    				valuePerColor[max_index]++;
    			}
    			else if(ColorTbl[i][j].equals(this.mincolor)){
    				rowsDominationColor[i][min_index]++;
    				colsDominationColor[j][min_index]++;
    				valuePerColor[min_index]++;
    			}
    			else if(ColorTbl[i][j].equals(this.middlecolor)){
    				rowsDominationColor[i][middle_index]++;
    				colsDominationColor[j][middle_index]++;
    				valuePerColor[middle_index]++;
    			}
    		}
    	}
    	
    	findDomination(colsDominationColor,max_index,min_index,middle_index,colsDominateMax,colsDominateMin,colsDominateMiddle);
    	findDomination(rowsDominationColor,max_index,min_index,middle_index,rowsDominateMax,rowsDominateMin,rowsDominateMiddle);
    	
    }
	
	public void ComparingToSiblingColumn_v1(String[][] PivotTable){
    	countHigherPerColumn=new int[PivotTable[0].length-1];
    	countLowerPerColumn=new int[PivotTable[0].length-1];
    	countEqualPerColumn=new int[PivotTable[0].length-1];
    	nullValuesPerColumn=new int[PivotTable[0].length-1];
    	valuesOfColumn=new String[PivotTable[0].length-1];
    	
    	for(int j=1;j<PivotTable[0].length;j++){
    		countHigherPerColumn[j-1]=0;
    		countLowerPerColumn[j-1]=0;
    		valuesOfColumn[j-1]=PivotTable[0][j];
    		if(j!=this.boldColumn){
	    		for(int i=1;i<PivotTable.length;i++){
	    			if(!PivotTable[i][j].equals("-")  && !PivotTable[i][this.boldColumn].equals("-")){	    				
		    			if(Double.parseDouble(PivotTable[i][this.boldColumn])>Double.parseDouble(PivotTable[i][j])){
		    				countHigherPerColumn[j-1]++;
		    			}
		    			else if(Double.parseDouble(PivotTable[i][this.boldColumn])<Double.parseDouble(PivotTable[i][j])){
		    				countLowerPerColumn[j-1]++;
		    			}
		    			else countEqualPerColumn[j-1]++;
	    			}
	    			else nullValuesPerColumn[j-1]++;
	    		}
    		}
    		else {
    			for(int i=1;i<PivotTable.length;i++) {
    				if(PivotTable[i][j].equals("-")){
    					nullValuesPerColumn[j-1]++;
    				}
    			}
    		}
    	}
    }
	
	public void ComparingToSiblingRow_v1(String[][] PivotTable){
		 	countHigherPerRow=new int[PivotTable.length-1];
		 	countLowerPerRow=new int[PivotTable.length-1];
		 	countEqualPerRow=new int[PivotTable.length-1];
	    	nullValuesPerRow=new int[PivotTable.length-1];
	    	valuesOfRow=new String[PivotTable.length-1];
	    		    	
	    	for(int i=1;i<PivotTable.length;i++){
	    		countHigherPerRow[i-1]=0;
	    		countLowerPerRow[i-1]=0;
	    		countEqualPerRow[i-1]=0;
	    		valuesOfRow[i-1]=PivotTable[i][0];
	    		if(i!=this.boldRow){
		    		for(int j=1;j<PivotTable[0].length;j++){
		    			if(!PivotTable[i][j].equals("-") && !PivotTable[this.boldRow][j].equals("-")){	    				
			    			if(Double.parseDouble(PivotTable[this.boldRow][j])>Double.parseDouble(PivotTable[i][j])){
			    				countHigherPerRow[i-1]++;
			    			}
			    			else if(Double.parseDouble(PivotTable[this.boldRow][j])<Double.parseDouble(PivotTable[i][j])){
								countLowerPerRow[i-1]++;
			    			}
			    			else countEqualPerRow[i-1]++;
		    			}
		    			else nullValuesPerRow[i-1]++;
		    		}
	    		}
	    		else {
	    			for(int j=1;j<PivotTable[0].length;j++) {
	    				if(PivotTable[i][j].equals("-")){
	    					nullValuesPerRow[i-1]++;
	    				}
	    			}
	    		}
	    	}
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
	
	boolean tryParseFloat(String value){
    	try{
    		Float.parseFloat(value);
    	}
    	catch(Exception ex){
    		return false;
    	}
    	return true;
	}
	
	boolean isTableMaxValue(String[] table,String value){
		if(value==null) return true;
		String max_value=table[0];
		for(int i=1;i<table.length;i++){
			if(table[i]==null) return false;
			if(Float.parseFloat(table[i])>Float.parseFloat(max_value)) max_value=table[i];
		}
		return (value.equals(max_value));
	}
	
	boolean isTableMinValue(String[] table,String value){
		if(value==null) return true;
		String min_value=table[0];
		for(int i=1;i<table.length;i++){
			if(table[i]==null) return false;
			if(Float.parseFloat(table[i])<Float.parseFloat(min_value)) min_value=table[i];
		}
		return (value.equals(min_value));
	}
	
	boolean returnConditionForMaxMin(int index,String tmp_value,String table_value,int mode/* 0 min,1 max */){
		if(mode==0) return (index==0 || Float.parseFloat(tmp_value)>Float.parseFloat(table_value));
		return (index==0 || Float.parseFloat(tmp_value)<Float.parseFloat(table_value));
	}
		
}
