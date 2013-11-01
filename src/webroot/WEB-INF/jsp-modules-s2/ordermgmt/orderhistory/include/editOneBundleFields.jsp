<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ page import="java.util.*, java.lang.*"%>

<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].toBeSaved" value="%{toBeSaved}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].toBeReturned" value="%{toBeReturned}"/>

<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].bundleId" value="%{bundle.bundleId}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].comments" value="%{bundle.comments}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].courseName" value="%{bundle.courseName}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].courseNumber" value="%{bundle.courseNumber}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].instructor" value="%{bundle.instructor}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].numberOfStudents" value="%{bundle.numberOfStudents}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].organization" value="%{bundle.organization}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].startOfTerm" value="%{bundle.startOfTerm}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].startOfTermStr" value="%{bundle.startOfTermStr}"/>
<s:hidden theme="simple" name="editBundles[%{#bEditStatus.index}].yourReference" value="%{bundle.yourReference}"/>

<br/>