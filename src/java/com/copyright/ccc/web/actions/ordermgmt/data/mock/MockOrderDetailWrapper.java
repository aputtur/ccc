package com.copyright.ccc.web.actions.ordermgmt.data.mock;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;



public class MockOrderDetailWrapper implements Serializable
{

	    private static final long serialVersionUID = 1L;
	    
	    private int _orderId = -1;
	    private int _courseId = -1;
	    private int _detailId;
	    
	    //non-editable fields displayed
	    private Date _orderDate;
	    private int _confNumber;
	    private String _orderSource;
	    private String _billingStatus;
	    private String _invoiceNum;
	    private String _enteredBy;
	    private String _internetLogin;
	    private String _permissionSource;
	    private String _permissionStatus;  //????used or not????
	    private String _detailNumber;
	    private String _adjID;
	    
	    //fields common to all products
	    private String _assignedTo;
	    private String _entryStatus;  //Item Status (label)
	    private String _tou;
	    private String _idnoType;
	    private String _idno;
	    private String _title;
	    private String _tpuDisplay;
	    private String _article;
	    private String _publisher;
	    private String _rightsholder;
	    private String _pymt;
	    private String _withdrawnCD;
	    private String _volumePriced;
	    private String _pinned;
	    private String _rhRefNum;
	    private String _rightInst;
	    private String _itemCycle;
	    private String _errorDesc;
	    
	    //fields specific to APS
	    private String _chapterOrArticle;
	    private String _author;
	    private String _editor;
	    private String _edition;
	    private String _translator;
	    private String _refNum;	//Line Item Reference # (label)
	    private Date _dateOfIssue;
	    private Date _pubDate;
	    private int _volume;
	    private int _numPages;
	    private int _numCopies;
	    private int _estCopies;
	    private String _pageRange;
	    
	    //permissions and fees info
	    private String _permissionType; //maybe int/enum??
	    private String _requestID;
	    private double _perPageFee;  //double? always in USD?
	    private double _baseFee;
	    private double _flatFee;
	    private double _rightsholderPcnt;
	    private String _comment; //enum??
	    private String _customComment;
	    

	    
	    /**
	     * Default constructor
	     */
	    public MockOrderDetailWrapper(){ }

	    public MockOrderDetailWrapper(MockOrderDetailWrapper cpy){ 
	    	super();
	    	try {
	    		PropertyUtils.copyProperties(this, cpy);
	    	} catch (IllegalAccessException e1) {
	    		//throw new ServiceRuntimeException("IllegalAccessException", e1);
	    	} catch (InvocationTargetException e2) {
	    		//throw new ServiceRuntimeException("InvocationTargetException", e2);
	    	} catch (NoSuchMethodException e3) {
	    		//throw new ServiceRuntimeException("NoSuchMethodException", e3);
	    	}
	    }
	    
	    public int getDetailId()
	    {
	        return _detailId;
	    }

	    public void setDetailId(int cid)
	    {
	        this._detailId = cid;
	    }
	    
	    public int getCourseId()
	    {
	        return _courseId;
	    }

	    public void setCourseId(int cid)
	    {
	        this._courseId = cid;
	    }
	    
	    
	    public int getOrderId()
	    {
	        return _orderId;
	    }

	    public void setOrderId(int oid)
	    {
	        this._orderId = oid;
	    }
	    
	    public int getConfNumber()
	    {
	        return _confNumber;
	    }

	    public void setConfNumber(int n)
	    {
	        this._confNumber = n;
	    }
	    

	    public String getDetailNumber() {
	        return _detailNumber;
	    }

	    public void setDetailNumber(String num) {
	    	_detailNumber = num;	    
	    }
	    
	    public String getInvoiceNum() {
	        return _invoiceNum;
	    }

	    public void setInvoiceNum(String num) {
	    	_invoiceNum = num;	    
	    }
	    
	    public String getOrderSource() {
	        return _orderSource;
	    }

	    public void setOrderSource(String num) {
	    	_orderSource = num;	    
	    }
	    
	    public String getPermissionSource() {
	        return _permissionSource;
	    }

	    public void setPermissionSource(String num) {
	    	_permissionSource = num;	    
	    }
	    
	    public String getEnteredBy() {
	        return _enteredBy;
	    }

	    public void setEnteredBy(String num) {
	    	_enteredBy = num;	    
	    }
	    
	    public String getInternetLogin() {
	        return _internetLogin;
	    }

	    public void setInternetLogin(String num) {
	    	_internetLogin = num;	    
	    }
	    
	    public String getAssignedTo() {
	        return _assignedTo;
	    }

	    public void setAssignedTo(String num) {
	    	_assignedTo = num;	    
	    }
	    
	    public String getBillingStatus() {
	        return _billingStatus;
	    }

	    public void setBillingStatus(String num) {
	    	_billingStatus = num;	    
	    }


		public String getPermissionStatus() {
			return _permissionStatus;
		}


		public void setPermissionStatus(String status) {
			_permissionStatus = status;
		}


		public String getAdjID() {
			return _adjID;
		}


		public void setAdjID(String _adjid) {
			_adjID = _adjid;
		}


		public String getEntryStatus() {
			return _entryStatus;
		}


		public void setEntryStatus(String status) {
			_entryStatus = status;
		}


