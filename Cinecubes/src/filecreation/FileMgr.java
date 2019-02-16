package filecreation;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import storymgr.Act;
import storymgr.FinalResult;
import storymgr.Slide;
import storymgr.Story;
import storymgr.Tabular;

public abstract class FileMgr {
    /**
	 * @uml.property  name="finalResult"
	 * @uml.associationEnd  
	 */
    protected FinalResult finalResult;
     
	public FinalResult getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(FinalResult finalresult) {
		finalResult = finalresult;
	}
   
	public void createFile(Story story){
		int slide_so_far_created = 0;
		for (Act actItem : story.getActs()) {
			if (actItem.getId() == 0) {
				
				Slide slide = (Slide) actItem.getEpisode(0);
				slide.subTimeCreationPutInPPTX(System.nanoTime());
					
				createIntroSlide(slide,slide_so_far_created);
				slide.subTimeCreationPutInPPTX(System.nanoTime());
				slide_so_far_created += actItem.getSizeOfEpisodes();
			} else if (actItem.getId() == -1) {
				Slide slide = (Slide) actItem.getEpisode(0);
				slide.setTimeCreationPutInPPTX(System.nanoTime());
				createSummarySlide(slide, slide_so_far_created + 2);
				slide.subTimeCreationPutInPPTX(System.nanoTime());
				slide_so_far_created += actItem.getSizeOfEpisodes();
			} else if (actItem.getSizeOfEpisodes() > 1
					|| actItem.getId() == 20) {
				for (int j = 0; j < actItem.getSizeOfEpisodes(); j++) {
					
					Slide slide = (Slide) actItem.getEpisode(j);

					slide.setTimeCreationPutInPPTX(System.nanoTime());

					if (slide.getTitle().contains("Act"))
						createNewSlide(null, null, slide.getFilenameAudio(),
							 slide.getTitle(), j + slide_so_far_created + 2,
							 null, null, slide.getSubTitle(), null,
								(actItem.getId() == 3 ? 0 : 1));
					else if (slide.getNotes().length() == 0) {
						Tabular tmp_tbl = ((Tabular) slide.getVisual());
						createNewSlide(slide.getVisual().getPivotTable(),
								tmp_tbl.getColorTable(), null, slide.getTitle(), j
										+ slide_so_far_created + 2, null, null,
								slide.getSubTitle(),
								(Tabular) slide.getVisual(),
								(actItem.getId() == 3 ? 0 : 1));
					} else {
						Tabular tmp_tbl = ((Tabular) slide.getVisual());
						createNewSlide(slide.getVisual().getPivotTable(),
								tmp_tbl.getColorTable(), slide.getFilenameAudio(),
								slide.getTitle(), j+ slide_so_far_created + 2,
								slide.getTitleColumn(), slide.getTitleRow(),
								slide.getSubTitle(),
								(Tabular) slide.getVisual(),
								(actItem.getId() == 3 ? 0 : 1));
						addNotesOnSlide(slide.getNotes());
					}
					slide.subTimeCreationPutInPPTX(System.nanoTime());
				}
				slide_so_far_created += actItem.getSizeOfEpisodes();
			}
		}
		

		FileOutputStream fout;
		try {
			fout = new FileOutputStream(this.finalResult.getFilename());
			writeOutput(fout);
			fout.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	 }

		abstract protected void createNewSlide(String[][] table, Color[][] colorTable,
				String AudioFilename, String Title, int slideid,
				String titleColumn, String titleRow, String subtitle,
				Tabular tabular, int hide_slide);
		
		abstract protected void addNotesOnSlide(String notes); 
		
		abstract protected void createIntroSlide(Slide episode, int slide_so_far_created);
		
		abstract protected void createSummarySlide(Slide episode, int slideId);
		
		abstract protected void writeOutput(FileOutputStream fout) throws IOException;


}
