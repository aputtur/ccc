package com.copyright.ccc.business.data;

public interface OrderPartnerXref
{
    void setOrderConfirm( long orderConfirm );
    long getOrderConfirm();

    void setPartnerId( String partnerId );
    String getPartnerId();
}