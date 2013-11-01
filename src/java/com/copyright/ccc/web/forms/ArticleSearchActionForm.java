package com.copyright.ccc.web.forms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.web.util.ArticleDisplay;
import com.copyright.ccc.web.util.WebUtils;
import com.copyright.domain.data.WorkExternal;

public class ArticleSearchActionForm extends BasicSearchForm {
	
	private static final long serialVersionUID = 1L;
	
	private String articleTitle;
	private String articleAuthor;
	private String articleIDno;
	private String hostIDno;
	private String articleStartMonth;
	private String articleStartYear;
	private String articleEndMonth;
	private String articleEndYear;
	private String itemVolume;
	private String itemIssue;
	private String itemStartPage;
	private String _sort;
	//private String dateRange;
	private List<ArticleDisplay> articles;
	
	private Publication selectedPublication = null;
	private long selectedPublicationWrkInst = 0;
    private String selectedTou = null;
    private String selectedCategory = null;
    
    
    private String selectedRRTou=null;
    private long selectedRightInst = 0;
    private long selectedRightHolderInst = 0;
    private String selectedRRTouId=null;
    private long selectedCategoryId=0;
    private boolean isBiactive = false;
    
    private String selectedOfferChannel = "";
    
    
    
	public ArticleSearchActionForm()
    {
    	//	We default to some these values.  This
    	//	should be handy for our simple search.
    	
        this.clearState();
    }
		
	@Override
    public void clearState()
    {
    	super.clearState();
    	articleTitle = "";
    	articleAuthor = "";
    	articleIDno = "";
    	hostIDno = "";
    	articleStartMonth = "";
    	articleStartYear = "";
    	articleEndMonth = "";
    	articleEndYear = "";
    	setItemVolume("");
    	setItemIssue("");
    	setItemStartPage("");
    	_sort = "";
    	
    	articles = new ArrayList<ArticleDisplay>();
    }
    
    private void clearResults() {
    	articles = new ArrayList<ArticleDisplay>();
    }
	
	//private ArticleObject selectedArticle;
	private String selectedArticleID;
	
	public String getSelectedArticleID() {
		return selectedArticleID;
	}
	public void setSelectedArticleID(String selectedArticleID) {
		this.selectedArticleID = selectedArticleID;
	}
	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	public String getArticleAuthor() {
		return articleAuthor;
	}
	public void setArticleAuthor(String articleAuthor) {
		this.articleAuthor = articleAuthor;
	}
	public String getArticleIDno() {
		return articleIDno;
	}
	public void setArticleIDno(String articleIDno) {
		this.articleIDno = articleIDno;
	}
	public String getArticleStartMonth() {
		return articleStartMonth;
	}
	public void setArticleStartMonth(String articleStartMonth) {
		this.articleStartMonth = articleStartMonth;
	}
	public String getArticleStartYear() {
		return articleStartYear;
	}
	public void setArticleStartYear(String articleStartYear) {
		this.articleStartYear = articleStartYear;
	}
	public String getArticleEndMonth() {
		return articleEndMonth;
	}
	public void setArticleEndMonth(String articleEndMonth) {
		this.articleEndMonth = articleEndMonth;
	}
	public String getArticleEndYear() {
		return articleEndYear;
	}
	public void setArticleEndYear(String articleEndYear) {
		this.articleEndYear = articleEndYear;
	}
	
	public void setArticles(List<WorkExternal> articleSearchResults){
		articles = new ArrayList<ArticleDisplay>();
		for (WorkExternal work : articleSearchResults) {
			articles.add(new ArticleDisplay(work));
		}
	}
	
