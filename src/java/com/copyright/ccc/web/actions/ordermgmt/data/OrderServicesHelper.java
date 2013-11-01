package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.OrderBundle;
import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.ccc.business.data.OrderPurchase;
import com.copyright.ccc.business.data.OrderSearchCriteria;
import com.copyright.ccc.business.data.OrderSearchResult;
import com.copyright.ccc.business.services.ItemConstants;
import com.copyright.ccc.business.services.OrderDataSourceEnum;
import com.copyright.ccc.business.services.ServiceInvocationException;
import com.copyright.ccc.business.services.ServiceLocator;
import com.copyright.ccc.business.services.cart.PricingServices;
import com.copyright.ccc.business.services.order.OrderHeaderImpl;
import com.copyright.ccc.business.services.order.OrderItemImpl;
import com.copyright.ccc.business.services.order.OrderLicenseServices;
import com.copyright.ccc.business.services.order.OrderLicensesException;
import com.copyright.ccc.business.services.order.OrderPurchaseServices;
import com.copyright.ccc.business.services.order.OrderPurchasesException;
import com.copyright.ccc.util.LogUtil;
import com.copyright.ccc.web.transaction.RepublicationConstants;
import com.copyright.domain.data.WorkExternal;
import com.copyright.svc.order.api.data.Item;
import com.copyright.svc.order.api.data.ItemStatusEnum;
import com.copyright.svc.searchRetrieval.api.data.PublicationSearch;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalConsumerContext;
import com.copyright.svc.searchRetrieval.api.data.SearchRetrievalResult;

