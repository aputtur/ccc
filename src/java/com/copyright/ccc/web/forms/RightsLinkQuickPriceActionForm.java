package com.copyright.ccc.web.forms;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessages;

import com.copyright.ccc.business.data.Publication;
import com.copyright.ccc.business.services.cart.PurchasablePermission;
import com.copyright.ccc.web.CCValidatorForm;
import com.copyright.ccc.web.util.ArticleDisplay;
import com.copyright.ccc.web.util.ChapterDisplay;
import com.copyright.domain.data.WorkExternal;
import com.copyright.rightslink.base.data.RightResponse;
import com.copyright.rightslink.base.data.RlInitOrderFlow;
import com.copyright.rightslink.html.data.HtmlScreenResponse;


public class RightsLinkQuickPriceActionForm extends CCValidatorForm
{
	//	Save our sort/display specs in the form.

        /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	

	// error variable to display
	private ActionMessages errors=null;
	public ActionMessages getErrors() {
		if(errors==null)
			errors=new ActionMessages();
		return errors;
	}
	public void setErrors(ActionMessages errors) {
		this.errors=errors;
	}

	//	The stuff we need to display.
	
	private boolean readyForCart=false;
	private PurchasablePermission purchasablePermission;
	private boolean updateableCartItem=false;
	private BigDecimal totalPrice=BigDecimal.valueOf(0.00);
	private boolean isPriceAvailable=false;
	private Long itemId;
	private String rightPermissionType;
	private Publication selectedPubItem;
	private Long selectedPubItemWrkInst; //hidden field that identifies the wrk, even after session expiration
	private ArticleDisplay selectedArticle;
	private ChapterDisplay selectedChapter;
	private String quickPriceErrorMessage;
	private boolean showUpdatePriceButton=false;
	private boolean showGetPriceButton=false;
	private String displayPrice=StringUtils.EMPTY;
	private String terms=StringUtils.EMPTY;
	
    private String selectedTou = null;
    private String selectedCategory = null;
    private String previousHiddenvariables;
    private String rightsLinkButtonHTML;
    private boolean redirectedFromLogin=false;
	RightResponse _rightResponse=new RightResponse();
	HtmlScreenResponse _htmlScreenResponse=new HtmlScreenResponse();
	
    WorkExternal work ;
    WorkExternal subWork ;
    RlInitOrderFlow rlInitOrderFlow ;
    
    //Store RLink Special Order from scratch User Input values
    private boolean isRLinkSpecialOrderFromScratch = false;
    private String publicationName;
    private String issn;
    private String publisher;
    private String author;
    private String volume;
    private String edition;
    private String pubDate;
    

    private Map<String, String> requestParameters	    	=	 new HashMap<String, String>();
    
    private boolean             _orderExists     = false;
    
    public boolean    getOrderExists() { return _orderExists; }
    
    public void setOrderExists( boolean b )   { _orderExists = b;     }

	public Map<String, String> getRequestParameters() {
		return requestParameters;
	}
	public void setRequestParameters(Map<String, String> requestParameters) {
		this.requestParameters = requestParameters;
	}
	
	public RlInitOrderFlow getRlInitOrderFlow() {
		if(rlInitOrderFlow==null)
			rlInitOrderFlow=new RlInitOrderFlow();
		return rlInitOrderFlow;
	}

	public void setRlInitOrderFlow(RlInitOrderFlow rlInitOrderFlow) {
		this.rlInitOrderFlow = rlInitOrderFlow;
	}

	public WorkExternal getWork() {
		if(work==null)
			work=new WorkExternal();
		return work;
	}

	public void setWork(WorkExternal work) {
		this.work = work;
	}

	public WorkExternal getSubWork() {
		return subWork;
	}

	public void setSubWork(WorkExternal subWork) {
		this.subWork = subWork;
	}



	public HtmlScreenResponse getHtmlScreenResponse() {
		if(_htmlScreenResponse==null){
			_htmlScreenResponse=new HtmlScreenResponse();
		}
		return _htmlScreenResponse;
	}

	public void setHtmlScreenResponse(HtmlScreenResponse screenResponse) {
		_htmlScreenResponse = screenResponse;
	}


	

	public RightsLinkQuickPriceActionForm() {
		// Should be no need to do anything here.
	}

	public RightResponse getRightResponse() {
		return _rightResponse;
	}

	public void setRightResponse(RightResponse response) {
		_rightResponse = response;
	}

	public void setRightPermissionType(String rightPermissionType) {
		this.rightPermissionType = rightPermissionType;
	}

