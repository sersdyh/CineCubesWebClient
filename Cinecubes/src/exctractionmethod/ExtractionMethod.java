package exctractionmethod;

import java.sql.ResultSet;
import java.util.TreeSet;

/**
 * @author  Asterix
 */
public abstract class ExtractionMethod {
    
    private Result Res;
    
    public ExtractionMethod(){
    	Res = new Result();
    }

   public boolean createResultArray(ResultSet resultSet) {
	   	Res=new Result();
   		return Res.createResultArray(resultSet);
   }
     
   public Result getResult() {
	   return Res;
   }
    abstract public String toString();

    abstract public boolean compareExtractionMethod(ExtractionMethod toCompare);
    
    
    public String[][] getResultArray(){
    	return Res.getResultArray();
    }
    
    
    public TreeSet<String> getRowPivot(){
    	return Res.getRowPivot();
    }
    
    public TreeSet<String> getColPivot(){
    	return Res.getColPivot();
    }
    
    public String getTitleosColumns(){
		return Res.getTitleosColumns();
	}
    
    abstract public void addReturnedFields(String aggregationFuction, String attribute);
    abstract public void addFilter(String[] index);
    abstract public void addGroupers(String[] index);
    abstract public void addSourceCube(String[] index);
    
  public boolean isEmptyResultArray(){
	  return  Res.getResultArray() == null;
  }
}