public class OrderServicesHelper implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(OrderServicesHelper.class);
	
	private OrderServicesHelper(){}
	
	public static OrderPurchase getConfirmationOrderPurchaseFor( String confirmNumber ) {
		if ( StringUtils.isNotEmpty(confirmNumber) ) {
			if ( StringUtils.isNumeric(confirmNumber.trim() ) ) {
				try {
					OrderPurchase op = OrderPurchaseServices.getOrderPurchaseForConfirmationNumber(Long.valueOf(confirmNumber).longValue());
					if ( op != null) {
						return op;
					}
				} catch (OrderPurchasesException e) {
					LOGGER.error(LogUtil.getStack(e));
					return null;
				} catch (NumberFormatException e) {
					LOGGER.error(LogUtil.getStack(e));
					return null;
				}
			}
		}
		return null;
	}
	private static OrderLicense getOrderLicenseForDetailId( Long detailId ) throws OrderLicensesException{
		OrderSearchResult or = OrderLicenseServices.searchOrderLicensesForDetailNumber(detailId);
		if ( or.getTotalRows() == 1 ) {
			return or.getOrderLicenses().get(0);
		}
		return null;
	}
	
	public static List<ProcessMessage> cancelOpenDetailsFor( String confirmNumber) {
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		
		long confirmNumberLong = 0;
		
		try {
			confirmNumberLong = Long.valueOf(confirmNumber).longValue();
		} catch (Exception e) {
			processMessages.add(new ProcessMessage(confirmNumber,"ERROR","Invalid confirm number - " + confirmNumber + " Exception: " + ExceptionUtils.getRootCauseMessage(e) ));
			return processMessages;
		}

		try {
			OrderPurchaseServices.cancelPurchase(confirmNumberLong);
		} catch (OrderPurchasesException op) {
			processMessages.add(new ProcessMessage(confirmNumber,"ERROR","Cancel all details failed for confirmation number - " + confirmNumber + " Exception: " + op.getMessageCode()));
			return processMessages;
		}
		
		processMessages.add(new ProcessMessage(confirmNumber,"CANCEL","Cancel open details completed."));
		
		return processMessages;
		
/*		
		OrderSearchCriteriaWrapper searchCriteria = new OrderSearchCriteriaWrapper();
		searchCriteria.setConfNumber(confirmNumber);
		searchCriteria.setSortCriteriaBy(OrderSearchCriteriaSortColumnEnum.SORT_BY_VIEW_DEFAULT.name());
		searchCriteria.setSortOrder(OrderSearchCriteriaSortDirectionEnum.ASC.getUserText());
		searchCriteria.setShowCancelled(false);
		int attemptedCount = 0;
		searchCriteria.setDisplayFromRow(1);
		searchCriteria.setDisplayToRow(500);
		try {
			OrderSearchResult searchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria.getServiceSearchCriteriaForSearch());
			int totalToProcessCount = searchResult.getTotalRows();
			
			while ( attemptedCount < totalToProcessCount ) {
				
				for ( OrderLicense orl : searchResult.getOrderLicenses() ) {
					attemptedCount++;
					Long detailId = Long.valueOf( orl.getID() );
					if ( orl.isCancelable() ) {
						try {
							boolean or = OrderLicenseServices.cancelLicense(orl);
							if ( or ) {
								OrderLicense orlupdated = getOrderLicenseForDetailId( orl.getID() );
								if ( orlupdated != null ) {
									processMessages.add(new ProcessMessage(Long.valueOf(orlupdated.getID()).toString(),"CANCEL","completed - " + orlupdated.getLastUpdatedDate().toString() ));
								} else {
									processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Unable to find updated detail record to cancel"));
								}
							}
						} catch (OrderLicensesException e) {
							LOGGER.warn(LogUtil.getStack(e));
							processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Cancel all failed - at detail #" +orl.getID() + ExceptionUtils.getRootCauseMessage(e)));
						} 
					} 
				}
				if ( totalToProcessCount > attemptedCount ) {
					searchCriteria.setDisplayFromRow(searchCriteria.getDisplayToRow()+1);
					searchCriteria.setDisplayToRow(searchCriteria.getDisplayToRow()+499);
					searchResult = OrderLicenseServices.searchOrderLicenses(searchCriteria.getServiceSearchCriteriaForSearch());
					totalToProcessCount = searchResult.getTotalRows();
				}
			}
			
		} catch (OrderLicensesException e) {
			LOGGER.warn(LogUtil.getStack(e));
			processMessages.add(new ProcessMessage(confirmNumber,"ERROR","Cancel all failed - " + ExceptionUtils.getRootCauseMessage(e) ));
		}
		
		return processMessages;
*/		
	}
	
	public static List<ProcessMessage> cancelOrderLicenseForDetailId( Long detailId ) {
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		try {
			OrderLicense orl = getOrderLicenseForDetailId(detailId);
			if ( orl != null ) {
				if ( orl.isCancelable() ) {
					boolean or = OrderLicenseServices.cancelLicense(orl);
					if ( or ) {
						OrderLicense orlupdated = getOrderLicenseForDetailId( orl.getID() );
						if ( orlupdated != null ) {
							processMessages.add(new ProcessMessage(Long.valueOf(orlupdated.getID()).toString(),"CANCEL","completed - " + orlupdated.getLastUpdatedDate().toString() ));
						} else {
							processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Unable to find updated detail record to cancel"));
						}
					}
				} else {
					processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Unable to cancel detail, it is non cancelable."));
				}
			}
//		} catch (OrderLicensesException e) {
//			processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Cancel failed - " + ExceptionUtils.getRootCauseMessage(e) ));
		} catch (OrderLicensesException e) {
			LOGGER.warn(LogUtil.getStack(e));			
			processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Cancel failed - " + ExceptionUtils.getRootCauseMessage(e) ));
		}
		return processMessages;
	}
	
	public static List<ProcessMessage> unCancelOrderLicenseForDetailId( Long detailId ) {
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		try {
			OrderLicense orl = getOrderLicenseForDetailId(detailId);
			if ( orl != null ) {
				if ( !orl.isCancelable() ) {
					OrderLicense orlupdated = getOrderLicenseForDetailId( orl.getID() );
					if ( orlupdated != null ) {
						processMessages.add(new ProcessMessage(Long.valueOf(orlupdated.getID()).toString(),"UNCANCEL","completed - " + orlupdated.getLastUpdatedDate().toString() ));
					} else {
						processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Unable to find updated detail record to uncancel"));
					}
				} else {
					processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","Unable to cancel detail, it is non un-cancelable."));
				}
			}
		} catch (OrderLicensesException e) {
			LOGGER.warn(LogUtil.getStack(e));
			processMessages.add(new ProcessMessage(detailId.toString(),"ERROR","uncancel failed - " + ExceptionUtils.getRootCauseMessage(e) ));
		}
		return processMessages;
	}

	public static List<ProcessMessage> saveOrderDetailOnConfirmationPage(EditItemWrapper eiw, ItemWrapper itemWrapper, boolean isInvoiceView, boolean savingCoiDetails, boolean savingMultipleDetails ) {
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		List<ProcessMessage> ckMessages = new ArrayList<ProcessMessage>();
		int doPricing = 0;

		OrderLicense lastOrderLicense = itemWrapper.getItem();
		itemWrapper.setToBeSaved(true);
		
		try {
			OrderLicense orl = getOrderLicenseForDetailId( itemWrapper.getItem().getID() );
			if ( orl != null ) {	
				if ( isInvoiceView || orl.isInvoiced() || orl.isCanceled()) {
					orl.setRhRefNum(eiw.getItem().getRhRefNum());
				} else {
					orl.setRhRefNum( eiw.getItem().getRhRefNum() );
					
					Long editWrWorkInst = null;
					Long editWorkInst = null; // TF
					
					boolean workChanged = false;
					
					if ( trimmedLongValue( eiw.getItem().getWrWorkInst()) != null ) {
						editWrWorkInst = trimmedLongValue(eiw.getItem().getWrWorkInst() );
						if ((orl.getItemSourceKey() == null) ||
							(!orl.getItemSourceKey().equals(editWrWorkInst)))
							workChanged = true;
					} else if (orl.getItemSourceKey() != null) {
						workChanged = true;
					}
						
					if (workChanged) {
						orl.setItemSourceKey(editWrWorkInst);
						orl.setExternalItemId(editWorkInst);
						if (orl.isManualSpecialOrder()) {
							orl.setStandardNumber( eiw.getItem().getStandardNumber() );
							orl.setEditor( eiw.getItem().getEditor() );
							orl.setPublicationTitle( eiw.getItem().getPublicationTitle() );
							orl.setItemSubDescription( eiw.getItem().getItemSubDescription() );
							orl.setPublisher( eiw.getItem().getPublisher() );
						} else {
							// Get the publication information based on the new wr work inst
							if (orl.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
								initWorkForOmsLicense ( orl, editWrWorkInst, editWorkInst);
							}
						}
							
					}
					
				    if( orl.isAcademic() )
				    {
				      setAcademicParameters( orl, eiw );  
					  ckMessages = checkIfPricingUpdateIsNeededAcademic(eiw, isInvoiceView, orl, lastOrderLicense);
				    }
					    
				    else if( orl.isRepublication() )
				    {
					  setRepublicationParameters( orl, eiw );  
					  ckMessages = checkIfPricingUpdateIsNeededRepublication(eiw, isInvoiceView, orl, lastOrderLicense);
				    }
				    
				    else if( orl.isPhotocopy() )
				    {
				      setPhotocopyParameters(orl, eiw);
					  ckMessages = checkIfPricingUpdateIsNeededPhotocopy(eiw, isInvoiceView, orl, lastOrderLicense);
				    }
				    
				    else if( orl.isEmail() )
				    {
				      setEmailParameters( orl, eiw );
					  ckMessages = checkIfPricingUpdateIsNeededEmail(eiw, isInvoiceView, orl, lastOrderLicense);
				    }
				    
				    else if( orl.isNet() )
				    {
				      setNetParameters( orl, eiw );
					  ckMessages = checkIfPricingUpdateIsNeededNet(eiw, isInvoiceView, orl, lastOrderLicense);
				    }

				    processMessages.addAll( validateDetailForSave(orl, eiw, isInvoiceView || orl.isInvoiced()) );

					if ( ckMessages != null && !ckMessages.isEmpty() ) {
						for ( ProcessMessage pm : ckMessages ) {
							if ( pm.isError() ) { 
								processMessages.add(pm);
							} else if ( pm.getErrorLevel().equalsIgnoreCase(PRICING_REQUIRED_FLAG) ) {
								doPricing++;
							}
						}
					}
				}
				
				if ( processMessages.isEmpty() ) { 
					try {
						if ( doPricing > 0 && 
							 orl.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue() &&
							 orl.getWorkInst() != null) {
								orl = PricingServices.updateItemPrice(orl);								
						}
				 		if ( !eiw.getItem().isUpdatePricingOnly() &&
				 			 (orl.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue() ||
				 			  (savingCoiDetails && !savingMultipleDetails))) {						
							OrderLicenseServices.updateLicense(orl);
						} else {
							itemWrapper.setItem(orl);
						}
						processMessages.add(new ProcessMessage(Long.valueOf(orl.getID()).toString(),"SAVE","completed - " + orl.getLastUpdatedDate().toString() ));
					} catch (OrderLicensesException e) {
						LOGGER.warn(LogUtil.getStack(e));
						processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
					} catch (ServiceInvocationException e) {
						LOGGER.warn(LogUtil.getStack(e));
						processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
					}
				}	
			} else {
				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Unable to find detail record to update"));
			}
		} catch (OrderLicensesException e) {
			LOGGER.warn(LogUtil.getStack(e));
			processMessages.add(new ProcessMessage(new Long(itemWrapper.getItem().getID()).toString(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
		} catch (Exception e ) {
			LOGGER.error(LogUtil.getStack(e));
			processMessages.add(new ProcessMessage(new Long(itemWrapper.getItem().getID()).toString(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));			
		}
		
		return processMessages;
	}
	
	public static List<ProcessMessage> saveBundleOnConfirmationPage(EditBundle eb, OrderBundle orderBundle, OrderPurchase orderPurchase ) {
		
		List<OrderLicense> orderLicenseList = null;
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		
		if (eb.getNumberOfStudentsLong() == null || eb.getNumberOfStudentsLong().equals(0L)) {
			processMessages.add(new ProcessMessage(orderBundle.getCourseName(),"ERROR","Number of Students is empty"));
		}
		if (StringUtils.isEmpty(eb.getOrganization())) {
			processMessages.add(new ProcessMessage(orderBundle.getOrganization(),"ERROR","University/Institution is empty"));
		}
		if (StringUtils.isEmpty(eb.getStartOfTermStr())) {
			processMessages.add(new ProcessMessage(orderBundle.getStartOfTermStr(),"ERROR","Start of Term is empty"));
		} else if (trimmedDateValue(eb.getStartOfTermStr()) == null) {
			processMessages.add(new ProcessMessage(orderBundle.getStartOfTermStr(),"ERROR","Invalid date format for Start of Term"));
		}
		if (StringUtils.isEmpty(eb.getCourseName())) {
			processMessages.add(new ProcessMessage(orderBundle.getCourseName(),"ERROR","Course Name is empty"));
		}
				
		if ( !processMessages.isEmpty() ) {
			return processMessages;
		}
		
		OrderSearchResult orderSearchResult = null;
		OrderPurchase orderPurchaseForUpdate = null;
		if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
			try {
				orderSearchResult = OrderPurchaseServices.searchFullCOIOrderPurchaseByConfirmNumber(orderPurchase.getConfirmationNumber());
				if (orderSearchResult != null) {
					orderPurchaseForUpdate = orderSearchResult.getOrderPurchaseByConfirmId(orderPurchase.getConfirmationNumber());
					orderLicenseList = orderSearchResult.getOrderLicenses();
				}
			} catch (OrderPurchasesException e) {
				processMessages.add(new ProcessMessage(new Long(orderPurchase.getConfirmationNumber()).toString(),"ERROR","Unable to retrieve licenses for order: " + e.getMessage()));				
				
//					_logger.error( "Error retrieving getting full COI purchase for cascade update, confirm number: " + orderPurchaseId+ " " +
//								"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
//								"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
			}		
		} else {
			try
			{
				orderPurchaseForUpdate = orderPurchase;
				orderSearchResult = OrderLicenseServices.searchAllOrderLicensesForConfirmationNumber(orderPurchase.getConfirmationNumber());
				if (orderSearchResult != null) {
					orderLicenseList = orderSearchResult.getOrderLicenses();
				}
			} catch (OrderLicensesException e) {
				processMessages.add(new ProcessMessage(new Long(orderPurchase.getConfirmationNumber()).toString(),"ERROR","Unable to retrieve licenses for order: " + e.getMessage()));				
//					_logger.error( "Error retrieving licenses for cascade update, confirm number: " + orderPurchaseId + " " +
//								"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
//								"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
			}
		}
		
//		orderBundle.setAccountingReference(trimmedStringValue(eb.getAccountingReference()));
//		orderBundle.setComments(trimmedStringValue(eb.getComments()));
//		orderBundle.setCourseName(trimmedStringValue(eb.getCourseName()));
//		orderBundle.setCourseNumber(trimmedStringValue(eb.getCourseNumber()));
//		orderBundle.setInstructor(trimmedStringValue(eb.getInstructor()));
//		orderBundle.setOrderEnteredBy(trimmedStringValue(eb.getOrderEnteredBy()));
//		orderBundle.setOrganization(trimmedStringValue(eb.getOrganization()));
//		orderBundle.setStartOfTerm(trimmedDateValue(eb.getStartOfTermStr()));
//		orderBundle.setYourReference(trimmedStringValue(eb.getYourReference()));
			

//		if (orderBundle.getNumberOfStudents() == eb.getNumberOfStudents()) {			
//			if ( processMessages.isEmpty() ) {
//				try {
//					OrderPurchaseServices.updateOrderBundle(orderPurchaseForUpdate, orderBundle);
//					return processMessages;
//				} catch (Exception e) {
//					processMessages.add(new ProcessMessage(orderBundle.getCourseName(),"ERROR","Unable to update course: " + e.getMessage()));				
//					return processMessages;
//				}
//			} else {
//				return processMessages;
//			}
//		}
				
        boolean hasErrors = false;   
    	
    	if( orderLicenseList != null)
        {
            for(OrderLicense orderLicense : orderLicenseList) {          
    	        int originalNumberOfStudents = orderLicense.getNumberOfStudents();
                 if (orderLicense.isAcademic() && 
                     (orderLicense.getInvoiceId() == null ||
                      orderLicense.getInvoiceId().isEmpty()) &&
                	 !orderLicense.getItemStatusCd().equals(ItemConstants.CANCELED) &&
                	 !orderLicense.getItemStatusCd().equals(ItemStatusEnum.CANCELLED.name())) {
            	    if (orderLicense.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
            	    	if (orderLicense.getNumberOfStudents() != eb.getNumberOfStudentsLong().intValue()) {	
                    	    orderLicense.setNumberOfStudents( eb.getNumberOfStudentsLong().intValue());
            	    		try {
            	    			orderLicense=PricingServices.updateItemPrice(orderLicense);
    	        			} catch (ServiceInvocationException e) {
    	        				processMessages.add(new ProcessMessage(new Long(orderLicense.getID()).toString(),"ERROR","Unable to update pricing for license " + orderLicense.getID() + ": " + e.getMessage()));				
//	    	        			_logger.error(LogUtil.getStack(e));
	    	        			hasErrors = true;
    	        			}
            	    	}
    	        	} else {
    	        		try {
                       	    orderLicense.setNumberOfStudents( eb.getNumberOfStudentsLong().intValue());
	    	        		OrderLicenseServices.updateOrderLicense( orderLicense );		    	        			
    	        		} catch (OrderLicensesException e) {
    	    				processMessages.add(new ProcessMessage(new Long(orderLicense.getID()).toString(),"ERROR","Unable to update license " + orderLicense.getID() + ": " + e.getMessage()));				
//			    	    		_logger.error( "Error updating license for cascade update, licenseId: " + orderLicense.getID() + " " +
//			    						"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
//			    		                "Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
			    	        hasErrors = true;
			    	    }
			        }
	    	    }
                
    	        if( hasErrors )
  		          orderLicense.setNumberOfStudents( originalNumberOfStudents );
            } 
        }
		
		if (orderPurchaseForUpdate != null && orderPurchaseForUpdate.getOrderBundles() != null) {
			for (OrderBundle updateOrderBundle : orderPurchaseForUpdate.getOrderBundles()) {
				if (updateOrderBundle.getBundleId() == eb.getBundleId()) {
					updateOrderBundle.setAccountingReference(trimmedStringValue(eb.getAccountingReference()));
					updateOrderBundle.setComments(trimmedStringValue(eb.getComments()));
					updateOrderBundle.setCourseName(trimmedStringValue(eb.getCourseName()));
					updateOrderBundle.setCourseNumber(trimmedStringValue(eb.getCourseNumber()));
					updateOrderBundle.setInstructor(trimmedStringValue(eb.getInstructor()));
					updateOrderBundle.setOrderEnteredBy(trimmedStringValue(eb.getOrderEnteredBy()));
					updateOrderBundle.setOrganization(trimmedStringValue(eb.getOrganization()));
					updateOrderBundle.setStartOfTerm(trimmedDateValue(eb.getStartOfTermStr()));
					updateOrderBundle.setYourReference(trimmedStringValue(eb.getYourReference()));
					updateOrderBundle.setNumberOfStudents(eb.getNumberOfStudents());					
					if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.OMS.getOrderDataSourceId().intValue()) {
						try {
							OrderPurchaseServices.updateFullCOIOrderPurchase(orderPurchaseForUpdate);
						} catch (OrderPurchasesException e) {
							processMessages.add(new ProcessMessage(orderBundle.getCourseName(),"ERROR","Unable to update course and students: " + e.getMessage()));				
							LOGGER.error( "Error updating full COI purchase/licenses for cascade update, confirm number: " + orderPurchaseForUpdate.getOrderHeaderId()+ " " +
									"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
									"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
						}
						break;
					}		
					if (orderPurchase.getOrderDataSource() == OrderDataSourceEnum.TF.getOrderDataSourceId().intValue()) {
						try
						{
							OrderPurchaseServices.updateOrderBundle( orderPurchase, updateOrderBundle );
						} 
						catch (OrderPurchasesException e)
						{
							processMessages.add(new ProcessMessage(orderBundle.getCourseName(),"ERROR","Unable to update course: " + e.getMessage()));				
							LOGGER.error( "Error updating COI purchase/licenses for cascade update, confirm number: " + orderPurchaseForUpdate.getOrderHeaderId() + " " +
									"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
									"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
//							_logger.error( LogUtil.getStack(e) );

						}
						break;
					}
				}
			}
		}
		return processMessages;
    }         
	
	public static List<ProcessMessage> saveCoiDetailsInBulk (OrderSearchResultWrapper[] wrappedResultsArray) {
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
	
		long confirmNumber = 0;
		for (OrderSearchResultWrapper orderSearchResultWrapper : wrappedResultsArray) {
			if (orderSearchResultWrapper.getSvcSearchResults() != null) {
				for (OrderPurchase orderPurchase : orderSearchResultWrapper.getSvcSearchResults().getOrderPurchases()) {
					confirmNumber = orderPurchase.getConfirmationNumber();
					break;
				}
			}
			if (confirmNumber > 0) {
				break;
			}
		}
				
		OrderSearchResult orderSearchResult = null;
		OrderPurchase orderPurchaseForUpdate = null;
		OrderHeaderImpl orderHeaderImpl = null;

		try {
			orderSearchResult = OrderPurchaseServices.searchFullCOIOrderPurchaseByConfirmNumber(confirmNumber);
			if (orderSearchResult != null) {
				orderPurchaseForUpdate = orderSearchResult.getOrderPurchaseByConfirmId(confirmNumber);
				orderHeaderImpl = (OrderHeaderImpl) orderPurchaseForUpdate;
			}
		} catch (OrderPurchasesException e) {
			processMessages.add(new ProcessMessage(new Long(confirmNumber).toString(),"ERROR","Unable to retrieve order for update: " + e.getMessage()));				
			return processMessages;
		}		
		
		for (OrderSearchResultWrapper orderSearchResultWrapper : wrappedResultsArray) {
			for (ItemWrapper itemWrapper : orderSearchResultWrapper.getDetailsList()) {
				if (itemWrapper.isToBeSaved()) {
					for (Item item : orderHeaderImpl.getOrderHeader().getItems()) {
						OrderItemImpl updatedOrderItemImpl = (OrderItemImpl) itemWrapper.getItem();
						Item updatedItem = updatedOrderItemImpl.getItem();
						if (item.getItemId().equals(updatedItem.getItemId())) {
							orderHeaderImpl.getOrderHeader().getItems().remove(item);
							orderHeaderImpl.getOrderHeader().getItems().add(updatedItem);
							break;
						}
					}
				}
			}
		}

		try {
			OrderPurchaseServices.updateFullCOIOrderPurchase(orderPurchaseForUpdate);
		} catch (OrderPurchasesException e) {
			processMessages.add(new ProcessMessage(new Long(confirmNumber).toString(),"ERROR","Unable to update licenses for purchase: " + e.getMessage()));				
			LOGGER.error( "Error updating full COI purchase/licenses for cascade update, confirm number: " + orderPurchaseForUpdate.getOrderHeaderId()+ " " +
					"Message Code: " + e.getMessageCode() + " Message: " + e.getMessage() + " "	 +
					"Stack Trace: " + ExceptionUtils.getFullStackTrace(e));
		}

		
		return processMessages;
	}
	
	
	private static final String PRICING_REQUIRED_FLAG = "PRICING-REQUIRED";
	
	  /**
	 * @param orl
	 * @param eiw
	 */
	private static void setAcademicParameters( OrderLicense orl, EditItemWrapper eiw )
	  {   

		orl.setPublicationYearOfUse( eiw.getItem().getPublicationYearOfUse() );
  
		if ( trimmedLongValue( eiw.getItem().getNumberOfPages()) != null ) {
			orl.setNumberOfPages( trimmedLongValue( eiw.getItem().getNumberOfPages() ).longValue() );
		}
		if ( trimmedLongValue( eiw.getItem().getNumberOfStudents()) != null )
		{
			orl.setNumberOfStudents(Integer.parseInt(eiw.getItem().getNumberOfStudents()));
		}
		orl.setChapterArticle( eiw.getItem().getChapterArticle() );
	    orl.setCustomAuthor( eiw.getItem().getCustomAuthor());
		orl.setCustomVolume( eiw.getItem().getCustomVolume() );						
		orl.setPageRange( eiw.getItem().getPageRange() );
		orl.setDateOfIssue( eiw.getItem().getDateOfIssue() );
		orl.setCustomEdition( eiw.getItem().getCustomEdition() );
		orl.setCustomerReference(eiw.getItem().getCustomerReference());

	  }

	  private static void setRepublicationParameters( OrderLicense orl, EditItemWrapper eiw )
	  {
	   
	    //pubDate
		if ( trimmedDateValue(eiw.getItem().getPublicationDateOfUse()) != null ) {
			orl.setPublicationDateOfUse( trimmedDateValue(eiw.getItem().getPublicationDateOfUse()));
		}
  
	    //rlsPages
		if ( trimmedLongValue( eiw.getItem().getNumberOfPages()) != null ) {
			orl.setNumberOfPages( trimmedLongValue( eiw.getItem().getNumberOfPages() ).longValue() );
		}
	    
	    //chapterArticle
		orl.setChapterArticle( eiw.getItem().getChapterArticle() );
	    
		//forProfit
		if ( StringUtils.isNotEmpty( trimmedStringValue(eiw.getItem().getBusiness()) ) ) {
			orl.setBusiness( trimmedStringValue(eiw.getItem().getBusiness()));
		}
	    
	    //content types...
	    orl.setTypeOfContent(eiw.getItem().getTypeOfContent()); 
	       
	    //origAuthor
	    orl.setSubmitterAuthor(Boolean.parseBoolean(eiw.getItem().getSubmitterAuthor()));  // TODO This field will come into play
	    
	    //publicationDate
//	    orl.setContentsPublicationDate( eiw.getContentsPublicationDate() ); //TODO This field will come into play
	     
	    //hdrRepubPub
		orl.setRepublishingOrganization( trimmedStringValue(eiw.getItem().getRepublishingOrganization()));
	    
	    //repubTitle
	    orl.setNewPublicationTitle( eiw.getItem().getRepubWork() ); // TODO This field will come into play
	    
	    //repubDate
		if ( trimmedDateValue(eiw.getItem().getRepublicationDate()) != null ) {
			orl.setRepublicationDate( trimmedDateValue(eiw.getItem().getRepublicationDate()));
		}
		
	    //repubPublisher
		if ( trimmedDateValue(eiw.getItem().getRepublishingOrganization()) != null ) {
			orl.setRepublishingOrganization( trimmedStringValue(eiw.getItem().getRepublishingOrganization()));
		}
	     
	    //language & translated
		if ( trimmedStringValue(eiw.getItem().getTranslationLanguage()) != null ) {
			orl.setTranslationLanguage( trimmedStringValue(eiw.getItem().getTranslationLanguage()));
		}
	        
	    //lcnHdrRefNum
		orl.setCustomerReference( eiw.getItem().getCustomerReference() );
	    
	    //author
		orl.setCustomAuthor( eiw.getItem().getCustomAuthor() );
	    
		orl.setPageRange( eiw.getItem().getPageRange() );

		if ( trimmedIntegerValue(eiw.getItem().getCirculationDistribution()) != null ) {
			orl.setCirculationDistribution( trimmedIntegerValue(eiw.getItem().getCirculationDistribution()));
		}
	  }

	  private static void setPhotocopyParameters(OrderLicense orl, EditItemWrapper eiw)
	  {
	    
		orl.setPublicationYearOfUse( eiw.getItem().getPublicationYearOfUse() );
		if ( trimmedLongValue( eiw.getItem().getNumberOfCopies()) != null ) {
			orl.setNumberOfCopies( trimmedLongValue(eiw.getItem().getNumberOfCopies()).longValue() );
		}
		if ( trimmedLongValue( eiw.getItem().getNumberOfPages()) != null ) {
			orl.setNumberOfPages( trimmedLongValue( eiw.getItem().getNumberOfPages() ).longValue() );
		}
		orl.setChapterArticle( eiw.getItem().getChapterArticle() );
	    orl.setCustomAuthor( eiw.getItem().getCustomAuthor() );
	    orl.setCustomEdition( eiw.getItem().getCustomEdition() );
		orl.setCustomVolume( eiw.getItem().getCustomVolume() );						
		orl.setCustomerReference( eiw.getItem().getCustomerReference() );	
	  }

	  private static void setEmailParameters(OrderLicense orl, EditItemWrapper eiw)
	  {   
	    
		orl.setPublicationYearOfUse( eiw.getItem().getPublicationYearOfUse() );
		orl.setChapterArticle( eiw.getItem().getChapterArticle() );
		if ( trimmedLongValue(eiw.getItem().getNumberOfRecipients()) != null ) {
			orl.setNumberOfRecipients( trimmedLongValue(eiw.getItem().getNumberOfRecipients()));
		}
		orl.setDateOfUse( trimmedDateValue( eiw.getItem().getDateOfUse()));
		orl.setCustomerReference(eiw.getItem().getCustomerReference());
	    orl.setCustomAuthor( eiw.getItem().getCustomAuthor() );	  
	    orl.setEditor(eiw.getItem().getEditor());	    
	  }

	  private static void setNetParameters(OrderLicense orl, EditItemWrapper eiw)
	  {
    
		orl.setPublicationYearOfUse( eiw.getItem().getPublicationYearOfUse() );
		orl.setChapterArticle( eiw.getItem().getChapterArticle() );
		orl.setCustomAuthor(eiw.getItem().getCustomAuthor());
		if ( trimmedIntegerValue(eiw.getItem().getDuration()) != null ) {
			orl.setDuration( trimmedIntegerValue(eiw.getItem().getDuration()));
		}
		orl.setDateOfUse( trimmedDateValue( eiw.getItem().getDateOfUse()));
		orl.setWebAddress(eiw.getItem().getWebAddress());
		orl.setEditor(eiw.getItem().getEditor());
		orl.setCustomerReference(eiw.getItem().getCustomerReference());
	  }

	  private static List<ProcessMessage> checkIfPricingUpdateIsNeededAcademic( EditItemWrapper eiw, boolean isInvoiceView, OrderLicense current, OrderLicense last ) {
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		if ( !isInvoiceView ) {
				
				int dirty = 0;
				
				try {
					dirty += isChanged(current.getWorkInst(),last.getWorkInst());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","TF Work ID value is missing"));			
				}
				try {
					dirty += isChanged(current.getPublicationYearOfUse(),last.getPublicationYearOfUse());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Publication Year value is missing"));			
				}
				try {
					dirty += isChanged(current.getNumberOfPages(),last.getNumberOfPages());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Number of Pages value is missing"));
				}
				try {
					dirty += isChanged(current.getNumberOfCopies(),last.getNumberOfCopies());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERRROR","Number of Copies value is missing"));
				}
				
				if ( dirty > 0 ) {
					processMessages.add( new ProcessMessage(eiw.getItem().getID(),PRICING_REQUIRED_FLAG,"Pricing needs to be updated"));
				}
		}
		
		return processMessages;
		
	}
	
	private static List<ProcessMessage> checkIfPricingUpdateIsNeededRepublication( EditItemWrapper eiw, boolean isInvoiceView, OrderLicense current, OrderLicense last ) {
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		if ( !isInvoiceView ) {
//			try{
		
//				OrderLicense last = getOrderLicenseForDetailId( eiw.getItem().getID() );
				
				int dirty = 0;
				
				try {
					dirty += isChanged(current.getWorkInst(),last.getWorkInst());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","TF Work ID value is missing"));			
				}
				try {
					dirty += isChanged(current.getPublicationYearOfUse(),last.getPublicationYearOfUse());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Publication Year value is missing"));			
				}
				try {
					dirty += isChanged(current.getNumberOfPages(),last.getNumberOfPages());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Number of Pages value is missing"));
				}
				try {
					dirty += isChanged(current.getCirculationDistribution(),last.getCirculationDistribution());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Circ/Dist value is missing"));
				}
				try {
					dirty += isChanged(current.getBusiness(),last.getBusiness());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","For Profit value is missing"));
				}
				try {
					dirty += isChanged(current.getTypeOfContent(),last.getTypeOfContent());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Type of Content value is missing"));
				}
				try {
					dirty += isChanged(current.getSubmitterAuthor(),last.getSubmitterAuthor());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Submitter Author value is missing"));
				}
				try {
					dirty += isChanged(current.getLanguage(),last.getLanguage());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Language value is missing"));
				}
				try {
					dirty += isChanged(current.getTypeOfContentDescription(),last.getTypeOfContentDescription());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Type of Content Description value is missing"));
				}
				try {
					dirty += isChanged(current.getNumberOfQuotes(),last.getNumberOfQuotes());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfLogos(),last.getNumberOfLogos());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfGraphs(),last.getNumberOfGraphs());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfPhotos(),last.getNumberOfPhotos());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfIllustrations(),last.getNumberOfIllustrations());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfCartoons(),last.getNumberOfCartoons());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfCharts(),last.getNumberOfCharts());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				try {
					dirty += isChanged(current.getNumberOfExcerpts(),last.getNumberOfExcerpts());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
				}
				
				if ( dirty > 0 ) {
					processMessages.add( new ProcessMessage(eiw.getItem().getID(),PRICING_REQUIRED_FLAG,"Pricing needs to be updated"));
				}
//			} catch (OrderLicensesException e) {
//				LOGGER.warn(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
//			} catch (Exception e ) {
//				LOGGER.error(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));			
//			}
		}
		
		return processMessages;
		
	}
	
	private static List<ProcessMessage> checkIfPricingUpdateIsNeededPhotocopy( EditItemWrapper eiw, boolean isInvoiceView, OrderLicense current, OrderLicense last ) {
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		if ( !isInvoiceView ) {
//			try{
		
//				OrderLicense last = getOrderLicenseForDetailId( eiw.getItem().getID() );
				
				int dirty = 0;
				
				try {
					dirty += isChanged(current.getWorkInst(),last.getWorkInst());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","TF Work ID value is missing"));			
				}
				try {
					dirty += isChanged(current.getPublicationYearOfUse(),last.getPublicationYearOfUse());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Publication Year value is missing"));			
				}
				try {
					dirty += isChanged(current.getNumberOfCopies(),last.getNumberOfCopies());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERRROR","Number of Copies value is missing"));
				}
				try {
					dirty += isChanged(current.getNumberOfPages(),last.getNumberOfPages());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Number of Pages value is missing"));
				}
				
				if ( dirty > 0 ) {
					processMessages.add( new ProcessMessage(eiw.getItem().getID(),PRICING_REQUIRED_FLAG,"Pricing needs to be updated"));
				}
//			} catch (OrderLicensesException e) {
//				LOGGER.warn(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
//			} catch (Exception e ) {
//				LOGGER.error(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));			
//			}
		}
		
		return processMessages;
		
	}
	
	private static List<ProcessMessage> checkIfPricingUpdateIsNeededNet( EditItemWrapper eiw, boolean isInvoiceView, OrderLicense current, OrderLicense last ) {
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		if ( !isInvoiceView ) {
//			try{
		
//				OrderLicense last = getOrderLicenseForDetailId( eiw.getItem().getID() );
				
				int dirty = 0;
				
				try {
					dirty += isChanged(current.getWorkInst(),last.getWorkInst());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","TF Work ID value is missing"));			
				}
				try {
					dirty += isChanged(current.getPublicationYearOfUse(),last.getPublicationYearOfUse());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Publication Year value is missing"));			
				}
				try {
					dirty += isChanged(current.getDuration(),last.getDuration());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Duration value is missing"));
				}
				
				if ( dirty > 0 ) {
					processMessages.add( new ProcessMessage(eiw.getItem().getID(),PRICING_REQUIRED_FLAG,"Pricing needs to be updated"));
				}
//			} catch (OrderLicensesException e) {
//				LOGGER.warn(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
//			} catch (Exception e ) {
//				LOGGER.error(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));			
//			}
		}
		
		return processMessages;
		
	}
	
	private static List<ProcessMessage> checkIfPricingUpdateIsNeededEmail( EditItemWrapper eiw, boolean isInvoiceView, OrderLicense current, OrderLicense last ) {
		
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		if ( !isInvoiceView ) {
//			try{
		
//				OrderLicense last = getOrderLicenseForDetailId( eiw.getItem().getID() );
				
				int dirty = 0;
				
				try {
					dirty += isChanged(current.getWorkInst(),last.getWorkInst());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","TF Work ID value is missing"));			
				}
				try {
					dirty += isChanged(current.getPublicationYearOfUse(),last.getPublicationYearOfUse());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Publication Year value is missing"));			
				}
				try {
					dirty += isChanged(current.getNumberOfRecipients(),last.getNumberOfRecipients());
				} catch (Exception e) {
					LOGGER.warn(LogUtil.getStack(e));
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Number of Recipients value is missing"));
				}
				
				if ( dirty > 0 ) {
					processMessages.add( new ProcessMessage(eiw.getItem().getID(),PRICING_REQUIRED_FLAG,"Pricing needs to be updated"));
				}
//			} catch (OrderLicensesException e) {
//				LOGGER.warn(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));
//			} catch (Exception e ) {
//				LOGGER.warn(LogUtil.getStack(e));
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR",ExceptionUtils.getRootCauseMessage(e)));			
//			}
		}
		
		return processMessages;
		
	}
	private static List<ProcessMessage> validateDetailForSave(OrderLicense orl, EditItemWrapper eiw, boolean isInvoiceView) {
		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();

		if ( isInvoiceView ) 
		{
//			if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getRhRefNum())) ) {
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "RH Ref Number is missing") );
//			}
		} 
		else 
		{
//			if (orl.isCanceled()) {
//				processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Order detail has been canceled") );
//				return processMessages;
//			}
			
			if ( orl.isAPS() || orl.isECCS())
			{
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getCustomAuthor())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Author is required") );
				} 				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPageRange())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Page Range is required") );
				} 				
			}
			if ( orl.isAPS() || orl.isECCS() ) {
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfStudents(), "Number of Students", true /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfPages(), "Number of Pages", true /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationYearOfUse())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is required") );
				} 				
			}
			
			if ( orl.isPhotocopy() ) {
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfCopies(), "Number of Copies", true /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfPages(), "Number of Pages", true /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationYearOfUse())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is required") );
				} 				
			}
			
			if ( orl.isEmail() ) {
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfRecipients(), "Number of Recipients", true /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getDateOfUse() ) ) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Date Used is required") );
				} else {
					trimmedDateValue(eiw.getItem().getDateOfUse(), processMessages, eiw.getItem().getID(), "Date Used is not mm/dd/yyyy date");
				}				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationYearOfUse())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is required") );
				} 				
			}
			if ( orl.isInternet() ) {
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getDuration(), "Duration", true /*isRequired*/, Integer.valueOf(0) /*numberClass*/ ) );
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getDateOfUse() ) ) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Date Used is required") );
				} else {
					trimmedDateValue(eiw.getItem().getDateOfUse(), processMessages, eiw.getItem().getID(), "Date Used is not mm/dd/yyyy date");
				}
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationYearOfUse())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is required") );
				} 				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getWebAddress())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Web Addresse is required") );
				} 	
			}
			if ( orl.isIntranet()  || orl.isExtranet() ) {
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getDuration(), "Duration", true /*isRequired*/, Integer.valueOf(0) /*numberClass*/ ) );
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getDateOfUse() ) ) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Date Used is required") );
				} else {
					trimmedDateValue(eiw.getItem().getDateOfUse(), processMessages, eiw.getItem().getID(), "Date Used is not mm/dd/yyyy date");
				}
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationYearOfUse())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is required") );
				} 				
			}
		
			if ( orl.isRepublication() ) {
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getCirculationDistribution(), "Circ/Dist", true /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );

				if (Long.valueOf(eiw.getItem().getCirculationDistribution()) < 1)
				{
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Circ/Dist must be greater than zero") );					
				}
				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getRepublicationDate())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Repub Date is required") );
				} else {				
					trimmedDateValue(eiw.getItem().getRepublicationDate(), processMessages, eiw.getItem().getID(), "Repub Date is not mm/dd/yyyy date");
				}
				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationDateOfUse())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is required") );
				} else {				
					trimmedDateValue(eiw.getItem().getPublicationDateOfUse(), processMessages, eiw.getItem().getID(), "Pub Date is not mm/dd/yyyy date");
				}

				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getRepublishingOrganization())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Repub Publisher is required") );
				} 

				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getTypeOfContent())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Content Type is required") );
				} else if (eiw.getItem().getTypeOfContent().equals(RepublicationConstants.CONTENT_SELECTED_PAGES) 
						&& Integer.valueOf(StringUtils.trimToEmpty(eiw.getItem().getNumberOfPages())) < 1) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR","Number of Pages must be greater than 0"));
				}

				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getSubmitterAuthor())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Is perm requestor original author is required") );
				} 
				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getChapterArticle())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Content Desc is required") );
				} 
				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPageRange())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Page Range is required") );
				}
				
				if ( StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getCustomAuthor())) ) {
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Author is required") );
				} 					
				
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfPages(), "Number of Pages", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
//				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfCopies(), "Number of Copies", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
//				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfStudents(), "Number of Students", false /*isRequired*/, Integer.valueOf(0) /*numberClass*/ ) );
//				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfRecipients(), "Number of Recipients", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfCartoons(), "Number of Cartoons", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfCharts(), "Number of Charts", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfExcerpts(), "Number of Excerpts", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfFigures(), "Number of Figures", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfGraphs(), "Number of Graphs", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfIllustrations(), "Number of Illustrations", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfLogos(), "Number of Logos", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfPhotos(), "Number of Photos", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				processMessages.addAll( checkNumberOf( eiw.getItem().getID(), eiw.getItem().getNumberOfQuotes(), "Number of Quotes", false /*isRequired*/, Long.valueOf(0) /*numberClass*/ ) );
				
			}
			
			// for all products validate pub year if we have it (the check for if we have it is above under the product specific checks)
			if ( eiw.getItem().getPublicationYearOfUse() != null && !StringUtils.isEmpty( StringUtils.trimToEmpty(eiw.getItem().getPublicationYearOfUse())) ) 
			{				
				if ( !isPublicationDateValid(eiw.getItem().getPublicationYearOfUse() ) ) 
				{
					processMessages.add(new ProcessMessage(eiw.getItem().getID(),"ERROR", "Pub Date is not yyyy or yyyy/mm or yyyy/mm/dd date") );
				}
			}
