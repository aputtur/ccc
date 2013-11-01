/**  This class represents the PaymentService object which will be used to get the correct payment vendor.
  *  @version 4.0
  *  @author Dave Marcou
  *  @since Rightslink Permissions 1.0
 */

package com.copyright.ccc.business.services.payment;

public class PaymentServiceMgr
{
    private PaymentServiceMgr()
    {}

    /**
    * static method to return an instance of the correct PaymentVendor class
    * @return PaymentVendor
    * @pre  $none
    * @post $none
	*/

    public static PaymentVendor getPaymentVendor(PaymentRequest paymentRequest) 
    {
        return new CyberSourcePaymentVendor(paymentRequest);
    }
}

