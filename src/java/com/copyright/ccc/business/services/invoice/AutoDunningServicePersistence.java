package com.copyright.ccc.business.services.invoice;

import java.sql.SQLData;

import com.copyright.ccc.business.data.IAutoDunningParam;
import com.copyright.ccc.business.data.access.CC2DataAccessConstants;
import com.copyright.ccc.business.data.access.CC2OracleProcedureInvokerFactory;
import com.copyright.opi.SingleDTOProcedureInvoker;
import com.copyright.workbench.util.ArrayUtils2;

public class AutoDunningServicePersistence
{
	private static AutoDunningServicePersistence persistence;

	private AutoDunningServicePersistence()
	{
	}

	public synchronized static AutoDunningServicePersistence getInstance()
	{
		if (persistence != null)
			return persistence;
		else
		{
			persistence = new AutoDunningServicePersistence();
			return persistence;
		}
	}

	public IAutoDunningParam[] getAutoDunningParam()
	{
		IAutoDunningParam[] params = null;

		SingleDTOProcedureInvoker invoker = CC2OracleProcedureInvokerFactory.getInstance().singleDTOInvoker();

		invoker.configure(CC2DataAccessConstants.CCUser.GET_AUTODUNNING_PARAMS, AutoDunningParamResultTypeDTO
				.getRefInstance());

		invoker.setNoDataFoundAcceptable(true);

		invoker.invoke();

		AutoDunningParamResultTypeDTO paramsData = (AutoDunningParamResultTypeDTO) invoker.getDTO();

		SQLData[] data = null;

		if (paramsData != null)
		{
			data = (SQLData[]) paramsData.getAutoDunningParams();
		}
		else
		{
			return null;
		}

		params = (IAutoDunningParam[]) ArrayUtils2.convertArray(data, IAutoDunningParam.class);

		return params;
	}

}
