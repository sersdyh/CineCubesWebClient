package highlightmgr;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class HighlightMax extends Highlight {
	
	public HighlightMax() {
		super();
		semanticValue=new ArrayList<String>();
    	index_checked=new ArrayList<Integer>();
    	semanticValue_index=new ArrayList<Integer>();
	}
	
	
	public void execute(String[][] table){
		if(table==null) return;
		int num_of_msrs_in_table=table.length-2;
    	int maxLenght=(int) Math.floor(num_of_msrs_in_table*0.25);
    	 	
        String[] tmp_Values=new String[maxLenght];
    	int[] tmp_indexValues=new int[maxLenght];
    	DecimalFormat df = new DecimalFormat("#.##");
		df.setMinimumFractionDigits(2);
		 
	     DecimalFormatSymbols dfs = new DecimalFormatSymbols(); //new Add
	     dfs.setDecimalSeparator('.');
	     df.setDecimalFormatSymbols(dfs);
		
		 for(int j=0;j<maxLenght;j++) tmp_indexValues[j]=0;
		 for(int j=2 ; j<num_of_msrs_in_table+2;j++ ){
			for(int k=0;k<maxLenght;k++) {
				if(table[j][2]==null || table[j][2].equals("-") || table[j][2].length()==0) continue;
				if(returnConditionForMaxMin(tmp_indexValues[k],tmp_Values[k],table[j][2],1)  && isTableMinValue(tmp_Values,tmp_Values[k])){
					tmp_Values[k]=df.format(Float.parseFloat(table[j][2]));
					tmp_indexValues[k]=j;
					break;
				}
			}
		 }
    	for(int j=0;j<maxLenght;j++) {
    		semanticValue.add(tmp_Values[j].trim());
    		index_checked.add(tmp_indexValues[j]);
    		semanticValue_index.add(tmp_indexValues[j]);
    	}
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
