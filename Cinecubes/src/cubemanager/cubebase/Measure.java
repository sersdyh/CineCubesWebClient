package cubemanager.cubebase;

import cubemanager.starschema.Attribute;

public class Measure{
    /**
	 * @uml.property  name="id"
	 */
    private Integer id;
    /**
	 * @uml.property  name="name"
	 */
    private String name;
    /**
	 * @uml.property  name="attr"
	 * @uml.associationEnd  
	 */
    private Attribute attribute;
    
    public Attribute getAttribute() {
    	return attribute;
    }
    
    public String getName() {
    	return name;
    }
    
    public Measure( Integer id ,String name, Attribute attribute) {
    	this.id = id;
    	this.name = name;
    	this.attribute = attribute;
    }
  
    public Measure(){    	
    }
}
