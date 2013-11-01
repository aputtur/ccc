package com.copyright.ccc.web.forms;

import org.apache.struts.action.ActionForm;

public class PopupTextActionForm extends ActionForm 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
    
    public PopupTextActionForm() { }

    public void setText(String termText) {
       this.text = termText;
    }

    public String getText() {
       return text;
    }
}
