package filecreation;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.TextAlign;
import org.apache.poi.xslf.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.xmlbeans.XmlObject;

import storymgr.Slide;
import storymgr.Story;
import storymgr.Tabular;

public class PptxFile extends FileMgr {

	private XMLSlideShow slideShowPPTX;
	private XSLFSlideMaster defaultMaster;
	private String[] slideXml;
	private AdditionElementsOnPPTX element;
	private String unZipZipTime;

	public PptxFile() {
		
		unZipZipTime = "";
		InputStream file = getClass().getClassLoader().getResourceAsStream(
				"resources/notes.pptx");
		try {
			slideShowPPTX = new XMLSlideShow(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		defaultMaster = slideShowPPTX.getSlideMasters()[0];
	}

	@Override
	public void createFile(Story story) {
		slideXml = new String[story.getNumberOfSlides()];
		super.createFile(story);
		 element = new AdditionElementsOnPPTX(getFinalResult(),slideXml);
		element.doWrapUp(story);
	}
	
	protected void writeOutput(FileOutputStream fout) throws IOException {
		slideShowPPTX.removeSlide(0);
		slideShowPPTX.write(fout);
	}
	
	protected void createIntroSlide(Slide episode, int count) {
		slideXml[count] = "";
		XSLFSlide slide;
		slide = slideShowPPTX.createSlide(defaultMaster
				.getLayout(SlideLayout.TITLE));

		XSLFTextShape title1 = slide.getPlaceholder(0);
		title1.clearText();
		XSLFTextParagraph paragraph = title1.addNewTextParagraph();
		XSLFTextRun tltTxtRun = paragraph.addNewTextRun();
		tltTxtRun.setText(episode.getTitle());
		paragraph.setTextAlign(TextAlign.LEFT);

		XSLFTextShape title2 = slide.getPlaceholder(1);
		title2.clearText();
		XSLFTextParagraph paragraph2 = title2.addNewTextParagraph();
		XSLFTextRun tltTxtRun2 = paragraph2.addNewTextRun();

		tltTxtRun2.setBold(false);
		tltTxtRun2.setText(episode.getSubTitle());
		tltTxtRun2.setFontSize(20);
		paragraph2.setTextAlign(TextAlign.JUSTIFY);

		setRelationshipForNotes(slide, 2);

		createSlideWithXMlAudio(slide, episode.getFilenameAudio(), 2, 1);
	}

	protected void createSummarySlide(Slide episode, int slideId) {
		XSLFSlide slide;
		slide = slideShowPPTX.createSlide(defaultMaster
				.getLayout(SlideLayout.TITLE_AND_CONTENT));
		XSLFTextShape title1 = slide.getPlaceholder(0);
		title1.setText(episode.getTitle());

		XSLFTextShape title2 = slide.getPlaceholder(1);
		title2.clearText();

		String[] findings = episode.getNotes().split("@");
		for (String finding : findings) {
			String[] lines = finding.replace("~~\n", "~~").split("\n");

			int lvl1 = 1;
			if (lines.length == 1)
				continue;
			for (int i = 0; i < lines.length; i++) {
				XSLFTextParagraph p = title2.addNewTextParagraph();
				if (lines[i].contains("~~")) {
					lvl1 = 2;
					lines[i] = lines[i].replace("~~", "");
				} else if (lines[i].contains("##")) {
					lvl1 = 1;
					lines[i] = lines[i].replace("##", "");
				}
				p.setBullet(true);
				if (i == 0)
					p.setLevel(0);
				else
					p.setLevel(lvl1);

				XSLFTextRun tltTxtRun = p.addNewTextRun();
				tltTxtRun.setBold(false);
				tltTxtRun.setText(lines[i]);
				tltTxtRun.setFontSize(14);

			}
		}
		episode.changeNotes();
		setRelationshipForNotes(slide, slideId);
		createSlideWithXMlAudio(slide, episode.getFilenameAudio(),
				slideId, 1);
	}

	protected void createNewSlide(String[][] table, Color[][] colorTable,
			String AudioFilename, String Title, int slideid,
			String titleColumn, String titleRow, String subtitle,
			Tabular tabular, int hide_slide) {
		slideXml[slideid - 2] = "";
		XSLFSlideLayout titleLayout = defaultMaster
				.getLayout(SlideLayout.TITLE_ONLY);
		XSLFSlide slide;

		if (table != null) {
			slide = slideShowPPTX.createSlide(titleLayout);
			slide.setFollowMasterGraphics(true);

			setRelationshipForNotes(slide, slideid);

			createTableInSlide(slide, slideShowPPTX.getPageSize(), table,
					colorTable, titleColumn, titleRow, tabular);
			this.setTitle(slide, Title, 16.0, true);
		} else {
			slide = slideShowPPTX.createSlide(defaultMaster
					.getLayout(SlideLayout.TITLE));

			setRelationshipForNotes(slide, slideid);
			XSLFTextShape title1 = slide.getPlaceholder(0);
			title1.setText(Title);

			XSLFTextShape title2 = slide.getPlaceholder(1);
			title2.clearText();
			XSLFTextParagraph p = title2.addNewTextParagraph();

			XSLFTextRun tltTxtRun = p.addNewTextRun();

			tltTxtRun.setBold(false);
			tltTxtRun.setText(subtitle);
			tltTxtRun.setFontSize(20);
			p.setTextAlign(TextAlign.JUSTIFY);

		}
		createSlideWithXMlAudio(slide, AudioFilename, slideid, hide_slide);

	}

	private void setRelationshipForNotes(XSLFSlide slide, int slideid) {
		String NotesRelationShip = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/notesSlide";
		URI uri = null;
		try {
			uri = new URI("../notesSlides/notesSlide" + slideid + ".xml");
		} catch (URISyntaxException ex) {
			System.out.println(ex.getMessage());
		}
		PackageRelationship addRelationship = slide.getPackagePart()
				.addRelationship(uri, TargetMode.INTERNAL, NotesRelationShip);
		slide.addRelation(addRelationship.getId(), slide);
	}

	/*
	 * rId1 -->MEDIA relationship ID rId2 -->AUDIO relationship ID rId4 -->IMAGE
	 * relationship ID
	 */
	private XSLFTable createTableInSlide(XSLFSlide slide,
			java.awt.Dimension pgsize, String[][] table, Color[][] colorTable,
			String titleColumn, String titleRow, Tabular tabular) {

		int page_width = pgsize.width; // slide width
		int toColorDarkGray = 0;
		XSLFTable tbl = slide.createTable();
		Color other = Color.black;
		if (table[0][0].length() > 0 && table[1][0].length() == 0)
			toColorDarkGray = 1;

		for (int i = 0; i < table.length; i++) {
			XSLFTableRow addRow = tbl.addRow();
			for (int j = 0; j < table[0].length; j++) {
				XSLFTableCell cell = addRow.addCell();
				XSLFTextParagraph p = cell.addNewTextParagraph();
				XSLFTextRun r = p.addNewTextRun();
				p.setTextAlign(TextAlign.CENTER);

				cell.setTopInset(0);
				cell.setLeftInset(0);
				cell.setRightInset(0);
				cell.setBottomInset(0);
				r.setFontSize(12);
				if (tabular.checkBoldColumn(j))
					r.setBold(true);
				if (tabular.checkBoldRow(i))
					r.setBold(true);
				try {
					r.setFontColor(colorTable[i][j]);
					r.setText(table[i][j]);
					if (table[i][j].equals(""))
						cell.setVerticalAlignment(VerticalAlignment.MIDDLE);

					if ((i == 0 || j == toColorDarkGray)
							&& !(i == 0 && j == toColorDarkGray)) {
						r.setFontColor(other);
					}
					if (toColorDarkGray == 1 && j == 0) {
						r.setItalic(true);
						r.setFontColor(Color.black);
					}
					if ((j == 0 && i > 0)
							|| ((j == 0 || j == 1) && toColorDarkGray == 1))
						p.setTextAlign(TextAlign.RIGHT);
					if (j == 0 || ((j == 0 || j == 1) && toColorDarkGray == 1))
						cell.setLeftInset(0.5);
				} catch (Exception e) {
					r.setText("");
				}
			}
		}
		double table_width = 0;
		for (int k = 0; k < tbl.getNumberOfColumns(); k++) {
			table_width += tbl.getColumnWidth(k);
		}
		tbl.setAnchor(new Rectangle2D.Double(
				((page_width / 2) - table_width / 2), 100, 100, 100));

		return tbl;
	}

	private void createSlideWithXMlAudio(XSLFSlide slide, String AudioFilename,
			int slideid, int hide_slide) {
		String AudioRelationShip = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/audio";
		String MediaRelationShip = "http://schemas.microsoft.com/office/2007/relationships/media";
		String ImageRelationShip = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
		PackagePart packagePart = slide.getPackagePart();
		XmlObject xmlObject = slide.getXmlObject();
		try {
			if (AudioFilename != null) {
				URI uri = null;
				URI uri2 = null;
				try {
					String tmp = AudioFilename.replace("audio/", "");
					uri = new URI("../media/" + tmp + ".wav");
					uri2 = new URI("../media/play.png");
				} catch (URISyntaxException ex) {
					System.out.println(ex.getMessage());
				}

				PackageRelationship addRelationship2 = packagePart
						.addRelationship(uri, TargetMode.INTERNAL,
								MediaRelationShip);
				PackageRelationship addRelationship1 = packagePart
						.addRelationship(uri, TargetMode.INTERNAL,
								AudioRelationShip);
				PackageRelationship addRelationship3 = packagePart
						.addRelationship(uri2, TargetMode.INTERNAL,
								ImageRelationShip);
				slide.addRelation(addRelationship3.getId(), slide);
				slide.addRelation(addRelationship2.getId(), slide);
				slide.addRelation(addRelationship1.getId(), slide);

				int id = 4;
				if (slideid - 2 == 0) {
					String SoundNode = this.getSoundNodeString(
							addRelationship2.getId(), addRelationship1.getId(),
							addRelationship3.getId(), AudioFilename, id++);
					try {
						byte[] pictureData = new byte[getClass()
								.getClassLoader()
								.getResourceAsStream("resources/cube_dali.png")
								.available()];
						getClass().getClassLoader()
								.getResourceAsStream("resources/cube_dali.jpg")
								.read(pictureData);
						int idx = slideShowPPTX.addPicture(pictureData,
								XSLFPictureData.PICTURE_TYPE_JPEG);
						XSLFPictureShape pic = slide.createPicture(idx);
						pic.setAnchor(new Rectangle2D.Double(slideShowPPTX
								.getPageSize().getWidth() - 300, 0, 300, 300));
						xmlObject = slide.getXmlObject();
					} catch (IOException e) {
						System.err.print("In add picture:");
						e.printStackTrace();
					}
					String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
							+ xmlObject
									.toString()
									.replace("main1", "a")
									.replace("<main", "<p")
									.replace("</main", "</p")
									.replace(":main=", ":p=")
									.replace("rel=", "r=")
									.replace("rel:", "r:")
									.replace("xml-fragment", "p:sld")
									.replace("</p:spTree>",
											SoundNode + "</p:spTree>");
					test = test.replace("<p:cSld>", "<p:cSld>"
							+ setBackroundToSlide());
					slideXml[slideid - 2] = test.replace("</p:sld>",
							getAutoSlideShow() + getTimingNode(id++) + "</p:sld>");
				} else {
					String SoundNode = this.getSoundNodeString(
							addRelationship2.getId(), addRelationship1.getId(),
							addRelationship3.getId(), AudioFilename, id);
					String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
							+ xmlObject
									.toString()
									.replace("main1", "a")
									.replace("<main", "<p")
									.replace("</main", "</p")
									.replace(":main=", ":p=")
									.replace("rel=", "r=")
									.replace("rel:", "r:")
									.replace("xml-fragment", "p:sld")
									.replace("</p:spTree>",
											SoundNode + "</p:spTree>");
					test = test.replace("<p:cSld>", "<p:cSld>"
							+ setBackroundToSlide());
					slideXml[slideid - 2] = test
							.replace(
									"</p:sld>",
									getAutoSlideShow() + getTimingNode(id)
											+ "</p:sld>")
							.replace(
									"<p:sld ",
									"<p:sld xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" show=\""
											+ String.valueOf(hide_slide)
											+ "\" ");
				}
			} else {
				String test = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ xmlObject.toString().replace("main1", "a")
								.replace("<main", "<p")
								.replace("</main", "</p")
								.replace(":main=", ":p=").replace("rel=", "r=")
								.replace("rel:", "r:")
								.replace("xml-fragment", "p:sld");
				slideXml[slideid - 2] = test.replace("</p:sld>",
						getAutoSlideShow() + "</p:sld>").replace("<p:sld ",
						"<p:sld show=\"" + String.valueOf(hide_slide) + "\" ");
				;
			}

		} catch (Exception ex) {
			System.err.print("In add audio:");
			System.err.println(ex.getMessage());
		}
	}

	private String setBackroundToSlide() {
		return  "	<p:bg>" + "			<p:bgPr>\n"
				+ "				<a:solidFill>" + "					<a:srgbClr val=\"" +  "EBE9E9"
				+ "\"/>" + "				</a:solidFill>" + "				<a:effectLst/>"
				+ "			</p:bgPr>" + "		</p:bg>";
	}

	private String getSoundNodeString(String rId1, String rId2, String rId4,
			String audioFilename, int id) {

		return "<p:pic>" + "<p:nvPicPr>" + "<p:cNvPr id=\""
				+ String.valueOf(id)
				+ "\" name=\""
				+ audioFilename.replace("audio/", "").concat(".wav")
				+ "\">"
				+ "<a:hlinkClick r:id=\"\" action=\"ppaction://media\"/>"
				+ "</p:cNvPr>"
				+ "<p:cNvPicPr>"
				+ "<a:picLocks noChangeAspect=\"1\"/>"
				+ "</p:cNvPicPr>"
				+ "<p:nvPr>"
				+ "<a:audioFile r:link=\""
				+ rId2
				+ "\"/>"
				+ "<p:extLst>"
				+ "<p:ext uri=\"{DAA4B4D4-6D71-4841-9C94-3DE7FCFB9230}\">"
				+ "<p14:media xmlns:p14=\"http://schemas.microsoft.com/office/powerpoint/2010/main\" r:embed=\""
				+ rId1 + "\"/>" + "</p:ext>" + "</p:extLst>" + "</p:nvPr>"
				+ "</p:nvPicPr>" + "<p:blipFill>" + "<a:blip r:embed=\"" + rId4
				+ "\"/>" + "<a:stretch>" + "<a:fillRect/>" + "</a:stretch>"
				+ "</p:blipFill>" + "<p:spPr>" + "<a:xfrm>"
				+ "<a:off x=\"0\" y=\"0\"/>"
				+ "<a:ext cx=\"609600\" cy=\"609600\"/>" + "    </a:xfrm>"
				+ "<a:prstGeom prst=\"rect\">" + "<a:avLst/>" + "</a:prstGeom>"
				+ "</p:spPr>" + "</p:pic>";
	}

	private String getTimingNode(int id) {
		return "<p:timing>"
				+ "<p:tnLst>"
				+ "<p:par>"
				+ "<p:cTn id=\"1\" dur=\"indefinite\" restart=\"never\" nodeType=\"tmRoot\">"
				+ "<p:childTnLst>"
				+ "<p:seq concurrent=\"1\" nextAc=\"seek\">"
				+ "<p:cTn id=\"2\" dur=\"indefinite\" nodeType=\"mainSeq\">"
				+ "<p:childTnLst>"
				+ "<p:par>"
				+ "<p:cTn id=\"3\" fill=\"hold\">"
				+ "<p:stCondLst>"
				+ "<p:cond delay=\"indefinite\"/>"
				+ "<p:cond evt=\"onBegin\" delay=\"0\">"
				+ "<p:tn val=\"2\"/>"
				+ "</p:cond>"
				+ "</p:stCondLst>"
				+ "<p:childTnLst>"
				+ "<p:par>"
				+ "<p:cTn id=\"4\" fill=\"hold\">"
				+ "<p:stCondLst>"
				+ "<p:cond delay=\"0\"/>"
				+ "</p:stCondLst>"
				+ "<p:childTnLst>"
				+ "<p:par>"
				+ "<p:cTn id=\"5\" presetID=\"1\" presetClass=\"mediacall\" presetSubtype=\"0\" fill=\"hold\" nodeType=\"afterEffect\">"
				+ "<p:stCondLst>" + "<p:cond delay=\"0\"/>" + "</p:stCondLst>"
				+ "<p:childTnLst>"
				+ "<p:cmd type=\"call\" cmd=\"playFrom(0.0)\">" + "<p:cBhvr>"
				+ "<p:cTn id=\"6\" dur=\"4169\" fill=\"hold\"/>" + "<p:tgtEl>"
				+ "<p:spTgt spid=\""
				+ String.valueOf(id)
				+ "\"/>"
				+ "</p:tgtEl>"
				+ "</p:cBhvr>"
				+ "</p:cmd>"
				+ "</p:childTnLst>"
				+ "</p:cTn>"
				+ "</p:par>"
				+ "</p:childTnLst>"
				+ "</p:cTn>"
				+ "</p:par>"
				+ "</p:childTnLst>"
				+ "</p:cTn>"
				+ "</p:par>"
				+ "</p:childTnLst>"
				+ "</p:cTn>"
				+ "<p:prevCondLst>"
				+ "<p:cond evt=\"onPrev\" delay=\"0\">"
				+ "<p:tgtEl>"
				+ "<p:sldTgt/>"
				+ "</p:tgtEl>"
				+ "</p:cond>"
				+ "</p:prevCondLst>"
				+ "<p:nextCondLst>"
				+ "<p:cond evt=\"onNext\" delay=\"0\">"
				+ "<p:tgtEl>"
				+ "<p:sldTgt/>"
				+ "</p:tgtEl>"
				+ "</p:cond>"
				+ "</p:nextCondLst>"
				+ "</p:seq>"
				+ "<p:audio>"
				+ "<p:cMediaNode vol=\"100000\">"
				+ "<p:cTn id=\"7\" fill=\"hold\" display=\"0\">"
				+ "<p:stCondLst>"
				+ "<p:cond delay=\"indefinite\"/>"
				+ "</p:stCondLst>"
				+ "<p:endCondLst>"
				+ "<p:cond evt=\"onStopAudio\" delay=\"0\">"
				+ "<p:tgtEl>"
				+ "<p:sldTgt/>"
				+ "</p:tgtEl>"
				+ "</p:cond>"
				+ "</p:endCondLst>"
				+ "</p:cTn>"
				+ "<p:tgtEl>"
				+ "<p:spTgt spid=\""
				+ String.valueOf(id)
				+ "\"/>"
				+ "</p:tgtEl>"
				+ "</p:cMediaNode>"
				+ "</p:audio>"
				+ "</p:childTnLst>"
				+ "</p:cTn>"
				+ "</p:par>"
				+ "</p:tnLst>"
				+ "</p:timing>";
		 
	}

	private String getAutoSlideShow() {
		return "<mc:AlternateContent xmlns:mc=\"http://schemas.openxmlformats.org/markup-compatibility/2006\">"
				+ "<mc:Choice xmlns:p14=\"http://schemas.microsoft.com/office/powerpoint/2010/main\" Requires=\"p14\">"
				+ "<p:transition p14:dur=\"100\" advClick=\"0\" advTm=\"10000\">"
				+ "<p:cut/>"
				+ "</p:transition>"
				+ "</mc:Choice>"
				+ "<mc:Fallback xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"\">"
				+ "<p:transition advClick=\"0\" advTm=\"10000\">"
				+ "<p:cut/>"
				+ "</p:transition>"
				+ "</mc:Fallback>"
				+ "</mc:AlternateContent>";
	}
	
	/* Set Title In Slide */
	private void setTitle(XSLFSlide slide, String title, double fontSize, boolean bold) {
		XSLFTextShape oldTitle = slide.getPlaceholder(0);
		oldTitle.clearText();
		XSLFTextRun tltTxtRun = oldTitle.addNewTextParagraph().addNewTextRun();
		tltTxtRun.setFontFamily("Arial");
		tltTxtRun.setBold(bold);
		tltTxtRun.setFontSize(fontSize);
		tltTxtRun.setText(title);
	}

	@Override
	protected void addNotesOnSlide(String notes) {	}

	public String getunZipZipTime(){
		return unZipZipTime ;
	}
	
}