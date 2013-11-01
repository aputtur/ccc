package com.copyright.ccc.web.forms;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class MicroSiteForm extends ActionForm{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
    private final Map<String,String> values = new HashMap<String,String>();
    public MicroSiteForm() {
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label=label;
    }

   public String getValue(String attname) {
        return values.get(attname);        
    }
    public void setValue(String attname, String value) {
        values.put(attname,value);
    }
   
    public Map<String,String> getValues(){
        return new HashMap<String,String>(values);
    }
}

