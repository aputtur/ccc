package com.copyright.ccc.business.services.invoice;

import com.copyright.ccc.business.data.IAutoDunningParam;
import com.copyright.ccc.business.data.IAutoDunningParamResultType;
import com.copyright.opi.data.PersistentDO;

public class AutoDunningParamResultTypeImpl extends PersistentDO implements IAutoDunningParamResultType
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */

	private IAutoDunningParam[] params = null;

	/**
	 * @param params
	 */
	public void setAutoDunningParams(IAutoDunningParam[] params)
	{
		this.params = params;
	}

	public IAutoDunningParam[] getAutoDunningParams()
	{
		return params;
	}

	// protected default constructor for safety
	protected AutoDunningParamResultTypeImpl()
	{

	}

}
