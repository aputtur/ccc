package com.copyright.ccc.business.services.order;

import java.util.Date;

import org.apache.log4j.Logger;

import com.copyright.ccc.business.data.OrderLicense;
import com.copyright.data.inventory.Right;
import com.copyright.data.inventory.StandardWork;
import com.copyright.data.inventory.Work;
import com.copyright.data.order.License;
import com.copyright.data.order.ProductUsageConstants;
import com.copyright.workbench.i18n.Money;
import com.copyright.workbench.logging.LoggerHelper;

public class LicenseWrapper extends License 
{
	private static final long serialVersionUID = 1L;

	private static final Logger _logger = LoggerHelper.getLogger();
	
	private OrderLicense ordrLicense;

	// special order indicators

	public OrderLicense getOrdrLicense() {
		return ordrLicense;
	}

	public void setOrdrLicense(OrderItemImpl ordrLicense) {
		this.ordrLicense = ordrLicense;
	}

	public LicenseWrapper(long rgtInst) {
		super(rgtInst);
		// TODO Auto-generated constructor stub
	}

	public LicenseWrapper(OrderLicense ordrLicense) {
		//this(123);
		this(0);
		this.ordrLicense = ordrLicense;

		permissionStatus = ordrLicense.getPermissionStatus();
		displayPermissionStatus = ordrLicense.getDisplayPermissionStatus();
		displayPermissionStatusCd = "";
		createDate = ordrLicense.getCreateDate();
		purchaseId = ordrLicense.getOrderId();
		orderId = ordrLicense.getOrderId();
		invoiceId = ordrLicense.getInvoiceId();
		billingStatus = ordrLicense.getBillingStatus();
		creditAuth = ordrLicense.getCreditAuth();
		reDistFlag = "";
		pinningDate = ordrLicense.getPinningDate();
		lastFourCreditCard = ordrLicense.getLastFourCreditCard();
		isCancelable = ordrLicense.isCancelable();
		withdrawnCode = ordrLicense.getWithdrawnCode();
		externalCommentOverride = ordrLicense.getExternalCommentOverride();
		creditCardType = ordrLicense.getCreditAuth();
		odtStatusCd = "";
		odtStatusCdMsg = "";
		evtInst = 0;
		invoiceDate = null;
		creUser = null;

		StandardWork wd = new StandardWork();
		wd.setMainAuthor(ordrLicense.getAuthor());
		wd.setEdition(ordrLicense.getEdition());
		wd.setStandardNumber(ordrLicense.getStandardNumber());
		wd.setPublisher(ordrLicense.getPublisher());
		wd.setMainTitle(ordrLicense.getPublicationTitle());
		wd.setVolume(ordrLicense.getVolume());
		wd.setWrkInst(0);
		work = wd;
	}

	@Override
	public boolean isDistributed() {
		
		return ordrLicense.isDistributed();

	}

	/**
	 * @return the event Inst of the license's distribution
	 */
	// TODO:Map this field
	@Override
	public long getEvtInst() {
		return Long.valueOf(123);
	}

	/**
	 * @return createDate the date the license was created
	 */
	@Override
	public Date getCreateDate() {
		return ordrLicense.getCreateDate();
	}

	/**
	 * @return creditAuth the credit card auth code
	 */
	@Override
	public String getCreditAuth() {
		
		return ordrLicense.getCreditAuth();
	}

	/**
	 * @return invoiceId the invoice number for this license
	 */
	@Override
	public String getInvoiceId() {
		return ordrLicense.getInvoiceId();
	}

	/**
	 * @return isCancelable true if this license may be canceled
	 */
	@Override
	public boolean isCancelable() {
		return ordrLicense.isCancelable();
	}

	/**
	 * @return orderId the order header this license is associated with (used
	 *         for Academic only)
	 */
	@Override
	public long getOrderId() {
		return ordrLicense.getOrderId();
	}

	/**
	 * @return purchaseId the purchase number for this license (AKA confirmation
	 *         number)
	 */
	@Override
	public long getPurchaseId() {
		return ordrLicense.getPurchaseId();
	}

	/**
	 * @return permissionStatus whether the license is granted, denied, pending,
	 *         etc
	 */
	@Override
	public String getPermissionStatus() {
		return ordrLicense.getPermissionStatus();
	}

