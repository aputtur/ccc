package com.copyright.ccc.web.actions.ordermgmt.data.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.copyright.ccc.web.actions.ordermgmt.data.OrderSearchCriteriaWrapper;
import com.copyright.workbench.security.UserContextHelperBase;

public class MockOrderDataServices implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	public static MockOrderWrapper queryOrderByConfirmNumber( Long confirmNumber ) {
		
		for ( MockOrderWrapper ors : getOrdersToSearch() ) {
			
			if ( ors.getConfNumber() == confirmNumber.intValue()) {
				return ors;
			}
		}
						
		return new MockOrderWrapper();
	}
	
	public static int queryMyOrderCount() {
		int myCount = 0;
//		for ( MockOrderWrapper ors : getOrdersToSearch() ) {
//			if ( StringUtils.isNotEmpty(ors.getInternetLogin()) && StringUtils.isNotEmpty(ors.getOrderStatus()) ) {
//				if ( ors.getOrderStatus().equalsIgnoreCase("Not Completely Entered") ) {
//					if ( ors.getInternetLogin().equalsIgnoreCase(UserContextHelperBase.getCurrentAsInterface().getAuthenticatedUser().getUsername()) ) {
//						myCount++;
//					}
//				}
//			}
//		}
						
		return myCount;
	}
	
	
	public static MockOrderSearchResults queryOrders( OrderSearchCriteriaWrapper searchCriteria ) {
	   // boolean showCancelled = searchCriteria.getShowCancelled();
	   // boolean showCourseInfo = searchCriteria.getShowCourseInfo();
	    
	    MockOrderSearchResults oResults = new MockOrderSearchResults();
	    
		for ( MockOrderWrapper orwrapper : getOrdersToSearch() ) {
			boolean isMyOrder = false;
			if ( StringUtils.isNotEmpty(searchCriteria.getInternetLogin_adv() ) && StringUtils.isNotEmpty(searchCriteria.getOrderStatus_adv()) ) {
				if ( searchCriteria.getOrderStatus_adv().equalsIgnoreCase("Not Completely Entered") ) {
					isMyOrder = true;
				}
			}
			
			boolean includeOrder = true;
			
			if ( isMyOrder ) {
			   includeOrder = false;
			   if ( StringUtils.isNotEmpty(orwrapper.getInternetLogin()) && StringUtils.isNotEmpty(orwrapper.getOrderStatus()) ) {
				   if ( orwrapper.getInternetLogin().equalsIgnoreCase(searchCriteria.getInternetLogin_adv()) 
						   && orwrapper.getOrderStatus().equalsIgnoreCase("Not Completely Entered")) {
					   includeOrder = true;
				   }
			   }
			} 
			
//			if ( includeOrder ) {
//			
//				if ( showOrderHeaders && showCourseInfo && showOrderDetails) {
//					oResults.getOrderList().add(orwrapper);
//				} else if ( showOrderHeaders && showCourseInfo ) {
//					oResults.getOrderList().add( new MockOrderWrapper( orwrapper, showCourseInfo, showOrderDetails ) );
//				} else if ( showOrderHeaders && showOrderDetails ) {
//					MockOrderWrapper nowr = new MockOrderWrapper( orwrapper );
//	                for ( MockCourseInfoWrapper cwr : orwrapper.getMyCourses() ) {
//	                	for ( MockOrderDetailWrapper dw : cwr.getMyDetails() ) {
//	                		nowr.getMyOrderDetails().add(dw);
//	                	}
//	                }
//	                for ( MockOrderDetailWrapper odw : orwrapper.getMyOrderDetails() ) {
//	                	nowr.getMyOrderDetails().add(odw);
//	                }
//	                oResults.getOrderList().add(nowr);
//				} else if ( showOrderHeaders ) {
//					oResults.getOrderList().add( new MockOrderWrapper( orwrapper ) );
//				} else if ( showCourseInfo && showOrderDetails ) {
//	                for ( MockCourseInfoWrapper cwr : orwrapper.getMyCourses() ) {
//	                	oResults.getCourseInfoList().add( new MockCourseInfoWrapper( cwr, showOrderDetails ) );
//	                }
//				} else if ( showCourseInfo ) {
//	                for ( MockCourseInfoWrapper cwr : orwrapper.getMyCourses() ) {
//	                	oResults.getCourseInfoList().add( new MockCourseInfoWrapper(cwr));
//	                }				
//				} else if ( showOrderDetails ) {
//	                for ( MockCourseInfoWrapper cwr : orwrapper.getMyCourses() ) {
//	                	for ( MockOrderDetailWrapper dw : cwr.getMyDetails() ) {
//	                		oResults.getDetailList().add(dw);
//	                	}
//	                }
//	                for ( MockOrderDetailWrapper odw : orwrapper.getMyOrderDetails() ) {
//	                	oResults.getDetailList().add(odw);
//	                }
//				}
//			}
		}
		return oResults;
	}
	
	private static List<MockOrderWrapper> ordersToSearch = new ArrayList<MockOrderWrapper>();

    private static Date getNextDate( int addOne ) {
    	Calendar orDate = Calendar.getInstance();
    	orDate.add(Calendar.MONTH, addOne);
    	return orDate.getTime();
    }

    private static void makeMyOrder(MockOrderWrapper op ){
    	op.setOrderStatus("Not Completely Entered");
    	op.setInternetLogin(UserContextHelperBase.getCurrentAsInterface().getAuthenticatedUser().getUsername());
    }
    
    private static void setOrderHeader( MockOrderWrapper op, int orders, int bills, int accts) {
		op.setAcctNumber("Acct-"+(orders%2==0?accts:(accts++)+""));
		op.setAcctRef((orders%2==0?"COI":"TF")+(orders+bills+accts)+"");
		op.setBillingRef("Bill-"+(orders%2==0?bills:(bills++)+""));
		op.setDbSource((orders%2==0?"COI":"TF"));
		op.setLicenseeName("Licensee: "+(bills%2==0?"JQ Publishing":"RA Johns"));
		op.setOrderStatus("Invoiced");
		op.setPoNum("PO-"+(orders+bills+accts)+"");
    }
    
	private static List<MockOrderWrapper> getOrdersToSearch() {
		if ( ordersToSearch != null && !ordersToSearch.isEmpty() ) {
			return ordersToSearch;
		} else {
			
			int orders = 2317199;
			int courses = 31032200;
			int courseNums = 1000;
			int details = 54093300;
			int dates = 0;
			int bills = 1000;
			int accts = 5000;
			
			MockOrderWrapper op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			
			setOrderHeader( op, orders, bills, accts);

			makeMyOrder( op );
			//op.setConfNumber(2317200);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse1");

			//add course with 3 details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Ben"));		
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Bob"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Pam"));
			//add course with no details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bob"));
			op.getMyCourses().get(1).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			//add 2 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			
			
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse2");

			//add 4 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse3");
			//add course with 1 detail
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bill"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			ordersToSearch.add(op);	
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse1");

			//add course with 3 details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Ben"));		
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Bob"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Pam"));
			//add course with no details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bob"));
			op.getMyCourses().get(1).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			//add 2 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			
			
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			orders = 2318299 -1;
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse2");
			makeMyOrder( op );

			//add 4 details without courses
			
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse3");
			//add course with 1 detail
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bill"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			ordersToSearch.add(op);	
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse1");

			//add course with 3 details\
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Ben"));		
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Bob"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Pam"));
			//add course with no details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bob"));
			op.getMyCourses().get(1).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			//add 2 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			
			
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse2");

			//add 4 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse3");
			//add course with 1 detail
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bill"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			ordersToSearch.add(op);	
			op = new MockOrderWrapper();
			orders = 2317739 -1;
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse1");
			makeMyOrder( op );

			//add course with 3 details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Ben"));		
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Bob"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Pam"));
			//add course with no details
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bob"));
			op.getMyCourses().get(1).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			//add 2 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			
			
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse2");

			//add 4 details without courses
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Bob"));
			op.getMyOrderDetails().add(createFakeDetail(orders, -1, details++, "Essie"));
			ordersToSearch.add(op);	
			
			op = new MockOrderWrapper();
			op.setConfNumber(orders++);
			op.setOrderDate(getNextDate( dates++ ));
			setOrderHeader( op, orders, bills, accts);
			//op.setOrderEnteredBy("Essie");
			//op.setStartOfTerm(Calendar.getInstance().getTime());
			//op.setCourseName("MyCourse3");
			//add course with 1 detail
			courses++;
			op.getMyCourses().add(createFakeCourseInfo(orders, courses, "Course "+courseNums++, "Bill"));
			op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(orders, courses, details++, "Essie"));
			ordersToSearch.add(op);	
			
			return ordersToSearch;
		}
	}
	
	private static MockCourseInfoWrapper createFakeCourseInfo(int orderId, int courseId, String courseNumber, String instructor) {
		
		MockCourseInfoWrapper ci = new MockCourseInfoWrapper();
		ci.setOrderId(orderId);
		ci.setCourseId(courseId);
		ci.setCourseNumber(courseNumber);
		ci.setInstructorName(instructor);
		ci.setUniversityName("MIT");
		switch (courseId%3) {
		case 0:
			ci.setCourseNumber("18.05");
			ci.setCourseName("Political Science");
			break;
		case 1:
			ci.setCourseNumber("11.01");
			ci.setCourseName("English Literature");
			break;
		case 2:
			ci.setCourseNumber("22.11");
			ci.setCourseName("Advanvced Calculus");
			break;
		}
		ci.setStartOfTerm(Calendar.getInstance().getTime());
		//ci.setMyDetails(new ArrayList<MockOrderDetailWrapper>());
		
		return ci;
	}
	
	private static MockOrderDetailWrapper createFakeDetail(int orderId, int courseId, int detailId, String person) {
		
		MockOrderDetailWrapper ci = new MockOrderDetailWrapper();
		ci.setOrderId(orderId);
		ci.setConfNumber(orderId);
		ci.setCourseId(courseId);
		ci.setDetailNumber("det-"+orderId+"-"+detailId);
		ci.setDetailId(detailId);
		ci.setEnteredBy(person);
		ci.setAdjID("Adj987");
			
		return ci;
		//res.detailList.add(ci);
	}
	
}
