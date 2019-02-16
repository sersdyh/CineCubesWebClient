package storymgr;

import java.util.TreeSet;

public abstract class  Visual {
	
	protected String[][] PivotTable;
	
	public Visual(){
		
	}
	
	abstract public void CreatePivotTable(TreeSet<String> RowPivot,TreeSet<String> ColPivot,String QueryResult[][],String[] extraPivot);
	abstract  public String[][] getPivotTable();
	abstract  public void setPivotTable(String[][] pivotTable);
	
}
