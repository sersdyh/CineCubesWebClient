package cubemanager.cubebase;

import java.util.ArrayList;

public class Level {
    /**
	 * @uml.property  name="hierarchy"
	 * @uml.associationEnd  inverse="lvls:CubeMgr.CubeBase.Hierarchy"
	 */
    private Hierarchy hierarchy;
    /**
	 * @uml.property  name="lvlAttributes"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="level:CubeMgr.CubeBase.LevelAttribute"
	 */
    private ArrayList<LevelAttribute> lvlAttributes;
    /**
	 * @uml.property  name="id"
	 */
    private Integer id;
    /**
	 * @uml.property  name="name"
	 */
    private String name;

    public String getAttributeName(int i){
    	return lvlAttributes.get(i).getAttribute().getName();
    }
    
    public String getName() {
    	return name;
    }
    
    public Level(Integer position,String nm,Hierarchy Hier){
    	id=position;
    	name=nm;
    	lvlAttributes=new ArrayList<LevelAttribute>();
    	setHierarchy(Hier);
    }

    public Level(Integer position,String nm){
    	id=position;
    	name=nm;
    	lvlAttributes=new ArrayList<LevelAttribute>();
    }
        
    public void setLevelAttribute(ArrayList<LevelAttribute> levelAttributes){
    	this.lvlAttributes=levelAttributes;
    }

	public Hierarchy getLinearHierarchy() {
		return hierarchy;
	}

	/**
	 * @param hier
	 * @uml.property  name="hierarchy"
	 */
	public void setHierarchy(Hierarchy hier) {
		hierarchy = hier;
	}

	public void addLevelAttribute(LevelAttribute lvlattribute) {
		lvlAttributes.add(lvlattribute);
	}
}
