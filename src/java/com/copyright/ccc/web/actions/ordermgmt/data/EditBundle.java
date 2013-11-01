package com.copyright.ccc.web.actions.ordermgmt.data;

import java.io.Serializable;
import java.util.Date;

import com.copyright.ccc.business.data.OrderBundle;

public class EditBundle implements OrderBundle, Serializable{

	private static final long serialVersionUID = 1L;

	private boolean numStudentsSame;
	private Date    startOfTerm;
	private long    bundleId;
	private long    numberOfStudents;
	private long	estimatedCopies;
	private String  comments;
	private String  courseName;
	private String  courseNumber;
	private String  instructor;
	private String  organization;
	@Deprecated
	private String  school;
	private String  startOfTermStr;
	private String  yourReference;
	private String  accountingReference;
	private String  orderEnteredBy;
	
	public EditBundle() {
		super();
	}
	
	public EditBundle( OrderBundle bundle) {
		super();
		if ( bundle != null ) {
			this.setBundleId( bundle.getBundleId() );
			this.setComments( bundle.getComments() );
			this.setCourseName( bundle.getCourseName() );
			this.setCourseNumber( bundle.getCourseNumber() );
			this.setOrderEnteredBy( bundle.getOrderEnteredBy());
			this.setInstructor( bundle.getInstructor() );
			this.setNumberOfStudents( bundle.getNumberOfStudents() );
			this.setNumStudentsSame( bundle.isNumStudentsSame() );
			this.setOrganization( bundle.getOrganization() );
			this.setStartOfTerm( bundle.getStartOfTerm() );
			this.setStartOfTermStr( bundle.getStartOfTermStr() );
			this.setYourReference( bundle.getYourReference() );
			this.setAccountingReference( bundle.getYourReference() );
		}
	}
	
	public boolean isNumStudentsSame() {
		return numStudentsSame;
	}
	public void setNumStudentsSame(boolean numStudentsSame) {
		this.numStudentsSame = numStudentsSame;
	}
	public Date getStartOfTerm() {
		return startOfTerm;
	}
	public void setStartOfTerm(Date startOfTerm) {
		this.startOfTerm = startOfTerm;
	}
	public long getBundleId() {
		return bundleId;
	}
	public void setBundleId(long bundleId) {
		this.bundleId = bundleId;
	}
	public long getNumberOfStudents() {
		return numberOfStudents;
	}
	public void setNumberOfStudents(long numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
	}
	public Long getNumberOfStudentsLong() {
		return numberOfStudents;
	}
	public void setNumberOfStudentsLong(Long numberOfStudents) {
		this.numberOfStudents = numberOfStudents==null? 0 : numberOfStudents;
	}
	public Long getEstimatedCopies() {
		return estimatedCopies;
	}
	public void setEstimatedCopies(Long estimatedCopies) {
		this.estimatedCopies = estimatedCopies==null? 0 : estimatedCopies;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
		this.school = organization;
	}
	@Deprecated
	public String getSchool() {
		return school;
	}
	@Deprecated
	public void setSchool(String school) {
		this.school = school;
	}
	public String getStartOfTermStr() {
		return startOfTermStr;
	}
	public void setStartOfTermStr(String startOfTermStr) {
		this.startOfTermStr = startOfTermStr;
	}
	public String getYourReference() {
		return yourReference;
	}
	public String getAccountingReference() {
		return accountingReference;
	}
	public void setYourReference(String yourReference) {
		this.yourReference = yourReference;
//		this.accountingReference = yourReference;
	}
	
	public void setAccountingReference(String accountingReference) {
		this.accountingReference = accountingReference;
//		this.yourReference = accountingReference;
	}

	public String getOrderEnteredBy() {
		return orderEnteredBy;
	}

	public void setOrderEnteredBy(String orderEnteredBy) {
		this.orderEnteredBy = orderEnteredBy;
	}
	
	public String getBundleTitles() {
		return null;
	}
	
	public long getDetailCount() {
		return 0;
	}

}
