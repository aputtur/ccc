package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.web.util.ChapterDisplay;
import com.copyright.domain.data.WorkExternal;

public class ChapterSearchActionForm extends BasicSearchForm {
	
	private static final long serialVersionUID = 1L;
	
	private String chapterTitle;
	private String chapterAuthor;
	private String chapterIDno;
	private String hostIDno;
	
	private String _sort;
	private List<ChapterDisplay> chapters;	
	private Publication selectedPublication = null;
    private String selectedTou = null;
    private String selectedCategory = null;
    
    
    private String selectedRRTou=null;
    private long selectedRightInst = 0;
    private long selectedRightHolderInst = 0;
    private String selectedRRTouId=null;
    private long selectedCategoryId=0;
    
    private String selectedOfferChannel = "";
    

    
    
    
    
	public ChapterSearchActionForm()
    {
    	//	We default to some these values.  This
    	//	should be handy for our simple search.
    	
        this.clearState();
    }
		
	@Override
    public void clearState()
    {
    	super.clearState();
    	chapterTitle = "";
    	chapterAuthor = "";
    	chapterIDno = "";
    	hostIDno = "";
  
    	_sort = "";
    	chapters = new ArrayList<ChapterDisplay>();
    }
    
    private void clearResults() {
    	chapters = new ArrayList<ChapterDisplay>();
    }
	
	//private ChapterObject selectedChapter;
	private String selectedChapterID;
	
	public String getSelectedChapterID() {
		return selectedChapterID;
	}
	public void setSelectedAChapterID(String selectedChapterID) {
		this.selectedChapterID = selectedChapterID;
	}
	public String getChapterTitle() {
		return chapterTitle;
	}
	public void setChapterTitle(String chapterTitle) {
		this.chapterTitle = chapterTitle;
	}
	public String getChapterAuthor() {
		return chapterAuthor;
	}
	public void setChapterAuthor(String chapterAuthor) {
		this.chapterAuthor = chapterAuthor;
	}
	public String getChapterIDno() {
		return chapterIDno;
	}
	public void setChapterIDno(String chapterIDno) {
		this.chapterIDno = chapterIDno;
	}
	
	public void setChapters(List<WorkExternal> chapterSearchResults){
		chapters = new ArrayList<ChapterDisplay>();
		for (WorkExternal work : chapterSearchResults) {
			chapters.add(new ChapterDisplay(work));
		}
	}
	
	public List<ChapterDisplay> getChapters(){
		//return an empty list if there are no results
		return chapters;
	}
	
	public String getHostIDno() {
		return hostIDno;
	}
	public void setHostIDno(String hostIDno) {
		this.hostIDno = hostIDno;
	}
	
	public String getSort() {
		return _sort;
	}
	public void setSort( String s )         { _sort=s;    }
	
	public int getResultCountPageSizeCompare() {
		if (Integer.parseInt(getPageSize()) > Integer.parseInt(getCount())){
			return -1;
		}
		if (Integer.parseInt(getPageSize()) == Integer.parseInt(getCount())){
			return 0;
		}
	    
		return 1;
	}
	
	
	@Override
    public ActionErrors validate( ActionMapping      mapping
            , HttpServletRequest request )
	{
	//	Capture some of the values we need to look at, and
	//	also create a new messages object.
	
	ActionErrors messages = super.validate( mapping, request );
	
	
    if (getSearchParameters().isEmpty())
    {
    	messages.add( ActionMessages.GLOBAL_MESSAGE
    	        , new ActionMessage( "search.error.message.required.four","Title","Author Name","IDNO","Date Range") );
    	clearResults();
    	super.clearState();
    }
	
	return messages;
	}
    
    public String getSearchParameters() {
    	String searchParameters =
    		getChapterTitle().trim() +
    		getChapterAuthor().trim() +
    		getChapterIDno().trim();
    		
    	return searchParameters;
    }
    
    public void clearSearchParameters(){
    	setChapterTitle("");
    	setChapterAuthor("");
    	setChapterIDno("");
    }
    
    @Override
    public String getIsMore()
    {
        int pageRangeTotal = 0;
        
        if (chapters == null || chapters.isEmpty()) return "no";

       return super.getIsMore();
    }

	public Publication getSelectedPublication() {
		return selectedPublication;
	}

	public void setSelectedPublication(Publication selectedPublication) {
		this.selectedPublication = selectedPublication;
	}

	public String getSelectedTou() {
		return selectedTou;
	}

	public void setSelectedTou(String selectedTou) {
		this.selectedTou = selectedTou;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public void setSelectedRRTou(String selectedRRTou) {
		this.selectedRRTou = selectedRRTou;
	}

	public String getSelectedRRTou() {
		return selectedRRTou;
	}

	public void setSelectedRightInst(long selectedRightInst) {
		this.selectedRightInst = selectedRightInst;
	}

	public long getSelectedRightInst() {
		return selectedRightInst;
	}

	public void setSelectedRightHolderInst(long selectedRightHolderInst) {
		this.selectedRightHolderInst = selectedRightHolderInst;
	}

	public long getSelectedRightHolderInst() {
		return selectedRightHolderInst;
	}

	public void setSelectedRRTouId(String selectedRRTouId) {
		this.selectedRRTouId = selectedRRTouId;
	}

	public String getSelectedRRTouId() {
		return selectedRRTouId;
	}

	public void setSelectedCategoryId(long selectedCategoryId) {
		this.selectedCategoryId = selectedCategoryId;
	}

	public long getSelectedCategoryId() {
		return selectedCategoryId;
	}
	
	/**
	 * @return the selectedOfferChannel
	 */
	public String getSelectedOfferChannel()
	{
		return selectedOfferChannel;
	}

	/**
	 * @param selectedOfferChannel the selectedOfferChannel to set
	 */
	public void setSelectedOfferChannel(String selectedOfferChannel)
	{
		this.selectedOfferChannel = selectedOfferChannel;
	}



}
