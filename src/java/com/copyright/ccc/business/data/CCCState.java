package com.copyright.ccc.business.data;

import java.io.Serializable;

import com.copyright.base.svc.data.AbstractPersistentDTO;

public class CCCState extends AbstractPersistentDTO implements Serializable

{
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	
	public String getCode()
	{
		return code;
	}
	public void setCode(String stateCode)
	{
		this.code = stateCode;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String stateName)
	{
		this.name = stateName;
	}

}
