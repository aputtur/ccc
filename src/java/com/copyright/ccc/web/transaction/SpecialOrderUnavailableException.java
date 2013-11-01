package com.copyright.ccc.web.transaction;

import com.copyright.ccc.business.data.TransactionItem;

public class SpecialOrderUnavailableException extends CCUnavailableException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpecialOrderUnavailableException( TransactionItem transactionItem )
    {
        super( transactionItem );
    }
}
