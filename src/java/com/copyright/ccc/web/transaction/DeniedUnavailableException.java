package com.copyright.ccc.web.transaction;

import com.copyright.ccc.business.data.TransactionItem;

public class DeniedUnavailableException extends CCUnavailableException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeniedUnavailableException( TransactionItem transactionItem )
    {
        super( transactionItem );
    }
}
