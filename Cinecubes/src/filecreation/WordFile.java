package filecreation;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import storymgr.Slide;
import storymgr.Tabular;

public class WordFile extends FileMgr {

	private XWPFDocument document = new XWPFDocument();

	public 	WordFile() {
		super();
	}

	@Override
	protected void createIntroSlide(Slide episode, int slide_so_far_created) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		XWPFRun run2 = paragraph.createRun();

		run.setBold(true);
		run.setFontSize(24);
		run.setText(episode.getTitle());
		run.addBreak();
		run2.setText(episode.getSubTitle());
		run2.addBreak();
	}
	
	@Override
	protected void createNewSlide(String[][] table, Color[][] colorTable,
			String AudioFilename, String Title, int slideid,
			String titleColumn, String titleRow, String subtitle,
			Tabular tabular, int hide_slide) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		XWPFRun run2 = paragraph.createRun();

		if (table != null) {
			createTable(table, colorTable, titleColumn, titleRow, tabular);
			run.setBold(true);
			run.setFontSize(12);
			run.setText(Title);
			run.addBreak();
		} else {
			run.setBold(true);
			run.setFontSize(24);
			run.setText(Title);
			run.addBreak();

			run2.setText(subtitle);
			run2.addBreak();
		}
	}
	
	private void createTable(String[][] table, Color[][] colorTable,
			String titleColumn, String titleRow, Tabular tabular) {
		int toColorDarkGray = 0;
		XWPFTable pin = document.createTable();
		if (table[0][0].length() > 0 && table[1][0].length() == 0)
			toColorDarkGray = 1;

		for (int i = 0; i < table.length; i++) {
			XWPFTableRow row = pin.createRow();
			for (int j = 0; j < table[0].length; j++) {
				XWPFTableCell cell = row.createCell();
				XWPFParagraph paragraph = cell.addParagraph();
				XWPFRun run = paragraph.createRun();
				run.setFontFamily("Calibri");
				run.setFontSize(12);
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				if (tabular.checkBoldColumn(j))
					run.setBold(true);
				if (tabular.checkBoldRow(i))
					run.setBold(true);
				try {
					int color = Integer.valueOf(colorTable[i][j].getRGB());
					if (color == -16776961) {
						run.setColor("0000FF");// blue
					} else if (color == -65536) {
						run.setColor("FF0000");// red
					} else {
						run.setColor("000000");// black
					}
					run.setText(table[i][j]);

					if (table[i][j].equals(""))
						paragraph.setVerticalAlignment(TextAlignment.CENTER);

					if (toColorDarkGray == 1 && j == 0) {
						run.setItalic(true);
						run.setColor("000000");
					}
					if ((j == 0 && i > 0)
							|| ((j == 0 || j == 1) && toColorDarkGray == 1))
						paragraph.setAlignment(ParagraphAlignment.RIGHT);
					if (j == 0 || ((j == 0 || j == 1) && toColorDarkGray == 1))
						;
					paragraph.setAlignment(ParagraphAlignment.LEFT); 
				} catch (Exception e) {
					run.setText("");
				}
			}
		}
		pin.removeRow(0);
		// table to auto-fit to document page width and aligning that table to
		// center.
		CTTbl tbl = pin.getCTTbl();
		CTTblPr pr = tbl.getTblPr();
		CTTblWidth tblW = pr.getTblW();
		tblW.setW(BigInteger.valueOf(5000));
		tblW.setType(STTblWidth.PCT);
		pr.setTblW(tblW);
		tbl.setTblPr(pr);
		CTJc jc = pr.addNewJc();
		jc.setVal(STJc.RIGHT);
		pr.setJc(jc);

	}
	
	@Override
	protected void addNotesOnSlide(String notes) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setFontSize(12);
		String lines[] = notes.split("\\r?\\n");
		for (int i = 0; i < lines.length; i++) {
			run.setText(lines[i]);
			run.addBreak();
		}	
	}
	
	@Override
	protected void createSummarySlide(Slide episode, int slideId) {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun runTitle = paragraph.createRun();
		XWPFRun runText = paragraph.createRun();

		runTitle.setBold(true);
		runTitle.setFontSize(24);
		runTitle.setText(episode.getTitle());
		runTitle.addBreak();

		String[] findings = episode.getNotes().split("@");
		for (String finding : findings) {
			String[] lines = finding.replace("~~\n", "~~").split("\n");
			if (lines.length == 1)
				continue;
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].contains("~~")) {
					lines[i] = lines[i].replace("~~", "");
				} else if (lines[i].contains("##")) {
					lines[i] = lines[i].replace("##", "");
				}

				runText.setText("-" + lines[i]);
				runText.setFontSize(14);
				runText.addBreak();

			}
		}
		episode.changeNotes();
	}
	
	protected void writeOutput(FileOutputStream fout) throws IOException {
		document.write(fout);
	}
	
}
