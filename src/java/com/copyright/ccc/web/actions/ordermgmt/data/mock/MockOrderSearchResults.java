package com.copyright.ccc.web.actions.ordermgmt.data.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MockOrderSearchResults implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public List<MockOrderWrapper> orderList = new ArrayList<MockOrderWrapper>();
	public List<MockCourseInfoWrapper> courseInfoList = new ArrayList<MockCourseInfoWrapper>();
	public List<MockOrderDetailWrapper> detailList = new ArrayList<MockOrderDetailWrapper>();

	
	public static MockOrderSearchResults getFakeOrderResult() {
		MockOrderSearchResults res = new MockOrderSearchResults();
		res.orderList = new ArrayList<MockOrderWrapper>();
		
		MockOrderWrapper op = new MockOrderWrapper();
		op.setConfNumber(122333);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		//op.setCourseName("MyCourse");
		res.orderList.add(op);
		return res;
	}
	
	public static MockOrderSearchResults getFakeOrderResults3() {
		MockOrderSearchResults res = new MockOrderSearchResults();
		res.orderList = new ArrayList<MockOrderWrapper>();
		
		MockOrderWrapper op = new MockOrderWrapper();
		op.setConfNumber(122333);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		//op.setCourseName("MyCourse1");
		res.orderList.add(op);	
		
		op = new MockOrderWrapper();
		op.setConfNumber(456456);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		//op.setCourseName("MyCourse2");
		res.orderList.add(op);	
		
		op = new MockOrderWrapper();
		op.setConfNumber(777889);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		//op.setCourseName("MyCourse3");
		res.orderList.add(op);	
		return res;
	}
	
	public static MockOrderSearchResults getFakeOrderCourseResults3() {
		MockOrderSearchResults res = new MockOrderSearchResults();
		res.orderList = new ArrayList<MockOrderWrapper>();
		//res.courseInfoList = new ArrayList<MockCourseInfoWrapper>();
		//res.detailList = new ArrayList<MockOrderDetailWrapper>();
		

		MockOrderWrapper op = new MockOrderWrapper();
		op.setConfNumber(122333);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		op.setOrderID(111);
		//op.setMyCourses(new ArrayList<MockCourseInfoWrapper>());
		
		//add course with 3 details
		op.getMyCourses().add(createFakeCourseInfo(111, "Course 1", "Ben", 1));		
		op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(111, "det1-111-1", 1, 100, "Essie"));
		op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(111, "det1-111-2", 1, 200, "Bob"));
		op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(111, "det1-111-3", 1, 300, "Essie"));
		//add course with no details
		op.getMyCourses().add(createFakeCourseInfo(111, "Course 2", "Bob", 2));
		//add 2 details without courses
		op.getMyOrderDetails().add(createFakeDetail(111, "det-111-5", -1, 500, "Bob"));
		op.getMyOrderDetails().add(createFakeDetail(111, "det-111-6", -1, 600, "Essie"));
		res.orderList.add(op);			
		
		
		op = new MockOrderWrapper();
		op.setConfNumber(456456);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		op.setOrderID(222);
		//op.setMyCourses(new ArrayList<MockCourseInfoWrapper>());
		//do not add any courses or details
		res.orderList.add(op);		
		
		op = new MockOrderWrapper();
		op.setConfNumber(777889);
		//op.setOrderEnteredBy("Essie");
		//op.setStartOfTerm(Calendar.getInstance().getTime());
		op.setOrderID(333);
		//op.setMyCourses(new ArrayList<MockCourseInfoWrapper>());
		//add course with 1 detail
		op.getMyCourses().add(createFakeCourseInfo(333, "Course 3", "Bill", 3));
		op.getMyCourses().get(0).getMyDetails().add(createFakeDetail(333, "det3-333-4", 3, 400, "Essie"));
		res.orderList.add(op);		
		
		return res;
	}

	public static MockOrderSearchResults getFakeCourseDetailResults3() {
		MockOrderSearchResults res = new MockOrderSearchResults();
		res.courseInfoList = new ArrayList<MockCourseInfoWrapper>();
		res.detailList = new ArrayList<MockOrderDetailWrapper>();
		//res.courseInfoList = new ArrayList<MockCourseInfoWrapper>();
		//res.detailList = new ArrayList<MockOrderDetailWrapper>();
		

		//add course with 3 details
		res.courseInfoList.add(createFakeCourseInfo(-1, "Course 1", "Ben", 1));		
		res.courseInfoList.get(0).getMyDetails().add(createFakeDetail(-1, "det1-111-1", 1, 100, "Essie"));
		res.courseInfoList.get(0).getMyDetails().add(createFakeDetail(-1, "det1-111-2", 1, 200, "Bob"));
		res.courseInfoList.get(0).getMyDetails().add(createFakeDetail(-1, "det1-111-3", 1, 300, "Essie"));
		//add course with no details
		res.courseInfoList.add(createFakeCourseInfo(111, "Course 2", "Bob", 2));
		//add 2 details without courses
		res.detailList.add(createFakeDetail(-1, "det-111-5", -1, 500, "Bob"));
		res.detailList.add(createFakeDetail(-1, "det-111-6", -1, 600, "Essie"));

		//add course with 1 detail
		res.courseInfoList.add(createFakeCourseInfo(-1, "Course 3", "Bill", 3));
		res.courseInfoList.get(1).getMyDetails().add(createFakeDetail(-1, "det3-333-4", 3, 400, "Essie"));	
		
		return res;
	}
	
	public static MockOrderSearchResults getFakeDetailResults3() {
		MockOrderSearchResults res = new MockOrderSearchResults();
		res.detailList = new ArrayList<MockOrderDetailWrapper>();	
		//res.orderList = new ArrayList<MockOrderWrapper>();

		//add 6 details	
		res.detailList.add(createFakeDetail(-1, "det1-111-1", -1, 100, "Essie"));
		res.detailList.add(createFakeDetail(-1, "det1-111-2", -1, 200, "Bob"));
		res.detailList.add(createFakeDetail(-1, "det1-111-3", -1, 300, "Essie"));
		res.detailList.add(createFakeDetail(-1, "det-111-5", -1, 500, "Bob"));
		res.detailList.add(createFakeDetail(-1, "det-111-6", -1, 600, "Essie"));
		res.detailList.add(createFakeDetail(-1, "det3-333-4", -1, 400, "Essie"));

		return res;
	}
	
	public static MockCourseInfoWrapper createFakeCourseInfo(int orderId, String courseNumber, String instructor, int courseId) {
		
		MockCourseInfoWrapper ci = new MockCourseInfoWrapper();
		ci.setOrderId(orderId);
		ci.setCourseId(courseId);
		ci.setCourseNumber(courseNumber);
		ci.setInstructorName(instructor);
		ci.setUniversityName("MIT");
		ci.setCourseName("18.05");
		ci.setStartOfTerm(Calendar.getInstance().getTime());
		//ci.setMyDetails(new ArrayList<MockOrderDetailWrapper>());
		
		return ci;
	}
	
	public static MockOrderDetailWrapper createFakeDetail(int orderId, String detNum, int courseId, int detailId, String person) {
		
		MockOrderDetailWrapper ci = new MockOrderDetailWrapper();
		ci.setOrderId(orderId);
		ci.setCourseId(courseId);
		ci.setDetailNumber(detNum);
		ci.setDetailId(detailId);
		ci.setEnteredBy(person);
		ci.setAdjID("Adj987");
		
		return ci;
		//res.detailList.add(ci);
	}

	//TODO: instead of this, take in searchCriteria, and return true only if results that are reauested are present
	public boolean hasResults() {
		if (orderList != null && orderList.size() >0)
			return true;
		if (courseInfoList != null && courseInfoList.size() >0)
			return true;
		if (detailList != null && detailList.size() >0)
			return true;
		
		return false;
	}

	public static MockOrderSearchResults getFakeOrderDetailResults3() {
		MockOrderSearchResults res = new MockOrderSearchResults();
		res.orderList = new ArrayList<MockOrderWrapper>();
		
		MockOrderWrapper op = new MockOrderWrapper();
		op.setConfNumber(122333);
		op.setOrderID(111);
		
		//add 4 details without courses
		op.getMyOrderDetails().add(createFakeDetail(111, "det1-111-1", -1, 100, "Essie"));
		op.getMyOrderDetails().add(createFakeDetail(111, "det1-111-2", -1, 200, "Bob"));
		op.getMyOrderDetails().add(createFakeDetail(111, "det1-111-3", -1, 300, "Essie"));
		op.getMyOrderDetails().add(createFakeDetail(111, "det-111-5", -1, 500, "Bob"));
		op.getMyOrderDetails().add(createFakeDetail(111, "det-111-6", -1, 600, "Essie"));
		res.orderList.add(op);			
		
		op = new MockOrderWrapper();
		op.setConfNumber(456456);
		op.setOrderID(222);
		//do not add any courses or details
		res.orderList.add(op);		
		
		op = new MockOrderWrapper();
		op.setConfNumber(777889);
		op.setOrderID(333);
		//add course with 1 detail
		op.getMyOrderDetails().add(createFakeDetail(333, "det3-333-4", -1, 400, "Essie"));
		res.orderList.add(op);		
		
		return res;
	}

	public List<MockOrderWrapper> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<MockOrderWrapper> orderList) {
		this.orderList = orderList;
	}

	public List<MockCourseInfoWrapper> getCourseInfoList() {
		return courseInfoList;
	}

	public void setCourseInfoList(List<MockCourseInfoWrapper> courseInfoList) {
		this.courseInfoList = courseInfoList;
	}

	public List<MockOrderDetailWrapper> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<MockOrderDetailWrapper> detailList) {
		this.detailList = detailList;
	}


/*	
	public static List<MockCourseInfoWrapper> GetCourseInfoForOrder(int orderId, MockOrderSearchResults osr) {
		List<MockCourseInfoWrapper> res = new ArrayList<MockCourseInfoWrapper>();
		for (MockCourseInfoWrapper ci : osr.courseInfoList)
		{
			if (ci.getOrderId() == orderId)
				res.add(ci);
		}
		
		if (res.size()!= 0)
			return res;
		else
			return null;
	}
	
	public static List<MockOrderDetailWrapper> GetDetailForCourse(int courseId, MockOrderSearchResults osr) {
		List<MockOrderDetailWrapper> res = new ArrayList<MockOrderDetailWrapper>();
		for (MockOrderDetailWrapper od : osr.detailList)
		{
			if (od.getCourseId() == courseId)
				res.add(od);
		}
		
		if (res.size()!= 0)
			return res;
		else
			return null;
	}
	
*/
}