package cubemanager;

import java.rmi.RemoteException;
import java.util.ArrayList;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Measure;

public class CubeManager {

	/**
	 * @uml.property  name="cBase"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private CubeBase CBase;

	public CubeManager(String lookup) {
		CBase = new CubeBase(lookup);
	}

	public void CreateCubeBase(String filename, String username,
			String password) {
		CBase.registerCubeBase(filename, username, password);
	}

	public void InsertionDimensionLvl(String dimensionName,
			String dimensionTbl, ArrayList<String> fld_Name,
			ArrayList<String> customFld_Name, ArrayList<String> hierachylst) {
		CBase.addDimension(dimensionName);
		CBase.addDimensionTbl(dimensionTbl);
		CBase.setDimensionLinearHierachy(hierachylst, fld_Name, customFld_Name);
	}

	public void InsertionCube(String name_creation, String sqltable,
			ArrayList<String> dimensionlst,
			ArrayList<String> DimemsionRefField, ArrayList<String> measurelst,
			ArrayList<String> measureRefField) {

		CBase.addCube(name_creation);
		CBase.addSqlRelatedTbl(sqltable);
		CBase.setCubeDimension(dimensionlst, DimemsionRefField);
	}

	public CubeQuery createDefaultCubeQuery() {
		CubeQuery cubequery = new CubeQuery("New Request");
		cubequery.setAggregateFunction( "AVG");
		Measure msrToAdd = new Measure(1,"Hrs",CBase.getDatabase().
				getFieldOfSqlTable("adult", "hours_per_week"));
		cubequery.getListMeasure().add(msrToAdd);
		cubequery.addGammaExpression("marital_dim", "lvl0");
		cubequery.addGammaExpression("education_dim", "lvl1");
		cubequery.addSigmaExpression("marital_dim.lvl1", "=",
				"'Partner-absent'");
		cubequery.addSigmaExpression("education_dim.lvl2", "=", "'University'");
		for (BasicStoredCube bsc : CBase.BasicCubes) {
			if (bsc.getName().equals("adult_cube")) {
				cubequery.setBasicStoredCube(bsc);
			}
		}
		return cubequery;
	}
	
	public CubeQuery createCubeQueryFromString(String cubeQstring, String msrname)
			throws RemoteException {
		String[] rows = cubeQstring.trim().split("\n");
		String Cbname = null, nameCQ = null, aggregateFunction = null, measureName = null;
		String[][] sigma = null;
		String[][] gamma = null;
		for (int i = 0; i < rows.length; i++) {
			String[] temp = rows[i].split(":");
			if (temp[0].equals("CubeName")) {
				Cbname = temp[1].trim();
			}
			if (temp[0].equals("Name")) {
				nameCQ = temp[1].trim();
			} else if (temp[0].equals("AggrFunc")) {
				aggregateFunction = temp[1].trim();
			} else if (temp[0].equals("Measure")) {
				measureName = temp[1].trim();
			} else if (temp[0].equals("Gamma")) {
				String[] tmp_gamma = temp[1].split(",");
				gamma = new String[tmp_gamma.length][2];
				for (int j = 0; j < tmp_gamma.length; j++) {
					String[] split_gamma = tmp_gamma[j].trim().split("\\.");
					gamma[j][0] = split_gamma[0];
					gamma[j][1] = split_gamma[1];
				}
			} else if (temp[0].equals("Sigma")) {
				String[] tmp_sigma = temp[1].split(",");
				sigma = new String[tmp_sigma.length][3];
				for (int j = 0; j < tmp_sigma.length; j++) {
					String[] split_sigma = tmp_sigma[j].trim().split("=");
					sigma[j][0] = split_sigma[0];
					sigma[j][1] = "=";
					sigma[j][2] = split_sigma[1];
				}
			}
		}
		CubeQuery cubequery = new CubeQuery(nameCQ);
		cubequery.setAggregateFunction(aggregateFunction);
		/* Must Create Measure In Cube Parser->> I Have Done this */
		/* Search for Measure */
		Measure msrToAdd = new Measure(1,measureName,CBase.getDatabase().getFieldOfSqlTable(Cbname,
				measureName));
		cubequery.getListMeasure().add(msrToAdd);
		msrname = measureName;
		/* Need work to done up here */

		for (int i = 0; i < gamma.length; i++) {
			cubequery.addGammaExpression(gamma[i][0], gamma[i][1]);
		}

		for (int i = 0; i < sigma.length; i++) {
			cubequery.addSigmaExpression(sigma[i][0], sigma[i][1], sigma[i][2]);
		}
		for (BasicStoredCube bsc : CBase.BasicCubes) {
			if (bsc.getName().equals(Cbname + "_cube")) {
				cubequery.setBasicStoredCube(bsc);
			}
		}
		return cubequery;
	}
	
	public CubeBase getCubeBase(){
		return CBase;
	}
	
}
