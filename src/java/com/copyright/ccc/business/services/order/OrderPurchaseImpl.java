package com.copyright.ccc.business.services.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.user.User;
import com.copyright.data.order.CoursePack;
import com.copyright.data.order.Purchase;
import com.copyright.svc.ldapuser.api.data.UserType;

	public class OrderPurchaseImpl implements OrderPurchase {

		private static final long serialVersionUID = 1L;
		
	    Purchase _purchase;
	    public String _purchaseTitles;
	    User _user;
	    private String buyerUserIdentifier="";
	    static String GateWaySourceCode = "GW";
   
	    public void setPurchase (Purchase purchase) {
	       _purchase = purchase;
	    }
	    
	    public Purchase getPurchase() {
	        if (_purchase == null) {
	           // Keith: Is this OK? It keeps reporting from choking.
	           _purchase = new Purchase();
	        }
	        return _purchase;
	    }
	    
	    public ArrayList<OrderBundle> getOrderBundles() {
	    	
	    	ArrayList<OrderBundle> orderBundles = new ArrayList<OrderBundle>();
	    	
	    	if (this.getPurchase().isAcademic()) {
	    		OrderPurchaseBundleImpl orderPurchaseBundleImpl = new OrderPurchaseBundleImpl();
	    		orderPurchaseBundleImpl.setPurchase(this.getPurchase());
	    		orderBundles.add(orderPurchaseBundleImpl);
	    	}

	    	return orderBundles;
	    }
	    
	    
	    public User getUser() {
	        return _user;
	    }

	    public void setUser(User user) {
	        _user = user;
	    }
	    
	    public long getConfirmationNumber() {
	        return this.getPurchase().getPurInst();
	    } 

	    public boolean isOpen() {
	        return this.getPurchase().isOpen();
	    }

	    public boolean isClosed() {
	        if (this.getPurchase().isOpen()) {
	            return false; }
	        else {
	            return true;
	        }
	    }
	    
	    public boolean isAcademic() {

	        return this.getPurchase().isAcademic();
//	        if ( (this.getCoursePack().getNumStudents() == null || this.getCoursePack().getNumStudents().longValue() == 0) &&
//	            this.getCoursePack().getCourseNumber() == null) {
//	            return false; }
//	        else {
//	            return true;
//	        }
	    }

	    public boolean isCancelable() {
	        return this.getPurchase().isCancelable();
	    }
	        
	    public String getPurchaseTitles() {
	    	if (!this.getPurchase().isAcademic()) {
	    		return this.getPurchase().getTitleList();
	    	}
	    	return null;
	    }
	    
//	    public void setPurchaseTitles (String purchaseTitles) {
//	        _purchaseTitles = purchaseTitles;
//	    }
	           
	    public String getBillingReference() {
	        return this.getPurchase().getBillingReference();
	    }    

	    public void setBillingReference(String billingReference) {
	    	this.getPurchase().setBillingReference(billingReference);
	    }
	    
	    public long getByrInst() {
	        return this.getPurchase().getByrInst();
	    }    

	    public long getDetailCount() {
	    	if (!this.getPurchase().isAcademic()) {
		        return this.getPurchase().getDetailCount();	    		
	    	}
	    	return 0;
	    }    

//	    public boolean getModified() {
//	        return _purchase.getIsModified();
//	    }    

	    public String getPoNumber() {
	        return this.getPurchase().getPoNumber();
	    }    

	    public void setPoNumber(String poNumber) {
	    	this.getPurchase().setPoNumber(poNumber);
	    }
	    
	    public Date getPurchaseDate() {
	        return this.getPurchase().getPurchaseDate();
	    }    

	    public long getPurInst() {
	        return this.getPurchase().getPurInst();
	    } 

	    public Long getOrderHeaderId() {
	        return Long.valueOf(this.getPurchase().getPurInst());
	    } 

    	public String getOrderSourceCd() {
    		return getPurchase().getOrderSource();
    	}
    	 
        public String getOrderStatusCd() {
        	if (getPurchase().isOrderComplete()) {
        		return ItemConstants.ORDER_STATUS_COMPLETE_CD;
        	} else {
        		return ItemConstants.ORDER_STATUS_NOT_COMPLETE_CD;        		
        	}
        } 

        public String getOrderStatusDisplay() {
        	if (getPurchase().isOrderComplete()) {
        		return ItemConstants.ORDER_STATUS_COMPLETE_DESC;
        	} else {
        		return ItemConstants.ORDER_STATUS_NOT_COMPLETE_DESC;        		
        	}
        	
       } 

	    /**
	    * isShowPurchase is a boolean used by the UI to determine if the Purchase
	    * has been Deleted.  A Deleted Purchase should not be shown in the UI.
	    * 
	    * @return true if the OrderPurchase should still be visible to the user in the UI.
	    * Otherwise return false;
	    */
	    public boolean isShowPurchase() {
	        return this.getPurchase().isShowPurchase();
	    }    

	    /** 
	    * Setter for the Purchase showPurchase flag to determine if the Purchase
	    * should be visible to the user in the UI.
	    * @param showPurchaseFlag is true if the Purchase is not Deleted and false if 
	    * the Purchase has been deleted and should not be shown in Order History
	    */
	    public void setShowPurchase(boolean showPurchaseFlag)
	    {
	       if (_purchase != null) {
	          _purchase.setShowPurchase(showPurchaseFlag);
	       }           
	    }
	        
//	    public String getTransactionId() {
//	        return _purchase.getTransactionId();
//	    }    
	    
	    public CoursePack getCoursePack() {
	        if (_purchase != null) {
	           if (_purchase.getSingleCoursePack() == null) {
	              _purchase.setSingleCoursePack(new CoursePack());
	           }
	        } else {
	            _purchase = new Purchase();
	            _purchase.setSingleCoursePack(new CoursePack());
	        }
	        
	        return _purchase.getSingleCoursePack();
	    }

	    public String getCourseName() { 
	    
	        return this.getCoursePack().getCourseName();
//	       String name = "";
//	       CoursePack pack = _purchase.getSingleCoursePack();
//	       if (pack != null) {
//	         name = pack.getCourseName();
//	       }
//	       return name; 
	    }
	    
	    public String getCourseNumber() 
	    { 
	        return this.getCoursePack().getCourseNumber();
	    }

	    public long getNumberOfStudents() 
	    {
	        if (this.getCoursePack().getNumStudents() == null) {
	            return 0;
	        } else {
	            return this.getCoursePack().getNumStudents().longValue();
	        }
	    }
	    
//	    public String getNumStudSameFlg() {        
//	        return null;
//	    }

	    public boolean isNumStudentsSame() {
	        return this.getPurchase().isNumStudentsSame();
	    }
	    
	    public String getPromoCode() {
	        return this.getPurchase().getPromoCode();
	    }
	    
	    public String getSchool() 
	    { 
	        return this.getCoursePack().getUniversityName();
	    }
	       
	    public String getInstructor() 
	    { 
	        return this.getCoursePack().getInstructorName();
	    }
	       
	    public String getYourReference() { 
	       return this.getCoursePack().getHeaderRef();   // ?? changed from _purchase.getLcnHeaderRefNum();  
	    }

	    public String getComments() { 
	        return this.getCoursePack().getOrderEnteredBy(); 
	    }

	    
	    public Date getStartOfTerm() 
	    {      
	        return this.getCoursePack().getStartOfTerm(); 
	    }

	    public String getStartOfTermStr() {
	       String termStartStr = "";
	       Date startOfTerm = this.getStartOfTerm();
	       if (startOfTerm != null) {
	           SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	           termStartStr = format.format(startOfTerm);                   
	       }

	       return termStartStr;
	    }
	    
	    @Deprecated
	    public String getAccountingReference() 
	    { 
	        return this.getCoursePack().getAcctingRef();
	    }
	        
	    public String getOrderEnteredBy() { 
	       return this.getCoursePack().getOrderEnteredBy();
	    }
	    
	    public String getOrderEnteredByUserIdentifier() {
	    	if (this.getUser() != null) {
	    		if (this.getUser().getUsername() != null) {
	    			return this.getUser().getUsername();
	    		}
	    	}
	    	
	    	return null;
	    }
	    
	    /**
	    * This method supports the UI order date display field.  It gets
	    * the Purchase Date from this class' aggregate Purchase object
	    * and formats it to a display string for the JSP
	    * @return the purchase order date as a String
	    */
	     public String getOrderDateStr() {
	        Date purchDate = this.getPurchase().getPurchaseDate();
	        String orderDateStr = "";
	        if (purchDate != null) {
	           SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
	           orderDateStr = format.format(purchDate);            
	        }
	    
	        return orderDateStr;
	     }
	     
	    public Date getOrderDate() {
	       return this.getPurchase().getPurchaseDate();
	    }

	    public void setSchool (String school)  { 
	        this.getCoursePack().setUniversityName(StringUtils.upperCase(school)); 
	    }
	    
	    public void setCourseNumber (String courseNumber)  { 
	        this.getCoursePack().setCourseNumber(StringUtils.upperCase(courseNumber)); 
	    }
	    
	    public void setCourseName (String courseName)  { 
	        this.getCoursePack().setCourseName(StringUtils.upperCase(courseName)); 
	    }
	    
	    public void setNumberOfStudents (long numberOfStudents)  {
	        this.getCoursePack().setNumStudents(numberOfStudents); 
	    }
	    
	    public void setInstructor (String instructor)  {
	        this.getCoursePack().setInstructorName(StringUtils.upperCase(instructor)); 
	    }
	    
	    public void setYourReference (String yourReference)  {
	        this.getCoursePack().setHeaderRef(StringUtils.upperCase(yourReference)); 
	    }

	    public void setComments (String comments)  {
	        this.getCoursePack().setOrderEnteredBy(comments); 
	    }
	    
	    @Deprecated
		public void setAccountingReference (String accountingReference)  {
	        this.getCoursePack().setAcctingRef(StringUtils.upperCase(accountingReference)); 
	    }
	    
	    public void setOrderEnteredBy (String orderEnteredBy)  {
	        this.getCoursePack().setOrderEnteredBy(orderEnteredBy); 
	    }
	    
	    public void setStartOfTerm(Date startOfTerm) {
	        this.getCoursePack().setStartOfTerm(startOfTerm); 
	    }

		public Long getLicenseeAccount() {
			if (getUser() != null) {
				return getUser().getAccountNumber();				
			}
			return null;
		}
		
		public String getLicenseeName() {

			if (getUser() != null) {
				if (getUser().getLdapUser() != null) {
					if (getUser().getLdapUser().getUserType().equals(UserType.ORG_ADD.getValue()) ||
						getUser().getLdapUser().getUserType().equals(UserType.ORGANIZATION.getValue())) {
						if (getUser().getOrganization() != null) {
							return getUser().getOrganization().getOrganizationName();
						}
					} else {
						if (getUser().getLdapUser() != null) {
							StringBuffer returnName = new StringBuffer();
							boolean firstText = true;
							if (getUser().getDisplayTitle(getUser().getLdapUser().getPrefix()) != null) {
								returnName.append(getUser().getLdapUser().getPrefix());
								firstText = false;
							}
							if (getUser().getPerson().getName().getFirstName() != null) {
								if (firstText == false) {
									returnName.append(" ");
								}
								returnName.append(getUser().getLdapUser().getFirstName());
								firstText = false;
							}
							if (getUser().getPerson().getName().getMiddleName() != null) {
								if (firstText == false) {
									returnName.append(" ");
								}
								firstText = false;
								returnName.append(getUser().getLdapUser().getMiddleName());
							}
							if (getUser().getPerson().getName().getLastName() != null) {
								if (firstText == false) {
									returnName.append(" ");
								}
								returnName.append(getUser().getLdapUser().getLastName());
							}
//							if (getUser().getPerson().getName().getSuffix() != null) {
//								returnName.append(getUser().getLdapUser().getSuffix());
//							}
							if (returnName.length()>0) {
								return returnName.toString();
							} else {
								return null;
							}	
						
						}
					}
				} else {
					return getUser().getName();
				}
//				return getUser().getAccountContactName();				
			}
			
/*			if (getUser() != null) {
				if (getUser().getPerson() != null) {
					if (getUser().getPerson().getUserType().equals(UserType.ORGANIZATION.getValue()) ||
						getUser().getPerson().getUserType().equals(UserType.ORG_ADD.getValue())) {
						if (getUser().getOrganization() != null) {
							return getUser().getOrganization().getOrganizationName();
						}
					} else {
						if (getUser().getPerson() != null) {
							if (getUser().getPerson().getName() != null) {
								StringBuffer returnName = new StringBuffer();
								if (getUser().getPerson().getName().getPrefix() != null) {
									returnName.append(getUser().getPerson().getName().getPrefix());
								}
								if (getUser().getPerson().getName().getFirstName() != null) {
									returnName.append(getUser().getPerson().getName().getFirstName());
								}
								if (getUser().getPerson().getName().getMiddleName() != null) {
									returnName.append(getUser().getPerson().getName().getMiddleName());
								}
								if (getUser().getPerson().getName().getLastName() != null) {
									returnName.append(getUser().getPerson().getName().getLastName());
								}
								if (getUser().getPerson().getName().getSuffix() != null) {
									returnName.append(getUser().getPerson().getName().getSuffix());
								}
								return returnName.toString();
							}
						
						}
					}
				} else {
					return getUser().getName();
				}
//				return getUser().getAccountContactName();				
			}
			
*/
			return null;

		}
		
		public void setBuyerUserIdentifier(String buyerUserIdentifier) {
			this.buyerUserIdentifier = buyerUserIdentifier;
		}

		public String getBuyerUserIdentifier() {
			if (!buyerUserIdentifier.isEmpty()){
				return buyerUserIdentifier;
			}
			if (getUser() != null) {
				return getUser().getUsername();				
			}
			return null;
		}
		
		
	
		public int getOrderDataSource() {
			return OrderDataSourceEnum.TF.getOrderDataSourceId().intValue();
		}
		public String getOrderDataSourceDisplay() {
			return OrderDataSourceEnum.TF.name();		
		}
		
}
