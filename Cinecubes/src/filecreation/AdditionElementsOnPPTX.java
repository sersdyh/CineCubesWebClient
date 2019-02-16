package filecreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import storymgr.Act;
import storymgr.FinalResult;
import storymgr.Slide;
import storymgr.Story;

public class AdditionElementsOnPPTX {
	
	private FinalResult finalResult;
	private ArrayList<String> fileList;
	private String contentType;
	private String contentTypeNotesDef;
	private String unZipZipTime;
	private String[] slideXml;
	
	public AdditionElementsOnPPTX(FinalResult finalResult, String[] SlideXml) {
		fileList = new ArrayList<>();
		contentTypeNotesDef = "";
		unZipZipTime = "";
		this.finalResult = finalResult;
		this.slideXml = SlideXml;
	}
	
	public String getUnZipZipTime(){
		return unZipZipTime;
	}
	
	public void doWrapUp(Story story) {
		long StartUnzip = System.nanoTime();
		renamePPTXtoZip();
		unZipFiles();
		initializeContentType();
		int slide_so_far_created = 0;
		unZipZipTime = "Unzip Time\t" + (System.nanoTime() - StartUnzip) + "\n";
		for (Act actItem : story.getActs()) {
			if (actItem.getSizeOfEpisodes() > 1 || actItem.getId() == 0
					|| actItem.getId() == -1 || actItem.getId() == 20) {
				for (int j = 0; j < actItem.getSizeOfEpisodes(); j++) {
					Slide slide = (Slide) actItem.getEpisode(j);

					long strTime = System.nanoTime();

					if (slide.getNotes().length() == 0)
						addAudiotoPPTX(j + slide_so_far_created + 2, null,
								slide.getNotes());
					else
						addAudiotoPPTX(j + slide_so_far_created + 2, slide
								.getFilenameAudio(), slide.getNotes());

					slide.addTimeCreationPutInPPTX(System.nanoTime() - strTime);
				}
				slide_so_far_created += actItem.getSizeOfEpisodes();
			}
		}
		long startZip = System.nanoTime();
		writeContentType();
		generateFileList(new File("ppt/unzip"));
		zipFiles();
		renameZiptoPPTX();
		unZipZipTime = "Zip Time\t" + (System.nanoTime() - startZip) + "\n";

	}

	private void renamePPTXtoZip() {
		File oldFile = new File(this.finalResult.getFilename());
		File delFile = new File(this.finalResult.getFilename() + ".zip");
		if (delFile.exists())
			delFile.delete();
		oldFile.renameTo(new File(this.finalResult.getFilename() + ".zip"));
	}
	
