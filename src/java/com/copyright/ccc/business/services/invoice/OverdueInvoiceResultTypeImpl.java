package com.copyright.ccc.business.services.invoice;

import com.copyright.ccc.business.data.IOverdueInvoice;
import com.copyright.ccc.business.data.IOverdueInvoiceResultType;
import com.copyright.opi.data.PersistentDO;

public class OverdueInvoiceResultTypeImpl extends PersistentDO implements IOverdueInvoiceResultType
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */

	private IOverdueInvoice[] _overdueInvoices = null;

	
	public void setOverdueInvoices(IOverdueInvoice[] overdueInvoices)
	{
		this._overdueInvoices = overdueInvoices;
	}

	public IOverdueInvoice[] getOverdueInvoices()
	{
		return _overdueInvoices;
	}

	// protected default constructor for safety
	protected OverdueInvoiceResultTypeImpl()
	{
		// setUserType( DEFAULT_USER_TYPE );
	}

}
