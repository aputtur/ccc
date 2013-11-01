package com.copyright.ccc.web.tags;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.Var;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.JavascriptValidatorTag;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.Resources;
import org.apache.struts.validator.ValidatorPlugIn;

/**
 * Custom tag that generates JavaScript for client side validation based
 * on the validation rules loaded by the <code>ValidatorPlugIn</code>
 * defined in the struts-config.xml file.  It also supports the custom
 * validateForCondition validator.
 */
public class CCJavascriptValidatorTag extends JavascriptValidatorTag
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * A Comparator to use when sorting ValidatorAction objects.
     */
    private static final Comparator<ValidatorAction> actionComparator = 
    new Comparator<ValidatorAction>() {
        public int compare(ValidatorAction va1, ValidatorAction va2) {

            if ((va1.getDepends() == null || va1.getDepends().length() == 0)
                && (va2.getDepends() == null || va2.getDepends().length() == 0)) {
                return 0;

            } else if (
                (va1.getDepends() != null && va1.getDepends().length() > 0)
                    && (va2.getDepends() == null || va2.getDepends().length() == 0)) {
                return 1;

            } else if (
                (va1.getDepends() == null || va1.getDepends().length() == 0)
                    && (va2.getDepends() != null && va2.getDepends().length() > 0)) {
                return -1;

            } else {
                return va1.getDependencyList().size() - va2.getDependencyList().size();
            }
        }
    };
    
    /**
    * Returns fully rendered JavaScript.
    * @since Struts 1.2
    */
    @Override
    protected String renderJavascript() throws JspException {
         StringBuffer results = new StringBuffer();

         ModuleConfig config = TagUtils.getInstance().getModuleConfig(pageContext);
         ValidatorResources resources =
             (ValidatorResources) pageContext.getAttribute(
                 ValidatorPlugIn.VALIDATOR_KEY + config.getPrefix(),
                 PageContext.APPLICATION_SCOPE);

         if (resources == null) {
             throw new JspException(
                 "ValidatorResources not found in application scope under key \"" 
                 + ValidatorPlugIn.VALIDATOR_KEY + config.getPrefix() + "\"");
         }        

         Locale locale = TagUtils.getInstance().getUserLocale(this.pageContext, null);

         Form form = resources.getForm(locale, formName);

         if ("true".equalsIgnoreCase(dynamicJavascript) && form == null)
         {
             throw new JspException("No form found under '"
                                    + formName
                                    + "' in locale '"
                                    + locale
                                    + "'");
         }

         if (form != null) {
             if ("true".equalsIgnoreCase(dynamicJavascript)) {
                 results.append(
                         this.createDynamicJavascript(config, resources, locale, form));

             } else if ("true".equalsIgnoreCase(staticJavascript)) {
                 results.append(this.renderStartElement());
                 if ("true".equalsIgnoreCase(htmlComment)) {
                     results.append(HTML_BEGIN_COMMENT);
                 }
             }
         }

         if ("true".equalsIgnoreCase(staticJavascript)) {
             results.append(getJavascriptStaticMethods(resources));
         }

         if (form != null
             && ("true".equalsIgnoreCase(dynamicJavascript)
                 || "true".equalsIgnoreCase(staticJavascript))) {

             results.append(getJavascriptEnd());
         }

         return results.toString();
     }

    /**
     * Generates the dynamic JavaScript for the form.
     * @param config
     * @param resources
     * @param locale
     * @param form
     */
    private String createDynamicJavascript(
        ModuleConfig config,
        ValidatorResources resources,
        Locale locale,
        Form form) throws JspException {

        StringBuffer results = new StringBuffer();

        MessageResources messages = 
            TagUtils.getInstance().retrieveMessageResources(pageContext, bundle, true);
    
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        ServletContext application = pageContext.getServletContext();

        List<ValidatorAction> actions = this.createActionList(resources, form);

        final String methods = this.createMethods(actions, this.stopOnError(config));

        results.append(this.getJavascriptBegin(methods));
        
        @SuppressWarnings("unchecked")
        List<Field> fields = form.getFields();
        results.append(this.createValidationResult( fields ));

        for (Iterator<ValidatorAction> i = actions.iterator(); i.hasNext();) {
            ValidatorAction va = i.next();
            int jscriptVar = 0;
            String functionName = null;


            if (va.getJsFunctionName() != null
                && va.getJsFunctionName().length() > 0) {
                functionName = va.getJsFunctionName();
            } else {
                functionName = va.getName();
            }

            
            results.append("    function " + formName + "_" + functionName + " () { \n");
            @SuppressWarnings("unchecked")
            Iterator<Field> x = form.getFields().iterator();
            for (; x.hasNext();) {
                Field field = x.next();

                // Skip indexed fields for now until there is a good way to handle
                // error messages (and the length of the list (could retrieve from scope?))
                if (field.isIndexed()
                    || field.getPage() != page
                    || !field.isDependency(va.getName())) {

                    continue;
                }

                String message =  Resources.getMessage(application,
                                                       request,
                                                       messages,
                                                       locale,
                                                       va,
                                                       field);

                message = (message != null) ? message : "";

                // prefix variable with 'a' to make it a legal identifier
                results.append(
                    "     this.a"
                        + jscriptVar++
                        + " = new Array(\""
                        + field.getKey()
                        + "\", \""
                        + escapeQuotes(message)
                        + "\", ");

                results.append("new Function (\"varName\", \"");

                @SuppressWarnings("unchecked")
                Map<String, Var> vars = field.getVars();
                // Loop through the field's variables.
                Iterator<Map.Entry<String, Var>> varsIterator = vars.entrySet().iterator();
                boolean initializedConditionArrays = false;
                while (varsIterator.hasNext()) {
                	Map.Entry<String, Var> mapEntry = varsIterator.next();
                    String varName = mapEntry.getKey();
                    Var var = mapEntry.getValue();
                    String varValue = var.getValue();
                    String jsType = var.getJsType();

                    // skip requiredif variables field, fieldIndexed, fieldTest, fieldValue
                    if (varName.startsWith("field")) {
                        continue;
                    }
                    
                    if(!initializedConditionArrays && varName.startsWith("condition"))
                    {
                        results.append("this.conditionField = new Array(); " +
                            "this.conditionFieldTest = new Array(); " +
                            "this.conditionFieldValue = new Array(); " +
                            "this.conditionJoin = new Array(); ");
                        initializedConditionArrays = true;
                    }
                    
                    String varValueEscaped = escapeJavascript(varValue);

                    if (Var.JSTYPE_INT.equalsIgnoreCase(jsType)) {
                        results.append(
                            "this."
                                + varName
                                + "="
                                + varValueEscaped
                                + "; ");
                    } else if (Var.JSTYPE_REGEXP.equalsIgnoreCase(jsType)) {
                        results.append(
                            "this."
                                + varName
                                + "=/"
                                + varValueEscaped
                                + "/; ");
                    } else if (Var.JSTYPE_STRING.equalsIgnoreCase(jsType)) {
                        results.append(
                            "this."
                                + varName
                                + "='"
                                + varValueEscaped
                                + "'; ");
                        // So everyone using the latest format doesn't need to change their xml files immediately.
                    } else if ("mask".equalsIgnoreCase(varName)) {
                        results.append(
                            "this."
                                + varName
                                + "=/"
                                + varValueEscaped
                                + "/; ");
                    } else {
                        results.append(
                            "this."
                                + varName
                                + "='"
                                + varValueEscaped
                                + "'; ");
                    }
                }

                results.append(" return this[varName];\"));\n");
            }
            results.append("    } \n\n");
        }

        return results.toString();
    }

     private String escapeQuotes(String in)
     {
         in = escapeSingleQuotes( in );
         
         if (in == null || in.indexOf("\"") == -1)
         {
             return in;
         }
         StringBuffer buffer = new StringBuffer();
         StringTokenizer tokenizer = new StringTokenizer(in, "\"", true);

         while (tokenizer.hasMoreTokens())
         {
             String token = tokenizer.nextToken();
             if (token.equals("\""))
             {
                 buffer.append("\\");
             }
             buffer.append(token);
         }

         return buffer.toString();
     }
     
     private String escapeSingleQuotes(String in)
     {
         if(in != null)
            return in.replaceAll( "'", "&#39;" );
         else
            return in;
     }

     /**
      * <p>Backslash-escapes the following characters from the input string:
      * &quot;, &apos;, \, \r, \n.</p>
      *
      * <p>This method escapes characters that will result in an invalid
      * Javascript statement within the validator Javascript.</p>
      *
      * @param str The string to escape.
      * @return The string <code>s</code> with each instance of a double quote,
      *         single quote, backslash, carriage-return, or line feed escaped
      *         with a leading backslash.
      *
      * @since Struts 1.2.8
      */
     private String escapeJavascript(String str)
     {
         if (str == null)
         {
             return null;
         }
         int length = str.length();
         if (length == 0)
         {
             return str;
         }

         // guess at how many chars we'll be adding...
         StringBuffer out = new StringBuffer(length + 4);
         // run through the string escaping sensitive chars
         for (int i=0; i < length; i++)
         {
             char c = str.charAt(i);
             if (c == '"'  ||
                 c == '\'' ||
                 c == '\\' || 
                 c == '\n' || 
                 c == '\r')
             {
                 out.append('\\');
             }
             out.append(c);
         }
         return out.toString();
     }

    /**
     * Determines if validations should stop on an error.
     * @param config The <code>ModuleConfig</code> used to lookup the
     * stopOnError setting.
     * @return <code>true</code> if validations should stop on errors.
     */
    private boolean stopOnError(ModuleConfig config) {
        Object stopOnErrorObj =
            pageContext.getAttribute(
                ValidatorPlugIn.STOP_ON_ERROR_KEY + '.' + config.getPrefix(),
                PageContext.APPLICATION_SCOPE);

        boolean stopOnError = true;

        if (stopOnErrorObj instanceof Boolean) {
            stopOnError = ((Boolean) stopOnErrorObj).booleanValue();
        }

        return stopOnError;
    }

     /**
      * Creates the JavaScript methods list from the given actions.
      * @param actions A List of ValidatorAction objects.
      * @param stopOnError If true, behaves like released version of struts 1.1
      *        and stops after first error. If false, evaluates all validations.
      * @return JavaScript methods.
      */
     private String createMethods(List<ValidatorAction> actions, boolean stopOnError) {
         StringBuffer methods = new StringBuffer();
         final String methodOperator = stopOnError ? " && " : " & ";

         Iterator<ValidatorAction> iter = actions.iterator();
         while (iter.hasNext()) {
             ValidatorAction va = iter.next();

             if (methods.length() > 0) {
                 methods.append(methodOperator);
             }
             methods.append(va.getMethod())
                    .append("(form, '" + formName + "')");
         }

         return methods.toString();
     }

     /**
      * Get List of actions for the given Form.
      * @param resources
      * @param form
      * @return A sorted List of ValidatorAction objects.
      */
	private List<ValidatorAction> createActionList(ValidatorResources resources, Form form) {

         List<String> actionMethods = new ArrayList<String>();

         @SuppressWarnings("unchecked")
         Iterator<Field> iterator = form.getFields().iterator();
         while (iterator.hasNext()) {
             Field field = iterator.next();

             @SuppressWarnings("unchecked")
             Iterator<String> x = field.getDependencyList().iterator();
             for (; x.hasNext();) {
                 String o = x.next();

                 if (o != null && !actionMethods.contains(o)) {
                     actionMethods.add(o);
                 }
             }
         }

         List<ValidatorAction> actions = new ArrayList<ValidatorAction>();

         // Create list of ValidatorActions based on actionMethods
         Iterator<String> iterator2 = actionMethods.iterator();
         while (iterator2.hasNext()) {
             String depends = iterator2.next();
             ValidatorAction va = resources.getValidatorAction(depends);

             // throw nicer NPE for easier debugging
             if (va == null) {
                 throw new NullPointerException(
                     "Depends string \""
                         + depends
                         + "\" was not found in validator-rules.xml.");
             }

             if (va.getJavascript() != null && va.getJavascript().length() > 0) {
                 actions.add(va);
             } else {
                 iterator2.remove();
             }
         }

         Collections.sort(actions, actionComparator);

         return actions;
     }
     
    /**
     * Returns the opening script element and some initial javascript.
     */
	@Override
    protected String getJavascriptBegin(String methods) {
        StringBuffer sb = new StringBuffer();
        
        String name = formName.substring(0, 1).toUpperCase() + formName.substring(1, formName.length());

        sb.append(this.renderStartElement());

        if (this.isXhtml() && "true".equalsIgnoreCase(this.cdata)) {
            sb.append("//<![CDATA[\r\n");
        }

        if (!this.isXhtml() && "true".equals(htmlComment)) {
            sb.append(HTML_BEGIN_COMMENT);
        }
        sb.append("\n    var bCancel = false; \n\n");
        
        sb.append("    var "+ formName + "_ValidationResult; \n\n");

        sb.append("    function validate" + name + "(form) { \n");
      
        sb.append("        if (bCancel) { \n");
        sb.append("            return new Array(); \n");
        sb.append("        } else { \n");

        // Always return true if there aren't any Javascript validation methods
        if ((methods == null) || (methods.length() == 0)) {
            sb.append("            return new Array(); \n");
        } else {
            sb.append("            " + formName + "_ValidationResult = new " + formName + "_ValidationResultObj();\n");
            sb.append("            " + methods + "; \n");
            sb.append("            return ( " + formName + "_ValidationResult.getAllErrorMessages() );\n");
        }
        sb.append("        } \n");
        sb.append("    } \n\n");

        return sb.toString();
    }
    
   
    protected String createValidationResult( List<Field> fields )
    {
        StringBuffer sb = new StringBuffer();

        sb.append("    function " + formName + "_ValidationResultObj () { \n");
        
        sb.append("        this.fieldNames = [" + this.getFieldNameArrayValues( fields ) + "]; \n");
        sb.append("        this.fieldsValid = {}; \n");
        sb.append("        this.fieldsMessages = {}; \n");
        sb.append("        this.addValidationResult = function (fieldName, isValid, errorMessage) {\n" + 
        "                this.fieldsValid[fieldName] = isValid;\n" + 
        "                if(errorMessage.length > 0) this.fieldsMessages[fieldName] = errorMessage;\n" + 
        "        }\n");
        sb.append("        this.getErrorMessage = function(fieldName){\n" + 
        "                if(this.fieldsMessages[fieldName] == undefined || this.fieldsMessages[fieldName] == null)\n" + 
        "                        return \"\";\n" + 
        "                else return this.fieldsMessages[fieldName];\n" + 
        "        }\n");
        sb.append("        this.getIsValid = function(fieldName){\n" + 
        "                if(this.fieldsValid[fieldName] == undefined || this.fieldsValid[fieldName] == null)\n" + 
        "                        return true;\n" + 
        "                else return this.fieldsValid[fieldName];\n" + 
        "        }\n");
        sb.append("        this.getAllErrorMessages = function ()\n" + 
        "        {\n" + 
        "                var allMessages = new Array();\n" + 
        "                for( var fieldNameIndex in this.fieldNames)\n" + 
        "                {\n" + 
        "                        var fieldName = this.fieldNames[fieldNameIndex];\n" +
        "                        if(this.fieldsMessages[fieldName] != undefined && this.fieldsMessages[fieldName] != null && this.fieldsMessages[fieldName].length > 0)\n" + 
        "                                allMessages.push( this.fieldsMessages[fieldName] );\n" + 
        "                }\n" + 
        "                return allMessages;\n" + 
        "        }\n");
       
        sb.append("    } \n\n");

        return sb.toString();
    }
    
    /** gets the values of the array of the field names to be validated **/
    private String getFieldNameArrayValues( List<Field> fields )
    {
        StringBuffer fieldNames = new StringBuffer();
        
        Iterator<Field> fieldsIterator = fields.iterator();
        
        while( fieldsIterator.hasNext() )
        {
            Field field = fieldsIterator.next();
            fieldNames.append( "'" + field.getKey() + "'");
            
            if( fieldsIterator.hasNext() )
                fieldNames.append( "," );
        }
        
        return fieldNames.toString();
    }
    
    /**
     * Returns true if this is an xhtml page.
     */
    private boolean isXhtml() {
        return TagUtils.getInstance().isXhtml(this.pageContext);
    }
    

}
