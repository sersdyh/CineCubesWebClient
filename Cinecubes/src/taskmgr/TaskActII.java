package taskmgr;

import java.util.HashSet;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;


public class TaskActII extends Task {

	public TaskActII() {
		super();
	}

    public void generateSubTasks(CubeBase cubeBase,CubeQuery cubequery, SubTask OriginSbTsk,
    		String measure){
		/* highlight for Original */
    	addNewSubTask();
    	addCubeQuery(getLastSubTask().createNewExtractionMethod( "II", measure));
		getLastSubTask().execute(cubeBase);
		getSubTasks().add(OriginSbTsk);
		addCubeQuery(cubequery);
		generateSubTasks_per_row(cubeBase);
		generateSubTasks_per_col(cubeBase);
	}
   
	private void generateSubTasks_per_row(CubeBase cubeBase) {
		HashSet<String> col_per_row = new HashSet<String>();
		for (int i = 0; i < cubeQuery.get(1).getSizeRowPivot(); i++) {
			String[][] table = cubeQuery.get(1).getResultArray();
			String Value = cubeQuery.get(1).getValueFromRowPivot(i);

			col_per_row.clear();
			for (int j = 2; j < table.length; j++) {
				if (table[j][1].equals(Value))
					col_per_row.add(table[j][0]);
			}

			String[] todrillinValues = new String[2];
			todrillinValues[1] = Value;
			todrillinValues[0] = col_per_row.toArray()[0].toString();
			CubeQuery newQuery = new CubeQuery(this.cubeQuery.get(1));
			long strTime = System.nanoTime();
			if (cubeQuery.get(1).doDrillInRowVersion(cubeBase, todrillinValues, col_per_row, newQuery)) {
				addSubTask(newQuery, -4, 0, strTime, cubeBase);
				this.getLastSubTask().addDifferenceFromOrigin(i);
				this.getLastSubTask().addDifferenceFromOrigin(1);
			}

		}

	}

	private void generateSubTasks_per_col(CubeBase cubeBase) {
		HashSet<String> row_per_col = new HashSet<String>();
		for (int i = 0; i < cubeQuery.get(1).getSizeColPivot(); i++) {
			String[][] table = cubeQuery.get(1).getResultArray();
			String Value = cubeQuery.get(1).getValueFromColPivot(i);
			row_per_col.clear();
			for (int j = 2; j < table.length; j++) {
				if (table[j][0].equals(Value))
					row_per_col.add(table[j][1]);
			}

			String[] todrillinValues = new String[2];
			todrillinValues[1] = Value;
			todrillinValues[0] = row_per_col.toArray()[0].toString();
			CubeQuery newQuery = new CubeQuery(cubeQuery.get(1));
			long strTime = System.nanoTime();
			if (cubeQuery.get(1).doDrillInColVersion(cubeBase, todrillinValues, row_per_col,newQuery)) {
				addSubTask(newQuery, -5, 0, strTime, cubeBase);
				this.getLastSubTask().addDifferenceFromOrigin(i);
				this.getLastSubTask().addDifferenceFromOrigin(1);
			}

		}

	}
	
}
