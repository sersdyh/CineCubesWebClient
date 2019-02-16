package highlightmgr;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class HighlightMin extends Highlight {
	
	public HighlightMin() {
		super();
		semanticValue=new ArrayList<String>();
    	index_checked=new ArrayList<Integer>();
    	semanticValue_index=new ArrayList<Integer>();
	}

	public void execute(String[][] table){
		
		
		if(table==null) return;
		int num_of_msrs_in_table=table.length-2;    	
    	int minLenght=(int) Math.floor(num_of_msrs_in_table*0.25);
    	
    	String[] tmp_minValues=new String[minLenght];
    	int[] tmp_indexValues=new int[minLenght];
    	DecimalFormat df = new DecimalFormat("#.##");
    	df.setMinimumFractionDigits(2);
    	
    	DecimalFormatSymbols dfs = new DecimalFormatSymbols(); //new Add
    	dfs.setDecimalSeparator('.');
    	df.setDecimalFormatSymbols(dfs);
    	
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
    		semanticValue.add(tmp_minValues[j].trim());
    		index_checked.add(tmp_indexValues[j]);
    		semanticValue_index.add(tmp_indexValues[j]);
    	}
    	
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
	
	boolean returnConditionForMaxMin(int index,String tmp_value,String table_value,int mode/* 0 min,1 max */){
		if(mode==0) return (index==0 || Float.parseFloat(tmp_value)>Float.parseFloat(table_value));
		return (index==0 || Float.parseFloat(tmp_value)<Float.parseFloat(table_value));
	}
}
