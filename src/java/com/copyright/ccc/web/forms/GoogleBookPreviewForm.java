package com.copyright.ccc.web.forms;

import org.apache.struts.action.ActionForm;

public class GoogleBookPreviewForm extends ActionForm 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isbn;
    
    public GoogleBookPreviewForm() { }

    public void setIsbn(String isb) {
       this.isbn = isb;
    }

    public String getIsbn() {
       return isbn;
    }
}