	public List<ArticleDisplay> getArticles(){
		//return an empty list if there are no results
		return articles;
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
	
	public String getDateRange() {
		String startDate="";
		String endDate="";
		StringBuffer dateRange = new StringBuffer();
		
		
		if (!getArticleStartMonth().isEmpty()&&Integer.parseInt(getArticleStartMonth())>= 10) {
		   startDate =  WebUtils.convertYYYYMMtoMediumDate(getArticleStartYear()+getArticleStartMonth());
		} else if (!getArticleStartMonth().isEmpty()) {
			startDate = WebUtils.convertYYYYMMtoMediumDate(getArticleStartYear()+"0"+getArticleStartMonth());
		}else if(!getArticleStartYear().isEmpty()) {
			startDate =  getArticleStartYear();
		}
		

		if (!getArticleEndMonth().isEmpty()&&Integer.parseInt(getArticleEndMonth())>= 10) {
		   endDate = WebUtils.convertYYYYMMtoMediumDate(getArticleEndYear()+getArticleEndMonth());
		} else if(!getArticleEndMonth().isEmpty()){
		   endDate = WebUtils.convertYYYYMMtoMediumDate(getArticleEndYear()+"0"+getArticleEndMonth());
		}else if(!getArticleStartYear().isEmpty()) {
			endDate =  getArticleEndYear();
		}
		
		if(!startDate.isEmpty() && !endDate.isEmpty()){
			dateRange.append("From: ").append(startDate).append(" ").append("To:").append(endDate);
		}else if(!startDate.isEmpty() && endDate.isEmpty()){
			dateRange.append(startDate).append(" ");
		}else if(startDate.isEmpty() && !endDate.isEmpty()){
			dateRange.append(endDate);
		}
			
		return dateRange.toString();
		
	}
	
	@Override
	public ActionErrors validate( ActionMapping      mapping
			, HttpServletRequest request )
	{
		/*
		 * if the form is not populated with this fundamental data
		 * then the session must have expired. Don't bother validating
		 * because the jsp will fail to paint anyway.
		 */
		if (getSelectedPublication()==null) {
			return new ActionErrors();
		}
		//	Capture some of the values we need to look at, and
		//	also create a new messages object.
		ActionErrors messages = super.validate( mapping, request );


		if (getSearchParameters().isEmpty())
		{
			messages.add( ActionMessages.GLOBAL_MESSAGE
					, new ActionMessage( "search.error.message.required.seven",new String[]{"Title","Author Name","IDNO","Volume","Issue","First page","Date Range"}) );

			clearResults();
			super.clearState();
		}

		return messages;
	}
    
    public String getSearchParameters() {
    	String searchParameters =
    		getArticleTitle().trim() +
    		getArticleAuthor().trim() +
    		getArticleIDno().trim() +
    		getItemVolume().trim() +
    		getItemIssue().trim() +
    		getItemStartPage().trim() +
    		getArticleStartMonth().trim() +
    		getArticleEndMonth().trim() +
    		getArticleStartYear().trim() +
    		getArticleEndYear().trim();
    	return searchParameters;
    }
    
    public void clearSearchParameters(){
    	setArticleTitle("");
    	setArticleAuthor("");
    	setArticleIDno("");
    	setItemVolume("");
    	setItemIssue("");
    	setItemStartPage("");
    	setArticleStartMonth("");
    	setArticleEndMonth("");
    	if(!getIsBiactive()){
    		setArticleStartYear("");
        	setArticleEndYear("");	
    	}
    	
    }
    
    @Override
    public String getIsMore()
    {
        int pageRangeTotal = 0;
        
        if (articles == null || articles.isEmpty()) return "no";

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

	public void setBiactive(boolean isBiactive) {
		this.isBiactive = isBiactive;
	}

	public boolean getIsBiactive() {
		return isBiactive;
	}

	public void setItemVolume(String itemVolume) {
		this.itemVolume = itemVolume;
	}

	public String getItemVolume() {
		return itemVolume;
	}

	public void setItemIssue(String itemIssue) {
		this.itemIssue = itemIssue;
	}

	public String getItemIssue() {
		return itemIssue;
	}

	public void setItemStartPage(String itemStartPage) {
		this.itemStartPage = itemStartPage;
	}

	public String getItemStartPage() {
		return itemStartPage;
	}

	public long getSelectedPublicationWrkInst() {
		if (this.selectedPublication!=null) {
			return selectedPublication.getWrkInst();
		}
		return selectedPublicationWrkInst;
	}

	public void setSelectedPublicationWrkInst(long selectedPublicationWrkInst) {
		this.selectedPublicationWrkInst = selectedPublicationWrkInst;
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
