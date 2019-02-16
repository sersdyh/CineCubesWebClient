package parsermgr;


import java.util.ArrayList;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class ParserManager {
	
	/**
	 * @uml.property  name="mode"
	 */
	public Integer mode;
	/**
	 * @uml.property  name="name_creation"
	 */
	public String name_creation;
	/**
	 * @uml.property  name="sqltable"
	 */
	public String sqltable;
	/**
	 * @uml.property  name="dimensionlst"
	 */
	public ArrayList<String> dimensionlst;
	/**
	 * @uml.property  name="hierachylst"
	 */
	public ArrayList<String> hierachylst;
	/**
	 * @uml.property  name="originallvllst"
	 */
	public ArrayList<String> originallvllst;
	/**
	 * @uml.property  name="customlvllst"
	 */
	public ArrayList<String> customlvllst;
	/**
	 * @uml.property  name="conditionlst"
	 */
	public ArrayList<String> conditionlst;
	/**
	 * @uml.property  name="tablelst"
	 */
	public ArrayList<String> tablelst;
	/**
	 * @uml.property  name="groupperlst"
	 */
	public ArrayList<String> groupperlst;
	/**
	 * @uml.property  name="measurelst"
	 */
	public ArrayList<String> measurelst;
	/**
	 * @uml.property  name="measurefields"
	 */
	public ArrayList<String> measurefields;
	/**
	 * @uml.property  name="aggregatefunc"
	 */
	public String aggregatefunc;
	
	public ParserManager() {
		dimensionlst=new ArrayList<String>();
		hierachylst=new ArrayList<String>();
		originallvllst=new ArrayList<String>();
		customlvllst=new ArrayList<String>();
		conditionlst=new ArrayList<String>();
		tablelst=new ArrayList<String>();
		groupperlst=new ArrayList<String>();
		measurelst=new ArrayList<String>();
		measurefields=new ArrayList<String>();
		
	}
	
	public void parse(String toParse){
		CharStream stream =	new ANTLRStringStream(toParse);
		CubeSqlLexer lexer = new CubeSqlLexer(stream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		CubeSqlParser parser = new CubeSqlParser(tokenStream);
		dimensionlst.clear();
		hierachylst.clear();
		originallvllst.clear();
		customlvllst.clear();
		try {
			parser.start();	
			
			/*For DIMENSION only*/
			hierachylst.addAll(parser.hierachylst);
			
			/*For CUBE only*/
			measurefields.addAll(parser.measurefields);
			measurelst.addAll(parser.measurelst);
			
			/*SHARED CUBE,DIMENSION */
			dimensionlst.addAll(parser.dimensionlst);
			originallvllst.addAll(parser.originallvllst);
			customlvllst.addAll(parser.customlvllst);
			mode=parser.mode;
			name_creation=parser.name_creation;
			sqltable=parser.sql_table;
			
			/* SQL QUERY STAFF */ 
			aggregatefunc=parser.aggregatefunc;
			conditionlst.addAll(parser.conditionlst);
			tablelst.addAll(parser.tablelst);
			groupperlst.addAll(parser.groupperlst);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}

	}

}
