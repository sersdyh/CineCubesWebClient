package cubemanager.starschema;

public class Attribute {
	/**
	 * @uml.property  name="name"
	 */
	private String name;
	/**
	 * @uml.property  name="datatype"
	 */
	private String datatype;
    
    public Attribute(String nm,String dt){
    	name = nm;
    	datatype=dt;    			
    }
    
    public String getDatatype(){
    	return datatype;
    }
    
    public String getName(){
    	return name;
    }
    
}