	@Override
	public long getLastFourCreditCard() {
		return ordrLicense.getLastFourCreditCard();
	}

	@Override
	public String getBillingStatus() {
		return ordrLicense.getBillingStatus();
	}

	@Override
	public Date getPinningDate() {
		return ordrLicense.getPinningDate();
	}

	@Override
	public String getDisplayPermissionStatus() {
		return ordrLicense.getDisplayPermissionStatus();
	}

	@Override
	public String getWithdrawnCode() {
		return ordrLicense.getWithdrawnCode();
	}

	@Override
	public long getID() {
		return ordrLicense.getID();
	}

	@Override
	public String getExternalCommentOverride() {
		return ordrLicense.getExternalCommentOverride();
	}

	// TODO:MAP this field
	@Override
	public String getReDistFlag() {
		return "";
	}

	public long getRefOdtInst() {
		return ordrLicense.getLicenseDetailReferenceID();
	}

	@Override
	public String getCreditCardType() {
		return ordrLicense.getCreditAuth();
	}

	// TODO:Map these fields
	@Override
	public String getOdtStatusCd() {
		return "";
	}

	@Override
	public String getOdtStatusCdMsg() {
		return "";
	}

	@Override
	public String getDisplayPermissionStatusCd() {
		return "";
	}

	@Override
	public Date getInvoiceDate() {
		return ordrLicense.getInvoiceDate();
	}

	@Override
	public String getCreUser() {
		return "";
	}

	/**
	 * Returns the usage associated with this request.
	 * 
	 * @return the usage associated with this request.
	 */
	//public UsageData getUsageData() {
	//	return usageData;
	//}

	/**
	 * Returns the text description of this request's specific type of use, if
	 * available, <code>null</code> otherwise.
	 * <p>
	 * 
	 * @return <code>String</code>
	 */
	@Override
	public String getTypeOfUseDescription() {
		String _retVal = null;

		if (null != this.usageData) {
			long _tpuInst = this.usageData.getTpuInst();
			_retVal = ProductUsageConstants.getTypeUseDescription(_tpuInst);
		}

		return _retVal;
	}

	/**
	 * Returns the work parameters associated with this request.
	 * 
	 * @return the work parameters associated with this request.
	 */
	@Override
	public Work getWork() {

		return work;
	}

	/**
	 * Returns the price associated with this request.
	 * 
	 * @return the price associated with this request.
	 */
	@Override
	public Money getPrice() {
		return price;
	}

	/**
	 * Returns the customer reference associated with this request.
	 * 
	 * @return the customer reference associated with this request.
	 */
	@Override
	public String getCustomerRef() {
		return customerRef;
	}

	/**
	 * Returns the right instance id associated with this request.
	 * 
	 * @return the right instance id associated with this request.
	 */

	@Override
	public long getRgtInst() {
		return rgtInst;
	}

	/**
	 * @return <code>true</code> if this PermissionRequest represents a special
	 *         order, namely if <i>special order limits</i> were exceeded, or if
	 *         the <i>right inst</i> has not been set or it is Contact RH or it
	 *         is a generic Special Order based on other permission .
	 */
	// public boolean isSpecialOrder()
	// {
	// return (this.specialOrderLimitsExceeded || this.isContactRHPermission ||
	// this.isManualSpecialOrder || this.isSpecialOrderResearch());
	// }
	/**
	 * @return <code>true</code> if this <code>PermissionRequest</code>
	 *         represents an adjustment to an existing order detail item,
	 *         <code>false</code> otherwise.
	 * 
	 */
	@Override
	public boolean isAdjustmentRequest() {
		return (ordrLicense.getOrderId() > 0);
	}

	@Override
	public boolean getSpecialOrderLimitsExceeded() {
		return false;
	}

	/**
	 * Overrides <code>Object.toString()</code>
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append("PermissionRequest: ").append(" id=")
				.append(this.getID()).append(", price=")
				.append(this.getPrice()).append(", customerRef=").append(
						this.getCustomerRef()).append(", rgtInst=").append(
						this.getRgtInst()).append(
						", specialOrderLimitsExceeded=").append(
						this.getSpecialOrderLimitsExceeded()).append("\n + ")
				.append(this.getWork()).append("\n + ").append(
						this.getUsageData());

		return buffer.toString();
	}

	/**
	 * @return Returns the isContactRHPermission.
	 */
	@Override
	public boolean isContactRHPermission() {
		return ordrLicense.isContactRightsholder();
	}

