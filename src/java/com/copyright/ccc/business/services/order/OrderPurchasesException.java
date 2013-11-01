package com.copyright.ccc.business.services.order;

import com.copyright.ccc.CCException;

public class OrderPurchasesException extends CCException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderPurchasesException() {
        super();
    }
    
    public OrderPurchasesException(Throwable cause) {
        super(cause);
    }
    
    public OrderPurchasesException(String message, String messageCode, Throwable cause) {
        super(message,cause);
        this.setMessageCode(messageCode);
    }

    public OrderPurchasesException(String message, Throwable cause) {
        super(message,cause);
    }

    public OrderPurchasesException(String message, String messageCode) {
        super(message);
        this.setMessageCode(messageCode);
    }
}
