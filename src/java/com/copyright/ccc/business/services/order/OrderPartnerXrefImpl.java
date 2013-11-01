package com.copyright.ccc.business.services.order;

import com.copyright.ccc.business.data.OrderPartnerXref;
import com.copyright.opi.data.StandardDO;

public class OrderPartnerXrefImpl extends StandardDO implements OrderPartnerXref
{
    //  Simple class for cross referencing an order confirmation number
    //  with a partner id (Google Analytics UTM_SOURCE).

    private static final long serialVersionUID = 1L;
    private long _orderConfirm;
    private String _partnerId;

    protected OrderPartnerXrefImpl() 
    {
        // Ignore the man behind the curtain.
    }

    public void setOrderConfirm( long orderConfirm )
    {
        _orderConfirm = orderConfirm;
    }

    public long getOrderConfirm()
    {
        return _orderConfirm;
    }

    public void setPartnerId( String partnerId )
    {
        _partnerId = partnerId;
    }

    public String getPartnerId()
    {
        return _partnerId;
    }
}