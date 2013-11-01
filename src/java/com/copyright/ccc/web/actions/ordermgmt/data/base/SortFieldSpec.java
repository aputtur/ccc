package com.copyright.ccc.web.actions.ordermgmt.data.base;

import java.io.Serializable;

public abstract class SortFieldSpec<E, K> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	E field;
	K direction;
	
	public SortFieldSpec(E field, K direction) {
		this.field = field;
		this.direction = direction;
	}
	
	/**
	 * @return the field
	 */
	public E getField() {
		return field;
	}
	/**
	 * @return the direction
	 */
	public K getDirection() {
		return direction;
	}

}
