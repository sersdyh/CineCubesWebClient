package highlightmgr;

public class HighlightCompareColumn extends Highlight {

	public HighlightCompareColumn() {
		super();
	}

	public void execute(String[][] PivotTable) {
		countHigher = new int[PivotTable[0].length - 1];
		countLower = new int[PivotTable[0].length - 1];
		countEqual = new int[PivotTable[0].length - 1];
		nullValues = new int[PivotTable[0].length - 1];
		helpValues = new String[PivotTable[0].length - 1];

		for(int i = 0; i < PivotTable.length; i++) {
			for(int j = 0; j < PivotTable[i].length; j++) {
				PivotTable[i][j] = PivotTable[i][j].replace(",", ".");
			}
		}

		for (int j = 1; j < PivotTable[0].length; j++) {
			countHigher[j - 1] = 0;
			countLower[j - 1] = 0;
			helpValues[j - 1] = PivotTable[0][j];
			if (j != this.bold) {
				for (int i = 1; i < PivotTable.length; i++) {
					if (!PivotTable[i][j].equals("-")
							&& !PivotTable[i][this.bold].equals("-")) {
						if (Double.parseDouble(PivotTable[i][this.bold]) > Double
								.parseDouble(PivotTable[i][j])) {
							countHigher[j - 1]++;
						} else if (Double.parseDouble(PivotTable[i][this.bold]) <
								Double.parseDouble(PivotTable[i][j])) {
							countLower[j - 1] ++;
						} else {
							countEqual[j - 1]++;
						}
					} else {
						nullValues[j - 1]++;
					}
				}
			} else {
				for(int i = 1; i < PivotTable.length; i++) {
					if(PivotTable[i][j].equals("-")) {
						nullValues[j - 1]++;
					}
				}
			}
		}
	}
}