	public String getRightPermissionType() {
		return rightPermissionType;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedTou(String selectedTou) {
		this.selectedTou = selectedTou;
	}

	public String getSelectedTou() {
		return selectedTou;
	}

	public void setSelectedPubItem(Publication selectedPubItem) {
		this.selectedPubItem = selectedPubItem;
	}

	public Publication getSelectedPubItem() {
		return selectedPubItem;
	}


	public void setSelectedArticle(ArticleDisplay selectedArticle) {
		this.selectedArticle = selectedArticle;
	}

	public ArticleDisplay getSelectedArticle() {
		return selectedArticle;
	}
	public void setSelectedChapter(ChapterDisplay selectedChapter) {
		this.selectedChapter = selectedChapter;
	}

	public ChapterDisplay getSelectedChapter() {
		return selectedChapter;
	}

	public void setQuickPriceErrorMessage(String quickPriceErrorMessage) {
		this.quickPriceErrorMessage = quickPriceErrorMessage;
	}

	public String getQuickPriceErrorMessage() {
		return quickPriceErrorMessage;
	}

	public void setReadyForCart(boolean readyForCart) {
		this.readyForCart = readyForCart;
	}

	public boolean isReadyForCart() {
		return readyForCart;
	}

	public void setUpdateableCartItem(boolean updateableCartItem) {
		this.updateableCartItem = updateableCartItem;
	}

	public boolean getIsUpdateableCartItem() {
		return updateableCartItem;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setPurchasablePermission(PurchasablePermission purchasablePermission) {
		this.purchasablePermission = purchasablePermission;
	}

	public PurchasablePermission getPurchasablePermission() {
		return purchasablePermission;
	}

	public Boolean isArticleSubWork(){
		return (this.getSelectedArticle()!=null) && (this.getSelectedArticle().getPublicationType().equalsIgnoreCase("article"));
	}
	public Boolean isChapterSubWork(){
		return (this.getSelectedChapter()!=null) && (this.getSelectedChapter().getPublicationType().equalsIgnoreCase("chapter"));
	}

	public void setRightsLinkButtonHTML(String rightsLinkButtonHTML) {
		this.rightsLinkButtonHTML = rightsLinkButtonHTML;
	}

	public String getRightsLinkButtonHTML() {
		return rightsLinkButtonHTML;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setIsPriceAvailable(boolean isPriceAvailable) {
		this.isPriceAvailable = isPriceAvailable;
	}

	public boolean getIsPriceAvailable() {
		return isPriceAvailable;
	}

	public void setShowGetPriceButton(boolean showGetPriceButton) {
		this.showGetPriceButton = showGetPriceButton;
	}

	public boolean isShowGetPriceButton() {
		return showGetPriceButton;
	}

	public boolean getDisplayTBDPrice(){
		if(!getIsPriceAvailable() && getTotalPrice().compareTo(BigDecimal.valueOf(-1))==0){
			return false;
		}
		else if(!getIsPriceAvailable()){ 
			return true;
		}
		else{
			return false;
		}
	}
	public boolean getDisplayUpdatePriceBtn(){
		
		return isShowUpdatePriceButton();
	}

	public void setShowUpdatePriceButton(boolean showUpdatePriceButton) {
		this.showUpdatePriceButton = showUpdatePriceButton;
	}

	public boolean isShowUpdatePriceButton() {
		return showUpdatePriceButton;
	}

	public void setPreviousHiddenvariables(String previousHiddenvariables) {
		this.previousHiddenvariables = previousHiddenvariables;
	}

	public String getPreviousHiddenvariables() {
		return previousHiddenvariables;
	}
	
	public void resetFormValues(){
		setQuickPriceErrorMessage(null);
		setErrors(null);
		// fresh start
		setUpdateableCartItem(false);
		setShowGetPriceButton(true);
		setShowUpdatePriceButton(false);
		setIsPriceAvailable(false);
		setTotalPrice(BigDecimal.ZERO.setScale(2));
		setRLinkSpecialOrderFromScratch(false);
	}
	public void setRedirectedFromLogin(boolean redirectedFromLogin) {
		this.redirectedFromLogin = redirectedFromLogin;
	}
	public boolean isRedirectedFromLogin() {
		return redirectedFromLogin;
	}

	public void setDisplayPrice(String displayPrice) {
		this.displayPrice = displayPrice;
	}

	public String getDisplayPrice() {
		return displayPrice;
	}

	public Long getSelectedPubItemWrkInst() {
		return selectedPubItemWrkInst;
	}

	public void setSelectedPubItemWrkInst(Long selectedPubItemWrkInst) {
		this.selectedPubItemWrkInst = selectedPubItemWrkInst;
	}
	
	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getTerms() {
		return terms;
	}
	
	public boolean isRepub()
	{
		if (selectedCategory.toUpperCase().startsWith("REPUB"))
		{
			return true;
		}
		
		return false;
	}
	public void setRLinkSpecialOrderFromScratch(boolean isRLinkSpecialOrder) {
		this.isRLinkSpecialOrderFromScratch = isRLinkSpecialOrder;
	}
	public boolean getRlinkSpecialOrderFromScratch() {
		return isRLinkSpecialOrderFromScratch;
	}
	public void setPublicationName(String publicationName) {
		this.publicationName = publicationName;
	}
	public String getPublicationName() {
		return publicationName;
	}
	public void setIssn(String issn) {
		this.issn = issn;
	}
	public String getIssn() {
		return issn;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getVolume() {
		return volume;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public String getEdition() {
		return edition;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthor() {
		return author;
	}
	
	
	
	
}
