package com.copyright.ccc.web;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.Resources;
import org.apache.struts.validator.ValidatorForm;


public abstract class CCValidatorForm extends ValidatorForm
{
	private static final long serialVersionUID = 1L;
	
    private String _operation = ""; //dispatch operation
    
    protected final Logger _logger = Logger.getLogger( this.getClass() );

    @Override
    public ActionErrors validate(ActionMapping mapping, 
                                 HttpServletRequest request)
    {
        String mappingParameter = mapping.getParameter();
        
        if( StringUtils.isNotEmpty(mappingParameter) )
            _operation = request.getParameter(mappingParameter);
        
        ActionErrors errors = new ActionErrors();
        
        ServletContext application = getServlet().getServletContext();
        
        String formName = getValidationKey(mapping, request);
        
        Validator validator = Resources.initValidator(formName,
                          this,
                          application, request,
                          errors, page);
        
        
        //get non-dispatch-action-specific validation errors
        try 
        {
            validatorResults = validator.validate();
        } 
        catch (ValidatorException e) {
            _logger.error( "Form Validator Exception - " + e.getMessage(), e);
        }
        
        //get dispatch-action-specific validation errors.
        
        //the concatenator "_" is "_" because it is a valid character in a javascript method.  If
        //an invalid javascript method character is used, then client side validation using the 
        //built in Struts validator will not work.
        validator.setFormName(formName + "_" + _operation);

        try 
        {
            validatorResults = validator.validate();
        } 
        catch (ValidatorException e) {
            _logger.error( "Dispatch Action Form Validator Exception - " + e.getMessage(), e);
        }
        
        return errors;
    }
    
    public String getOperation()
    {
        return _operation;
    }
   
}