//			trimmedLongValue(eiw.getItem().getWrWorkInst(), processMessages, eiw.getItem().getID(), "WR Work Id is not numeric");
//			trimmedLongValue(eiw.getItem().getWorkInst(), processMessages, eiw.getItem().getID(), "TF Work Id is not numeric");
			
		}
		
		return processMessages;
	}
	
	private static List<ProcessMessage> checkNumberOf( String detailId, String numberOf, String messageText, boolean isRequired, Object numberClass ) {

		List<ProcessMessage> processMessages = new ArrayList<ProcessMessage>();
		if ( StringUtils.isEmpty( StringUtils.trimToEmpty(numberOf)) ) {
			if ( isRequired ) {
				processMessages.add(new ProcessMessage(detailId,"ERROR", messageText + " is required") );
			}
		} else {
			if ( numberClass instanceof Long ) {
				trimmedLongValue( numberOf, processMessages, detailId, messageText + " is not numeric");
			}
			if ( numberClass instanceof Integer ) {
				trimmedIntegerValue( numberOf, processMessages, detailId, messageText + " is not numeric");
			}
		}
		return processMessages;	
	}
	
	private static Long trimmedLongValue(String value, List<ProcessMessage> processMessages, String detailId, String errorMessage) {
		
		Long lValue = trimmedLongValue(value);
		if ( lValue != null ) {
			return lValue;
		} else {
			processMessages.add(new ProcessMessage(detailId, "ERROR", errorMessage) );
			return null;
		}
	}
	
	private static Integer trimmedIntegerValue(String value, List<ProcessMessage> processMessages, String detailId, String errorMessage) {
		
		Integer iValue = trimmedIntegerValue(value);
		if ( iValue != null ) {
			return iValue;
		} else {
			processMessages.add(new ProcessMessage(detailId, "ERROR", errorMessage) );
			return null;
		}
	}
	private static Date trimmedDateValue(String value, List<ProcessMessage> processMessages, String detailId, String errorMessage) {
		String[] dFormats = {"MM/dd/yyyy"};
		return trimmedDateValue(value, processMessages, detailId, errorMessage, dFormats);
	}	

	private static Date trimmedDateValue(String value, List<ProcessMessage> processMessages, String detailId, String errorMessage, String[] dateFormats) {
		
		Date dValue = trimmedDateValue(value, dateFormats);
		if ( dValue != null ) {
			return dValue;
		} else {
			processMessages.add(new ProcessMessage(detailId, "ERROR", errorMessage) );
			return null;
		}
	}
	
	public static Long trimmedLongValue(String value) {
		if ( StringUtils.isNotEmpty(value)) {
			if ( StringUtils.isNotEmpty( value.trim() ) ) {
				if ( StringUtils.isNumeric( value.trim() ) ) {
					return Long.valueOf(value.trim());
				} 
			}
		}
		return null;
	}	
	private static Integer trimmedIntegerValue(String value) {
		if ( StringUtils.isNotEmpty(value)) {
			if ( StringUtils.isNotEmpty( value.trim() ) ) {
				if ( StringUtils.isNumeric( value.trim() ) ) {
					return Integer.valueOf(value.trim());
				} 
			}
		}
		return null;
	}	
	private static String trimmedStringValue(String value) {
		if ( StringUtils.isNotEmpty(value)) {
			if ( StringUtils.isNotEmpty( value.trim() ) ) {
				return value.trim(); 
			}
		}
		return null;
	}	
	
	private static final String[] DEFAULT_DATE_FORMATS = {"MM/dd/yyyy","M/dd/yyyy","M/d/yyyy","MM/d/yyyy"};
	
	private static Date trimmedDateValue(String value) {
		return trimmedDateValue(value, DEFAULT_DATE_FORMATS);
	}
	
	private static Date trimmedDateValue(String value, String[] formats) {
		if ( StringUtils.isNotEmpty(value)) {
			if ( StringUtils.isNotEmpty( value.trim() ) ) {
				String[] dFormats = DEFAULT_DATE_FORMATS;
				if ( formats != null && formats.length > 0 ) {
					dFormats = formats;
				}
				Date dValue = parseDate(value, dFormats);
				if ( dValue != null ) {
					return dValue;
				}
			}
		}
		return null;
	}	

	/**
	 * Determins if the publication date entered by the user is valid.
	 * 
	 * Valid pub dates are yyyy or yyyy/MM or yyyy/MM/dd
	 * 
	 * @param publicationDate
	 * @return
	 */
	private final static String DATE_SEPARATOR = "/";

	private static boolean isPublicationDateValid(String publicationDate)
	{
		String trimmed = StringUtils.trimToEmpty(publicationDate);
		
		if(StringUtils.isEmpty(trimmed))
			return true;
		
		String[] tokens = trimmed.split(DATE_SEPARATOR);
		
		if(tokens.length > 3)
			return false;
		
		int month = 0;
		
		for(int i = 0; i < tokens.length; i++)
		{
			String token = tokens[i];
			
			if((i == 0 && token.length() != 4) || (i > 0 && token.length() != 2))
				return false;
			
			try
			{
				int tokenInt = Integer.parseInt(token);
				
				if(tokenInt < 0)
					return false;
				
				//year
				else if(i == 0 && (tokenInt < 1000 || tokenInt > 3000))
					return false;
				//month
				else if(i == 1)
				{
					month = tokenInt;
					
					if(tokenInt > 12)
						return false;
				}
				//date
				else if(i == 2)
				{
					if((tokenInt > 0 && month <= 0) || tokenInt > 31)
						return false;
				}
			}
			catch(NumberFormatException nfe)
			{
				return false;
			}
		}
		
		return true;
	}

	/**
	 * parses input date string based on input list of formats
	 * will return null if the input string fails to match any
	 * of the formats.
	 */
	private static Date parseDate(String input, String[] formats)
    {
        Date parsedDate = null;

		if (input != null && formats !=null)
		{
	        for (int i=0; i<formats.length && parsedDate == null; i++)
	        {
	        	if (formats[i] != null)
	        	{
		            try {
						SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
						sdf.setLenient(false);
						parsedDate = sdf.parse(input);
					} catch (ParseException e) {
						continue;
					}
	        	}
	        }
		}
		
        return parsedDate;
    }
	
    private static int isChanged(long current, long last) {
    	return (current!=last)?1:0;
	}
    
    private static int isChanged(int current, int last) {
    	return (current!=last)?1:0;
	}

    private static int isChanged(Long current, Long last) {
		if ( current == null ) {
			if ( last == null ) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if ( last == null ) {
				return 1;
			} else {
				return (!(current.equals(last)))?1:0;
			}
		}
	}
	
	private static int isChanged(String current, String last) {
		if ( StringUtils.isEmpty(current) ) {
			if ( StringUtils.isEmpty(last) ) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if ( StringUtils.isEmpty( last) ) {
				return 1;
			} else {
				return (!(current.equals(last)))?1:0;
			}
		}
	}
    /**
	 * Replaces single quotes with javascript escape single quotes.
	 * 
	 * @param stringToEscape
	 */
	public static String escapeForJavascript(String stringToEscape)
	{
		return stringToEscape.replaceAll("'", "\\\\'");
	}
	
	public static List<Long> getBundleIdsForOMSConfirmNumber( Long confirmNumber ) {
		List<Long> bundleIds = new ArrayList<Long>();
		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
		orderSearchCriteria.setConfirmNumber(confirmNumber);
		orderSearchCriteria.setDataSource(OrderDataSourceEnum.OMS.name());
		OrderSearchResult orderSearchResult;
		try {
			orderSearchResult = OrderPurchaseServices.searchCOIOrderPurchases(orderSearchCriteria);
		} catch (OrderPurchasesException e) {
			return bundleIds;
		}

		if (orderSearchResult != null) {
			OrderPurchase orderPurchase = orderSearchResult.getOrderPurchaseByConfirmId(confirmNumber);
			if (orderPurchase != null) {
				OrderHeaderImpl orderHeaderImpl = (OrderHeaderImpl) orderPurchase;
				if (orderHeaderImpl.getOrderBundles() != null) {
					for (OrderBundle orderBundle : orderHeaderImpl.getOrderBundles()) {
						Long bId = new Long(orderBundle.getBundleId());
						if ( !bundleIds.contains(bId) ) {
							bundleIds.add(bId);
						}
					}
				}
			}
		}
		
        return bundleIds;		
	}
	public static List<Long> getBundleIdsForTFConfirmNumber( Long confirmNumber ) {
		List<Long> bundleIds = new ArrayList<Long>();
		OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
		orderSearchCriteria.setConfirmNumber(confirmNumber);
		orderSearchCriteria.setDataSource(OrderDataSourceEnum.TF.name());
		OrderSearchResult orderSearchResult;
		try {
			orderSearchResult = OrderLicenseServices.searchOrderLicenses(orderSearchCriteria);
		} catch (OrderLicensesException e) {
			return bundleIds;
		}

		if (orderSearchResult != null) {
			if ( orderSearchResult.getOrderBundles() != null && !orderSearchResult.getOrderBundles().isEmpty() ) {
				for ( Map.Entry<Long,OrderBundle> entry : orderSearchResult.getOrderBundles().entrySet() ) {
					Long bId = entry.getKey();
					if ( !bundleIds.contains(bId) ) {
						bundleIds.add(bId);
					}
				}
			}
		}
		return bundleIds;		
	}

    public static void initWorkForOmsLicense ( OrderLicense orderLicense, Long wrWorkInst, Long externalItemId)
    {
 	
    	OrderItemImpl orderItemImpl = (OrderItemImpl) orderLicense;
    	
    	WorkExternal workExternal = null;
    	
    	if ( wrWorkInst != null ) {
    		workExternal = getWorkForWrWorkInst(wrWorkInst);
    	} else if (externalItemId != null) {
    		workExternal = getWorkForWrWorkInst(externalItemId);    		
    	}
    	
    	if (workExternal != null) {
        	orderItemImpl.setItemTypeCd(workExternal.getPublicationType());
        	orderItemImpl.setItemSourceCd(ItemConstants.ITEM_SOURCE_CD_WR);
        	orderItemImpl.setItemSourceKey(workExternal.getPrimaryKey());
        	orderItemImpl.setExternalItemId(workExternal.getTfWrkInst());
        	orderItemImpl.setStandardNumber( workExternal.getIdnoWop());
        	orderItemImpl.setItemDescription( workExternal.getFullTitle());
        	orderItemImpl.setAuthor( workExternal.getAuthorName());
        	orderItemImpl.setEditor( workExternal.getEditorName());
        	orderItemImpl.setVolume( workExternal.getVolume() );
        	orderItemImpl.setEdition( workExternal.getEdition() );
        	orderItemImpl.setPublisher( workExternal.getPublisherName());

        	orderItemImpl.setItemSourceKey(workExternal.getPrimaryKey());   
        	orderItemImpl.setSeries(workExternal.getSeries());
        	orderItemImpl.setSeriesNumber(workExternal.getSeriesNumber());
        	orderItemImpl.setPublicationType(workExternal.getPublicationType());
        	orderItemImpl.setCountry(workExternal.getCountry());
        	orderItemImpl.setLanguage(workExternal.getLanguage());
        	orderItemImpl.setIdnoLabel(workExternal.getIdnoTypeCode());
        	orderItemImpl.setIdnoTypeCd(workExternal.getIdnoTypeCode());
        	try {orderItemImpl.setPages(workExternal.getPages().toString()); } catch (Exception e) {};
    	}
    	
 	}
    private static WorkExternal getWorkForWrWorkInst(Long wrWorkInst) 
	{
		  List<String> sortList = new ArrayList<String>();
		  int page = 1;
		  int pageSize = 100;
		
		  PublicationSearch publicationSearch = new PublicationSearch();
		  publicationSearch.setWrWrkInst(wrWorkInst.toString());
		  SearchRetrievalResult result = ServiceLocator.getSearchRetrievalService().searchPublication(new SearchRetrievalConsumerContext(), publicationSearch, sortList, page, pageSize);
		  List<WorkExternal> lstWork = result.getWorks();
  		  
	      if (lstWork == null || lstWork.size() == 0) {
	          //didn't find the WR work inst
	          return null;
	      }
	      return lstWork.get(0);
	}
	  
}
