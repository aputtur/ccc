package com.copyright.ccc.business.services.cart;


/**
 *  Exception class indicating that course details must be provided before attempting to perform
 *  a transaction (e.g. add item to shopping cart).
 */
public class CourseNotDefinedException extends PurchasablePermissionException
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private CourseNotDefinedException(){}
  
  CourseNotDefinedException( PurchasablePermission purchasablePermission )
  {
    super( purchasablePermission );
  }
}