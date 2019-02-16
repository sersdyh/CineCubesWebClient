package cubemanager.cubebase;

import cubemanager.starschema.FactTable;

public class BasicStoredCube extends Cube {
    
	/**
	 * @uml.property  name="fCtbl"
	 * @uml.associationEnd  
	 */
	private FactTable FCtbl;
        
	public BasicStoredCube(String NAME) {
			super(NAME);
	}
	 
	public void setFactTable(FactTable Factbl){
		FCtbl=Factbl;
	}

	public FactTable FactTable() {
		return FCtbl;
	}

}
