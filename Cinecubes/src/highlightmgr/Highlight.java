package highlightmgr;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Highlight {

	
	public int[] countHigher;
	public int[] countLower;
	public int[] countEqual;
	public int[] nullValues;
	public String[] helpValues;
	public int bold;
	
	public ArrayList<Integer> DominateMax;
	public ArrayList<Integer> DominateMin;
	public ArrayList<Integer> DominateMiddle;
	public Integer[] max_appearance_per_color;
	public Integer[] valuePerColor;
	public Integer[][] DominationColor;
	
	public Color maxcolor;
	public String maxcolor_name;
	public Color mincolor;
	public String mincolor_name;
	public Color middlecolor;
	public String middlecolor_name;
	
	public ArrayList<String> semanticValue;
	public ArrayList<Integer> index_checked;
	public ArrayList<Integer> semanticValue_index;
	
	
	public Highlight(){
		maxcolor=Color.RED;
    	maxcolor_name="red";
    	mincolor=Color.blue;
    	mincolor_name="blue";
    	middlecolor=Color.black;
    	middlecolor_name="black";
    	valuePerColor=new Integer[3];
    	max_appearance_per_color=new Integer[3];
	}
	
	abstract public void execute(String[][] table);
}
