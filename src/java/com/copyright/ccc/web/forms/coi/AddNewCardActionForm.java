package com.copyright.ccc.web.forms.coi;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.copyright.ccc.web.CCValidatorForm;

public class AddNewCardActionForm extends CCValidatorForm {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String txtFname = null;
	    private String txtLname = null;
	    
	    
	    

	   

	    public void setTxtFname(String fname )
	    {
	        this.txtFname = fname;
	    }

	    public String getTxtFname()
	    {
	        return txtFname;
	    }
	    
	    public void setTxtLname(String lname )
	    {
	        this.txtLname = lname;
	    }

	    public String getTxtLname()
	    {
	        return txtLname;
	    }
	    
	    /**
	     *
	     */
	    public AddNewCardActionForm() {
	        super();
	        // TODO Auto-generated constructor stub
	    }

	    /**
	     * This is the action called from the Struts framework.
	     * @param mapping The ActionMapping used to select this instance.
	     * @param request The HTTP Request we are processing.
	     */
	    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, org.apache.struts.action.ActionForm form) {
	        ActionErrors errors = new ActionErrors();
	        AddNewCardActionForm  f = (AddNewCardActionForm)form; // get the form bean
	        String strFname = f.getTxtFname();
	        String strLname = f.getTxtLname();

	        if (strFname==null || strFname.equals(""))
	        {
	            errors.add("First Name", new ActionMessage("error.strFname"));
	        }
	        if (strLname == null || strLname.equals(""))
	        {
	            errors.add("Last Name", new ActionMessage("error.strLname"));
	        }
	        
	        return errors;
	    }
    
}
