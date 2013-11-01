<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<s:textfield theme="simple" name="editDetails[%{detailIndex}].toBeSaved" value="%{toBeSaved}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].toBeReturned" value="%{toBeReturned}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].updatePricingOnly" value="false"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.ID" value="%{item.ID}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.bundleId" value="%{item.bundleId}"/>

<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.rhRefNum" value="%{item.rhRefNum}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.chapterArticle" value="%{item.chapterArticle}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.customAuthor" value="%{item.customAuthor}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.customerReference" value="%{item.customerReference}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.pageRange" value="%{item.pageRange}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.editor" value="%{item.editor}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.dateOfIssue" value="%{item.dateOfIssue}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfPages" value="%{item.numberOfPages}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.customEdition" value="%{item.customEdition}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.publicationYearOfUse" value="%{item.publicationYearOfUse}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.publicationDateOfUse" value="%{item.publicationYearOfUse}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfCopies" value="%{item.numberOfCopies}"/>
 <!-- <s:textfield theme="simple" name="editDetails[%{detailIndex}].item.translator" value="%{missing.translator}"/> -->
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.customVolume" value="%{item.customVolume}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.publicationTitle" value="%{item.publicationTitle}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.wrWorkInst" value="%{item.wrWorkInst}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.itemSubDescription" value="%{item.itemSubDescription}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.workInst" value="%{item.workInst}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.idnoLabel" value="%{item.idnoLabel}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.standardNumber" value="%{item.standardNumber}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.publisher" value="%{item.publisher}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.duration" value="%{item.duration}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.dateOfUse" value="%{item.dateOfUse}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfRecipients" value="%{item.numberOfRecipients}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.webAddress" value="%{item.webAddress}"/>

<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.business" value="%{item.business}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.circulationDistribution" value="%{item.circulationDistribution}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfCartoons" value="%{item.numberOfCartoons}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfCharts" value="%{item.numberOfCharts}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfExcerpts" value="%{item.numberOfExcerpts}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfFigures" value="%{item.numberOfFigures}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfGraphs" value="%{item.numberOfGraphs}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfIllustrations" value="%{item.numberOfIllustrations}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfLogos" value="%{item.numberOfLogos}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfPhotos" value="%{item.numberOfPhotos}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfQuotes" value="%{item.numberOfQuotes}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.republicationDate" value="%{item.republicationDate}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.republishFullArticle" value="%{item.republishFullArticle}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.republishingOrganization" value="%{item.republishingOrganization}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.republishInVolEd" value="%{item.republishInVolEd}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.republishNonTextPortion" value="%{item.republishNonTextPortion}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.republishPoNumDtl" value="%{item.republishPoNumDtl}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.repubWork" value="%{item.repubWork}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.submitterAuthor" value="%{item.submitterAuthor}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.translated" value="%{item.translated}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.translationLanguage" value="%{item.translationLanguage}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.typeOfContent" value="%{item.typeOfContent}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.rgtInst" value="%{item.rgtInst}"/>
<s:textfield theme="simple" name="editDetails[%{detailIndex}].item.numberOfStudents" value="%{item.numberOfStudents}"/>

<br/>