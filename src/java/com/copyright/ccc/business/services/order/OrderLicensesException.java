package com.copyright.ccc.business.services.order;

import com.copyright.ccc.CCException;

public class OrderLicensesException extends CCException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrderLicensesException() {
        super();
    }
    
    public OrderLicensesException(Throwable cause) {
        super(cause);
    }

    public OrderLicensesException(String message, String messageCode, Throwable cause) {
        super(message,cause);
        this.setMessageCode(messageCode);
    }

    public OrderLicensesException(String message, Throwable cause) {
        super(message,cause);
    }

    public OrderLicensesException(String message, String messageCode) {
        super(message);
        this.setMessageCode(messageCode);
    }

}
