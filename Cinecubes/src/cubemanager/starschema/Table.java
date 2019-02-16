package cubemanager.starschema;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table {
   
    private List<Attribute> LstAttr;
    private String tableName;
    
    public Table(String name){
    	tableName=name;
    	LstAttr=new ArrayList<Attribute>();
    }
    
    public void addAllAttribute(Table newtable){
    	LstAttr.addAll(newtable.LstAttr);
    }
    
    public void setAttribute(Connection con){
    	try {
    		DatabaseMetaData meta = con.getMetaData();
    	    ResultSet rsColumns = meta.getColumns(null, null, tableName, null);
    	    while (rsColumns.next()) {
    	    	LstAttr.add(new Attribute(rsColumns.getString("COLUMN_NAME"),rsColumns.getString("TYPE_NAME")));
    	      }
    		
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void printColumns(){
    	for(Attribute item : LstAttr){
    		System.out.println("----"+item.getName()+":"+item.getDatatype());
    	}
    }
    
    public String getTableName(){
    	return tableName;
    }
    
    public Attribute getAttribute(String name){
    	for(Attribute item : LstAttr){
    		if (item.getName().equals(name)) return item;
    	}
    	return null;
    }
}

