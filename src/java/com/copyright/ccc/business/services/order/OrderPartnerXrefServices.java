package com.copyright.ccc.business.services.order;

import org.apache.log4j.Logger;

import java.sql.SQLData;

import com.copyright.ccc.business.data.OrderPartnerXref;
import com.copyright.ccc.business.data.access.CC2DataAccessConstants;
import com.copyright.ccc.business.data.access.CC2OracleProcedureInvokerFactory;

import com.copyright.opi.InvocationExpectedEventRuntimeException;
import com.copyright.opi.InvocationRuntimeException;
import com.copyright.opi.InvocationUnexpectedErrorRuntimeException;
import com.copyright.opi.NoOutputProcedureInvoker;
import com.copyright.opi.SingleDTOProcedureInvoker;
import com.copyright.opi.TypedParameter;
import com.copyright.opi.data.DOBase;
import com.copyright.opi.SQLUtils;

public class OrderPartnerXrefServices
{
    //  A simple helper class to read and write order<-->partner cross
    //  references.  These are used by finance to tie UTM_SOURCE's back to
    //  orders.

    static Logger _logger = Logger.getLogger( OrderPartnerXrefServices.class );

    public OrderPartnerXrefServices() 
    {
        // Is there anybody out there?
    }

    public OrderPartnerXref createOrderPartnerXref()
    {
        OrderPartnerXref opx = new OrderPartnerXrefImpl();
        return opx;
    }

    //  Look up the order header by confirmation number, return the
    //  partner id.

    public OrderPartnerXref getOrderPartnerXrefByConfirm( long orderConfirm )
    {
        TypedParameter[] inputParameters = {
            new TypedParameter( orderConfirm )
        };

        SingleDTOProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();

        invoker.configure(
            CC2DataAccessConstants.OrderPartnerXref.READ_XREF_BY_CONFIRM_NUMBER, 
            OrderPartnerXrefDTO.getRefInstance(),
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( true );
        invoker.invoke();

        OrderPartnerXrefImpl opx = (OrderPartnerXrefImpl) invoker.getDTO();

        return opx;
    }

    //  Insert the partner ID into the CCC_ORDER table, referenced by the
    //  order confirmation number.

    public void setOrderPartnerXref( OrderPartnerXref orderPartnerXref )
    {
        OrderPartnerXrefImpl opx = (OrderPartnerXrefImpl) orderPartnerXref;

        opx = ( OrderPartnerXrefImpl ) SQLUtils.ensureUnderlyingDTO( opx, OrderPartnerXrefDTO.class );

        TypedParameter[] inputParameters = {
            new TypedParameter( opx )
        };

        NoOutputProcedureInvoker invoker =
            CC2OracleProcedureInvokerFactory.getInstance().noOutputInvoker();

        invoker.configure(
            CC2DataAccessConstants.OrderPartnerXref.WRITE_XREF,
            inputParameters
        );

        invoker.setNoDataFoundAcceptable( true );
        invoker.invoke();
    }
}