package com.copyright.ccc.web.transaction;

import com.copyright.ccc.business.data.TransactionItem;

public class ChangedToRegularFromSpecialUnavailableException extends CCUnavailableException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangedToRegularFromSpecialUnavailableException( TransactionItem transactionItem )
    {
        super( transactionItem );
    }
}
