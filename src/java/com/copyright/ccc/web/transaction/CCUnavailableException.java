package com.copyright.ccc.web.transaction;

import com.copyright.ccc.CCException;
import com.copyright.ccc.business.data.TransactionItem;

public class CCUnavailableException extends CCException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TransactionItem _transactionItem = null;
    
    public CCUnavailableException( TransactionItem transactionItem )
    {
        _transactionItem = transactionItem;
    }
    
    public TransactionItem getTransactionItem()
    {
        return _transactionItem;
    }

}
