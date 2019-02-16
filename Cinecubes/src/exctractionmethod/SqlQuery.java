package exctractionmethod;

import java.util.ArrayList;


public class SqlQuery extends ExtractionMethod {
    
    private String[] SelectClauseMeasure;  	/* 0-->AggregateFuncName, 1--> field */  
    private ArrayList<String[]> FromClause; 	/* 0-->TABLE, 1-->customName */
    private ArrayList<String[]> WhereClause;	/* 0-->sqlfld1,1-->op,2-->sqlfld2 */
    private ArrayList<String[]> GroupByClause;
    
    public SqlQuery(){
    	init();
    }
    
 public ArrayList<String[]> getFromClauseArrayList(){
    	return FromClause;
    }
    
 public String[] getSelectClauseMeasure(){
	 return SelectClauseMeasure;
 }   
 
    public ArrayList<String[]> getGroupByClause(){
    	return GroupByClause;
    }
    
 public ArrayList<String[]> getWhereClauseArrayList(){
	 return WhereClause;
    }
    
    private void init(){
    	FromClause=new ArrayList<String[]>();
    	WhereClause=new ArrayList<String[]>();
    	GroupByClause=new ArrayList<String[]>();
    	SelectClauseMeasure=new String[2];
    }
    
    public SqlQuery(String Aggregatefunc,ArrayList<String> Tables,ArrayList<String> Conditions,ArrayList<String> GroupAttr){
    	init();
    	SelectClauseMeasure=Aggregatefunc.split("~");
        for(String table:Tables){
        	FromClause.add(table.split("~"));
        }
        for(String condition:Conditions){
        	WhereClause.add(condition.split("~"));
        }
        for(String grouper:GroupAttr){
        	GroupByClause.add(grouper.split("~"));
        }
        
    }
    
    public SqlQuery(String[] Aggregatefunc,ArrayList<String[]> Tables,ArrayList<String[]> Conditions,ArrayList<String[]> GroupAttr){
    	init();
    	SelectClauseMeasure[0]=Aggregatefunc[0];
    	SelectClauseMeasure[1]=Aggregatefunc[1];
    	FromClause.addAll(Tables);
    	WhereClause.addAll(Conditions);
    	GroupByClause.addAll(GroupAttr);
    }
    public String getSelectClause(){
    	String ret_value="";
    	ret_value+=getGroupClause()+",";
    	ret_value+= SelectClauseMeasure[0]+"("+SelectClauseMeasure[1]+") as measure,COUNT(*) as cnt";
    	return ret_value;
    }
       
    public String getFromClause(){
    	int i=0;
    	String ret_value="";
    	
    	for(String[] x : FromClause ) {    
    		if(i>0) ret_value+=",";
    		ret_value+=mergeStringTable(x);
    		i++;
    	}
    	return ret_value;
    }
    
    public String getWhereClause(){
    	int i=0;
    	String ret_value="";
    	
    	for(String[] x : WhereClause ) {    
    		if(i>0) ret_value+=" AND ";
    		ret_value+=mergeStringTable(x);
    		i++;
    	}
    	return ret_value;
    }
    
    public String getGroupClause(){/*add this to play groupper=1 to select clause*/
    	String ret_value="";
    	int i=0;
    	
    	for(String[] x : GroupByClause ){
    		if(i>0) ret_value+=",";
    		ret_value+=mergeStringTable(x);
    		i++;
    	}
    	
    	return ret_value;
    }
    
   public void printQuery(){
    	System.out.println(toString());
    }
    
    public String toString(){
    	String ret_value="SELECT "+getSelectClause();
    	ret_value+="\nFROM "+ getFromClause();
    	ret_value+="\nWHERE "+getWhereClause();
    	ret_value+="\nGROUP BY "+getGroupClause(); 
    	ret_value+="\n"+getOrderClauseByMeasure(0);
    	if(getGroupClause().length()>0)	return ret_value ;
    	else return "SELECT '"+this.SelectClauseMeasure[0]+"'";
    }
    
    /*  order_type=0 -> ASCENING
     *  order_type=1 -> DESCENDING
     */
    public String getOrderClauseByMeasure(int order_type){
    	if(order_type==0) return " ORDER BY measure ASC";
    	return " ORDER BY measure DESC";
    }
    
    private String mergeStringTable(String[] tomerge){
    	String ret_value="";
    	for(int i=0;i<tomerge.length;i++){
    		ret_value+=" "+tomerge[i];
    	}
    	return ret_value;
    }
    
    @Override
    public void addReturnedFields(String aggregationFuction, String attribute){
    	SelectClauseMeasure[0] =aggregationFuction;
    	SelectClauseMeasure[1]= attribute;
    }
    
    
	@Override
	public boolean compareExtractionMethod(ExtractionMethod toCompare) {
		int count=0;
		SqlQuery tocomp=(SqlQuery)toCompare;
		for(int i=0;i<this.WhereClause.size();i++){
			if(tocomp.WhereClause.get(i)[0]==this.WhereClause.get(i)[0]){
				if(tocomp.WhereClause.get(i)[1]==this.WhereClause.get(i)[1]){
					if(tocomp.WhereClause.get(i)[2]==this.WhereClause.get(i)[2]){
						count++;
					}
				}
			}
		}
		return (count == this.WhereClause.size() ? true : false);
	}

	@Override
	public void addFilter(String[] index) {
		this.WhereClause.add(index);
	}
	
	@Override
	public void addGroupers(String[] index) {
		this.GroupByClause.add(index);
	}
	
	@Override
	public void addSourceCube(String[] index) {
		this.FromClause.add(index);
	}
	
	
}