	/**
	 * @return Returns the isManualSpecialOrder.
	 */
	@Override
	public boolean isManualSpecialOrder() {
		return ordrLicense.isManualSpecialOrder();
	}

	/**
	 * @return Returns the isSpecialOrderResearch.
	 */
	@Override
	public boolean isSpecialOrderResearch() {
		return ordrLicense.isSpecialOrder();
	}

	/**
	 * @return Returns the right.
	 */
	@Override
	public Right getRight() {
		return ordrLicense.getRight();
	}

	/**
	 * @return the type of adjustment, fee or parameter.
	 * @see #{@link #setAdjustmentType(String)}
	 */
	@Override
	public String getAdjustmentType() {
		return "";
	}

	/**
	 * @param type
	 *            The code that defines the adjustment type of this
	 *            <code>PermissionRequest</code>. Can either be
	 *            <code>ADJUSTMENT_TYPE_FEE</code> or
	 *            <code>ADJUSTMENT_TYPE_PARAMETER</code>, meaning a fee override
	 *            or parameter (quantity) adjustment to the original
	 *            <code>Licence</code>.
	 */
	// public void setAdjustmentType(String type) throws OrderMgmtException
	// {
	// if( null != type && !type.equals(ADJUSTMENT_TYPE_FEE) &&
	// !type.equals(ADJUSTMENT_TYPE_PARAMETER )){
	// throw new
	// OrderMgmtException(OrderMgmtMessageCodes.INVALID_ADJUSTMENT_TYPE);
	// }else{
	// _adjustmentType = type;
	// }
	// }
	/**
	 * Return the verisign CC transaction id.
	 * 
	 * <p>
	 * Applies to licenses, and permission requests created from licenses for
	 * adjustment purposes.
	 * 
	 * @return The verisign transaction ID.
	 */
	@Override
	public String getTransactionId() {
		return ordrLicense.getTransactionId();
	}

	/**
	 * @return the licensee fee amount
	 */
	@Override
	public double getLicenseeFee() {
		return ordrLicense.getLicenseeFee();
	}

	/**
	 * @return The party id of the licensee, if one exists.
	 */
	@Override
	public long getLicenseePartyID() {
		return ordrLicense.getLicenseePartyId();
	}

	/**
	 * @return the orderDetailReferenceID, the order detail ID from the original
	 *         license, or <code>PermissionRequest</code> that was purchased.
	 */
	@Override
	public long getOrderDetailReferenceID() {
		return ordrLicense.getOrderId();
	}

	/**
	 * @return the reason code for the adjustment
	 */
	@Override
	public long getReasonCd() {
		return ordrLicense.getReasonCd();
	}

	/**
	 * @return the Reason description for adjustment
	 */
	@Override
	public String getReasonDesc() {
		return ordrLicense.getReasonDesc();
	}

	/**
	 * @return the rightsholder fee amount
	 */
	@Override
	public double getRightsholderFee() {
		return ordrLicense.getRightsholderFee();
	}

	/**
	 * @return the royaltyPayable amount
	 */
	@Override
	public double getRoyaltyPayable() {
		return ordrLicense.getRoyalty();
	}

	/**
	 * @return the discount amount
	 */
	@Override
	public double getDiscount() {
		return ordrLicense.getDiscount();
	}

	/**
	 * @return <code>true</code> if this PermissionRequest represents a special
	 *         order, namely if <i>special order limits</i> were exceeded, or if
	 *         the <i>right inst</i> has not been set or it is Contact RH or it
	 *         is a generic Special Order based on other permission .
	 */
	@Override
	public boolean isSpecialOrder() {
		return ordrLicense.isSpecialOrder();
	}
	
	
	
	public String checkNull(String param){
		if(param != null){
			return param;
		}
		else return "";
	}
	
