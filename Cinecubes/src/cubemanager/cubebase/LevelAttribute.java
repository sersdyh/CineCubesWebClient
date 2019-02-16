package cubemanager.cubebase;

import cubemanager.starschema.Attribute;

public class LevelAttribute{
    
	/**
	 * @uml.property  name="nameLvlAttr"
	 */
	private String nameLvlAttr;
	/**
	 * @uml.property  name="moreinfos"
	 */
	private String Moreinfos;
    /**
	 * @uml.property  name="level"
	 * @uml.associationEnd  inverse="lvlAttributes:CubeMgr.CubeBase.Level"
	 */
    private Level level;
    /**
	 * @uml.property  name="attribute"
	 * @uml.associationEnd  
	 */
    private Attribute attribute;
  
    public LevelAttribute(String name,String infos){
    	setNameLvlAttr(name);
    	setMoreinfos(infos);
    }
    
	/**
	 * @return
	 * @uml.property  name="nameLvlAttr"
	 */
	public String getNameLvlAttr() {
		return nameLvlAttr;
	}

	/**
	 * @param nameLvlAttr
	 * @uml.property  name="nameLvlAttr"
	 */
	public void setNameLvlAttr(String nameLvlAttr) {
		this.nameLvlAttr = nameLvlAttr;
	}

	/**
	 * @return
	 * @uml.property  name="moreinfos"
	 */
	public String getMoreinfos() {
		return Moreinfos;
	}

	/**
	 * @param moreinfos
	 * @uml.property  name="moreinfos"
	 */
	public void setMoreinfos(String moreinfos) {
		Moreinfos = moreinfos;
	}


	/**
	 * @return
	 * @uml.property  name="level"
	 */
	public Level getLevel() {
		return level;
	}


	/**
	 * @param level
	 * @uml.property  name="level"
	 */
	public void setLevel(Level level) {
		this.level = level;
	}


	/**
	 * @param attribute
	 * @uml.property  name="attribute"
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return
	 * @uml.property  name="attribute"
	 */
	public Attribute getAttribute() {
		return attribute;
	}
    
}
