package com.copyright.ccc.web.actions.ordermgmt.data.mock;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

//import com.copyright.opi.data.DOBase;


public class MockCourseInfoWrapper implements Serializable
{

	    private static final long serialVersionUID = 1L;
	    
	    private Date    _startOfTerm     = null;
	    private Long    _numStudents     = null;
	    private String  _universityName  = null;
	    private String  _instructorName  = null;
	    private String  _courseName      = null;
	    private String  _courseNumber    = null;
	    private String _comments;
	    private String _docRefNumber;
	    private String _acctRefNumber;
	    private String _billingRefNumber;
	    private String _enteredBy;
	    
	    private boolean isEmpty         = true; 
	    
	    private int _orderId;
	    private int _courseId;
	    List<MockOrderDetailWrapper> _myDetails;

	    
	    public List<MockOrderDetailWrapper> getMyDetails()
	    {
	        return _myDetails;
	    }
	    
	    public void setMyDetails(List<MockOrderDetailWrapper> list) {
	    	_myDetails = list;
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
	    
	    /**
	     * Default constructor
	     */
	    public MockCourseInfoWrapper(){ 
	    	
	    	_myDetails = new ArrayList<MockOrderDetailWrapper>();
	    }

	    public MockCourseInfoWrapper(MockCourseInfoWrapper cpy){ 
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
	    	
	    	_myDetails = new ArrayList<MockOrderDetailWrapper>();
	    	
	    	if ( cpy.getMyDetails() != null ) {
	    		List<MockOrderDetailWrapper> ls = new ArrayList<MockOrderDetailWrapper>(cpy.getMyDetails().size());
	    		for ( Iterator<MockOrderDetailWrapper> iter=cpy.getMyDetails().iterator(); iter.hasNext(); ) {
	    			ls.add(new MockOrderDetailWrapper( iter.next() ));
	    		}
	    		this.setMyDetails(ls);
	    	} 
	    		
	    }
	    public MockCourseInfoWrapper(MockCourseInfoWrapper cpy, boolean showDetails){ 
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
	    	
	    	_myDetails = new ArrayList<MockOrderDetailWrapper>();
	    	
	    	if ( showDetails ) {
	    		if ( cpy.getMyDetails() != null ) {
	    			List<MockOrderDetailWrapper> ls = new ArrayList<MockOrderDetailWrapper>(cpy.getMyDetails().size());
	    			for ( Iterator<MockOrderDetailWrapper> iter=cpy.getMyDetails().iterator(); iter.hasNext(); ) {
	    				ls.add(new MockOrderDetailWrapper( iter.next() ));
	    			}
	    			this.setMyDetails(ls);
	    		} 
	    		
	    	}
	    }
	    
	    private boolean hasValue(String str)
	    {
	        if(null != str && !str.equals("")) 
	            return true;
	        
	        return false;        
	    }
	    /**
	     * @return the course name.
	     */
	    public String getCourseName()
	    {
	        return _courseName;
	    }

	    /**
	     * @param courseName The course name to set.
	     */
	    public void setCourseName(String courseName)
	    {
	        if(hasValue(courseName))
	            this.isEmpty = false;
	        this._courseName = courseName;
	    }

	    /**
	     * @return the course number.
	     */
	    public String getCourseNumber()
	    {
	        return _courseNumber;
	    }

	    /**
	     * @param courseNumber The course number to set.
	     */
	    public void setCourseNumber(String courseNumber)
	    {
	        if(hasValue(courseNumber))
	            this.isEmpty = false;
	        this._courseNumber = courseNumber;
	    }

	    /**
	     * @return The instructor name.
	     */
	    public String getInstructorName()
	    {
	        return _instructorName;
	    }

	    /**
	     * @param instructorName 
	     *            The name of the instructor name for this <code>CoursePack</code>.
	     */
	    public void setInstructorName(String instructorName)
	    {
	        if(hasValue(instructorName))
	            this.isEmpty = false;
	        this._instructorName = instructorName;
	    }

	    /**
	     * @return The number of students in this <code>CoursePack</code>.
	     */
	    public Long getNumStudents()
	    {
	        return _numStudents;
	    }

	    /**
	     * @param numStudents 
	     *            The number of students in this <code>CoursePack</code>.
	     */
	    public void setNumStudents(Long numStudents)
	    {
	        if(null != numStudents)
	            this.isEmpty = false;
	        this._numStudents = numStudents;
	    }

	    /**
	     * @return Returns the start of term.
	     */
	    public Date getStartOfTerm()
	    {
	        return _startOfTerm;
	    }

	    /**
	     * @param startOfTerm The start of term for this <code>CoursePack</code>.
	     */
	    public void setStartOfTerm(Date startOfTerm)
	    {
	        if(null != startOfTerm)
	            this.isEmpty = false;
	        this._startOfTerm = startOfTerm;
	    }

	    /**
	     * @return Returns the university name.
	     */
	    public String getUniversityName()
	    {
	        return _universityName;
	    }

	    /**
	     * @param universityName The university name.
	     */
	    public void setUniversityName(String universityName)
	    {
	        if(hasValue(universityName))
	            this.isEmpty = false;
	        this._universityName = universityName;
	    }

	    
	    /**
	     * 
	     * @return <code>true</code> if any of field on this <code>CoursePack</code> 
	     *         has been set; <br><code>false</code> otherwise.
	     *         
	     */
	    public boolean isEmpty()
	    {
	        return this.isEmpty;
	    }

		public String get_comments() {
			return _comments;
		}

		public void set_comments(String _comments) {
			this._comments = _comments;
		}

		public String get_docRefNumber() {
			return _docRefNumber;
		}

		public void set_docRefNumber(String refNumber) {
			_docRefNumber = refNumber;
		}

		public String get_acctRefNumber() {
			return _acctRefNumber;
		}

		public void set_acctRefNumber(String refNumber) {
			_acctRefNumber = refNumber;
		}

		public String get_billingRefNumber() {
			return _billingRefNumber;
		}

		public void set_billingRefNumber(String refNumber) {
			_billingRefNumber = refNumber;
		}

		public String get_enteredBy() {
			return _enteredBy;
		}

		public void set_enteredBy(String by) {
			_enteredBy = by;
		}
	    
	    
}