		public String getTou() {
			return _tou;
		}


		public void setTou(String _tou) {
			this._tou = _tou;
		}


		public String getIdnoType() {
			return _idnoType;
		}


		public void setIdnoType(String type) {
			_idnoType = type;
		}


		public String getIdno() {
			return _idno;
		}


		public void setIdno(String _idno) {
			this._idno = _idno;
		}


		public String getTitle() {
			return _title;
		}


		public void setTitle(String _title) {
			this._title = _title;
		}


		public String getTpuDisplay() {
			return _tpuDisplay;
		}


		public void setTpuDisplay(String display) {
			_tpuDisplay = display;
		}


		public String getPublisher() {
			return _publisher;
		}


		public void setPublisher(String _publisher) {
			this._publisher = _publisher;
		}


		public String getRightsholder() {
			return _rightsholder;
		}


		public void setRightsholder(String _rightsholder) {
			this._rightsholder = _rightsholder;
		}


		public String getPymt() {
			return _pymt;
		}


		public void setPymt(String _pymt) {
			this._pymt = _pymt;
		}


		public String getWithdrawnCD() {
			return _withdrawnCD;
		}


		public void setWithdrawnCD(String _withdrawncd) {
			_withdrawnCD = _withdrawncd;
		}


		public String getVolumePriced() {
			return _volumePriced;
		}


		public void setVolumePriced(String priced) {
			_volumePriced = priced;
		}


		public String getPinned() {
			return _pinned;
		}


		public void setPinned(String _pinned) {
			this._pinned = _pinned;
		}


		public String getRhRefNum() {
			return _rhRefNum;
		}


		public void setRhRefNum(String refNum) {
			_rhRefNum = refNum;
		}


		public String getRightInst() {
			return _rightInst;
		}


		public void setRightInst(String inst) {
			_rightInst = inst;
		}


		public String getChapterOrArticle() {
			return _chapterOrArticle;
		}


		public void setChapterOrArticle(String orArticle) {
			_chapterOrArticle = orArticle;
		}


		public String getAuthor() {
			return _author;
		}


		public void setAuthor(String _author) {
			this._author = _author;
		}


		public String getEditor() {
			return _editor;
		}


		public void setEditor(String _editor) {
			this._editor = _editor;
		}


		public String getEdition() {
			return _edition;
		}


		public void setEdition(String _edition) {
			this._edition = _edition;
		}


		public String getTranslator() {
			return _translator;
		}


		public void setTranslator(String _translator) {
			this._translator = _translator;
		}


		public String getRefNum() {
			return _refNum;
		}


		public void setRefNum(String num) {
			_refNum = num;
		}


		public Date getDateOfIssue() {
			return _dateOfIssue;
		}


		public void setDateOfIssue(Date ofIssue) {
			_dateOfIssue = ofIssue;
		}


		public Date getPubDate() {
			return _pubDate;
		}


		public void setPubDate(Date date) {
			_pubDate = date;
		}


		public int getVolume() {
			return _volume;
		}


		public void setVolume(int _volume) {
			this._volume = _volume;
		}


		public int getNumPages() {
			return _numPages;
		}


		public void setNumPages(int pages) {
			_numPages = pages;
		}


		public int getNumCopies() {
			return _numCopies;
		}


		public void setNumCopies(int copies) {
			_numCopies = copies;
		}


		public int geEstCopies() {
			return _estCopies;
		}


		public void setEstCopies(int copies) {
			_estCopies = copies;
		}


		public String getPageRange() {
			return _pageRange;
		}


		public void setPageRange(String range) {
			_pageRange = range;
		}


		public String getPermissionType() {
			return _permissionType;
		}


		public void setPermissionType(String type) {
			_permissionType = type;
		}


		public String getRequestID() {
			return _requestID;
		}


		public void setRequestID(String _requestid) {
			_requestID = _requestid;
		}


		public double getPerPageFee() {
			return _perPageFee;
		}


		public void setPerPageFee(double pageFee) {
			_perPageFee = pageFee;
		}


		public double getBaseFee() {
			return _baseFee;
		}


		public void setBaseFee(double fee) {
			_baseFee = fee;
		}


		public double getFlatFee() {
			return _flatFee;
		}


		public void setFlatFee(double fee) {
			_flatFee = fee;
		}


		public double getRightsholderPcnt() {
			return _rightsholderPcnt;
		}


		public void setRightsholderPcnt(double pcnt) {
			_rightsholderPcnt = pcnt;
		}


		public String getComment() {
			return _comment;
		}


		public void setComment(String _comment) {
			this._comment = _comment;
		}


		public String getCustomComment() {
			return _customComment;
		}


		public void setCustomComment(String comment) {
			_customComment = comment;
		}


		public Date getOrderDate() {
			return _orderDate;
		}


		public void setOrderDate(Date date) {
			_orderDate = date;
		}


		public String getArticle() {
			return _article;
		}


		public void setArticle(String _article) {
			this._article = _article;
		}


		public String getItemCycle() {
			return _itemCycle;
		}


		public void setItemCycle(String cycle) {
			_itemCycle = cycle;
		}


		public String getErrorDesc() {
			return _errorDesc;
		}


		public void setErrorDesc(String desc) {
			_errorDesc = desc;
		}
		
		

}