	private void unZipFiles() {

		byte[] buffer = new byte[1024];
		try {
			// create output directory is not exists
			File folder = new File("ppt/unzip");
			if (folder.exists())
				deleteFolder(folder);
			folder.mkdir();
			ZipInputStream zis = new ZipInputStream(new FileInputStream(
					this.finalResult.getFilename() + ".zip"));
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File("ppt/unzip" + File.separator + fileName);

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				// buffer=new byte[1024];
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void deleteFolder(File file) {

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					deleteFolder(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}

	private void initializeContentType() {
		this.contentType = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
				+ "<Default Extension=\"png\" ContentType=\"image/png\"/>"
				+ "<Default Extension=\"jpeg\" ContentType=\"image/jpeg\"/>"
				+ "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
				+ "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
				+ "<Default Extension=\"wav\" ContentType=\"audio/wav\"/>"
				+ "<Override PartName=\"/ppt/presentation.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml\"/>"
				+ "<Override PartName=\"/ppt/slideMasters/slideMaster1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideMaster+xml\"/>";
	}
	
	private void addAudiotoPPTX(int slideId, String AudioFilename,
			String NotesTxt) {
		try {
			byte[] noteRelsFrom = new byte[getClass().getClassLoader()
					.getResourceAsStream("resources/notesSlide.xml.rels")
					.available()];
			byte[] noteFrom = new byte[getClass().getClassLoader()
					.getResourceAsStream("resources/notesSlide.xml")
					.available()];
			getClass().getClassLoader()
					.getResourceAsStream("resources/notesSlide.xml.rels")
					.read(noteRelsFrom);
			getClass().getClassLoader()
					.getResourceAsStream("resources/notesSlide.xml")
					.read(noteFrom);
			/* Copy audio and image to ppt/media folder */
			if (AudioFilename != null && !NotesTxt.equals("")) {

				File folder_media = new File("ppt/unzip/ppt/media");
				if (!folder_media.exists()) {
					folder_media.mkdir();
				}

				byte[] pngFrom = new byte[getClass().getClassLoader()
						.getResourceAsStream("resources/play.png").available()];
				getClass().getClassLoader()
						.getResourceAsStream("resources/play.png")
						.read(pngFrom);
				File pngTo1 = new File("ppt/unzip/ppt/media/play.png");
				pngTo1.createNewFile();
				FileOutputStream pngTo = new FileOutputStream(
						"ppt/unzip/ppt/media/play.png");
				pngTo.write(pngFrom);
				pngTo.close();

				File wavFrom = new File(AudioFilename + ".wav");
				File wavTo = new File("ppt/unzip/ppt/media/"
						+ AudioFilename.replace("audio/", "") + ".wav");

				/* Start Copy */
				Files.copy(wavFrom.toPath(), wavTo.toPath(),
						StandardCopyOption.REPLACE_EXISTING);
				wavFrom.deleteOnExit();
				/* End of copy */

			}

			/* Write Notes */
			String relsNotesFilename = "ppt/unzip/ppt/notesSlides/_rels/notesSlide"
					+ String.valueOf(slideId) + ".xml.rels";
			String NotesFilename = "ppt/unzip/ppt/notesSlides/notesSlide"
					+ String.valueOf(slideId) + ".xml";
			(new File(relsNotesFilename)).createNewFile();
			(new File(NotesFilename)).createNewFile();
			FileOutputStream noteRelsTo = new FileOutputStream(
					relsNotesFilename);
			FileOutputStream noteTo = new FileOutputStream(NotesFilename);
			noteRelsTo.write(noteRelsFrom);
			noteRelsTo.close();
			noteTo.write(noteFrom);
			noteTo.close();

			this.replace_papaki(relsNotesFilename, String.valueOf(slideId));
			this.replace_papaki(NotesFilename, NotesTxt);
			contentTypeNotesDef += this.appendContentTypeNotes(slideId);
			/* End of Write the Notes */

			/* Write to Slide the audio */
			FileOutputStream slide = new FileOutputStream(
					"ppt/unzip/ppt/slides/slide" + String.valueOf(slideId)
							+ ".xml");
			try {
				slide.write(slideXml[slideId - 2].getBytes());
				this.appendContentType(slideId);
				slide.close();
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

	}
	
	private void replace_papaki(String nameFile, String toReplace) {
		try {
			File file = new File(nameFile);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null) {
				oldtext += line + "\r\n";
			}
			reader.close();

			// To replace a line in a file
			String newtext = oldtext.replaceAll("@", toReplace);

			FileWriter writer = new FileWriter(nameFile);
			writer.write(newtext);
			writer.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private String appendContentTypeNotes(int slidenumber) {
		return "<Override PartName=\"/ppt/notesSlides/notesSlide"
				+ String.valueOf(slidenumber)
				+ ".xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.notesSlide+xml\"/>";
	}
	

	private void appendContentType(int slidenumber) {
		this.contentType = this.contentType
				.concat("<Override PartName=\"/ppt/slides/slide"
						+ String.valueOf(slidenumber)
						+ ".xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slide+xml\"/>");
	}
	
	private void writeContentType() {
		this.contentType = this.contentType
				+ "<Override PartName=\"/ppt/notesMasters/notesMaster1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.notesMaster+xml\"/>"
				+ "<Override PartName=\"/ppt/presProps.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.presProps+xml\"/>"
				+ "<Override PartName=\"/ppt/viewProps.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.viewProps+xml\"/>"
				+ "<Override PartName=\"/ppt/theme/theme1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.theme+xml\"/>"
				+ "<Override PartName=\"/ppt/tableStyles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.tableStyles+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout3.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout4.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout5.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout6.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout7.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout8.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout9.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout10.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/slideLayouts/slideLayout11.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml\"/>"
				+ "<Override PartName=\"/ppt/theme/theme2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.theme+xml\"/>"
				+ this.contentTypeNotesDef
				+ "<Override PartName=\"/docProps/core.xml\" ContentType=\"application/vnd.openxmlformats-package.core-properties+xml\"/>"
				+ "<Override PartName=\"/docProps/app.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.extended-properties+xml\"/>"
				+ "</Types>";
		try {
			FileOutputStream ConType = new FileOutputStream(
					"ppt/unzip/[Content_Types].xml");
			ConType.write(this.contentType.getBytes());
			ConType.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Traverse a directory and get all files,
	 * and add the file into fileList  
	 * @param node file or directory
	 */
	private void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			fileList.add(node.toString().replace("ppt/uzip", ""));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}

	}
	
	private void zipFiles() {

		byte[] buffer = new byte[2048];
		File delFile = new File(this.finalResult.getFilename() + ".zip");
		if (delFile.exists())
			delFile.delete();
		try {

			FileOutputStream fos = new FileOutputStream(
					this.finalResult.getFilename() + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (String file : this.fileList) {
				if (file.equals("slide1") == false
						&& file.equals("Slide1") == false) {
					ZipEntry ze = new ZipEntry(file.replace("ppt\\unzip\\", "")
							.replace("ppt/unzip/", ""));
					zos.putNextEntry(ze);
					FileInputStream in = new FileInputStream(file);

					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					in.close();
				}
			}

			zos.closeEntry();
			// remember close it
			zos.close();
			fos.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void renameZiptoPPTX() {
		try {
			File oldFile = new File(this.finalResult.getFilename() + ".zip");
			File delFile = new File(this.finalResult.getFilename());
			if (delFile.exists())
				delFile.delete();
			oldFile.renameTo(new File(this.finalResult.getFilename()));
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
