package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class EditItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ID;
	private String rhRefNum;
	private String chapterArticle;
	private String customAuthor;
	private String customerReference;
	private String pageRange;
	private String editor;
	private String dateOfIssue;
	private String numberOfPages;
	private String customEdition;
	private String publicationYearOfUse;
	private String publicationDateOfUse;
	private String numberOfCopies;
	private String translator;
	private String customVolume;
	
	// bibliographic work fields
	private String publicationTitle;
	private String wrWorkInst;
	private String itemSubDescription;
	private String workInst;
	private String idnoLabel;
	private String standardNumber;
	private String publisher;
	
	private String duration;
	private String dateOfUse;
	private String numberOfRecipients;
	private String webAddress;
	
	private String rgtInst;
	private String business;
	private String circulationDistribution;
	private String numberOfCartoons;
	private String numberOfCharts;
	private String numberOfExcerpts;
	private String numberOfFigures;
	private String numberOfGraphs;
	private String numberOfIllustrations;
	private String numberOfLogos;
	private String numberOfPhotos;
	private String numberOfQuotes;
	private String numberOfStudents;
	private String republicationDate;
	private String republishFullArticle;
	private String republishingOrganization;
	private String republishInVolEd;
	private String republishNonTextPortion;
	private String republishPoNumDtl;
	private String repubWork;
	private String submitterAuthor;
	private String translated;
	private String translationLanguage;
	private String typeOfContent;
	private String typeOfContentDescription;
	
	
	//belongs to a bundle # > 0 = true
	private String bundleId;
	
	private boolean updatePricingOnly = false;


	public boolean isABundledDetail() {
		if ( StringUtils.isNotEmpty( getBundleId() ) ) {
			if ( Long.valueOf(getBundleId()).intValue() > 0  ) {
				return true;
			}
		}
		return false;
	}
	
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		ID = id;
	}
	public String getStandardNumber() {
		return standardNumber;
	}
	public void setStandardNumber(String standardNumber) {
		this.standardNumber = standardNumber;
	}
	public String getRhRefNum() {
		return rhRefNum;
	}
	public void setRhRefNum(String rhRefNum) {
		this.rhRefNum = rhRefNum;
	}
	public String getChapterArticle() {
		return chapterArticle;
	}
	public void setChapterArticle(String chapterArticle) {
		this.chapterArticle = chapterArticle;
	}
	public String getCustomAuthor() {
		return customAuthor;
	}
	public void setCustomAuthor(String customAuthor) {
		this.customAuthor = customAuthor;
	}
	public String getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}
	public String getPageRange() {
		return pageRange;
	}
	public void setPageRange(String pageRange) {
		this.pageRange = pageRange;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getDateOfIssue() {
		return dateOfIssue;
	}
	public void setDateOfIssue(String dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}
	public String getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(String numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public String getCustomEdition() {
		return customEdition;
	}
	public void setCustomEdition(String customEdition) {
		this.customEdition = customEdition;
	}
	public String getPublicationYearOfUse() {
		return publicationYearOfUse;
	}
	public void setPublicationYearOfUse(String publicationYearOfUse) {
		this.publicationYearOfUse = publicationYearOfUse;
	}
	public String getNumberOfCopies() {
		return numberOfCopies;
	}
	public void setNumberOfCopies(String numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}
	public String getTranslator() {
		return translator;
	}
	public void setTranslator(String translator) {
		this.translator = translator;
	}
	public String getCustomVolume() {
		return customVolume;
	}
	public void setCustomVolume(String customVolume) {
		this.customVolume = customVolume;
	}
	public String getPublicationTitle() {
		return publicationTitle;
	}
	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}
	public String getWrWorkInst() {
		return wrWorkInst;
	}
	public void setWrWorkInst(String wrWorkInst) {
		this.wrWorkInst = wrWorkInst;
	}
	public String getItemSubDescription() {
		return itemSubDescription;
	}
	public void setItemSubDescription(String itemSubDescription) {
		this.itemSubDescription = itemSubDescription;
	}
	public String getWorkInst() {
		return workInst;
	}
	public void setWorkInst(String workInst) {
		this.workInst = workInst;
	}
	public String getIdnoLabel() {
		return idnoLabel;
	}
	public void setIdnoLabel(String idnoLabel) {
		this.idnoLabel = idnoLabel;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDateOfUse() {
		return dateOfUse;
	}

	public void setDateOfUse(String dateOfUse) {
		this.dateOfUse = dateOfUse;
	}

	public String getNumberOfRecipients() {
		return numberOfRecipients;
	}

	public void setNumberOfRecipients(String numberOfRecipients) {
		this.numberOfRecipients = numberOfRecipients;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getCirculationDistribution() {
		return circulationDistribution;
	}

	public void setCirculationDistribution(String circulationDistribution) {
		this.circulationDistribution = circulationDistribution;
	}

	public String getNumberOfCartoons() {
		return numberOfCartoons;
	}

	public void setNumberOfCartoons(String numberOfCartoons) {
		this.numberOfCartoons = numberOfCartoons;
	}

	public String getNumberOfCharts() {
		return numberOfCharts;
	}

	public void setNumberOfCharts(String numberOfCharts) {
		this.numberOfCharts = numberOfCharts;
	}

	public String getNumberOfExcerpts() {
		return numberOfExcerpts;
	}

	public void setNumberOfExcerpts(String numberOfExcerpts) {
		this.numberOfExcerpts = numberOfExcerpts;
	}

	public String getNumberOfFigures() {
		return numberOfFigures;
	}

	public void setNumberOfFigures(String numberOfFigures) {
		this.numberOfFigures = numberOfFigures;
	}

	public String getNumberOfGraphs() {
		return numberOfGraphs;
	}

	public void setNumberOfGraphs(String numberOfGraphs) {
		this.numberOfGraphs = numberOfGraphs;
	}

	public String getNumberOfIllustrations() {
		return numberOfIllustrations;
	}

	public void setNumberOfIllustrations(String numberOfIllustrations) {
		this.numberOfIllustrations = numberOfIllustrations;
	}

	public String getNumberOfLogos() {
		return numberOfLogos;
	}

	public void setNumberOfLogos(String numberOfLogos) {
		this.numberOfLogos = numberOfLogos;
	}

	public String getNumberOfPhotos() {
		return numberOfPhotos;
	}

	public void setNumberOfPhotos(String numberOfPhotos) {
		this.numberOfPhotos = numberOfPhotos;
	}

	public String getNumberOfQuotes() {
		return numberOfQuotes;
	}

	public void setNumberOfQuotes(String numberOfQuotes) {
		this.numberOfQuotes = numberOfQuotes;
	}

	public String getRepublicationDate() {
		return republicationDate;
	}

	public void setRepublicationDate(String republicationDate) {
		this.republicationDate = republicationDate;
	}

	public String getRepublishFullArticle() {
		return republishFullArticle;
	}

	public void setRepublishFullArticle(String republishFullArticle) {
		this.republishFullArticle = republishFullArticle;
	}

	public String getRepublishingOrganization() {
		return republishingOrganization;
	}

	public void setRepublishingOrganization(String republishingOrganization) {
		this.republishingOrganization = republishingOrganization;
	}

	public String getRepublishInVolEd() {
		return republishInVolEd;
	}

	public void setRepublishInVolEd(String republishInVolEd) {
		this.republishInVolEd = republishInVolEd;
	}

	public String getRepublishNonTextPortion() {
		return republishNonTextPortion;
	}

	public void setRepublishNonTextPortion(String republishNonTextPortion) {
		this.republishNonTextPortion = republishNonTextPortion;
	}

	public String getRepublishPoNumDtl() {
		return republishPoNumDtl;
	}

	public void setRepublishPoNumDtl(String republishPoNumDtl) {
		this.republishPoNumDtl = republishPoNumDtl;
	}

	public String getRepubWork() {
		return repubWork;
	}

	public void setRepubWork(String repubWork) {
		this.repubWork = repubWork;
	}

	public String getSubmitterAuthor() {
		return submitterAuthor;
	}

	public void setSubmitterAuthor(String submitterAuthor) {
		this.submitterAuthor = submitterAuthor;
	}

	public String getTranslated() {
		return translated;
	}

	public void setTranslated(String translated) {
		this.translated = translated;
	}

	public String getTranslationLanguage() {
		return translationLanguage;
	}

	public void setTranslationLanguage(String translationLanguage) {
		this.translationLanguage = translationLanguage;
	}

	public String getTypeOfContentDescription() {
		return typeOfContentDescription;
	}

	public void setTypeOfContentDescription(String typeOfContentDescription) {
		this.typeOfContentDescription = typeOfContentDescription;
	}

	public String getNumberOfStudents() {
		return numberOfStudents;
	}

	public void setNumberOfStudents(String numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}

	public void setUpdatePricingOnly(boolean updatePricingOnly) {
		this.updatePricingOnly = updatePricingOnly;
	}

	public boolean isUpdatePricingOnly() {
		return updatePricingOnly;
	}
	
	public String getRgtInst() {
		return rgtInst;
	}

	public void setRgtInst(String rgtInst) {
		this.rgtInst = rgtInst;
	}

	/**
	 * @return the typeOfContent
	 */
	public String getTypeOfContent()
	{
		return typeOfContent;
	}

	/**
	 * @param typeOfContent the typeOfContent to set
	 */
	public void setTypeOfContent(String typeOfContent)
	{
		this.typeOfContent = typeOfContent;
	}

	/**
	 * @return the publicationDateOfUse
	 */
	public String getPublicationDateOfUse()
	{
		return publicationDateOfUse;
	}

	/**
	 * @param publicationDateOfUse the publicationDateOfUse to set
	 */
	public void setPublicationDateOfUse(String publicationDateOfUse)
	{
		this.publicationDateOfUse = publicationDateOfUse;
	}
}