	/*public UsageData getUsageData()
	{
		UsageData result = null;

		if (ordrLicense.getPrdInst() == 1L)
		{
			UsageDataPhotocopy udp = new UsageDataPhotocopy();
			udp.setChapterArticle(ordrLicense.getChapterArticle());
			udp.setNumCopies(new Long(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_COPIES.name()).getCachedParmValue())));
			udp.setNumPages(new Long(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_PAGES.name()).getCachedParmValue())));
			result = udp;
		}
		
		else if (ordrLicense.getPrdInst() == 36L)
		{

			if (ordrLicense.getTpuInst() == 133L)
			{
				
				UsageDataEmail ude = new UsageDataEmail();

				ude.setAuthor(ordrLicense.getAuthor());
				ude.setChapterArticle(ordrLicense.getChapterArticle());
				ude.setCountry(ordrLicense.getCountry());

				if (ordrLicense.getDateOfUse() != null)
				{
					ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_PAGES);
					DateFormat f = new SimpleDateFormat("MM/dd/yyyy");
					ude.setDateOfUse(ordrLicense.getDateOfUse());
					//try
					//{
					//	if (null != f)
					//		ude.setDateOfUse(ordrLicense.getDateOfUse());
					//} catch (ParseException e)
					//{
					//}
				}
				Long NumRecip = new Long(ordrLicense.getNumberOfRecipients());
				ude.setNumRecipients(NumRecip);

				result = ude;
			} else if (ordrLicense.getTpuInst() == 134L || ordrLicense.getTpuInst() == 203L
					|| ordrLicense.getTpuInst() == 204L)
			{
				UsageDataNet udn = new UsageDataNet();
				udn.setAuthor(ordrLicense.getAuthor());
				udn.setChapterArticle(ordrLicense.getChapterArticle());

				if (ordrLicense.getDateOfUse() != null)
				{
					DateFormat f = new SimpleDateFormat("MM/dd/yyyy");
					udn.setDateOfUse(ordrLicense.getDateOfUse());
					//try
					//{
					//	if (null != f)
					//		udn.setDateOfUse(ordrLicense.getDateOfUse());
					//} catch (ParseException e)
					//{
					//}
				}

			if (ordrLicense.getDuration() != 0)
				{
					udn.setDuration(udn.getDurationIndex(String.valueOf(ordrLicense.getDuration())));
				}

				udn.setWebAddress(ordrLicense.getWebAddress());
			result = udn;
			}

	} else if (ordrLicense.getPrdInst() == 2L || ordrLicense.getPrdInst() == 12L)
		{
			UsageDataAcademic uda = new UsageDataAcademic();
			uda.setAuthor(ordrLicense.getAuthor());
			uda.setChapterArticle(ordrLicense.getChapterArticle());
			uda.setPageRanges(ordrLicense.getItem().getItemParms().get(ItemParmEnum.PAGE_RANGE.name()).getCachedParmValue());

			uda.setDateOfIssue(ordrLicense.getDateOfIssue());

			uda.setEdition(ordrLicense.getEdition());
			
			int numStud = ordrLicense.getNumberOfStudents();

			if (ordrLicense.getPrdInst() == 2L)
			{
				
				// this comes from copies in the db for APS
				if(ordrLicense.getItem().getItemParms().containsKey(ItemParmEnum.NUM_COPIES.name()))
				uda.setNumStudents(new Long(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_COPIES.name()).getCachedParmValue())));
				
			} else if (ordrLicense.getPrdInst() == 12L)
			{
				uda.setNumStudents(new Long(numStud));
				// this comes from students in the db for ECC
				//if(ordrLicense.getItem().getItemParms().containsKey(ItemParmEnum.NUM_STUDENTS.name()))
				//uda.setNumStudents(new Long(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_STUDENTS.name()).getCachedParmValue())));
			}
			
			uda.setNumPages(new Long(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_PAGES.name()).getCachedParmValue())));
			uda.setVolume(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.CUSTOM_VOLUME.name()).getCachedParmValue()));

			result = uda;
		} else if (ordrLicense.getPrdInst() == 44L)
		{
			UsageDataRepublication udr = new UsageDataRepublication();

			udr.setPageRanges(ordrLicense.getPageRange());

			// dynamic detail fee based information on RLS licenses
			if (null != ordrLicense.getItem().getItemParms().get(ItemParmEnum.CIRCULATION_DISTRIBUTION.name()).getCachedParmValue())
			{
				Long circ = new Long(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.CIRCULATION_DISTRIBUTION.name()).getCachedParmValue()));
				udr.setCirculation(circ.longValue());
			}

			udr.setFullArticle(checkNull(ordrLicense.getItem().getItemParms().get(ItemParmEnum.FULL_ARTICLE.name()).getCachedParmValue()));

			Long numCartoons = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_CARTOONS.name()).getCachedParmValue());
			udr.setNumCartoons(numCartoons.longValue());

			Long numCharts = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_CHARTS.name()).getCachedParmValue());
			udr.setNumCharts(numCharts.longValue());

			Long numExcerpts = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_EXCERPTS.name()).getCachedParmValue());
			udr.setNumExcerpts(numExcerpts.longValue());

			Long numFigures = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_FIGURES.name()).getCachedParmValue());
			udr.setNumFigures(numFigures.longValue());

			Long numGraphs = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_GRAPHS.name()).getCachedParmValue());
			udr.setNumGraphs(numGraphs.longValue());

     		Long numIllustrations = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_ILLUSTRATIONS.name()).getCachedParmValue());
			udr.setNumIllustrations(numIllustrations.longValue());

			Long numLogos = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_LOGOS.name()).getCachedParmValue());
			udr.setNumLogos(numLogos.longValue());

			Long numPhotos = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_PHOTOS.name()).getCachedParmValue());
			udr.setNumPhotos(numPhotos.longValue());

			Long numQuotes = new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NUM_QUOTES.name()).getCachedParmValue());
			udr.setNumQuotes(numQuotes.longValue());

			udr.setOrigAuthor(ordrLicense.getItem().getItemParms().get(ItemParmEnum.CUSTOM_AUTHOR.name()).getCachedParmValue());

			udr.setRlsPages(new Long(ordrLicense.getItem().getItemParms().get(ItemParmEnum.RLS_PAGES.name()).getCachedParmValue()));

			// dynamic detail information we store on every RLS license
			udr.setChapterArticle(ordrLicense.getChapterArticle());
			udr.setAuthor(ordrLicense.getAuthor());
            //TODO:Find appropriate match
			//udr.setPoNumDtl(ordrLicense.getPoNum());

			udr.setVolume(ordrLicense.getVolume());
			//TODO:Find appropriate match
			//udr.setSection(ordrLicense.getSection());
			udr.setTranslated(ordrLicense.getTranslated());
			udr.setLanguage(ordrLicense.getLanguage());
			//TODO:Find appropriate match
			//udr.setNonTextPortion(ordrLicense.getNonTextPortion());
			//udr.setFirstPage(ordrLicense.getFirstPage());
			//udr.setLastPage(ordrLicense.getLastPage());

			// dynamic order header information we store on every RLS license
			// object
			udr.setForProfit(ordrLicense.getItem().getItemParms().get(ItemParmEnum.FOR_PROFIT.name()).getCachedParmValue());
			udr.setRepubInVolEd(ordrLicense.getItem().getItemParms().get(ItemParmEnum.REPUBLISH_IN_VOL_ED.name()).getCachedParmValue());
			udr.setRepubTitle(ordrLicense.getRepublicationTitle());
			udr.setHdrRepubPub(ordrLicense.getItem().getItemParms().get(ItemParmEnum.NEW_PUBLICATION_TITLE.name()).getCachedParmValue());
			

			if (ordrLicense.getItem().getItemParms().get(ItemParmEnum.REPUBLICATION_DATE.name()).getCachedParmValue() != null)
			{
			udr.setRepubDate(DateUtils2.parseDate(ordrLicense.getItem().getItemParms().get(ItemParmEnum.REPUBLICATION_DATE.name()).getCachedParmValue(),
						"MM/dd/yyyy"));
			}

			// static order header information we store on every RLS license
			// object
			udr.setLcnHdrRefNum(ordrLicense.getLicenseeRefNum());
			//TODO:Find appropriate match
			udr.setLcnAcctRefNum(ordrLicense.getLicenseeRefNum());

			result = udr;
		}
		if (result != null)
		{
			result.setProduct(ordrLicense.getPrdInst());
		}
		return result;
	}*/

}
