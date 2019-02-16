package taskmgr;
/*package TaskMgr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import CubeMgr.CubeBase.CubeBase;
import CubeMgr.CubeBase.CubeQuery;
import CubeMgr.StarSchema.Database;
import HelpTask.SqlQuery;

public class TaskFathers extends Task {

	public TaskFathers() {
		super();
	}

	
	/* (non-Javadoc)
	 * @see TaskMgr.Task#generateSubTasks(CubeMgr.CubeBase.CubeBase)
	 *//*
	@Override
    public void generateSubTasks(CubeBase cubeBase,CubeQuery cubequery,
    		SubTask OriginSbTsk, String measure){
    	SubTask stsk=getLastSubTask();
    	SqlQuery Sbsql=(SqlQuery) stsk.getExtractionMethod();
    	ArrayList<String[]> lst=findFathers(cubeBase,Sbsql);
    	
    	for(int i=0;i<lst.size();i++){
    		createSubTask(Sbsql,cubeBase.getDatabase(),lst.get(i),null);
    		for(int j=i;j<lst.size();j++){
    			if(j!=i  && lst.get(i)[1]!=lst.get(j)[1]){
    				createSubTask(Sbsql,cubeBase.getDatabase(),lst.get(i),lst.get(j));
    			}
       		}
    	}
    	printBorderLine();
    	printBorderLine();
    	Sbsql.printQuery();
    }
    
    private ArrayList<String[]> findFathers(CubeBase cubeBase, SqlQuery Original) {
    	printBorderLine();
		System.out.println("Generated Queries");
		printBorderLine();
		ArrayList<String[]> finds=new ArrayList<String[]>();
		for( int i=0;i<Original.getWhereClauseArrayList().size();i++){
			String[] condition=Original.getWhereClauseArrayList().get(i);
			if(condition[2].contains("'") || tryParseInt(condition[2])){
				String[] tmp2=condition[0].split("\\."); //0->table  ,1--> field name
				String table=tmp2[0];
				for(String[] fromTable : Original.getFromClauseArrayList()){
					if(fromTable.length==1 && table.equals(fromTable[0])) break;
					else if(table.equals(fromTable[1]) || table.equals(fromTable[0])) {
							table=fromTable[0];
							
					}
				}
				/*
				 * I must create a function probably to Find fathers
				 * That I do with Cube and Dimension
				 * of tmp2[1] and after must create the tmp_query
				 * SELECT DISTINCT <<father of tmp2[1] >> + " FROM "+table
				 *//*
				//String father
				String tmp_query = "SELECT DISTINCT " + tmp2[1] + " FROM "+ table 
						+ " WHERE " + tmp2[1] + "!=" + condition[2];
				ResultSet rs = cubeBase.getDatabase().executeSql(tmp_query);
				
				try {
					rs.beforeFirst();
					while(rs.next()){
						String newValue = "'" + rs.getString(1) + "'";
						if(tryParseInt(rs.getString(1))) newValue = rs.getString(1);
						String[] toAdd = {newValue,Integer.toString(i)};
						finds.add(toAdd);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return finds;
	}

	private void createSubTask(SqlQuery Sbsql,Database DB,String [] condA,String [] condB){
    	SubTask sbtsk = new SubTask();
		ArrayList<String []> newWhere = new ArrayList<String []>();
		CopyWhereClause(Sbsql.getWhereClauseArrayList(), newWhere);
		newWhere.get(Integer.parseInt(condA[1]))[2] = condA[0];
		if(condB!=null && Integer.parseInt(condB[1]) != Integer.parseInt(condA[1])) 
			newWhere.get(Integer.parseInt(condB[1]))[2] = condB[0];
		SqlQuery newsql = new SqlQuery(Sbsql.getSelectClauseMeasure(),Sbsql.getFromClauseArrayList(),newWhere,Sbsql.getGroupByClause());
		newsql.printQuery();
		sbtsk.setExtractionMethod(newsql);
		sbtsk.addDifferenceFromOrigin(Integer.parseInt(condA[1]));
		if(condB != null && Integer.parseInt(condB[1]) != Integer.parseInt(condA[1])) sbtsk.addDifferenceFromOrigin(Integer.parseInt(condB[1]));
		sbtsk.execute(DB);
		subTasks.add(sbtsk);
    }

	void printSqlQueryArrayList(ArrayList<SqlQuery> toprint){
		for(SqlQuery x : toprint) x.printQuery();
	}
	
	void printStringArrayList(ArrayList<String> toprint){
		printBorderLine();
		for(String x: toprint) {
			System.out.println(x);
		}
		printBorderLine();
	}
	
	boolean tryParseInt(String value){
		boolean ret_value = true;
	    try{
	    	Integer.parseInt(value);
	    }catch(NumberFormatException nfe){  
	          ret_value = false;
	    }  
	     	return ret_value;  
	}
	
	void printBorderLine(){
    	System.out.println("=====================================");
    }
	
	void CopyWhereClause(ArrayList<String[]> from,ArrayList<String[]> to){
		for (int i = 0; i < from.size(); i++){
			String[] old = from.get(i);
			String[] toadd = new String[old.length];
			for (int j = 0; j < old.length; j++){
				toadd[j] = old[j];
			}
			to.add(toadd);
		}
	}

}
*/