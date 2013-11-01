package com.copyright.ccc.web.transaction;

import com.copyright.ccc.business.data.TransactionItem;

public class ContactRHDirectlyUnavailableException extends CCUnavailableException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContactRHDirectlyUnavailableException( TransactionItem transactionItem )
    {
        super( transactionItem );
    }
}
