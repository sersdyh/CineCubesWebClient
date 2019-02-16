package cubemanager.starschema;

import java.util.List;

public class FactTable extends Table {
    
	/**
	 * @uml.property  name="dimTable"
	 */
	private List<DimensionTable> DimTable;
    
    public FactTable(String name) {
		super(name);
	}
    
	public List<DimensionTable> getDimTable() {
		return DimTable;
	}
	
	public void setDimTable(List<DimensionTable> dimTable) {
		DimTable = dimTable;
	}

}
