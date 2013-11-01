package com.copyright.ccc.web.actions.ordermgmt.data.mock;

//import com.copyright.ccc.business.services.user.User;
//import com.copyright.data.order.CoursePack;
//import com.copyright.data.order.Purchase;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

public class MockOrderWrapper {

    private int _confNumber;
    private Date  _orderDate     = null;
    private String _acctRef		 = null;
    private String _poNum		 = null;
    private String billingRef;
    private String orderStatus;  //maybe should be int/enum
    private String acctNumber;
    private String licenseeName;
    private String internetLogin;
   
    private String dbSource; // COI vs. TF.....maybe should be int/enum?

	int _orderId;
    List<MockCourseInfoWrapper> _myCourses;
    List<MockOrderDetailWrapper> _myOrderDetails;

    public MockOrderWrapper() {  
    	
    	_myCourses = new ArrayList<MockCourseInfoWrapper>();
    	_myOrderDetails = new ArrayList<MockOrderDetailWrapper>();
    }
    public MockOrderWrapper(MockOrderWrapper cpy) {  
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
    	
    	_myCourses = new ArrayList<MockCourseInfoWrapper>();
    	_myOrderDetails = new ArrayList<MockOrderDetailWrapper>();
    	
		if ( cpy.getMyCourses() != null ) {
			List<MockCourseInfoWrapper> ls = new ArrayList<MockCourseInfoWrapper>(cpy.getMyCourses().size());
			for ( Iterator<MockCourseInfoWrapper> iter=cpy.getMyCourses().iterator(); iter.hasNext(); ) {
				ls.add(new MockCourseInfoWrapper( iter.next() ));
			}
			this.setMyCourses(ls);
		} 
		if ( cpy.getMyOrderDetails() != null ) {
			List<MockOrderDetailWrapper> ls = new ArrayList<MockOrderDetailWrapper>(cpy.getMyOrderDetails().size());
			for ( Iterator<MockOrderDetailWrapper> iter=cpy.getMyOrderDetails().iterator(); iter.hasNext(); ) {
				ls.add(new MockOrderDetailWrapper( iter.next() ));
			}
			this.setMyOrderDetails(ls);
		} 
    }
    public MockOrderWrapper(MockOrderWrapper cpy, boolean showCourses, boolean showDetails) {  

    	try {
			PropertyUtils.copyProperties(this, cpy);
		} catch (IllegalAccessException e1) {
			//throw new ServiceRuntimeException("IllegalAccessException", e1);
		} catch (InvocationTargetException e2) {
			//throw new ServiceRuntimeException("InvocationTargetException", e2);
		} catch (NoSuchMethodException e3) {
			//throw new ServiceRuntimeException("NoSuchMethodException", e3);
		}
		
    	_myCourses = new ArrayList<MockCourseInfoWrapper>();
    	_myOrderDetails = new ArrayList<MockOrderDetailWrapper>();
    	
    	if ( showCourses ) {
    		if ( cpy.getMyCourses() != null ) {
    			List<MockCourseInfoWrapper> ls = new ArrayList<MockCourseInfoWrapper>(cpy.getMyCourses().size());
    			for ( Iterator<MockCourseInfoWrapper> iter=cpy.getMyCourses().iterator(); iter.hasNext(); ) {
    				ls.add(new MockCourseInfoWrapper( iter.next(), showDetails ));
    			}
    			this.setMyCourses(ls);
    		} 
    		
    	}
    	if ( showDetails ) {
    		if ( cpy.getMyOrderDetails() != null ) {
    			List<MockOrderDetailWrapper> ls = new ArrayList<MockOrderDetailWrapper>(cpy.getMyOrderDetails().size());
    			for ( Iterator<MockOrderDetailWrapper> iter=cpy.getMyOrderDetails().iterator(); iter.hasNext(); ) {
    				ls.add(new MockOrderDetailWrapper( iter.next() ));
    			}
    			this.setMyOrderDetails(ls);
    		} 
    		
    	}
    }
    
    public List<MockOrderDetailWrapper> getMyOrderDetails()
    {
        return _myOrderDetails;
    }
    
    public void setMyOrderDetails(List<MockOrderDetailWrapper> list) {
    	_myOrderDetails = list;
    }
    
    public List<MockCourseInfoWrapper> getMyCourses()
    {
        return _myCourses;
    }
    
    public void setMyCourses(List<MockCourseInfoWrapper> list) {
    	_myCourses = list;
    }


    
    public int getOrderId()
    {
        return _orderId;
    }

    public void setOrderID(int oid)
    {
        this._orderId = oid;
    }
    
    public int getConfNumber() {
        return _confNumber;
    }

    public void setConfNumber(int cn) {
        _confNumber = cn;
    }
          

   // public long getDetailCount() {
   //     return this.getPurchase().getDetailCount();
   // }    


    public String getPoNum() {
        return _poNum;
    }    

    public void setPoNum (String po) {
    	_poNum = po;
    }
    

    
    public String getAcctRef() 
    { 
        return _acctRef;
    }
        
    public void setAcctRef(String ar) {
       _acctRef = ar;
    }
    
    /**
    * This method supports the UI order date display field.  It gets
    * the Purchase Date from this class' aggregate Purchase object
    * and formats it to a display string for the JSP
    * @return the purchase order date as a String
    */
     public String getOrderDateStr() {
       // Date purchDate = this.getPurchase().getPurchaseDate();
       // String orderDateStr = "";
       // if (purchDate != null) {
       //    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
        //   orderDateStr = format.format(purchDate);            
        //}
    //
        return "orderDate";
     }
     
    public Date getOrderDate() {
       return _orderDate;
    }
    
    public void setOrderDate (Date dt) {
    	_orderDate = dt;
    }
    
    
    public String getDbSource() {
		return dbSource;
	}

	public void setDbSource(String dbSource) {
		this.dbSource = dbSource;
	}

	public String getBillingRef() {
		return billingRef;
	}

	public void setBillingRef(String billingRef) {
		this.billingRef = billingRef;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getAcctNumber() {
		return acctNumber;
	}

	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}

	public String getLicenseeName() {
		return licenseeName;
	}

	public void setLicenseeName(String licenseeName) {
		this.licenseeName = licenseeName;
	}

	public String getInternetLogin() {
		return internetLogin;
	}

	public void setInternetLogin(String internetLogin) {
		this.internetLogin = internetLogin;
	}

	
}
