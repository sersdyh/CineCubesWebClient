package cubemanager.cubebase;

import java.util.ArrayList;

import cubemanager.starschema.DimensionTable;

public class Dimension{
    /**
	 * @uml.property  name="hierachy"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="CubeMgr.CubeBase.Level"
	 */
    private ArrayList<Hierarchy> hierachy;
    /**
	 * @uml.property  name="dimTbl"
	 * @uml.associationEnd  
	 */
    private DimensionTable DimTbl;
    /**
	 * @uml.property  name="name"
	 */
    private String name;
    
    public Dimension(String nameDim){
    	name=nameDim;
    	hierachy=new ArrayList<Hierarchy>();
    }
    
    public boolean hasSameName(String dimensionName){
    	return name.equals(dimensionName);
    }
    
    /**
	 * @return
	 * @uml.property  name="dimTbl"
	 */
    public String getTableName() {
            return DimTbl.getTableName();
    }

    /**
	 * @param dimTbl
	 * @uml.property  name="dimTbl"
	 */
    public void setDimTbl(DimensionTable dimTbl) {
            DimTbl = dimTbl;
    }

    public ArrayList<Hierarchy> getHier() {
            return hierachy;
    }

    public void setHier(ArrayList<Hierarchy> hier) {
    	hierachy = hier;
    }    
}
